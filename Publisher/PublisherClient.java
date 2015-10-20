


import java.io.*;
import java.net.*;
import java.util.Random;

public class PublisherClient {
	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println(
					"Usage: java PublisherClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		Socket yoloSocket = new Socket(hostName, portNumber);
		Thread read = new Thread(new Read(yoloSocket));
		Thread write = new Thread(new Write(yoloSocket));
		read.start();
		write.start();
	}
}
class Read implements Runnable {
	Socket clientSokect;
	public Read(Socket client) {
		this.clientSokect = client;
	}

	public void run() {

		try(
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSokect.getInputStream()));) 
				{
			String fromServer;
			while(true) {
				fromServer = in.readLine();
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye."))
					break;
			}
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
}

class Write implements Runnable {
	Socket clientSocket;
	boolean isRunning = true;
	public Write(Socket client) {
		this.clientSocket = client;
	}

	public void run() {

		try(
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) 
				{
			BufferedReader stdIn =
					new BufferedReader(new InputStreamReader(System.in));
			String fromUser;
			GenerateRandom generateRandom = new GenerateRandom(clientSocket);
			Thread random = new Thread(generateRandom);
			while (isRunning) {
				fromUser = stdIn.readLine();
				if (fromUser != null) {
					if(fromUser.equalsIgnoreCase("forward()")) {
						if(!random.isAlive()) {
							random.start();
						}
						else {
							generateRandom.switchRunning();
						}

					}else
						if(fromUser.split(" ")[0].equalsIgnoreCase("unpublish"))
							generateRandom.switchRunning();
					out.println(fromUser);
				}
			}
				} catch (Exception e) {
					e.printStackTrace();
				} 
	}
}

/**
 * class that generates random floats
 * @author up200908399
 *
 */

class GenerateRandom implements Runnable {
	private Socket clientSocket;
	private boolean isRunning = true;

	public GenerateRandom(Socket client) {
		this.clientSocket = client;
	}

	public void run() {
		try(
				PrintWriter cout = new PrintWriter(clientSocket.getOutputStream(), true);) 
				{
			cycle(cout);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}
	/*
	 *sends data to subscribers 
	 */
	private void cycle(PrintWriter cout) {
		Random rand = new Random();
		while (isRunning) {
			cout.println("forward " + rand.nextFloat());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(!isRunning) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cycle(cout);
	}
	/*
	 * method to pause the thread 
	 */
	public void switchRunning() {
		if(isRunning)
			isRunning = false;
		else
			isRunning = true;
	}
}
