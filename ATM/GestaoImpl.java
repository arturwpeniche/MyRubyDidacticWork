import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;


public class GestaoImpl extends UnicastRemoteObject implements Gestao {
	private final String err1 = "Conta ja existente!";
	private final String err2 = "Conta nao existente!";
	private final String err3 = "Pin Errado!";
	private final String err4 = "Nao pussui saldo suficiente";
	private final String err5 = "Faltam parametro";
	
	private final String success1= "Conta criada com sucesso: ";
	private final String success2= "Conta apagado com sucesso";
	private final String success3= "Possui na conta ";
	public static HashMap<String,String[]> contas;
	public static int id;
	protected GestaoImpl() throws RemoteException {
		super();
		contas=new HashMap<String,String[]>();
		id=1;
	}
	@Override
	public String abrirConta(String i,String pin, String titular, int saldo) throws RemoteException {
		if(pin.equals("") || pin.equals(""))
			return err5;
		//inicialmente id=0; 0 nao está definifido na hash, logo é assumido como conta nova
		if(contas.containsKey(i)){
			return err1;
		}
		String aux[]=new String[3];
		aux[0]=pin;
		aux[1]=titular;
		aux[2]=saldo+"";
		String newId=idToString(id);
		contas.put(newId,aux);
		id++;
		return success1+newId;
		 
		
		
	}
	/**
	 * 1 passa a 0001 p.e.
	 * @param id2
	 * @return
	 */
	private String idToString(int id2) {
		String aux=id2+"";
		while(aux.length()<4)
			aux='0'+aux;
		return aux;
	}
	@Override
	public String fecharConta(String id,String pin, String titular) throws RemoteException {
		if(pin.equals("") || pin.equals(""))
			return err5;
		if(!contas.containsKey(id))
			return err2;
		
		String[] cliente=contas.get(id);
		if(pin.equals(cliente[0]))
			contas.remove(id);
		else
			return err3;
		return success2;
	}
	@Override
	public byte[] listar() throws RemoteException {
		
        ByteArrayOutputStream output = new ByteArrayOutputStream(); //enviado como array de bytes
        

		Set<String> cursor=contas.keySet();
		for(String key: cursor){
			String info[]=contas.get(key);
			
			String x="ID:"+key+"INFO: "+info[0]+" , "+info[1]+" , "+info[2]+'\n';
			
			try {
				output.write(x.getBytes("UTF-8"),0,x.getBytes("UTF-8").length);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output.toByteArray();
	}
	@Override
	public String levantar(String id,String pin, String titular, int saldo) throws RemoteException {
		if(pin.equals("") || pin.equals("") || saldo==0)
			return err5;
		if(!contas.containsKey(id))
			return err2;
		String[] cliente=contas.get(id);
		System.out.println(cliente[0]+" "+cliente[1]+" "+cliente[2]);
		if(pin.equals(cliente[0])){
			if(Integer.parseInt(contas.get(id)[2])<saldo)
				return err4;
			contas.get(id)[2]=(Integer.parseInt(contas.get(id)[2])-saldo)+"";
		}else
			return err3;
		return success3+contas.get(id)[2]+" euros";
		
	}
	@Override
	public String depositar(String id,String pin, String titular, int saldo) throws RemoteException {
		if(pin.equals("") || pin.equals("") || saldo==0)
			return err5;
		if(!contas.containsKey(id))
			return err2;
		String[] cliente=contas.get(id);
		if(pin.equals(cliente[0]))
			contas.get(id)[2]=(Integer.parseInt(contas.get(id)[2])+saldo)+"";
		else
			return err3;
		return success3+contas.get(id)[2]+" euros";
	}
	
}
