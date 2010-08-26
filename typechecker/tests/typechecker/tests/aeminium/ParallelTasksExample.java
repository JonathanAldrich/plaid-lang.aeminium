package typechecker.tests.aeminium;

import static typechecker.tests.aeminium.AeminiumUtils.dummyPermType;
import static typechecker.tests.aeminium.AeminiumUtils.makeApplication;
import static typechecker.tests.aeminium.AeminiumUtils.makeLet;
import static typechecker.tests.aeminium.AeminiumUtils.uniqueInt;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import plaid.compilerjava.util.QualifiedID;
import plaid.runtime.PlaidObject;
import plaid.runtime.PlaidRuntime;
import plaid.runtime.Util;
import plaid.runtime.PlaidRuntimeState.RUNTIME_STATE;
import typechecker.tests.aeminium.AeminiumUtils.Perm;
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
			makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
				makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
					makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
						makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
							makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
								makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
									makeLet(makeApplication("f", "x", Perm.IMMUTABLE),
											makeApplication("f", "x", Perm.IMMUTABLE))
			))))));
		
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
	
	public PlaidObject makeMainMethodDecl() {
		PlaidObject mainBody = TestUtils.application(TestUtils.id("compute", dummyPermType), TestUtils.unitLiteral());
		
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
	
	public PlaidObject makePrintString(String toPrint) {
		PlaidObject p1 = TestUtils.id("p1", dummyPermType);
		PlaidObject p2 = TestUtils.id("p2", dummyPermType);
		PlaidObject s = TestUtils.id("s", dummyPermType);
		
		return
			makeLet(p1, TestUtils.dereference(TestUtils.id("System", dummyPermType), TestUtils.id("out", dummyPermType)),
				makeLet(p2, TestUtils.dereference(p1, TestUtils.id("print", dummyPermType)),
					makeLet(s, TestUtils.stringLiteral(toPrint),
							   TestUtils.application(p2, s))));
	}
	
	public PlaidObject makeCalledMethodDecl(String name) {
		PlaidObject body = makePrintString("");
		PlaidObject methodType = TestUtils.methodType(Util.string(name),
				  									  dummyPermType,
				  									  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()),
				  									  TestUtils.convertJavaListToPlaidList(new ArrayList<PlaidObject>()));

		PlaidObject methodDecl = TestUtils.methodDecl(Util.string(name),
				  									  body,
				  									  TestUtils.id(name + "Arg", dummyPermType),
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
