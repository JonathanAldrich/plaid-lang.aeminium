package plaid.compilerjava.AST;

import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;

public class NewGroup implements Expression {
	private Token token;
	private ID id;
	
	public NewGroup(Token token, ID id) {
		this.token = token;
		this.id = id;
	}

	@Override
	public void codegen(CodeGen out, ID y, IDList localVars) {
		// TODO Auto-generated method stub

	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitNode(this);
	}

	@Override
	public void visitChildren(ASTVisitor visitor) {
		id.accept(visitor);
	}

}
