

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;

public class PublisherServer {

	private static final int MAX_CONNECTIONS = 20;
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java KnockKnockServer <port number>");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		int i=0;

		try {
			while(i++ < MAX_CONNECTIONS) {
				ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				MultiCom mult= new MultiCom(clientSocket);
				Thread t = new Thread(mult);
				t.start();
				serverSocket.close();
			}



		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}


}
 class Stream{
	private String ip,name;
	private ArrayList<Socket> subscrivers;
	private int port;
	public Stream(String n, String i,int p){
		this.setIp(i);
		this.setName(n);
		this.setPort(p);
		this.subscrivers=new ArrayList<Socket>();
	}
	public String removeSubscriver(Socket i) throws Exception{
		if(subscrivers.contains(i)){
			subscrivers.remove(i);
			return "Removed from "+name;
		}
		throw new Exception("Client with ip "+i+" isn't a subscriver");
	}
	public void addSubscriver(Socket i){
		subscrivers.add(i);
	}
	public ArrayList<Socket> getSubscrivers(){
		return subscrivers;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
class MultiCom implements Runnable{
	Socket clientSocket;
	public static HashMap<String,Stream> pubs=new HashMap<String,Stream>();
	public static HashMap<String,String> p=new HashMap<String, String>();
	public MultiCom(Socket cs) {
		this.clientSocket=cs;
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out =
					new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			String inputLine, outputLine;
			outputLine = "welcome!";
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = processInput(inputLine,clientSocket);
				out.println(outputLine);
				if (outputLine.equals("Bye."))
					break;
			}
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static  String processInput(String inputLine,Socket cs) {
		
		String ip=cs.getInetAddress().getHostAddress();
		int port=cs.getPort();
		String[] in=inputLine.split(" ");
		//publish stream
		if(in[0].equals("publish") && in.length>1){
			//must not be taken!
			if(!pubs.containsKey(in[1]) && !p.containsKey(ip+" "+port)){
				pubs.put(in[1],new Stream(in[1],ip,port) );
				p.put(ip+" "+port, in[1]);
				System.out.println("Created "+pubs.get(in[1]).getName());

				return "Sucess!You're a publisher";
			}
			return "Error,you're already a publisher or desired pub already taken!";
			//unpublish stream
		}else if (in[0].equals("unpublish") && in.length>1){
			//must be his stream
			if( pubs.containsKey(in[1])&& ip.equals(pubs.get(in[1]).getIp()) ){
				p.remove(pubs.get(in[1]).getIp()+ " "+ pubs.get(in[1]).getPort());
				pubs.remove(in[1]);
				return "Sucess!, removed from "+ in[1];
			}
			return "Pub doesnt exist or you're not the owner!";
			//lists all the streams
		}else if(in[0].equals("list")){
			System.out.println(pubs.keySet().size());
			String o="";
			for(String x: pubs.keySet())
				o += x + '\n';
			return(o);
			/*forwards to all subscribers*/
		}else if(in[0].equals("forward")){
			String client = ip+" "+port;
			Stream stream = pubs.get(p.get(client));
			if(stream!=null){
				for(Socket s : stream.getSubscrivers()){
					try {
						PrintWriter out =new PrintWriter(s.getOutputStream(), true);
						out.println(in[1]);
	
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				}
				return "Sent for "+stream.getSubscrivers().size()+" subscrivers!";
			}
			//subscribe to given stream
		}else if(in[0].equals("subscribe") && in.length>1){
			//checks if stream exists
			if(pubs.containsKey(in[1])){
				pubs.get(in[1]).addSubscriver(cs);
				return "Success!";
			}else
				return "Error! "+in[1]+" doesnt exist";
			//unsubscribe to given stream
		}else if(in[0].equals("unsubscribe") && in.length>1){
			if(pubs.containsKey(in[1]))
				try {
					return pubs.get(in[1]).removeSubscriver(cs);
					
				} catch (Exception e) {
					return "Error! "+e.getMessage();
				}
		}

		return "someError maybe?";
	}

}