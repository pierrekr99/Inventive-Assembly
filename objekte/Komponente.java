package objekte;

import java.util.Comparator;

public class Komponente  {
	
//	Attribute
	private String name;
	private String komponentennr;
	private boolean verfuegbarkeit;
	private String kategorie;

	
	public Komponente(String name, String komponentennr, boolean verfuegbarkeit, String kategorie) {
		super();
		this.name = name;
		this.komponentennr = komponentennr;
		this.verfuegbarkeit = verfuegbarkeit;
		this.kategorie = kategorie;
	}

	public String getName() {
		return name;
	}

	public String getKomponentennr() {
		return komponentennr;
	}

	public boolean isVerfuegbarkeit() {
		return verfuegbarkeit;
	}

	public String getKategorie() {
		return kategorie;
	}

	@Override
	public String toString() {
		return "Komponentennr = " + komponentennr
			;
	}

}
