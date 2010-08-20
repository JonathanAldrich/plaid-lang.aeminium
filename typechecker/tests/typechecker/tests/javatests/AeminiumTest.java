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
	
	@Test
	public void codeGenTest() {
		PlaidObject letVar = TestUtils.id("var$plaid");
		
		PlaidObject foo = TestUtils.id("foo");
		PlaidObject x = TestUtils.id("x");
		PlaidObject firstApp = TestUtils.application(foo, x);
		
		PlaidObject bar = TestUtils.id("bar");
		PlaidObject secondApp = TestUtils.application(bar, x);
		
		PlaidObject let = TestUtils.let(letVar, firstApp, secondApp);
		
		PlaidObject dummyType = TestUtils.type(new PlaidObject[0], new PlaidObject[0]);
		PlaidObject dummyPermType = TestUtils.permtype(TestUtils.unique(), dummyType);
		
		PlaidObject intType = TestUtils.getStructuralTypeFromAbbrev("Integer");
		PlaidObject immInt = TestUtils.permtype(TestUtils.immutable(), intType);
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(immInt);
		
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
