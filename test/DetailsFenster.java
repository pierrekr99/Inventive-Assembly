package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import Datenbank.datenbankVerbindung;
import objekte.Komponente;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class DetailsFenster extends JFrame implements TableCellRenderer {

	private JPanel contentPane;
	private JTable tKomponenten;
	private datenbankVerbindung verbindung = new datenbankVerbindung();

	int zeilen = 5;// Aufragliste.size; Die Anzahl der Zeilen, die die Tabelle hat
	int zeile = 0;// Zeile in der der Neue Auftrag eingefügt wird

	Object[][] komponenten = new Object[zeilen][6];// Nur das wird später eingelesen
	String[] komponente = new String[4];// Ein Auftrag wird als Zeile erstellt (Zeile mit 4 Spalten)

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetailsFenster frame = new DetailsFenster();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DetailsFenster() {
		setTitle("Auftragsdetails");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Nur dieses Fenster wird Geschlossen
		setBounds(100, 100, 800, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane sPAuftraege = new JScrollPane();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(sPAuftraege, GroupLayout.DEFAULT_SIZE, 1574, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addGap(31).addComponent(sPAuftraege, GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)));

		komponenten();
		tKomponenten = new JTable();// Neue Tabelle
		tKomponenten.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -größe in der Tabelle
		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -größe in der
																					// Kopfzeile der Tabelle
		tKomponenten.setModel(new DefaultTableModel(komponenten, // Benötigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingefügt
				new String[] { "TeileNr.", "Name", "Kategorie", "Verfügbarkeit", }));// Generierung der
																						// Tabelle
		sPAuftraege.setViewportView(tKomponenten);

		JPanel panel = new JPanel();
		sPAuftraege.setColumnHeaderView(panel);
		contentPane.setLayout(gl_contentPane);

	}

	private void komponenten() {// Komponenten werden aus Komponentensliste ausgelesen und in
								// komponenten[][]eingebaut

		verbindung.verbinden();

//		verbindung.komponenteEinlesen();
//		verbindung.auftragEinlesen();
		

	}

//for (int i = 0; i < db.getKomponentenListe().size(); i++) {
//	auftraege[i][0] = db.getKomponentenListe().get(i).getKomponentenNummer();
//	auftraege[i][1] = db.getAuftragsListe().get(i).getAuftragsNummer();
//	auftraege[i][2] = db.getAuftragsListe().get(i).getStatus();
//	auftraege[i][3] = db.getAuftragsListe().get(i).getFrist();

//}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}

}
