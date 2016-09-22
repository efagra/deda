/**
 * @file Home.java
 */
package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import client.DedaClient;
import client.deps.Globals;

import server.ifaces.ProjIntrf;
import server.ifaces.UserAccount;
import server.ifaces.UserLogin;

/**
 * Η οθόνη που βλέπει ο χρήστης αφού περάσει την οθόνη εισαγωγής {@link client.gui.Login}.
 * Εμφανίζει μια λίστα με τα <code>project</code>, στα οποία ο χρήστης συμμετέχει.
 * Επιλέγοντας, κάποιο από αυτά με διπλό κλικ από το ποντίκι, ο χρήστης
 * μπορεί να περάσει στην επόμενη οθόνη. Επίσης, από το <code>menu</code> αυτής της
 * οθόνης μπορεί να αλλάξει το προσωπικό συνθηματικό του, που χρησιμεύει
 * για τη διαδικασία αναγνώρισής του από το σύστημα.
 * 
 * @author Team13
 * @version 1.5
 */
@SuppressWarnings("serial")
public class Home extends JFrame implements ActionListener {

	/**
	 * Αναφέρεται στο ίδιο το αντικείμενο της κλάσης, όταν αυτό ενεργοποιείται.
	 * Χρησιμεύει για το πέρασμα αυτής της οθόνης, σαν όρισμα σε κατασκευαστές
	 * άλλων οθονών για την υποστήριξη της λειτουργίας επιστροφής στην προηγούμενη
	 * οθόνη, δλδ αυτή. Επίσης, χρησιμεύει για κλήση μεθόδων των γονικών της κλάσεων
	 * από τις υπερφορτωμένες συναρτήσεις του <code>ActionListener</code>, όπου η
	 * χρήση του αναγνωριστικού <code>this</code> δεν είναι δυνατή.
	 */
	private Object myid;

	private JPanel panel = new JPanel();
	private JPanel panelA = new JPanel();
	private JPanel panelB = new JPanel();

	private JMenuBar menu = new JMenuBar();
	private JMenu profile = new JMenu("Profile");
	private JMenu close = new JMenu("Close");
	private JMenuItem myProfile = new JMenuItem("Change Password");
	private JMenuItem logout = new JMenuItem("Logout");
	private JMenuItem exitnow = new JMenuItem("Exit");

	private JLabel keno1 = new JLabel("");
	private JLabel keno2 = new JLabel("");
	private JLabel keno3 = new JLabel("");
	private JLabel keno4 = new JLabel("");

	/**
	 * Το δέντρο που εμφανίζει τη λίστα με τα project
	 */
	private JTree jTree1 ; // Tree project
	private DefaultMutableTreeNode topproj;

	private static String projclicked = "";

	private UserAccount usrlogon;
	private ArrayList<String> pnames;

	/**
	 * Ο κατασκευαστής δημιουργεί την οθόνη μετά το <code>login</code> στο σύστημα.
	 * Η παράμετρος που δέχεται είναι το αντικείμενο του χρήστη που βρίσκεται στο
	 * <code>server</code> και ενεργοποιείται μόνο μετά από την επιτυχή αυθεντικοποίηση
	 * του χρήστη. Συνεπώς, η οθόνη δεν μπορεί να χρησιμοποιηθεί αν δεν γίνει κάποιο
	 * έγκυρο <code>login</code>.
	 * 
	 * @param usr το αντικείμενο που δημιουργείται στο <code>server</code> για το χρήστη
	 * @throws HeadlessException
	 * @throws RemoteException
	 */
	public Home(UserAccount usr) throws HeadlessException, RemoteException {

		super("Home Screen, User : " + usr.getName());
		this.usrlogon = usr;
		menu.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.DARK_GRAY));
		menu.add(profile);
		profile.add(myProfile);
		menu.add(close);
		close.add(logout);
		close.addSeparator();
		close.add(exitnow);
		myProfile.addActionListener(this);
		exitnow.addActionListener(this);
		logout.addActionListener(this);
		myid=this;

		/* Tree Project */
		topproj = new DefaultMutableTreeNode("Project List");

		// ektiponei sto tree1 ola ta projects
		try { pnames = this.usrlogon.getPrjnames(); }
		catch (RemoteException e) { e.printStackTrace(); }
		if (pnames!=null) {
			for (int i=0; i<pnames.size(); i++) {
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(pnames.get(i));
				topproj.add(a);
			}
		}
		jTree1 = new JTree(topproj);
		JScrollPane jsp = new JScrollPane(jTree1);
		jsp.setPreferredSize(new Dimension(500,300));
		jTree1.setPreferredSize(new Dimension(500,100));
		jsp.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.DARK_GRAY));

		panelA.setLayout(new GridLayout(1, 2, 10, 0));
		panelA.add(jsp, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1,3));
		panelB.setLayout(new GridLayout(1, 5, 10, 0));
		panelB.add(keno1);
		panelB.add(keno2);
		panelB.add(keno3);
		panelB.add(keno4);
		panel.add(panelA, BorderLayout.WEST);
		panel.add(keno3);
		panel.add(keno4);

		this.setJMenuBar(menu);
		panel.setBackground(Color.LIGHT_GRAY);
		this.setContentPane(panel);
		this.setSize(800,600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		jTree1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = jTree1.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					int numberofclicks = me.getClickCount();
					if (m != null && numberofclicks == 2) {
						projclicked = m.toString();
						((JFrame) myid).setVisible(false);
						((Window) myid).dispose();
						try {
							String url = "//" + Globals.SERVER + ":" + Globals.PORT	+ "/projects/" + projclicked;
							ProjIntrf prj = (ProjIntrf) Naming.lookup(url);
							new ProjectHome(usrlogon, prj);
							}
						catch (RemoteException e) { e.printStackTrace(); }
						catch (MalformedURLException e1) { e1.printStackTrace(); }
						catch (NotBoundException e1) { e1.printStackTrace(); }
					}
				}
			}});

	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == myProfile) {

			new MyProfile(usrlogon);

		} else if(e.getSource() == logout) {

			String serverUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.LOGINSERVICE;
			try {
				UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
				usr.deleteUser(usrlogon.getName(), usrlogon.getPassword());
				this.dispose();
				new DedaClient();
			} catch (MalformedURLException e1) { e1.printStackTrace();
			} catch (RemoteException e1) { e1.printStackTrace();
			} catch (NotBoundException e1) { e1.printStackTrace(); }

		} else if(e.getSource() == exitnow) {

			String serverUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.LOGINSERVICE;
			try {
				UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
				usr.deleteUser(this.usrlogon.getName(), this.usrlogon.getPassword());
			} catch (MalformedURLException e1) { e1.printStackTrace();
			} catch (RemoteException e1) { e1.printStackTrace();
			} catch (NotBoundException e1) { e1.printStackTrace(); }
			System.exit(0);

		}
	}
}
