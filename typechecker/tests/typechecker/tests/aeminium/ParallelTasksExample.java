package typechecker.tests.aeminium;

import static typechecker.tests.aeminium.AeminiumUtils.uniqueDontCare;
import static typechecker.tests.aeminium.AeminiumUtils.makeLet;
import static typechecker.tests.aeminium.AeminiumUtils.uniqueInt;
import static typechecker.tests.aeminium.AeminiumUtils.immutableString;
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

public class ParallelTasksExample {
	@BeforeClass
	public static void beforeClass() {
		// we need to do this so the Runtime doesn't deadlock if there are hooks into it
		// (e.g. in debug mode)
		PlaidRuntime.getRuntime().setRuntimeState(RUNTIME_STATE.RUNNING);
	}
	
	public PlaidObject makeTestMethodDecl() {
		final PlaidObject x = TestUtils.id("x", uniqueInt);
		
		PlaidObject methodBody =
			makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(1)),
				makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(2)),
					makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(3)),
						makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(4)),
							makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(5)),
								makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(6)),
									makeLet(TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(7)),
											TestUtils.application(TestUtils.id("f", immutableDontCare), AeminiumUtils.makeIntLiteral(8)))
			))))));
		
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
	
	public PlaidObject makePrintString() {
		PlaidObject p1 = TestUtils.id("p1", uniqueDontCare);
		PlaidObject p2 = TestUtils.id("p2", uniqueDontCare);
		
		return
			makeLet(p1, TestUtils.dereference(TestUtils.id("System", uniqueDontCare), TestUtils.id("out", uniqueDontCare)),
				makeLet(p2, TestUtils.dereference(p1, TestUtils.id("println", uniqueDontCare)),
						TestUtils.application(p2, TestUtils.id("fArg", immutableString))));
	}
	
	public PlaidObject makeCalledMethodDecl(String name) {
		PlaidObject body = makePrintString();
		PlaidObject methodType = TestUtils.methodType(Util.string(name),
				  									  uniqueDontCare,
				  									  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
				  									  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));

		PlaidObject methodDecl = TestUtils.methodDecl(Util.string(name),
				  									  body,
				  									  TestUtils.id(name + "Arg", uniqueDontCare),
				  									  Util.falseObject(),
				  									  methodType);

		return methodDecl;		
	}
	
	public PlaidObject makeCompilationUnit() {
		List<QualifiedID> qids = new ArrayList<QualifiedID>();
		PlaidObject imports = TestUtils.importList(qids);
		
		List<String> packageName = new ArrayList<String>();
		packageName.add("aeminiumTests");
		packageName.add("parallelTasks");
		
		List<PlaidObject> decls = new ArrayList<PlaidObject>();
		decls.add(makeMainMethodDecl());
		decls.add(makeTestMethodDecl());
		decls.add(makeCalledMethodDecl("f"));
		
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
