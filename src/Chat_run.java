import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Chat_run implements Runnable {
	
	private Socket client;
	
	public Chat_run(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
		OutputStream out = client.getOutputStream();
		PrintWriter pr = new PrintWriter(out);
		
		InputStream in = client.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String s = null;
		s = br.readLine();
		
		while(s != null) {
			pr.write(s + "\n");
			pr.flush();
		System.out.println("Empfangen vom Client: " + s);
		}
		
		pr.close();
		br.close();
		client.close();
		}catch (Exception e) {	
	}
	}
}
