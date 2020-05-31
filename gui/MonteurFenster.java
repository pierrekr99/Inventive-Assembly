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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Datenbank.datenbankVerbindung;
import objekte.Auftrag;
import objekte.Auftraggeber;

public class MonteurFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();

	private JTextField suchFeld;
	private JTable auftraegeMonteurTBL;
	private JPanel contentPane;

	int zeilen = 0; // zeilen tabelle

	String details = "Details";// Hier könnte man den Detailsbutton Rendern

	JComboBox auswahlBoxStatus = new JComboBox();

	private ArrayList<Auftrag> angepassteAuftragsListe = new ArrayList<Auftrag>();

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonteurFenster window = new MonteurFenster();

					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MonteurFenster() {

		setTitle("Inventive Assembly Monteur Auftragsansicht");// Namen setzen
		setExtendedState(JFrame.MAXIMIZED_BOTH);// Großansicht
		setBounds(0, 0, 700, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// fenster schließen bei x
		GridBagLayout gridBagLayout = new GridBagLayout();// layout von hier
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 362, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);// bis hier

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);// layout für tabbed pane von hier
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16)); // schriftgröße
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);// bis hier

		JPanel auftraegeTab = new JPanel();
		tabbedPane.addTab("Aufträge", null, auftraegeTab, null);// tab sichtbar

		suchFeld = new JTextField(); // suchfeld erstellen
		suchFeld.setFont(new Font("Tahoma", Font.PLAIN, 16));// formatierung schrift
		suchFeld.setText("search");// suchfeld name
		suchFeld.setColumns(10);

		JButton logoutKnopf = new JButton("logout");// logout button erstellen
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));// formatierung schrift
		logoutKnopf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {// logout befehl...zurück zum login

				LoginFenster login = new LoginFenster();// loginfenster erstellen
				login.setVisible(true);
				dispose();// aktuelles Fenster schließen

			}
		});

		/**
		 * layout regelungen für scroll pane und anordnung der
		 * komponenten*************************************************************************
		 * ************************************************************************************************
		 */

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton dbAktualisierenKnopf = new JButton("DB aktualisieren");
		dbAktualisierenKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));// formatierung schrift
		dbAktualisierenKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabelleInArrayEinlesen();
				auftraegeAktualisieren();
				auswahlBoxStatus();
				
				System.out.println("--------------------aufträge-----------");
				
				for (Auftrag auftrag : db.getAuftragsListe()) {
					System.out.println(auftrag);
				}
			}
		});
		
		GroupLayout gl_auftraegeTab = new GroupLayout(auftraegeTab);
		gl_auftraegeTab.setHorizontalGroup(
			gl_auftraegeTab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_auftraegeTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_auftraegeTab.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
						.addGroup(gl_auftraegeTab.createSequentialGroup()
							.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
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
						.addComponent(dbAktualisierenKnopf))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
					.addContainerGap())
		);

		/**
		 * ***********************************************************************************************
		 * ************************************************************************************************
		 */

		auftraegeMonteurTBL = new JTable();// tabelle erstellen
		auftraegeMonteurTBL.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// formatierung schrift kopf
		auftraegeAktualisieren();

		auftraegeMonteurTBL.setRowHeight(50); // Zeilen höhe
		auftraegeMonteurTBL.setAutoCreateRowSorter(true); // durch Anklicken der Kopfzeile (in der jeweiligen Spalte)
															// werden die Aufträge nach diesem Attribut
															// in der natürlichen Ordnung und umgekehrt sortiert

		auswahlBoxStatus();

		auftraegeMonteurTBL.addMouseListener(new MouseAdapter() {// MouseListener für das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht
												// dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();// wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 0) {// wenn in DetailsSpalte
						detailsFenster(row);// Detailsfenster wird geöffnet und reihe des auftrags wird übergeben um
											// details aufrufen zu können

					}
				}
			}
		});

		auftraegeMonteurTBL.setFont(new Font("Tahoma", Font.PLAIN, 18));// formatierung schrift in tabelle
		scrollPane.setViewportView(auftraegeMonteurTBL);
		auftraegeTab.setLayout(gl_auftraegeTab);

	}

	private void auswahlBoxStatus() {
		
		auswahlBoxStatus.removeAllItems(); 
		
		// auswahlmöglichkeiten
		
		auswahlBoxStatus.addItem("Teile fehlen");
		auswahlBoxStatus.addItem("disponiert");
		auswahlBoxStatus.addItem("Im Lager");

		TableColumn statusSpalte = auftraegeMonteurTBL.getColumnModel().getColumn(2);// auskommentiert weil das bei
																						// design ansicht weggemacht
																						// wird
		statusSpalte.setCellEditor(new DefaultCellEditor(auswahlBoxStatus));
	}

	/**
	 * hilfsmethoden Die methode zum Füllen der
	 * Tabelle*************************************************************************
	 * ************************************************************************************************
	 */
	private Object[][] auftraege() {// Aufträge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut

		zeilen = // wie viele zeilen hat die Tabelle(mit einem Collection.stream() ermittelt)
				(int) db.getAuftragsListe().stream().filter(
						(a) -> a.getZustaendig().getMitarbeiterNummer().equals(LoginFenster.getMitarbeiternummer())) // alle
																														// Aufträge
																														// von
																														// anderen
																														// Monteuren
																														// werden
																														// aussortiert,
																														// Vergleich
																														// über
																														// die
																														// Mitarbeiternummer
																														// und
																														// der
																														// Eingabe
																														// im
																														// tf_MitarbeiterID.
						.count(); // zählen der übrigen Aufträge

//		angepassteAuftragsListe
		db.getAuftragsListe().stream()
				.filter((a) -> a.getZustaendig().getMitarbeiterNummer().equals(LoginFenster.getMitarbeiternummer())) // alle
																														// Aufträge
																														// von
																														// anderen
																														// Monteuren
																														// werden
																														// aussortiert,
																														// Vergleich
																														// über
																														// die
																														// Mitarbeiternummer
																														// und
																														// der
																														// Eingabe
																														// im
																														// tf_MitarbeiterID.
				.forEach(angepassteAuftragsListe::add); // übrigen Aufträge werden der angepassten Auftragsliste
														// hinzugefügt

		Object[][] auftraege = new Object[zeilen][6];// Struktur Tabelle
		for (int i = 0; i < zeilen; i++) {
			auftraege[i][0] = details;
			auftraege[i][1] = angepassteAuftragsListe.get(i).getAuftragsNummer();// angepassteAuftragsliste.get(zeile).getAuftragsnr()
			auftraege[i][2] = angepassteAuftragsListe.get(i).getStatus();
			auftraege[i][3] = angepassteAuftragsListe.get(i).getFrist();
			auftraege[i][4] = angepassteAuftragsListe.get(i).getErstellungsdatum();
			auftraege[i][5] = angepassteAuftragsListe.get(i).getAuftraggeber().getKundenNummer();
		}

		return auftraege;
	}

	/**
	 * *************************************************************************************************
	 */

	private void detailsFenster(int row) {// Öffnet Detailsfenster
		try {
			DetailsFenster detailsFenster = new DetailsFenster(row);// Fenster wird erstellt
			detailsFenster.setVisible(true);// Fenster wird sichtbar
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void auftraegeAktualisieren() {
		auftraegeMonteurTBL.setModel(new DefaultTableModel(// befüllung
				auftraege(),

//				,
				new String[] { "", "Auftragsnummer", "Status", "Datum", "Frist", "Auftraggeber"// welche spaltennamen
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
					false, false, true, false, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
				return columnEditables[column];
			}
		});
	}

	private void tabelleInArrayEinlesen() {
		for (int i = 0; i < zeilen; i++) {
			for (Auftrag auftrag : db.getAuftragsListe()) {
				if (auftrag.getAuftragsNummer().equals(auftraegeMonteurTBL.getValueAt(i, 1))) {
					String status = auftraegeMonteurTBL.getValueAt(i, 2).toString();
					if (!auftrag.getStatus().equals(status)) {
						auftrag.setStatus(status);
					}
				}
			}
		}

	}
}