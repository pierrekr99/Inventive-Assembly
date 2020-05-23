package objekte;

public class Mitarbeiter {

//	Attribute
	private String name;
	private String vorname;
	private String mitarbeiterNummer;
	private String passwort;
	private String anwesenheit;
	private String rolle;

	public Mitarbeiter(String name, String vorname, String mitarbeiterNummer, String passwort, String anwesenheit,
			String rolle) {
		super();
		this.name = name;
		this.vorname = vorname;
		this.mitarbeiterNummer = mitarbeiterNummer;
		this.passwort = passwort;
		this.anwesenheit = anwesenheit;
		this.rolle = rolle;
	}

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

	public String getAnwesenheit() {
		return anwesenheit;
	}

	public void setAnwesenheit(String anwesenheit) {
		this.anwesenheit = anwesenheit;
	}

	public String getRolle() {
		return rolle;
	}

	public void setRolle(String rolle) {
		this.rolle = rolle;
	}

	@Override
	public String toString() {
		return "Mitarbeiter [name=" + name + ", vorname=" + vorname + ", mitarbeiterNummer=" + mitarbeiterNummer
				+ ", passwort=" + passwort + ", anwesenheit=" + anwesenheit + ", rolle=" + rolle + "]";
	}
	
	

}
