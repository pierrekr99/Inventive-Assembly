package objekte;

import java.util.Comparator;

/**
 * 
 *         Aus dieser Klasse kann ein Exemplar vom Typ Komponente erstellt
 *         werden, die verfügbar oder nicht verfügbar sein kann. Sie werden
 *         einem Auftrag zugeordnet. 
 */
public class Komponente {

	/*
	 * ***************************************************** Attribute
	 *****************************************************/

	private String name;
	private String komponentenNummer;
	private boolean verfuegbarkeit;
	private String kategorie;
	private String attribut;

	/*
	 * ***************************************************** Konstruktor
	 *****************************************************/

	public Komponente(String name, String komponentenNummer, boolean verfuegbarkeit, String kategorie,
			String attribut) {
		super();
		this.name = name;
		this.komponentenNummer = komponentenNummer;
		this.verfuegbarkeit = verfuegbarkeit;
		this.kategorie = kategorie;
		this.attribut = attribut;
	}

	/*
	 * ***************************************************** Getter + Setter
	 *****************************************************/

	public String getAttribut() {
		return attribut;
	}

	public void setAttribut(String attribut) {
		this.attribut = attribut;
	}

	public String getName() {
		return name;
	}

	public String getKomponentenNummer() {
		return komponentenNummer;
	}

	public boolean isVerfuegbarkeit() {
		return verfuegbarkeit;
	}

	public String getKategorie() {
		return kategorie;
	}

	/*
	 * ***************************************************** toString Methode
	 *****************************************************/

	@Override
	public String toString() {
		return "Komponente [name=" + name + ", komponentenNummer=" + komponentenNummer + ", verfuegbarkeit="
				+ verfuegbarkeit + ", kategorie=" + kategorie + ", attribut=" + attribut + "]";
	}

}
