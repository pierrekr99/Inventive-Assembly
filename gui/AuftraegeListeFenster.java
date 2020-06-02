package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import Datenbank.datenbankVerbindung;
import gui.DisponentFenster.JButtonEditor;
import gui.DisponentFenster.JButtonRenderer;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AuftraegeListeFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();

	private JPanel contentPane;
	private JTable tabelle;
	private int zeilenTabelle = 0;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuftraegeListeFenster frame = new AuftraegeListeFenster(1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the frame.
	 */
	public AuftraegeListeFenster(int row) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		tabelle = new JTable();
		scrollPane.setViewportView(tabelle);
		tabelle.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		tabelle.setFont(new Font("Tahoma", Font.PLAIN, 18));// Schriftart und -größe in der Tabelle
		tabelle.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		tabelleAktualisieren(row); // Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		tabelle.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
												// Aufträge nach diesem Attribut
												// in der natürlichen Ordnung und umgekehrt sortiert
		tabelle.addMouseListener(new MouseAdapter() {// MouseListener für das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht
												// dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();// wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 0) {// wenn in DetailsSpalte
//						detailsFenster();//Detailsfenster wird geöffnet
						DetailsFenster frame = new DetailsFenster(row); // reihe des Auftrags wird übergeben um details aufrufen zu können
						frame.setVisible(true);
					}
				}
			}
		});
		monteureTblFormat();
	}
	
	private void monteureTblFormat() {
		tabelle.getColumnModel().getColumn(0).setPreferredWidth(150);
		tabelle.getColumnModel().getColumn(0).setMinWidth(150);
		tabelle.getColumnModel().getColumn(0).setMaxWidth(150);

		tabelle.getColumnModel().getColumn(1).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(1).setMinWidth(200);
		tabelle.getColumnModel().getColumn(1).setMaxWidth(250);

		tabelle.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(2).setMinWidth(150);
		tabelle.getColumnModel().getColumn(2).setMaxWidth(250);

		tabelle.getColumnModel().getColumn(3).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(3).setMinWidth(200);
		tabelle.getColumnModel().getColumn(3).setMaxWidth(200);
		
		tabelle.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(2).setMinWidth(200);
		tabelle.getColumnModel().getColumn(2).setMaxWidth(200);

		tabelle.getColumnModel().getColumn(3).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(3).setMinWidth(200);
		tabelle.getColumnModel().getColumn(3).setMaxWidth(200);

		tabelle.setRowHeight(50);
	}
	
	private Object[][] tabelle(int row) {
		int zeile = 0;
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			if (db.getMonteurListe().get(row).getMitarbeiterNummer()
					.equals(db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer())) {
				zeilenTabelle++;
			}
		}
		Object[][] auftraege = new Object[zeilenTabelle][6];
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			if (db.getMonteurListe().get(row).getMitarbeiterNummer()
					.equals(db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer())) {
				auftraege[zeile][0] = "Details";
				auftraege[zeile][1] = db.getAuftragsListe().get(i).getAuftragsNummer();
				auftraege[zeile][2] = db.getAuftragsListe().get(i).getStatus();
				auftraege[zeile][3] = db.getAuftragsListe().get(i).getErstellungsdatum();
				auftraege[zeile][4] = db.getAuftragsListe().get(i).getFrist();
				auftraege[zeile][5] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
				zeile++;
			}
			}
		zeile = 0;
		zeilenTabelle=0;
		return auftraege;
	}

	private void tabelleAktualisieren(int row) {
		tabelle.setModel(new DefaultTableModel(tabelle(row), // Benötigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingefügt
				new String[] { "", "AuftragsNummer", "Status", "Erstellungsdatum", "Frist", "Auftragsgeber" }) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
					false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
				return columnEditables[column];
			}
		});
		tabelle.getColumn(tabelle.getColumnName(0)).setCellRenderer(new JButtonRenderer());
		tabelle.getColumn(tabelle.getColumnName(0)).setCellEditor(new JButtonEditor());
	}
	
	/**
	 * Buttons in der Tabelle
	 */
	
	class JButtonRenderer implements TableCellRenderer {

		JButton button = new JButton();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			table.setShowGrid(true);
			table.setGridColor(Color.LIGHT_GRAY);
			button.setText("Details anzeigen");
			return button;
		}
	}

	class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
		JButton button;
		String txt;

		public JButtonEditor() {
			super();
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						DetailsFenster frame = new DetailsFenster(tabelle.getEditingRow());
						frame.setVisible(true);
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			txt = (value == null) ? "" : value.toString();
			button.setText(txt);
			return button;
		}
	}
}