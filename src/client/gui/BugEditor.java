/**
 * @file BugEditor.java
 */
package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import server.ifaces.BugIntrf;
import server.ifaces.ProjIntrf;
import client.deps.Globals;

/**
 * Η οθόνη διαχείρισης για το ορισμένο <code>Bug</code>. Είναι η τέταρτη οθόνη
 * ιεραρχικά από την εκκίνηση του προγράμματος και βρίσκεται μετά την
 * {@link client.gui.ProjectHome} και παράλληλα με την {@link client.gui.History}.
 * <p>
 * Εμφανίζει όλα τα στοιχεία που αφορούν το ανοιγμένο <code>Bug</code> και επιτρέπει
 * την επεξεργασία τους στο χρήστη που τα προσπελαύνει. Ο χρήστης μπορεί είτε να
 * πειράξει τα στοιχεία, είτε απλά να τα δει. Κατά την αποθήκευση η οθόνη αποθηκεύει
 * το <code>bug</code> στο <code>server</code> καλώντας τις κατάλληλες μεθόδους και
 * επαναφέρει την προηγούμενη οθόνη σε πρώτο πλάνο.
 * 
 * @author Team13
 * @version 1.5
 * @see client.gui.History
 */
@SuppressWarnings("serial")
public class BugEditor extends JFrame implements ActionListener {

	private JPanel panel = new JPanel();
	private JPanel panelSouth = new JPanel();
	private JPanel text = new JPanel();
	private JPanel radioPanel = new JPanel();
	private JPanel radioPanel2 = new JPanel();

	private JLabel deadline = new JLabel("Deadline:   ");
	private JTextField txtdead = new JTextField(15);

	private JToolBar toolbar = new JToolBar();
	private JButton send = new JButton();

	private JTextArea editor = new JTextArea(10, 10);

	private ImageIcon deadbug = new ImageIcon(Globals.IMG + "deadbug.jpeg");
	private ImageIcon belos = new ImageIcon(Globals.IMG + "send.png");

	//radiobuttons
	private ButtonGroup bgroup = new ButtonGroup();
	private JRadioButton low = new JRadioButton("Low", false);
	private JRadioButton medium = new JRadioButton("Medium", false);
	private JRadioButton high = new JRadioButton("High", false);
	private ButtonGroup statusgroup = new ButtonGroup();
	private JRadioButton open = new JRadioButton("Open", true);
	private JRadioButton close = new JRadioButton("Close", false);
	private JRadioButton reopen = new JRadioButton("Reopen", false);
	private JRadioButton abandon = new JRadioButton("Abandon", false);

	private ProjectHome parentscreen;
	private BugIntrf b;

	private boolean isAbandoned = false;

	/**
	 * Ο κατασκευαστής ενεργοποιεί την οθόνη και τη φέρνει σε πρώτο πλάνο.
	 * Δεν κλείνει την προηγούμενη οθόνη καθώς μπορούν να υπάρχουν πολλά
	 * στιγμιότυπα αυτής της κλάσης ενεργά, ταυτόχρονα. Δέχεται σαν όρισμα
	 * τη γονική οθόνη που την ενεργοποιεί, για να την ενημερώσει για τυχόν
	 * αλλαγές που αφορούν τα <code>bug</code>. Επίσης, δέχεται και το απομακρυσμένο
	 * αντικείμενο <code>bug</code> από το οποίο με κλήσεις στις δημόσιες
	 * απομακρυσμένες μεθόδους, αντλεί τα δεδομένα από το <code>server</code>
	 * τα οποία και παρουσιάζει. Με τον ίδιο τρόπο ενημερώνει το <code>server</code>
	 * για τις όποιες αλλαγές.
	 * 
	 * @param parent η γονική οθόνη
	 * @param bug το απομακρυσμένο αντικείμενο που παρουσιάζει ή/και μεταβάλλει
	 */
	public BugEditor(ProjectHome parent, BugIntrf bug) {

		super();
		this.parentscreen = parent;
		this.b = bug;
		try {
			if (b.getStatus().equals("abandoned")) isAbandoned=true;
			if (isAbandoned) super.setTitle("Bug Editor, Bug: " + b.getName() + " (abandoned)");
			else super.setTitle("Bug Editor, Bug: " + b.getName());
		} catch (RemoteException e) { e.printStackTrace(); }

		//ta radio buttons
		bgroup.add(low);
		bgroup.add(medium);
		bgroup.add(high);
		if (isAbandoned) {
			bgroup.clearSelection();
			low.setEnabled(false);
			medium.setEnabled(false);
			high.setEnabled(false);
		} else {
			try {
				if (b.getPriority().equals("low")) {
					bgroup.clearSelection();
					low.setSelected(true);
				} else if (b.getPriority().equals("medium")) {
					bgroup.clearSelection();
					medium.setSelected(true);
				} else if (b.getPriority().equals("high")) {
					bgroup.clearSelection();
					high.setSelected(true);
				} else { bgroup.clearSelection(); low.setSelected(true); }
			} catch (RemoteException e) { e.printStackTrace(); }
		}

		statusgroup.add(abandon);
		statusgroup.add(close);
		statusgroup.add(reopen);
		statusgroup.add(open);
		if (isAbandoned) {
			statusgroup.clearSelection();
			abandon.setSelected(true);
			abandon.setEnabled(false);
			close.setEnabled(false);
			reopen.setEnabled(false);
			open.setEnabled(false);
		} else {
			try {
				if (b.getStatus().equals("closed")) {
					statusgroup.clearSelection();
					close.setSelected(true);
				} else if (b.getStatus().equals("reopened")) {
					statusgroup.clearSelection();
					reopen.setSelected(true);
				} else if (b.getStatus().equals("opened")) {
					statusgroup.clearSelection();
					open.setSelected(true);
				} else { statusgroup.clearSelection(); open.setSelected(true); }
			} catch (RemoteException e) { e.printStackTrace(); }
		}

		panel.setLayout(new BorderLayout());
		panel.add(toolbar, BorderLayout.NORTH);
			toolbar.add(radioPanel);
				radioPanel.setLayout(new GridLayout(3, 1));
				radioPanel.add(low);
				radioPanel.add(medium);
				radioPanel.add(high);
				radioPanel.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(), "Set Priority"));
			toolbar.addSeparator();
			toolbar.add(radioPanel2);
				radioPanel2.setLayout(new GridLayout(3, 1));
				radioPanel2.add(open);
				radioPanel2.add(close);
				radioPanel2.add(reopen);
				radioPanel2.add(abandon);
				radioPanel2.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(), "Set Status"));
			toolbar.addSeparator();
			toolbar.add(text);
				text.setLayout(new GridLayout(3,1));
				text.add(deadline);
				text.add(txtdead);
					txtdead.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.DARK_GRAY));
					if (isAbandoned) txtdead.setEnabled(false);
			toolbar.addSeparator();
		panel.add(editor, BorderLayout.CENTER);
			editor.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.DARK_GRAY));
			try { editor.setText(b.getDescription()); }
			catch (RemoteException e) { e.printStackTrace(); }
			if (isAbandoned) editor.setEnabled(false);
		panel.add(panelSouth, BorderLayout.SOUTH);
			panelSouth.setLayout(new BorderLayout());
			panelSouth.setBackground(Color.lightGray);
			panelSouth.add(send, BorderLayout.EAST);
				send.setBackground(Color.lightGray);
				send.setIcon(belos);
				send.setToolTipText("Send");
				send.addActionListener(this);

		this.setSize(712,604);
		this.setContentPane(panel);
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == send) {

			if (this.isAbandoned) {
				JOptionPane.showMessageDialog(null,"The bug is abandoned","Warning", JOptionPane.INFORMATION_MESSAGE, deadbug);
				this.dispose(); // einai abandoned kleisto
			} else {
				try {
					if (low.isSelected()) { this.b.setPriority("low"); }
					else if (medium.isSelected()) { this.b.setPriority("medium"); }
					else if (high.isSelected()) { this.b.setPriority("high"); }

					if (open.isSelected()) { this.b.setStatus("opened"); }
					else if (reopen.isSelected()) { this.b.setStatus("reopened"); }
					else if (close.isSelected()) { this.b.setStatus("closed"); }
					else if (abandon.isSelected()) { this.b.setStatus("abandoned"); }

					this.b.setDescription(editor.getText());
					this.b.setDeadLine(txtdead.getText());
					this.b.save();
					String url = "//" + Globals.SERVER + ":" + Globals.PORT + "/projects/" + this.b.getProject();
					ProjIntrf prj = (ProjIntrf) Naming.lookup(url);
					prj.addBug(this.b.getName());
				} catch (RemoteException e1) { e1.printStackTrace();
				} catch (MalformedURLException e2) { e2.printStackTrace();
				} catch (NotBoundException e3) { e3.printStackTrace(); }
				this.parentscreen.updateTreeBug();
				this.dispose();
			}

		}
	}

}
