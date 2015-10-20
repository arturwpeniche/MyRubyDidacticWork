
public interface Gestao extends java.rmi.Remote { 

	public String abrirConta(String i,String p,String t,int s) throws java.rmi.RemoteException; 
	public String fecharConta(String i,String p,String t) throws java.rmi.RemoteException; 
	public byte[] listar() throws java.rmi.RemoteException; 
	public String levantar(String i,String p,String t,int s) throws java.rmi.RemoteException; 
	public String depositar(String i,String p,String t,int s) throws java.rmi.RemoteException; 
			
	} 

