package objekte;

public class Mitarbeiter {
	
//	Attribute
	private String name;
	private String vorname;
	private String mitarbeiternr;
	private String passwort;
	
//	Rolle durch get.class()

	
	public Mitarbeiter(String name, String vorname, String mitarbeiternr, String passwort) {
		super();
		this.name = name;
		this.vorname = vorname;
		this.mitarbeiternr = mitarbeiternr;
		this.passwort = passwort;
	}

	public String getName() {
		return name;
	}

	public String getVorname() {
		return vorname;
	}

	public String getMitarbeiternr() {
		return mitarbeiternr;
	}

	public String getPasswort() {
		return passwort;
	}

	@Override
	public String toString() {
		return "Mitarbeiter [name=" + name + ", vorname=" + vorname + ", mitarbeiternr=" + mitarbeiternr + ", passwort="
				+ passwort + "]";
	}
	
	
	

}
