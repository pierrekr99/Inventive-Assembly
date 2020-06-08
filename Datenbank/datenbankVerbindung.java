package Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



import objekte.*;

public class datenbankVerbindung {

	Connection verbindung = null;
	ResultSet rs;
	Statement stmt;


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
		
		
		ArrayList<String> test = new ArrayList<String>();
		
		
		for (int i = 0; i < 5; i++) {
			test.add("Strich");
		}
		
		monteurListe.add(new Mitarbeiter("Monteur", "nicht", "zugewiesen", "0000", "123", test));
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
			//System.out.println("Verbindung hergestellt");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void trennen() { // trennt die Verbindung mit der Datenbank

		try {
			verbindung.close();
			//System.out.println("Verbindung getrennt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void einlesen() {  //Methode um alle Daten von der Datenbank neu einzulesen
		auftraggeberEinlesen();
		disponentEinlesen();
		komponenteEinlesen();
		monteurEinlesen();
		auftragEinlesen();
	}

	public void auftragEinlesen() { //ließt alle Aufträge ein

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {
				int indexmitarbeiter = -1;
				int indexAuftraggeber = -1;
				Auftraggeber a;
				Mitarbeiter m = null;
				
				
				ArrayList<Komponente> komponentenlisteauftrag = new ArrayList<>();
				String[] komponentennrarray = rs.getString("Komponenten").split(",");

				for (String ab : komponentennrarray) { //Passende Exemplare von Komponenten werden geuscht

					for (int i = 0; i < komponentenListe.size(); i++) {
						if (ab.equals(komponentenListe.get(i).getKomponentenNummer())) {
							komponentenlisteauftrag.add(komponentenListe.get(i));

						}
					}

				}
				
				for (int i = 0; i < monteurListe.size(); i++) { //Passender Exemplare von Monteur wird gesucht
					
					if (monteurListe.get(i).getMitarbeiterNummer().equals(rs.getString("ZustaendigMitarbeiterNummer"))) {
						indexmitarbeiter = i;
					}
				}

				for (int i = 0; i < auftraggeberListe.size(); i++) { //passender Exemplare von Auftraggeber wird gesucht

					if (auftraggeberListe.get(i).getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						indexAuftraggeber = i;
					}
				}
				
			
				if(indexmitarbeiter == -1) {
									
					for (Mitarbeiter monteur : monteurListe) {
						if(monteur.getName().equals("nicht")) {
							m = monteur;
						}
						else {
							
						}
					}
					
					 System.out.println("Kein Monteur konnte dem Auftrag zugewiesen werden"); 
				}
				else {
					 m = monteurListe.get(indexmitarbeiter);
				}
				
				if(indexAuftraggeber == -1) {
					 a = null; // Wenn kein auftraggeber dem Auftrag zugewiesen ist oder keiner gefunden wurde
				}
				else {
					 a = auftraggeberListe.get(indexAuftraggeber);
				}
				

				objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), m, a, komponentenlisteauftrag); // Exemplar von Aftrag wird erstellt mit den Daten aus der Datenbank

				auftragsListe.add(Auftrag); //Exexmplar Auftrag wird der auftragsListe hinzugefügt
				
			}

			//System.out.println("Aufträge einlesen:");
//			for (Auftrag auftrag : auftragsListe) {
//				System.out.println(auftrag);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void auftraggeberEinlesen() { //Auftraggeber werden eingelesen aus der Datenbank

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"), rs.getString("KundenNummer")); //Exemplar von Auftraggeber wird erstellt
				auftraggeberListe.add(Auftraggeber); //Exemplar Auftraggeber wird der aufttragsliste hinzugefügt
			}
			//System.out.println("Auftraggeber einlesen:" + auftraggeberListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disponentEinlesen() { //Disponent wird eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {

				if (rs.getString("Rolle").equals("Disponent")) {
					Mitarbeiter Disponent = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"), rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"), null); //Exemplar von Disponent wird erstellt mit den Daten aus der Datenbank
					disponentListe.add(Disponent); //Explar Disponent wird der disponentenListe hinzugefügt

				}
			}
			//System.out.println("Disponenten einlesen:" + disponentListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void komponenteEinlesen() { //Komponenten werden eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `komponente`");

			while (rs.next()) {
				objekte.Komponente Komponente = new Komponente(rs.getString("Name"), rs.getString("KomponentenNummer"), rs.getBoolean("Verfuegbarkeit"), rs.getString("Kategorie"), rs.getString("Attribut"));
				komponentenListe.add(Komponente);
			}
			//System.out.println("-------------------------------");
//			for (Komponente komponente : komponentenListe) {
//				System.out.println(komponente);
//			}
			//System.out.println("-------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void monteurEinlesen() { // Monteure werden eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {
				ArrayList<String> anwesenheit = new ArrayList<String>();
				String Mitarbeiternummer = rs.getString("MitarbeiterNummer");

				Statement stmte = verbindung.createStatement();
				ResultSet rss;
				rss = stmte.executeQuery("SELECT * FROM `schichtplan` WHERE `Mitarbeiternummer` = " + Mitarbeiternummer + "");
				
				//Schichtplan Array wird erstellt
				
				rss.next();
				anwesenheit.add(rss.getString("Montag"));
				anwesenheit.add(rss.getString("Dienstag"));
				anwesenheit.add(rss.getString("Mittwoch"));
				anwesenheit.add(rss.getString("Donnerstag"));
				anwesenheit.add(rss.getString("Freitag"));
				
				if (rs.getString("Rolle").equals("Monteur")) {
					Mitarbeiter Monteur = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"), rs.getString("Vorname"), Mitarbeiternummer, rs.getString("Passwort"), anwesenheit);
				monteurListe.add(Monteur);
				}
				
			}
			//System.out.println("Monteur einlesen:" + monteurListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	public void setZustaendig(Auftrag auftrag, Mitarbeiter monteur) {
		
		try {
			
			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate("UPDATE `auftrag` SET `ZustaendigName` = '" + monteur.getName()
					+ "', `ZustaendigMitarbeiterNummer` = '" + monteur.getMitarbeiterNummer()
					+ "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void setStatus(Auftrag auftrag, String status) {
		// TODO Auto-generated method stub
		
		try {
			
			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate(
					"UPDATE `auftrag` SET `Status` = '"+ status +"' WHERE (`AuftragsNummer` = '"
							+ auftrag.getAuftragsNummer() + "');");
			// die veränderten Werte werden von der ArrayList direkt in die DB übertragen
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
