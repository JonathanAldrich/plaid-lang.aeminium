package typechecker.tests.aeminium;

import static typechecker.tests.aeminium.AeminiumUtils.immutableDontCare;
import static typechecker.tests.aeminium.AeminiumUtils.immutableInt;
import static typechecker.tests.aeminium.AeminiumUtils.uniqueDontCare;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import plaid.compilerjava.util.QualifiedID;
import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.Util;
import typechecker.tests.utils.TestUtils;

public class FibonacciTest {
	private static final int FIBONACCI_ARG = 25;
	
	public static PlaidObject read(String varName) {
		return TestUtils.id(varName, immutableDontCare);
	}
	
	public static PlaidObject write(String varName) {
		return TestUtils.id(varName, uniqueDontCare);
	}
	
	@BeforeClass
	public static void beforeClass() {
		PlaidRuntime.getRuntime().init();
	}
	
	@AfterClass
	public static void afterClass() {
		PlaidRuntime.getRuntime().shutdown();
	}
	
	/*
	 * 	IfLambda = {
	 * 		1;
	 * 	}
	 */
	public PlaidObject makeIfLambda() {
		final PlaidObject x = TestUtils.id("x", immutableDontCare);
		
		List<PlaidObject> lamTypes = new ArrayList<PlaidObject>();
		lamTypes.add(immutableDontCare);
		List<PlaidObject> lamNames = new ArrayList<PlaidObject>();
		lamNames.add(x);
		
		PlaidObject lamMethodType = TestUtils.methodType(Util.string("IfLambda"), uniqueDontCare,
														 TestUtils.convertJavaListToPlaidList(lamTypes),
														 TestUtils.convertJavaListToPlaidList(lamNames));

		PlaidObject ifBody = TestUtils.intLiteral(1);
		
		return TestUtils.lambda(x, ifBody, lamMethodType);
	}
	
	/*
	 * 	ElseLambda = {
	 * 		let r0 = (n.-) in
	 * 			let r1 = (r0 1) in
	 * 				let r2 = (fibonacci r1) in
	 * 					let r3 = (n.-) in
	 * 						let r4 = (r3 2) in
	 * 							let r5 = (fibonacci r4) in
	 * 								let r6 = (r2.+) in (r6 r5)
	 * 	}
	 */
	public PlaidObject makeElseLambda() {
		final PlaidObject x = TestUtils.id("x", immutableDontCare);
		final PlaidObject n = TestUtils.id("n", immutableInt);
		
		List<PlaidObject> lamTypes = new ArrayList<PlaidObject>();
		lamTypes.add(immutableDontCare);
		List<PlaidObject> lamNames = new ArrayList<PlaidObject>();
		lamNames.add(x);
		
		PlaidObject lamMethodType = TestUtils.methodType(Util.string("ElseLambda"), uniqueDontCare,
														 TestUtils.convertJavaListToPlaidList(lamTypes),
														 TestUtils.convertJavaListToPlaidList(lamNames));

		PlaidObject elseBody =
			TestUtils.let(write("r0"), TestUtils.dereference(n, read("-")),
				TestUtils.let(write("r1"), TestUtils.application(read("r0"), TestUtils.intLiteral(1)),
					TestUtils.let(write("r2"), TestUtils.application(read("fibonacci"), read("r1")),
						TestUtils.let(write("r3"), TestUtils.dereference(n, read("-")),
							TestUtils.let(write("r4"), TestUtils.application(read("r3"), TestUtils.intLiteral(2)),
								TestUtils.let(write("r5"), TestUtils.application(read("fibonacci"), read("r4")),
									TestUtils.let(write("r6"), TestUtils.dereference(read("r2"), read("+")),
													  TestUtils.application(read("r6"), read("r5")))))))));
		
		
		return TestUtils.lambda(x, elseBody, lamMethodType);		
	}
	
	/*
	 * 	method fibonacci(immutable Integer n) {
	 * 		let t0 = (n.<=) in
	 * 			let t1 = (t0 2) in
	 * 				let t2 = $IfLambda in
	 * 					let t3 = $ElseLambda in
	 * 						let t4 = (ifElse t1) in 
	 * 							let t5 = (t4 t2) in (t5 t3)
	 * 	}
	 */
	public PlaidObject makeFibonacciMethodDecl() {
		final PlaidObject n = TestUtils.id("n", immutableInt);

		final PlaidObject ifLambda = makeIfLambda();
		final PlaidObject elseLambda = makeElseLambda();
		
		PlaidObject methodBody =
			TestUtils.let(write("t0"), TestUtils.dereference(n, read("<=")),
				TestUtils.let(write("t1"), TestUtils.application(read("t0"), TestUtils.intLiteral(2)),
					TestUtils.let(write("t2"), ifLambda,
						TestUtils.let(write("t3"), elseLambda,
							TestUtils.let(write("t4"), TestUtils.application(read("ifElse"), read("t1")),
								TestUtils.let(write("t5"), TestUtils.application(read("t4"), read("t2")),
												  TestUtils.application(read("t5"), read("t3"))))))));
		
		
		List<PlaidObject> argTypes = new ArrayList<PlaidObject>();
		argTypes.add(immutableInt);
		
		List<PlaidObject> argNames = new ArrayList<PlaidObject>();
		argNames.add(n);

		PlaidObject methodType = TestUtils.methodType(Util.string("fibonacci"),
													  uniqueDontCare,
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
		PlaidObject methodBody =
			TestUtils.let(write("p1"), TestUtils.dereference(read("System"), read("out")),
				TestUtils.let(write("p2"), TestUtils.dereference(read("p1"), read("println")),
					TestUtils.let(write("f"), TestUtils.intLiteral(FIBONACCI_ARG),
						TestUtils.let(write("x"), TestUtils.application(read("fibonacci"), read("f")),
										 TestUtils.application(read("p2"), read("x"))))));
		
		PlaidObject methodType = TestUtils.methodType(Util.string("printResult"),
													  uniqueDontCare,
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
													  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));
		
		PlaidObject methodDecl = TestUtils.methodDecl(Util.string("printResult"),
													  methodBody,
													  TestUtils.id("printResultArg", uniqueDontCare),
													  Util.falseObject(),
													  methodType);
		
		return methodDecl;
	}
	
	public PlaidObject makeMainMethodDecl() {
		PlaidObject mainBody = TestUtils.application(TestUtils.id("printResult", uniqueDontCare), TestUtils.unitLiteral());
		
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
