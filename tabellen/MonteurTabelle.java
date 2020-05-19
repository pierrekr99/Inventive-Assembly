package tabellen;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import objekte.Monteur;

public class MonteurTabelle extends AbstractTableModel implements Serializable {

//	implementieren des Interfaces Serializable**/
	private static final long serialVersionUID = -3516729476934710024L;

	
//	Erstellung der Tabelle und des Konstruktors
	private ArrayList<Monteur> monteurListe;
	private String[] columns = {"Mitarbeiternr", "Nachname", "Vorname", "Anwesenheit"};

	
	public MonteurTabelle(ArrayList<Monteur> monteurListe) {
		this.monteurListe = monteurListe;
	}

	
	
//	Definieren der Tabelle
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return monteurListe.size();
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

	
//	wird eventuell zum Einlesen  der Daten benötigt
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	
//	Wertrückgabe der Tabelle
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return monteurListe.get(rowIndex).getMitarbeiternr();
		case 1:
			return monteurListe.get(rowIndex).getName();
		case 2:
			return monteurListe.get(rowIndex).getVorname();
		case 3:
			return monteurListe.get(rowIndex).getAnwesenheit();

		default:
			return null;
		}

	}

}
