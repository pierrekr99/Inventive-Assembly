package objekte;

import java.util.ArrayList;

public class Mitarbeiter {

	/** *****************************************************
	 *	Attribute
	 	*****************************************************/
	
	private String rolle;
	private String name;
	private String vorname;
	private String mitarbeiterNummer;
	private String passwort;
	private ArrayList<String> anwesenheit; // Montag bis Freitag, dargestellt in der DB
	
	/** *****************************************************
	 *	Konstruktor
	 	*****************************************************/
	
	public Mitarbeiter(String rolle, String name, String vorname, String mitarbeiterNummer, String passwort, ArrayList<String> anwesenheit) {
		super();
		this.rolle = rolle;
		this.name = name;
		this.vorname = vorname;
		this.mitarbeiterNummer = mitarbeiterNummer;
		this.passwort = passwort;
		this.anwesenheit = anwesenheit;
	}
	
	/** *****************************************************
	 *	Getter + Setter
	 	*****************************************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getMitarbeiterNummer() {
		return mitarbeiterNummer;
	}

	public void setMitarbeiterNummer(String mitarbeiterNummer) {
		this.mitarbeiterNummer = mitarbeiterNummer;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public ArrayList<String> getAnwesenheit() {
		return anwesenheit;
	}

	public void setAnwesenheit(ArrayList<String> anwesenheit) {
		this.anwesenheit = anwesenheit;
	}

	public String getRolle() {
		return rolle;
	}

	public void setRolle(String rolle) {
		this.rolle = rolle;
	}

	/** *****************************************************
	 *	toString Methode
	 	*****************************************************/
	
	@Override
	public String toString() {
		return "Mitarbeiter [rolle=" + rolle + ", name=" + name + ", vorname=" + vorname + ", mitarbeiterNummer="
				+ mitarbeiterNummer + ", passwort=" + passwort + ", anwesenheit=" + anwesenheit + "]";
	}



	
	
	

}
