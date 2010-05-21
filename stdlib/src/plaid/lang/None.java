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
 *  but WITHOUT ANY WARRANTY;
 without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License 
 *  along with Plaid Programming Language.  If not, see <http://www.gnu.org/licenses/>.
 */

package plaid.lang;
@plaid.runtime.annotations.RepresentsState(name = "None", toplevel = true)
public class None{
	public static final java.util.List<plaid.runtime.utils.Import> vAr352$plaid;
	static {
		vAr352$plaid = new java.util.ArrayList<plaid.runtime.utils.Import>();
	}
	public static final plaid.runtime.PlaidScope current$c0pe = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().packageScope("plaid.lang",vAr352$plaid);
	@plaid.runtime.annotations.RepresentsState(name = "None", toplevel = false)
	public static final plaid.runtime.PlaidObject None;
	static {
		final plaid.runtime.PlaidState vAr353$plaid;
		vAr353$plaid = plaid.runtime.Util.newState();
		None = vAr353$plaid.getPrototype();
	}
	@plaid.runtime.annotations.RepresentsTag(name = "plaid.lang.None")
	public static final plaid.runtime.PlaidTag None$Tag$plaid;
	static {
		final plaid.runtime.PlaidState vAr354$plaid;
		final plaid.runtime.PlaidObject vAr355$plaid;
		vAr355$plaid = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lookup("Option", current$c0pe);
		vAr354$plaid = plaid.runtime.Util.toPlaidState(vAr355$plaid);
		None$Tag$plaid = plaid.runtime.Util.tag("plaid.lang.None", vAr354$plaid);
	}
}
