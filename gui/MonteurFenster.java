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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	static datenbankVerbindung db = main.Main.getdb();
	// Datenbankverbindung aus der Main

	private JTextField suchFeld;
	private JTable auftraegeMonteurTBL;
	private JPanel contentPane;
	private JLabel lblDatum;

	int zeilen = 0;
	// zeilen in der Auftragstabelle

	String details = "Details anzeigen";
	// Hier wird der Detailsbutton gerendert

	JComboBox auswahlBoxStatus = new JComboBox();

	private ArrayList<Auftrag> angepassteAuftragsListe = new ArrayList<Auftrag>();
	// Arrayliste f�r Auftr�ge des Monteurs der sich eingeloggt hat

	private TableRowSorter<DefaultTableModel> sorter1;


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
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };//
		getContentPane().setLayout(gridBagLayout);// ...bis hier

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);// layout f�r tabbed pane von hier...
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Schriftgr��e
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);
		//Was macht das? und mach das mal so dass das tabbed pane sich wieder an die Gr��e des Fensters anpasst

		JPanel auftraegeTab = new JPanel();
		tabbedPane.addTab("Auftr�ge", null, auftraegeTab, null);// tab wird sichtbar

		suchFeld = new JTextField(); // Suchfeld erstellen
		suchFeld.setFont(new Font("Tahoma", Font.PLAIN, 16));// Formatierung der Schrift
		suchFeld.setText("Suche");// Suchfeld name
		suchFeld.setColumns(10);
		suchFeld.addMouseListener(new MouseListener() {
			
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
				suchFeld.setText("");
				// Textfeld wird beim Anklicken leer gemacht
				
			}
		});
		
		suchFeld.addKeyListener((KeyListener) new KeyListener() {
			// Dem Textfield wird ein KeyListener hinzugef�gt, welcher den Text in dem
			// TextField zur�ckssetzt, sodass der Benutzer direkt nach dem gew�nschten
			// Kriterium suchen kann.

			int zaehler = 0;
			// Der Zaehler wird ben�tigt, um das Suchkriterium eingeben zu k�nnen. W�rde
			// der Zaehler fehlen, k�nnte der Benutzer nicht sein Suchkriterium
			// eingeben, da dieses nach jedem "Tastendruck" zur�ckgesetzt wird.

			public void keyPressed(KeyEvent e) {
				if (zaehler < 1) {
					suchFeld.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			};

			public void keyTyped(KeyEvent e) {
				if (zaehler < 1) {
					suchFeld.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			}

			public void keyReleased(KeyEvent e) {
				if (zaehler < 1) {
					suchFeld.setText("");
					zaehler++;
					// wenn der zaehler <1 ist, wird das Textfield geleert. Anschlie�end wird der
					// Zaehler erh�ht.
				}
			}
		});


		JButton logoutKnopf = new JButton("Logout");
		// logout button erstellen

		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// Formatierung der Schrift

		logoutKnopf.addActionListener(new ActionListener() {
			// was passiert, wenn der Knopf gedr�ckt wird

			@Override
			public void actionPerformed(ActionEvent e) {
				// logout Befehl...zur�ck zum login

				LoginFenster login = new LoginFenster();
				// loginfenster erstellen

				login.setVisible(true);

				login.setLocationRelativeTo(null);
				// Fenster erscheint mittig vom Bildschirm

				dispose();
				// aktuelles Fenster schlie�en

			}
		});

		JButton dbAktualisierenKnopf = new JButton("Aktualisieren");
		// "DB aktualisieren" button wird erstellt

		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// Formatierung der Schrift

		dbAktualisierenKnopf.addActionListener(new ActionListener() {
			// was passiert wenn der "DB aktualisieren" Knopf gedr�ckt wird

			public void actionPerformed(ActionEvent e) {
				
				suchFeld.setText("");
				// folgende Hilfsmethoden werden ausgef�hrt
				
				tabelleInArrayEinlesen();
				db.einlesen();
				auftraegeAktualisieren();
				auswahlBoxStatus(auftraegeMonteurTBL, auswahlBoxStatus, 2);

			}
		});

		/*
		 * layout Regelungen f�r scroll pane und Anordnung der Komponenten (vom
		 * windowbuilder
		 * erstellt)********************************************************
		 * ************************************************************************************************
		 */

		JScrollPane scrollPane = new JScrollPane();
		// Einstellungen f�r das scroll pane
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
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
						.addGroup(gl_auftraegeTab.createSequentialGroup()
							.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lbl_eingeloggterMonteur, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
						.addComponent(logoutKnopf)
						.addComponent(dbAktualisierenKnopf)
						.addComponent(lbl_eingeloggterMonteur)
						.addComponent(lblDatum)
						.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
					.addContainerGap())
		);

		/**
		 * ***********************************************************************************************
		 * ************************************************************************************************
		 */

		auftraegeMonteurTBL = new JTable();
		auftraegeMonteurTBL.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		// Formatierung Schrift Kopf

		auftraegeAktualisieren();
		// Tabelle einlesen mit Hilfsmethode

		auftraegeMonteurTBL.setRowHeight(50);
		// Zeilenh�he

		auftraegeMonteurTBL.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// Formatierung der Schrift in der Tabelle

		scrollPane.setViewportView(auftraegeMonteurTBL);
		auftraegeTab.setLayout(gl_auftraegeTab);

	}

	/*
	 * Hilfsmethode: Suchleiste und Tabelle
	 * sortieren****************************************************
	 * **************************************************************************************************
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
		// ein neuer RowSorter wird erstellt, durch Anklicken des TableHeaders wird
		// Index geliefert, anschlie�end kann mit diesem nach der nat�rlichen Ordnung
		// bzw. einen Comparator sortiert werden

		sorter1 = new TableRowSorter<>((DefaultTableModel) auftraegeMonteurTBL.getModel());
		auftraegeMonteurTBL.setRowSorter(sorter1);
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

		int columnIndexForKundenNummer = 5;
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
			String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
			// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
			// sortiert werden kann

			return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
			// Ordnen der Daten �ber CompareTo-Methode

		}));

		sorter1.setComparator(columnIndexToSortDatum1, ((String datum1, String datum2) -> {
			// Zuweisen eines Comparators, der ausgew�hlte Spalte nach Erstellungsdatum
			// sortiert

			String[] datumGetrennt1 = datum1.split("\\.");
			String[] datumGetrennt2 = datum2.split("\\.");
			// Datum-String wird in 3 Teile geteilt

			if (datumGetrennt1.length != datumGetrennt2.length) {
				throw new ClassCastException();
			}

			String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
			String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
			// Datum wird zusammengesetzt, sodass nun das Jahr am Anfang steht und es
			// sortiert werden kann

			return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
			// Ordnen der Daten �ber CompareTo-Methode

		}));

		sorter1.setSortKeys(sortKeys);
		sorter1.sort();
		// dem Sorter wird die sortierte ArrayList �bergeben, er schreibt diese dann nur
		// noch in die Tabelle herein und diese wird somit neu bef�llt und sortiert

		suchFeld.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				search(suchFeld.getText());
			}
			// etwas wurde ins Suchfeld geschrieben - dies wird dann erfasst

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(suchFeld.getText());
			}
			// f�r den Fall, dass verschiedene Zeichen wieder gel�scht werden, wird das hier
			// erfasst

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(suchFeld.getText());
			}
			// falls die Eingabe ver�ndert wurde, wird auch dies erfasst

			public void search(String str) {
				if (str.length() == 0) {
					sorter1.setRowFilter(null);
					// wenn noch nichts eingegeben wurde, wird auch noch nicht gefiltert

				} else {
					sorter1.setRowFilter(RowFilter.regexFilter(str));
					// hier wird die Tabelle verglichen mit der Eingabe und nur passende Zeilen
					// ausgegeben
				}
			}
		});
	}

	/**
	 * Hilfsmethode: Auftraege aktualisieren - Tabelle wird
	 * erstellt********************************************
	 ***********************************************************************************************************
	 */

	private void auftraegeAktualisieren() {
		// Bef�llung der Tabelle

		auftraegeMonteurTBL.setModel(new DefaultTableModel(auftraege(),
				// Methode wird aufgerufen und liest jetzt die Tabelle ein

				new String[] { "", "Auftragsnummer", "Status", "Erstellungsdatum", "Frist", "Auftraggeber"
				// welche Spaltennamen gibt es

				}) {
			boolean[] columnEditables = new boolean[] {
					// welche spalten lassen sich �ndern

					true, false, true, false, false, false };

			public boolean isCellEditable(int row, int column) {
				// Kontrollmethode ob Spalten sich �ndern lassen

				return columnEditables[column];
			}

		});
		auftraegeMonteurTBL.getTableHeader().setReorderingAllowed(false);
		// Tabellenspalten lassen sich nicht verschieben

		auswahlBoxStatus(auftraegeMonteurTBL, auswahlBoxStatus, 2);
		// Combobox in Spalte Status erstellt

		auftraegeMonteurTBL.getColumn(auftraegeMonteurTBL.getColumnName(0))
				.setCellRenderer(new JButtonRenderer("auftraegeMonteurTBL"));
		auftraegeMonteurTBL.getColumn(auftraegeMonteurTBL.getColumnName(0)).setCellEditor(new JButtonEditor());
		// Button Details erstellen

		suchen(auftraegeMonteurTBL);
		// Suchfunktion implementiert
	}


	/**
	 * Hilfsmethoden: Die Methode zum F�llen der
	 * Tabelle**********************************************
	 *************************************************************************************************
	 */
	private Object[][] auftraege() {
		// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut

		db.getAuftragsListe().clear();
		// alte Liste l�schen

		db.auftragEinlesen();
		// neu einlesen mit aktuellen Daten

		angepassteAuftragsListe.clear();
		// angepassteAuftragsliste wird auch geleert

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
		// �bereinstimmen. Die Zeilen Variable wird �berschrieben

		zeilen = (int) db.getAuftragsListe().stream()
				.filter((a) -> (a.getStatus().equals("disponiert") || a.getStatus().equals("Teile fehlen"))
						&& a.getZustaendig().getMitarbeiterNummer().equals(LoginFenster.getMitarbeiternummer()))

				.count();

		Object[][] auftraege = new Object[zeilen][6];
		// Struktur der Tabelle

		// einlesen der Tabelle aus der angepassten Auftragsliste
		for (int i = 0; i < zeilen; i++) {

			auftraege[i][0] = details;

			auftraege[i][1] = "";
			if (angepassteAuftragsListe.get(i).getAuftragsNummer() != null)
				auftraege[i][1] = angepassteAuftragsListe.get(i).getAuftragsNummer();

			auftraege[i][2] = "";
			if (angepassteAuftragsListe.get(i).getStatus() != null)
				auftraege[i][2] = angepassteAuftragsListe.get(i).getStatus();

			auftraege[i][3] = "";
			if (angepassteAuftragsListe.get(i).getErstellungsdatum() != null)
				auftraege[i][3] = angepassteAuftragsListe.get(i).getErstellungsdatum();

			auftraege[i][4] = "";
			if (angepassteAuftragsListe.get(i).getFrist() != null)
				auftraege[i][4] = angepassteAuftragsListe.get(i).getFrist();

			auftraege[i][5] = "";
			if (angepassteAuftragsListe.get(i).getAuftraggeber().getKundenNummer() != null)
				auftraege[i][5] = angepassteAuftragsListe.get(i).getAuftraggeber().getKundenNummer();

		}
		return auftraege;
	}

	/*
	 * Hilfsmethoden: Erstellen der Combobox, sowie Bef�llen und
	 * Funktionalit�t***************************
	 * ***************************************************************************************************
	 */
	private void auswahlBoxStatus(JTable table, JComboBox combobox, int spalte) {

		combobox.removeAllItems();
		// erstmal alle Items rausl�schen

		// Auswahlm�glichkeiten

		combobox.addItem("Teile fehlen");
		combobox.addItem("disponiert");
		combobox.addItem("im Lager");

		TableColumn statusSpalte = table.getColumnModel().getColumn(spalte);
		// in welche Spalte soll die Combobox

		statusSpalte.setCellEditor(new DefaultCellEditor(combobox));
		// Combobox jetzt anklickbar
	}

	/*
	 * Hilfsmethoden: Status�nderungen werden in die Datenbank eingetragen
	 * *****************************************************************************
	 **/

	/**
	 * Diese Methode erm�glicht es dem Monteur, den Status eines Auftrags zu ver�ndern.
	 */
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

						db.setStatus(auftrag, status); 

						if (auftrag.getStatus().equals("nicht zugewiesen")) {
							for (Mitarbeiter monteur : db.getMonteurListe()) {
								if (monteur.getMitarbeiterNummer().equals("0000")) {
									auftrag.setZustaendig(monteur);
									// wenn der Auftrag "nicht zugewiesen" ist, wird auch der jeweilige Monteur ggf.
									// von diesem Auftrag entfernt
									db.setZustaendig(auftrag, monteur); 
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
					suchFeld.setText("");
					
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