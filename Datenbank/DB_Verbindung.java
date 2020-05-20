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

	Connection Verbindung = null;
	ResultSet rs;
	List<Auftrag> Auftragsliste = new ArrayList<>();
	List<Auftraggeber> Auftraggeberliste = new ArrayList<>();
	List<Disponent> Disponentliste = new ArrayList<>();
	List<Komponente> Komponentenliste = new ArrayList<>();
	List<Monteur> Monteurliste = new ArrayList<>();

	public static void main(String[] args) {

		DB_Verbindung test = new DB_Verbindung();
		test.verbinden();
		test.auftraggeber_einlesen();
		test.disponent_einlesen();
		test.komponente_einlesen();
		test.monteur_einlesen();
		test.auftrag_einlesen();
	}

	public void verbinden() { // stellt Verbindung mit der Datenbank her

		String adresse = "jdbc:mysql://3.125.60.55/db2";
		String treiber = "com.mysql.cj.jdbc.Driver";
		String benutzername = "db2";
		String passwort = "!db2.winf?";

		try {
			Class.forName(treiber);
			Verbindung = DriverManager.getConnection(adresse, benutzername, passwort);
			System.out.println("Verbindung hergestellt");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void trennen() { // trennt die Verbindung mit der Datenbank

		try {
			Verbindung.close();
			System.out.println("Verbindung getrennt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void auftrag_einlesen() { // alle Aufträge werden eingelesen

		try {
			Statement stmt = Verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {

				ArrayList<Komponente> Komponentenlisteauftrag = new ArrayList<>();

				String[] komponentennrarray = rs.getString("Komponenten").split(","); // Der String in der Tabelle Spalte Komponenten, werden nach dem Komma in
																						// verschiedene Strings augeteilt und in den Array gespeichert
				for (String ab : komponentennrarray) { // Für jede Komponentennummer wird nun das richtige Exemplar von Komponente
														// gesucht
					for (int i = 0; i < Komponentenliste.size(); i++) {
						if (ab.equals(Komponentenliste.get(i).getKomponentennr())) {
							Komponentenlisteauftrag.add(Komponentenliste.get(i)); // das Exmplar mit der passenden Kompinentennummer wird der Liste hinzugefügt
						}
					}

				}

				objekte.Auftrag Auftrag = new Auftrag(rs.getString("Auftragsnr"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), rs.getString("ZuständigkeitName"),  rs.getString("ZuständigkeitNr"), rs.getString("Auftraggeber"), Komponentenlisteauftrag);

				Auftragsliste.add(Auftrag);

			}
			System.out.println("Aufträge einlesen:" + Auftragsliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void auftraggeber_einlesen() { // Methode vum alle Auftraggeber aus der Tabelle "auftraggeber" von der
											// Datenbank einlesen

		try {
			Statement stmt = Verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftraggeber`");

			while (rs.next()) {
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"), rs.getString("Kundennr")); // Ein
																														// Exemplar
																														// von
																														// Auftraggeber
																														// wird
																														// erstellt
				Auftraggeberliste.add(Auftraggeber);
			}
			System.out.println("Auftraggeber einlesen:" + Auftraggeberliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disponent_einlesen() {

		try {
			Statement stmt = Verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `disponent`");

			while (rs.next()) {
				objekte.Disponent Disponent = new Disponent(rs.getString("Name"), rs.getString("Vorname"), rs.getString("Mitarbeiternr"), rs.getString("Passwort"));
				Disponentliste.add(Disponent);
			}
			System.out.println("Disponenten einlesen:" + Disponentliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void komponente_einlesen() {

		try {
			Statement stmt = Verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `komponente`");

			while (rs.next()) {
				objekte.Komponente Komponente = new Komponente(rs.getString("Name"), rs.getString("Komponentennr"), rs.getBoolean("Verfügbarkeit"), rs.getString("Kategorie"));
				Komponentenliste.add(Komponente);
			}
			System.out.println("Komponenten einlesen:" + Komponentenliste);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void monteur_einlesen() {

		try {
			Statement stmt = Verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `monteur`");

			while (rs.next()) {
				objekte.Monteur Monteur = new Monteur(rs.getString("Name"), rs.getString("Vorname"), rs.getString("Mitarbeiternr"), rs.getString("Passwort"), rs.getString("Anwesenheit"));
				Monteurliste.add(Monteur);
			}
			System.out.println("Monteur einlesen:" + Monteurliste);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getPassword(String id) {
		String passwort = "";
		try {
			Statement stmt = Verbindung.createStatement();
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
