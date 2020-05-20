package objekte;

public class Monteur extends Mitarbeiter {

	// Attribute
	private String anwesenheit;

	public Monteur(String name, String vorname, String mitarbeiterNummer, String passwort) {
		super(name, vorname, mitarbeiterNummer, passwort);
		this.anwesenheit = anwesenheit;
	}

	public String getAnwesenheit() {
		return anwesenheit;
	}

	public void setAnwesenheit(String anwesenheit) {
		this.anwesenheit = anwesenheit;
	}

}
