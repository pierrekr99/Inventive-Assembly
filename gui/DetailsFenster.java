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

import Datenbank.datenbankVerbindung;
import objekte.Auftrag;

public class DetailsFenster extends JFrame {

	static datenbankVerbindung db = main.Main.getdb();
	private JPanel contentPane;
	private JTable tKomponenten;
	private JTable tMonteur;
	private JScrollPane sPKomponenten;
	private JScrollPane sPMonteur;

	/**
	 * Create the frame.
	 */
	public DetailsFenster(Auftrag auftrag) { // reihe des auftrags als parameter

		setTitle("Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Nur dieses Fenster wird geschlossen
		
		setBounds(100, 100, 1060, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
//		JPanel panel = new JPanel();
		setIconImage(LoginFenster.getImage());

//		-------  Komponenten Tabelle  ----------------------------------------

		komponenten(auftrag);
		sPKomponenten = new JScrollPane();
		sPKomponenten.setBounds(5, 36, 922, 449);
		tKomponenten = new JTable();
		tKomponenten.setCellSelectionEnabled(true);
		
		tKomponenten.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben
		
		// DefaultTableModel(Tabelle,Kopfzeile)
		tKomponenten.setModel(new DefaultTableModel(komponenten(auftrag),
				new String[] { "TeileNummer", "Name", "Attribut", "Kategorie", "Verfügbarkeit" }) {
			
			boolean[] columnEditables = new boolean[] {false, false, false, false, false};
			// welche Spalten lassen sich ändern

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];}
			// Kontrollmethode ob spalten sich ändern lassen
			
		});

		sPKomponenten.setViewportView(tKomponenten);
//		sPKomponenten.setColumnHeaderView(panel);

//      -------  Monteur Tabelle  ----------------------------------------

		auftragMonteur(auftrag);
		sPMonteur = new JScrollPane();
		tMonteur = new JTable();
		
		// DefaultTableModel(Tabelle,Kopfzeile)
		tMonteur.setModel(new DefaultTableModel(auftragMonteur(auftrag),
				new String[] { "AuftragsNummer", "KundenNummer", "Auftraggeber", "MonteurNummer", "MonteurName" }) {

			boolean[] columnEditables = new boolean[] {false, false, false, false, false};
			// welche spalten lassen sich ändern

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];}
			// kontrollmethode ob spalten sich ändern lassen
			
		});

		sPMonteur.setViewportView(tMonteur);

//      -------  Eilbestellung  ----------------------------------------		

		
		// MouseListener für KomponentenTabelle um Eilbestellung auszuführen
		tKomponenten.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {
				// Wenn die Maus Gedrückt wird 
					
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// Welches Objekt/Reihe/Spalte wurde ausgewählt
					
					
					if (column == 4 && tKomponenten.getValueAt(row, column).equals("nicht verfügbar")) {
					// Wenn Spalte 4 (Verfügbarkeit) ausgewäglt wurde und diese "nicht verfügbar beinhaltet"	
					
						JOptionPane.showMessageDialog(null, ("Eilbestellung für [" + tKomponenten.getValueAt(row, 1)
								+ " " + tKomponenten.getValueAt(row, 2) + "] wurde ausgeführt"));
						// Nachricht über Eilbestellung inkl. Name der fehlenden Komponente 
					}
				}
			}
		});

		
		// -------  Formatierung  -------------------------------------------------
		tMonteur.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		tMonteur.setRowHeight(50);
		tMonteur.setFont(new Font("Tahoma", Font.PLAIN, 18));
		monteureTblFormat();

		tKomponenten.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 22));
		tKomponenten.setRowHeight(50);
		tKomponenten.setFont(new Font("Tahoma", Font.PLAIN, 18));

		tKomponenten.setAutoCreateRowSorter(true);
		
		/*
		 * durch Anklicken der Kopfzeile (in der jeweiligen Spalte) werden die Komponenten 
		 * nach diesem Attribut in der natürlichen Ordnung und umgekehrt sortiert
		 */
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(sPKomponenten, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
						.addComponent(sPMonteur, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(sPMonteur, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE).addGap(47)
						.addComponent(sPKomponenten, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)));
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
		tMonteur.getColumnModel().getColumn(3).setMinWidth(150);
		tMonteur.getColumnModel().getColumn(3).setMaxWidth(200);

		tMonteur.getColumnModel().getColumn(4).setPreferredWidth(100);
		tMonteur.getColumnModel().getColumn(4).setMinWidth(100);
		tMonteur.getColumnModel().getColumn(4).setMaxWidth(500);
		
		tMonteur.getTableHeader().setReorderingAllowed(false);
		// Spalten lassen sich nicht verschieben
	}

	private Object[][] komponenten(Auftrag auftrag) {
		// Komponenten werden aus Komponentensliste ausgelesen und in ein Array eingebaut
		// auftrag dient dazu die Komponenten des richtigen Auftrags zu erhalten
	
		int zeilen = auftrag.getKomponenten().size();
		Object[][] komponenten = new Object[zeilen][5];

		for (int i = 0; i < auftrag.getKomponenten().size(); i++) { 
		// 	fügt Komponenten eines Auftrags in das Array ein																		 

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
			// Wenn eine Komponente im Auftrag eingetragen ist und die Verfügbarkeit "true" ist
				
				komponenten[i][4] = "verfügbar";
				// Fünfte Spalte: "verfügbar"
				
			}else{
				komponenten[i][4] = "nicht verfügbar";
				// Ansonsten "Nicht verfügbar"
			}
		}
		
		return komponenten;
	}

	private Object[][] auftragMonteur(Auftrag auftrag) { 
		// fügt zugewiesenen Monteur und Auftraggeber in das Array ein

		Object[][] monteur = new Object[1][5];
		// MonteurArray mit nur einer Zeile

		monteur[0][0] = "";
		if (auftrag.getAuftragsNummer() != null)
			monteur[0][0] = auftrag.getAuftragsNummer();
		// Erste Spalte: Auftragsnummer

		monteur[0][1] = "";
		if (auftrag.getAuftraggeber().getKundenNummer() != null)
			monteur[0][1] = auftrag.getAuftraggeber().getKundenNummer();
		// Zweite Spalte: Kundennummer

		monteur[0][2] = "";
		if (auftrag.getAuftraggeber().getName() != null)
			monteur[0][2] = auftrag.getAuftraggeber().getName();
		// Dritte Spalte: Auftraggeber

		monteur[0][3] = "nicht Zugewiesen";
		monteur[0][4] = "nicht Zugewiesen";
		// Vierte und Fünfte Spalte auf "nicht Zugewiesen" setzten

		if (auftrag.getZustaendig() != null) {
			if (auftrag.getZustaendig().getMitarbeiterNummer() != null
					&& auftrag.getZustaendig().getMitarbeiterNummer() != "0000")
				monteur[0][3] = auftrag.getZustaendig().getMitarbeiterNummer();
			// Wenn ein Monteur zugewiesen wurde: Mitarbeiternummer wir in die vierte Spalte eingetragen

			if (auftrag.getZustaendig().getName() != null && auftrag.getZustaendig().getVorname() != null
					&& auftrag.getZustaendig().getMitarbeiterNummer() != "0000")
				monteur[0][4] = auftrag.getZustaendig().getName() + ", " + auftrag.getZustaendig().getVorname();
			// Wenn ein Monteur zugewiesen wurde: Monteurname wird in die fünfte Spalte eingetragen
		}

		return monteur;
	}
}
