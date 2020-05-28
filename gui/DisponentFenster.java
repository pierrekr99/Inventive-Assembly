package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DisponentFenster extends JFrame {
	
	static datenbankVerbindung db = main.Main.getdb();

	private JPanel contentPane;
	private JTextField txtSuche;
	private JTable auftraegeTbl;
	private JTable monteureTbl;

	int zeilen = 0;
	int zeilenMonteure = 0;
	int summeAuftraege = 0;
	String details = "Details";// Hier k�nnte man den Detailsbutton Rendern

	JComboBox monteureCombobox = new JComboBox(); // erstellung einer Combobox
	JComboBox auftraegeCombobox = new JComboBox();

	/**
	 * Launch the application.
	 */

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { DisponentFenster frame = new
	 * DisponentFenster(); frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster
	 * Vollbild frame.setVisible(true); } catch (Exception e) { e.printStackTrace();
	 * } } }); }
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

		JButton logoutKnopf = new JButton("Logout");// Logout schlie�t das fenster und �ffnet das LoginFenster
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		logoutKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFenster login = new LoginFenster();
				login.setVisible(true);
				dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 797, Short.MAX_VALUE)
								.addComponent(logoutKnopf)))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(logoutKnopf))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE).addContainerGap()));

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
		monteureCombobox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		auftraegeTbl.setModel(new DefaultTableModel(auftraege(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "", "AuftragsNummer", "Status", "Erstellungsdatum", "Frist", "MonteurName",
						"MonteurNummer", "Auftragsgeber" }) {
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

		String monteur;
		monteureCombobox.addActionListener(null
				//zugewiesenen Monteur auslesen und in Datenbank zuweisung �ndern
				);
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

		monteureTbl.setModel(new DefaultTableModel(monteure(), // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "Name", "MittarbeiterNummer", "Verf�gbarkeit", "Auftraege"// welche spaltennamen
				}) {
			boolean[] columnEditables = new boolean[] { // welche spalten lassen sich �ndern
					false, false, false, true };

			public boolean isCellEditable(int row, int column) {// kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});
		monteureTblFormat();
		auftraegeTblFormat();
		TableColumn auftraegeColumn = monteureTbl.getColumnModel().getColumn(3);// eine bestimmte Spalte f�r Combobox
																				// ausw�hlen
		auftraegeColumn.setCellEditor(new DefaultCellEditor(auftraegeCombobox));// in die Spalte die Combobox einbinden

		contentPane.setLayout(gl_contentPane);

		auftraegeCombobox.addActionListener(null
				//Soll die Ver�nderung der Zelle verhindern, damit nur die Auftragsnummern im Dropdown angezeigt werden
				//Hier Den inhalt der Zelle wieder auf ""Summe: " + summeAuftraege(i) + "         Details"" setzen
				);
		
		auftraegeTbl.addMouseListener(new MouseAdapter() {// MouseListener f�r das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {// Wenn die Maus Gedr�ckt wird (Beim Dr�cken die Maus bewegen z�hlt nicht
												// dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();// wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 0) {// wenn in DetailsSpalte
//						detailsFenster();//Detailsfenster wird ge�ffnet
						DetailsFenster frame = new DetailsFenster(row); // reihe des Auftrags wird �bergeben um details aufrufen zu k�nnen
						frame.setVisible(true);
					}
				}
			}
		});
	}

	private void auftraegeTblFormat() {
		monteureTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		monteureTbl.getColumnModel().getColumn(0).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(0).setMaxWidth(400);

		monteureTbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(1).setMaxWidth(300);

		monteureTbl.getColumnModel().getColumn(2).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(2).setMaxWidth(400);

		monteureTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMinWidth(100);
		monteureTbl.getColumnModel().getColumn(3).setMaxWidth(400);

		monteureTbl.setRowHeight(50);
	}

	private void monteureTblFormat() {
		auftraegeTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
		auftraegeTbl.getColumnModel().getColumn(0).setMinWidth(100);
		auftraegeTbl.getColumnModel().getColumn(0).setMaxWidth(100);

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

	/**
	 * Hilfsmethoden
	 */

	private void auftraegeCombobox(int j) {// Auftr�ge dropdown
		// auftraegeCombobox.removeAllItems();
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			if (db.getAuftragsListe().get(i).getZustaendig().getMitarbeiterNummer()
					.equals(db.getMonteurListe().get(j).getMitarbeiterNummer())) {
				auftraegeCombobox.addItem(db.getAuftragsListe().get(i).getAuftragsNummer());
			}
		}
	}

	private String summeAuftraege(int i) {// z�hlt die zugeh�rigen Auftr�ge des Monteurs
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

	private Object[][] monteure() {// Erstellt Tabelle mit Monteuren
		zeilenMonteure = db.getMonteurListe().size();
		Object[][] monteure = new Object[zeilenMonteure][4];// Nur das wird sp�ter eingelesen
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteure[i][0] = db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName();
			monteure[i][1] = db.getMonteurListe().get(i).getMitarbeiterNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			monteure[i][2] = db.getMonteurListe().get(i).getAnwesenheit();
			monteure[i][3] = "Summe: " + summeAuftraege(i) + "         Details";// Dropdown fehlt noch
			auftraegeCombobox(i);
		}
		return monteure;
	}

	private void monteureCombobox() {// F�gt Optionen zur Statusver�nderung hinzu
		monteureCombobox.removeAllItems();
		for (int i = 0; i < db.getMonteurListe().size(); i++) {
			monteureCombobox
					.addItem(db.getMonteurListe().get(i).getVorname() + " " + db.getMonteurListe().get(i).getName());
		}
	}

	private Object[][] auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		zeilen = db.getAuftragsListe().size();
		Object[][] auftraege = new Object[zeilen][8];// Nur das wird sp�ter eingelesen
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


}
