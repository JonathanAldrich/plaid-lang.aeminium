package plaid.runtime;

import java.util.List;

public interface PlaidTag {
	
	public PlaidState caseOf();
	
	public PlaidTag superTag();
	
	public boolean hasSuperTag();
	
	public boolean matches(String tag);
	
	public String getName();
	
	public String rootState();
	
	public List<String> getHierarchy();
}
