package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Datenbank.datenbankVerbindung;
import gui.LoginFenster;
import test.MonteurAuftraege;

public class Main {
	
	
	static datenbankVerbindung db = new datenbankVerbindung();
	
	
	public static void main(String[] args) {

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Login Fenster wird erstellt
					LoginFenster login = new LoginFenster();
					login.setTitle("Inventive Assembly");
					login.setResizable(false);
					login.setVisible(true);
					login.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static datenbankVerbindung getdb() {
		return db;
		// Datenbankverbindung wird an andere Klassen übergeben
	}
	
	
}
