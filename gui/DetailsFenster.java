package gui;

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

import datenbank.DatenbankVerbindung;
import objekte.Auftrag;

public class DetailsFenster extends JFrame {

	static DatenbankVerbindung db = main.Main.getdb();
	private JPanel contentPane;
	private JTable tblKomponenten;
	private JTable tblMonteur;
	private JScrollPane sPKomponenten;
	private JScrollPane sPMonteur;

	/**
	 * Erstellt ein Fenster mit Auftragsdetails.
	 * @param auftrag Auftrag, von welchem man die Details sehen möchte
	 */
	public DetailsFenster(Auftrag auftrag) { 

		setTitle("Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Nur dieses Fenster wird geschlossen

		setBounds(100, 100, 1060, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setIconImage(LoginFenster.getImage());

		
		erstellenKomponetenTabelle(auftrag);
		erstellenMonteurTabelle(auftrag);
		eilbestellen();
		komponentenTblFormat();
		monteureTblFormat();

		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(sPMonteur, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014,
										Short.MAX_VALUE)
								.addComponent(sPKomponenten, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(sPMonteur, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE).addGap(46)
						.addComponent(sPKomponenten, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Tabelle für die Komponenten wird erstellt
	 * @param auftrag Auftrag, von welchem man die Details sehen möchte
	 */
	private void erstellenKomponetenTabelle(Auftrag auftrag) {
		
		sPKomponenten = new JScrollPane();
		sPKomponenten.setBounds(5, 36, 922, 449);
		tblKomponenten = new JTable();
		tblKomponenten.setCellSelectionEnabled(true);

		tblKomponenten.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben

		tblKomponenten.setModel(new DefaultTableModel(befuellenKomponentenTabelle(auftrag),
				new String[] { "TeileNummer", "Name", "Attribut", "Kategorie", "Verfügbarkeit" }) {
			// DefaultTableModel(Tabelle,Kopfzeile)

			boolean[] columnEditables = new boolean[] { false, false, false, false, false };
			// welche Spalten lassen sich ändern

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
			// Kontrollmethode ob spalten sich ändern lassen
		});

		sPKomponenten.setViewportView(tblKomponenten);

	}

	/**
	 * Tabelle für den zugewiesenen Monteur und Auftragsgeber wird erstellt
	 * @param auftrag Auftrag, von welchem man die Details sehen möchte
	 */
	private void erstellenMonteurTabelle(Auftrag auftrag) {

		sPMonteur = new JScrollPane();
		sPMonteur.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		tblMonteur = new JTable();

		// DefaultTableModel(Tabelle,Kopfzeile)
		tblMonteur.setModel(new DefaultTableModel(befuellenMonteurTabelle(auftrag),
				new String[] { "AuftragsNummer", "KundenNummer", "Auftraggeber", "MonteurNummer", "MonteurName" }) {

			boolean[] columnEditables = new boolean[] { false, false, false, false, false };
			// welche spalten lassen sich ändern

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
			// kontrollmethode ob spalten sich ändern lassen

		});

		sPMonteur.setViewportView(tblMonteur);
	}
	
	/**
	 * Eine Nachricht erscheint wenn die Verfügbarkeit einer Komponenten 
	 * angeklickt wird und diese nicht verfügbar ist 
	 */
	private void eilbestellen() {
		
		tblKomponenten.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {
					// Wenn die Maus Gedrückt wird

					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// Welches Objekt/Reihe/Spalte wurde ausgewählt

					if (column == 4 && tblKomponenten.getValueAt(row, column).equals("nicht verfügbar")) {
						// Wenn Spalte 4 (Verfügbarkeit) ausgewäglt wurde und diese "nicht verfügbar
						// beinhaltet"

						JOptionPane.showMessageDialog(null, ("Eilbestellung für [" + tblKomponenten.getValueAt(row, 1)
								+ " " + tblKomponenten.getValueAt(row, 2) + "] wurde ausgeführt"));
						// Nachricht über Eilbestellung inkl. Name der fehlenden Komponente
					}
				}
			}
		});

	}

	/**
	 * Formatiert die Tabelle tMonteur
	 */
	private void monteureTblFormat() {
		// Festlegung der Größe der Spalten
		tblMonteur.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		tblMonteur.setRowHeight(50);
		tblMonteur.setFont(new Font("Tahoma", Font.PLAIN, 18));

		tblMonteur.getColumnModel().getColumn(0).setPreferredWidth(150);
		tblMonteur.getColumnModel().getColumn(0).setMinWidth(100);
		tblMonteur.getColumnModel().getColumn(0).setMaxWidth(400);

		tblMonteur.getColumnModel().getColumn(1).setPreferredWidth(175);
		tblMonteur.getColumnModel().getColumn(1).setMinWidth(100);
		tblMonteur.getColumnModel().getColumn(1).setMaxWidth(300);

		tblMonteur.getColumnModel().getColumn(2).setPreferredWidth(150);
		tblMonteur.getColumnModel().getColumn(2).setMinWidth(100);
		tblMonteur.getColumnModel().getColumn(2).setMaxWidth(400);

		tblMonteur.getColumnModel().getColumn(3).setPreferredWidth(125);
		tblMonteur.getColumnModel().getColumn(3).setMinWidth(150);
		tblMonteur.getColumnModel().getColumn(3).setMaxWidth(300);

		tblMonteur.getColumnModel().getColumn(4).setPreferredWidth(150);
		tblMonteur.getColumnModel().getColumn(4).setMinWidth(100);
		tblMonteur.getColumnModel().getColumn(4).setMaxWidth(400);

		tblMonteur.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben
	}

	/**
	 * Formatiert die Tabelle tKomponenten aktiviert RowSorter
	 */
	private void komponentenTblFormat() {
		// Festlegung der Größe der Spalten
		tblKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		tblKomponenten.setRowHeight(50);
		tblKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 18));

		tblKomponenten.setAutoCreateRowSorter(true);
		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die
		 * Komponenten nach diesem Attribut in der natürlichen Ordnung und umgekehrt
		 * sortiert
		 */

		tblKomponenten.getColumnModel().getColumn(0).setPreferredWidth(150);
		tblKomponenten.getColumnModel().getColumn(0).setMinWidth(100);
		tblKomponenten.getColumnModel().getColumn(0).setMaxWidth(400);

		tblKomponenten.getColumnModel().getColumn(1).setPreferredWidth(175);
		tblKomponenten.getColumnModel().getColumn(1).setMinWidth(100);
		tblKomponenten.getColumnModel().getColumn(1).setMaxWidth(300);

		tblKomponenten.getColumnModel().getColumn(2).setPreferredWidth(150);
		tblKomponenten.getColumnModel().getColumn(2).setMinWidth(100);
		tblKomponenten.getColumnModel().getColumn(2).setMaxWidth(400);

		tblKomponenten.getColumnModel().getColumn(3).setPreferredWidth(125);
		tblKomponenten.getColumnModel().getColumn(3).setMinWidth(150);
		tblKomponenten.getColumnModel().getColumn(3).setMaxWidth(300);

		tblKomponenten.getColumnModel().getColumn(4).setPreferredWidth(150);
		tblKomponenten.getColumnModel().getColumn(4).setMinWidth(100);
		tblKomponenten.getColumnModel().getColumn(4).setMaxWidth(400);

		tblKomponenten.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben
	}
	
	/**
	 * Befüllt ein Array mit den Komponenten des Auftrags,
	 * indem die Daten aus dem jeweiligen Auftrag gelesen
	 * werden.
	 * 
	 * @param auftrag Auftrag, von welchem man die Details sehen möchte
	 * @return Array mit jeweiligen Komponenten
	 */
	private Object[][] befuellenKomponentenTabelle(Auftrag auftrag) {
		
		int zeilen = auftrag.getKomponenten().size();
		Object[][] komponenten = new Object[zeilen][5];

		for (int i = 0; i < auftrag.getKomponenten().size(); i++) {
			// fügt Komponenten eines Auftrags in das Array ein

			komponenten[i][0] = "";
			if (auftrag.getKomponenten().get(i).getKomponentenNummer() != null)
				komponenten[i][0] = auftrag.getKomponenten().get(i).getKomponentenNummer();
			// Erste Spalte: KomponentenNummer

			komponenten[i][1] = "";
			if (auftrag.getKomponenten().get(i).getName() != null)
				komponenten[i][1] = auftrag.getKomponenten().get(i).getName();
			// Zweite Spalte: Name der Komponente

			komponenten[i][2] = "";
			if (auftrag.getKomponenten().get(i).getAttribut() != null)
				komponenten[i][2] = auftrag.getKomponenten().get(i).getAttribut();
			// Dritte Spalte: Attribut

			komponenten[i][3] = "";
			if (auftrag.getKomponenten().get(i).getKategorie() != null)
				komponenten[i][3] = auftrag.getKomponenten().get(i).getKategorie();
			// Vierte Spalte: Kategorie

			if (auftrag.getKomponenten().get(i) != null && auftrag.getKomponenten().get(i).isVerfuegbarkeit() == true) {
				// Wenn eine Komponente im Auftrag eingetragen ist und die Verfügbarkeit "true"
				// ist

				komponenten[i][4] = "verfügbar";
				// Fünfte Spalte: "verfügbar"

			} else {
				komponenten[i][4] = "nicht verfügbar";
				// Ansonsten "Nicht verfügbar"
			}
		}

		return komponenten;
	}

	/**
	 * Befüllt ein Array mit der Auftragsnummer, Kundennummer, Auftragssgeber und
	 * dem zugwiesenen Monteur, indem die Daten aus dem jeweiligen Auftrag gelesen
	 * werden.
	 * 
	 * @param auftrag Auftrag, von welchem man die Details sehen möchte
	 * @return Array mit jeweiligen Details
	 */
	private Object[][] befuellenMonteurTabelle(Auftrag auftrag) {
		// fügt zugewiesenen Monteur und Auftraggeber in das Array ein

		Object[][] monteur = new Object[1][5];
		// MonteurArray mit nur einer Zeile

		monteur[0][0] = "";
		if (auftrag.getAuftragsNummer() != null)
			monteur[0][0] = auftrag.getAuftragsNummer();

		monteur[0][1] = "";
		if (auftrag.getAuftraggeber().getKundenNummer() != null)
			monteur[0][1] = auftrag.getAuftraggeber().getKundenNummer();

		monteur[0][2] = "";
		if (auftrag.getAuftraggeber().getName() != null)
			monteur[0][2] = auftrag.getAuftraggeber().getName();

		monteur[0][3] = "";
		monteur[0][4] = "nicht zugewiesen";

		if (auftrag.getZustaendig() != null) {
			if (auftrag.getZustaendig().getMitarbeiterNummer() != null
					&& auftrag.getZustaendig().getMitarbeiterNummer() != "0000")
				monteur[0][3] = auftrag.getZustaendig().getMitarbeiterNummer();
			// Wenn ein Monteur zugewiesen wurde: Mitarbeiternummer wir in die vierte Spalte
			// eingetragen

			if (auftrag.getZustaendig().getName() != null && auftrag.getZustaendig().getVorname() != null
					&& auftrag.getZustaendig().getMitarbeiterNummer() != "0000")
				monteur[0][4] = auftrag.getZustaendig().getName() + ", " + auftrag.getZustaendig().getVorname();
			// Wenn ein Monteur zugewiesen wurde: Monteurname wird in die fünfte Spalte
			// eingetragen
		}

		return monteur;
	}
}
