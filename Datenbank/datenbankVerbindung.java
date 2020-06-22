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

	private Connection verbindung = null;
	private ResultSet rs;
	private Statement stmt;

	private ArrayList<Auftrag> auftragsListe = new ArrayList<>();
	private ArrayList<Auftraggeber> auftraggeberListe = new ArrayList<>();
	private ArrayList<Mitarbeiter> disponentListe = new ArrayList<>();
	private ArrayList<Komponente> komponentenListe = new ArrayList<>();
	private ArrayList<Mitarbeiter> monteurListe = new ArrayList<>();

	// === Konstruktor ===

	public datenbankVerbindung() {

		verbinden();
		einlesen();

	}

	// === Hilfsmethoden ===

	
	/**
	 * stellt Verbindung mit der Datenbank her
	 * 
	 */
	public void verbinden() {

		String adresse = "jdbc:mysql://3.125.60.55/db2";
		String treiber = "com.mysql.cj.jdbc.Driver";
		String benutzername = "db2";
		String passwort = "!db2.winf?";

		try {
			Class.forName(treiber);
			verbindung = DriverManager.getConnection(adresse, benutzername, passwort);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * trennt die Verbindung mit der Datenbank
	 */
	public void trennen() {

		try {
			verbindung.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * lieﬂt die Datenbank neu ein
	 */
	public void einlesen() {
		auftragsListe.clear();
		auftraggeberListe.clear();
		disponentListe.clear();
		komponentenListe.clear();
		monteurListe.clear();

		monteurListe.add(new Mitarbeiter("Monteur", "nicht", "zugewiesen", "0000", "123", null)); 
		//erstellt "leeren" Monteur, jedem Auftrag ohne Monteur, wird dieser "leere" Monteur zugewieﬂen
		
		auftraggeberEinlesen();
		disponentEinlesen();
		komponenteEinlesen();
		monteurEinlesen();
		auftragEinlesen();
	}

	// === Einlese-Methoden ===

	/**
	 * Auftr‰ge werden aus der Datenbank eingelesen
	 */
	public void auftragEinlesen() { 

		try {

			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {
				int indexmitarbeiter = -1;
				Auftraggeber auftraggeber = null;
				Mitarbeiter mitarbeiter = null;

				ArrayList<Komponente> komponentenlisteauftrag = new ArrayList<>();

				// In der Datenbanktabelle f¸r Auftr‰ge sind die Komponenten in diesem Format
				// xxxx,yyyy,zzzz abgespeichert,
				// deshalb muss der String erstmal gespalten werden
				
				String[] komponentennrarray = rs.getString("Komponenten").split(",");

				// Alle Komponentennummern werden nun mit den Komponentennummern der Exemplare
				// verglichen
				// falls es zu einer ‹bereinstimmung kommt wird das Exemplar der Komponente dem
				// Auftrag zugewieﬂen,
				// bzw. erstmal in dem Array "komponentenlisteauftrag" gespeichert
				
				for (String komponente : komponentennrarray) {

					for (int i = 0; i < komponentenListe.size(); i++) {
						if (komponente.equals(komponentenListe.get(i).getKomponentenNummer())) {
							komponentenlisteauftrag.add(komponentenListe.get(i));

						}
					}

				}
				
				// die Mitarbeiternummer wird aus dem Auftrag ausgelesen und mit den Nummern von
				// den Monteurexemplaren verglichen
				// bei einer ‹bereinstimmung wird der Index von dem Monteurexemplar gespeichert
				// sollte es zu keiner ‹berseinstimmung kommen bleibt "indexmitarbeiter" -1
				
				for (int i = 0; i < monteurListe.size(); i++) { // Passendes Exemplar von Monteur wird gesucht

					if (monteurListe.get(i).getMitarbeiterNummer().equals(rs.getString("ZustaendigMitarbeiterNummer"))) {
						indexmitarbeiter = i;
					}
				}

				for (int i = 0; i < auftraggeberListe.size(); i++) { // passendes Exemplar von Auftraggeber wird
																		// gesucht

					if (auftraggeberListe.get(i).getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						auftraggeber = auftraggeberListe.get(i);
					}
				}

				
				if (indexmitarbeiter == -1) { 
					for (Mitarbeiter monteur : monteurListe) {
						if (monteur.getName().equals("nicht")) {
							mitarbeiter = monteur;
						}
					}
					//wenn es zu keiner ‹bereinstimmung gekommen ist, wird dem Auftrag der "leere" Monteur zugeordnet 
				} else {
					mitarbeiter = monteurListe.get(indexmitarbeiter);
				}
				 
				
				
				
				objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), mitarbeiter, auftraggeber, komponentenlisteauftrag); 
				// Exemplar von Auftrag wird erstellt
				
				auftraggeber = null;
				mitarbeiter = null; 
			
				auftragsListe.add(Auftrag); // Exexmplar Auftrag wird der auftragsListe hinzugef¸gt

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Auftraggeber werden eingelesen aus der Datenbank
	 */
	public void auftraggeberEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"), rs.getString("KundenNummer")); // Exemplar von Auftraggeber wird erstellt
				auftraggeberListe.add(Auftraggeber); // Exemplar Auftraggeber wird der aufttragsliste hinzugef¸gt
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Disponent wird aus der Datenbank eingelesen
	 */
	public void disponentEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {

				if (rs.getString("Rolle").equals("Disponent")) {
					Mitarbeiter Disponent = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"), rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"), null);
					

					disponentListe.add(Disponent);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Komponenten werden aus der Datenbank eingelesen
	 */
	public void komponenteEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `komponente`");

			while (rs.next()) {
				objekte.Komponente Komponente = new Komponente(rs.getString("Name"), rs.getString("KomponentenNummer"), rs.getBoolean("Verfuegbarkeit"), rs.getString("Kategorie"), rs.getString("Attribut"));
				
				komponentenListe.add(Komponente);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Monteure werden aus der Datenbank eingelesen
	 */
	public void monteurEinlesen() { 

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {
				ArrayList<String> anwesenheit = new ArrayList<String>();
				String Mitarbeiternummer = rs.getString("MitarbeiterNummer");

				Statement stmte = verbindung.createStatement();
				ResultSet rss;
				rss = stmte.executeQuery("SELECT * FROM `schichtplan` WHERE `Mitarbeiternummer` = " + Mitarbeiternummer + "");
				rss.next();
				anwesenheit.add(rss.getString("Montag"));
				anwesenheit.add(rss.getString("Dienstag"));
				anwesenheit.add(rss.getString("Mittwoch"));
				anwesenheit.add(rss.getString("Donnerstag"));
				anwesenheit.add(rss.getString("Freitag"));
				//Schichplan wird erstellt
				
				if (rs.getString("Rolle").equals("Monteur")) {
					Mitarbeiter Monteur = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"), rs.getString("Vorname"), Mitarbeiternummer, rs.getString("Passwort"), anwesenheit);
					monteurListe.add(Monteur);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// === Setter-Methoden ===

	/**
	 * ver‰ndert in der Datenbank den zust‰ndigen Monteur eines Auftrages
	 * @param auftrag
	 * @param monteur
	 * 
	 */
	public void setZustaendig(Auftrag auftrag, Mitarbeiter monteur) { 

		try {

			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate("UPDATE `auftrag` SET `ZustaendigName` = '" + monteur.getName() + "', `ZustaendigMitarbeiterNummer` = '" + monteur.getMitarbeiterNummer() + "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**ver‰ndert den Status eines Auftrages in der Datenbank
	 * @param auftrag
	 * @param status
	 */
	public void setStatus(Auftrag auftrag, String status) { 

		try {

			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate("UPDATE `auftrag` SET `Status` = '" + status + "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// === Getter-Methoden ===

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
}
