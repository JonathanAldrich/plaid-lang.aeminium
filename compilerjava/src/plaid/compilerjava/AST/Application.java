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

import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;
import plaid.compilerjava.util.IdGen;

public class Application implements Expression {
	private Token token;
	private Expression f;
	private Expression arg;
	
	public Application(Expression function, Expression argument) {
		super();
		this.f = function;
		this.arg = argument;
	}

	public Application() {
	}

	public Application(Token t, Expression function, Expression argument) {
		super();
		this.token = t;
		this.f = function;
		this.arg = argument;
	}

	public Application(Token t) {
		this.token = t;
	}

	
	public Expression getFunction() {
		return f;
	}

	public void setFunction(Expression function) {
		this.f = function;
	}

	public Expression getArg() {
		return arg;
	}

	public void setArgument(Expression argument) {
		this.arg = argument;
	}
	
	public Token getToken() {
		return token;
	}
	
	@Override
	public void codegen(CodeGen out, ID y, IDList localVars) {
		out.setLocation(token);
		ID x = IdGen.getId();
		ID z = IdGen.getId();
		out.declareFinalVar(CodeGen.plaidObjectType, x.getName()); //public PlaidObject x
		out.declareFinalVar(CodeGen.plaidObjectType, z.getName()); //public PlaidObject z
		f.codegen(out, x, localVars);
		arg.codegen(out, z, localVars);
		out.setLocation(token);
		out.assignToCall(y.getName(),x.getName(), z.getName());  // y = Util.call(x,z);
	}

	@Override
	public void visitChildren(ASTVisitor visitor) {
		f.accept(visitor);
		arg.accept(visitor);
	}
	
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitNode(this);
	}

}
