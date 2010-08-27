package typechecker.tests.aeminium;

import plaid.runtime.PlaidObject;
import typechecker.tests.utils.TestUtils;

public class AeminiumUtils {
	public static enum Perm {
		UNIQUE,
		IMMUTABLE
	}
	
	public static final PlaidObject dummyType = TestUtils.type(new PlaidObject[0], new PlaidObject[0]);
	public static final PlaidObject dummyPermType = TestUtils.permtype(TestUtils.unique(), dummyType);
	public static final PlaidObject immutableDummyPermType = TestUtils.permtype(TestUtils.immutable(), dummyType);
	// TODO: Why is this broken?  Doesn't matter right now because we only care about the permission.
	// final PlaidObject intType = TestUtils.getStructuralTypeFromAbbrev("Integer");
	public static final PlaidObject intType = TestUtils.type(new PlaidObject[] { TestUtils.id("Integer") }, new PlaidObject[0]);
	public static final PlaidObject immutableInt = TestUtils.permtype(TestUtils.immutable(), intType);
	public static final PlaidObject uniqueInt = TestUtils.permtype(TestUtils.unique(), intType);
	public static final PlaidObject stringType = TestUtils.type(new PlaidObject[] { TestUtils.id("String") }, new PlaidObject[0]);
	public static final PlaidObject immutableString = TestUtils.permtype(TestUtils.immutable(), stringType);
	
	private static int varCounter = 1;
	
	
	public static PlaidObject makeApplication(String function, String arg, Perm perm) {
		PlaidObject foo = TestUtils.id(function, immutableDummyPermType);
		
		PlaidObject permType;
		if (perm == Perm.UNIQUE)
			permType = uniqueInt;
		else
			permType = immutableInt;
		
		PlaidObject x = TestUtils.id(arg, permType);
		return TestUtils.application(foo, x);
	}
	
	public static PlaidObject makeLet(PlaidObject x, PlaidObject exp, PlaidObject body) {
		return TestUtils.let(x, exp, body);
	}
	
	public static PlaidObject makeLet(PlaidObject exp, PlaidObject body) {
		return makeLet(TestUtils.id("tmp" + varCounter++/* + "$plaid"*/, dummyPermType), exp, body);
	}
	
	public static PlaidObject makeIntLiteral(int num) {
		return TestUtils.intLiteral(num, uniqueInt);
	}
}
