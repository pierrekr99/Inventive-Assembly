package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Datenbank.datenbankVerbindung;

public class DisponentFenster extends JFrame {

	private JPanel contentPane;
	private JTextField txtSuche;
	private JTable auftraegeTbl;
	private JTable monteureTbl;

	int zeilen = 0;
	int zeilenMonteure = 0;
	int summeAuftraege = 0;
	String details = "Details";// Hier k�nnte man den Detailsbutton Rendern

	datenbankVerbindung db = new datenbankVerbindung();
	JComboBox monteureCombobox = new JComboBox(); // erstellung einer Combobox
	JComboBox auftraegeCombobox = new JComboBox();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisponentFenster frame = new DisponentFenster();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster Vollbild
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
	public DisponentFenster() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtSuche = new JTextField();
		txtSuche.setText("Suche");
		txtSuche.setColumns(10);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE).addComponent(txtSuche,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		/**
		 * Auftraege Reiter.
		 */
		JScrollPane auftraegeSp = new JScrollPane();
		tabbedPane.addTab("Auftr�ge", null, auftraegeSp, null);

		auftraegeTbl = new JTable();

		auftraegeSp.setViewportView(auftraegeTbl);
		auftraegeTbl.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		auftraegeTbl.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -gr��e in der Tabelle
		auftraegeTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -gr��e in der
																					// Kopfzeile der Tabelle
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "", "Auftragsnr", "Status", "Erstellungsdatum", "Frist", "MonteurName", "MonteurNummer",
						"Auftragsgeber" }) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					false, false, false, false, false, true, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});
		monteureCombobox();
		TableColumn monteureColumn = auftraegeTbl.getColumnModel().getColumn(5);// eine bestimmte Spalte f�r Combobox
																				// ausw�hlen
		monteureColumn.setCellEditor(new DefaultCellEditor(monteureCombobox));// in die Spalte die Combobox einbinden

		/**
		 * Monteure Reiter.
		 */
		JScrollPane monteureSp = new JScrollPane();
		tabbedPane.addTab("Monteure", null, monteureSp, null);

		monteureTbl = new JTable();
		monteureSp.setViewportView(monteureTbl);
		monteureTbl.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		monteureTbl.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -gr��e in der Tabelle
		monteureTbl.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -gr��e in der
																					// Kopfzeile der Tabelle
		monteureTbl.setModel(new DefaultTableModel(monteure(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "Name", "MittarbeiterNummer", "Verf�gbarkeit", "Auftraege"// welche spaltennamen
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					false, false, false, false };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});
		

		contentPane.setLayout(gl_contentPane);
	}

	private String summeAuftraege(int i) {
		String summe;
		for (int j = 0; j < db.getAuftragsListe().size(); j++) {
			if (db.getAuftragsListe().get(j).getZustaendig().getMitarbeiterNummer()
					.equals(db.getMonteurListe().get(i).getMitarbeiterNummer())) {
				/*
				 * Hier wird die MitarbeiterNummer des Zust�ndigen Mitarbeitersi einem Auftrag
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


	private Object[][] monteure() {
		db.verbinden();
		db.auftraggeberEinlesen();
		db.disponentEinlesen();
		db.komponenteEinlesen();
		db.monteurEinlesen();
		db.auftragEinlesen();
		zeilenMonteure = db.getMonteurListe().size();
		Object[][] monteure = new Object[zeilenMonteure][4];// Nur das wird sp�ter eingelesen
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteure[i][0] = db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName();
			monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
			monteure[i][3] = "Summe: " + summeAuftraege(i) + "         Details";//Dropdown fehlt noch
		}
		return monteure;
	}

	private void monteureCombobox() {
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteureCombobox.addItem(db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName());
		}
	}

	private Object[][] auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		db.verbinden();
		db.auftraggeberEinlesen();
		db.disponentEinlesen();
		db.komponenteEinlesen();
		db.monteurEinlesen();
		db.auftragEinlesen();
		zeilen = db.getAuftragsListe().size();
		Object[][] auftraege = new Object[zeilen][8];// Nur das wird sp�ter eingelesen
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			auftraege[i][0] = details;
			auftraege[i][1] = db.getAuftragsListe().get(i).getAuftragsNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			auftraege[i][2] = db.getAuftragsListe().get(i).getStatus();
			auftraege[i][3] = db.getAuftragsListe().get(i).getErstellungsdatum();
			auftraege[i][4] = db.getAuftragsListe().get(i).getFrist();
			auftraege[i][5] = db.getAuftragsListe().get(i).getZustaendig().getVorname() + " "
					+ db.getAuftragsListe().get(i).getZustaendig().getName();
			auftraege[i][6] = db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer();
			auftraege[i][7] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
		}
		return auftraege;
	}

}
