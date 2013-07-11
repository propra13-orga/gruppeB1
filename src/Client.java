
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {


	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner eingabe = new Scanner(System.in);
	
		try {
			@SuppressWarnings("resource")
			Socket client = new Socket("localhost", 2367);
			System.out.println("Client gestartet!");
			
			OutputStream out = client.getOutputStream();
			PrintWriter pr = new PrintWriter(out);
			
			InputStream in = client.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			System.out.print("Eingabe: ");
			eingabe.nextLine();
			
			
			pr.write("Hallo Server!\n");
			pr.flush();
			
			String s = null;
			while(s != null) {
				
			System.out.println("Empfangen vom Server: " + s);
			}
			
			br.close();
			pr.close();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
