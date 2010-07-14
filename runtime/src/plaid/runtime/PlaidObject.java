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
 
package plaid.runtime;

import java.util.Collection;
import java.util.Map;

/**
 * The PlaidObject interface is n
 */
public interface PlaidObject {
	/**
	 * Adds a member to the Plaid object
	 * @param member - name and the definition location
	 * @param obj - value of the member
	 */
	public void addMember(PlaidMemberDef member, PlaidObject obj);
	
	/**
	 * Update the value of a mutable member (field)
	 * @param name - field name (does not change definition location, only the value of the field)
	 * @param obj - new value for the field
	 */
	public void updateMember(String name, PlaidObject obj);

	/**
	 * Removes a member from the object
	 * @param name - name of the member
	 */
	public void removeMember(String name);
	
	public Map<PlaidMemberDef, PlaidObject> getMembers();
		
	public void addState(PlaidObject state);

	public void removeState(PlaidObject state);
	
	public Collection<PlaidObject> getStates() throws PlaidException;
	
	public void addTag(PlaidTag tag);
	
	public void removeTag(PlaidTag tag);
	
	public boolean matchesTag(String tagName);
	
	public Collection<PlaidTag> getTags() throws PlaidException;
		
	/**
	 * Change the state of the current object to the specified 
	 * @param update
	 * @return 
	 * @throws PlaidException
	 */
	public PlaidObject changeState(PlaidObject update) throws PlaidException;

	public PlaidObject copy();
}

