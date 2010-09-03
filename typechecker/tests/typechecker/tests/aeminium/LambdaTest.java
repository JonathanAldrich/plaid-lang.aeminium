package typechecker.tests.aeminium;

import static typechecker.tests.aeminium.AeminiumUtils.uniqueDontCare;
import static typechecker.tests.aeminium.AeminiumUtils.makeLet;
import static typechecker.tests.aeminium.AeminiumUtils.uniqueInt;
import static typechecker.tests.aeminium.AeminiumUtils.immutableInt;
import static typechecker.tests.aeminium.AeminiumUtils.immutableDontCare;

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

public class LambdaTest {
	@BeforeClass
	public static void beforeClass() {
		// we need to do this so the Runtime doesn't deadlock if there are hooks into it
		// (e.g. in debug mode)
		PlaidRuntime.getRuntime().setRuntimeState(RUNTIME_STATE.RUNNING);
	}
	
	public PlaidObject makeTestMethodDecl() {
		final PlaidObject x = TestUtils.id("x", uniqueInt);
		final PlaidObject n = TestUtils.id("n", immutableInt);
		
		List<PlaidObject> lamTypes = new ArrayList<PlaidObject>();
		lamTypes.add(immutableInt);
		List<PlaidObject> lamNames = new ArrayList<PlaidObject>();
		lamNames.add(n);
		
		PlaidObject lamMethodType = TestUtils.methodType(Util.string("<LAMBDA>"), uniqueDontCare,
														 TestUtils.convertJavaListToPlaidList(lamTypes),
														 TestUtils.convertJavaListToPlaidList(lamNames));

		PlaidObject p1 = TestUtils.id("p1", uniqueDontCare);
		PlaidObject p2 = TestUtils.id("p2", uniqueDontCare);
		PlaidObject s = TestUtils.id("s", uniqueDontCare);
		
		PlaidObject lambdaBody = 
			makeLet(p1, TestUtils.dereference(TestUtils.id("System", uniqueDontCare), TestUtils.id("out", uniqueDontCare)),
				makeLet(p2, TestUtils.dereference(p1, TestUtils.id("println", uniqueDontCare)),
					makeLet(s, TestUtils.stringLiteral("Inside Lambda"),
							   TestUtils.application(p2, TestUtils.id("s", immutableDontCare)))));
		
		PlaidObject lambda = TestUtils.lambda(n, lambdaBody, lamMethodType);
		
		PlaidObject methodBody = 
			makeLet(TestUtils.id("t0", uniqueDontCare), lambda,
					TestUtils.application(TestUtils.id("t0", uniqueDontCare),
										  TestUtils.intLiteral(42)));
					
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(uniqueInt);
		
		List<PlaidObject> argNames = new ArrayList<PlaidObject>();
		argNames.add(x);

		PlaidObject methodType = TestUtils.methodType(Util.string("compute"),
													  uniqueDontCare,
													  TestUtils.convertJavaListToPlaidList(argTypes),
													  TestUtils.convertJavaListToPlaidList(argNames));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("compute"),
													  methodBody,
													  x,
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeMainMethodDecl() {
		PlaidObject mainBody = TestUtils.application(TestUtils.id("compute", uniqueDontCare), TestUtils.unitLiteral());
		
		PlaidObject methodType = TestUtils.methodType(Util.string("main"),
													  uniqueDontCare,
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("main"),
													  mainBody,
													  TestUtils.id("mainArg", uniqueDontCare),
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeCompilationUnit() {
		List<QualifiedID> qids = new ArrayList<QualifiedID>();
		PlaidObject imports = TestUtils.importList(qids);
		
		List<String> packageName = new ArrayList<String>();
		packageName.add("aeminiumTests");
		packageName.add("lambdaTest");
		
		List<PlaidObject> decls = new ArrayList<PlaidObject>();
		decls.add(makeMainMethodDecl());
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
