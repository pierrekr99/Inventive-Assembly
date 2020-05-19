package tabellen;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import objekte.Monteur;

public class DetailsZuständigkeitTabelle extends AbstractTableModel implements Serializable {

//		implementieren des Interfaces Serializable**/
	private static final long serialVersionUID = 7711077838311496498L;
	
	//		Erstellung der Tabelle und des Konstruktors
	private ArrayList<Monteur> detailListe;
	private String[] columns = { "Name", "Mitarbeiternr" };

	public DetailsZuständigkeitTabelle(ArrayList<Monteur> detailListe) {
			this.detailListe = detailListe;
		}

//		Definieren der Tabelle
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return detailListe.size();
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

//		wird eventuell zum Einlesen  der Daten benötigt
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

//		Wertrückgabe der Tabelle
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return detailListe.get(rowIndex).getName();
		case 1:
			return detailListe.get(rowIndex).getMitarbeiternr();
		default:
			return null;
		}

	}

}
