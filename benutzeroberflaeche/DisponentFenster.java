package benutzeroberflaeche;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import datenbank.DatenbankVerbindung;
import objekte.Auftrag;
import objekte.Mitarbeiter;

public class DisponentFenster extends JFrame {

	static DatenbankVerbindung db = main.Main.getdb();
	// erm�glicht auf den Inhalt der DB, die in der Main geladen wurde

	private JPanel contentPane;
	private JTextField txtSuche;
	private JTable auftraegeTbl;
	private JTable archivTbl;
	private JTable monteureTbl;
	private JScrollPane auftraegeSp;
	private JScrollPane archivSp;
	private JScrollPane monteureSp;
	private JLabel lblDatum;
	private int indexWochentag = 0;

	Object[][] auftraege;
	int zeilen = 0;
	private Object[][] archiv;
	private int zeilenArchiv = 0;
	int zeilenMonteure = 0;

	String details = "Details anzeigen";
	String monteur;

	JComboBox monteureCombobox = new JComboBox();
	JComboBox datumComboBox;
	JComboBox auswahlBoxStatus = new JComboBox();

	TableColumn monteureColumn;

	private ArrayList<Instant> auftragsDaten = new ArrayList<Instant>();
	private ArrayList<Auftrag> archivListe = new ArrayList<Auftrag>();
	private ArrayList<Auftrag> auftragsListe = new ArrayList<Auftrag>();

	private TableRowSorter<DefaultTableModel> sorter;
	private TableRowSorter<DefaultTableModel> sorter1;
	private TableRowSorter<DefaultTableModel> sorter2;

	/**
	 * Erstellt ein das Fenster mit der Disponentensicht
	 */
	public DisponentFenster() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// trennt die Verbindung von der DB wenn das Fenster geschlossen wird
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				db.trennen();
			}
		}));
		
		setBounds(100, 100, 1047, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtSuche = new JTextField();
		txtSuche.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSuche.setText("Suche");
		txtSuche.setColumns(10);
		txtSuche.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				txtSuche.setText("");
				// Textfeld wird beim Anklicken leer gemacht

			}
		});
		txtSuche.addKeyListener((KeyListener) new KeyListener() {
			// Dem Textfield wird ein KeyListener hinzugef�gt, welcher den Text in dem
			// TextField zur�ckssetzt, sodass der Benutzer direkt nach dem gew�nschten
			// Kriterium suchen kann.

			int zaehler = 0;
			// Der Zaehler wird ben�tigt, um das Suchkriterium eingeben zu k�nnen. W�rde
			// der Zaehler fehlen, k�nnte der Benutzer nicht sein Suchkriterium
			// eingeben, da dieses nach jedem "Tastendruck" zur�ckgesetzt wird.

			public void keyPressed(KeyEvent e) {
				if (zaehler < 1) {
					txtSuche.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			};

			public void keyTyped(KeyEvent e) {
				if (zaehler < 1) {
					txtSuche.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			}

			public void keyReleased(KeyEvent e) {
				if (zaehler < 1) {
					txtSuche.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			}
		});

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// wenn ein anderes Tab ausgew�hlt wird

				if (tabbedPane.getSelectedComponent() == monteureSp) {
					datumComboBox.setVisible(true);
					// datumComboBox wird nur im Tab Monteur angezeigt

					lblDatum.setVisible(false);
				} else {
					datumComboBox.setVisible(false);
					lblDatum.setVisible(true);
					// in allen anderen Tabs wir das lblDatum angezeigt
				}
			}
		});

		befuellenDatumComboBox();
		// Bef�llt die datumComboBox

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		// Datumsformat

		lblDatum = new JLabel(f.format(new Date()));
		// heutigen Tag �bergeben

		lblDatum.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton logoutKnopf = new JButton("Logout");
		// Logout schlie�t das fenster und �ffnet das LoginFenster

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
				txtSuche.setText("");
				// Das SuchFeld wird zur�ckgesetzt, um eine ArrayIndexOutOfBoundsException zu
				// vermeiden.

				monteureInArrayEinlesen();
				archivInDBAktualisieren();
				/*
				 * die aktuelle Tabelle wird in db.getAuftragsListe() eingelesen, dieser wird
				 * ggf. ein neuer Monteur/Status zugewiesen (stimmt dann wieder mit den Tabellen
				 * ein)
				 */

				db.einlesen();
				/*
				 * alle Listen, die direkt aus der DB bef�llt werden, werden gel�scht und
				 * anschlie�end mit der aktualisierten, neu eingelesenen Datenbank bef�llt
				 */

				statusAktualisieren();

				/*
				 * Jeder Status wird bei Knopfdruck �berpr�ft (alle Verf�gbarkeiten der Teile
				 * werden �berpr�ft) und ggf. �berschrieben
				 */

				auftraegeAktualisieren();
				/*
				 * Tabelle wird graphisch aktualisiert, Mitarbeiternummer wird bei Austausch des
				 * Monteurs automatisch mit�berschrieben, auch der Status wird �berpr�ft
				 */

				monteureAktualisieren();
				/*
				 * Tabelle wird graphisch aktualisiert, die Summe der Auftr�ge eines Monteurs
				 * passt sich an die neuen Zahlen an
				 */

				archivAktualisieren();
				/*
				 * Tabelle wird graphisch aktualisiert, die Summe der Auftr�ge eines Monteurs
				 * passt sich an die neuen Zahlen an
				 */

			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 520, Short.MAX_VALUE)
										.addComponent(lblDatum).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(datumComboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(dbAktualisierenKnopf).addGap(18)
										.addComponent(logoutKnopf))
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 520, Short.MAX_VALUE)
										.addComponent(lblDatum).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(datumComboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(dbAktualisierenKnopf).addGap(18)
										.addComponent(logoutKnopf))
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE))
						.addContainerGap()));

		gl_contentPane
				.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(logoutKnopf).addComponent(dbAktualisierenKnopf)
												.addComponent(datumComboBox, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(datumComboBox, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblDatum)))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE).addGap(6)));

		/*
		 * Auftraege Reiter.==================================================
		 */

		auftraegeSp = new JScrollPane();
		tabbedPane.addTab("Auftr�ge", null, auftraegeSp, null);

		auftraegeTbl = new JTable();
		auftraegeSp.setViewportView(auftraegeTbl);
		auftraegeTbl.setCellSelectionEnabled(true);
		// Einzelne Zellen k�nnen ausgew�hlt werden

		auftraegeTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -gr��e in der Tabelle

		auftraegeTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -gr��e in der Kopfzeile der Tabelle

		auftraegeAktualisieren();
		// Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Auftr�ge
		 * nach diesem Attribut in der nat�rlichen Ordnung und umgekehrt sortiert
		 */

		/*
		 * Archiv Reiter.==================================================
		 */

		archivSp = new JScrollPane();
		tabbedPane.addTab("Archiv", null, archivSp, null);

		archivTbl = new JTable();

		archivSp.setViewportView(archivTbl);
		archivTbl.setCellSelectionEnabled(true);
		// Einzelne Zellen k�nnen ausgew�hlt werden

		archivTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -gr��e in der Tabelle

		archivTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -gr��e in der Kopfzeile der Tabelle

		archivAktualisieren();
		// Erstellen/aktualisieren der Auftragstabelle -> mehr Details in der Methode

		auswahlBoxStatus(archivTbl, 2);
		// erstellen einer Combobox, durch die der Disponent ggf. den Status r�ckg�ngig
		// machen kann

		/*
		 * Monteure Reiter.==================================================
		 */

		monteureSp = new JScrollPane();
		tabbedPane.addTab("Monteure", null, monteureSp, null);

		monteureTbl = new JTable();
		monteureSp.setViewportView(monteureTbl);
		monteureTbl.setCellSelectionEnabled(true);
		// Einzelne Zellen k�nnen ausgew�hlt werden

		monteureTbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und -gr��e in der Tabelle

		monteureTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Schriftart und -gr��e in der Kopfzeile der Tabelle

		monteureAktualisieren();
		// Erstellen/aktualisieren der Monteurtabelle -> mehr Details in der Methode

		monteureTblFormat();
		// Monteure Tabelle wird formatiert

		contentPane.setLayout(gl_contentPane);
		// Group-Layout im contentPane wird festgelegt

	}

	/*
	 * GUI-Hilfsmethoden.==================================================
	 */

	/**
	 * Implementierung von Such - und Sortierfunktion
	 * <p>
	 * Es wird ein RowSorter erstellt, welcher der �bergebenen Tabelle zugewiesen
	 * wird. Zudem wird eine ArrayList erstellt, die den Sortierschl�ssel f�r jede
	 * Spalte enth�lt(Spalte, Reihenfolge) und anschlie�end dem Sorter �bergeben
	 * wird, der die in der ArrayList gespeicherten Exemplare sortiert und
	 * anschlie�end die Sortierung in die Tabelle bzw. je nach Bedarf dem
	 * DocumentListener �bergibt.
	 * 
	 * @param table Die �bergebene Tabelle bekommt eine Sortierfunktion (durch einen
	 *              neu erstellten RowSorter) sowie eine Suchfunktion.
	 */
	private void suchen(JTable table) {

		if (table == auftraegeTbl) {
			// �berpr�fen, ob es sich um die richtige Tabelle handelt

			sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
			table.setRowSorter(sorter);
			ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
			// Erstellen eines neuen RowSorters, welcher der Tabelle zugewiesen wird. Zudem
			// wird eine ArrayList erstellt, in der sp�ter die Sorierung vorgenommen wird

			int columnIndexForAuftragsNummer = 1;
			sortKeys.add(new RowSorter.SortKey(columnIndexForAuftragsNummer, SortOrder.ASCENDING));

			int columnIndexForStatus = 2;
			sortKeys.add(new RowSorter.SortKey(columnIndexForStatus, SortOrder.ASCENDING));

			int columnIndexToSortDatum = 4;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum, SortOrder.ASCENDING));

			int columnIndexToSortDatum1 = 3;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum1, SortOrder.ASCENDING));

			int columnIndexForMonteur = 5;
			sortKeys.add(new RowSorter.SortKey(columnIndexForMonteur, SortOrder.ASCENDING));

			int columnIndexForMitarbeiterNummer = 6;
			sortKeys.add(new RowSorter.SortKey(columnIndexForMitarbeiterNummer, SortOrder.ASCENDING));

			int columnIndexForKundenNummer = 7;
			sortKeys.add(new RowSorter.SortKey(columnIndexForKundenNummer, SortOrder.ASCENDING));
			// Der Index liefert die Spalte, in der sortiert werden soll. Der zweite
			// Parameter gibt an, wie sortiert werden soll(SortOrder.ASCENDING -> nat�rliche
			// Ordnung, oder nach bestimmten Comparator)

			sorter.setComparator(columnIndexToSortDatum, ((String datum1, String datum2) -> {
				// Zuweisen eines Comparators, der ausgew�hlte Spalte nach Frist sortiert

				String[] datumGetrennt1 = datum1.split("\\.");
				String[] datumGetrennt2 = datum2.split("\\.");
				// Datum-String wird in 3 Teile geteilt

				if (datumGetrennt1.length != datumGetrennt2.length) {
					// Daten werden miteinander verglichen, ob sie die selbe L�nge besitzen

					throw new ClassCastException();
				}

				String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
				String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
				// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
				// sortiert werden kann

				return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
				// Sortieren der Daten �ber CompareTo-Methode

			}));

			sorter.setComparator(columnIndexToSortDatum1, ((String datum1, String datum2) -> {
				// Zuweisen eines Comparators, der ausgew�hlte Spalte nach Erstellungsdatum
				// sortiert

				String[] datumGetrennt1 = datum1.split("\\.");
				String[] datumGetrennt2 = datum2.split("\\.");
				// Datum-String wird in 3 Teile geteilt

				if (datumGetrennt1.length != datumGetrennt2.length) {
					// Daten werden miteinander verglichen, ob sie die selbe L�nge besitzen
					throw new ClassCastException();
				}

				String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
				String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
				// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
				// sortiert werden kann

				return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
				// Sortieren der Daten �ber CompareTo-Methode

			}));

			sorter.setSortKeys(sortKeys);
			sorter.sort();
			// dem Sorter wird die sortierte ArrayList �bergeben, er schreibt diese dann nur
			// noch in die Tabelle herein und diese wird somit neu bef�llt und sortiert

		} else if (table == archivTbl) {
			// �berpr�fen, ob es sich um die richtige Tabelle handelt

			sorter1 = new TableRowSorter<>((DefaultTableModel) table.getModel());
			table.setRowSorter(sorter1);
			ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
			// Erstellen eines neuen RowSorters, welcher der Tabelle zugewiesen wird. Zudem
			// wird eine ArrayList erstellt, in der sp�ter die Sorierung vorgenommen wird

			int columnIndexForAuftragsNummer = 1;
			sortKeys.add(new RowSorter.SortKey(columnIndexForAuftragsNummer, SortOrder.ASCENDING));

			int columnIndexForStatus = 2;
			sortKeys.add(new RowSorter.SortKey(columnIndexForStatus, SortOrder.ASCENDING));

			int columnIndexToSortDatum = 4;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum, SortOrder.ASCENDING));

			int columnIndexToSortDatum1 = 3;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSortDatum1, SortOrder.ASCENDING));

			int columnIndexForMonteur = 5;
			sortKeys.add(new RowSorter.SortKey(columnIndexForMonteur, SortOrder.ASCENDING));

			int columnIndexForMitarbeiterNummer = 6;
			sortKeys.add(new RowSorter.SortKey(columnIndexForMitarbeiterNummer, SortOrder.ASCENDING));

			int columnIndexForKundenNummer = 7;
			sortKeys.add(new RowSorter.SortKey(columnIndexForKundenNummer, SortOrder.ASCENDING));
			// Der Index liefert die Spalte, in der sortiert werden soll. Der zweite
			// Parameter gibt an, wie sortiert werden soll(SortOrder.ASCENDING -> nat�rliche
			// Ordnung, oder nach bestimmten Comparator)

			sorter1.setComparator(columnIndexToSortDatum, ((String datum1, String datum2) -> {
				// Zuweisen eines Comparators, der ausgew�hlte Spalte nach Frist sortiert

				String[] datumGetrennt1 = datum1.split("\\.");
				String[] datumGetrennt2 = datum2.split("\\.");
				// Datum-String wird in 3 Teile geteilt

				if (datumGetrennt1.length != datumGetrennt2.length) {
					// Daten werden miteinander verglichen, ob sie dieselbe L�nge besitzen

					throw new ClassCastException();
				}
				String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
				// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
				// sortiert werden kann

				String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];

				return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
				// Sortieren der Daten �ber CompareTo-Methode

			}));

			sorter.setComparator(columnIndexToSortDatum1, ((String datum1, String datum2) -> {
				// Zuweisen eines Comparators, der ausgew�hlte Spalte nach Erstellungsdatum
				// sortiert

				String[] datumGetrennt1 = datum1.split("\\.");
				String[] datumGetrennt2 = datum2.split("\\.");
				// Datum-String wird in 3 Teile geteilt

				if (datumGetrennt1.length != datumGetrennt2.length) {
					// Daten werden miteinander verglichen, ob sie dieselbe L�nge besitzen

					throw new ClassCastException();
				}
				String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
				String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
				// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
				// sortiert werden kann

				return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
				// Sortieren der Daten �ber CompareTo-Methode

			}));

			sorter1.setSortKeys(sortKeys);
			sorter1.sort();
			// Der Index liefert die Spalte, in der sortiert werden soll. Der zweite
			// Parameter gibt an, wie sortiert werden soll(SortOrder.ASCENDING -> nat�rliche
			// Ordnung, oder nach bestimmten Comparator)

		} else if (table == monteureTbl) {
			// �berpr�fen, ob es sich um die richtige Tabelle handelt

			sorter2 = new TableRowSorter<DefaultTableModel>((DefaultTableModel) table.getModel());
			table.setRowSorter(sorter2);
		}

		// 3 Sorter = 3 Suchfunktionen. Diese sind im Prinzip gleich, au�er, dass jede
		// einen eigenen Sorter hat

		txtSuche.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}
			// etwas wurde ins Suchfeld geschrieben - dies wird dann erfasst

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}
			// f�r den Fall, dass verschiedene Zeichen wieder gel�scht werden, wird das hier
			// erfasst

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}
			// falls die Eingabe ver�ndert wurde, wird auch dies erfasst

			public void search(String str) {
				if (str.length() == 0) {
					sorter.setRowFilter(null);
					// wenn noch nichts eingegeben wurde, wird auch noch nicht gefiltert

				} else {
					sorter.setRowFilter(RowFilter.regexFilter(str));
					// hier wird die Tabelle verglichen mit der Eingabe und nur passende Zeilen
					// ausgegeben

				}
			}
		});

		txtSuche.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			public void search(String str) {
				if (str.length() == 0) {
					sorter1.setRowFilter(null);
				} else {
					sorter1.setRowFilter(RowFilter.regexFilter(str));
				}
			}
		});

		txtSuche.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(txtSuche.getText());
			}

			public void search(String str) {
				if (str.length() == 0) {
					sorter2.setRowFilter(null);
				} else {
					sorter2.setRowFilter(RowFilter.regexFilter(str));
				}
			}
		});
	}

	/**
	 * Aktualisierung einer Tabelle
	 * <p>
	 * auftraegeAktualisieren erstellt das DefaultTableModel, generiert die Buttons,
	 * formatiert die Tabelle, f�hrt suche(tabelle) f�r diese Tabelle aus und f�hrt
	 * monteureCombobox(tabelle) aus. Im DefaulttableModel wird die Tabelle und die
	 * die Kopfzeile erstellt, zu dem wird festgelegt welche Spalten Editierbar
	 * sind.
	 * 
	 * @see {@link benutzeroberflaeche.DisponentFenster #auftraege()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonRenderer}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonEditor}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #tblFormat(JTable)}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #suchen(JTable)}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #monteureCombobox(JTable)}
	 */
	private void auftraegeAktualisieren() {

		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), new String[] { "", "AuftragsNummer", "Status",
				"Erstellungsdatum", "Frist", "MonteurName", "MonteurNummer", "Auftragsgeber" }) {

			boolean[] columnEditables = new boolean[] { true, false, false, false, false, true, false, false };
			// welche Spalten lassen sich �ndern

			public boolean isCellEditable(int row, int column) {
				// Kontrollmethode ob spalten sich �ndern lassen

				return columnEditables[column];
			}
		});

		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellRenderer(new JButtonRenderer("auftraegeTbl"));
		// ButtonRenderer wird in Spalte 0 ausgef�hrt

		auftraegeTbl.getColumn(auftraegeTbl.getColumnName(0)).setCellEditor(new JButtonEditor("auftraegeTbl"));
		// ButtonEditorwird in Spalte 0 ausgef�hrt

		tblFormat(auftraegeTbl);
		// Tabelle wird formatiert

		suchen(auftraegeTbl);
		// Suchfunktion + Sortieralgorithmus wird in Tabelle implementiert

		monteureCombobox(auftraegeTbl);
		// monteureCombobox wird konfiguriert (muss bei jeder Aktualisierung geschehen)
	}

	/**
	 * Funktioniert wie auftraegeAktualisieren(). Der Unterschied liegt darin, dass
	 * eine andere Combobox generiert wird und somit auch eine andere Spalte
	 * Editierbar ist.
	 * 
	 * @see {@link benutzeroberflaeche.DisponentFenster #auftraegeAktualisieren()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #archiv()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonRenderer}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonEditor}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #tblFormat(JTable)}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #suchen(JTable)}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #auswahlBoxStatus(JTable, JComboBox, int)}
	 */
	private void archivAktualisieren() {

		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		archivTbl.setModel(new DefaultTableModel(archiv(), new String[] { "", "AuftragsNummer", "Status",
				"Erstellungsdatum", "Frist", "MonteurName", "MonteurNummer", "Auftragsgeber" }) {

			boolean[] columnEditables = new boolean[] { true, false, true, false, false, false, false, false };
			// welche Spalten lassen sich �ndern

			public boolean isCellEditable(int row, int column) {
				// Kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});

		archivTbl.getColumn(archivTbl.getColumnName(0)).setCellRenderer(new JButtonRenderer("archivTbl"));
		// ButtonRenderer wird in Spalte 0 ausgef�hrt

		archivTbl.getColumn(archivTbl.getColumnName(0)).setCellEditor(new JButtonEditor("archivTbl"));
		// ButtonEditorwird in Spalte 0 ausgef�hrt

		tblFormat(archivTbl);
		// Tabelle wird formatiert

		suchen(archivTbl);
		// Suchfunktion + Sortieralgorithmus wird in Tabelle implementiert

		auswahlBoxStatus(archivTbl, 2);
	}

	/**
	 * Funktioniert wie auftraegeAktualisieren(). Der Unterschied liegt darin, dass
	 * es hier keine Combobox gibt, ein anderes Format verwendet wird und ein
	 * anderer Button erstellt wird.
	 * 
	 * @see {@link benutzeroberflaeche.DisponentFenster #auftraegeAktualisieren()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #monteure()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonRenderer}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster.JButtonEditor}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #suchen(JTable)}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #monteureTblFormat()}
	 */
	private void monteureAktualisieren() {

		// DefaultTableModel(Tabelle,Kopfzeile){z.B. was ist editierbar?}
		monteureTbl.setModel(new DefaultTableModel(monteure(),
				new String[] { "Name", "MitarbeiterNummer", "Verf�gbarkeit", "Auftraege" }) {

			boolean[] columnEditables = new boolean[] { false, false, false, true };
			// welche Spalten lassen sich �ndern

			public boolean isCellEditable(int row, int column) {
				// Kontrollmethode ob spalten sich �ndern lassen

				return columnEditables[column];
			}
		});

		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellRenderer(new JButtonRenderer("monteureTbl"));
		// ButtonRenderer wird in Spalte mit dem Index 3 ausgef�hrt

		monteureTbl.getColumn(monteureTbl.getColumnName(3)).setCellEditor(new JButtonEditor("monteureTbl"));
		// ButtonEditorwird in Spalte mit dem Index 3 ausgef�hrt

		monteureTblFormat();
		// Tabelle wird formatiert

		suchen(monteureTbl);
		// Suchfunktion + Sortieralgorithmus wird in Tabelle implementiert
	}

	/**
	 * auftraege() arrayListeBefuellen(ArrayList<Auftrag>) mit dem Parameter
	 * "auftragsListe" aus, z�hlt mit auftragsListe.size() die Zeilen die die
	 * Tabelle haben wird, erstellt ein zweidimensionales Array auftraege, bef�llt
	 * die einzelne Felder im Array und nachbbestimmten Bedingungen.
	 * 
	 * @return Object[][] auftraege: Ein Array, das die Inhalte der Tabelle im
	 *         Auftragsreiter enth�lt. Die Position im array wird in der Form
	 *         auftraege[Zeie][Spalte] definiert}
	 *         <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #arrayListeBefuellen(ArrayList)}
	 */
	public Object[][] auftraege() {
		// Erstellt Inhalt zur bef�llung der auftraegeTabelle
		arrayListeBefuellen(auftragsListe);
		// bef�llen der ArrayList mit den passenden Auftr�gen

		zeilen = auftragsListe.size();
		// gr��e der Tabelle wird ermittelt

		// gr��e der Tabelle wird ermittelt

		Object[][] auftraege = new Object[zeilen][8];
		// dieses Array wird die Tabelle bef�llen

		for (int i = 0; i < auftragsListe.size(); i++) {
			auftraege[i][0] = details;

			auftraege[i][1] = "";
			if (auftragsListe.get(i).getAuftragsNummer() != null)
				auftraege[i][1] = auftragsListe.get(i).getAuftragsNummer();
			// AuftragsNummer

			auftraege[i][2] = "";
			if (auftragsListe.get(i).getStatus() != null)
				auftraege[i][2] = auftragsListe.get(i).getStatus();
			// Status

			auftraege[i][3] = "";
			if (auftragsListe.get(i).getErstellungsdatum() != null)
				auftraege[i][3] = auftragsListe.get(i).getErstellungsdatum();
			// Erstellungsdatum

			auftraege[i][4] = "";
			if (auftragsListe.get(i).getFrist() != null)
				auftraege[i][4] = auftragsListe.get(i).getFrist();
			// Frist

			auftraege[i][5] = "";
			auftraege[i][6] = "";
			if (auftragsListe.get(i).getZustaendig() != null && auftragsListe.get(i).getZustaendig() != null
					&& !auftragsListe.get(i).getZustaendig().getMitarbeiterNummer().equals("0000")) {
				// ist ein Monteur zust�ndug?

				auftraege[i][5] = auftragsListe.get(i).getZustaendig().getName() + ", "
						+ auftragsListe.get(i).getZustaendig().getVorname() + " ["
						+ summeAuftraege(auftragsListe.get(i).getZustaendig()) + "]";
				// MitarbeiterName (Name, Vorname)

				auftraege[i][6] = auftragsListe.get(i).getZustaendig().getMitarbeiterNummer();
				// MitarbeiterNummer

			} else if (auftragsListe.get(i).getZustaendig().getMitarbeiterNummer().equals("0000")) {
				auftraege[i][5] = auftragsListe.get(i).getZustaendig().getName() + " "
						+ auftragsListe.get(i).getZustaendig().getVorname();
				// Nicht zugewiesen
			}

			auftraege[i][7] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				auftraege[i][7] = auftragsListe.get(i).getAuftraggeber().getKundenNummer();
			// Auftraggeber

		}
		return auftraege;
	}

	/**
	 * Funktioniert wie auftraege()
	 * 
	 * @return Object[][] archiv
	 *         <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #auftraege()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #arrayListeBefuellen(ArrayList)}
	 */
	public Object[][] archiv() {
		// Erstellt Inhalt zur bef�llung der auftraegeTabelle

		arrayListeBefuellen(archivListe);
		// bef�llen der ArrayList mit den passenden Auftr�gen

		zeilenArchiv = archivListe.size();
		// gr��e der Tabelle wird ermittelt

		Object[][] archiv = new Object[zeilenArchiv][8];
		// dieses Array wird die Tabelle bef�llen

		for (

				int i = 0; i < archivListe.size(); i++) {
			archiv[i][0] = details;

			archiv[i][1] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				archiv[i][1] = archivListe.get(i).getAuftragsNummer();
			// AuftragsNummer

			archiv[i][2] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				archiv[i][2] = archivListe.get(i).getStatus();
			// Status

			archiv[i][3] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				archiv[i][3] = archivListe.get(i).getErstellungsdatum();
			// Erstellungsdatum

			archiv[i][4] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				archiv[i][4] = archivListe.get(i).getFrist();
			// Frist

			archiv[i][5] = "";
			archiv[i][6] = "";
			if (archivListe.get(i).getZustaendig() != null && archivListe.get(i).getZustaendig() != null
					&& !archivListe.get(i).getZustaendig().getMitarbeiterNummer().equals("0000")) {
				// ist ein Monteur zust�ndug?

				archiv[i][5] = archivListe.get(i).getZustaendig().getName() + ", "
						+ archivListe.get(i).getZustaendig().getVorname();
				// MitarbeiterName (Name, Vorname)

				archiv[i][6] = archivListe.get(i).getZustaendig().getMitarbeiterNummer();
				// MitarbeiterNummer
			}

			archiv[i][7] = "";
			if (auftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				archiv[i][7] = archivListe.get(i).getAuftraggeber().getKundenNummer();
			// Auftraggeber

		}
		return archiv;
	}

	/**
	 * Funktioniert wie auftraege()
	 * 
	 * @return Object[][] monteure
	 *         <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #auftraege()}
	 *      <p>
	 * @see {@link benutzeroberflaeche.DisponentFenster #arrayListeBefuellen(ArrayList)}
	 */
	private Object[][] monteure() {
		// Erstellt Inhalt zur bef�llung der monteureTabelle

		zeilenMonteure = db.getMonteurListe().size() - 1;
		// gr��e der Tabelle wird ermittelt

		Object[][] monteure = new Object[zeilenMonteure][4];
		// dieses Array wird die Tabelle bef�llen

		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			if (!db.getMonteurListe().get(i).getMitarbeiterNummer().equals("0000")) {

				monteure[i][0] = "";
				if (db.getMonteurListe().get(i).getName() != null && db.getMonteurListe().get(i).getVorname() != null)
					monteure[i][0] = db.getMonteurListe().get(i).getName() + ", "
							+ db.getMonteurListe().get(i).getVorname();
				// MitarbeiterName (Name, Vorname)

				monteure[i][1] = "";
				if (db.getMonteurListe().get(i).getMitarbeiterNummer() != null)
					monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();
				// MitarbeiterNummer

				monteure[i][2] = "";
				if (indexWochentag <= 4 && db.getMonteurListe().get(i).getAnwesenheit() != null) {
					// f�r Montag bis Freitag

					monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit().get(indexWochentag);
					// hier wird nur noch die Anwesenheit am jeweiligen Tag eingetragen

				} else if (db.getMonteurListe().get(i).getAnwesenheit() != null) {
					// Samstag und Sonntag wird die komplette Liste angezeigt
					monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
				}

				monteure[i][3] = "Auftr�ge anzeigen [0]";
				if (db.getMonteurListe().get(i) != null)
					monteure[i][3] = "Auftr�ge anzeigen [" + summeAuftraege(db.getMonteurListe().get(i)) + "]";
				// Summe der Auftr�ge
			}
		}
		return monteure;
	}

	/**
	 * Tabellenformat Archiv und Auftr�ge
	 * <p>
	 * Die Spaltenformatierungen werden bestimmt mit Breite, H�he und
	 * Verschiebbarkeit
	 * 
	 * @param table
	 */
	private void tblFormat(JTable table) {
		// Details
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(0).setMaxWidth(150);

		// Auftragsnummer
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMaxWidth(250);

		// Status
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(2).setMaxWidth(200);

		// Erstellungsdatum
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(3).setMaxWidth(250);

		// Frist
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(4).setMaxWidth(250);

		// MonteurName
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(5).setMaxWidth(500);

		// MonteurNummer
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(6).setMaxWidth(200);

		// Aufraggeber
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(7).setMaxWidth(200);

		table.setRowHeight(50);

		table.getTableHeader().setReorderingAllowed(false);
	}

	/**
	 * Tabellenformat Monteure
	 * <p>
	 * Die Spaltenformatierungen werden bestimmt mit Breite, H�he und
	 * Verschiebbarkeit
	 */
	private void monteureTblFormat() {
		// Name
		monteureTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		monteureTbl.getColumnModel().getColumn(0).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(0).setMaxWidth(500);

		// MitarbeiterNummer
		monteureTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMaxWidth(400);

		// Verf�gbarkeit
		monteureTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMaxWidth(500);

		// AuftragsNummer
		monteureTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMaxWidth(500);

		monteureTbl.setRowHeight(50);

		monteureTbl.getTableHeader().setReorderingAllowed(false);
	}

	/**
	 * Hilfsmethoden: Erstellen der Combobox, sowie Bef�llen und Funktionalit�t
	 * <p>
	 * Hier wird die Auswahlbox f�r die Monteure generiert. Diese werden aus der
	 * Datenbank gelesen. F�r die Mitarbeiternummer 0000, wird der Auftrag nicht
	 * zugeteilst
	 * 
	 * @param table
	 */
	private void monteureCombobox(JTable table) {
		monteureCombobox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Schriftart und Gr��e

		monteureColumn = table.getColumnModel().getColumn(5);
		// eine bestimmte Spalte f�r Combobox ausw�hlen

		monteureColumn.setCellEditor(new DefaultCellEditor(monteureCombobox));
		// in die Spalte die Combobox einbinden

		monteureCombobox.addActionListener(null);
		// zugewiesenen Monteur auslesen und in Datenbank zuweisung �ndern

		monteureCombobox.removeAllItems();
		// monteureCombobox wird geleert vor Bef�llung

		Collections.sort(db.getMonteurListe(), new Comparator<Mitarbeiter>() {
			// sortiert die monteureCombobox nach Nachname

			@Override
			public int compare(Mitarbeiter o1, Mitarbeiter o2) {

				return o1.getName().compareTo(o2.getName());
			}
		});

		for (int i = 0; i < db.getMonteurListe().size(); i++) {

			if (db.getMonteurListe().get(i).getMitarbeiterNummer().equals("0000")) {
				monteureCombobox.addItem(
						db.getMonteurListe().get(i).getName() + " " + db.getMonteurListe().get(i).getVorname());
				// wenn kein Monteur dem Auftrag zugewiesen ist, wird die Combobox an dieser
				// Stelle mit "nicht (Nachname) zugewiesen (Vorname)" bef�llt

			} else {
				monteureCombobox
						.addItem(db.getMonteurListe().get(i).getName() + ", " + db.getMonteurListe().get(i).getVorname()
								+ " [" + summeAuftraege(db.getMonteurListe().get(i)) + "]");
				// ansonsten erfolgt Bef�llung der Combobox nach folgendem System: Name,
				// Vorname, [Anzahl der Auftr�ge]

			}
		}

	}

	/**
	 * Hilfsmethoden: Erstellen der Combobox, sowie Bef�llen und Funktionalit�t
	 * <p>
	 * Hier wird die Auswahlbox f�r den Status generiert. Diese ist im Archiv zu
	 * finden
	 * 
	 * @param table
	 * @param combobox
	 * @param spalte
	 */
	private void auswahlBoxStatus(JTable table, int spalte) {

		auswahlBoxStatus.removeAllItems();
		// erstmal alle rausl�schen

		auswahlBoxStatus.addItem("Teile fehlen");
		auswahlBoxStatus.addItem("disponiert");
		auswahlBoxStatus.addItem("im Lager");
		auswahlBoxStatus.addItem("nicht zugewiesen");
		// auswahlm�glichkeiten

		TableColumn statusSpalte = table.getColumnModel().getColumn(spalte);
		// in welche Spalte soll die Combobox eingef�gt werden

		statusSpalte.setCellEditor(new DefaultCellEditor(auswahlBoxStatus));
		// Combobox jetzt anklickbar
	}

	/*
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
				// wird ein knopf in der Monteur Tabelle gedr�ckt?

				button.setText("Auftr�ge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
			// Text im Button wird festgelegt und anzahl der Aufr�ge f�r die der Monteur
			// zust�ndig ist wird gez�hlt

			return button;
		}
	}

	class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
		JButton button;
		String tabelle;
		// in welcher Tabelle ist der Button

		public JButtonEditor(String string) {
			super();
			this.tabelle = string;
			button = new JButton();
			button.setOpaque(true);
			// ist der Button undurchsichtig oder nicht

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// was passiert wenn man den Button anklickt

					txtSuche.setText("");

					if (tabelle.equals("auftraegeTbl")) {
						// wird der detailsButton gedr�ckt?

						Auftrag auftrag = welcherAuftrag(auftraegeTbl.getEditingRow(), auftraegeTbl);
						// welcher Auftrag wird in der Zeile des geklickten Buttons angezeigt

						if (auftrag != null) {
							// es existiert ein Auftrag in der Zeile

							DetailsFenster frame = new DetailsFenster(auftrag);
							frame.toFront();
							frame.requestFocus();
							frame.transferFocus();
							frame.setVisible(true);
							// DetailsFenster wird ge�ffnet und der angezeigte Auftrag wird ihm mitgegeben

							auftraegeAktualisieren();
							// Tabelle wird neu geladen, damit der Button wieder erscheint
						}

					} else if (tabelle.equals("monteureTbl")) {
						Mitarbeiter monteur = welcherMonteur(monteureTbl.getEditingRow());
						if (summeAuftraege(monteur).equals("0")) {
							// ist der der Monteur f�r 0 Auftr�ge zust�ndig?

							JOptionPane nichtZugewiesen = new JOptionPane();
							nichtZugewiesen.showMessageDialog(null, "keine Auftr�ge zugewiesen");
							// Monteur ist f�r keinen Auftrag zust�ndig -> Warnung

							return;
							// Button wird vorzeitig beendet
						}

						// Monteur existiert und ist f�r mindestens einen Auftrag zust�ndig

						AuftraegeListeFenster frame = new AuftraegeListeFenster(monteur);
						frame.setVisible(true);
						// AuftraegeListeFenster und der Monteur

						monteureAktualisieren();
						// Tabelle wird neu geladen, damit der Button wieder erscheint
					} else if (tabelle.equals("archivTbl")) {
						Auftrag auftrag = welcherAuftrag(archivTbl.getEditingRow(), archivTbl);
						// welcher Auftrag wird in der Zeile des geklickten Buttons angezeigt

						if (auftrag != null) {
							// es existiert ein Auftrag in der Zeile

							DetailsFenster frame = new DetailsFenster(auftrag);
							frame.setVisible(true);
							// DetailsFenster wird ge�ffnet und der angezeigte Auftrag wird ihm mitgegeben

							archivAktualisieren();
							// Tabelle wird neu geladen, damit der Button wieder erscheint
						}
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
				// die �bergebene Tabelle ist die MonteurTabelle

				button.setText("Auftr�ge anzeigen [" + summeAuftraege(welcherMonteur(row)) + "]");
				// Text im Button wird festgelegt und anzahl der Aufr�ge f�r die der Monteur
				// zust�ndig ist wird gez�hlt
			}
			return button;
		}
	}

	/*
	 * funktionale Hilfsmethoden.==================================================
	 */

	/**
	 * Vergleicht monteur mit der Zust�ndigkeit in einem Auftrag und z�hlt
	 * summeAuftraege um 1 hoch, wenn die beiden �bereinstimmen.
	 * 
	 * @param monteur
	 * @return int summeAuftraege als String
	 */
	private String summeAuftraege(Mitarbeiter monteur) {
		// z�hlt die Auftr�ge f�r die der Monteur Zust�ndig ist

		int summeAuftraege = 0;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (monteur != null
					&& db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer()
							.equals(monteur.getMitarbeiterNummer())
					&& !db.getAuftragsListe().get(j).getStatus().equals("im Lager")) {
				// Zust�ndiger Monteur = Monteur in der MonteurListe?
				// Auftr�ge, welche bereits abgeschlossen/ im Lager sind, z�hlen nicht mehr in
				// die Auftragssumme des einzelnen Monteurs

				summeAuftraege = summeAuftraege + 1;
			}
		}
		return "" + summeAuftraege;
	}

	private Auftrag welcherAuftrag(int editingRow, JTable tabelle) {
		for (Auftrag auftrag : db.getAuftragsListe()) {

			if (tabelle == auftraegeTbl && auftraegeTbl.getValueAt(editingRow, 1).equals(auftrag.getAuftragsNummer())) {
				return auftrag;
			} else if (tabelle == archivTbl
					&& archivTbl.getValueAt(editingRow, 1).equals(auftrag.getAuftragsNummer())) {
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
	 * Vergleicht die Monteure, die in der Tabelle einem Auftrag zugewiesen sind mit
	 * den Monteuren aus der Auftragsliste. Bei Unstimmigkeiten wird der Monteur aus
	 * der Tabelle in die Liste sowie in der Datenbank �bernommen.
	 */
	private void monteureInArrayEinlesen() {

		for (int i = 0; i < zeilen; i++) {// jede Tabellenzeile wird mit dieser for-Schleife durchlaufen

			for (Auftrag auftrag : db.getAuftragsListe()) {

				if (auftraegeTbl.getValueAt(i, 1).equals(auftrag.getAuftragsNummer())) {
					// vergleicht Auftragsnummer aus Tabellenzeile mit Auftragsnummer in der
					// db.getAuftragsliste();

					String ausgewaehlterMonteur = auftraegeTbl.getValueAt(i, 5).toString();
					// der in der Combobox ausgew�hlte Monteur wird in einen String umgewandelt und
					// die Combobox somit eliminiert

					String[] namentrennung;
					// Trennung in Vor- [1] und Nachname [0] des Monteurs

					if (auftraegeTbl.getValueAt(i, 5).equals("nicht zugewiesen")) {
						namentrennung = ausgewaehlterMonteur.split(" ");
						// falls der Auftrag "nicht" zugewiesen ist, muss ein anderes Splitverfahren
						// verwendet werden

					} else {
						namentrennung = ausgewaehlterMonteur.split(", ");
						// Trennung in Vor- [1] und Nachname [0] des Monteurs

					}

					if (!namentrennung[0].equals(auftrag.getZustaendig().getName())) {
						// vergleicht den zust�ndigen Monteur (anhand des Nachnamens) aus dem Auftrag
						// aus der Tabellenzeile mit dem
						// gleichen Auftrag aus der db.getAuftragsliste();

						for (Mitarbeiter monteur : db.getMonteurListe()) {

							if (monteur.getName().equals(namentrennung[0])) {
								// sollte der Monteur nicht �bereinstimmen (er wurde also vom Diponenten
								// ge�ndert), dann wird die db.getMonteurliste() mit einer for-each Schleife
								// durchlaufen und der Nachnam des Monteurs (aus der Tabelle) mit den Nachnamen
								// aus der Monteurliste verglichen

								auftrag.setZustaendig(monteur);
								// wenn in der Monteurliste der passende Monteur gefunden wurde (d.h. er stimmt
								// mit dem Monteur aus der Tabelle �berein), dann wird der jeweilige Auftrag
								// �berschrieben und bekommt den neuen Monteur zugewiesen (dies geschieht mit
								// dem Setter)

								db.setZustaendig(auftrag, monteur); // auftrag bekommt neuen Monteur zugewie�en in der
																	// Datenbank

							}
							int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream()
									.filter((k) -> k.isVerfuegbarkeit()).count();
							// der gerade ge�nderte Auftrag wird nun auch nochmal auf seinen Status
							// �berpr�ft. Hierf�r werden die verf�gbaren Komponenten gez�hlt (mittels eines
							// Streams)

							if (verfuegbareKomponenten == 5) {

								auftrag.setStatus("disponiert");
								// wenn die Anzahl der verf�gbaren Komponenten genau 5 betr�gt, sind alle
								// relevanten Bauteile vorhanden und der Status wird ggf. in der
								// db.getAuftragsliste() auf "disponiert" gesetzt (falls er noch auf "Teile
								// fehlen" gesetzt ist)
								// Anmerkung: Diese Methode ist auch nochmal als eigene Methode vorzufinden,
								// allerdings hat der Disponent hier die M�glichkeit, einen Auftrag, welcher
								// "aus Versehen" im Lager gelandet ist, wieder einem Monteur zuweisen und der
								// Auftragsstatus wird dann wieder ge�ndert.

								db.setStatus(auftrag, "disponiert"); // Ver�ndert den Status in der Datenbank

							} else if (verfuegbareKomponenten != 5) {
								auftrag.setStatus("Teile fehlen");
								// wenn die Anzahl der Teile kleiner als 5 ist, hei�t das im Umkehrschluss, dass
								// mind. ein Teil nicht verf�gbar ist und somit wird der Auftragsstatus auf
								// "Teile fehlen" gesetzt.

								db.setStatus(auftrag, "Teile fehlen");

							}
							if (auftrag.getZustaendig().getMitarbeiterNummer().equals("0000")) {
								auftrag.setStatus("nicht zugewiesen");
								// wenn kein Monteur einem Auftrag zugewiesen ist, wird der Status auf nicht
								// zugewiesen gestellt. Anmerkung: Diese Methode ist auch nochmal als eigene
								// Methode vorzufinden,
								// allerdings hat der Disponent hier die M�glichkeit, einen Auftrag, welcher
								// "aus Versehen" im Lager gelandet ist, wieder einem Monteur zuweisen und der
								// Auftragsstatus wird dann wieder ge�ndert.

								db.setStatus(auftrag, "nicht zugewiesen");
							}
						}

					}
					;
				}

			}
		}
	}

	/**
	 * Status wird aktualisiert.
	 * <p>
	 * Zu Beginn werden die Arraylisten durch die Methode arrayListeBefuellen()
	 * aktualisiert. Dann wird jeder Status und die damit verbundene Anzahl der
	 * Komponentenverf�gbarkeit aus der Tabelle mit der Anzahl der verf�gbaren
	 * Komponenten verglichen. Sollte sich die Verf�gbarkeit einer Komponente
	 * �ndern, wird umgehend der Status angepasst.
	 * 
	 * @see {@link benutzeroberflaeche.DisponentFenster#arrayListeBefuellen(ArrayList)}
	 */
	private void statusAktualisieren() {
		arrayListeBefuellen(auftragsListe);

		for (Auftrag auftrag : auftragsListe) {

			int verfuegbareKomponenten = (int) auftrag.getKomponenten().stream().filter((k) -> k.isVerfuegbarkeit())
					.count();
			// der gerade ge�nderte Auftrag wird nun auch nochmal auf seinen Status
			// �berpr�ft. Hierf�r werden die verf�gbaren Komponenten gez�hlt (mittels eines
			// Streams)

			if (verfuegbareKomponenten == 5 && !auftrag.getStatus().equals("im Lager")
					&& !auftrag.getZustaendig().getMitarbeiterNummer().equals("0000")) {
				auftrag.setStatus("disponiert");
				// wenn die Anzahl der verf�gbaren Komponenten genau 5 betr�gt (und der Auftrag
				// nicht "im Lager" ist), sind alle
				// relevanten Bauteile vorhanden und der Status wird ggf. in der
				// db.getAuftragsliste() auf "disponiert" gesetzt (falls er noch auf "Teile
				// fehlen" gesetzt ist)

				db.setStatus(auftrag, "disponiert");

			} else if (verfuegbareKomponenten != 5 && !auftrag.getStatus().equals("im Lager")
					&& !auftrag.getZustaendig().getMitarbeiterNummer().equals("0000")) {
				auftrag.setStatus("Teile fehlen");
				// wenn die Anzahl der Teile kleiner als 5 ist (und der Auftrag nicht "im Lager"
				// ist, hei�t das im Umkehrschluss, dass mind. ein Teil nicht verf�gbar ist und
				// somit wird der Auftragsstatus auf "Teile fehlen" gesetzt.

				db.setStatus(auftrag, "Teile fehlen"); // Ver�ndert den Status in der Datenbank

			} else if (auftrag.getZustaendig().getMitarbeiterNummer().equals("0000")) {
				auftrag.setStatus("nicht zugewiesen");
				// wenn kein Monteur einem Auftrag zugewiesen ist, wird der Status auf nicht
				// zugewiesen gestellt

				db.setStatus(auftrag, "nicht zugewiesen");
			}
		}
	}

	/**
	 * Ein Auftrag, welcher nicht "im Lager" ist, wird aus der Archivtabelle
	 * entfernt und bekommt den passenden Status zugewiesen. Darauf hin erfolgt die
	 * Aktualisierung mit der DB.
	 */
	private void archivInDBAktualisieren() {

		for (int i = 0; i < archivListe.size(); i++) {
			for (Auftrag auftrag : archivListe) {
				// die Auftr�ge in der Tabelle werden mit dem Auftr�gen in der Archivliste
				// verglichen

				if (archivTbl.getValueAt(i, 1).equals(auftrag.getAuftragsNummer())) {
					if (!archivTbl.getValueAt(i, 2).equals(auftrag.getStatus())) {

						auftrag.setStatus(archivTbl.getValueAt(i, 2).toString());

						db.setStatus(auftrag, auftrag.getStatus());
						// wenn der selbe Auftrag in der Tabelle einen anderen Status als "im Lager"
						// hat, wird der Status �berschrieben und aktualisiert (in der DB)

						if (auftrag.getStatus().equals("nicht zugewiesen")) {
							for (Mitarbeiter monteur : db.getMonteurListe()) {
								// ist der Status des Auftrages auf "nicht zugewiesen" gesetzt, wird die
								// Monteurliste durchlaufen

								if (monteur.getMitarbeiterNummer().equals("0000")) {
									auftrag.setZustaendig(monteur);
									db.setZustaendig(auftrag, monteur);
									// wenn der Monteur "0000" nicht zugewiesen erreicht wurde, wird dieser dem
									// Auftrag als Platzhalter hinzugewiesen. Dies wird direkt in der DB
									// gespeichert.

								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Erstellt eine ComboBox, in der man das Datum der kommenden vier Tage
	 * ausw�hlen kann und einen Index f�r den jeweiligen Wochentag
	 */
	private void befuellenDatumComboBox() {
		// ComboBox um das Datum ausw�hlen und die Verf�gbarkeit
		// der Monteure am jeweiligen Tag einsehen zu k�nnen

		DateFormat f = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		// EEEE steht f�r den Wochentag

		Calendar c = Calendar.getInstance();
		// Kalendar Objekt wird erzeugt

		Date datum = new Date();
		// heutiger Tag

		String tag1 = f.format(datum);
		// formatiert das Datum

		String[] datumArray = new String[5];
		datumArray[0] = tag1;
		// Array an der Stelle 0 wir mit dem heutigen Tag bef�llt

		for (int i = 1; i < 5; i++) {
			// Array wir mit Tagen bef�llt

			c.setTime(datum);
			// c wird auf datum gesetzt

			c.add(Calendar.DATE, 1);
			// c wird ein Tag addiert

			datum = c.getTime();
			// datum wird gleich c gesetzt

			String tag = f.format(datum);
			// datum wird formatiert

			datumArray[i] = tag;
			// der jeweilige Tag wir in das Array �bergeben
		}

		datumComboBox = new JComboBox(datumArray);
		// Combobox wird bef�llt

		datumComboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		datumComboBox.setSelectedIndex(0);
		// heutige Tag als Defaultwert

		datumComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Wenn in der ComboBox ein anderer Tag ausgew�hlt wird

				String ausgewaeltesDatum = (String) datumComboBox.getSelectedItem();
				// liest Datum als String aus

				String[] ausgewaelterWochentag = ausgewaeltesDatum.split(",");
				// wochentag und datum wird getrennt

				String s = ausgewaelterWochentag[0];
				// nur der wochentag wird in s gespeichert

				switch (s) {
				/*
				 * index f�r wochentag. wird in der Methode "monteure()"ben�tigt um
				 * verf�gbarkeit der monteure am jeweiligen Tag aufrufen zu k�nnen
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
				monteureAktualisieren();
				// Verf�gbarkeit Spalte wird sofort aktualisiert
			}
		});

	}

	/**
	 * Hilfsmethode, die das heutige Datum ermittelt
	 * 
	 * @return Instant grenze
	 *         <p>
	 *         heutiges Datum
	 */
	private Instant getGrenze() {
		Instant grenze = null;
		try {
			Date date = java.util.Calendar.getInstance().getTime();
			// ermitteln des heutigen Datums

			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
			String dateString = dateFormatter.format(date);
			grenze = new SimpleDateFormat("dd.MM.yyyy").parse(dateString).toInstant();
			// das heutige Datum wird in einen String gewandelt und dann in ein
			// Instant-Datum
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grenze;
	}

	/**
	 * Die �bergebene Liste wird aktualisiert.
	 * <p>
	 * Es wird �berpr�ft, welchen Status ein Auftrag hat, anschlie�end wird der
	 * Auftrag der richtigen Liste zugeordnet. Diese kann dann in die Tabelle oder
	 * in die DB eingelesen werden.
	 * 
	 * @param liste Die �bergebene Liste wird aktualisiert.
	 */
	private void arrayListeBefuellen(ArrayList<Auftrag> liste) {

		liste.clear(); // �bergebene Liste wird gel�scht und anschlie�end neu bef�llt

		Instant auftragsFrist = null;
		Instant grenze = getGrenze(); // heutiges Datum

		for (Auftrag auftrag : db.getAuftragsListe()) {

			try {
				auftragsFrist = new SimpleDateFormat("dd.MM.yyyy").parse(auftrag.getFrist()).toInstant();
				// es wird versucht, die Auftragsfrist in ein Instant-Datum zu konvertieren
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// Kriterien zur Listenbef�llung

			if (auftrag.getStatus().equals("im Lager") && liste == archivListe) {
				archivListe.add(auftrag);
				// wenn der Auftrag "im Lager" ist, dann wird er der Archivliste zugewiesen
				// (wenn diese auch als Parameter �bergeben wurde)
			}
			if (((auftragsFrist.isBefore(grenze) && !auftrag.getStatus().equals("im Lager")
					|| (auftragsFrist.isAfter(grenze) && !auftrag.getStatus().equals("im Lager"))))
					&& liste == auftragsListe) {
				auftragsListe.add(auftrag);
				// wenn der Auftrag noch nicht im Lager ist und die Auftragsliste als Parameter
				// �bergeben wurde, dann wird der Auftrag der dieser hinzugef�gt
			}
		}

	}
}
