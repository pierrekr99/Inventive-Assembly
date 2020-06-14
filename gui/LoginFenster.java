package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Datenbank.datenbankVerbindung;
import objekte.Mitarbeiter;

public class LoginFenster extends JFrame {

	private static final long serialVersionUID = 1L;



	static datenbankVerbindung db = main.Main.getdb();
	
	
	
	private JPanel contentPane;
	private JTextField tf_MitarbeiterID;
	private JPasswordField tf_password;
	private Icon icon;
	private Icon bild;
	private static Image image = new ImageIcon("C:\\Users\\Eclipse_treiber_f�r_db\\Logo_IA.png").getImage();
	private static String mitarbeiternummer;
	
	

	public static Image getImage() {
        return image;
    }
	
	public static String getMitarbeiternummer() {
		return mitarbeiternummer;
	}


	public LoginFenster() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setIconImage(image); //Titel- und Taskleiste
		setTitle("Login");

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		tf_MitarbeiterID = new JTextField();
		tf_MitarbeiterID.setText("Mitarbeiter-ID");
		tf_MitarbeiterID.setToolTipText("Hier ihre ID eingeben...");
		tf_MitarbeiterID.setBounds(364, 265, 144, 25);
		panel.add(tf_MitarbeiterID);
		tf_MitarbeiterID.setColumns(10);
		tf_MitarbeiterID.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				tf_MitarbeiterID.setText("");
			}
		});
		

		tf_password = new JPasswordField();
		tf_password.setToolTipText("Hier Passwort eingeben...");
		tf_password.setColumns(10);
		tf_password.setBounds(364, 301, 144, 25);
		panel.add(tf_password);		
		
		JButton bt_Login = new JButton("Login");
		bt_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean loginFehler = false;
				String id = tf_MitarbeiterID.getText();
				mitarbeiternummer = tf_MitarbeiterID.getText();
				String passwort = String.valueOf(tf_password.getPassword());
				
				for (Mitarbeiter mitarbeiter : db.getDisponentListe()) { // Disponentenliste durchlaufen
					if(id.equals(mitarbeiter.getMitarbeiterNummer()) && passwort.equals(mitarbeiter.getPasswort())) { //vergleich der eingegebenen Daten mit den Daten aus der Disponentenliste
						DisponentFenster disponent = new DisponentFenster(); // wenn ein Objekt gefunden wurde, dann Disponentenfenster erzeugen
						disponent.setExtendedState(JFrame.MAXIMIZED_BOTH); //
						disponent.setVisible(true); // Fenster anzeigen
						disponent.setIconImage(image); //Icon in der Taskleiste
						disponent.setTitle("Inventive Assembly"); // Titel setzen
						dispose(); //Login verschwindet
						loginFehler = true; // LoginVariable wird true gesetzt
					}
				}
				
				for(Mitarbeiter mitarbeiter : db.getMonteurListe()) {
					if(id.equals(mitarbeiter.getMitarbeiterNummer()) && passwort.equals(mitarbeiter.getPasswort())) {
						MonteurFenster monteur = new MonteurFenster(); // Monteurfenster erzeugen
						monteur.setExtendedState(JFrame.MAXIMIZED_BOTH);
						monteur.setVisible(true); // Monteurfenster wird angezeigt
						monteur.setIconImage(image);
						monteur.setTitle("Inventive Assembly");
						dispose(); // Login verschwindet
						loginFehler = true;
					}
				}
				if(!loginFehler) { // wenn funktion-Variable false ist erscheint eine Fehlermeldung
					JOptionPane.showMessageDialog(null, "Anmeldedaten �berpr�fen!");
				}
				
			}
		});
		
		tf_MitarbeiterID.addKeyListener(new KeyAdapter() { // LoginButton mit Enter ausl�sen
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){ // LoginButton mit Enter ausl�sen
	               
	            	bt_Login.doClick();
	            	
	            }
	        }

	    });
		
		tf_password.addKeyListener(new KeyAdapter() { 
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	               
	            	bt_Login.doClick();
	            	
	            }
	        }

	    });
		
		
		bt_Login.setFont(new Font("Tahoma", Font.PLAIN, 11));
		bt_Login.setBounds(364, 351, 144, 23);
		panel.add(bt_Login);

		JLabel logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setBounds(267, 31, 317, 78);
		panel.add(logoLabel);
		icon = new ImageIcon("C:\\Users\\Eclipse_treiber_f�r_db\\Login.png"); //Titelbild
		logoLabel.setIcon(icon);
		
		JLabel bildLabel = new JLabel("");
		bildLabel.setBounds(240, 120, 562, 391);
		panel.add(bildLabel);
		bild = new ImageIcon("C:\\Users\\Eclipse_treiber_f�r_db\\Logo.png");
		bildLabel.setIcon(bild);
		
		JLabel loginLabel = new JLabel("");
		loginLabel.setBounds(10, 21, 247, 71);
		panel.add(loginLabel);

	}
}
