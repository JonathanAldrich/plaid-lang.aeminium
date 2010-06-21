package plaid.compilerjava.AST;

import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;
import plaid.compilerjava.util.CodeGen;
import plaid.compilerjava.util.IDList;

public class Share implements Expression {
	private Token token;
	private ID group;
	private Expression stmtList1;
	private Expression stmtList2;
	
	public Share(Token token, ID group, Expression stmtList1, Expression stmtList2) {
		this.token = token;
		this.group = group;
		this.stmtList1 = stmtList1;
		this.stmtList2 = stmtList2;
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
		group.accept(visitor);
		stmtList1.accept(visitor);
		stmtList2.accept(visitor);
	}
}
