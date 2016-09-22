/**
 * @file Login.java
 */
package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import server.ifaces.UserAccount;
import server.ifaces.UserLogin;

/**
 * Η διαχειριστική οθόνη εισόδου στο <code>server</code>. Είναι η πρώτη οθόνη την οποία
 * αντικρύζουν όλοι οι χρήστες κατά την εκκίνηση της εφαρμογής διαχείρισης του <code>server</code>.
 * 
 * @author Team13
 * @version 1.5
 */
@SuppressWarnings("serial")
public class Login extends JFrame implements ActionListener {

	private JPanel panel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel leftCenter = new JPanel();
	private JPanel leftCenterCenter = new JPanel();
	private JPanel leftCenterSouth = new JPanel();
	private JPanel leftSouth = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel centerSouthPanel = new JPanel();

	private JButton login = new JButton("LOGIN");
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel PasswordLabel = new JLabel("Password:");
	private String errmsg1 = "Λάθος username ή password.";
	private JLabel error = new JLabel(errmsg1);

	private JTextField usernameField = new JTextField(10);
	private JPasswordField PasswordField = new JPasswordField(10);

	/**
	 * Η ενεργή σύνδεση με το <code>server</code>.
	 */
	private UserLogin usrlogin;

	/**
	 * Ο κατασκευαστής ενεργοποιεί την οθόνη. Απαιτείται η ύπαρξη μιας
	 * ενεργής σύνδεσης με το <code>server</code> μέσω της οποίας θα γίνει η
	 * ανταλλαγή των δεδομένων αυθεντικοποίησης του χρήστη. Εξυπακούεται,
	 * ότι αυτή η σύνδεση θα γίνει μέσω του <code>localhost</code> καθώς
	 * μόνο τοπικά μπορεί ο διαχειριστής να ρυθμίσει το <code>server</code>.
	 * 
	 * @param usr η ενεργή σύνδεση με το <code>server</code>
	 */
	public Login(UserLogin usr) {

		super("Welcome to DEDA Administration");
		this.usrlogin = usr;
		Font f1=new Font("Times New Roman",Font.BOLD ,17);
		usernameLabel.setFont(f1);
		usernameLabel.setForeground(Color.white);
		usernameLabel.setLabelFor(usernameField);
		PasswordLabel.setFont(f1);
		PasswordLabel.setForeground(Color.white);
		PasswordLabel.setLabelFor(PasswordField);
		PasswordField.addActionListener(this);
		login.setBackground(Color.black);
		login.setForeground(Color.white);
		login.addActionListener(this);
		error.setForeground(Color.RED);
		error.setVisible(false);

		//upolpoiisi panel. Kanw ena basiko panel sto opoio prostithentai ena aristero kai ena kentriko
		panel.setLayout(new BorderLayout());

		leftCenterCenter.setLayout(new GridLayout(2,2,0,10));
		leftCenterCenter.setBackground(Color.black);
			leftCenterCenter.add(usernameLabel);
			leftCenterCenter.add(usernameField);
			leftCenterCenter.add(PasswordLabel);
			leftCenterCenter.add(PasswordField);

		leftCenterSouth.setBackground(Color.black);
			leftCenterSouth.add(login);

		leftCenter.setLayout(new BorderLayout());
			leftCenter.add(leftCenterCenter, BorderLayout.NORTH);
			leftCenter.add(leftCenterSouth, BorderLayout.CENTER);


		leftSouth.setLayout(new BorderLayout());
		leftSouth.setBackground(Color.black);

		//aristero panel
		leftPanel.setLayout(new GridLayout(3,1));
		leftPanel.setBackground(Color.black);
			leftPanel.add(error);
			leftPanel.add(leftCenter);
			leftPanel.add(leftSouth);

		centerSouthPanel.setLayout(new GridLayout(14,1,0,5));
		centerSouthPanel.setBackground(Color.black);
			centerSouthPanel.add(new JLabel("          -----------------------------"));
			centerSouthPanel.add(new JLabel("          About:"));
			centerSouthPanel.add(new JLabel("            Students of UOM"));
			centerSouthPanel.add(new JLabel("            Project Name: DEDA"));
			centerSouthPanel.add(new JLabel("            Lesson: Distributed Systems"));
			centerSouthPanel.add(new JLabel("          -----------------------------"));
			centerSouthPanel.add(new JLabel("          Contact info:"));
			centerSouthPanel.add(new JLabel("              it067@uom.gr"));
			centerSouthPanel.add(new JLabel("            it06112@uom.gr"));
			centerSouthPanel.add(new JLabel("            it06186@uom.gr"));

		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(Color.black);
			centerPanel.add(new JLabel("DEDA"), BorderLayout.NORTH);
			centerPanel.add(centerSouthPanel, BorderLayout.CENTER);


		panel.add(leftPanel, BorderLayout.WEST);
		panel.add(centerPanel, BorderLayout.CENTER);

		this.setContentPane(panel);
		this.setSize(550,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == login) {

			String pass = "" ;
			for (int i = 0;i < PasswordField.getPassword().length;i++) { pass = pass + PasswordField.getPassword()[i]; }
			try {
				error.setVisible(false);
				error.setText(errmsg1);
				if (!usernameField.getText().equals("root")) {
					error.setVisible(true);
				} else {
					UserAccount usr = (UserAccount) usrlogin.getAuthority(usernameField.getText(), pass);
					if (usr==null) { error.setVisible(true); }
					else {
						new HomeAdmin(usr);
						this.setVisible(false);
						this.dispose();
					}
				}
			} catch (RemoteException e1) { e1.printStackTrace(); }
		}

	}

}
