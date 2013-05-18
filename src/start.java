import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class start extends JFrame implements ActionListener{
	
	private JButton schliessen;
	private JButton einstellung;
	private JButton info;
	private JButton ende;
	
	public static void main(String[] args) {
		
		start frame = new start("Menue");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650,350);
		
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
	
	public static void frame(){
		start frame = new start("Menue");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650,350);
		
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	public static void fenster(){
		JFrame fenster = new JFrame();
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setSize(650,350);
		fenster.setVisible(true);
		fenster.add(new Main());
		//init von Main()//
		
		/*die "GameFrame" ist nur ein Beispiel, muss ins Main verandert werden.
		aber diese Files kann ich nicht in meine Eclipse öffnen und damit kann auch nicht überprufung, deswegen copy ich das Start.class 
		nur einfach von miene Workspace ins Github. in ain paar zeit will ich wieder korrigieren.
		*/
	}
	
public static void auswahl(){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==schliessen){
			
			fenster();
			
		}
		
		if (e.getSource()==info){
			Object[] options = {"OK"};
			
			JOptionPane.showOptionDialog(null, "Progammiert von Hyojin Lee", "Informatino", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
		}
		
		if (e.getSource()==einstellung){
			 auswahl();
		}
		
		if (e.getSource()==ende){
			System.exit(0);
		}
	}
	
	
}


