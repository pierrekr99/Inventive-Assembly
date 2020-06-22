package objekte;

import java.util.Comparator;

/**
 * Aus dieser Klasse wird ein Exemplar vom Typ Auftraggeber erstellt, der von
 * der HighSpeedGmbH bedient wird.
 *
 */
public class Auftraggeber {

	/*
	 * ***************************************************** Attribute
	 *****************************************************/

	private String name;
	private String kundenNummer;

	/*
	 * ***************************************************** Konstruktor
	 *****************************************************/

	public Auftraggeber(String name, String kundenNummer) {
		super();
		this.name = name;
		this.kundenNummer = kundenNummer;

		/*
		 * ***************************************************** Getter + Setter
		 *****************************************************/

	}

	public String getName() {
		return name;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	/*
	 * ***************************************************** toString Methode
	 *****************************************************/

	@Override
	public String toString() {
		return " [Name = " + name + ", Kundennr = " + kundenNummer + "]";
	}

}
