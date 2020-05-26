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

public class MonteurFenster {

	private JFrame fenster;
	private JTextField suchFeld;
	private JTable auftraegeMonteurTBL;
	
	
	
	int zeilen = 0; //zeilen tabelle
	String details = "Details";// Hier k�nnte man den Detailsbutton Rendern
	

	datenbankVerbindung db = new datenbankVerbindung(); //verbindung Datenbank

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonteurFenster window = new MonteurFenster();

					window.fenster.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the application.
	 */
	public MonteurFenster() {

		einleiten();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void einleiten() {
		fenster = new JFrame(); //neues fenster
		fenster.setTitle("Inventive Assembly Monteur Auftragsansicht");//Namen setzen
		fenster.setExtendedState(JFrame.MAXIMIZED_BOTH);//Gro�ansicht
		fenster.setBounds(0, 0, 700, 600);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//fenster schlie�en bei x
		GridBagLayout gridBagLayout = new GridBagLayout();//layout von hier
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 362, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		fenster.getContentPane().setLayout(gridBagLayout);//bis hier

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);//layout f�r tabbed pane von hier
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16)); //schriftgr��e
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		fenster.getContentPane().add(tabbedPane, gbc_tabbedPane);//bis hier

		JPanel auftraegeTab = new JPanel();
		tabbedPane.addTab("Auftr�ge", null, auftraegeTab, null);// tab sichtbar

		suchFeld = new JTextField(); //suchfeld erstellen
		suchFeld.setFont(new Font("Tahoma", Font.PLAIN, 16));//formatierung schrift
		suchFeld.setText("search");//suchfeld name
		suchFeld.setColumns(10);

		JButton logoutKnopf = new JButton("logout");//logout button erstellen
		logoutKnopf.setFont(new Font("Tahoma", Font.PLAIN, 16));//formatierung schrift
		logoutKnopf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {//logout befehl...
				System.out.println("moin");
				
			}
		});

		/**
		 * layout regelungen f�r scroll pane und anordnung der komponenten*************************************************************************
		 * ************************************************************************************************
		 */
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_auftraegeTab = new GroupLayout(auftraegeTab);//layout
		gl_auftraegeTab.setHorizontalGroup(gl_auftraegeTab.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_auftraegeTab.createSequentialGroup().addContainerGap().addGroup(gl_auftraegeTab
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
						.addGroup(gl_auftraegeTab.createSequentialGroup()
								.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 477, Short.MAX_VALUE)
								.addComponent(logoutKnopf)))
						.addContainerGap()));
		gl_auftraegeTab
				.setVerticalGroup(gl_auftraegeTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_auftraegeTab.createSequentialGroup().addContainerGap()
								.addGroup(gl_auftraegeTab.createParallelGroup(Alignment.BASELINE)
										.addComponent(suchFeld, GroupLayout.PREFERRED_SIZE, 29,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(logoutKnopf))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
								.addContainerGap()));
		
		/**
		 * ***********************************************************************************************
		 * ************************************************************************************************
		 */

		auftraegeMonteurTBL = new JTable();//tabelle erstellen
		auftraegeMonteurTBL.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));//formatierung schrift
		auftraegeMonteurTBL.setModel(new DefaultTableModel(//bef�llung
			auftraege(),
				
//				,
			new String[] {
				"Auftragsnummer", "Details", "Status", "Datum", "Frist", "Auftraggeber"//welche spaltennamen
			}
		) {
			boolean[] columnEditables = new boolean[] {//welche spalten lassen sich �ndern
				false, false, true, false, false, false
			};
			public boolean isCellEditable(int row, int column) {//kontrollmethode ob spalten sich �ndern lassen
				return columnEditables[column];
			}
		});

		JComboBox auswahlBoxStatus = new JComboBox();//combo box f�r status
		auswahlBoxStatus.addItem("Im Lager");//auswahlm�glichkeiten
		auswahlBoxStatus.addItem("Teile fehlen");
		auswahlBoxStatus.addItem("disponiert");
		TableColumn statusSpalte = auftraegeMonteurTBL.getColumnModel().getColumn(2);//auskommentiert weil das bei design ansicht weggemacht wird
		statusSpalte.setCellEditor(new DefaultCellEditor(auswahlBoxStatus));
		
		auftraegeMonteurTBL.addMouseListener(new MouseAdapter() {//MouseListener f�r das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {//Wenn die Maus Gedr�ckt wird (Beim Dr�cken die Maus bewegen z�hlt nicht dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();//wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 0) {//wenn in DetailsSpalte
						deteilsFenster();//Detailsfenster wird ge�ffnet
					}
				}
			}
		});
		auftraegeMonteurTBL.setFont(new Font("Tahoma", Font.PLAIN, 16));//formatierung schrift
		scrollPane.setViewportView(auftraegeMonteurTBL);
		auftraegeTab.setLayout(gl_auftraegeTab);

		
		
	}
	/**
	 * Die methode zum F�llen der Tabelle*************************************************************************
	 * ************************************************************************************************
	 */
	private Object[][] auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		db.verbinden();
		db.auftraggeberEinlesen();
		db.disponentEinlesen();
		db.komponenteEinlesen();
		db.monteurEinlesen();
		db.auftragEinlesen();
		zeilen = db.getAuftragsListe().size();//wie viele zeilen hat die Tabelle
		Object[][] auftraege = new Object[zeilen][6];//Struktur Tabelle
		for (int i = 0; i < db.getAuftragsListe().size(); i++) {
			auftraege[i][0] = details;
			auftraege[i][1] = db.getAuftragsListe().get(i).getAuftragsNummer();// Auftragsliste.get(zeile).getAuftragsnr()
			auftraege[i][2] = db.getAuftragsListe().get(i).getStatus();
			auftraege[i][3] = db.getAuftragsListe().get(i).getFrist();
			auftraege[i][4] = db.getAuftragsListe().get(i).getErstellungsdatum();
			auftraege[i][5] = db.getAuftragsListe().get(i).getAuftraggeber().getKundenNummer();
		}
		return auftraege;
	}
	/**
	 * *************************************************************************************************
	 */

	public void deteilsFenster() {//�ffnet Detailsfenster
		try {
			DetailsFenster detailsFenster = new DetailsFenster();//Fenster wird erstellt
			detailsFenster.setVisible(true);//Fenster wird sichtbar
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
