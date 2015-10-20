


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
public class FtpImpl extends java.rmi.server.UnicastRemoteObject implements Ftp {
	public static String src;
	protected FtpImpl() throws RemoteException {
		super();
		src=System.getProperty("user.dir"); //our dir
	}
	public String cd(String source,String string) throws RemoteException {
		if(string.equals("."))
			return source;
		else if(string.equals("..")){
			if(source.equals("/")) return source;
			String [] temp = source.split("/");
			String temp1="";
			for(int i=0;i<temp.length-1;i++){
				temp1+=temp[i]+"/";
			}
			return temp1; // returns same string less old current dir
		}else{
			System.out.println(src+source+string);
			File f = new File(src+source+string);
			if(f.exists() && f.isDirectory())
				return source+string+"/";
		}	
		return "Server wasnt able to cd to given path";
	}

	public byte[] ls(String a) throws RemoteException {
		try
	    {
	        String lscmd = "ls "+src+a;
	        Process p=Runtime.getRuntime().exec(lscmd); //creates a process to exec ls
	        p.waitFor();//waits for process to end his job
	        InputStream input =p.getInputStream();
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        byte[] buffer = new byte[8192];

	        for (int length = 0; (length = input.read(buffer)) > 0;) {
	            output.write(buffer, 0, length);
	        }
	        return output.toByteArray();
	    }
	    catch(IOException e1) {
	        System.out.println("Pblm found1.");
	    }
	    catch(InterruptedException e2) {
	        System.out.println("Pblm found2.");
	    }
		return null;

	}

	public byte[] get(String source,String file) throws Exception {
		
		File f= new File(source.substring(1)+file);
		if(f.isDirectory())
			throw new Exception("its a directory");
		if(!f.isFile())
			throw new Exception("it is not a file");
		if(f.exists() && !f.isDirectory() &&f.isFile()){ //if exists, isnt a dir and is a file
			byte[] b= new byte[(int) f.length()];
			ByteArrayOutputStream bis = new ByteArrayOutputStream();
			bis.write(b,0,b.length);
			return bis.toByteArray();
		}
		throw new Exception("File not found or it's a directory");
	}

	
	public void put(String source, byte[] a) throws RemoteException,Exception {
			File tmp=new File(src+source);
			if(tmp.isDirectory())
				throw new Exception(src+source+" is a directory");
			FileOutputStream file;
			try {
				file = new FileOutputStream(src+source);
				BufferedOutputStream bos= new BufferedOutputStream(file);
				bos.write(a,0,a.length); //should be better implemented
				bos.close();
			} catch (FileNotFoundException e) {
				throw new Exception("File not found" );
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
	}

	public String exit() throws RemoteException {
		return "Bye bye!";
	}

}
