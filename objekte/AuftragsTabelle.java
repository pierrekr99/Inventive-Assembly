package objekte;

import java.io.Serializable;
import java.util.ArrayList;


import javax.swing.table.AbstractTableModel;

public class AuftragsTabelle extends AbstractTableModel implements Serializable {

//	implementieren des Interfaces Serializable 
	private static final long serialVersionUID = -3516729476934710024L;

//	Erstellung der Tabelle und des Konstruktors
	private ArrayList<Auftrag> auftragsListe;
	private String[] columns = { "Auftragsnr", "Erstellungsdatum", "Frist", "Status", "Zuständigkeit", "Auftraggeber", "Komponenten"};

	public AuftragsTabelle(ArrayList<Auftrag> auftragsListe) {
			this.auftragsListe = auftragsListe;
		}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return auftragsListe.size();
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return auftragsListe.get(rowIndex).getAuftragsnr();
		case 1:
			return auftragsListe.get(rowIndex).getErstellungsdatum();
		case 2:
			return auftragsListe.get(rowIndex).getFrist();
		case 3:
			return auftragsListe.get(rowIndex).getStatus();
		default:
			return null;
		}

	}

}
