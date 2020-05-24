package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Datenbank.datenbankVerbindung;
import test.MonteurAuftraege;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginFenster extends JFrame {

	private JPanel contentPane;
	private JTextField tf_MitarbeiterID;
	private JPasswordField tf_password;
	private Icon icon;
	private datenbankVerbindung verbindung = new datenbankVerbindung();

	public LoginFenster() {

		verbindung.verbinden();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel titelLabel = new JLabel("L O G I N ");
		titelLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 18));
		titelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(titelLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel idLabel = new JLabel("Mitarbeiter-ID:");
		idLabel.setFont(new Font("Verdana", Font.ITALIC, 11));
		idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		idLabel.setBounds(223, 135, 97, 29);
		panel.add(idLabel);

		JLabel passwortLabel = new JLabel("Passwort:");
		passwortLabel.setFont(new Font("Verdana", Font.ITALIC, 11));
		passwortLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwortLabel.setBounds(223, 189, 97, 29);
		panel.add(passwortLabel);

		tf_MitarbeiterID = new JTextField();
		tf_MitarbeiterID.setToolTipText("Hier ihre ID eingeben...");
		tf_MitarbeiterID.setBounds(330, 138, 144, 25);
		panel.add(tf_MitarbeiterID);
		tf_MitarbeiterID.setColumns(10);

		tf_password = new JPasswordField();
		tf_password.setToolTipText("Hier Passwort eingeben...");
		tf_password.setColumns(10);
		tf_password.setBounds(330, 192, 144, 25);
		panel.add(tf_password);

		JButton bt_Login = new JButton("Login");
		bt_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id = tf_MitarbeiterID.getText();//Hier die inderscheidung zwischen Disponent und Monteur durch vergleich der Nummer mit Bereich für jew Rolle
				
				if(tf_password.getText().equals(verbindung.getPassword(id))) {
					if(verbindung.getRolle(id).equals("Monteur")) {
						MonteurFenster monteur = new MonteurFenster();
						monteur.setVisible(true);
						setVisible(false);
					}else {
						DisponentFenster disponent = new DisponentFenster();
						disponent.setVisible(true);
						setVisible(false);
					}
				} else {
					JOptionPane.showMessageDialog( null,"Passwort erneut eingeben","Anmeldedatenfehler", JOptionPane.PLAIN_MESSAGE);
				}

			}
		});
		bt_Login.setFont(new Font("Verdana", Font.ITALIC, 11));
		bt_Login.setBounds(330, 247, 144, 23);
		panel.add(bt_Login);

		JLabel textLabel = new JLabel("Bitte geben Sie hier ihre Anmeldedaten ein...");
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		textLabel.setBounds(242, 69, 278, 14);
		panel.add(textLabel);

		JLabel logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setBounds(594, 0, 180, 102);
		panel.add(logoLabel);
		icon = new ImageIcon("C:\\Users\\Eclipse_treiber_für_db\\Logo_final180x100.png");
		logoLabel.setIcon(icon);

	}
}
