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
import plaid.runtime.PlaidLocalScope;
import plaid.runtime.PlaidScope;
import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.PlaidScope;
import plaid.runtime.Util;
import plaid.runtime.annotations.RepresentsState;
import plaid.runtime.utils.Delegate;
import plaid.runtime.utils.Import;

@RepresentsState(name="Integer", toplevel=true, javaobject=true)
public class Integer$plaid {
	public static PlaidScope globalScope = PlaidRuntime.getRuntime().getClassLoader().globalScope("plaid.lang", new ArrayList<Import>());

	@RepresentsState(name="Integer") 
	public static PlaidObject foo = Util.newState();
	
	static {
		
		foo.addMember("plus$plaid", Util.protoMethod("plaid.lang.Integer.plus$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() +
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		})/*, true*/);
		
		foo.addMember("sub$plaid", Util.protoMethod("plaid.lang.Integer.sub$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() -
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		})/*, true*/);
		
		foo.addMember("mult$plaid", Util.protoMethod("plaid.lang.Integer.mult$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() *
				((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		})/*, true*/);
		
		foo.addMember("div$plaid", Util.protoMethod("plaid.lang.Integer.div$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() /
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		})/*, true*/);
		
		foo.addMember("mod$plaid", Util.protoMethod("plaid.lang.Integer.mod$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				int x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() %
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				return Util.integer(x);
			}
		})/*, true*/);
		
		foo.addMember("eqeq$plaid", Util.protoMethod("plaid.lang.Integer.eqeq$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() ==
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true)
					return Util.trueObject();
				else
					return Util.falseObject();
			}
		})/*, true*/);
		
		foo.addMember("lt$plaid", Util.protoMethod("plaid.lang.Integer.lt$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()) <
					((Integer)((PlaidJavaObject)args).getJavaObject());
				if (x == true)
					return Util.trueObject();
				else
					return Util.falseObject();
			}
		})/*, true*/);
		
		foo.addMember("gt$plaid", Util.protoMethod("plaid.lang.Integer.gt$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() >
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true)
					return Util.trueObject();
				else
					return Util.falseObject();
			}
		})/*, true*/);
		foo.addMember("lteq$plaid", Util.protoMethod("plaid.lang.Integer.lteq$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() <=
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true)
					return Util.trueObject();
				else
					return Util.falseObject();
			}
		})/*, true*/);

		foo.addMember("gteq$plaid", Util.protoMethod("plaid.lang.Integer.gteq$plaid", new Delegate() {
			@Override
			public PlaidObject invoke(PlaidObject thisVar, PlaidObject args)  throws PlaidException {
				@SuppressWarnings("unused")
				PlaidScope scope = new PlaidLocalScope(globalScope);
				boolean x = ((Integer)((PlaidJavaObject)thisVar).getJavaObject()).intValue() >=
					((Integer)((PlaidJavaObject)args).getJavaObject()).intValue();
				if (x == true)
					return Util.trueObject();
				else
					return Util.falseObject();
			}
		})/*, true*/);
	
	
	}
	
	//TODO : should this Integer be a matchable tag?
	@plaid.runtime.annotations.RepresentsTag(name = "plaid.lang.Integer")
	public static final plaid.runtime.PlaidTag IntegerTag$plaid;
	static {
		final plaid.runtime.PlaidState vAr100$plaid;
		final plaid.runtime.PlaidObject vAr101$plaid;
		vAr101$plaid = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lookup("Object", globalScope);
		vAr100$plaid = plaid.runtime.Util.toPlaidState(vAr101$plaid);
		IntegerTag$plaid = plaid.runtime.Util.tag("plaid.lang.Integer", vAr100$plaid);
	}
}
