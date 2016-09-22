/**
 * @file MyProfile.java
 */
package client.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import server.ifaces.UserAccount;

/**
 * Το παράθυρο αλλαγής συνθηματικού εισόδου.
 * 
 * @author Team13
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MyProfile extends JFrame implements ActionListener {

	private String errmsg1 = "Λάθος παλιό password.";
	private String errmsg2 = "Το νέο password δεν έχει επιβεβαιωθεί σωστά.";

	private JLabel error = new JLabel();
	private JLabel pass = new JLabel("Δώσε το παλιό password:");
	private JLabel passn = new JLabel("Δώσε το νέο password:");
	private JLabel passr = new JLabel("Δώσε ξανά το νέο password:");

	private JPasswordField oldpass = new JPasswordField(10);
	private JPasswordField newpass1 = new JPasswordField(10);
	private JPasswordField newpass2 = new JPasswordField(10);

	private JButton save = new JButton("Save");
	private JButton cancel = new JButton("Cancel");

	private UserAccount user;

	/**
	 * Ο κατασκευαστής δημιουργεί το παράθυρο αλλαγής συνθηματικού.
	 * Δέχεται μια ενεργή σύνδεση με το αντικείμενο του ενεργού χρήστη
	 * στο <code>server</code>. Μέσω αυτής της σύνδεσης καλεί τη μέθοδο
	 * αλλαγής συνθηματικού με τα κατάλληλα ορίσματα αφού φιλτράρει τα
	 * στοιχεία.
	 * 
	 * @param usr η σύνδεση στο <code>server</code> με το ενεργό αντικείμενο του χρήστη
	 */
	public MyProfile(UserAccount usr) {

		super();

		this.user = usr;
		this.setLayout(new FlowLayout());
		this.add(error);
		error.setForeground(Color.RED);
		error.setVisible(false);

		JPanel left = new JPanel();
		left.setLayout(new GridLayout(3,1));
		left.add(pass);
		left.add(passn);
		left.add(passr);

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(3,1));
		right.add(oldpass);
		right.add(newpass1);
		right.add(newpass2);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(0,2));
		center.add(left);
		center.add(right);

		this.add(center);
		save.addActionListener(this);
		this.add(save);
		cancel.addActionListener(this);
		this.add(cancel);

		this.setSize(400,150);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == save) {

			error.setVisible(false);
			String npass1 = "";
			for (int i = 0;i < newpass1.getPassword().length;i++) { npass1 = npass1 + newpass1.getPassword()[i]; }
			String npass2 = "";
			for (int i = 0;i < newpass2.getPassword().length;i++) { npass2 = npass2 + newpass2.getPassword()[i]; }
			if (!npass1.equals(npass2)) {
				error.setText(errmsg2);
				error.setVisible(true);
			} else {
				try {
					String opass = "" ;
					for (int i = 0;i < oldpass.getPassword().length;i++) { opass = opass + oldpass.getPassword()[i]; }
					if (user.setPassword(opass, npass1)) {
						this.setVisible(false);
						this.dispose();
					} else {
						error.setText(errmsg1);
						error.setVisible(true);
					}
				} catch (RemoteException e1) { e1.printStackTrace(); }
			}

		} else if (e.getSource() == cancel) {
			this.setVisible(false);
			this.dispose();
		}
	}

}
