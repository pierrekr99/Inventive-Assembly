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
	ArrayList<Auftrag> auftragsListe = new ArrayList<>();
	ArrayList<Auftraggeber> auftraggeberListe = new ArrayList<>();
	ArrayList<Disponent> disponentListe = new ArrayList<>();
	ArrayList<Komponente> komponentenListe = new ArrayList<>();
	ArrayList<Monteur> monteurListe = new ArrayList<>();

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

	public ArrayList<Disponent> getDisponentListe() {
		return disponentListe;
	}

	public ArrayList<Komponente> getKomponentenListe() {
		return komponentenListe;
	}

	public ArrayList<Monteur> getMonteurListe() {
		return monteurListe;
	}

	public static void main(String[] args) {

		datenbankVerbindung test = new datenbankVerbindung();
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

				String[] komponentennrarray = rs.getString("Komponenten").split(","); // Der String in der Tabelle
																						// Spalte Komponenten, werden
																						// nach dem Komma in
																						// verschiedene Strings
																						// augeteilt und in den Array
																						// gespeichert
				for (String ab : komponentennrarray) { // Für jede Komponentennummer wird nun das richtige Exemplar von
														// Komponente
														// gesucht
					for (int i = 0; i < komponentenListe.size(); i++) {
						if (ab.equals(komponentenListe.get(i).getKomponentenNummer())) {
							Komponentenlisteauftrag.add(komponentenListe.get(i)); // das Exmplar mit der passenden
																					// Kompinentennummer wird der Liste
																					// hinzugefügt
						}
					}

				}

				/*
				 * Der zuständige Monteur und der Auftraggeber sind im Auftrag als Objekte
				 * gespeichert, allerdings ist eine Objektspeicherung in der Datenbank nicht
				 * möglich. Somit vergleicht man hier beim Monteur die Mitarbeiternummer aus der
				 * DB mit allen Mitarbeiternummern aus der Monteurliste. Bei Übereinstimmung
				 * wird dann der Monteur, dem die Mitarbeiternummer zugeordnet werden konnte,
				 * mit seinen Attributen (die bereits in der Monteurliste gespeichert sind) in
				 * dem Monteurexemplar zustaendig abgespeichert und somit im Auftrag vermerkt.
				 * Gleiches Vorgehen beim Auftraggeber ...
				 */

				Monteur zustaendig = new Monteur(null, null, null, null, null);

				for (Monteur monteur : monteurListe) {
					if (monteur.getMitarbeiterNummer().equals(rs.getString("ZustaendigMitarbeiterNummer"))) {
						zustaendig.setName(monteur.getName());
						zustaendig.setVorname(monteur.getVorname());
						zustaendig.setMitarbeiterNummer(monteur.getMitarbeiterNummer());
						zustaendig.setPasswort(monteur.getPasswort());
						zustaendig.setAnwesenheit(monteur.getAnwesenheit());
					}
				}

				Auftraggeber auftraggeber = new Auftraggeber(null, null);

				for (Auftraggeber kunde : auftraggeberListe) {
					if (kunde.getKundenNummer().equals(rs.getString("Auftraggeber"))) {
						auftraggeber.setKundenNummer(kunde.getKundenNummer());
						auftraggeber.setName(kunde.getName());
					}
				}
				
				if (auftraggeber == null) {
					/*
					 Falls kein Auftraggeber mit dieser Nummer gefunden wird, kann ggf. neuer Auftraggeber angelegt werden? -> oder nur nice-to-have ??
					 Falls ja, müsste es hier einen Verweis auf ein extra GUI Fenster geben.
					 */
					System.out.println("Keinen passenden Auftraggeber gefunden. Möchten Sie einen neuen Auftraggeber anlegen?");
				}

				if (zustaendig.getMitarbeiterNummer() != null && auftraggeber.getKundenNummer() != null) {
					objekte.Auftrag Auftrag = new Auftrag(rs.getString("AuftragsNummer"),
							rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), zustaendig,
							auftraggeber, Komponentenlisteauftrag);

					auftragsListe.add(Auftrag);
				}else {
					System.out.println("Fehler! Der Auftrag konnte leider nicht angelegt werden. Bitte überprüfen Sie ihre Eingaben!");
					
				}
			}
			System.out.println("Aufträge einlesen:");
			for (Auftrag auftrag : auftragsListe) {
				System.out.println(auftrag);
			}

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
