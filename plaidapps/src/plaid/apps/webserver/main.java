package plaid.apps.webserver;
@plaid.runtime.annotations.RepresentsMethod(name = "main", toplevel = true)
public class main{
	public static final java.util.List<plaid.runtime.utils.Import> vAr739$plaid;
	static {
		vAr739$plaid = new java.util.ArrayList<plaid.runtime.utils.Import>();
		plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr739$plaid",vAr739$plaid);
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.net.ServerSocket"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.lang.System"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.net.Socket"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.net.ServerSocket"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.net.InetAddress"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.io.InputStream"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.io.OutputStream"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("java.lang.System"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("plaid.lang.*"));
		vAr739$plaid.add(new plaid.runtime.utils.Import("plaid.lang.globals.*"));
	}
	public static final plaid.runtime.PlaidScope global$c0pe = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().globalScope("plaid.apps.webserver",vAr739$plaid);
	public static void main(String[] args) {
		plaid.lang.Sys.setArgs(args,0);
		plaid.runtime.PlaidRuntime.getRuntime().init();
		plaid.runtime.PlaidRuntime.getAeminium().init();
		plaid.runtime.PlaidRuntime.getRuntime().enterCall(plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().unit(),"main");
		main_func.invoke(plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().unit());
		plaid.runtime.PlaidRuntime.getRuntime().leaveCall(plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().unit(),"main");
		plaid.runtime.PlaidRuntime.getAeminium().shutdown();
		plaid.runtime.PlaidRuntime.getRuntime().shutdown();
	}
	@plaid.runtime.annotations.RepresentsMethod(name = "main", toplevel = false)
	public static final plaid.runtime.PlaidMethod main_func;
	static {
		final plaid.runtime.PlaidScope local$c0pe = new plaid.runtime.PlaidLocalScope(global$c0pe);
		main_func = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lambda(new plaid.runtime.utils.Lambda () {
			public plaid.runtime.PlaidObject invoke(final plaid.runtime.PlaidObject vAr522$plaid) throws plaid.runtime.PlaidException {
				final plaid.runtime.PlaidScope temp$c0pe = local$c0pe;
				final plaid.runtime.PlaidScope local$c0pe = new plaid.runtime.PlaidLocalScope(temp$c0pe);
				local$c0pe.insert("vAr522$plaid", vAr522$plaid, false);
				plaid.runtime.PlaidObject vAr738$plaid=null;
				{
					final plaid.runtime.PlaidObject httpServer;
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",89,26);
					final plaid.runtime.PlaidState vAr740$plaid;
					final plaid.runtime.PlaidObject vAr741$plaid;
					vAr741$plaid = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lookup("HttpServer", global$c0pe);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr741$plaid",vAr741$plaid);
					vAr740$plaid = plaid.runtime.Util.toPlaidState(vAr741$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr740$plaid",vAr740$plaid);
					httpServer = vAr740$plaid.instantiate();
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("httpServer",httpServer);
					local$c0pe.insert("httpServer", httpServer, false);
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,20);
					final plaid.runtime.PlaidObject vAr742$plaid;
					final plaid.runtime.PlaidObject vAr743$plaid;
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,20);
					final plaid.runtime.PlaidObject vAr744$plaid;
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,9);
					vAr744$plaid = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lookup("httpServer", local$c0pe);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr744$plaid",vAr744$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,9);
					vAr742$plaid = plaid.runtime.PlaidRuntime.getRuntime().getClassLoader().lookup("start", vAr744$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr742$plaid",vAr742$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,26);
					vAr743$plaid = plaid.runtime.Util.integer(8080);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr743$plaid",vAr743$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateLocation("/home/manuel/aeminium-workspace/plaid-lang-aeminium/plaidapps/apps/webserver/HttpServer.plaid",90,20);
					vAr738$plaid = plaid.runtime.Util.call(vAr742$plaid, vAr743$plaid);
					plaid.runtime.PlaidRuntime.getRuntime().updateVar("vAr738$plaid",vAr738$plaid);
				}
				return vAr738$plaid;
			}
		}
		);
	}
}
