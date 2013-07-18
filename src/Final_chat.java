package network;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.*;
import java.net.*;
import java.io.*;
 
class CompositePanel extends Panel { public CompositePanel() {}

public void setEnabled(boolean b) {
	Component [] complist = getComponents();
	for(int i=0; i<complist.length; i++) {
	complist[i].setEnabled(b);
	super.setEnabled(b);
}
}
}

public class Final_chat extends Applet {
	
  Panel t_panel,l_panel;
  TextField user,port,host,message;
  Button b_connect,b_disconnect;
  List reponse;
  String name,other_name,message2;
  Socket socket;
  InputStream in;
  OutputStream out;
  
  public void init() {
	  
	  setLayout(null);
	  setBackground(SystemColor.control);
	  Label label;
	  
	  t_panel = new CompositePanel();
	  t_panel.setBounds(10, 10, 380, 104);
	  t_panel.setLayout(null);
	  
	  l_panel = new CompositePanel();
	  l_panel.setBounds(10, 126, 380, 364);
	  l_panel.setLayout(null);
	  
	  
	  label = new Label("Player");
	  label.setBounds(6, 6, 40, 20);
	  user = new TextField();
	  user.setBounds(46, 6, 200, 20);
	  t_panel.add(label);
	  t_panel.add(user);
	  
	  
	  label = new Label("Port");
	  label.setBounds(6, 30, 40, 20);
	  port = new TextField("2000");
	  port.setBounds(46, 30, 70, 20);
	  t_panel.add(label);
	  t_panel.add(port);
	  
	  
	  label = new Label("Host");
	  label.setBounds(6, 54, 40, 20);
	  host = new TextField();
	  host.setBounds(46, 54, 200, 20);
	  t_panel.add(label);
	  t_panel.add(host);
	  
	  reponse = new List();
	  reponse.setBounds(6, 22, 368, 266);
	  
	  label = new Label("ChatBox");
	  label.setBounds(8, 4, 120, 30);
	  message = new TextField(" ");
	  message.setBounds(10, 311, 370, 30);
	  l_panel.add(label);
	  l_panel.add(message);
	  message.addActionListener(new ActionListener() {
		  public void actionPerfomed(ActionEvent e) {
			  message();
		  }

		@Override
		public void actionPerformed(ActionEvent e) {
			message();
			
		}

	  });
	  
	  
	  
	  
	  
	  
	  b_connect = new Button("Verbinden");
	  b_connect.setBounds(290, 40, 100, 40);
	  b_connect.addActionListener(new ActionListener() {
		  @SuppressWarnings("unused")
		public void actionPerfomed(ActionEvent e) {
			  connect();
		  }

		@Override
		public void actionPerformed(ActionEvent e) {
		   connect();
			
		}
	  });
	  t_panel.add(b_connect);
	  
	  
	  b_disconnect = new Button("Trennen");
	  b_disconnect.setBounds(284, 338, 90, 20);
	  b_disconnect.addActionListener(new ActionListener() {
		  @SuppressWarnings("unused")
		public void actionPerfomed(ActionEvent e) {
			  disconnect();
		  }

		@Override
		public void actionPerformed(ActionEvent e) {
			disconnect();
			
		}
	  });
	  l_panel.add(b_disconnect);
	  
	  add(t_panel);
	  add(l_panel);  
	  
  }
  
   public void paint(Graphics p) {
	   p.setColor(SystemColor.controlLtHighlight);
	   p.drawRect(10, 10, 390, 104);
	   p.setColor(SystemColor.controlShadow);
	   p.drawRect(9, 9, 390, 104);
	   p.setColor(SystemColor.controlLtHighlight);
	   p.drawRect(10, 126, 390, 369);
	   p.setColor(SystemColor.controlShadow);
	   p.drawRect(9, 125, 390, 369);
	   
   }
  
   
   private void connect() {
	   t_panel.setEnabled(false);
	   try {
		   String name = user.getText();
		   ServerSocket servSocket = new ServerSocket(Integer.parseInt(port.getText()));
		   socket = servSocket.accept();
		   in = socket.getInputStream();
		   out = socket.getOutputStream();
		   out.write(name.getBytes());
		   if((other_name = readSocket()) == null)
			   return;
		   if((message2 = readSocket())== null)
			   return;
		   reponse.removeAll();
		   servSocket.close();
		   reponse.add(other_name+" "+message2);  
	   } catch(IOException e) {
		   t_panel.setEnabled(true);
		   return;
	   }
	   message.requestFocus();
	   l_panel.setEnabled(true);
	   
	   try {
		   String name = user.getText();
		   socket = new Socket(InetAddress.getByName(host.getText()),Integer.parseInt(port.getText()));
		   in = socket.getInputStream();
		   out = socket.getOutputStream();
		   if((other_name = readSocket())== null)
			   return;
		   if((message2 = readSocket())== null)
			   return;
		   out.write(name.getBytes());
		   reponse.removeAll();
		   reponse.add(other_name+" "+message2);   
	   } catch(IOException e) {
		   t_panel.setEnabled(true);
		   return;
	   }
	   message.requestFocus();
	   l_panel.setEnabled(true);
   }
   
   
   
   private void disconnect() {
	   l_panel.setEnabled(false);
	   try {
		   socket.close();
	   }catch(IOException e) {}
	   host.requestFocus();
	   t_panel.setEnabled(true);
   }
   
   
   
   private String readSocket() throws IOException {
	   int length;
	   StringBuffer buffer = new StringBuffer();
	   byte[] np = new byte [1024];
	   do {
		   length = in.read(np);
		   if(length<0) {
			   disconnect();
			   return null;
		   } buffer.append(new String(np, 0, length));
	   } while(in.available() >0);
	   return buffer.toString();
		      
	   }
   
   private void message() {
	   message2 = message.getText();
	   l_panel.setEnabled(true);
	   message.setText("");
	   reponse.add(user+" "+message);
	   try {
		   out.write(message2.getBytes());
		   l_panel.setEnabled(false);
		   reponse.add(other_name+" "+message);
		   if((message2 = readSocket()) == null)
		   return;
		   l_panel.setEnabled(true);
	   } catch(IOException e) { disconnect();}
   }

	   
  	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
