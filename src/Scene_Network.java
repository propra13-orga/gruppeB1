
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class Scene_Network extends JFrame {
	
	private JTextField name,host,server,chat_network;
	private JButton connect,disconnect,send;
	private List reponse;
	
	public Scene_Network() {
		//super("Network/Chat Verbindungen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		
		c.add(new JLabel("Name:"));
		name = new JTextField(10);
		name.setInputVerifier(new InputVerifier() {
			public boolean verify(JComponent input) {
				return isNotEmpty((JTextField) input);
			}
		});
		c.add(name);
		
		c.add(new JLabel("Host:"));
		host = new JTextField(10);
		host = new JTextField("2367");
		host.setEditable(false);
		c.add(host);
		
		c.add(new JLabel("Server:"));
		server = new JTextField(10);
		server = new JTextField("Play1");
		server.setEditable(false);
		c.add(server);
		
		c.add(new JLabel("Chat/Network"));
		chat_network.setBounds(10, 126, 380, 364);
		chat_network = new JTextField(60);
		chat_network = new JTextField(null);
		chat_network.add(reponse = new List());
		c.add(chat_network);
		
		connect = new JButton("connect");
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.add(connect);
		
		disconnect = new JButton("disconnect");
		disconnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.add(disconnect);
		
		send = new JButton("send");
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.add(send);	
		
		setSize(300,300);
		setVisible(true);
	}
	
	private boolean isNotEmpty(JTextField f) {
		String s = f.getText().trim();
		if (s.length() == 0)
			return false;
		else
			return true;
	}
	
	public void actionPerfomed(ActionEvent e) {
		// this.connect = hier wird die die verbindung client server erstellt
		// this.disconnect = hier wird die verbindung zerstört
		// this.send = nachricht wird verschickt
	}
	
	

	
	public static void main(String[] args) {
		new Scene_Network();

	}

}
