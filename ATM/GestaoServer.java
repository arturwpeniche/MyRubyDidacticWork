import java.rmi.Naming;


public class GestaoServer  extends Thread{
	public GestaoServer() {
	     try {
	       Gestao g = new GestaoImpl();
	       Naming.rebind("rmi://localhost:9013/GestaoService", g);
	       //Naming.rebind("FtpService", f);
	     } catch (Exception e) {
	       System.out.println("Trouble: " + e);
	     }
	   }

	   public static void main(String args[]) {
	     Thread t = new Thread(new GestaoServer());
	     t.run();
	   }
}
