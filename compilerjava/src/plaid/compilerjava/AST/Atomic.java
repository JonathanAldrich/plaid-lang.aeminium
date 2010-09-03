package plaid.compilerjava.AST;

import java.util.List;
import java.util.Set;

import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;

public class Atomic implements Expression {
	private Token token;
	private List<ID> groupList;
	private Expression stmtList;
	
	public Atomic(Token token, List<ID> groupList, Expression stmtList) {
		this.token = token;
		this.groupList = groupList;
		this.stmtList = stmtList;
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
	public <T> void visitChildren(ASTVisitor<T> visitor) {
		for (ID id : groupList)
			id.accept(visitor);
		stmtList.accept(visitor);
	}
}
