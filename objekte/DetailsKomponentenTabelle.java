package objekte;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DetailsKomponentenTabelle extends AbstractTableModel implements Serializable {

//		implementieren des Interfaces Serializable**/
	private static final long serialVersionUID = -6330715590403021515L;

	// Erstellung der Tabelle und des Konstruktors
	private ArrayList<Komponente> KomponentenListe;
	private String[] columns = { "Kategorie", "TeileNr", "Verfügbar", "Nicht verfügbar" };

	public DetailsKomponentenTabelle(ArrayList<Komponente> KomponentenListe) {
		this.KomponentenListe = KomponentenListe;
	}

//		Definieren der Tabelle
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return KomponentenListe.size();
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
			return KomponentenListe.get(rowIndex).getKategorie();
		case 1:
			return KomponentenListe.get(rowIndex).getKomponentennr();
		case 2:
			if (KomponentenListe.get(rowIndex).isVerfuegbarkeit()) {
				return KomponentenListe.get(rowIndex).getName();
			} else return " ";
		case 3:
			if (KomponentenListe.get(rowIndex).isVerfuegbarkeit() == false) {
				return KomponentenListe.get(rowIndex).getName();
			} else return " ";

		default:
			return null;
		}

	}
}
