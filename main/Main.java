package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Datenbank.datenbankVerbindung;
import gui.LoginFenster;
import test.MonteurAuftraege;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					// Login 
					LoginFenster login = new LoginFenster();
					login.setTitle("Inventive Assembly");
					login.setResizable(false);
					login.setVisible(true);
					login.setLocationRelativeTo(null);
					
					// MonteurAuftr‰ge
					MonteurAuftraege frame = new MonteurAuftraege();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster Groﬂ
					// frame.setUndecorated(true);//Vollbild
					frame.setVisible(false);// Fenster erscheint
					
					// DisponentAuftr‰ge
					
					// DetailsFenster
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
