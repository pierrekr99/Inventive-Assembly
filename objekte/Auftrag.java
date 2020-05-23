package objekte;

import java.util.ArrayList;

public class Auftrag {
	
//<<<<<<< HEAD
//
//=======
//>>>>>>> branch 'master' of https://github.com/pierrekr99/Inventive-Assembly1

//	Attribute
	private String auftragsNummer;
	private String erstellungsdatum;
	private String frist;
	private String status;
	private Mitarbeiter zustaendig; // in Tabelle angegeben durch Monteurexemplar.getName(); + angegeben durch Monteurexemplar.getMitarbeiternummer();
	private Auftraggeber auftraggeber;
	//in Tabelle angegeben durch Auftraggeberexemplar.getKundennummer();
	private ArrayList <Komponente> komponenten; //in Tabelle angegeben in ArrayList mit komponentenexemplar.getKomponentennummer()

	
// Konstruktor

	public Auftrag(String auftragsnummer, String erstellungsdatum, String frist, String status, Mitarbeiter zustaendig,
			Auftraggeber auftraggeber, ArrayList<Komponente> komponenten) {
		super();
		this.auftragsNummer = auftragsnummer;
		this.erstellungsdatum = erstellungsdatum;
		this.frist = frist;
		this.status = status;
		this.zustaendig = zustaendig;
		this.auftraggeber = auftraggeber;
		this.komponenten = komponenten;
	}


	public String getAuftragsNummer() {
		return auftragsNummer;
	}


	public void setAuftragsNummer(String auftragsnummer) {
		this.auftragsNummer = auftragsnummer;
	}


	public String getErstellungsdatum() {
		return erstellungsdatum;
	}


	public void setErstellungsdatum(String erstellungsdatum) {
		this.erstellungsdatum = erstellungsdatum;
	}


	public String getFrist() {
		return frist;
	}


	public void setFrist(String frist) {
		this.frist = frist;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Mitarbeiter getZustaendig() {
		return zustaendig;
	}


	public void setZustaendig(Mitarbeiter zustaendig) {
		this.zustaendig = zustaendig;
	}


	public Auftraggeber getAuftraggeber() {
		return auftraggeber;
	}


	public void setAuftraggeber(Auftraggeber auftraggeber) {
		this.auftraggeber = auftraggeber;
	}


	public ArrayList<Komponente> getKomponenten() {
		return komponenten;
	}


	public void setKomponenten(ArrayList<Komponente> komponenten) {
		this.komponenten = komponenten;
	}


	@Override
	public String toString() {
		return "Auftrag [auftragsnummer=" + auftragsNummer + ", erstellungsdatum=" + erstellungsdatum + ", frist="
				+ frist + ", status=" + status + ", zustaendig=" + zustaendig + ", auftraggeber=" + auftraggeber
				+ ", komponenten=" + komponenten + "]";
	}

	

}
