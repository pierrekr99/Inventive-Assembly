package test;

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
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class MontuerAuftraege extends JFrame {

	private JPanel contentPane;
	private JTable tAuftraege;
	private JTextField txtSuche;

	// dieser teil wird sp�ter ausgelagert
	int zeilen = 500;// Aufragliste.size; Die Anzahl der Zeilen, die die Tabelle hat
	int zeile = 0;// Zeile in der der Neue Auftrag eingef�gt wird
	String auftragsNr = "1234567";// diese infos werden sp�ter aus der Datenbank ausgelesen
	String status = "Offen";
	String frist = "28.05.20";
	String datum = "16.05.20";
	String auftraggeber = "Highspeed GmbH";

	Object[][] auftraege = new Object[zeilen][];// Nur das wird sp�ter eingelesen
	String[] auftrag = new String[6];// Ein AUftrag wird als Zeile erstellt (Zeile mit 6 Spalten
	String details = "Details";// Hier k�nnte man den Detailsbutton Rendern

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MontuerAuftraege frame = new MontuerAuftraege();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);// Fenster Gro�
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
	public MontuerAuftraege() {
		setTitle("Invenive Assembly");// Name des Fensters
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Was passiert beim schlie�en des Fensters
		setBounds(100, 100, 1010, 450);// (MinBreite, MinH�he, StandardBreite, StandardH�he)
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

		auftraege();
		tAuftraege = new JTable();// Neue Tabelle
		tAuftraege.setCellSelectionEnabled(true);// Einzelne Zellen k�nnen ausgew�hlt werden
		tAuftraege.setFont(new Font("Tahoma", Font.PLAIN, 16));// Schriftart und -gr��e in der Tabelle
		tAuftraege.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));// Schriftart und -gr��e in der
																				// Kopfzeile der Tabelle
		tAuftraege.setModel(new DefaultTableModel(auftraege, // Ben�tigter Inhalt: (String[][],String[])
				// Sonst wird hier ein eigenes Modell Eingef�gt
				new String[] { "", "AuftragsNr.", "Status", "Frist", "Datum", "Auftraggeber" }));// Generierung der
																									// Tabelle

		JComboBox comboBox = new JComboBox(); // erstellung einer Combobox
		comboBox.addItem("Im Lager");// Optionen in der Combobox
		comboBox.addItem("Teile fehlen");
		comboBox.addItem("Offen");
		TableColumn statusColumn = tAuftraege.getColumnModel().getColumn(2);// eine bestimmte Spalte f�r Combobox
																			// ausw�hlen
		statusColumn.setCellEditor(new DefaultCellEditor(comboBox));// in die Spalte die Combobox einbinden

		tAuftraege.addMouseListener(new MouseAdapter() {//MouseListener f�r das Fenster
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
		sPAuftraege.setViewportView(tAuftraege);

		JPanel panel = new JPanel();
		sPAuftraege.setColumnHeaderView(panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void auftraege() {// Auftr�ge werden aus Auftragsliste asugelesen und in auftraege[][] eingebaut
		neueZeile();

	}

	private String[] neuerAuftrag() { // F�llt den Auftrag mit den Parametern
		auftrag[0] = details;
		auftrag[1] = auftragsNr;// Auftragsliste.get(zeile).getAuftragsnr()
		auftrag[2] = status;
		auftrag[3] = frist;
		auftrag[4] = datum;
		auftrag[5] = auftraggeber;
		return auftrag;
	}

	private void neueZeile() {
		auftraege[zeile] = neuerAuftrag();//F�gt neuen Auftrag in auftr�ge ein
		zeile++;
	}

	public void deteilsFenster() {//�ffnet Detailsfenster
		try {
			DetailsFenster detailsFenster = new DetailsFenster();//Fenster wird erstellt
			detailsFenster.setVisible(true);//Fenster wird sichtbar
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
