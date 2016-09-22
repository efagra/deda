/**
 * @file HomeAdmin.java
 */
package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import dbms.ifaces.Dbms;
import dbms.ifaces.UserDB;

import server.deps.Globals;
import server.ifaces.PrjAdmin;
import server.ifaces.ProjIntrf;
import server.ifaces.UserAccount;
import server.ifaces.UserLogin;
import server.sys.User;

/**
 * Η διαχειριστική οθόνη του <code>server</code>. Η οθόνη αυτή εμφανίζεται μετά την
 * {@link server.gui.Login} μόνο για το χρήστη <code>root</code> και μόνο ύστερα από
 * την επιτυχή αυθεντικοποίησή του από το σύστημα. Μέσω αυτής της οθόνης, ο διαχειριστής
 * μπορεί να δημιουργήσει και να διαγράψει χρήστες και <code>project</code>, να αναθέσει
 * χρήστες σε <code>project</code> και να ορίσει <code>manager</code> για κάποιο
 * <code>project</code>. Τέλος μπορεί να ορίσει νέο συνθηματικό πρόσβασης για τον
 * εαυτό του. Για την διαδικασία ορισμού νέου συνθηματικού, αυτή η οθόνη καλεί την
 * {@link server.gui.MyProfile}.
 * 
 * @author Team13
 * @version 1.5
 */
@SuppressWarnings("serial")
public class HomeAdmin extends JFrame implements ActionListener {

	private JPanel panel = new JPanel();
	private JPanel panelA = new JPanel();
//	private JPanel panelB = new JPanel();

	private JMenuBar menu = new JMenuBar();
	private JMenu profile = new JMenu("Profile");
	private JMenu project = new JMenu("Project");
	private JMenu user = new JMenu("User");
	private JMenu close = new JMenu("Close");
	private JMenuItem myProfile = new JMenuItem("Change Password");
	private JMenuItem newprojmenu = new JMenuItem("New Project");
	private JMenuItem delprojmenu = new JMenuItem("Delete Project");
	private JMenuItem setman2proj = new JMenuItem("Set Manager");
	private JMenuItem adduser2proj = new JMenuItem("Recruit");
	private JMenuItem deluser2proj = new JMenuItem("Fire");
	private JMenuItem newusermenu = new JMenuItem("New User");
	private JMenuItem delusermenu = new JMenuItem("Delete User");
	private JMenuItem exitnow = new JMenuItem("Exit");

	/**
	 * Το δέντρο για τα <code>project</code>
	 */
	private JTree jTree1; // Project tree
	/**
	 * Το δέντρο για τους χρήστες του επιλεγμένου <code>project</code>
	 */
	private JTree jTree2; // User/Project tree
	/**
	 * Το δέντρο για το σύνολο των χρηστών του συστήματος
	 */
	private JTree jTree3; // User tree
	private DefaultMutableTreeNode topproj;
	private DefaultMutableTreeNode topusrppr;
	private DefaultMutableTreeNode topusr;
	private JScrollPane jsp1;
	private JScrollPane jsp2;
	private JScrollPane jsp3;
	private static String projclicked = "", userclicked = "", userprojclicked = ""; // metaferei tin pliroforia me mono click apo ta jtrees

	/**
	 * Το τοπικό αντικείμενο του διαχειριστή που προσπελαύνεται
	 * με τεχνικές απομακρυσμένης πρόσβασης.
	 */
	private UserAccount admin;
	/**
	 * Το απομακρυσμένο αντικείμενο της υπηρεσίας της βάσης δεδομένων.
	 */
	private UserDB usrdb;
	/**
	 * Το τοπικό αντικείμενο της διαχείρισης <code>project</code> που προσπελαύνεται
	 * με τεχνικές απομακρυσμένης πρόσβασης.
	 */
	private PrjAdmin prjmng;

	/**
	 * Ο κατασκευαστής δημιουργεί την οθόνη μετά το <code>login</code> στο σύστημα.
	 * Η παράμετρος που δέχεται είναι το αντικείμενο του διαχειριστή που βρίσκεται στο
	 * <code>server</code> και ενεργοποιείται μόνο μετά από την επιτυχή αυθεντικοποίησή
	 * του. Συνεπώς, η οθόνη δεν μπορεί να χρησιμοποιηθεί αν δεν γίνει κάποιο έγκυρο
	 * <code>login</code>. Σημειώνεται, ότι ο <code>server</code> είναι στο ίδιο
	 * μηχάνημα και το αντικείμενο είναι τοπικό. Ωστόσο, η προσπέλασή του υλοποιείται
	 * μέσω <code>rmi</code>.
	 * 
	 * @param usr το αντικείμενο που δημιουργείται στο <code>server</code> για το διαχειριστή
	 */
	public HomeAdmin(UserAccount usr) {

		super("Admin's Home");
		this.admin = usr;

		connect2dbms();
		connect2prjsrv();

		initiateMenu();
		initiateTreeProject();
		initiateTreeUserProject();
		initiateTreeUser();

		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.LIGHT_GRAY);

//		panelB.setLayout(new GridLayout(1, 9, 20, 0));
//		panelB.add(jsp3, BorderLayout.CENTER);

		panelA.setLayout(new GridLayout(1, 3, 10, 0));
			panelA.add(jsp1, BorderLayout.CENTER);
			panelA.add(jsp2, BorderLayout.CENTER);
			panelA.add(jsp3, BorderLayout.CENTER);
			panelA.add(new Panel(), BorderLayout.CENTER);

		panel.add(panelA, BorderLayout.CENTER);
//		panel.add(panelB, BorderLayout.SOUTH);
//		panel.add(panelB, BorderLayout.EAST);

		this.setJMenuBar(menu);
		this.setContentPane(panel);
		this.setSize(900,700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

			/*Menu*/
		if (e.getSource() == newprojmenu) {

			String ProjectName = JOptionPane.showInputDialog(null, "New Project's Name is: ","New Project", 3);
			if (ProjectName!=null)
			while (ProjectName.equals("")) {
				String tmp = JOptionPane.showInputDialog(null, "Project's Name cannot be empty" +
						"\nEnter another one: ","New Project", 3);
				if (tmp!=null) ProjectName = tmp;
				else break;
			}
			try {
				if (prjmng.newProject(ProjectName)) updateTreeProject();
				else
					JOptionPane.showMessageDialog(null, "Project "+ ProjectName + " already exists!");
			} catch (RemoteException e1) { e1.printStackTrace(); }

		} else if(e.getSource() == delprojmenu) {

			Object yes[]={"Yes","No"};
			if (projclicked != "") {
				int answer = JOptionPane.showOptionDialog(null,
						"Are you sure you want to delete project " +
						projclicked + " ?","Exit", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, yes, yes[0]);
				if (answer == 0) {
					try { prjmng.deleteProject(projclicked); }
					catch (RemoteException e1) { e1.printStackTrace(); }
				}
				updateTreeProject();
			} else JOptionPane.showMessageDialog(null,
					"You must first select a project!","Help",
					JOptionPane.INFORMATION_MESSAGE);

		} else if(e.getSource() == setman2proj) {

			if ( projclicked == "" || userclicked == "" ) {
				JOptionPane.showMessageDialog(null,
					"You must first select a project and a user!","Help",
					JOptionPane.INFORMATION_MESSAGE);
			} else {
				String url = "//" + Globals.SERVER + ":" + Globals.PORT	+ "/projects/" + projclicked;
				ProjIntrf prj;
				try {
					prj = (ProjIntrf) Naming.lookup(url);
					prj.setManager(userclicked);
					prj.addUser(userclicked); }
				catch (MalformedURLException e1) { e1.printStackTrace(); }
				catch (RemoteException e1) { e1.printStackTrace(); }
				catch (NotBoundException e1) { e1.printStackTrace(); }
				userclicked = "";
				projclicked = "";
			}

		} else if(e.getSource() == adduser2proj) {

			if (userprojclicked.equals(userclicked) && !userclicked.equals("") && !projclicked.equals("")) {
				JOptionPane.showMessageDialog(null, "User " + userclicked +
						" already assigned to project " + projclicked + "!","Help",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!userclicked.equals("") && !projclicked.equals("")) {
				try {
					String url = "//" + Globals.SERVER + ":" + Globals.PORT + "/projects/" + projclicked;
					ProjIntrf pjr = (ProjIntrf) Naming.lookup(url);
					pjr.addUser(userclicked);
					}
				catch (RemoteException e1) { e1.printStackTrace(); }
				catch (MalformedURLException e2) { e2.printStackTrace(); }
				catch (NotBoundException e3) { e3.printStackTrace(); }
				updateTreeUserProject();
			}

		} else if(e.getSource() == deluser2proj) {

			if (!userprojclicked.equals("") && !projclicked.equals("")) {
				try {
					String url = "//" + Globals.SERVER + ":" + Globals.PORT + "/projects/" + projclicked;
					ProjIntrf pjr = (ProjIntrf) Naming.lookup(url);
					pjr.removeUser(userprojclicked);
					}
				catch (RemoteException e1) { e1.printStackTrace(); }
				catch (MalformedURLException e2) { e2.printStackTrace(); }
				catch (NotBoundException e3) { e3.printStackTrace(); }
				updateTreeUserProject();

			}

		} else if(e.getSource() == newusermenu) {

			ArrayList<String> unames = null;
			int found = 0;
			try { unames = this.usrdb.getUsers(); }
			catch (RemoteException e1) { e1.printStackTrace(); }

			String n = JOptionPane.showInputDialog(null, "New User's Name is: ","New User", 3);
			if (n!=null) {
				if (unames!=null) {
					for (int i=0; i<unames.size(); i++) {
						if (unames.get(i).equals(n)) { found=1; }
					}
				}

				while (n.equals("") && found==0) {
					String tmp = JOptionPane.showInputDialog(null, "Username cannot be empty" +
							"\nEnter another one: ","New User", 3);
					if (tmp!=null) n = tmp;
					else break;
				}

				if (found==1) {
					JOptionPane.showMessageDialog(null, "User "+ n + " already exists!");
				} else {
					String p = JOptionPane.showInputDialog(null, "Enter a password for the new user: ","Password", 3);
					try { User u = new User(usrdb,n,p); u.save(); }
					catch (RemoteException e1) { e1.printStackTrace(); }
					updateTreeUser();
				}
			}

		} else if(e.getSource() == delusermenu) {

			Object yes[]={"Yes","No"};
			if (userclicked != "") {
				int answer = JOptionPane.showOptionDialog(null,
						"Are you sure you want to delete user " +
						userclicked + " ?","Exit", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, yes, yes[0]);
				if (answer == 0) {
					try { new User(usrdb, userclicked).remove(); }
					catch (RemoteException e1) { e1.printStackTrace(); }
				}
				updateTreeUser();
			} else JOptionPane.showMessageDialog(null,
					"You must first select a user!","Help",
					JOptionPane.INFORMATION_MESSAGE);

		}

		else if (e.getSource() == myProfile) { new MyProfile(admin); }

		else if (e.getSource() == exitnow) {

			Object ok[]={"Ok","Cancel"};
			int answer1 = JOptionPane.showOptionDialog
							(null,"Are you sure you want to exit?\n" +
							"Any unsaved changes will be lost!",
							"Exit", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null, ok, ok[0]);
			if (answer1 == 0) {
				String serverUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.LOGINSERVICE;
				try {
					UserLogin usrlg = (UserLogin) Naming.lookup(serverUrl);
					usrlg.deleteUser(this.admin.getName(), this.admin.getPassword());
				} catch (MalformedURLException e1) { e1.printStackTrace();
				} catch (RemoteException e1) { e1.printStackTrace();
				} catch (NotBoundException e1) { e1.printStackTrace(); }
				System.exit(0);
			}

		}

	}

	/**
	 * Πραγματοποιεί τη σύνδεση με το <code>server</code> που περιέχει τη βάση δεδομένων.
	 */
	public void connect2dbms() {
		int port = 4541;
		String server = Globals.SERVER;
		String dbmsname = Globals.SERVERNAME;
		String dbmsservice = Globals.SERVERDB;
		String dbmsUrl = "//" + server + ":" + port + "/" + dbmsname;
		try {
			Dbms dbs = (Dbms) Naming.lookup(dbmsUrl);
			if (dbs.getStatus() == "stopped") { dbs.startService(); }
			String UserdbUrl = "//" + server + ":" + dbs.getPort() + "/" + dbmsservice;
			this.usrdb = (UserDB) Naming.lookup(UserdbUrl);
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (RemoteException e) { e.printStackTrace();
		} catch (NotBoundException e) { e.printStackTrace(); }
	}

	/**
	 * Πραγματοποιεί τη σύνδεση με την τοπική υπηρεσία διαχείρισης <code>project</code>.
	 */
	public void connect2prjsrv() {
		// Σύνδεση με τον DedaServer ProjectService
		int port = Globals.PORT;
		String server = Globals.SERVER;
		String service = Globals.PROJECTSERVICE;
		String urlString = "//" + server + ":" + port + "/" + service;
		try { this.prjmng = (PrjAdmin) Naming.lookup(urlString); }
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (RemoteException e) { e.printStackTrace(); }
		catch (NotBoundException e) { e.printStackTrace(); }
	}

	/**
	 * Αρχικοποιεί τα <code>Menu</code> της οθόνης, μέσω των οποίων
	 * ο διαχειριστής έχει πρόσβαση στις λειτουργίες που θέλει να
	 * εκτελέσει.
	 */
	public void initiateMenu() {

		menu.add(profile);
			profile.add(myProfile);
				myProfile.addActionListener(this);
		menu.add(project);
			project.add(newprojmenu);
				newprojmenu.addActionListener(this);
			project.add(delprojmenu);
				delprojmenu.addActionListener(this);
			project.addSeparator();
			project.add(setman2proj);
				setman2proj.addActionListener(this);
			project.add(adduser2proj);
				adduser2proj.addActionListener(this);
			project.add(deluser2proj);
				deluser2proj.addActionListener(this);
		menu.add(user);
			user.add(newusermenu);
				newusermenu.addActionListener(this);
			user.add(delusermenu);
				delusermenu.addActionListener(this);
		menu.add(close);
			close.add(exitnow);
				exitnow.addActionListener(this);

	}

	/**
	 * Αρχικοποιεί το δέντρο με τα <code>project</code> που έχουν δηλωθεί στο σύστημα.
	 * Χρησιμοποιεί την ενεργή σύνδεση με τη βάση δεδομένων και αντλεί από αυτήν τη
	 * ζητούμενη πληροφορία.
	 */
	public void initiateTreeProject() {

			/*Tree Project*/
		topproj = new DefaultMutableTreeNode("Project List");
		ArrayList<String> pnames = null;
		try { pnames = this.usrdb.getProjects(); }
		catch (RemoteException e) { e.printStackTrace(); }
		if (pnames!=null) {
			for (int i=0; i<pnames.size(); i++) {
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(pnames.get(i));
				topproj.add(a);
			}
		}
		jTree1 = new JTree(topproj);
		jsp1 = new JScrollPane(jTree1);
		jsp1.setPreferredSize(new Dimension(500,300));
		jTree1.setPreferredSize(new Dimension(500,300));
		jTree1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = jTree1.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					int numberofclicks = me.getClickCount();
					if (m != null && numberofclicks == 1) {
						projclicked = m.toString();
						topusrppr.removeAllChildren();
						jTree2.updateUI();
						ArrayList<String> upnames = null;
						try { upnames = usrdb.getUsers(projclicked); }
						catch (RemoteException e) { e.printStackTrace(); }
						if (upnames!=null) {
							for (int i=0; i<upnames.size(); i++) {
								DefaultMutableTreeNode a = new DefaultMutableTreeNode(upnames.get(i));
								topusrppr.add(a);
							}
							jTree2.updateUI();
			}}}}});

	}

	/**
	 * Αρχικοποιεί το δέντρο με τους χρήστες που είναι ορισμένοι για το <code>project</code>.
	 * Επειδή κατά την αρχικοποίηση των δέντρων δεν επιλέγεται κάποιος κόμβος εξ ορισμού,
	 * το δέντρο αυτό είναι κενό και περιέχει μόνο την προτροπή της επιλογής ενός κόμβου
	 * από το δέντρο με τα <code>project</code>. Αφού γίνει η επιλογή, τότε το δέντρο αυτό
	 * ενημερώνεται από την {@link server.gui.HomeAdmin#updateTreeUserProject()}.
	 */
	public void initiateTreeUserProject() {

			/*Tree User/Project*/
		topusrppr = new DefaultMutableTreeNode("User/Project List");
		DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("Select a project");
		topusrppr.add(a1);
		jTree2 = new JTree(topusrppr);
		jsp2 = new JScrollPane(jTree2);
		jsp2.setPreferredSize(new Dimension(500,300));
		jTree2.setPreferredSize(new Dimension(500,300));
		jTree2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = jTree2.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					if (m != null) { userprojclicked = m.toString(); }
			}}});

	}

	/**
	 * Αρχικοποιεί το δέντρο με τους χρήστες που έχουν δηλωθεί στο σύστημα.
	 * Χρησιμοποιεί την ενεργή σύνδεση με τη βάση δεδομένων και αντλεί από αυτήν τη
	 * ζητούμενη πληροφορία.
	 */
	public void initiateTreeUser() {

			/*Tree User*/
		topusr = new DefaultMutableTreeNode("User List");
		ArrayList<String> unames = null;
		try { unames = this.usrdb.getUsers(); }
		catch (RemoteException e) { e.printStackTrace(); }
		if (unames!=null) {
			for (int i=0; i<unames.size(); i++) {
				if (!unames.get(i).equals("root")) {
					DefaultMutableTreeNode a = new DefaultMutableTreeNode(unames.get(i));
					topusr.add(a);
				}
			}
		}
		jTree3 = new JTree(topusr);
		jsp3 = new JScrollPane(jTree3);
		jsp3.setPreferredSize(new Dimension(500,300));
		jTree3.setPreferredSize(new Dimension(500,300));
		jTree3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = jTree3.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					if (m != null) { userclicked = m.toString(); }
			}}});

	}

	/**
	 * Ενημερώνει το δέντρο με τα <code>project</code> για τυχόν αλλαγές στη λίστα.
	 * Αντλεί τη λίστα από τη βάση δεδομένων, μέσω της ενεργούς σύνδεσης με τη βάση
	 * και ενημερώνει το δέντρο.
	 */
	public void updateTreeProject() {

		topproj.removeAllChildren();
		jTree1.updateUI();
		ArrayList<String> pnames = null;
		try { pnames = this.usrdb.getProjects(); }
		catch (RemoteException e1) { e1.printStackTrace(); }
		if (pnames!=null) {
			for (int i=0; i<pnames.size(); i++) {
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(pnames.get(i));
				topproj.add(a);
			}
		}
		jTree1.updateUI();

	}

	/**
	 * Ενημερώνει το δέντρο με τους χρήστες για τυχόν αλλαγές στη λίστα.
	 * Αντλεί τη λίστα από τη βάση δεδομένων, μέσω της ενεργούς σύνδεσης με τη βάση
	 * και ενημερώνει το δέντρο.
	 */
	public void updateTreeUser() {

		topusr.removeAllChildren();
		jTree3.updateUI();
		ArrayList<String> un = null;
		try { un = this.usrdb.getUsers(); }
		catch (RemoteException e1) { e1.printStackTrace(); }
		if (un!=null) {
			for (int i=0; i<un.size(); i++) {
				if (!un.get(i).equals("root")) {
					DefaultMutableTreeNode a = new DefaultMutableTreeNode(un.get(i));
					topusr.add(a);
				}
			}
		}
		jTree3.updateUI();

	}

	/**
	 * Ενημερώνει το δέντρο με τους χρήστες ανά <code>project</code> για τυχόν αλλαγές
	 * στη λίστα. Εκτελείται κάθε φορά που γίνεται μια νέα επιλογή <code>project</code>
	 * από το αντίστοιχο δέντρο. Αντλεί τη λίστα από τη βάση δεδομένων, μέσω της ενεργούς
	 * σύνδεσης με τη βάση και ενημερώνει το δέντρο.
	 */
	public void updateTreeUserProject() {

		topusrppr.removeAllChildren();
		jTree2.updateUI();
		ArrayList<String> upn = null;
		try { upn = this.usrdb.getUsers(projclicked); }
		catch (RemoteException e1) { e1.printStackTrace(); }
		if (upn!=null) {
			for (int i=0; i<upn.size(); i++) {
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(upn.get(i));
				topusrppr.add(a);
			}
		}
		jTree2.updateUI();

	}

}
