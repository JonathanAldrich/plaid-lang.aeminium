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

package plaid.lang;

import java.util.ArrayList;

import plaid.runtime.PlaidException;
import plaid.runtime.PlaidJavaObject;
import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.PlaidScope;
import plaid.runtime.Util;
import plaid.runtime.annotations.RepresentsState;
import plaid.runtime.utils.Delegate;
import plaid.runtime.utils.Import;

@RepresentsState(name="Integer", toplevel=true, javaobject=true)
public class Integer$plaid {
	public static PlaidScope packageScope = PlaidRuntime.getRuntime().getClassLoader().packageScope("plaid.lang", new ArrayList<Import>());

	@RepresentsState(name="Integer") 
	public static PlaidObject foo = Util.newState();
	
	static {
		
		foo.addMember("plus", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() +
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		}));
		
		foo.addMember("minus", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() -
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		}));
		
		foo.addMember("multiply", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() *
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		}));
		
		foo.addMember("divide", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() /
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		}));
		
		foo.addMember("modulo", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() %
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		}));
		
		foo.addMember("equalTo", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() ==
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true) return Util.trueObject();
				else return Util.falseObject();
			}
		}));
		
		foo.addMember("lessThan", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() <
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true) return Util.trueObject();
				else return Util.falseObject();
			}
		}));
		
		foo.addMember("greaterThan", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() >
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true) return Util.trueObject();
				else return Util.falseObject();
			}
		}));
		foo.addMember("lessThanOrEqualTo", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() <=
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true) return Util.trueObject();
				else return Util.falseObject();
			}
		}));

		foo.addMember("greaterThanOrEqualTo", Util.protoMethod(new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = PlaidRuntime.getRuntime().getClassLoader().lambdaScope(packageScope, thisVar);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() >=
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true) return Util.trueObject();
				else return Util.falseObject();
			}
		}));
	
	
	}
}
