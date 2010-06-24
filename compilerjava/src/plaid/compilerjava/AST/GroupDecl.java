package plaid.compilerjava.AST;

import java.io.File;

import plaid.compilerjava.CompilerConfiguration;
import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;
import plaid.compilerjava.util.QualifiedID;

public class GroupDecl implements Decl {
	private Token token;
	private ID dstId;
	private ID srcId;
	
	public GroupDecl(Token token, ID dstId) {
		this.token = token;
		this.dstId = dstId;
		this.srcId = null;
	}
	
	public GroupDecl(Token token, ID dstId, ID srcId) {
		this.token = token;
		this.dstId = dstId;
		this.srcId = srcId;
	}

	@Override
	public File codegen(QualifiedID qid, ImportList imports, CompilerConfiguration cc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void codegen(CodeGen out, ID y, IDList localVars) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// FIXME Do something reasonable
		return "$gr0up$" + dstId.getName();
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitNode(this);
	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public void visitChildren(ASTVisitor visitor) {
		dstId.accept(visitor);
		if (srcId != null)
			srcId.accept(visitor);
	}
}