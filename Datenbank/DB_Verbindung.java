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


public class DB_Verbindung {

	Connection Verbindung = null;
	ResultSet rs;
	List<Auftrag> Auftragsliste = new ArrayList<>();
	
	
	public static void main (String[] args) { 
		
		DB_Verbindung test = new DB_Verbindung();
		test.verbinden();
		
	}
	

	public void verbinden() { // stellt Verbindung mit der Datenbank her
	
		String adresse = "jdbc:mysql://3.125.60.55";
		String treiber = "com.mysql.cj.jdbc.Driver";
		String benutzername = "db2";
		String passwort = "!db2.winf?";

		try{
			Class.forName(treiber);
			Verbindung = DriverManager.getConnection(adresse, benutzername, passwort);
			
			System.out.println("Verbindung hergestellt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void trennen() {		//trennt die Verbindung mit der Datenbank

		try {
			Verbindung.close();
			System.out.println("Verbindung getrennt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Auftrag_einlesen() { 																 //alle Aufträge werden eingelesen
		try {
		Statement stmt = Verbindung.createStatement();
		rs = stmt.executeQuery("SELECT * FROM `AuftragListe`");
		
		while (rs.next()) {
			objekte.Auftrag a = new Auftrag(rs.getString("Auftragsnummer"), rs.getString("Erstellungsdatum"), rs.getString("Frist"), rs.getString("Status"), rs.getString("Zuständigkeit"), rs.getString("Auftraggeber"), null);																						
			Auftragsliste.add(a);
		}
		System.out.println("Aufträge einlesen:" + Auftragsliste);

		
		
	}catch(Exception e) {
		e.printStackTrace();
	}
		
		
	}
	
	
	public List<Auftrag> getList() { //Methoden Name English --> bekomme_Liste

		return Auftragsliste; // Rückgabewert die Arraylist mit allen Aufträgen

	}
	
}

	
	


