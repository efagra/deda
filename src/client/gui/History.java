/**
 * @file History.java
 */
package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import server.ifaces.BaseRep;

import client.deps.Globals;

/**
 * Η οθόνη που εμφανίζει το ιστορικό των εκδόσεων για κάποια αποθήκη.
 * Είναι η τέταρτη οθόνη ιεραρχικά από την εκκίνηση του προγράμματος και
 * βρίσκεται μετά την {@link client.gui.ProjectHome} και παράλληλα με την
 * {@link client.gui.BugEditor}.
 * <p>
 * Εμφανίζει ιστορικό από τις {@link server.sys.BaseRepository} (μέσω της
 * {@link server.ifaces.BaseRep}) και {@link client.sys.LocalRepository}
 * (σε τοπικό επίπεδο αλλά πάλι μέσω της διεπαφής). Επίσης, επιτρέπει στο
 * χρήστη τη λειτουργία της επαναφοράς παλαιότερης έκδοσης στο προσκήνιο.
 * 
 * @author Team13
 * @version 1.5
 * @see client.gui.BugEditor
 */
@SuppressWarnings("serial")
public class History extends JFrame implements ActionListener {

	private JPanel panel = new JPanel();
	private JPanel panelSouth = new JPanel();

	private JButton revert = new JButton();
	private JButton back = new JButton();

	private JTextArea commit = new JTextArea(50, 50);
	private JScrollPane jsp = new JScrollPane(commit);

	private ImageIcon revertIcon = new ImageIcon(Globals.IMG + "revert1.png");
	private ImageIcon backIcon = new ImageIcon(Globals.IMG + "back.png");

	private ProjectHome parentScreen;
	private BaseRep myrep;

	/**
	 * Ο κατασκευαστής ενεργοποιεί την οθόνη και τη φέρνει σε πρώτο πλάνο.
	 * Δεν κλείνει την προηγούμενη οθόνη. Δέχεται σαν όρισμα τη γονική οθόνη
	 * που την ενεργοποιεί, για να την ενημερώσει για τυχόν αλλαγές που αφορούν
	 * την τοπική αποθήκη λογισμικού σε περίπτωση που εκτελεστεί κάποια ενέργεια
	 * <code>revert</code>. Επίσης, δέχεται και το απομακρυσμένο/τοπικό
	 * αντικείμενο/διασύνδεση <code>BaseRep</code> μέσω του οποίου επιτυγχάνεται
	 * πρόσβαση στις κοινές μεθόδους των δύο διαφορετικών αντικειμένων.
	 * 
	 * @param parent η γονική οθόνη από την οποία ενεργοποιήθηκε αυτή η οθόνη
	 * @param repo η αποθήκη λογισμικού
	 */
	public History(ProjectHome parent, BaseRep repo) {

		super();
		this.parentScreen = parent;
		this.myrep = repo;
		String windowTitle = "";
		String pn = "";
		String un = "";
		try { pn = myrep.getProject(); un = myrep.getUser(); }
		catch (RemoteException e1) { e1.printStackTrace(); }

		if (un.equals("BASE")) windowTitle = "Base History log, Project: " + pn;
		else windowTitle = "Local History log, Project: " +	pn + ", User: " + un;

		super.setTitle(windowTitle);

		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(jsp, BorderLayout.CENTER);
			jsp.setPreferredSize(new Dimension(50,50));
				try { commit.setText(repo.showHistory()); }
				catch (RemoteException e) { e.printStackTrace(); }
				commit.setEditable(false);
		panel.add(panelSouth, BorderLayout.SOUTH);
			panelSouth.setLayout(new BorderLayout());
			panelSouth.add(back, BorderLayout.WEST);
				back.setIcon(backIcon);
				back.setToolTipText("Back");
				back.setBackground(Color.lightGray);
				back.addActionListener(this);
			panelSouth.add(revert, BorderLayout.EAST);
				revert.setIcon(revertIcon);
				revert.setToolTipText("Revert");
				revert.setBackground(Color.lightGray);
				revert.addActionListener(this);

		this.setSize(400,600);
		this.setContentPane(panel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == revert) {

			String n = JOptionPane.showInputDialog(null, "Revert to revision: ","OK", 3);
			if (n!=null)
				if (n.equals("")) JOptionPane.showMessageDialog(null, "You must enter a changeset number!");
				else {
					String historypattern = "changeset:   " + n + ":";
					if (commit.getText().contains(historypattern)) {
						try { myrep.revert(Integer.parseInt(n)); }
						catch (NumberFormatException e1) { e1.printStackTrace(); }
						catch (RemoteException e1) { e1.printStackTrace(); }
						this.parentScreen.updateTreeFile();
						this.dispose();
					} else JOptionPane.showMessageDialog(null, "You must enter a valid changeset number!");
				}

		} else if(e.getSource() == back) { this.dispose(); }

	}

}