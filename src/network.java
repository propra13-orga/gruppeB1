
import java.awt.*;
import java.net.*;
import java.io.*;

public class network {
	
	String player,player2,player3,player4,player5,msg;
	Socket socket;
	InputStream in;
	OutputStream out;
	
	

	public void destroy() {
		if (socket != null) {
			try { 
				socket.close();
			} catch (IOException e) {
				
			}
		}
		
	}
	
	private String readSocket() throws IOException {           // Byte Strom von Socket lesen 
		int length;                                            // und in String umwandeln
		byte[] inBuf = new byte[1024];
		StringBuffer stb = new StringBuffer();
		do {
			length = in.read(inBuf);
			if(length<0) {
				//disconnect("Verbindung wurde getrennt.");
				return null;
			}
			stb.append(new String(inBuf,0,length));
		} while(in.available()>0) ;
		  return stb.toString() ;
		
	}
	
	private void listen() {
		try {
			ServerSocket server = new ServerSocket();
			socket = server.accept();
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			if((player2 = readSocket())== null) return;
			if((player3 = readSocket())== null) return;
			if((player4 = readSocket())== null) return;
			if((player5 = readSocket())== null) return;		
			
		} catch(Exception e) {
			
		}
		
		
	}
	
	private void connect() {
		try {
		     socket = new Socket();
		     in = socket.getInputStream();
		     out = socket.getOutputStream();
		     
		     if((player2 = readSocket())== null) return;
				if((player3 = readSocket())== null) return;
				if((player4 = readSocket())== null) return;
				if((player5 = readSocket())== null) return;	
		     
		} catch(Exception e) {
			if(socket!=null) {
				try {
				socket.close();
			} catch (IOException ioe) {}
		 return;
			}      
		}
	}
	
	private void message() {
	try{
		out.write(null);
	} catch(IOException ioe){}
	 return;
	}
	
	private void disconnect() {
		try {
			socket.close();
		} catch(Exception e) {}
	
		
	}
	
	
	
	public static void main(String[] args) {
		// Fenster das im spiel geööfnet wird

	}

}
