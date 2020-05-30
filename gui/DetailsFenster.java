package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Datenbank.datenbankVerbindung;
import javax.swing.LayoutStyle.ComponentPlacement;

public class DetailsFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();

	private JPanel contentPane;
	private JTable tKomponenten;
	private JScrollPane sPKomponenten;
	private JScrollPane sPMonteur;
	private JTable tMonteur;

	int zeilen = 5;
	// die anzahl der spalten war vorher auf 6, obwohl jetzt nur 5 gebraucht werden
	Object[][] komponenten = new Object[zeilen][5];// Nur das wird später eingelesen
	Object[][] monteur = new Object[1][5];

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetailsFenster frame = new DetailsFenster(2);
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
	public DetailsFenster(int row) { // reihe des auftrags als parameter

//      -------  Fenster  -----------------------------------------------

		setTitle("Auftragsdetails");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Nur dieses Fenster wird Geschlossen
		setBounds(100, 100, 1060, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JPanel panel = new JPanel();

//		-------  Komponenten Tabelle  ----------------------------------------

		komponenten(row);
		sPKomponenten = new JScrollPane();
		sPKomponenten.setBounds(5, 36, 922, 449);
		tKomponenten = new JTable();// Neue Tabelle
		tKomponenten.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		tKomponenten.setModel(new DefaultTableModel(komponenten,
				new String[] { "TeileNummer",  "Name", "Attribut", "Kategorie", "Verfügbarkeit", })

		{
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
					false, false, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
				return columnEditables[column];
			}
		});// Generierung der Tabelle Benötigter Inhalt: (String[][],String[])
		sPKomponenten.setViewportView(tKomponenten);
		sPKomponenten.setColumnHeaderView(panel);

		eilbestellen();

//      -------  Monteur Tabelle  ----------------------------------------

		autragMonteur(row);
		sPMonteur = new JScrollPane();
		tMonteur = new JTable();
		tMonteur.setModel(
				new DefaultTableModel(monteur, new String[] { "AuftragsNummer", "KundenNummer","Auftraggeber", "MonteurNummer", "MonteurName" }));

		sPMonteur.setViewportView(tMonteur);

//		-------  Formatierung  -------------------------------------------------

		tMonteur.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// formatierung schrift kopf
		tMonteur.setRowHeight(50); // Zeilen höhe
		tMonteur.setFont(new Font("Tahoma", Font.PLAIN, 18));// formatierung schrift in tabelle
		monteureTblFormat();
		

		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// formatierung schrift kopf
		tKomponenten.setRowHeight(50); // Zeilen höhe
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 18));// formatierung schrift in tabelle
		
		tKomponenten.setAutoCreateRowSorter(true);// durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Komponenten nach diesem Attribut
												// in der natürlichen Ordnung und umgekehrt sortiert

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(sPMonteur, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
						.addComponent(sPKomponenten, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(sPMonteur, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addGap(47)
					.addComponent(sPKomponenten, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(101, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);

	}
	
	private void monteureTblFormat() {
		tMonteur.getColumnModel().getColumn(0).setPreferredWidth(150);
		tMonteur.getColumnModel().getColumn(0).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(0).setMaxWidth(200);

		tMonteur.getColumnModel().getColumn(1).setPreferredWidth(100);
		tMonteur.getColumnModel().getColumn(1).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(1).setMaxWidth(200);

		tMonteur.getColumnModel().getColumn(2).setPreferredWidth(100);
		tMonteur.getColumnModel().getColumn(2).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(2).setMaxWidth(500);

		tMonteur.getColumnModel().getColumn(3).setPreferredWidth(100);
		tMonteur.getColumnModel().getColumn(3).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(3).setMaxWidth(200);

		tMonteur.getColumnModel().getColumn(4).setPreferredWidth(100);
		tMonteur.getColumnModel().getColumn(4).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(4).setMaxWidth(500);
		
		
	}

	private void eilbestellen() {

		tKomponenten.addMouseListener(new MouseAdapter() {// MouseListener für das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht
												// dazu)
					JTable target = (JTable) e.getSource();
					int row1 = target.getSelectedRow();// wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 3 && komponenten[row1][column].equals(false)) {// wenn man in der Verfügbarkeitsspalte
																					// klickt und die verfügbarkeit
																					// false ist
						JOptionPane.showMessageDialog(null,
								("Eilbestellung für [" + komponenten[row1][1] + "] wurde ausgeführt"));
						// name der komponenten wird ausgegeben (komponenten spalte 1)
					}
				}
			}
		});

	}

	private void komponenten(int row) {// Komponenten werden aus Komponentensliste ausgelesen und in
		// komponenten[][]eingebaut

		// int row ist die reihe des auftrags um details des jeweiligen auftrags
		// ausgeben zu können

		for (int i = 0; i < db.getAuftragsListe().get(row).getKomponenten().size(); i++) { // fügt Komponenten
																							// eines Auftrags in
																							// die Tabelle ein

			komponenten[i][0] = db.getAuftragsListe().get(row).getKomponenten().get(i).getKomponentenNummer();
			komponenten[i][1] = db.getAuftragsListe().get(row).getKomponenten().get(i).getName();
			komponenten[i][2] = db.getAuftragsListe().get(row).getKomponenten().get(i).getAttribut();
			komponenten[i][3] = db.getAuftragsListe().get(row).getKomponenten().get(i).getKategorie();
			komponenten[i][4] = db.getAuftragsListe().get(row).getKomponenten().get(i).isVerfuegbarkeit();
			
		}

	}

	private void autragMonteur(int row) { // fügt auftragsnummer monteurname und nummer in tabelle ein

		monteur[0][0] = db.getAuftragsListe().get(row).getAuftragsNummer();
		monteur[0][1] = db.getAuftragsListe().get(row).getAuftraggeber().getKundenNummer();	
		monteur[0][2] = db.getAuftragsListe().get(row).getAuftraggeber().getName();	
		monteur[0][3] = db.getAuftragsListe().get(row).getZustaendig().getMitarbeiterNummer(); 
		monteur[0][4] = db.getAuftragsListe().get(row).getZustaendig().getVorname() + " "
				+ db.getAuftragsListe().get(row).getZustaendig().getName();

	}

}
