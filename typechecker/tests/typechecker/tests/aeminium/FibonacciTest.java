package typechecker.tests.aeminium;

import static typechecker.tests.aeminium.AeminiumUtils.dummyPermType;
import static typechecker.tests.aeminium.AeminiumUtils.immutableDummyPermType;
import static typechecker.tests.aeminium.AeminiumUtils.immutableInt;
import static typechecker.tests.aeminium.AeminiumUtils.makeLet;

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

public class FibonacciTest {
	@BeforeClass
	public static void beforeClass() {
		// we need to do this so the Runtime doesn't deadlock if there are hooks into it
		// (e.g. in debug mode)
		PlaidRuntime.getRuntime().setRuntimeState(RUNTIME_STATE.RUNNING);
	}
	
	
	/*
	 * 	method fibonacci(immutable Integer n) {
	 * 		let t0 = (n.<=) in
	 * 			let t1 = (t0 2) in
	 * 				let t2 = { 1; } in
	 * 					let t3 = { 2; } in
	 * 						let t4 = (ifElse t1) in 
	 * 							let t5 = (t4 t2) in (t5 t3)
	 * 	}
	 */
	public PlaidObject makeFibonacciMethodDecl() {
		final PlaidObject n = TestUtils.id("n", immutableInt);
		final PlaidObject x = TestUtils.id("x", dummyPermType);
		
		List<PlaidObject> lamTypes = new ArrayList<PlaidObject>();
		lamTypes.add(immutableInt);
		List<PlaidObject> lamNames = new ArrayList<PlaidObject>();
		lamNames.add(x);
		
		PlaidObject lamMethodType = TestUtils.methodType(Util.string("<LAMBDA>"), dummyPermType,
														 TestUtils.convertJavaListToPlaidList(lamTypes),
														 TestUtils.convertJavaListToPlaidList(lamNames));

		PlaidObject ifBody = TestUtils.intLiteral(1);
		
		final PlaidObject r0 = TestUtils.id("r0", dummyPermType);
		final PlaidObject r1 = TestUtils.id("r1", dummyPermType);
		PlaidObject elseBody =
			TestUtils.let(r0, TestUtils.dereference(n, TestUtils.id("-", dummyPermType)),
				TestUtils.let(r1, TestUtils.application(r0, TestUtils.intLiteral(1)),
					TestUtils.application(TestUtils.id("fibonacci", dummyPermType), r1)));
		
		
		PlaidObject ifLambda = TestUtils.lambda(x, ifBody, lamMethodType);
		PlaidObject elseLambda = TestUtils.lambda(x, elseBody, lamMethodType);
		
		
		final PlaidObject t0 = TestUtils.id("t0", dummyPermType);
		final PlaidObject t1 = TestUtils.id("t1", dummyPermType);
		final PlaidObject t2 = TestUtils.id("t2", dummyPermType);
		final PlaidObject t3 = TestUtils.id("t3", dummyPermType);
		final PlaidObject t4 = TestUtils.id("t4", dummyPermType);
		final PlaidObject t5 = TestUtils.id("t5", dummyPermType);
		
		PlaidObject methodBody =
			TestUtils.let(t0, TestUtils.dereference(n, TestUtils.id("<=", dummyPermType)),
				TestUtils.let(t1, TestUtils.application(t0, TestUtils.intLiteral(2)),
					TestUtils.let(t2, ifLambda,
						TestUtils.let(t3, elseLambda,
							TestUtils.let(t4, TestUtils.application(TestUtils.id("ifElse", dummyPermType), t1),
								TestUtils.let(t5, TestUtils.application(t4, t2),
												  TestUtils.application(t5, t3)))))));
		
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(immutableInt);
		
		List<PlaidObject> argNames = new ArrayList<PlaidObject>();
		argNames.add(n);

		PlaidObject methodType = TestUtils.methodType(Util.string("fibonacci"),
													  dummyPermType,
													  TestUtils.convertJavaListToPlaidList(argTypes),
													  TestUtils.convertJavaListToPlaidList(argNames));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("fibonacci"),
													  methodBody,
													  n,
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makePrintMethodDecl() {
		PlaidObject fibArg = TestUtils.intLiteral(5);
		
		PlaidObject p1 = TestUtils.id("p1", dummyPermType);
		PlaidObject p2 = TestUtils.id("p2", dummyPermType);
		PlaidObject x = TestUtils.id("x", dummyPermType);		
		PlaidObject printArg = TestUtils.application(TestUtils.id("fibonacci", dummyPermType), fibArg);
		PlaidObject methodBody = makeLet(p1, TestUtils.dereference(TestUtils.id("System", dummyPermType), TestUtils.id("out", dummyPermType)),
				makeLet(p2, TestUtils.dereference(p1, TestUtils.id("println", dummyPermType)),
						makeLet(x, printArg,
								   TestUtils.application(p2, x))));
		
		PlaidObject methodType = TestUtils.methodType(Util.string("printResult"),
													  dummyPermType,
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("printResult"),
													  methodBody,
													  TestUtils.id("printResultArg", dummyPermType),
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeMainMethodDecl() {
		PlaidObject mainBody = TestUtils.application(TestUtils.id("printResult", dummyPermType), TestUtils.unitLiteral());
		
		PlaidObject methodType = TestUtils.methodType(Util.string("main"),
													  dummyPermType,
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("main"),
													  mainBody,
													  TestUtils.id("mainArg", dummyPermType),
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeCompilationUnit() {
		List<QualifiedID> qids = new ArrayList<QualifiedID>();
		PlaidObject imports = TestUtils.importList(qids);
		
		List<String> packageName = new ArrayList<String>();
		packageName.add("aeminiumTests");
		packageName.add("fibonacciTest");
		
		List<PlaidObject> decls = new ArrayList<PlaidObject>();
		decls.add(makeMainMethodDecl());
		decls.add(makePrintMethodDecl());
		decls.add(makeFibonacciMethodDecl());
		
		return TestUtils.compilationUnit(decls, packageName, imports);
	}
	
	@Test
	public void codeGenTest() {
		PlaidObject cu = makeCompilationUnit();

		PlaidObject printer = TestUtils.printVisitor();
		Util.call(Util.lookup("visitCompilationUnit", printer), cu);
		
		PlaidObject aemCodeGen = TestUtils.aeminiumCodeGenVisitor();
		Util.call(Util.lookup("visitCompilationUnit", aemCodeGen), cu);
	}
}
