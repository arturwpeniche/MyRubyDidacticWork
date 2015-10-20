

import java.rmi.*;
public class FtpServer extends Thread{
	public FtpServer() {
	     try {
	       Ftp f = new FtpImpl();
	       Naming.rebind("rmi://localhost:4666/FtpService", f);
	       //Naming.rebind("FtpService", f);
	     } catch (Exception e) {
	       System.out.println("Trouble: " + e);
	     }
	   }

	   public static void main(String args[]) {
	     Thread yolo = new Thread(new FtpServer());
	     yolo.run();
	   }
}
