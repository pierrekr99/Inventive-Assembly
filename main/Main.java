package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import benutzeroberflaeche.LoginFenster;
import datenbank.DatenbankVerbindung;


public class Main {
	
	
	static DatenbankVerbindung db = new DatenbankVerbindung();
	
	
	public static void main(String[] args) {

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Login Fenster wird erstellt
					LoginFenster login = new LoginFenster();
					login.setResizable(false);
					login.setVisible(true);
					login.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static DatenbankVerbindung getdb() {
		return db;
		// Datenbankverbindung wird an andere Klassen übergeben
	}
	
	
}
