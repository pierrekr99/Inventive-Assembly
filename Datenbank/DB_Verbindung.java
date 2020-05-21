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
	List<Auftrag> auftragsListe = new ArrayList<>();
	List<Auftraggeber> auftraggeberListe = new ArrayList<>();
	List<Disponent> disponentListe = new ArrayList<>();
	List<Komponente> komponentenListe = new ArrayList<>();
	List<Monteur> monteurListe = new ArrayList<>();

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

	public void auftragEinlesen() { // alle Auftr�ge werden eingelesen

		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `auftrag`");

			while (rs.next()) {

				ArrayList<Komponente> Komponentenlisteauftrag = new ArrayList<>();

				String[] komponentennrarray = rs.getString("Komponenten").split(","); // Der String in der Tabelle
																						// Spalte Komponenten, werden
																						// nach dem Komma in
																						// verschiedene Strings
																						// augeteilt und in den Array
																						// gespeichert
				for (String ab : komponentennrarray) { // F�r jede Komponentennummer wird nun das richtige Exemplar von
														// Komponente
														// gesucht
					for (int i = 0; i < komponentenListe.size(); i++) {
						if (ab.equals(komponentenListe.get(i).getKomponentenNummer())) {
							Komponentenlisteauftrag.add(komponentenListe.get(i)); // das Exmplar mit der passenden
																					// Kompinentennummer wird der Liste
																					// hinzugef�gt
						}
					}

				}

				/*
				 * Der zust�ndige Monteur und der Auftraggeber sind im Auftrag als Objekte
				 * gespeichert, allerdings ist eine Objektspeicherung in der Datenbank nicht
				 * m�glich. Somit vergleicht man hier beim Monteur die Mitarbeiternummer aus der
				 * DB mit allen Mitarbeiternummern aus der Monteurliste. Bei �bereinstimmung
				 * wird dann der Monteur, dem die Mitarbeiternummer zugeordnet werden konnte,
				 * mit seinen Attributen (die bereits in der Monteurliste gespeichert sind) in
				 * dem Monteurexemplar zustaendig abgespeichert und somit im Auftrag vermerkt.
				 * Gleiches Vorgehen beim Auftraggeber ...
				 */

				Monteur zustaendig = null;

				for (Monteur monteur : monteurListe) {
					if (monteur.getMitarbeiterNummer().equals(rs.getString("Zust�ndigkeitNummer"))) {
						zustaendig.setName(monteur.getName());
						zustaendig.setVorname(monteur.getVorname());
						zustaendig.setMitarbeiterNummer(monteur.getMitarbeiterNummer());
						zustaendig.setPasswort(monteur.getPasswort());
						zustaendig.setAnwesenheit(monteur.getAnwesenheit());
					}
				}

				Auftraggeber auftraggeber = null;

				for (Auftraggeber kunde : auftraggeberListe) {
					if (kunde.getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						auftraggeber.setKundenNummer(kunde.getKundenNummer());
						auftraggeber.setName(kunde.getName());
					}
				}
				
				if (auftraggeber == null) {
					/*
					 Falls kein Auftraggeber mit dieser Nummer gefunden wird, kann ggf. neuer Auftraggeber angelegt werden? -> oder nur nice-to-have ??
					 Falls ja, m�sste es hier einen Verweis auf ein extra GUI Fenster geben.
					 */
					System.out.println("Keinen passenden Auftraggeber gefunden. M�chten Sie einen neuen Auftraggeber anlegen?");
				}

				if (zustaendig != null && auftraggeber != null) {
					objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"),
							rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), zustaendig,
							auftraggeber, Komponentenlisteauftrag);

					auftragsListe.add(Auftrag);
				}else {
					System.out.println("Fehler! Der Auftrag konnte leider nicht angelegt werden. Bitte �berpr�fen Sie ihre Eingaben!");
					
				}
			}
			System.out.println("Auftr�ge einlesen:" + auftragsListe);

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
				objekte.Auftraggeber Auftraggeber = new Auftraggeber(rs.getString("Name"),
						rs.getString("KundenNummer")); // Ein
														// Exemplar
														// von
														// Auftraggeber
														// wird
														// erstellt
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
			rs = stmt.executeQuery("SELECT * FROM `disponent`");

			while (rs.next()) {
				objekte.Disponent Disponent = new Disponent(rs.getString("Name"), rs.getString("Vorname"),
						rs.getString("MitarbeiterNummer"), rs.getString("Passwort"));
				disponentListe.add(Disponent);
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
			rs = stmt.executeQuery("SELECT * FROM `monteur`");

			while (rs.next()) {
				objekte.Monteur Monteur = new Monteur(rs.getString("Name"), rs.getString("Vorname"),
						rs.getString("MitarbeiterNummer"), rs.getString("Passwort"), rs.getString("Anwesenheit"));
				monteurListe.add(Monteur);
			}
			System.out.println("Monteur einlesen:" + monteurListe);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getPassword(String id) {
		String passwort = "";
		try {
			Statement stmt = verbindung.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `Login` WHERE `MitarbeiterNr` = " + id);
			while (rs.next()) {
				passwort = rs.getString("Passwort");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return passwort;
	}

}
