package objekte;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public abstract class AuftragsTabelle extends AbstractTableModel implements Serializable {

//	implementieren des Interfaces Serializable 
	private static final long serialVersionUID = -3516729476934710024L;

//	Erstellung der Tabelle und des Konstruktors
	private ArrayList<Auftrag> auftragsListe;
	private String[] kopfzeile;

	public AuftragsTabelle(ArrayList<Auftrag> auftragsListe) {
			this.auftragsListe = auftragsListe;
		}

	
	
//	Definieren der Tabelle
	@Override
	public abstract String getColumnName(int column);

	@Override
	public abstract int getRowCount();

	@Override
	public abstract int getColumnCount();

	@Override
	public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);

	
	
//	Wertrückgabe der Tabelle
	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);

}
