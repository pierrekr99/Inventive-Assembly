package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

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
import objekte.Komponente;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.LayoutStyle.ComponentPlacement;

public class DetailsFenster extends JFrame {

	private JPanel contentPane;
	private JTable tKomponenten;
	private JScrollPane sPKomponenten;
	private datenbankVerbindung verbindung = new datenbankVerbindung();

	int zeilen = 3;
	Object[][] komponenten = new Object[zeilen][6];// Nur das wird später eingelesen
	Object[][] monteur = new Object[2][3];
	private JTable tMonteur;
	private JScrollPane scrollPane;
	private JTable tableMonteur;

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
		setTitle("Auftragsdetails");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Nur dieses Fenster wird Geschlossen
		setBounds(100, 100, 1007, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		sPKomponenten = new JScrollPane();
		sPKomponenten.setBounds(5, 36, 922, 449);
		contentPane.setLayout(null);

		JScrollPane sPMonteur = new JScrollPane();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(sPKomponenten, GroupLayout.DEFAULT_SIZE, 961, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(258)
							.addComponent(sPMonteur, GroupLayout.PREFERRED_SIZE, 463, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addComponent(sPMonteur, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(62)
					.addComponent(sPKomponenten, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(100, Short.MAX_VALUE))
		);

		tableMonteur = new JTable();
		tableMonteur.setModel(
				new DefaultTableModel(new Object [][] {{null,null}}, new String[] { "MitarbeiterID", "Monteur" }));
		sPMonteur.setViewportView(tableMonteur);

		komponenten(row);
		tKomponenten = new JTable();// Neue Tabelle
		tKomponenten.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -größe in der Tabelle
		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -größe in der
																					// Kopfzeile der Tabelle
		tKomponenten.setModel(
				new DefaultTableModel(komponenten, new String[] { "TeileNr.", "Name", "Kategorie", "Verfügbarkeit", })

				{
					boolean[] columnEditables = new boolean[] { // welche spalten lassen sich ändern
							false, false, false, false };

					public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich ändern lassen
						return columnEditables[column];
					}

				});// Generierung
					// der
					// Tabelle
					// Benötigter
					// Inhalt:
					// (String[][],String[])

		sPKomponenten.setViewportView(tKomponenten);

		JPanel panel = new JPanel();
		sPKomponenten.setColumnHeaderView(panel);
		contentPane.setLayout(gl_contentPane);

		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));// formatierung schrift kopf
		tKomponenten.setRowHeight(50); // Zeilen höhe
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 18));// formatierung schrift in tabelle

//		tKomponenten.addMouseListener(new MouseAdapter() {// MouseListener für das Fenster
//			public void mouseClicked(MouseEvent e) {
//				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht
//												// dazu)
//					JTable target = (JTable) e.getSource();
//					int row1 = target.getSelectedRow();// wo wurde geklickt
//					int column = target.getSelectedColumn();
//					// do some action if appropriate column
//					if (column == 3 && ) {// wenn man in der Verfügbarkeitsspalte klickt und die verfügbarkeit false ist
//						JOptionPane.showMessageDialog(null,
//								("Eilbestellung für [" + "Teil"
//										+ "] wurde ausgeführt"));
//					}
////					verbindung.getAuftragsListe().get(row1).getKomponenten().get(row1).getName())
//				}
//			}
//		});

	}

	private void komponenten(int row) {// Komponenten werden aus Komponentensliste ausgelesen und in
		// komponenten[][]eingebaut

		// int row ist die reihe des auftrags um details des jeweiligen auftrags
		// ausgeben zu können

		verbindung.verbinden();
		verbindung.auftraggeberEinlesen();
		verbindung.disponentEinlesen();
		verbindung.komponenteEinlesen();
		verbindung.monteurEinlesen();
		verbindung.auftragEinlesen();

		for (int i = 0; i < verbindung.getAuftragsListe().get(row).getKomponenten().size(); i++) { // fügt Komponenten
																									// eines Auftrags in
			// die Tabelle ein

			komponenten[i][0] = verbindung.getAuftragsListe().get(row).getKomponenten().get(i).getKomponentenNummer();
			komponenten[i][1] = verbindung.getAuftragsListe().get(row).getKomponenten().get(i).getName();
			komponenten[i][2] = verbindung.getAuftragsListe().get(row).getKomponenten().get(i).getKategorie();
			komponenten[i][3] = verbindung.getAuftragsListe().get(row).getKomponenten().get(i).isVerfuegbarkeit();

		}

	}

}
