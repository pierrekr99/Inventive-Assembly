package objekte;

import java.util.Comparator;

public class Komponente  {
	
//	Attribute
	private String name;
	private String komponentenNummer;
	private boolean verfuegbarkeit;
	private String kategorie;

	
	public Komponente(String name, String komponentenNummer, boolean verfuegbarkeit, String kategorie) {
		super();
		this.name = name;
		this.komponentenNummer = komponentenNummer;
		this.verfuegbarkeit = verfuegbarkeit;
		this.kategorie = kategorie;
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

	@Override
	public String toString() {
		return "KomponentenNummer = " + komponentenNummer
			;
	}

}
