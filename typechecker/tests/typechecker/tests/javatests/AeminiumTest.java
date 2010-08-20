package typechecker.tests.javatests;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.Util;
import plaid.runtime.PlaidRuntimeState.RUNTIME_STATE;
import plaid.runtime.models.map.PlaidJavaObjectMap;
import typechecker.tests.utils.TestUtils;

public class AeminiumTest {
	@BeforeClass
	public static void beforeClass() {
		// we need to do this so the Runtime doesn't deadlock if there are hooks into it
		// (e.g. in debug mode)
		PlaidRuntime.getRuntime().setRuntimeState(RUNTIME_STATE.RUNNING);
	}
	
	private enum Perm {
		UNIQUE,
		IMMUTABLE
	}
	
	final PlaidObject dummyType = TestUtils.type(new PlaidObject[0], new PlaidObject[0]);
	final PlaidObject dummyPermType = TestUtils.permtype(TestUtils.unique(), dummyType);
	// TODO: Why is this broken?
	// final PlaidObject intType = TestUtils.getStructuralTypeFromAbbrev("Integer");
	final PlaidObject intType = TestUtils.type(new PlaidObject[] { TestUtils.id("Integer") }, new PlaidObject[0]);
	final PlaidObject immutableInt = TestUtils.permtype(TestUtils.immutable(), intType);
	final PlaidObject uniqueInt = TestUtils.permtype(TestUtils.unique(), intType);
	
	
	
	private PlaidObject makeApplication(String function, String arg, Perm perm) {
		PlaidObject foo = TestUtils.id(function, dummyPermType);
		
		PlaidObject permType;
		if (perm == Perm.UNIQUE)
			permType = uniqueInt;
		else
			permType = immutableInt;
		
		PlaidObject x = TestUtils.id(arg, permType);
		return TestUtils.application(foo, x);
	}
	
	@Test
	public void codeGenTest() {
		final PlaidObject x = TestUtils.id("x", immutableInt);
		PlaidObject letVar = TestUtils.id("var$plaid", dummyPermType);
		
		PlaidObject firstApp = makeApplication("foo", "x", Perm.IMMUTABLE);
		PlaidObject secondApp = makeApplication("bar", "x", Perm.IMMUTABLE);
		
		PlaidObject let = TestUtils.let(letVar, firstApp, secondApp);
		
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(immutableInt);
		
		List<PlaidObject> argNames = new ArrayList<PlaidObject>();
		argNames.add(x);

		PlaidObject methodType = TestUtils.methodType(Util.string("compute"),
													  dummyPermType,
													  TestUtils.convertJavaListToPlaidList(argTypes),
													  TestUtils.convertJavaListToPlaidList(argNames));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("compute"),
													  let,
													  x,
													  Util.falseObject(),
													  methodType);

		PlaidObject printer = TestUtils.printVisitor();
		Util.call(Util.lookup("visitMethodDecl", printer), methodDecl);
	}
}
