/** 
 * @author Thomas Verbeke
 * SERVER CLASS
 * 
 * TODO: This class will only serve a point to point connection. In our complete scenario this needs to be a single to many connection
 * http://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
 * 
 * **/

package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import communication.EthernetMessageHandler;

public class EthernetCommServer extends Thread {
	private ServerSocket serverSocket;
	   
   public EthernetCommServer(int port) throws IOException{
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(10000);
   }

   public void run(){
	   while(true){
		   try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				
				EthernetMessageHandler handler = new EthernetMessageHandler(server); //dispatch client connection to new Thread & Handle it
				handler.start();
		
				//server.close();
		   	} catch(SocketTimeoutException s){
		   		System.out.println("Socket timed out!");
		   		break;
		   	} catch(IOException e){
		   		e.printStackTrace();
		   		break;
         }
      }
   }
   
   public static void main(String [] args){
	   int port = Integer.parseInt(args[0]);
	   try {
		   Thread t = new EthernetCommServer(port);
		   t.start();
	   } catch(IOException e){
		   e.printStackTrace();
	   }
   	}
   
   
}
