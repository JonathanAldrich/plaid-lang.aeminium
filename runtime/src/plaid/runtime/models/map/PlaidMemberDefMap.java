package plaid.runtime.models.map;

import plaid.runtime.PlaidMemberDef;
import plaid.runtime.PlaidRuntimeException;

public class PlaidMemberDefMap implements PlaidMemberDef {

	private String memberName;
	private String definedIn;
	private boolean anonymous;
	private boolean mutable;
	private boolean overrides;
	private boolean overrideIsBound = false;
	private String overridenDef;
	
	public static final PlaidMemberDefMap anonymousMember(String memberName, boolean mutable, boolean overrides) {
		return new PlaidMemberDefMap(memberName, true, null, mutable, overrides);
	}
	
	public PlaidMemberDefMap(String memberName, boolean anonymous, String definedIn, boolean mutable, boolean overrides) {
		this.memberName = memberName;
		this.definedIn = definedIn;
		this.mutable = mutable;
		this.anonymous = anonymous;
		this.overrides = overrides;
	}
	
	public PlaidMemberDefMap(String memberName, String definedIn) {
		this(memberName, false, definedIn, false, false);
	}
	
	public PlaidMemberDefMap(String memberName) {
		this(memberName, true, null, false, false);
	}
	
	public PlaidMemberDefMap(String memberName, boolean mutable) {
		this(memberName, true, null, mutable, false);
	}

	@Override
	public String getMemberName() {
		return memberName;
	}

	@Override
	public String definedIn() {
		return definedIn;
	}

	@Override
	public boolean isAnonymous() {
		return anonymous;
	}
	
	@Override
	public boolean isMutable() {
		return mutable;
	}
	
	@Override
	public String overridenDef() {
		return overridenDef;
	}

	@Override
	public boolean overrides() {
		return overrides;
	}
	
	@Override
	public boolean overrideIsBound() {
		return overrideIsBound;
	}
	
	@Override
	public void bindOverride(String overridenDef) throws PlaidRuntimeException {
		if (!overrideIsBound) {
			this.overridenDef = overridenDef;
			overrideIsBound = true;
		}
		else throw new PlaidRuntimeException("Cannot re-bind an overriden member");
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(memberName);
		
		if (mutable) 
			ret.append("(mutable");
		else
			ret.append("(immutable");
		
		if (anonymous)
			ret.append(",anonymous");
		else
			ret.append("," + definedIn.toString());
		
		if (overrides) {
			ret.append(", overrides " + (overrideIsBound ? overridenDef : "?") );
		}
		
		ret.append(")");
		return ret.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (this == other)
			return true;
		if (!(other instanceof PlaidMemberDefMap))
			return false;
		
		PlaidMemberDefMap o = (PlaidMemberDefMap) other;
		return this.toString().equals(o.toString());
	}
}
