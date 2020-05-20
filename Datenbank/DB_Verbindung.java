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

public class DB_Verbindung {

	Connection verbindung = null;
	ResultSet rs;
	List<Auftrag> auftragsliste = new ArrayList<>();
	List<Auftraggeber> auftraggeberliste = new ArrayList<>();
	List<Disponent> disponentliste = new ArrayList<>();
	List<Komponente> komponentenliste = new ArrayList<>();
	List<Monteur> monteurliste = new ArrayList<>();

	public static void main(String[] args) {

		DB_Verbindung test = new DB_Verbindung();
		test.verbinden();
		test.auftraggeberEinlesen();
		test.disponentEinlesen();
		test.komponenteEinlesen();
		test.monteurEinlesen();
		test.auftragEinlesen();
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

	public void auftragEinlesen() { // alle Aufträge werden eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {

				ArrayList<Komponente> Komponentenlisteauftrag = new ArrayList<>();

				String[] komponentennrarray = rs.getString("Komponenten").split(","); // Der String in der Tabelle Spalte Komponenten, werden nach dem Komma in
																						// verschiedene Strings augeteilt und in den Array gespeichert
				for (String ab : komponentennrarray) { // Für jede Komponentennummer wird nun das richtige Exemplar von Komponente
														// gesucht
					for (int i = 0; i < komponentenliste.size(); i++) {
						if (ab.equals(komponentenliste.get(i).getKomponentenNummer())) {
							Komponentenlisteauftrag.add(komponentenliste.get(i)); // das Exmplar mit der passenden Kompinentennummer wird der Liste hinzugefügt
						}
					}

				}

				objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), rs.getString("ZuständigkeitName"),  rs.getString("ZuständigkeitNr"), rs.getString("Auftraggeber"), Komponentenlisteauftrag);

				auftragsliste.add(Auftrag);

			}
			System.out.println("Aufträge einlesen:" + auftragsliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void auftraggeberEinlesen() { // Methode vum alle Auftraggeber aus der Tabelle "auftraggeber" von der
											// Datenbank einlesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"), rs.getString("KundenNummer")); // Ein
																														// Exemplar
																														// von
																														// Auftraggeber
																														// wird
																														// erstellt
				auftraggeberliste.add(Auftraggeber);
			}
			System.out.println("Auftraggeber einlesen:" + auftraggeberliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disponentEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `disponent`");

			while (rs.next()) {
				objekte.Disponent Disponent = new Disponent(rs.getString("Name"), rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"));
				disponentliste.add(Disponent);
			}
			System.out.println("Disponenten einlesen:" + disponentliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void komponenteEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `komponente`");

			while (rs.next()) {
				objekte.Komponente Komponente = new Komponente(rs.getString("Name"), rs.getString("KomponentenNummer"), rs.getBoolean("Verfuegbarkeit"), rs.getString("Kategorie"));
				komponentenliste.add(Komponente);
			}
			System.out.println("Komponenten einlesen:" + komponentenliste);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void monteurEinlesen() {

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `monteur`");

			while (rs.next()) {
				objekte.Monteur Monteur = new Monteur(rs.getString("Name"), rs.getString("Vorname"), rs.getString("MitarbeiterNummer"), rs.getString("Passwort"), rs.getString("Anwesenheit"));
				monteurliste.add(Monteur);
			}
			System.out.println("Monteur einlesen:" + monteurliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getPassword(String id) {
		String passwort = "";
		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `Login` WHERE `MitarbeiterNr` = " + id);
			while(rs.next()) {
				passwort = rs.getString("Passwort");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return passwort;
	}

}
