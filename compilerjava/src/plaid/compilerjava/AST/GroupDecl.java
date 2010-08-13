package plaid.compilerjava.AST;

import java.io.File;
import java.util.Set;

import plaid.compilerjava.CompilerConfiguration;
import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;
import plaid.compilerjava.util.MemberRep;
import plaid.compilerjava.util.PackageRep;
import plaid.compilerjava.util.QualifiedID;

public class GroupDecl implements Decl {
	private Token token;
	private ID dstId;
	private ID srcId;
	private boolean isOwner; 
	
	public GroupDecl(Token token, ID dstId, boolean owner) {
		this.token = token;
		this.dstId = dstId;
		this.srcId = null;
		this.isOwner = owner;
	}
	
	public GroupDecl(Token token, ID dstId, ID srcId, boolean owner) {
		this.token = token;
		this.dstId = dstId;
		this.srcId = srcId;
		this.isOwner = owner;
	}
	
	@Override
	public File codegenTopDecl(QualifiedID qid, ImportList imports,
			CompilerConfiguration cc, Set<ID> globalVars, PackageRep plaidPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberRep generateHeader(PackageRep plaidpath, ImportList imports,
			String inPackage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void codegenNestedDecl(CodeGen out, ID y, IDList localVars,
			Set<ID> stateVars, String stateContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// FIXME Do something reasonable
		return "$gr0up$" + dstId.getName();
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public <T> void visitChildren(ASTVisitor<T> visitor) {
		dstId.accept(visitor);
		if (srcId != null)
			srcId.accept(visitor);
	}
	
	public boolean isOwner() {
		return isOwner;
	}
}