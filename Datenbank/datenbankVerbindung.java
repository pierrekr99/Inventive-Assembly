package Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import objekte.Auftrag;
import objekte.Komponente;
import objekte.*;

public class datenbankVerbindung {

	Connection verbindung = null;
	ResultSet rs;

	int indexmitarbeiter;
	int indexAuftraggeber;

	ArrayList<Auftrag> auftragsListe = new ArrayList<>();
	ArrayList<Auftraggeber> auftraggeberListe = new ArrayList<>();
	ArrayList<Mitarbeiter> disponentListe = new ArrayList<>();
	ArrayList<Komponente> komponentenListe = new ArrayList<>();
	ArrayList<Mitarbeiter> monteurListe = new ArrayList<>();


	public Connection getVerbindung() {
		return verbindung;
	}

	public ResultSet getRs() {
		return rs;
	}

	public ArrayList<Auftrag> getAuftragsListe() {
		return auftragsListe;
	}

	public ArrayList<Auftraggeber> getAuftraggeberListe() {
		return auftraggeberListe;
	}

	public ArrayList<Mitarbeiter> getDisponentListe() {
		return disponentListe;
	}

	public ArrayList<Komponente> getKomponentenListe() {
		return komponentenListe;
	}
	

	public ArrayList<Mitarbeiter> getMonteurListe() {
		return monteurListe;
	}

	public datenbankVerbindung() {
		verbinden();
		auftraggeberEinlesen();
		disponentEinlesen();
		komponenteEinlesen();
		monteurEinlesen();
		auftragEinlesen();
	
	}

	public void verbinden() { // stellt Verbindung mit der Datenbank her

		String adresse = "jdbc:mysql://3.125.60.55/db2";
		String treiber = "com.mysql.cj.jdbc.Driver";
		String benutzername = "db2";
		String passwort = "!db2.winf?";

		try {
			Class.forName(treiber);
			verbindung = DriverManager.getConnection(adresse, benutzername, passwort);
			System.out.println("Verbindung hergestellt");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void trennen() { // trennt die Verbindung mit der Datenbank

		try {
			verbindung.close();
			System.out.println("Verbindung getrennt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void auftragEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {

				ArrayList<Komponente> komponentenlisteauftrag = new ArrayList<>();  
				String[] komponentennrarray = rs.getString("Komponenten").split(","); 
																				
				for (String ab : komponentennrarray) { 
								
					
					for (int i = 0; i < komponentenListe.size(); i++) {
						if (ab.equals(komponentenListe.get(i).getKomponentenNummer())) {
							komponentenlisteauftrag.add(komponentenListe.get(i)); 
																				
						}
					}
					

				}

	

				for (int i = 0; i < monteurListe.size(); i++) {

					if (monteurListe.get(i).getMitarbeiterNummer()
							.equals(rs.getString("ZustaendigMitarbeiterNummer"))) {
						indexmitarbeiter = i;
					}
				}

				for (int i = 0; i < auftraggeberListe.size(); i++) {

					if (auftraggeberListe.get(i).getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						indexAuftraggeber = i;
					}
				}

				objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"), rs.getString("Erstellungsdatum"),
						rs.getString("Frist"), rs.getString("Status"), monteurListe.get(indexmitarbeiter),
						auftraggeberListe.get(indexAuftraggeber), komponentenlisteauftrag);
				
				

				auftragsListe.add(Auftrag);

			}

			System.out.println("Aufträge einlesen:");
			for (Auftrag auftrag : auftragsListe) {
				System.out.println(auftrag);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void auftraggeberEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"),
						rs.getString("KundenNummer")); 
				auftraggeberListe.add(Auftraggeber);
			}
			System.out.println("Auftraggeber einlesen:" + auftraggeberListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disponentEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {
				
				
				if (rs.getString("Rolle").equals("Disponent")) {
					Mitarbeiter Disponent = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"),
							rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"),
							rs.getString("Anwesenheit"));
					disponentListe.add(Disponent);

				}
			}
			System.out.println("Disponenten einlesen:" + disponentListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void komponenteEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `komponente`");

			while (rs.next()) {
				objekte.Komponente Komponente = new Komponente(rs.getString("Name"), rs.getString("KomponentenNummer"),
						rs.getBoolean("Verfuegbarkeit"), rs.getString("Kategorie"));
				komponentenListe.add(Komponente);
			}
			System.out.println("Komponenten einlesen:" + komponentenListe);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void monteurEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {
				
				if (rs.getString("Rolle").equals("Monteur")) {
					Mitarbeiter Monteur = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"),
							rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"),
							rs.getString("Anwesenheit"));
					monteurListe.add(Monteur);
				}
			}
			System.out.println("Monteur einlesen:" + monteurListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

}
