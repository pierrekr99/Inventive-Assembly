package objekte;

import java.util.ArrayList;

public class DiponentAuftragsTabelle extends AuftragsTabelle {

	
	private static final long serialVersionUID = -9117957090034007029L;
	
	private ArrayList<Auftrag> auftragsListe;
	private String[] kopfzeile = { "Auftragsnr","Status", "Erstellungsdatum", "Frist","Mitarbeitername", "MitarbeiterNR", "Auftraggeber" };
	
	public DiponentAuftragsTabelle(ArrayList<Auftrag> auftragsListe) {
		super(auftragsListe);
		this.auftragsListe = auftragsListe;
	}
	
	@Override
	public String getColumnName(int column) {
		return kopfzeile[column];
	}

	@Override
	public int getRowCount() {
		return auftragsListe.size();
	}

	@Override
	public int getColumnCount() {

		return kopfzeile.length;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	
	
//	Wertrückgabe der Tabelle
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return auftragsListe.get(rowIndex).getAuftragsnr();
		case 1:
			return auftragsListe.get(rowIndex).getStatus();
		case 2:
			return auftragsListe.get(rowIndex).getErstellungsdatum();
		case 3:
			return auftragsListe.get(rowIndex).getFrist();
		case 4:
			return auftragsListe.get(rowIndex).getZuständigkeitName();
		case 5:
			return auftragsListe.get(rowIndex).getZuständigkeitNr();
		case 6:
			return auftragsListe.get(rowIndex).getAuftraggeber();
			
		default:
			return null;
		}

	}

}
