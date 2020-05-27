package test; //kommentar test

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.CellRendererPane;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Datenbank.datenbankVerbindung;

import java.awt.Font;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class MonteurAuftraege extends JFrame {

	private JPanel contentPane;
	private JTable tAuftraege;
	private JTextField txtSuche;

	int zeilen;
	String details = "Details";// Hier könnte man den Detailsbutton Rendern
	
	datenbankVerbindung db = new datenbankVerbindung();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonteurAuftraege frame = new MonteurAuftraege();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster Groß
					// frame.setUndecorated(true);//Vollbild
					frame.setVisible(true);// Fenster erscheint
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MonteurAuftraege() {
		setTitle("Invenive Assembly");// Name des Fensters
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Was passiert beim schließen des Fensters
		setBounds(100, 100, 1010, 450);// (MinBreite, MinHöhe, StandardBreite, StandardHöhe)
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane sPAuftraege = new JScrollPane();

		txtSuche = new JTextField();// Hier kann man die Suhe umsetzen
		txtSuche.setText("Suche");
		txtSuche.setColumns(10);

		JButton btnLogout = new JButton("Logout");// LogoutButton
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 8));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(797)
					.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
				.addComponent(sPAuftraege, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtSuche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sPAuftraege, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
		);

		tAuftraege = new JTable();// Neue Tabelle
		tAuftraege.setCellSelectionEnabled(true);// Einzelne Zellen können ausgewählt werden
		tAuftraege.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -größe in der Tabelle
		tAuftraege.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -größe in der
																				// Kopfzeile der Tabelle
		tAuftraege.setModel(new DefaultTableModel(auftraege(), // Benötigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingefügt
				new String[] { "", "AuftragsNr.", "Status", "Frist", "Datum", "Auftraggeber" }));// Generierung der
																									// Tabelle

		JComboBox comboBox = new JComboBox(); // erstellung einer Combobox
		comboBox.addItem("Im Lager");// Optionen in der Combobox
		comboBox.addItem("Teile fehlen");
		comboBox.addItem("Offen");
		TableColumn statusColumn = tAuftraege.getColumnModel().getColumn(2);// eine bestimmte Spalte für Combobox
																			// auswählen
		statusColumn.setCellEditor(new DefaultCellEditor(comboBox));// in die Spalte die Combobox einbinden

		tAuftraege.addMouseListener(new MouseAdapter() {//MouseListener für das Fenster
			public void mouseClicked(MouseEvent e) {
				if (e.MOUSE_PRESSED == 501) {//Wenn die Maus Gedrückt wird (Beim Drücken die Maus bewegen zählt nicht dazu)
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();//wo wurde geklickt
					int column = target.getSelectedColumn();
					// do some action if appropriate column
					if (column == 0) {//wenn in DetailsSpalte
						deteilsFenster();//Detailsfenster wird geöffnet
					}
				}
			}
		});
		sPAuftraege.setViewportView(tAuftraege);

		JPanel panel = new JPanel();
		sPAuftraege.setColumnHeaderView(panel);
		contentPane.setLayout(gl_contentPane);
	}
	/**
	 * Die methode zum Füllen der Tabelle*************************************************************************
	 * ************************************************************************************************
	 */
	private Object[][] auftraege() {// Aufträge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		db.verbinden();
		db.auftraggeberEinlesen();
		db.disponentEinlesen();
		db.komponenteEinlesen();
		db.monteurEinlesen();
		db.auftragEinlesen();
		zeilen = db.getAuftragsListe().size();
		Object[][] auftraege = new Object[zeilen][6];// Nur das wird später eingelesen
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

	public void deteilsFenster() {//Öffnet Detailsfenster
		try {
			DetailsFenster detailsFenster = new test.DetailsFenster();//Fenster wird erstellt
			detailsFenster.setVisible(true);//Fenster wird sichtbar
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
