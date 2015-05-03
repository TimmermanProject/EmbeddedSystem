package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import shared.Frame;
import shared.RoomData;

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
				System.out.println("connected !");
				EthernetFrameHandler handler = new EthernetFrameHandler(server); //dispatch client connection to new Thread & Handle it
				handler.start();
				
				//server.close();
		   	} catch(SocketTimeoutException s){
		   		System.out.println("Socket timed out!");
		   	} 
		    catch(IOException e){
		   		e.printStackTrace();
      		} 
      }
   }
   

   public static void main(String [] args){
	  // int port = Integer.parseInt(args[0]);
	   int port = 8080;
	   try {
		   System.out.println("coucou");
		   Thread t = new EthernetCommServer(port);
		   t.start();
	   } catch(IOException e){
		   e.printStackTrace();
	   }
   	}
   
   
}