package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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

public class DisponentFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();

	private JPanel contentPane;
	private JTextField txtSuche;
	private JTable auftraegeTbl;
	private JTable monteureTbl;

	Object[][] auftraege;
	int zeilen = 0;
	int zeilenMonteure = 0;
	int summeAuftraege = 0;
	String details = "Details anzeigen";// Hier könnte man den Detailsbutton Rendern
	String monteur;

	JComboBox monteureCombobox = new JComboBox(); // erstellung einer Combobox
	TableColumn monteureColumn;

//	private boolean sortiert = false;
//	private boolean sortiert1 = false;

	/**
	 * Launch the application.
	 */
/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisponentFenster frame = new DisponentFenster();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster
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
	public DisponentFenster() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 450);
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
				dispose();
			}
		});

		JButton dbAktualisierenKnopf = new JButton("DB aktualisieren");

		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dbAktualisierenKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabelleInArrayEinlesen(); // die aktuelle Tabelle wird in db.getAuftragsListe() eingelesen, diese wird
											// ggf. überschrieben
				auftraegeAktualisieren(); // Tabelle wird graphisch aktualisiert, Mitarbeiternummer wird bei Austausch
											// des Monteurs automatisch mitüberschrieben
				auftraegeTblFormat(); // Wiederherstellung der selben Ansicht

				monteureAktualisieren(); // Tabelle wird graphisch aktualisiert, die Summe der Aufträge eines Monteurs
											// passt sich an die neuen Zahlen an

				monteureTblFormat(); // Wiederherstellung derselben Ansicht

				// die Combobox muss auch neu erstellt werden, da die alte leider nicht die
				// Aktualisierung überlebt hat

				monteureCombobox();

				System.out.println("----------------------------juhu----------------------");
				db.getAuftragsListe().forEach(System.out::println);
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 620, Short.MAX_VALUE)
								.addComponent(dbAktualisierenKnopf).addGap(18).addComponent(logoutKnopf)))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(logoutKnopf).addComponent(dbAktualisierenKnopf))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE).addContainerGap()));

		/**
		 * Auftraege Reiter.
		 */
		JScrollPane auftraegeSp = new JScrollPane();
		tabbedPane.addTab("Aufträge", null, auftraegeSp, null);

		auftraegeTbl = new JTable();
		auftraegeSp.setViewportView(auftraegeTbl);
		auftraegeTbl.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		auftraegeTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));// Schriftart und -größe in der Tabelle
		auftraegeTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// Schriftart und -größe in der
																					// Kopfzeile der Tabelle

		auftraegeAktualisieren(); // Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		auftraegeTbl.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
													// Aufträge nach diesem Attribut
													// in der natürlichen Ordnung und umgekehrt sortiert

		monteureCombobox();

		/**
		 * Monteure Reiter.
		 */
		JScrollPane monteureSp = new JScrollPane();
		tabbedPane.addTab("Monteure", null, monteureSp, null);

		monteureTbl = new JTable();
		monteureSp.setViewportView(monteureTbl);
		monteureTbl.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		monteureTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));// Schriftart und -größe in der Tabelle
		monteureTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// Schriftart und -größe in der
																					// Kopfzeile der Tabelle

		monteureAktualisieren();// Erstellen/aktualisieren der Monteurtabelle -> mehr Details in der Methode

		monteureTblFormat();
		auftraegeTblFormat();
		monteureTbl.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
													// Monteure nach diesem Attribut
													// in der natürlichen Ordnung und umgekehrt sortiert

		contentPane.setLayout(gl_contentPane);

		/*
		 * monteureTbl.getTableHeader().addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent e) { int spalte =
		 * monteureTbl.columnAtPoint(e.getPoint());
		 * 
		 * if (spalte == 0) { if (!sortiert) {
		 * monteureTbl.setAutoCreateRowSorter(false); db.getMonteurListe().sort((o1, o2)
		 * -> { return o1.getName() .compareTo(o2.getName()); });
		 * 
		 * sortiert = true; } else if (sortiert) {
		 * monteureTbl.setAutoCreateRowSorter(false); db.getMonteurListe().sort((o1, o2)
		 * -> { return o1.getName() .compareTo(o2.getName()) * -1; }); sortiert = false;
		 * }
		 * 
		 * 
		 * monteureAktualisieren(); monteureTbl.setAutoCreateRowSorter(true);
		 * 
		 * }
		 * 
		 * } });
		 * 
		 * auftraegeTbl.getTableHeader().addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent e) { int spalte1 =
		 * auftraegeTbl.columnAtPoint(e.getPoint());
		 * 
		 * if (spalte1 == 5) { if (!sortiert1) {
		 * auftraegeTbl.setAutoCreateRowSorter(false); db.getAuftragsListe().sort((o1,
		 * o2) -> { return o1.getZustaendig().getName()
		 * .compareTo(o2.getZustaendig().getName()); });
		 * 
		 * sortiert1 = true; } else if (sortiert1) {
		 * auftraegeTbl.setAutoCreateRowSorter(false); db.getAuftragsListe().sort((o1,
		 * o2) -> { return o1.getZustaendig().getName()
		 * .compareTo(o2.getZustaendig().getName()); }); sortiert1 = false; }
		 * 
		 * 
		 * monteureAktualisieren(); auftraegeTbl.setAutoCreateRowSorter(true);
		 * 
		 * }
		 * 
		 * } });
		 */

		auftraegeTbl.getModel().addTableModelListener(new TableModelListener() { // test Pierre

			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub

				int row = e.getFirstRow();

				System.out.println(e.getFirstRow());

				System.out.println(auftraegeTbl.getValueAt(row, 5));
				System.out.println(auftraegeTbl.getValueAt(row, 1));

			}

		});
		/*
		 * auftraegeTbl.addMouseListener(new MouseAdapter() {// MouseListener für das
		 * Fenster public void mouseClicked(MouseEvent e) { if (e.MOUSE_PRESSED == 501)
		 * {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht //
		 * dazu) JTable target = (JTable) e.getSource(); int row =
		 * target.getSelectedRow();// wo wurde geklickt int column =
		 * target.getSelectedColumn(); // do some action if appropriate column if
		 * (column == 0) {// wenn in DetailsSpalte // detailsFenster();//Detailsfenster
		 * wird geöffnet DetailsFenster frame = new DetailsFenster(row); // reihe des
		 * Auftrags wird übergeben um details // aufrufen zu können
		 * frame.setVisible(true); } } } });
		 */
		/*
		 * monteureTbl.addMouseListener(new MouseAdapter() {// MouseListener für das
		 * Fenster public void mouseClicked(MouseEvent e) { if (e.MOUSE_PRESSED == 501)
		 * {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht //
		 * dazu) JTable target = (JTable) e.getSource(); int row =
		 * target.getSelectedRow();// wo wurde geklickt int column =
		 * target.getSelectedColumn(); // do some action if appropriate column if
		 * (column == 3) {// wenn in DetailsSpalte // detailsFenster();//Detailsfenster
		 * wird geöffnet AuftraegeListeFenster frame = new AuftraegeListeFenster(row);
		 * // reihe des Auftrags wird // übergeben um details aufrufen // zu können
		 * frame.setVisible(true); } } } });
		 */
	}

	/**
	 * Hilfsmethoden
	 */

	private void monteureTblFormat() {
		monteureTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		monteureTbl.getColumnModel().getColumn(0).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(0).setMaxWidth(500);

		monteureTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMaxWidth(400);

		monteureTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMaxWidth(500);

		monteureTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMaxWidth(500);

		monteureTbl.setRowHeight(50);
	}

	private void auftraegeTblFormat() {
		auftraegeTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		auftraegeTbl.getColumnModel().getColumn(0).setMinWidth(150);
		auftraegeTbl.getColumnModel().getColumn(0).setMaxWidth(150);

		auftraegeTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(1).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(1).setMaxWidth(250);

		auftraegeTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(2).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(2).setMaxWidth(200);

		auftraegeTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(3).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(3).setMaxWidth(250);

		auftraegeTbl.getColumnModel().getColumn(4).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(4).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(4).setMaxWidth(250);

		auftraegeTbl.getColumnModel().getColumn(5).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(5).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(5).setMaxWidth(500);

		auftraegeTbl.getColumnModel().getColumn(6).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(6).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(6).setMaxWidth(200);

		auftraegeTbl.getColumnModel().getColumn(7).setPreferredWidth(100);
		auftraegeTbl.getColumnModel().getColumn(7).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(7).setMaxWidth(200);

		auftraegeTbl.setRowHeight(50);
	}

	private String summeAuftraege(int i) {// zählt die zugehörigen Aufträge des Monteurs
		String summe;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer()
					.equals(db.getMonteurListe().get(i).getMitarbeiterNummer())) {
				/*
				 * Hier wird die MitarbeiterNummer des Zuständigen Mitarbeiter in einem Auftrag
				 * mit der Mitarbeiter einse Mitarbeiters aus Der Datenbank Verglichen und wenn
				 * diese Übereinstimmen wird Hochgezählt.
				 */
				summeAuftraege = summeAuftraege + 1;
			}
		}
		summe = "" + summeAuftraege;
		summeAuftraege = 0;
		return summe;
	}

	private Object[][] monteure() {// Erstellt Tabelle mit Monteuren
		zeilenMonteure = db.getMonteurListe().size();
		Object[][] monteure = new Object[zeilenMonteure][4];// Nur das wird später eingelesen
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteure[i][0] = db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName();
			monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
			monteure[i][3] = "Aufträge anzeigen [" + summeAuftraege(i)+"]";// Dropdown fehlt noch

		}
		return monteure;
	}

	private void monteureCombobox() {// Fügt Optionen zur Statusveränderung hinzu

		monteureCombobox.setFont(new Font("Tahoma", Font.PLAIN, 18));

		monteureColumn = auftraegeTbl.getColumnModel().getColumn(5);// eine bestimmte Spalte für Combobox
		// auswählen
		monteureColumn.setCellEditor(new DefaultCellEditor(monteureCombobox));// in die Spalte die Combobox einbinden

		monteureCombobox.addActionListener(null
// zugewiesenen Monteur auslesen und in Datenbank zuweisung ändern
		);

		monteureCombobox.removeAllItems();
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteureCombobox
					.addItem(db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName());
		}
	}

	public Object[][] auftraege() {// Aufträge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		zeilen = db.getAuftragsListe().size();
		auftraege = new Object[zeilen][8];// Nur das wird später eingelesen
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			auftraege[i][0] = details;
			auftraege[i][1] = db.getAuftragsListe().get(i).getAuftragsNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			auftraege[i][2] = db.getAuftragsListe().get(i).getStatus();
			auftraege[i][3] = db.getAuftragsListe().get(i).getErstellungsdatum();
			auftraege[i][4] = db.getAuftragsListe().get(i).getFrist();
			auftraege[i][5] = "";
			auftraege[i][6] = "";
			auftraege[i][7] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
			if (db.getAuftragsListe().get(i).getZustaendig() != null) {
				auftraege[i][5] = db.getAuftragsListe().get(i).getZustaendig().getVorname() + " "
						+ db.getAuftragsListe().get(i).getZustaendig().getName();
				auftraege[i][6] = db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer();
			}
		}
		return auftraege;
	}

	private void auftraegeAktualisieren() {
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), // Benötigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingefügt
				new String[] { "", "AuftragsNummer", "Status", "Erstellungsdatum", "Frist", "MonteurName",
						"MonteurNummer", "Auftragsgeber" }) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
					true, false, false, false, false, true, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
				return columnEditables[column];
			}
		});
		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellRenderer(new JButtonRenderer("auftraegeTbl"));
		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellEditor(new JButtonEditor());
	}

	private void monteureAktualisieren() {
		monteureTbl.setModel(new DefaultTableModel(monteure(), // Benötigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingefügt
				new String[] { "Name", "MitarbeiterNummer", "Verfügbarkeit", "Auftraege"// welche spaltennamen
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
					false, false, false, true };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
				return columnEditables[column];
			}
		});
//		for (int i = 0; i < db.getMonteurListe().size(); i++) {
//
//			monteureTbl.setValueAt("Summe: " + summeAuftraege(i) + "         Details", i, 3);
//		}
		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellRenderer(new JButtonRenderer("monteureTbl"));//Button wird hinzugefügt
		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellEditor(new JButtonEditor());
	}

	/**
	 * Buttons in der Tabelle
	 */
	
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
			if (tabelle.equals("monteureTbl"))
				button.setText("Aufträge anzeigen [" + summeAuftraege(row)+"]");
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
					if (button.getText().equals(details)) {
						DetailsFenster frame = new DetailsFenster(auftraegeTbl.getEditingRow());
						frame.setVisible(true);
					} else {
						AuftraegeListeFenster frame = new AuftraegeListeFenster(monteureTbl.getEditingRow());
						frame.setVisible(true);
					}

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
			txt = value.toString();
			button.setText(txt);
			return button;
		}
	}

	/**
	 * Tabelle in Array Einlesen
	 */
	
	private void tabelleInArrayEinlesen() {
		for (int i = 0; i < zeilen; i++) {

			for (Auftrag auftrag : db.getAuftragsListe()) {

				if (auftraegeTbl.getValueAt(i, 1).equals(auftrag.getAuftragsNummer())) { // vergleicht Auftragsnummer
																							// aus
					// Tabellenzeile mit Aufträgen in der DB

					// monteureCombobox.getSelectedItem().toString();

					String ausgewaehlterMonteur = auftraegeTbl.getValueAt(i, 5).toString();
					String[] namentrennung = ausgewaehlterMonteur.split(" "); // Trennung in Vor und Nachname des
																				// Monteurs

					System.out.println(namentrennung[0]);
					System.out.println(namentrennung[1]);

					if (!namentrennung[1].equals(auftrag.getZustaendig().getName())) {
						// vergleicht den zuständigen Monteur aus dem Auftrag aus der Tabelle mit dem
						// gleichen Auftrag aus der DB, bei Unstimmigkeit wird neuer String erstellt.

						for (Mitarbeiter monteur : db.getMonteurListe()) {

							if (monteur.getName().equals(namentrennung[1])) { // ermitteln der Monteurdaten durch
																				// Namensabgleich in Monteurliste
								auftrag.setZustaendig(monteur); // neuer zuständiger Monteur wird eingetragen

								try {
									ResultSet rs;
									Statement stmt = db.getVerbindung().createStatement();

									stmt.executeUpdate("UPDATE `auftrag` SET `ZustaendigName` = '" + monteur.getName()
											+ "', `ZustaendigMitarbeiterNummer` = '" + monteur.getMitarbeiterNummer()
											+ "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");

								} catch (Exception e) {
									e.printStackTrace();
								}

								int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream()
										.filter((k) -> k.isVerfuegbarkeit()).count(); // überprüfen, ob alle Komponenten
																						// des Auftrags verfügbar sind

								if (verfuegbareKomponenten == 5) {
									auftrag.setStatus("disponiert"); // falls ja. wird der Status in disponiert geändert

									try {
										ResultSet rs;
										Statement stmt = db.getVerbindung().createStatement();

										stmt.executeUpdate(
												"UPDATE `auftrag` SET `Status` = 'disponiert' WHERE (`AuftragsNummer` = '"
														+ auftrag.getAuftragsNummer() + "');");

									} catch (Exception e) {
										e.printStackTrace();
									}

								}

							} else {
								auftrag.setStatus("Teile fehlen"); // falls nein, wird der Status in Teile fehlen
								// geändert
								try {
									ResultSet rs;
									Statement stmt = db.getVerbindung().createStatement();

									stmt.executeUpdate(
											"UPDATE `auftrag` SET `Status` = 'Teile fehlen' WHERE (`AuftragsNummer` = '"
													+ auftrag.getAuftragsNummer() + "');");

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					;
				}

			}
		}
	}

}
