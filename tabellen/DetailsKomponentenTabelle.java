package tabellen;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import objekte.Komponente;

public class DetailsKomponentenTabelle extends AbstractTableModel implements Serializable {

//		implementieren des Interfaces Serializable**/
	private static final long serialVersionUID = -6330715590403021515L;

	// Erstellung der Tabelle und des Konstruktors
	private ArrayList<Komponente> komponentenListe;
	private String[] columns = { "Kategorie", "TeileNr", "Verfügbar", "Nicht verfügbar" };

	public DetailsKomponentenTabelle(ArrayList<Komponente> komponentenListe) {
		this.komponentenListe = komponentenListe;
	}

//		Definieren der Tabelle
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return komponentenListe.size();
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

//		wird eventuell zum Einlesen  der Daten benötigt
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}
	
	

/*	Wertrückgabe der Tabelle
 
 *	wenn Komponente verfügbar ist, dann wird ihr Name bei verfügbar angezeigt
 *	Andernfalls ist Feld leer
 */
 
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return komponentenListe.get(rowIndex).getKategorie();
		case 1:
			return komponentenListe.get(rowIndex).getKomponentennr();
		case 2:
			if (komponentenListe.get(rowIndex).isVerfuegbarkeit()) {
				return komponentenListe.get(rowIndex).getName();
			} else return " ";
		case 3:
			if (komponentenListe.get(rowIndex).isVerfuegbarkeit() == false) {
				return komponentenListe.get(rowIndex).getName();
			} else return " ";

		default:
			return null;
		}

	}
}
