package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import Datenbank.datenbankVerbindung;
import objekte.Komponente;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class DetailsFenster extends JFrame implements TableCellRenderer {

	private JPanel contentPane;
	private JTable tKomponenten;
	private JScrollPane sPKomponenten;
	private datenbankVerbindung verbindung = new datenbankVerbindung();

	int zeilen = 10;// Aufragliste.size; Die Anzahl der Zeilen, die die Tabelle hat
	int zeile = 0;// Zeile in der der Neue Auftrag eingef�gt wird

	Object[][] komponenten = new Object[zeilen][6];// Nur das wird sp�ter eingelesen
	private JPanel panel_1;

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

		sPKomponenten = new JScrollPane();
		

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(sPKomponenten, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addComponent(sPKomponenten, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
		);

		komponenten();
		tKomponenten = new JTable();// Neue Tabelle
		tKomponenten.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -gr��e in der Tabelle
		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -gr��e in der
																					// Kopfzeile der Tabelle
		tKomponenten.setModel(
				new DefaultTableModel(komponenten, new String[] { "TeileNr.", "Name", "Kategorie", "Verf�gbarkeit", }));// Generierung
																														// der
																														// Tabelle
																														// Ben�tigter
																														// Inhalt:
																														// (String[][],String[])

		sPKomponenten.setViewportView(tKomponenten);

		JPanel panel = new JPanel();
		sPKomponenten.setColumnHeaderView(panel);
		contentPane.setLayout(gl_contentPane);

		tKomponenten.addMouseListener(new MouseAdapter() {// MouseListener f�r das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedr�ckt wird (Beim Dr�cken die Maus bewegen z�hlt nicht
												// dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();// wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 3 && verbindung.getKomponentenlisteauftrag().get(row).isVerfuegbarkeit() == false) {// wenn
																														// man
																														// in
																														// Verf�gbarkeitsspalte
																														// klickt
																														// und
																														// die
																														// verf�gbarkeit
																														// false
																														// ist

						JOptionPane.showMessageDialog(null,
								("Eilbestellung f�r [" + verbindung.getKomponentenlisteauftrag().get(row).getName())
										+ "] wurde ausgef�hrt");
					}
				}
			}
		});

	}

	private void komponenten() {// Komponenten werden aus Komponentensliste ausgelesen und in
								// komponenten[][]eingebaut

		verbindung.verbinden();
		verbindung.auftraggeberEinlesen();
		verbindung.disponentEinlesen();
		verbindung.komponenteEinlesen();
		verbindung.monteurEinlesen();
		verbindung.auftragEinlesen();

		for (int i = 0; i < verbindung.getKomponentenlisteauftrag().size(); i++) { // f�gt Komponenten eines Auftrags in
																					// die Tabelle ein
			komponenten[i][0] = verbindung.getKomponentenlisteauftrag().get(i).getKomponentenNummer();
			komponenten[i][1] = verbindung.getKomponentenlisteauftrag().get(i).getName();
			komponenten[i][2] = verbindung.getKomponentenlisteauftrag().get(i).getKategorie();
			komponenten[i][3] = verbindung.getKomponentenlisteauftrag().get(i).isVerfuegbarkeit();
		}

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}

}
