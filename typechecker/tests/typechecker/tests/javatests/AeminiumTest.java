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
	final PlaidObject immutableDummyPermType = TestUtils.permtype(TestUtils.immutable(), dummyType);
	// TODO: Why is this broken?  Doesn't matter right now because we only care about the permission.
	// final PlaidObject intType = TestUtils.getStructuralTypeFromAbbrev("Integer");
	final PlaidObject intType = TestUtils.type(new PlaidObject[] { TestUtils.id("Integer") }, new PlaidObject[0]);
	final PlaidObject immutableInt = TestUtils.permtype(TestUtils.immutable(), intType);
	final PlaidObject uniqueInt = TestUtils.permtype(TestUtils.unique(), intType);
	final PlaidObject stringType = TestUtils.type(new PlaidObject[] { TestUtils.id("String") }, new PlaidObject[0]);
	final PlaidObject immutableString = TestUtils.permtype(TestUtils.immutable(), stringType);
	
	private int varCounter = 1;
	
	
	private PlaidObject makeApplication(String function, String arg, Perm perm) {
		PlaidObject foo = TestUtils.id(function, immutableDummyPermType);
		
		PlaidObject permType;
		if (perm == Perm.UNIQUE)
			permType = uniqueInt;
		else
			permType = immutableInt;
		
		PlaidObject x = TestUtils.id(arg, permType);
		return TestUtils.application(foo, x);
	}
	
	private PlaidObject makeLet(PlaidObject x, PlaidObject exp, PlaidObject body) {
		return TestUtils.let(x, exp, body);
	}
	
	private PlaidObject makeLet(PlaidObject exp, PlaidObject body) {
		return makeLet(TestUtils.id("tmp" + varCounter++/* + "$plaid"*/, dummyPermType), exp, body);
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
				makeLet(p2, TestUtils.dereference(p1, TestUtils.id("println", dummyPermType)),
					makeLet(s, TestUtils.stringLiteral(toPrint),
							   TestUtils.application(p2, s))));
	}
	
	public PlaidObject makeCalledMethodDecl(String name) {
		PlaidObject body = makePrintString("I'm currently inside '" + name + "'!");
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
		packageName.add("simpleExample");
		
		List<PlaidObject> decls = new ArrayList<PlaidObject>();
		decls.add(makeMainMethodDecl());
		decls.add(makeTestMethodDecl());
		decls.add(makeCalledMethodDecl("f"));
		decls.add(makeCalledMethodDecl("g"));
		decls.add(makeCalledMethodDecl("h"));
		
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
