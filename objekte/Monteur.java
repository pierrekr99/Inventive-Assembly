package objekte;

public class Monteur extends Mitarbeiter {

	// Attribute
	private String anwesenheit;

	public Monteur(String name, String vorname, String mitarbeiterNummer, String passwort, String anwesenheit) {
		super(name, vorname, mitarbeiterNummer, passwort);
		this.anwesenheit = anwesenheit;
	}

	public String getAnwesenheit() {
		return anwesenheit;
	}

	public void setAnwesenheit(String anwesenheit) {
		this.anwesenheit = anwesenheit;
	}

	@Override
	public String toString() {
		return "Monteur [anwesenheit=" + anwesenheit + ", getName()=" + getName() + ", getVorname()=" + getVorname()
				+ ", getMitarbeiterNummer()=" + getMitarbeiterNummer() + ", getPasswort()=" + getPasswort() + "]";
	}

	
	

}
