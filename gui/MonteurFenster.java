package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.RowSorter;

import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Datenbank.datenbankVerbindung;
import gui.AuftraegeListeFenster.JButtonEditor;
import gui.AuftraegeListeFenster.JButtonRenderer;
import objekte.Auftrag;
import objekte.Auftraggeber;
import objekte.Mitarbeiter;

public class MonteurFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb(); // Datenbankverbindung aus der Main

	private JTextField suchFeld;
	private JTable auftraegeMonteurTBL;
	private JPanel contentPane;
	private JLabel lblDatum; 

	int zeilen = 0; // zeilen in der Auftragstabelle

	String details = "Details anzeigen";// Hier wird der Detailsbutton gerendert

	JComboBox auswahlBoxStatus = new JComboBox(); // Combobox zur Status�nderung

	private ArrayList<Auftrag> angepassteAuftragsListe = new ArrayList<Auftrag>(); // Arrayliste f�r Auftr�ge des sich
																					// anmeldenden Monteurs

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { MonteurFenster window = new
	 * MonteurFenster();
	 * 
	 * window.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */
	/**
	 * Create the application.
	 */
	public MonteurFenster() {

		setTitle("Inventive Assembly Monteur Auftragsansicht");// Namen des Fensters setzen
		setExtendedState(JFrame.MAXIMIZED_BOTH);// Gro�ansicht
		setBounds(0, 0, 700, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// fenster schlie�en bei x
		GridBagLayout gridBagLayout = new GridBagLayout();// layout von hier...
		gridBagLayout.columnWidths = new int[] { 0, 0 };//
		gridBagLayout.rowHeights = new int[] { 362, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };//
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };//
		getContentPane().setLayout(gridBagLayout);// ...bis hier

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);// layout f�r tabbed pane von hier...
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Schriftgr��e
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);// ...bis hier

		JPanel auftraegeTab = new JPanel();
		tabbedPane.addTab("Auftr�ge", null, auftraegeTab, null);// tab wird sichtbar

		suchFeld = new JTextField(); // Suchfeld erstellen
		suchFeld.setFont(new Font("Tahoma", Font.PLAIN, 16));// Formatierung der Schrift
		suchFeld.setText("Suche");// Suchfeld name
		suchFeld.setColumns(10);

		JButton logoutKnopf = new JButton("Logout");
		// logout button erstellen
		
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// Formatierung der Schrift
		
		logoutKnopf.addActionListener(new ActionListener() { 
			// was passiert, wenn der Knopf gedr�ckt wird

			@Override
			public void actionPerformed(ActionEvent e) {// logout befehl...zur�ck zum login

				LoginFenster login = new LoginFenster();// loginfenster erstellen
				login.setVisible(true);
				login.setLocationRelativeTo(null);
				dispose();// aktuelles Fenster schlie�en

			}
		});

		JButton dbAktualisierenKnopf = new JButton("Aktualisieren");// "DB aktualisieren" button wird erstellt
		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));// formatierung schrift
		dbAktualisierenKnopf.addActionListener(new ActionListener() {// was passiert wenn der aktualisieren Knopf
																		// gedr�ckt wird
			public void actionPerformed(ActionEvent e) {
				tabelleInArrayEinlesen(); // Hilfsmethoden werden ausgef�hrt
				auftraegeAktualisieren();
				auswahlBoxStatus(auftraegeMonteurTBL, auswahlBoxStatus, 2);

			}
		});

		/**
		 * layout regelungen f�r scroll pane und anordnung der komponenten (vom
		 * windowbuilder
		 * erstellt)********************************************************
		 * ************************************************************************************************
		 */

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JLabel lbl_eingeloggterMonteur = new JLabel("Nachname Vorname");
		String mitarbeiternummer = LoginFenster.getMitarbeiternummer();
		for (Mitarbeiter monteur : db.getMonteurListe()) {
			if (monteur.getMitarbeiterNummer().equals(mitarbeiternummer)) {
				lbl_eingeloggterMonteur.setText(monteur.getName() + ", " + monteur.getVorname());
			}
		}
		lbl_eingeloggterMonteur.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy"); // Datumsformat
		lblDatum = new JLabel(f.format(new Date())); // Heutigen Tag wird �bergeben
		lblDatum.setFont(new Font("Tahoma", Font.PLAIN, 16));

		GroupLayout gl_auftraegeTab = new GroupLayout(auftraegeTab);
		gl_auftraegeTab.setHorizontalGroup(
			gl_auftraegeTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_auftraegeTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_auftraegeTab.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
						.addGroup(gl_auftraegeTab.createSequentialGroup()
							.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lbl_eingeloggterMonteur, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
							.addComponent(lblDatum)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dbAktualisierenKnopf)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(logoutKnopf)))
					.addContainerGap())
		);
		gl_auftraegeTab.setVerticalGroup(
			gl_auftraegeTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_auftraegeTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_auftraegeTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(logoutKnopf)
						.addComponent(dbAktualisierenKnopf)
						.addComponent(lbl_eingeloggterMonteur)
						.addComponent(lblDatum))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
					.addContainerGap())
		);

		/**
		 * ***********************************************************************************************
		 * ************************************************************************************************
		 */

		auftraegeMonteurTBL = new JTable();// tabelle erstellen
		auftraegeMonteurTBL.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// Formatierung Schrift Kopf
		auftraegeAktualisieren(); // Tabelle einlesen mit Hilfsmethode

		auftraegeMonteurTBL.setRowHeight(50); // Zeilen h�he
		sortierenMonteurTbl(); // durch Anklicken der Kopfzeile (in der jeweiligen Spalte)
								// werden die Auftr�ge nach diesem Attribut
								// in der nat�rlichen Ordnung und umgekehrt sortiert

		auftraegeMonteurTBL.setFont(new Font("Tahoma", Font.PLAIN, 18));// Formatierung der Schrift in der Tabelle
		
		scrollPane.setViewportView(auftraegeMonteurTBL);
		auftraegeTab.setLayout(gl_auftraegeTab);

	}

	/*
	 * Hilfsmethode: Auftraege aktualisieren - Tabelle wird
	 * erstellt********************************************
	 ***********************************************************************************************************
	 */

	private void suchen(JTable table) {
		TableModel modelArchiv = table.getModel();
		
		TableRowSorter<TableModel> sorter1 = new TableRowSorter<>(modelArchiv);
		table.setRowSorter(sorter1);
		suchFeld.getDocument().addDocumentListener(new DocumentListener() {
	         @Override
	         public void insertUpdate(DocumentEvent e) {
	            search(suchFeld.getText());
	         }
	         @Override
	         public void removeUpdate(DocumentEvent e) {
	            search(suchFeld.getText());
	         }
	         @Override
	         public void changedUpdate(DocumentEvent e) {
	            search(suchFeld.getText());
	         }
	         public void search(String str) {
	            if (str.length() == 0) {
	               sorter1.setRowFilter(null);
	            } else {
	               sorter1.setRowFilter(RowFilter.regexFilter(str));
	            }
	         }
	      });
		}
	
	private void sortierenMonteurTbl() {
		// ein neuer RowSorter wird erstellt, durch Anklicken des TableHeaders wird
		// Index geliefert, anschlie�end kann mit diesem nach der nat�rlichen Ordnung
		// bzw. einen Comparator sortiert werden
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
				(DefaultTableModel) auftraegeMonteurTBL.getModel());
		auftraegeMonteurTBL.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>(); //

		int columnIndexForAuftragsNummer = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexForAuftragsNummer, SortOrder.ASCENDING));

		int columnIndexForStatus = 2;
		sortKeys.add(new RowSorter.SortKey(columnIndexForStatus, SortOrder.ASCENDING));

		int columnIndexToSortDatum = 4;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum, SortOrder.ASCENDING)); // beschreibt die
																							// Sortierreihenfolge in
																							// einer
																							// Spalte �ber ColumnIndex

		int columnIndexToSortDatum1 = 3;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum1, SortOrder.ASCENDING)); // beschreibt die
																							// Sortierreihenfolge in
																							// einer
																							// Spalte �ber ColumnIndex

		int columnIndexForKundenNummer = 5;
		sortKeys.add(new RowSorter.SortKey(columnIndexForKundenNummer, SortOrder.ASCENDING));

		sorter.setComparator(columnIndexToSortDatum, ((String datum1, String datum2) -> { // Erzeugen eines
																							// Comparators,der
																							// ausgew�hlte Spalte
																							// sortiert
			String[] datumGetrennt1 = datum1.split("\\."); // Datum-String wird in 3 Teile geteilt
			String[] datumGetrennt2 = datum2.split("\\.");
			if (datumGetrennt1.length != datumGetrennt2.length) // Daten werden miteinander verglichen, ob sie die Selbe
																// L�nge besitzen
				throw new ClassCastException();
			String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0]; // Datum wird
																										// zusammengesetzt
			String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];

			return datumZusammengesetzt1.compareTo(datumZusammengesetzt2); // Ordnen der Daten �ber CompareTo-Methode

		}));

		sorter.setComparator(columnIndexToSortDatum1, ((String datum1, String datum2) -> {
			String[] datumGetrennt1 = datum1.split("\\.");
			String[] datumGetrennt2 = datum2.split("\\.");
			if (datumGetrennt1.length != datumGetrennt2.length)
				throw new ClassCastException();
			String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
			String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];

			return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);

		}));

		sorter.setSortKeys(sortKeys);
		sorter.sort();
	}

	private void auftraegeAktualisieren() {
		auftraegeMonteurTBL.setModel(new DefaultTableModel(// Bef�llung der Tabelle
				auftraege(), // Methode wird aufgerufen und liest jetzt die Tabelle ein

//				,
				new String[] { "", "Auftragsnummer", "Status", "Erstellungsdatum", "Frist", "Auftraggeber"// welche
																											// spaltennamen
				// gibt es
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					true, false, true, false, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}

		});
		auftraegeMonteurTBL.getTableHeader().setReorderingAllowed(false);// Tabellenspalten lassen sich nicht
																			// verschieben
		auswahlBoxStatus(auftraegeMonteurTBL, auswahlBoxStatus, 2); // Combobox in Spalte Status erstellt
		auftraegeMonteurTBL.getColumn(auftraegeMonteurTBL.getColumnName(0))
				.setCellRenderer(new JButtonRenderer("auftraegeMonteurTBL"));
		auftraegeMonteurTBL.getColumn(auftraegeMonteurTBL.getColumnName(0)).setCellEditor(new JButtonEditor());// Button
																												// Details
																												// erstellen
		suchen(auftraegeMonteurTBL);
	}

	/*
	 * *****************************************************************************
	 * *************************************
	 * *****************************************************************************
	 * *************************************
	 */

	/**
	 * Hilfsmethoden: Die Methode zum F�llen der
	 * Tabelle**********************************************
	 *************************************************************************************************
	 */
	private Object[][] auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		db.getAuftragsListe().clear(); // alte liste l�schen
		db.auftragEinlesen();// neu einlesen mit aktuellen daten
		angepassteAuftragsListe.clear(); // angepassteAuftragsliste wird auch geleert

		// angepassteAuftragsListe wird bef�llt. Dabei werden mit Hilfe eines Filters,
		// diejenigen Auftr�ge ausgelesen,
		// die mit der Mitarbeiternummer des tf_MitarbeiterID. Felds imLoginFensters
		// �bereinstimmen. Au�erdem werden nur
		// die Auftr�ge ausgew�hlt, bei denen der Status "disponiert" oder "Teile
		// fehlen" ist

		db.getAuftragsListe().stream()
				.filter((a) -> a.getStatus().equals("disponiert") || a.getStatus().equals("Teile fehlen"))
				.filter((a) -> a.getZustaendig().getMitarbeiterNummer().equals(LoginFenster.getMitarbeiternummer()))

				.forEach(angepassteAuftragsListe::add);

		// Im Anschluss werden die Auftr�ge gez�hlt, damit die Zeilen der Tabelle
		// �bereinstimmen
		zeilen = (int) db.getAuftragsListe().stream()
				.filter((a) -> (a.getStatus().equals("disponiert") || a.getStatus().equals("Teile fehlen"))
						&& a.getZustaendig().getMitarbeiterNummer().equals(LoginFenster.getMitarbeiternummer()))

				.count();

		Object[][] auftraege = new Object[zeilen][6];// Struktur der Tabelle

		// einlesen der Tabelle aus der angepassten Auftragsliste
		for (int i = 0; i < zeilen; i++) {

			auftraege[i][0] = details;
			auftraege[i][1] = angepassteAuftragsListe.get(i).getAuftragsNummer();
			auftraege[i][2] = angepassteAuftragsListe.get(i).getStatus();
			auftraege[i][3] = angepassteAuftragsListe.get(i).getErstellungsdatum();
			auftraege[i][4] = angepassteAuftragsListe.get(i).getFrist();
			auftraege[i][5] = angepassteAuftragsListe.get(i).getAuftraggeber().getKundenNummer();

		}
		return auftraege;
	}

	/**
	 * Hilfsmethoden: Erstellen der Combobox, sowie Bef�llen und
	 * Funktionalit�t***************************
	 * ***************************************************************************************************
	 */
	private void auswahlBoxStatus(JTable table, JComboBox combobox, int spalte) { 

		combobox.removeAllItems();// erstmal alle rausl�schen

		// auswahlm�glichkeiten

		combobox.addItem("Teile fehlen");
		combobox.addItem("disponiert");
		combobox.addItem("im Lager");
		
		TableColumn statusSpalte = table.getColumnModel().getColumn(spalte);// in welche Spalte soll die
																			// Combobox
		statusSpalte.setCellEditor(new DefaultCellEditor(combobox));// Combobox jetzt anklickbar
	}

	/**
	 * *****************************************************************************
	 * Hilfsmethoden: Status�nderungen werden in die Datenbank eingetragen
	 **/

	private void tabelleInArrayEinlesen() {
		for (int i = 0; i < zeilen; i++) {
			// f�r jeden Auftrag der Tabelle abfragen ob der Status mit dem aus der
			// Auftragsliste �bereinstimmt

			for (Auftrag auftrag : db.getAuftragsListe()) {

				if (auftrag.getAuftragsNummer().equals(auftraegeMonteurTBL.getValueAt(i, 1))) {
					// die Auftragsnummer aus der Tabelle wird mit der db.getAuftragsliste()
					// verglichen

					String status = auftraegeMonteurTBL.getValueAt(i, 2).toString();
					// der ausgew�hlte Status aus der Combobox wird in einen String umgewandelt

					if (!auftrag.getStatus().equals(status)) {
						// dieser String wird nun mit dem Auftragsstatus aus der db.getAuftragslist()
						// verglichen

						auftrag.setStatus(status);
						// wenn ein Unterschied festgestellt wird, wird der Auftragsstatus aus der
						// ArrayList mit dem Status aus der Tabelle �berschrieben

						db.setStatus(auftrag, status); // nimmt �nderung in der DB vor

						if (auftrag.getStatus().equals("nicht zugewiesen")) {
							for (Mitarbeiter monteur : db.getMonteurListe()) {
								if (monteur.getMitarbeiterNummer().equals("0000")) {
									auftrag.setZustaendig(monteur);
									// wenn der Auftrag "nicht zugewiesen" ist, wird auch der jeweilige Monteur ggf.
									// von diesem Auftrag entfernt
									db.setZustaendig(auftrag, monteur); // nimmt �nderungen in der DB vor
								}
							}
						}
					}
				}
			}
		}

	}

	/************************************************************************************************************
	 * Hilfsklasse: Buttons in der Tabelle
	 **********************************************************************************************************/

	class JButtonRenderer implements TableCellRenderer {

		JButton button = new JButton();
		String tabelle;

		public JButtonRenderer(String string) {
			this.tabelle = string;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			table.setShowGrid(true);
			table.setGridColor(Color.LIGHT_GRAY);
			button.setText(details);
			return button;
		}
	}

	class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
		JButton button;

		public JButtonEditor() {
			super();
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DetailsFenster frame = new DetailsFenster(welcherAuftrag(auftraegeMonteurTBL.getEditingRow()));
					frame.setVisible(true);
					auftraegeAktualisieren();
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
			button.setText(details);
			return button;
		}
	}

	private Auftrag welcherAuftrag(int editingRow) {
		for (Auftrag auftrag : db.getAuftragsListe()) {

			if (auftraegeMonteurTBL.getValueAt(editingRow, 1).equals(auftrag.getAuftragsNummer())) {
				return auftrag;
			}
		}
		return null;
	}
}