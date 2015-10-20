

import java.io.File;
import java.io.InputStream;

public interface Ftp
extends java.rmi.Remote { 

public String cd(String z,String a) 
throws java.rmi.RemoteException; 

public byte[] ls(String a) 
throws java.rmi.RemoteException; 

public byte[] get(String z,String a) 
throws java.rmi.RemoteException, Exception; 

public void put(String z,byte[] content) 
throws java.rmi.RemoteException, Exception; 

public String exit() 
throws java.rmi.RemoteException; 
} 