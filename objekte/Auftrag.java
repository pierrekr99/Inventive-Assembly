package objekte;

import java.util.Comparator;
import java.util.ArrayList;

public class Auftrag {
	
	// kommentar von jani

//	Attribute
	private String auftragsnr;
	private String erstellungsdatum;
	private String frist;
	private String status;
	private String zuständigkeitNr; // angegeben durch Monteurexemplar.getMitarbeiternr();
	private String zuständigkeitName; // angegeben durch Monteurexemplar.getName();
	private String auftraggeber; // angegeben durch Auftraggeberexemplar.getKundennr();
	private ArrayList <Komponente> komponentennr;

	
	public Auftrag(String auftragsnr, String erstellungsdatum, String frist, String status, String zuständigkeitName, String zuständigkeitNr,
			String auftraggeber, ArrayList<Komponente> komponentennr) {
		super();
		this.auftragsnr = auftragsnr;
		this.erstellungsdatum = erstellungsdatum;
		this.frist = frist;
		this.status = status;
		this.zuständigkeitName = zuständigkeitName;
		this.zuständigkeitNr = zuständigkeitNr;
		this.auftraggeber = auftraggeber;
		this.komponentennr = komponentennr;
	}

	public String getZuständigkeitNr() {
		return zuständigkeitNr;
	}

	public String getZuständigkeitName() {
		return zuständigkeitName;
	}

	public String getAuftragsnr() {
		return auftragsnr;
	}

	public String getErstellungsdatum() {
		return erstellungsdatum;
	}

	public String getFrist() {
		return frist;
	}

	public String getStatus() {
		return status;
	}
	
	public String getAuftraggeber() {
		return auftraggeber;
	}
	

	public ArrayList<Komponente> getKomponentennr() {
		return komponentennr;
	}

	@Override
	public String toString() {
		return "Auftrag [auftragsnr=" + auftragsnr + ", erstellungsdatum=" + erstellungsdatum + ", frist=" + frist
				+ ", status=" + status + ", zuständigkeitNr=" + zuständigkeitNr + ", zuständigkeitName="
				+ zuständigkeitName + ", auftraggeber=" + auftraggeber + ", komponentennr=" + komponentennr + "]";
	}


}
