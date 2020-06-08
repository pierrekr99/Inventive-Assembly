package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Datenbank.datenbankVerbindung;
import objekte.Auftrag;
import objekte.Mitarbeiter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DisponentFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();
	// ermöglicht auf den Inhalt der DB, die in der Main geladen wurde

	private JPanel contentPane;
	private JTextField txtSuche;
	private JTable auftraegeTbl;
	private JTable monteureTbl;
	public int indexWochentag = 0;

	Object[][] auftraege;
	int zeilen = 0;
	int zeilenMonteure = 0;
	int summeAuftraege = 0;
	String details = "Details anzeigen";
	String monteur;

	JComboBox monteureCombobox = new JComboBox();
	JComboBox DatumCBox;
	TableColumn monteureColumn;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { DisponentFenster frame = new
	 * DisponentFenster(); frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	/**
	 * Create the frame.
	 */
	public DisponentFenster() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1047, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtSuche = new JTextField();
		txtSuche.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSuche.setText("Suche");
		txtSuche.setColumns(10);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton logoutKnopf = new JButton("Logout");// Logout schließt das fenster und Öffnet das LoginFenster
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		logoutKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFenster login = new LoginFenster();
				login.setVisible(true);
				login.setLocationRelativeTo(null);
				dispose();
			}
		});

		JButton dbAktualisierenKnopf = new JButton("Aktualisieren");

		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dbAktualisierenKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				statusAktualisieren();
				/*
				 * Jeder Status wird bei Knopfdruck überprüft (alle Verfügbarkeiten der Teile
				 * werden überprüft) und ggf. überschrieben
				 */

				monteureInArrayEinlesen();
				/*
				 * die aktuelle Tabelle wird in db.getAuftragsListe() eingelesen, dieser wird
				 * ggf. ein neuer Monteur zugewiesen (stimmt dann wieder mit der Tabelle ein)
				 */

				auftraegeAktualisieren();
				/*
				 * Tabelle wird graphisch aktualisiert, Mitarbeiternummer wird bei Austausch des
				 * Monteurs automatisch mitüberschrieben, auch der Status wird überprüft
				 */

				monteureAktualisieren();
				/*
				 * Tabelle wird graphisch aktualisiert, die Summe der Aufträge eines Monteurs
				 * passt sich an die neuen Zahlen an
				 */

			}
		});

//		gebeDatumAus();
		// Befüllt die datumComboBox

		DatumCBox = new JComboBox(gebeDatumAus());

		DatumCBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		DatumCBox.setSelectedIndex(0);

		DatumCBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String ausgewaeltesDatum = (String) DatumCBox.getSelectedItem();
				// liest Datum als String aus

				String[] ausgewaelterWochentag = ausgewaeltesDatum.split(",");
				// wochentag und datum wird getrennt

				String s = ausgewaelterWochentag[0];
				// nur der wochentag wird in s gespeichert

				switch (s) {
				/*
				 * index für wochentag. wird benötigt um verfügbarkeit der monteure aufrufen zu
				 * können
				 */

				case "Montag":
					indexWochentag = 0;
					break;
				case "Dienstag":
					indexWochentag = 1;
					break;
				case "Mittwoch":
					indexWochentag = 2;
					break;
				case "Donnerstag":
					indexWochentag = 3;
					break;
				case "Freitag":
					indexWochentag = 4;
					break;
				case "Samstag":
					indexWochentag = 5;
					break;
				case "Sonntag":
					indexWochentag = 6;
					break;
				default:
					indexWochentag = 6;
					break;
				}
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
						.createParallelGroup(
								Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 396, Short.MAX_VALUE)
								.addComponent(DatumCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(DatumCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(dbAktualisierenKnopf).addGap(18).addComponent(logoutKnopf))
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE)).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(logoutKnopf).addComponent(dbAktualisierenKnopf).addComponent(DatumCBox,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addComponent(DatumCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE).addGap(6)));

		/**
		 * Auftraege Reiter.==================================================
		 */
		JScrollPane auftraegeSp = new JScrollPane();
		tabbedPane.addTab("Aufträge", null, auftraegeSp, null);

		auftraegeTbl = new JTable();
		auftraegeSp.setViewportView(auftraegeTbl);
		auftraegeTbl.setCellSelectionEnabled(true);
		// Einzelne Zellen können ausgewählt werden

		auftraegeTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -größe in der Tabelle

		auftraegeTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -größe in der Kopfzeile der Tabelle

		auftraegeAktualisieren();
		// Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		auftraegeTbl.setAutoCreateRowSorter(true);
		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Aufträge
		 * nach diesem Attribut in der natürlichen Ordnung und umgekehrt sortiert
		 */

		monteureCombobox();
		// Befüllt die monteureCombobox

		/**
		 * Monteure Reiter.==================================================
		 */
		JScrollPane monteureSp = new JScrollPane();
		tabbedPane.addTab("Monteure", null, monteureSp, null);

		monteureTbl = new JTable();
		monteureSp.setViewportView(monteureTbl);
		monteureTbl.setCellSelectionEnabled(true);
		// Einzelne Zellen können ausgewählt werden

		monteureTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -größe in der Tabelle

		monteureTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -größe in der Kopfzeile der Tabelle

		monteureAktualisieren();
		// Erstellen/aktualisieren der Monteurtabelle -> mehr Details in der Methode

		monteureTblFormat();
		// Monteure Tabelle wird formatiert

		auftraegeTblFormat();
		// Aufträge Tabelle wird formatiert

		monteureTbl.setAutoCreateRowSorter(true);
		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Monteure
		 * nach diesem Attribut in der natürlichen Ordnung und umgekehrt sortiert
		 */

		contentPane.setLayout(gl_contentPane);
		// Group-Layout im contentPane wird festgelegt

	}

	/**
	 * GUI-Hilfsmethoden.==================================================
	 */
	private void auftraegeAktualisieren() {

		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), new String[] { "", "AuftragsNummer", "Status",
				"Erstellungsdatum", "Frist", "MonteurName", "MonteurNummer", "Auftragsgeber" }) {

			boolean[] columnEditables = new boolean[] { true, false, false, false, false, true, false, false };
			// welche spalten lassen sich ändern
		});

		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellRenderer(new JButtonRenderer("auftraegeTbl"));
		// ButtonRenderer wird in Spalte 0 ausgeführt

		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellEditor(new JButtonEditor());
		// ButtonEditorwird in Spalte 0 ausgeführt

		auftraegeTblFormat();
		// Tabelle wird formatiert

		monteureCombobox();
		// monteureCombobox wird konfiguriert (muss bei jeder Aktualisierung geschehen)
	}

	private void monteureAktualisieren() {

		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		monteureTbl.setModel(new DefaultTableModel(monteure(),
				new String[] { "Name", "MitarbeiterNummer", "Verfügbarkeit", "Auftraege" }) {

			boolean[] columnEditables = new boolean[] { false, false, false, true };
			// welche spalten lassen sich ändern
		});

		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellRenderer(new JButtonRenderer("monteureTbl"));
		// ButtonRenderer wird in Spalte 3 ausgeführt

		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellEditor(new JButtonEditor());
		// ButtonEditorwird in Spalte 3 ausgeführt

		monteureTblFormat();
		// Tabelle wird formatiert
	}

	public Object[][] auftraege() {
		// Erstellt Inhalt zur befüllung der auftraegeTabelle

		zeilen = db.getAuftragsListe().size();
		// größe der Tabelle wird ermittelt

		auftraege = new Object[zeilen][8];
		// dieses Array wird die Tabelle befüllen

		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			auftraege[i][0] = details;
			auftraege[i][1] = db.getAuftragsListe().get(i).getAuftragsNummer();
			// AuftragsNummer

			auftraege[i][2] = db.getAuftragsListe().get(i).getStatus();
			// Status

			auftraege[i][3] = db.getAuftragsListe().get(i).getErstellungsdatum();
			// Erstellungsdatum

			auftraege[i][4] = db.getAuftragsListe().get(i).getFrist();
			// Frist

			auftraege[i][5] = "";
			// MitarbeiterName (Name Vorname) wenn kein Monteur zugwiesen ist

			auftraege[i][6] = "";
			// MitarbeiterNummer wenn kein Monteur zugwiesen ist

			auftraege[i][7] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
			// Auftraggeber

			if (db.getAuftragsListe().get(i).getZustaendig() != null
					&& db.getAuftragsListe().get(i).getZustaendig() != null) {
				// ist ein Monteur zuständug?

				auftraege[i][5] = db.getAuftragsListe().get(i).getZustaendig().getName() + " "
						+ db.getAuftragsListe().get(i).getZustaendig().getVorname();
				// MitarbeiterName (Name Vorname)

				auftraege[i][6] = db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer();
				// MitarbeiterNummer
			}
		}
		return auftraege;
	}

	private Object[][] monteure() {
		// Erstellt Inhalt zur befüllung der monteureTabelle

		zeilenMonteure = db.getMonteurListe().size();
		// größe der Tabelle wird ermittelt

		Object[][] monteure = new Object[zeilenMonteure][4];
		// dieses Array wird die Tabelle befüllen

		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteure[i][0] = db.getMonteurListe().get(i).getName() + " " + db.getMonteurListe().get(i).getVorname();
			// MitarbeiterName (Name Vorname)

			monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();
			// MitarbeiterNummer

			if (indexWochentag <= 4) {
				// für Montag bis Freitag

				monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit().get(indexWochentag);
				// hier wird nur noch die Anwesenheit am jeweiligen Tag eingetragen

			} else {
				// Samstag und Sonntag wird die komplette Liste angezeigt
				monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
			}

			monteure[i][3] = "Aufträge anzeigen [" + summeAuftraege(db.getMonteurListe().get(i)) + "]";
			// Summe der Aufträge

		}
		return monteure;
	}

	private void auftraegeTblFormat() {
		// Details
		auftraegeTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		auftraegeTbl.getColumnModel().getColumn(0).setMinWidth(150);
		auftraegeTbl.getColumnModel().getColumn(0).setMaxWidth(150);

		// Auftragsnummer
		auftraegeTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(1).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(1).setMaxWidth(250);

		// Status
		auftraegeTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(2).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(2).setMaxWidth(200);

		// Erstellungsdatum
		auftraegeTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(3).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(3).setMaxWidth(250);

		// Frist
		auftraegeTbl.getColumnModel().getColumn(4).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(4).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(4).setMaxWidth(250);

		// MonteurName
		auftraegeTbl.getColumnModel().getColumn(5).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(5).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(5).setMaxWidth(500);

		// MonteurNummer
		auftraegeTbl.getColumnModel().getColumn(6).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(6).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(6).setMaxWidth(200);

		// Aufraggeber
		auftraegeTbl.getColumnModel().getColumn(7).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(7).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(7).setMaxWidth(200);

		// Zeilenhöhe
		auftraegeTbl.setRowHeight(50);

		// Nur lesen nicht schreiben
		auftraegeTbl.getTableHeader().setReorderingAllowed(false);
	}

	private void monteureTblFormat() {
		// Name
		monteureTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		monteureTbl.getColumnModel().getColumn(0).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(0).setMaxWidth(500);

		// MitarbeiterNummer
		monteureTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMaxWidth(400);

		// Verfügbarkeit
		monteureTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMaxWidth(500);

		// AuftragsNummer
		monteureTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMaxWidth(500);

		// Zeilenhöhe
		monteureTbl.setRowHeight(50);

		// Nur lesen nicht schreiben
		monteureTbl.getTableHeader().setReorderingAllowed(false);
	}

	private void monteureCombobox() {
		// Fügt Optionen zur Statusveränderung hinzu

		monteureCombobox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und Größe

		monteureColumn = auftraegeTbl.getColumnModel().getColumn(5);
		// eine bestimmte Spalte für Combobox auswählen

		monteureColumn.setCellEditor(new DefaultCellEditor(monteureCombobox));
		// in die Spalte die Combobox einbinden

		monteureCombobox.addActionListener(null);
		// zugewiesenen Monteur auslesen und in Datenbank zuweisung ändern

		monteureCombobox.removeAllItems();
		// monteureCombobox wird geleert vor befüllung

		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteureCombobox
					.addItem(db.getMonteurListe().get(i).getName() + " " + db.getMonteurListe().get(i).getVorname());
			// Name Vorname
		}
		Collections.sort(db.getMonteurListe(), new Comparator<Mitarbeiter>() {
			// sortiert die monteureCombobox

			@Override
			public int compare(Mitarbeiter o1, Mitarbeiter o2) {

				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	/**
	 * Buttons in der Tabelle.==================================================
	 */

	class JButtonRenderer implements TableCellRenderer {
		JButton button = new JButton();
		String tabelle;
		// in welcher Tabelle ist der Button

		public JButtonRenderer(String string) {
			this.tabelle = string;
		}

		// Wie soll der Button ausehen und was soll drin stehen
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			table.setShowGrid(true);
			table.setGridColor(Color.LIGHT_GRAY);
			button.setText(details);
			if (tabelle.equals("monteureTbl"))
				// wird ein knopf in der Monteur Tabelle gedrückt?

				button.setText("Aufträge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
			// Text im Button wird festgelegt und anzahl der Aufräge für die der Monteur
			// zuständig ist wird gezählt

			return button;
		}
	}

	class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
		JButton button;

		public JButtonEditor() {
			super();
			button = new JButton();
			button.setOpaque(true);
			// ist der Button undurchsichtig oder nicht

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// was passiert wenn man den Button anklickt

					if (button.getText().equals(details)) {
						// wird der deteilsButton gedrückt?

						Auftrag auftrag = welcherAuftrag(auftraegeTbl.getEditingRow());
						// welcher Auftrag wird in der Zeile des geklickten Buttons angezeigt

						if (auftrag != null) {
							// es existiert ein Auftrag in der Zeile

							DetailsFenster frame = new DetailsFenster(auftrag);
							frame.setVisible(true);
							// DetailsFenster wird geöffnet und der angezeigte Auftrag wird ihm mitgegeben

							auftraegeAktualisieren();
							// Tabelle wird neu geladen, damit der Button wieder erscheint
						}

					} else {
						Mitarbeiter monteur = welcherMonteur(monteureTbl.getEditingRow());
						if (summeAuftraege(monteur).equals("0")) {
							// ist der der Monteur für 0 Aufträge zuständig?

							JOptionPane nichtZugewiesen = new JOptionPane();
							nichtZugewiesen.showMessageDialog(null, "keine Aufträge zugewiesen");
							// Monteur ist für keinen Auftrag zuständig -> Warnung
						}

						// Monteur existiert und ist für mindestens einen Auftrag zuständig

						AuftraegeListeFenster frame = new AuftraegeListeFenster(monteur);
						frame.setVisible(true);
						// AuftraegeListeFenster und der Monteur

						monteureAktualisieren();
						// Tabelle wird neu geladen, damit der Button wieder erscheint
					}
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

			button.setText(details);
			if (table == monteureTbl) {
				// die übergebene Tabelle ist die MonteurTabelle

				button.setText("Aufträge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
				// Text im Button wird festgelegt und anzahl der Aufräge für die der Monteur
				// zuständig ist wird gezählt
			}
			return button;
		}
	}

	/**
	 * funktionale Hilfsmethoden.==================================================
	 */

	private String summeAuftraege(Mitarbeiter monteur) {
		// zählt die Aufträge für die der Monteur Zuständig ist

		String summe;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer()
					.equals(monteur.getMitarbeiterNummer())) {
				// Zuständiger Monteur = Monteur in der MonteurListe?

				summeAuftraege = summeAuftraege + 1;
			}
		}
		summe = "" + summeAuftraege;
		summeAuftraege = 0;
		// SummeAuftraege wird zurückgesetzt

		return summe;
	}

	private Auftrag welcherAuftrag(int editingRow) {
		for (Auftrag auftrag : db.getAuftragsListe()) {

			if (auftraegeTbl.getValueAt(editingRow, 1).equals(auftrag.getAuftragsNummer())) {
				return auftrag;
			}
		}
		return null;
	}

	private Mitarbeiter welcherMonteur(int editingRow) {
		for (Mitarbeiter monteur : db.getMonteurListe()) {

			if (monteureTbl.getValueAt(editingRow, 1).equals(monteur.getMitarbeiterNummer())) {
				return monteur;
			}
		}
		return null;
	}

	private void monteureInArrayEinlesen() {

		for (int i = 0; i < zeilen; i++) {// jede Tabellenzeile wird mit dieser for-Schleife durchlaufen

			for (Auftrag auftrag : db.getAuftragsListe()) {

				if (auftraegeTbl.getValueAt(i, 1).equals(auftrag.getAuftragsNummer())) {
					// vergleicht Auftragsnummer aus Tabellenzeile mit Auftragsnummer in der
					// db.getAuftragsliste();

					String ausgewaehlterMonteur = auftraegeTbl.getValueAt(i, 5).toString();
					// der in der Combobox ausgewählte Monteur wird in einen String umgewandelt und
					// die Combobox somit eliminiert

					String[] namentrennung = ausgewaehlterMonteur.split(" ");
					// Trennung in Vor- [1] und Nachname [0] des Monteurs

					if (!namentrennung[0].equals(auftrag.getZustaendig().getName())) {
						// vergleicht den zuständigen Monteur (anhand des Nachnamens) aus dem Auftrag
						// aus der Tabellenzeile mit dem
						// gleichen Auftrag aus der db.getAuftragsliste();

						for (Mitarbeiter monteur : db.getMonteurListe()) {

							if (monteur.getName().equals(namentrennung[0])) {
								// sollte der Monteur nicht übereinstimmen (er wurde also vom Diponenten
								// geändert), dann wird die db.getMonteurliste() mit einer for-each Schleife
								// durchlaufen und der Nachnam des Monteurs (aus der Tabelle) mit den Nachnamen
								// aus der Monteurliste verglichen

								auftrag.setZustaendig(monteur);
								// wenn in der Monteurliste der passende Monteur gefunden wurde (d.h. er stimmt
								// mit dem Monteur aus der Tabelle überein), dann wird der jeweilige Auftrag
								// überschrieben und bekommt den neuen Monteur zugewiesen (dies geschieht mit
								// dem Setter)

								db.setZustaendig(auftrag, monteur); // auftrag bekommt neuen Monteur zugewießen in der
																	// Datenbank

							}
							int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream()
									.filter((k) -> k.isVerfuegbarkeit()).count();
							// der gerade geänderte Auftrag wird nun auch nochmal auf seinen Status
							// überprüft. Hierfür werden die verfügbaren Komponenten gezählt (mittels eines
							// Streams)

							if (verfuegbareKomponenten == 5) {

								auftrag.setStatus("disponiert");
								// wenn die Anzahl der verfügbaren Komponenten genau 5 beträgt, sind alle
								// relevanten Bauteile vorhanden und der Status wird ggf. in der
								// db.getAuftragsliste() auf "disponiert" gesetzt (falls er noch auf "Teile
								// fehlen" gesetzt ist)
								// Anmerkung: Diese Methode ist auch nochmal als eigene Methode vorzufinden,
								// allerdings hat der Disponent hier die Möglichkeit, einen Auftrag, welcher
								// "aus Versehen" im Lager gelandet ist, wieder einem Monteur zuweisen und der
								// Auftragsstatus wird dann wieder geändert.

								db.setStatus(auftrag, "disponiert"); // Verändert den Status in der Datenbank

							} else if (verfuegbareKomponenten != 5) {
								auftrag.setStatus("Teile fehlen");
								// wenn die Anzahl der Teile kleiner als 5 ist, heißt das im Umkehrschluss, dass
								// mind. ein Teil nicht verfügbar ist und somit wird der Auftragsstatus auf
								// "Teile fehlen" gesetzt.

								db.setStatus(auftrag, "Teile fehlen"); // Verändert den Status in der Datenbank
							}
						}
					}
					;
				}

			}
		}
	}

	private void statusAktualisieren() {
		for (Auftrag auftrag : db.getAuftragsListe()) {
			int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream().filter((k) -> k.isVerfuegbarkeit())
					.count();
			// der gerade geänderte Auftrag wird nun auch nochmal auf seinen Status
			// überprüft. Hierfür werden die verfügbaren Komponenten gezählt (mittels eines
			// Streams)

			if (verfuegbareKomponenten == 5 && !auftrag.getStatus().equals("im Lager")) {
				auftrag.setStatus("disponiert");
				// wenn die Anzahl der verfügbaren Komponenten genau 5 beträgt (und der Auftrag
				// nicht "im Lager" ist), sind alle
				// relevanten Bauteile vorhanden und der Status wird ggf. in der
				// db.getAuftragsliste() auf "disponiert" gesetzt (falls er noch auf "Teile
				// fehlen" gesetzt ist)

				try {
					ResultSet rs;
					Statement stmt = db.getVerbindung().createStatement();

					stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'disponiert' WHERE (`AuftragsNummer` = '"
							+ auftrag.getAuftragsNummer() + "');");
					// die veränderten Werte werden von der ArrayList direkt in die DB übertragen

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (verfuegbareKomponenten != 5 && !auftrag.getStatus().equals("im Lager")) {
				auftrag.setStatus("Teile fehlen");
				// wenn die Anzahl der Teile kleiner als 5 ist (und der Auftrag nicht "im Lager"
				// ist, heißt das im Umkehrschluss, dass mind. ein Teil nicht verfügbar ist und
				// somit wird der Auftragsstatus auf "Teile fehlen" gesetzt.

				try {
					ResultSet rs;
					Statement stmt = db.getVerbindung().createStatement();

					stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'Teile fehlen' WHERE (`AuftragsNummer` = '"
							+ auftrag.getAuftragsNummer() + "');");
					// die veränderten Werte werden von der ArrayList direkt in die DB übertragen

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String[] gebeDatumAus() {
		// ComboBox um das Datum auwählen zu können

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		// EEEE steht für den Wochentag

		Calendar c = Calendar.getInstance();

		Date datum = new Date();

		String tag1 = datumAlsStringBekommen(datum);

		c.setTime(datum);
		c.add(Calendar.DATE, 1);
		datum = c.getTime();

		String tag2 = f.format(datum);

		c.setTime(datum);
		c.add(Calendar.DATE, 1);
		datum = c.getTime();

		String tag3 = f.format(datum);

		c.setTime(datum);
		c.add(Calendar.DATE, 1);
		datum = c.getTime();

		String tag4 = f.format(datum);

		c.setTime(datum);
		c.add(Calendar.DATE, 1);
		datum = c.getTime();

		String tag5 = f.format(datum);

		String[] Datum = { tag1, tag2, tag3, tag4, tag5 };

		return Datum;

	}

	private String datumAlsStringBekommen(Date date) {
		// gibt heutiges Datum zurück

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		// EEEE steht für den Wochentag
		return f.format(date);
	}

}
