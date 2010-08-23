package typechecker.tests.javatests;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import plaid.compilerjava.util.QualifiedID;
import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.Util;
import plaid.runtime.PlaidRuntimeState.RUNTIME_STATE;
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
	// TODO: Why is this broken?  Doesn't matter right now because we only care about the permission.
	// final PlaidObject intType = TestUtils.getStructuralTypeFromAbbrev("Integer");
	final PlaidObject intType = TestUtils.type(new PlaidObject[] { TestUtils.id("Integer") }, new PlaidObject[0]);
	final PlaidObject immutableInt = TestUtils.permtype(TestUtils.immutable(), intType);
	final PlaidObject uniqueInt = TestUtils.permtype(TestUtils.unique(), intType);
	
	private int varCounter = 1;
	
	
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
	
	private PlaidObject makeLet(PlaidObject exp, PlaidObject body) {
		return TestUtils.let(TestUtils.id("tmp" + varCounter++/* + "$plaid"*/, dummyPermType), exp, body);
	}
	
	public PlaidObject makeTestMethodDecl() {
		final PlaidObject x = TestUtils.id("x", uniqueInt);
		// final PlaidObject y = TestUtils.id("y", uniqueInt);
		
		PlaidObject methodBody =
			makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
				makeLet(makeApplication("g", "x", Perm.IMMUTABLE),
					makeLet(makeApplication("h", "x", Perm.UNIQUE),
						makeLet(makeApplication("g", "x", Perm.IMMUTABLE),
								makeApplication("h", "x", Perm.UNIQUE))
			)));
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(uniqueInt);
		
		List<PlaidObject> argNames = new ArrayList<PlaidObject>();
		argNames.add(x);

		PlaidObject methodType = TestUtils.methodType(Util.string("compute"),
													  dummyPermType,
													  TestUtils.convertJavaListToPlaidList(argTypes),
													  TestUtils.convertJavaListToPlaidList(argNames));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("compute"),
													  methodBody,
													  x,
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeCompilationUnit() {
		List<QualifiedID> qids = new ArrayList<QualifiedID>();
		PlaidObject imports = TestUtils.importList(qids);
		
		List<String> packageName = new ArrayList<String>();
		packageName.add("aeminiumTests");
		packageName.add("simpleExample");
		
		List<PlaidObject> decls = new ArrayList<PlaidObject>();
		decls.add(makeTestMethodDecl());
		
		return TestUtils.compilationUnit(decls, packageName, imports);
	}
	
	@Test
	public void codeGenTest() {
		PlaidObject cu = makeCompilationUnit();

		PlaidObject printer = TestUtils.printVisitor();
		Util.call(Util.lookup("visitCompilationUnit", printer), cu);
		
//		PlaidObject plaidCodeGen = TestUtils.plaidCodeGenVisitor();
//		Util.call(Util.lookup("visitCompilationUnit", plaidCodeGen), cu);
		
		PlaidObject aemCodeGen = TestUtils.aeminiumCodeGenVisitor();
		Util.call(Util.lookup("visitCompilationUnit", aemCodeGen), cu);
	}
}
