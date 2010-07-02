/**
 * Copyright (c) 2010 The Plaid Group (see AUTHORS file)
 * 
 * This file is part of Plaid Programming Language.
 *
 * Plaid Programming Language is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  Plaid Programming Language is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Plaid Programming Language.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package plaid.compilerjava.AST;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import plaid.compilerjava.CompilerConfiguration;
import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.FileGen;
import plaid.compilerjava.util.IDList;
import plaid.compilerjava.util.IdGen;
import plaid.compilerjava.util.QualifiedID;
import plaid.runtime.PlaidConstants;
import plaid.runtime.Util;
import plaid.runtime.annotations.RepresentsState;

public class StateDecl implements Decl {

	private Token token;
	private ID name;
	private State stateDef;
	private QI caseOf;
	private boolean isCaseOf;
	
	public StateDecl(Token t, ID name, State stateDef) {
		super();
		this.token = t;
		this.setName(name);
		this.setStateDef(stateDef);
		isCaseOf = false;
	}
	
	public StateDecl(Token t, ID name, State stateDef, QI caseOf) {
		super();
		this.token = t;
		this.setName(name);
		this.setStateDef(stateDef);
		this.caseOf = caseOf;
		isCaseOf = true;
	}
	
	public StateDecl(Token t) {
		this.token = t;
	}

	public ID getID() {
		return name;
	}
	
	public String getName() {
		return this.name.getName();
	}

	public void setName(ID name) {
		if (Util.isKeyword(name.getName()))
			name = new ID(name.getName() + PlaidConstants.ID_SUFFIX);
		this.name = name;
	}

	public State getStateDef() {
		return stateDef;
	}

	public void setStateDef(State r) {
		this.stateDef = r;
	}

	public Token getToken() {
		return token;
	}
	
	@Override

	public File codegenTopDecl(QualifiedID qid, ImportList imports, CompilerConfiguration cc, Set<ID> globalVars) {

		CodeGen out = new CodeGen(cc);	
		ID freshImports = IdGen.getId();
		ID theState = IdGen.getId();
		
		//package and needed imports
		out.declarePackage(qid.toString()); //package qid;
		
		//determine what members this state has and add annotations
		Set<ID> stateVars = genStateVars(imports.getImports());
		
		StringBuilder members = new StringBuilder();
		for (ID member : stateVars) members.append(member.getName() + ",");
		String memberString = members.toString();
		if (memberString.length() > 0) memberString = memberString.substring(0,memberString.length()-1);
		
		// state annotation and class definition
		out.stateAnnotation(name.getName(), true, memberString);
		out.declarePublicClass(name.getName()); out.openBlock();  // public class f {
		
		//generate code to create the package scope with imports
		out.declarePublicStaticFinalVar("java.util.List<plaid.runtime.utils.Import>",freshImports.getName());
		imports.codegen(out, freshImports);
		out.declareGlobalScope(qid.toString(),freshImports.getName());
		
		//Tag
		//Declare variable to hold the tag
		ID tag = new ID(name.getName() + "$Tag" + PlaidConstants.ID_SUFFIX);
		String tagPath = qid.toString() + "." + name.getName();
		out.tagAnnotation(tagPath);
		out.declarePublicStaticFinalVar(CodeGen.plaidTagType, tag.getName());
		

		
		out.openStaticBlock(); //static {
		if (isCaseOf) { //if we have a super tag
			ID caseOfState = IdGen.getId();
			ID caseOfTag = IdGen.getId();
			out.declareFinalVar(CodeGen.plaidStateType, caseOfState.getName());
			caseOf.codegenState(out, caseOfState, new IDList(), stateVars, null);
			out.declareFinalVar(CodeGen.plaidTagType, caseOfTag.getName());
			out.assignToStateTag(caseOfTag.getName(), caseOfState.getName());
			
			out.assignToNewTag(tag.getName(), tagPath,  caseOfTag.getName());  //tag = new PlaidTag(caseOfState)
		} else { //if we don't
			out.assignToNewTag(tag.getName(), tagPath);
		}
		out.closeBlock(); // } (for static block to init tag)

		
		//annotation for the prototype object representing the state
		out.stateAnnotation(name.getName(), false, "");
		out.declarePublicStaticFinalVar(CodeGen.plaidObjectType, name.getName());
		
		out.openStaticBlock(); //static {
		//out.append("final " + CodeGen.plaidScopeType + " local$c0pe = new plaid.runtime.PlaidLocalScope(" + CodeGen.globalScope + ");");
		out.declareLocalScope(CodeGen.globalScope);
		out.declareFinalVar(CodeGen.plaidStateType, theState.getName());

		IDList idList = new IDList(globalVars).add(new ID(CodeGen.thisVar)); // "this" should be visible during field initializations
		
		stateDef.codegenState(out, theState, idList, stateVars, tag);//this is this declaration.  It will not have any members, but at runtime can forward to its enclosing (instantiated) state

		out.assignToPrototype(name.getName(), theState.getName());
		
		out.closeBlock(); // } (for static block to init prototype)

		out.closeBlock(); // } (for class Def)
		
		return FileGen.createOutputFile(name.getName(), cc.getOutputDir(), out.formatFile(), qid);
		
	}

	//TODO : when will this codegen be called?
	@Override
	public void codegenNestedDecl(CodeGen out, ID y, IDList localVars, Set<ID> stateVars, ID tagContext) {

		out.setLocation(token);
		
		ID fresh = IdGen.getId();
		out.stateAnnotation(name.getName(), false, "");
		out.declareFinalVar(CodeGen.plaidObjectType, fresh.getName());
		stateDef.codegenState(out, fresh, localVars, stateVars, tagContext);

		// TODO: State decls are immutable by default
		out.addMember(y.getName(), name.getName(), fresh.getName()); //y.addMember(s,fresh)
	}

	@Override
	public void visitChildren(ASTVisitor visitor) {
		name.accept(visitor);
		stateDef.accept(visitor);
	}
	
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitNode(this);
	}

	@Override
	public void codegenNestedDecl(CodeGen out, ID y, IDList localVars,
			ID tagContext) {
		// TODO Auto-generated method stub
		
	}
	
	private Set<ID> genStateVars(List<QualifiedID> imports) {
		Set<ID> stateVars = new HashSet<ID>();
		Stack<State> workList = new Stack<State>();
		workList.push(stateDef);
		
		
		State s;
		while(!workList.isEmpty()) {
			s = workList.pop();
			if (s instanceof DeclList) {
				List<Decl> decls = ((DeclList) s).getDecls();
				for (Decl decl : decls) {
					//System.out.println("Adding to state vars: " + decl.getName());
					stateVars.add(new ID(decl.getName()));
				}
			} else if (s instanceof With) {
				With w = (With) s;
				workList.push(w.getR1());
				workList.push(w.getR2());
			} else if (s instanceof QI) {
				
				
				loadStateMembers(((QI) s).toString(), imports, stateVars);
			}
		}
		
		return stateVars;
	}
	
	@SuppressWarnings("unchecked")
	private void loadStateMembers(String stateName, List<QualifiedID> imports, Set<ID> stateVars) {
		ClassLoader cl = this.getClass().getClassLoader();
		
		List<String> toLookup = new ArrayList<String>();
		toLookup.add(stateName);
		for (QualifiedID i : imports) {
			String theImport = i.toString();
			if (theImport.endsWith(stateName)) toLookup.add(theImport);
			else if (theImport.endsWith("*")) toLookup.add(theImport.substring(0,theImport.length()-1) + stateName);
		}
		
		Class<Object> obj = null;
		for (String lkup : toLookup) {
		
			String names[] = {lkup + PlaidConstants.ID_SUFFIX, lkup};
			for ( String current : names) {
				try {
					obj = (Class<Object>) cl.loadClass(current);
					for (Annotation a : obj.getAnnotations()) {
						if (a instanceof RepresentsState) {
							String memberString = ((RepresentsState) a).members();
							for (String s : memberString.split(",")) stateVars.add(new ID(s));
						}
					}
					return; //once we've found the class, we're done
				} catch (ClassNotFoundException e) {
					// If there is no classfile then we need to keep searching
				}
			}
		}
		
		
	}
}
