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
		monteurListe.add(new Mitarbeiter("Monteur", "nicht", "zugewiesen", "0000", "123", null));
		auftraggeberEinlesen();
		disponentEinlesen();
		komponenteEinlesen();
		monteurEinlesen();
		auftragEinlesen();

	}

	// === Hilfsmethoden ===

	public void verbinden() { // stellt Verbindung mit der Datenbank her

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

	public void trennen() { // trennt die Verbindung mit der Datenbank

		try {
			verbindung.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void einlesen() { // Methode um alle Daten von der Datenbank neu einzulesen
		// Listen werden gelehrt und dann neu eingelesen
		auftragsListe.clear();
		auftraggeberListe.clear();
		disponentListe.clear();
		komponentenListe.clear();
		monteurListe.clear();

		monteurListe.add(new Mitarbeiter("Monteur", "nicht", "zugewiesen", "0000", "123", null));
		auftraggeberEinlesen();
		disponentEinlesen();
		komponenteEinlesen();
		monteurEinlesen();
		auftragEinlesen();
	}

	// === Einlese-Methoden ===

	public void auftragEinlesen() { // Aufträge werden aus der Datenbank eingelesen

		try {

			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {
				int indexmitarbeiter = -1;
				int indexAuftraggeber = -1;
				Auftraggeber a;
				Mitarbeiter m = null;

				ArrayList<Komponente> komponentenlisteauftrag = new ArrayList<>();

				// In der Datenbanktabelle für Aufträge sind die Komponenten in diesem Format
				// xxxx,yyyy,zzzz abgespeichert,
				// deshalb muss der String erstmal gespalten werden wo das Komma steht
				String[] komponentennrarray = rs.getString("Komponenten").split(",");

				// Alle Komponentennummern werden nun mit den Komponentennummern der Exemplare
				// verglichen
				// falls es zu einer Übereinstimmung kommt wird das Exemplar der Komponente dem
				// Auftrag zugewießen,
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
				// bei einer Übereinstimmung wird der Index von dem Monteurexemplar gespeichert
				// sollte es zu keiner Überseinstimmung kommen bleibt "indexmitarbeiter" -1
				for (int i = 0; i < monteurListe.size(); i++) { // Passender Exemplare von Monteur wird gesucht

					if (monteurListe.get(i).getMitarbeiterNummer().equals(rs.getString("ZustaendigMitarbeiterNummer"))) {
						indexmitarbeiter = i;
					}
				}

				//

				for (int i = 0; i < auftraggeberListe.size(); i++) { // passender Exemplare von Auftraggeber wird
																		// gesucht

					if (auftraggeberListe.get(i).getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						indexAuftraggeber = i;
					}
				}

				if (indexmitarbeiter == -1) {
					for (Mitarbeiter monteur : monteurListe) {
						if (monteur.getName().equals("nicht")) {
							m = monteur;
						}
					}

				} else {
					m = monteurListe.get(indexmitarbeiter);
				}

				if (indexAuftraggeber == -1) {
					a = null; // Wenn kein auftraggeber dem Auftrag zugewiesen ist oder keiner gefunden wurde
				} else {
					a = auftraggeberListe.get(indexAuftraggeber);
				}

				objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), m, a, komponentenlisteauftrag); // Exemplar von
																																																		// Aftrag wird
																																																		// erstellt mit
																																																		// den Daten aus
																																																		// der Datenbank

				auftragsListe.add(Auftrag); // Exexmplar Auftrag wird der auftragsListe hinzugefügt

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void auftraggeberEinlesen() { // Auftraggeber werden eingelesen aus der Datenbank

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"), rs.getString("KundenNummer")); // Exemplar von Auftraggeber wird erstellt
				auftraggeberListe.add(Auftraggeber); // Exemplar Auftraggeber wird der aufttragsliste hinzugefügt
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disponentEinlesen() { // Disponent wird aus der Datenbank eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {

				if (rs.getString("Rolle").equals("Disponent")) {
					Mitarbeiter Disponent = new Mitarbeiter(rs.getString("Rolle"), rs.getString("Name"),
							rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"), null); 
					//Exemplar von Disponent wir derstellt mit den Daten aus der Datenbank
																														
					disponentListe.add(Disponent); // Explar Disponent wird der disponentenListe hinzugefügt

				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void komponenteEinlesen() { // Komponenten werden aus der Datenbank eingelesen

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

	public void monteurEinlesen() { // Monteure werden aus der Datenbank eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `mitarbeiter`");

			while (rs.next()) {
				ArrayList<String> anwesenheit = new ArrayList<String>();
				String Mitarbeiternummer = rs.getString("MitarbeiterNummer");

				Statement stmte = verbindung.createStatement();
				ResultSet rss;
				rss = stmte.executeQuery("SELECT * FROM `schichtplan` WHERE `Mitarbeiternummer` = " + Mitarbeiternummer + "");

				// Schichtplan Array wird erstellt

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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// === Setter-Methoden ===

	public void setZustaendig(Auftrag auftrag, Mitarbeiter monteur) { // verändert in der Datenbank den zuständigen Monteur

		try {

			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate("UPDATE `auftrag` SET `ZustaendigName` = '" + monteur.getName() + "', `ZustaendigMitarbeiterNummer` = '" + monteur.getMitarbeiterNummer() + "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setStatus(Auftrag auftrag, String status) { // verändert in der Datenbank den Status eines Auftrages

		try {

			Statement stmt = verbindung.createStatement();

			stmt.executeUpdate("UPDATE `auftrag` SET `Status` = '" + status + "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");
			// die veränderten Werte werden von der ArrayList direkt in die DB übertragen
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// === Getter-Methoden ===

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
}
