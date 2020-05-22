package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Datenbank.datenbankVerbindung;
import test.MonteurAuftraege;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonteurAuftraege frame = new MonteurAuftraege();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster Groﬂ
					// frame.setUndecorated(true);//Vollbild
					frame.setVisible(true);// Fenster erscheint
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
