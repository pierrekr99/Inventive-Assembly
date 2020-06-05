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
	String details = "Details anzeigen";// Hier k�nnte man den Detailsbutton Rendern
	String monteur;

	JComboBox monteureCombobox = new JComboBox(); // erstellung einer Combobox
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

		datumComboBox();

		JButton logoutKnopf = new JButton("Logout");// Logout schlie�t das fenster und �ffnet das LoginFenster
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		logoutKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFenster login = new LoginFenster();
				login.setVisible(true);
				dispose();
			}
		});

		JButton dbAktualisierenKnopf = new JButton("Aktualisieren");

		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dbAktualisierenKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				statusAktualisieren(); // Jeder Status wird bei Knopfdruck �berpr�ft (alle Verf�gbarkeiten der Teile werden �berpr�ft)  und ggf. �berschrieben
				monteureInArrayEinlesen(); // die aktuelle Tabelle wird in db.getAuftragsListe() eingelesen, dieser wird
											// ggf. ein neuer Monteur zugewiesen (stimmt dann wieder mit der Tabelle ein)

				
				auftraegeAktualisieren(); // Tabelle wird graphisch aktualisiert, Mitarbeiternummer wird bei Austausch
											// des Monteurs automatisch mit�berschrieben, auch der Status wird �berpr�ft

				monteureAktualisieren(); // Tabelle wird graphisch aktualisiert, die Summe der Auftr�ge eines Monteurs
											// passt sich an die neuen Zahlen an

			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
										.addComponent(DatumCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(dbAktualisierenKnopf).addGap(18)
										.addComponent(logoutKnopf))
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(logoutKnopf).addComponent(dbAktualisierenKnopf).addComponent(DatumCBox,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE).addGap(6)));

		/**
		 * Auftraege Reiter.
		 */
		JScrollPane auftraegeSp = new JScrollPane();
		tabbedPane.addTab("Auftr�ge", null, auftraegeSp, null);

		auftraegeTbl = new JTable();
		auftraegeSp.setViewportView(auftraegeTbl);
		auftraegeTbl.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		auftraegeTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));// Schriftart und -gr��e in der Tabelle
		auftraegeTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// Schriftart und -gr��e in der
																					// Kopfzeile der Tabelle

		auftraegeAktualisieren(); // Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		auftraegeTbl.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
													// Auftr�ge nach diesem Attribut
													// in der nat�rlichen Ordnung und umgekehrt sortiert

		monteureCombobox();

		/**
		 * Monteure Reiter.
		 */
		JScrollPane monteureSp = new JScrollPane();
		tabbedPane.addTab("Monteure", null, monteureSp, null);

		monteureTbl = new JTable();
		monteureSp.setViewportView(monteureTbl);
		monteureTbl.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		monteureTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));// Schriftart und -gr��e in der Tabelle
		monteureTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// Schriftart und -gr��e in der
																					// Kopfzeile der Tabelle

		monteureAktualisieren();// Erstellen/aktualisieren der Monteurtabelle -> mehr Details in der Methode

		monteureTblFormat();
		auftraegeTblFormat();
		monteureTbl.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
													// Monteure nach diesem Attribut
													// in der nat�rlichen Ordnung und umgekehrt sortiert

		contentPane.setLayout(gl_contentPane);

		
		/*
		 * auftraegeTbl.addMouseListener(new MouseAdapter() {// MouseListener f�r das
		 * Fenster public void mouseClicked(MouseEvent e) { if (e.MOUSE_PRESSED == 501)
		 * {// Wenn die Maus Gedr�ckt wird (Beim Dr�cken die Maus bewegen z�hlt nicht //
		 * dazu) JTable target = (JTable) e.getSource(); int row =
		 * target.getSelectedRow();// wo wurde geklickt int column =
		 * target.getSelectedColumn(); // do some action if appropriate column if
		 * (column == 0) {// wenn in DetailsSpalte // detailsFenster();//Detailsfenster
		 * wird ge�ffnet DetailsFenster frame = new DetailsFenster(row); // reihe des
		 * Auftrags wird �bergeben um details // aufrufen zu k�nnen
		 * frame.setVisible(true); } } } });
		 */
		/*
		 * monteureTbl.addMouseListener(new MouseAdapter() {// MouseListener f�r das
		 * Fenster public void mouseClicked(MouseEvent e) { if (e.MOUSE_PRESSED == 501)
		 * {// Wenn die Maus Gedr�ckt wird (Beim Dr�cken die Maus bewegen z�hlt nicht //
		 * dazu) JTable target = (JTable) e.getSource(); int row =
		 * target.getSelectedRow();// wo wurde geklickt int column =
		 * target.getSelectedColumn(); // do some action if appropriate column if
		 * (column == 3) {// wenn in DetailsSpalte // detailsFenster();//Detailsfenster
		 * wird ge�ffnet AuftraegeListeFenster frame = new AuftraegeListeFenster(row);
		 * // reihe des Auftrags wird // �bergeben um details aufrufen // zu k�nnen
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
		monteureTbl.getTableHeader().setReorderingAllowed(false);
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
		auftraegeTbl.getTableHeader().setReorderingAllowed(false);
	}

	private String summeAuftraege(Mitarbeiter monteur) {// z�hlt die zugeh�rigen Auftr�ge des Monteurs
		String summe;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer()
					.equals(monteur.getMitarbeiterNummer())) {
				/*
				 * Hier wird die MitarbeiterNummer des Zust�ndigen Mitarbeiter in einem Auftrag
				 * mit der Mitarbeiter einse Mitarbeiters aus Der Datenbank Verglichen und wenn
				 * diese �bereinstimmen wird Hochgez�hlt.
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
		Object[][] monteure = new Object[zeilenMonteure][4];// Nur das wird sp�ter eingelesen
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteure[i][0] = db.getMonteurListe().get(i).getName() + " " + db.getMonteurListe().get(i).getVorname();
			monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();// Auftragsliste.get(zeile).getAuftragsnr()

			if (indexWochentag <= 4) {// f�r Montag bis Freitag
				monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit().get(indexWochentag);
				// hier wird nur noch die Anwesenheit am jeweiligen Tag eingetragen
			} else { // Samstag und Sonntag wird die komplette Liste angezeigt
				monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
			}

			monteure[i][3] = "Auftr�ge anzeigen [" + summeAuftraege(db.getMonteurListe().get(i)) + "]";// Dropdown fehlt noch

		}
		return monteure;
	}

	private void monteureCombobox() {// F�gt Optionen zur Statusver�nderung hinzu

		monteureCombobox.setFont(new Font("Tahoma", Font.PLAIN, 18));

		monteureColumn = auftraegeTbl.getColumnModel().getColumn(5);// eine bestimmte Spalte f�r Combobox
		// ausw�hlen
		monteureColumn.setCellEditor(new DefaultCellEditor(monteureCombobox));// in die Spalte die Combobox einbinden

		monteureCombobox.addActionListener(null
// zugewiesenen Monteur auslesen und in Datenbank zuweisung �ndern
		);

		monteureCombobox.removeAllItems();
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteureCombobox
					.addItem(db.getMonteurListe().get(i).getName()+ " " + db.getMonteurListe().get(i).getVorname());
		}
		Collections.sort(db.getMonteurListe(), new Comparator<Mitarbeiter>() {



            @Override
            public int compare(Mitarbeiter o1, Mitarbeiter o2) {
                // TODO Auto-generated method stub
                return o1.getName().compareTo(o2.getName());


            }
        });
	}

	public Object[][] auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		zeilen = db.getAuftragsListe().size();
		auftraege = new Object[zeilen][8];// Nur das wird sp�ter eingelesen
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
				auftraege[i][5] = db.getAuftragsListe().get(i).getZustaendig().getName() + " "
						+ db.getAuftragsListe().get(i).getZustaendig().getVorname();
				auftraege[i][6] = db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer();
			}
		}
		return auftraege;
	}

	private void auftraegeAktualisieren() {
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "", "AuftragsNummer", "Status", "Erstellungsdatum", "Frist", "MonteurName",
						"MonteurNummer", "Auftragsgeber" }) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					true, false, false, false, false, true, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});
		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellRenderer(new JButtonRenderer("auftraegeTbl"));
		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellEditor(new JButtonEditor());
		auftraegeTblFormat();
		monteureCombobox(); // die Combobox muss auch neu erstellt werden, da die alte leider nicht die
		// Aktualisierung �berlebt hat
	}

	private void monteureAktualisieren() {
		monteureTbl.setModel(new DefaultTableModel(monteure(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "Name", "MitarbeiterNummer", "Verf�gbarkeit", "Auftraege"// welche spaltennamen
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					false, false, false, true };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});
//		for (int i = 0; i < db.getMonteurListe().size(); i++) {
//
//			monteureTbl.setValueAt("Summe: " + summeAuftraege(i) + "         Details", i, 3);
//		}
		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellRenderer(new JButtonRenderer("monteureTbl"));// Button
																												// wird
																												// hinzugef�gt
		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellEditor(new JButtonEditor());
		monteureTblFormat();
		monteureCombobox(); // die Combobox muss auch neu erstellt werden, da die alte leider nicht die
		// Aktualisierung �berlebt hat
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
				button.setText("Auftr�ge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
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
					if (button.getText().equals(details)) {
						DetailsFenster frame = new DetailsFenster(welcherAuftrag(auftraegeTbl.getEditingRow()));
						frame.setVisible(true);
						auftraegeAktualisieren();

					} else {
						AuftraegeListeFenster frame = new AuftraegeListeFenster(
								welcherMonteur(monteureTbl.getEditingRow()));
						frame.setVisible(true);
						monteureAktualisieren();
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
			button.setText(details);
			if (table.getRowCount()==monteureTbl.getRowCount()&&table.getValueAt(row, 1).equals(monteureTbl.getValueAt(row, 1))) {
				button.setText("Auftr�ge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
			}
			return button;
		}
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

	/**
	 * Tabelle in Array Einlesen
	 */

	private void monteureInArrayEinlesen() {

		for (int i = 0; i < zeilen; i++) {

			for (Auftrag auftrag : db.getAuftragsListe()) {

				if (auftraegeTbl.getValueAt(i, 1).equals(auftrag.getAuftragsNummer())) { // vergleicht Auftragsnummer
																							// aus
					// Tabellenzeile mit Auftr�gen in der DB

					// monteureCombobox.getSelectedItem().toString();

					String ausgewaehlterMonteur = auftraegeTbl.getValueAt(i, 5).toString();
					String[] namentrennung = ausgewaehlterMonteur.split(" "); // Trennung in Vor und Nachname des
																				// Monteurs


					if (!namentrennung[0].equals(auftrag.getZustaendig().getName())) {
						// vergleicht den zust�ndigen Monteur aus dem Auftrag aus der Tabelle mit dem
						// gleichen Auftrag aus der DB, bei Unstimmigkeit wird neuer String erstellt.

						for (Mitarbeiter monteur : db.getMonteurListe()) {

							if (monteur.getName().equals(namentrennung[0])) { // ermitteln der Monteurdaten durch
																				// Namensabgleich in Monteurliste
								auftrag.setZustaendig(monteur); // neuer zust�ndiger Monteur wird eingetragen

								try {
									ResultSet rs;
									Statement stmt = db.getVerbindung().createStatement();

									stmt.executeUpdate("UPDATE `auftrag` SET `ZustaendigName` = '" + monteur.getName()
											+ "', `ZustaendigMitarbeiterNummer` = '" + monteur.getMitarbeiterNummer()
											+ "' WHERE (`AuftragsNummer` = '" + auftrag.getAuftragsNummer() + "');");

								} catch (Exception e) {
									e.printStackTrace();
								}
							}//1
							// hier
							int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream().filter((k) -> k.isVerfuegbarkeit())
									.count(); // �berpr�fen, ob alle Komponenten
												// des Auftrags verf�gbar sind

							if (verfuegbareKomponenten == 5 ) {
								auftrag.setStatus("disponiert"); // falls ja. wird der Status in disponiert ge�ndert

								try {
									ResultSet rs;
									Statement stmt = db.getVerbindung().createStatement();

									stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'disponiert' WHERE (`AuftragsNummer` = '"
											+ auftrag.getAuftragsNummer() + "');");

								} catch (Exception e) {
									e.printStackTrace();
								}

							} else if (verfuegbareKomponenten != 5) {
								auftrag.setStatus("Teile fehlen"); // falls nein, wird der Status in Teile fehlen
								// ge�ndert
								try {
									ResultSet rs;
									Statement stmt = db.getVerbindung().createStatement();

									stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'Teile fehlen' WHERE (`AuftragsNummer` = '"
											+ auftrag.getAuftragsNummer() + "');");

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}//2
					}
					;
				}

			}
		}
	}

	private void statusAktualisieren() {
		for (Auftrag auftrag : db.getAuftragsListe()) {
			int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream().filter((k) -> k.isVerfuegbarkeit())
					.count(); // �berpr�fen, ob alle Komponenten
								// des Auftrags verf�gbar sind

			if (verfuegbareKomponenten == 5 && !auftrag.getStatus().equals("im Lager")) {
				auftrag.setStatus("disponiert"); // falls ja. wird der Status in disponiert ge�ndert

				try {
					ResultSet rs;
					Statement stmt = db.getVerbindung().createStatement();

					stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'disponiert' WHERE (`AuftragsNummer` = '"
							+ auftrag.getAuftragsNummer() + "');");

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (verfuegbareKomponenten != 5 && !auftrag.getStatus().equals("im Lager")) {
				auftrag.setStatus("Teile fehlen"); // falls nein, wird der Status in Teile fehlen
				// ge�ndert
				try {
					ResultSet rs;
					Statement stmt = db.getVerbindung().createStatement();

					stmt.executeUpdate("UPDATE `auftrag` SET `Status` = 'Teile fehlen' WHERE (`AuftragsNummer` = '"
							+ auftrag.getAuftragsNummer() + "');");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int indexWochentag = 0;

	private void datumComboBox() { // ComboBox um das Datum auw�hlen zu k�nnen

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy"); // EEEE steht f�r den Wochentag
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
		DatumCBox = new JComboBox(Datum);
		DatumCBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		DatumCBox.setSelectedIndex(0);

		DatumCBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String ausgewaeltesDatum = (String) DatumCBox.getSelectedItem(); // liest Datum als String aus
				String[] ausgewaelterWochentag = ausgewaeltesDatum.split(","); // wochentag und datum wird getrennt

				String s = ausgewaelterWochentag[0]; // nur der wochentag wird in s gespeichert

				switch (s) { // index f�r wochentag. wird ben�tigt um verf�gbarkeit der monteure aufrufen zu
								// k�nnen
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

	}

	private String datumAlsStringBekommen(Date date) { // gibt heutiges Datum zur�ck

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy"); // EEEE steht f�r den Wochentag
		return f.format(date);
	}

}
