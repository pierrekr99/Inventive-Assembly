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

import datenbank.DatenbankVerbindung;
import gui.DisponentFenster.JButtonEditor;
import gui.DisponentFenster.JButtonRenderer;
import objekte.Auftrag;
import objekte.Mitarbeiter;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AuftraegeListeFenster extends JFrame {

	static DatenbankVerbindung db = main.Main.getdb();

	private JPanel contentPane;
	private JTable tabelle;
	private int zeilenTabelle = 0;
	private int zeile = 0;
	Mitarbeiter monteur;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { AuftraegeListeFenster frame = new
	 * AuftraegeListeFenster(db.getMonteurListe().get(1)); frame.setVisible(true); }
	 * catch (Exception e) { e.printStackTrace(); } } }); }
	 */

	/**
	 * Erstellt ein Fenster, das die Aufträge, die dem Monteur monteur zugewiesen
	 * wurden, in einer tabelle auflistet.
	 * 
	 * @param mitarbeiter
	 */
	public AuftraegeListeFenster(Mitarbeiter mitarbeiter) {
		monteur = mitarbeiter;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setIconImage(LoginFenster.getImage());

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		tabelle = new JTable();
		scrollPane.setViewportView(tabelle);
		tabelle.setCellSelectionEnabled(true);
		// Einzelne Zellen können ausgewählt werden

		tabelle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -größe in der Tabelle

		tabelle.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -größe in der Kopfzeile

		tabelleAktualisieren(mitarbeiter);
		// Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		tabelle.setAutoCreateRowSorter(true);
		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Aufträge
		 * nach diesem Attribut in der natürlichen Ordnung und umgekehrt sortiert
		 */

		tabelleFormat();

		this.setTitle(mitarbeiter.getName() + ", " + mitarbeiter.getVorname());
		// Titel des Fensters

	}

	/**
	 * GUI-Hilfsmethoden.
	 */
	private void tabelleAktualisieren(Mitarbeiter monteur) {
		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		tabelle.setModel(new DefaultTableModel(tabelle(monteur),
				new String[] { "", "AuftragsNummer", "Status", "Erstellungsdatum", "Frist", "Auftragsgeber" }) {

			boolean[] columnEditables = new boolean[] { true, false, false, false, false, false };
			// welche spalten lassen sich ändern

			public boolean isCellEditable(int row, int column) {
				// Kontrollmethode ob spalten sich ändern lassen

				return columnEditables[column];
			}
		});

		tabelle.getColumn(tabelle.getColumnName(0)).setCellRenderer(new JButtonRenderer());
		// ButtonRenderer wird in Spalte 0 ausgeführt

		tabelle.getColumn(tabelle.getColumnName(0)).setCellEditor(new JButtonEditor());
		// ButtonEditorwird in Spalte 0 ausgeführt
	}

	private Object[][] tabelle(Mitarbeiter monteur) {
		int zeile = 0;

		// Wie groß soll die tabelle für diesen Monteur werden
		zeilenTabelle = summeAuftraege(monteur);

		Object[][] auftraege = new Object[zeilenTabelle][6];
		// dieses Array befüllt die Tabelle

		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			if (richtigerAuftrag(i) && !db.getAuftragsListe().get(i).getStatus().equals("im Lager")) {
				// die dem Monteur zugewiesenen Aufträge werden herausgesucht, allerdings zählen
				// hierzu nicht mehr die Aufträge, die bereits im Lager sind

				auftraege[zeile][0] = "Details";
				auftraege[zeile][1] = "";
				if (db.getAuftragsListe().get(i).getAuftragsNummer() != null)
					auftraege[zeile][1] = db.getAuftragsListe().get(i).getAuftragsNummer();
				// AuftragsNummer

				auftraege[zeile][2] = "";
				if (db.getAuftragsListe().get(i).getStatus() != null)
					auftraege[zeile][2] = db.getAuftragsListe().get(i).getStatus();
				// Status

				auftraege[zeile][3] = "";
				if (db.getAuftragsListe().get(i).getErstellungsdatum() != null)
					auftraege[zeile][3] = db.getAuftragsListe().get(i).getErstellungsdatum();
				// Erstellungsdatum

				auftraege[zeile][4] = "";
				if (db.getAuftragsListe().get(i).getFrist() != null)
					auftraege[zeile][4] = db.getAuftragsListe().get(i).getFrist();
				// Frist

				auftraege[zeile][5] = "";
				if (db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer() != null)
					auftraege[zeile][5] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
				zeile++;
				// KundenNummer
			}
		}
		zeilenTabelle = 0;
		return auftraege;
	}

	private void tabelleFormat() {
		// Details
		tabelle.getColumnModel().getColumn(0).setPreferredWidth(150);
		tabelle.getColumnModel().getColumn(0).setMinWidth(150);
		tabelle.getColumnModel().getColumn(0).setMaxWidth(150);

		// AuftragsNummer
		tabelle.getColumnModel().getColumn(1).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(1).setMinWidth(200);
		tabelle.getColumnModel().getColumn(1).setMaxWidth(250);

		// Status
		tabelle.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(2).setMinWidth(150);
		tabelle.getColumnModel().getColumn(2).setMaxWidth(250);

		// Erstellungsdatum
		tabelle.getColumnModel().getColumn(3).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(3).setMinWidth(200);
		tabelle.getColumnModel().getColumn(3).setMaxWidth(200);

		// Frist
		tabelle.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(2).setMinWidth(200);
		tabelle.getColumnModel().getColumn(2).setMaxWidth(200);

		// Auftraggeber
		tabelle.getColumnModel().getColumn(3).setPreferredWidth(100);
		tabelle.getColumnModel().getColumn(3).setMinWidth(200);
		tabelle.getColumnModel().getColumn(3).setMaxWidth(200);

		// Zeilenhöhe
		tabelle.setRowHeight(50);

		tabelle.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben
	}

	/**
	 * Buttons in der Tabelle
	 */

	class JButtonRenderer implements TableCellRenderer {
		JButton button = new JButton();

		// Wie soll der Button ausehen und was soll drin stehen
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
					Auftrag auftrag = welcherAuftrag(tabelle.getEditingRow());
					// sucht den Auftrag bei dem auf den Button gklickt wurde

					DetailsFenster frame = new DetailsFenster(auftrag);
					frame.setVisible(true);
					// öffnet Detailsfenster

					tabelleAktualisieren(monteur);
					// Tabelle wird neu geladen, damit der Button wieder erscheint
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			// Button wird nach klick wieder beschriftet
			button.setText("Details anzeigen");
			return button;
		}
	}

	/**
	 * funktionale Hilfsmethoden.
	 */
	private boolean richtigerAuftrag(int i) {
		/*
		 * überprüft ob der Monteur der gleiche ist wie der in der AuftragsListe an der
		 * Stelle i
		 */
		return monteur.getMitarbeiterNummer()
				.equals(db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer());
	}

	private int summeAuftraege(Mitarbeiter monteur) {
		// zählt die Aufträge für die der Monteur Zuständig ist

		int summe = 0;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer().equals(
					monteur.getMitarbeiterNummer()) && !db.getAuftragsListe().get(j).getStatus().equals("im Lager")) {
				// Zuständiger Monteur = Monteur in der MonteurListe?
				// Aufträge, welche bereits abgeschlossen/ im Lager sind, zählen nicht mehr in
				// die Auftragssumme des einzelnen Monteurs

				summe++;
			}
		}
		return summe;
	}

	private Auftrag welcherAuftrag(int editingRow) {
		for (Auftrag auftrag : db.getAuftragsListe()) {

			if (tabelle.getValueAt(editingRow, 1).equals(auftrag.getAuftragsNummer())) {
				return auftrag;
			}
		}
		return null;
	}
}