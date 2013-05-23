import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class start extends JFrame implements ActionListener{
	
	private JButton schliessen;
	private JButton einstellung;
	private JButton info;
	private JButton ende;
	static int SLEEP_TIME = 10;
	private static start frame = new start("Menue");
	
	public static void main(String[] args) {
		
		//start frame = new start("Menue");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640,480);
		
		frame.setLayout(null);
		frame.setVisible(true);

	}
	
	public start(String title){
		super(title);
		schliessen = new JButton("Spiel starten");
		schliessen.addActionListener(this);
		schliessen.setBounds(120,40,160,40);
		add(schliessen);
		
		einstellung = new JButton("einstellung");
		einstellung.addActionListener(this);
		einstellung.setBounds(120,120,160,40);
		add(einstellung);
		
		info = new JButton("info");
		info.addActionListener(this);
		info.setBounds(120,200,160,40);
		add(info);
		
		ende = new JButton("ende");
		ende.addActionListener(this);
		ende.setBounds(120,280,160,40);
		add(ende);
	}
	

	
	/*public static void fenster(){
		JFrame fenster = new JFrame();
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setSize(650,350);
		fenster.setVisible(true);
		fenster.add(new GameFrame());
	}*/
	
public static void auswahl(){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==schliessen){
				
			//fenster();
			//Main.main(null);
			//direkte verwendung von Main.main() gilt nicht, kommt Map und Rolle vor aber sie koennen nicht bewegen
			Frame main = new Frame();
			main.start();			
			frame.setVisible(false);
			
			//erzeuge ein Class von Frame, die der Funktion von Start der Game dient. Kann man Frame als Bruecke zwischen start_menue und Game
			//der Grund von Erstellung der Thread ist Umgehen der Main().
			
		}
		
		if (e.getSource()==info){
			
			Object[] options = {"OK"};			
			JOptionPane.showOptionDialog(null, "Progammiert von Gruppe B1", "Information", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
		}
		
		if (e.getSource()==einstellung){
			 
			auswahl();
		}
		
		if (e.getSource()==ende){
			
			System.exit(0);
		}
	}
	
	
}


