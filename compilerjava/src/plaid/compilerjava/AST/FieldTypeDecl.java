package plaid.compilerjava.AST;

import plaid.compilerjava.coreparser.Token;
import plaid.compilerjava.tools.ASTVisitor;

public class FieldTypeDecl implements TypeDecl {
	private final PermType permType;
	
	public FieldTypeDecl(PermType permType) {
		if (permType == null)
			throw new RuntimeException("permType cannot be null");
		this.permType = permType;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitNode(this);
	}

	@Override
	public Token getToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void visitChildren(ASTVisitor visitor) {
		this.permType.accept(visitor);
	}

	public Object getPermType() {
		return this.permType;
	}

}
