package plaid.compilerjava.AST;

import java.util.Set;

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
	public void codegenExpr(CodeGen out, ID y, IDList localVars, Set<ID> stateVars) {
		// TODO Auto-generated method stub
	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}

	@Override
	public <T >void visitChildren(ASTVisitor<T> visitor) {
		id.accept(visitor);
	}

}
