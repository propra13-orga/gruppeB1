import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class Server {


	
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		ServerSocket server1;
		try {
			 server1 = new ServerSocket(2367);
			System.out.println("Server1 gestartet...");
		
		while(true) {
				
	try {
		
		
		
		Socket client = server1.accept();
		
		executor.execute(new Chat_run(client));
		
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
		}	
		}catch (IOException e1) {
			e1.printStackTrace();
		
	}
}
}
