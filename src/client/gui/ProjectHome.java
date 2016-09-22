/**
 * @file ProjectHome.java
 */
package client.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import server.ifaces.BaseAdmin;
import server.ifaces.BaseRep;
import server.ifaces.BugAdmin;
import server.ifaces.BugIntrf;
import server.ifaces.ProjIntrf;
import server.ifaces.UserAccount;
import server.ifaces.UserLogin;
import client.DedaClient;
import client.deps.CurrentDate;
import client.deps.Globals;
import client.sys.LocalRepository;

/**
 * Η οθόνη διαχείρισης για το ορισμένο <code>project</code>. Είναι η τρίτη οθόνη
 * ιεραρχικά από την εκκίνηση του προγράμματος και βρίσκεται μετά την {@link client.gui.Home}.
 * Εμφανίζει μία λίστα με τα αρχεία του <code>project</code> και μια λίστα
 * με τα δηλωμένα <code>bug</code> από όλους τους ενεργούς χρήστες που
 * δουλεύουν στο <code>project</code>. Η οθόνη δίνει πολλές δυνατότητες διαχείρισης
 * λειτουργιών επί του κώδικα με γραφικές επιλογές μέσα από <code>Button</code>
 * και <code>Menu</code> για τις λειτουργίες που προσφέρουν τα εξωτερικά υποσυστήματα
 * του προγράμματος. Επίσης, με διπλό κλικ σε κάποιο <code>bug</code> ανοίγει η
 * οθόνη {@link client.gui.BugEditor} για την τροποποίηση των στοιχείων του.
 * Τέλος, αν η οθόνη αυτή εκτελείται για τον <code>manager</code> του <code>project</code>
 * τότε εμφανίζει και επιπρόσθετες λειτουργίες σχετικά με τη διαχείριση των χρηστών
 * και την ανάθεση νέων χρηστών στην ανάπτυξη του συγκεκριμένου <code>project</code>.
 * 
 * @author Team13
 * @version 1.5
 */
@SuppressWarnings("serial")
public class ProjectHome extends JFrame implements ActionListener, ItemListener {

	private UserAccount myuser;
	private ProjIntrf myproj;
	private LocalRepository rep;
	private BugAdmin bgmng;
	private boolean isManager;
	private boolean canPush;
	private ProjectHome myid;

	private JPanel panel = new JPanel();
	private JPanel cards = new JPanel(new CardLayout());
	/**
	 * <code>Panel</code> στο οποίο εμφανίζονται τα στοιχεία
	 * που αφορούν την τοπική αποθήκη λογισμικού.
	 */
	private JPanel card1 = new JPanel(); //localrepository
	/**
	 * <code>Panel</code> στο οποίο εμφανίζεται η λίστα με
	 * τα <code>bug</code.
	 */
	private JPanel card2 = new JPanel(); //bug
	/**
	 * <code>Panel</code> στο οποίο εμφανίζεται η λίστα με
	 * τους χρήστες του συστήματος, καθώς επίσης και η πληροφορία
	 * για το ποιοί δουλεύουν στο συγκεκριμένο <code>project</code>.
	 */
	private JPanel card3 = new JPanel(); //useradministration

	private JMenuBar menu = new JMenuBar();
	private JMenu file = new JMenu("File");
	private JMenu bugs = new JMenu("Bugs");
	private JMenu myRepository = new JMenu("LocalRepository");
	private JMenu baseRepository = new JMenu("BaseRepository");
	private JMenu teamAdmin = new JMenu("Team Management");

	private JMenuItem newFile = new JMenuItem("New File");
	private JMenuItem saveFile = new JMenuItem("Save File");
	private JMenuItem delFile = new JMenuItem("Delete File");
	private JMenuItem logout = new JMenuItem("Logout");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem bugItem = new JMenuItem("List Bugs");
	private JMenuItem newBugItem = new JMenuItem("New Bug");
	private JMenuItem myItem = new JMenuItem("View MyRepo");
	private JMenuItem commitItem = new JMenuItem("Commit Changes");
	private JMenuItem updateItem = new JMenuItem("Update MyRepo");
	private JMenuItem historyItem = new JMenuItem("MyHistory log");
	private JMenuItem baseItem = new JMenuItem("BaseHistory log");
	private JMenuItem pushItem = new JMenuItem("Push Changes");
	private JMenuItem teamItem = new JMenuItem("Recruitement");

	private JToolBar toolbar = new JToolBar();
	private JButton backb = new JButton();
	private JButton home = new JButton();
	private JButton commit = new JButton();
	private JButton push = new JButton();
	private JButton update = new JButton();
	private JButton deleteFile = new JButton();
	private JButton myHistory = new JButton();
	private JButton bugb = new JButton();

	private ImageIcon backIcon = new ImageIcon(Globals.IMG + "back.png");
	private ImageIcon homeIcon = new ImageIcon(Globals.IMG + "spitaki.png");
	private ImageIcon commitIcon = new ImageIcon(Globals.IMG + "commit.png");
	private ImageIcon pushIcon = new ImageIcon(Globals.IMG + "push.png");
	private ImageIcon updateIcon = new ImageIcon(Globals.IMG + "update.png");
	private ImageIcon deleteIcon = new ImageIcon(Globals.IMG + "delete.png");
	private ImageIcon historyIcon = new ImageIcon(Globals.IMG + "myHistory.png");
	private ImageIcon bugbIcon = new ImageIcon(Globals.IMG + "bugs.png");

	private JTextArea editor = new JTextArea(50, 50);

	private DefaultMutableTreeNode topfile;
	private DefaultMutableTreeNode topbug;
	private JTree treefile;
	private JTree treebug;
	private ArrayList<String> buglist;

	private ArrayList<JCheckBox> userList = new ArrayList<JCheckBox>();
	private ArrayList<JRadioButton> writeList = new ArrayList<JRadioButton>();
	/**
	 * Το σύνολο των χρηστών που έχουν καταχωρηθεί στο σύστημα.
	 */
	private ArrayList<String> ulist = new ArrayList<String>();	// Total Users registered on the system
	/**
	 * Το σύνολο των χρηστών που έχουν αναταθεί σε αυτό το <code>project</code>.
	 */
	private ArrayList<String> uplist = new ArrayList<String>();	// Users assigned to this project
	/**
	 * Η ιδιότητα-δικαίωμα εγγραφής στη {@link server.sys.BaseRepository} για κάθε χρήστη.
	 * Η θέση του κάθε δικαιώματος, αντιστοιχεί στο χρήστη με την ίδια θέση στη {@link client.gui.ProjectHome#uplist}
	 * 
	 * @see dbms.ifaces.UserDB
	 */
	private ArrayList<Integer> upwlist = new ArrayList<Integer>();// The write property for each user in the uplist

	private static String fileclicked = "" ;
	private static String bugclicked = "";
	private final static String MYREP = "MyRepositoty";
	private final static String USERADMIN = "Users' Administration";
	private final static String BUGS = "Bugs";

	/**
	 * Ο κατασκευαστής δημιουργεί την οθόνη διαχείρισης για το συγκεκριμένο <code>Project</code>.
	 * Αρχικά η οθόνη είναι εστιασμένη και παρουσιάζει τα αρχεία της τοπικής αποθήκης
	 * του ενεργού χρήστη.
	 * 
	 * @param user ο χρήστης που ενεργοποιεί την οθόνη
	 * @param project το <code>Project</code> για το οποίο ενεργοποιείται η οθόνη
	 */
	public ProjectHome(UserAccount user, ProjIntrf project) {

		super();
		String pn = "";
		try { pn = project.getName(); }
		catch (RemoteException e) { e.printStackTrace(); }
		super.setTitle("Project Screen, Project: " + pn);
		this.myid = this;
		this.myuser = user;
		this.myproj = project;
		this.isManager = false;
		this.canPush = false;
		try {
			rep = new LocalRepository(myuser.getName(), myproj.getName());
			rep.init();
			this.isManager = myuser.getName().equals(myproj.getManager());
			if (this.myproj.getWrite(this.myuser.getName()) == 1) this.canPush=true;
			}
		catch (RemoteException e1) { e1.printStackTrace(); }

		// Σύνδεση με τον DedaServer BugService
		String url = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.BUGSERVICE;
		try { this.bgmng = (BugAdmin) Naming.lookup(url); }
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (RemoteException e) { e.printStackTrace(); }
		catch (NotBoundException e) { e.printStackTrace(); }

		initiateMenu();
		initiateToolbar();
		initiateTreeFile();
		initiateTreeBug();
		if (this.isManager) initiateAdminLists();

		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.MAGENTA);
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(cards, BorderLayout.WEST); //kainourio
			cards.add(card1, MYREP);
				card1.setLayout(new GridLayout());
				card1.add(treefile);
			cards.add(card2, BUGS);
				card2.setLayout(new GridLayout());
				card2.add(treebug);
			cards.add(card3, USERADMIN);
				card3.setLayout(new GridLayout());
				card3.setBackground(Color.WHITE);
				card3.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.darkGray));
		panel.add(editor, BorderLayout.CENTER);
			editor.setBorder(BorderFactory.createEtchedBorder(Color.lightGray,Color.DARK_GRAY));

		this.setJMenuBar(menu);
		this.setContentPane(panel);
		this.setSize(800,600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		CardLayout c1 = (CardLayout)(cards.getLayout());

		if (e.getSource() == newFile) {

			String n = "";
			n = JOptionPane.showInputDialog(null, "Give filename: ","OK", 3);
			if (n!=null)
				if (n.equals("")) JOptionPane.showMessageDialog(null, "Empty is not a valid filename!");
				else {
					editor.setText("");
					rep.createFile(n, "");
					updateTreeFile();
				}

		} else if (e.getSource() == saveFile) {

			if (!fileclicked.equals(""))
				try {
					FileWriter fw = new FileWriter(new File(rep.getRepo_path() + fileclicked));
					fw.write(editor.getText());
					fw.close();
				} catch (Exception a) { a.printStackTrace(); }

		} else if (e.getSource() == delFile) {

			if (fileclicked.equals("")) JOptionPane.showMessageDialog(null, "You must select a file!");
			else {
				Object yes[]={"Yes","No"};
				int answer = JOptionPane.showOptionDialog(null,
						"Are you sure you want to delete file " +
						fileclicked + " ?","Exit", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, yes, yes[0]);
				if (answer==0) {
					rep.deleteFile(fileclicked);
					updateTreeFile();
				}
			}

		} else if (e.getSource() == logout) {

			String serverUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.LOGINSERVICE;
			try {
				UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
				usr.deleteUser(myuser.getName(), myuser.getPassword());
				this.dispose();
				new DedaClient();
			} catch (MalformedURLException e1) { e1.printStackTrace();
			} catch (RemoteException e1) { e1.printStackTrace();
			} catch (NotBoundException e1) { e1.printStackTrace(); }

		} else if (e.getSource() == exit) {

			String serverUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.LOGINSERVICE;
			try {
				UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
				usr.deleteUser(myuser.getName(), myuser.getPassword());
				this.dispose();
			} catch (MalformedURLException e1) { e1.printStackTrace();
			} catch (RemoteException e1) { e1.printStackTrace();
			} catch (NotBoundException e1) { e1.printStackTrace(); }
			System.exit(0);

		} else if (e.getSource() == bugItem) {

			c1.show(cards, BUGS);
			editor.setText("");
			editor.setEditable(false);
			bugToolbar();

		} else if (e.getSource() == newBugItem) {

			String bt = JOptionPane.showInputDialog("Enter a title for the bug :");
			if (bt!=null) {
				if (bt!="") {
					if (!this.buglist.contains(bt)) {
						try {
							String pname = this.myproj.getName();
							bgmng.newBug(bt, pname);
							String url = "//" + Globals.SERVER + ":" + Globals.PORT +
												"/projects/" + pname + "/bugs/" + bt;
							BugIntrf bg = (BugIntrf) Naming.lookup(url);
							String auth = bg.getAuthor();
							if (auth==null) {bg.setAuthor(myuser.getName());}
							new BugEditor(this, bg);
						} catch (RemoteException e1) { e1.printStackTrace();
						} catch (MalformedURLException e2) { e2.printStackTrace();
						} catch (NotBoundException e3) { e3.printStackTrace(); }
					}
				}
			}

		} else if (e.getSource() == myItem) {

			c1.show(cards, MYREP);
			editor.setText("");
			editor.setEditable(true);
			resetToolbar();

		} else if (e.getSource() == commitItem) {

			String n = JOptionPane.showInputDialog(null, "Comment: ","Commit", 3);
			if (n!=null) {
				if (n.equals("")) { JOptionPane.showMessageDialog(null, "You must enter a comment!"); }
				else { rep.commit(n); }
			}

		} else if (e.getSource() == updateItem) {

			rep.commit("Auto Commit before update at " + CurrentDate.now());
			rep.update();
			updateTreeFile();

		} else if (e.getSource() == historyItem) {

			new History(this, rep);

		} else if (e.getSource() == baseItem) {

			String basemngUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/" + Globals.REPOSERVICE;
			try {
				BaseAdmin bm = (BaseAdmin) Naming.lookup(basemngUrl);
				bm.newBase(myproj.getName());
				String baseUrl = "//" + Globals.SERVER + ":" + Globals.PORT + "/projects/" +
												myproj.getName() + Globals.PATH_2_BASE;
				BaseRep br = (BaseRep) Naming.lookup(baseUrl);
				new History(this, br);
			} catch (MalformedURLException e2) { e2.printStackTrace();
			} catch (RemoteException e2) { e2.printStackTrace();
			} catch (NotBoundException e2) { e2.printStackTrace(); }

		} else if (e.getSource() == pushItem) {

			if (canPush) { rep.push(); }
			else JOptionPane.showMessageDialog(null, "Oops! You cannot do that");

		} else if (e.getSource() == teamItem) {

			c1.show(cards, USERADMIN);
			editor.setText("");
			editor.setEditable(false);
			resetToolbar();

		} else if (e.getSource() == backb) {

			try { new Home(myuser); }
			catch (HeadlessException e1) { e1.printStackTrace(); }
			catch (RemoteException e1) { e1.printStackTrace(); }
			this.setVisible(false);
			this.dispose();
			 
		} else if (e.getSource() == home) {

			c1.show(cards, MYREP);
			editor.setText("");
			editor.setEditable(true);
			resetToolbar();

		} else if (e.getSource() == commit) {

			String n = JOptionPane.showInputDialog(null, "Comment: ","Commit", 3);
			if (n!=null) {
				if (n.equals("")) { JOptionPane.showMessageDialog(null, "You must enter a comment!"); }
				else { rep.commit(n); }
			}

		} else if (e.getSource() == push) {

			if (canPush) { rep.push(); }
			else JOptionPane.showMessageDialog(null, "Oops! You cannot do that");

		} else if (e.getSource() == update) {

			rep.commit("Auto Commit before update at " + CurrentDate.now());
			rep.update();
			updateTreeFile();

		} else if(e.getSource() == deleteFile) {

			if (fileclicked.equals("")) JOptionPane.showMessageDialog(null, "You must select a file!");
			else {
				Object yes[]={"Yes","No"};
				int answer = JOptionPane.showOptionDialog(null,
						"Are you sure you want to delete file " +
						fileclicked + " ?","Exit", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, yes, yes[0]);
				if (answer==0) {
					rep.deleteFile(fileclicked);
					updateTreeFile();
				}
			}

		} else if(e.getSource() == myHistory) {

			new History(this, rep);

		} else if (e.getSource() == bugb) {

			c1.show(cards, BUGS);
			editor.setText("");
			editor.setEditable(false);
			bugToolbar();

		}

	}

	/**
	 * Ενημερώνει το δέντρο με τα αρχεία της τοπικής αποθήκης.
	 * Πρώτα αδειάζει το δέντρο και στη συνέχεια κάνει λίστα
	 * όλα τα αρχεία τοπικά και κάθε ένα από αυτά τα προσθέτει
	 * ως κόμβο στο δέντρο.
	 */
	public void updateTreeFile() {

		String[] dir = new File(rep.getRepo_path()).list();
		topfile.removeAllChildren();
		treefile.updateUI();
		if (dir!=null) {
			for (int i=0;i<dir.length;i++) {
				if (! dir[i].equals(".hg")) {
					DefaultMutableTreeNode a = new DefaultMutableTreeNode(dir[i]);
					topfile.add(a);
				}
			}
			treefile.updateUI();
		}

	}

	/**
	 * Ενημερώνει το δέντρο με τη λίστα των <code>bug</code> που έχουν αναφερθεί
	 * για το συγκεκριμένο <code>project</code> που έχει επιλεγεί. Πρώτα αδειάζει
	 * το δέντρο και στη συνέχεια παίρνει τη λίστα κατευθείαν από το <code>server</code>.
	 * Τέλος, για κάθε στοιχείο της λίστας, δημιουργεί από ένα κόμβο για το δέντρο.
	 */
	public void updateTreeBug() {

		try {
			buglist = myproj.getBugNames();
			topbug.removeAllChildren();
			treebug.updateUI();
			if (buglist!=null) {
				for (int i=0; i<buglist.size(); i++) {
					DefaultMutableTreeNode b = new DefaultMutableTreeNode(buglist.get(i));
					topbug.add(b);
				}
				treebug.updateUI();
			}
		} catch (RemoteException e) { e.printStackTrace(); }

	}

	/**
	 * Αρχικοποιεί τα <code>Menu</code> της οθόνης. Καλείται από τον κατασκευαστή.
	 */
	public void initiateMenu() {

		menu.setBorder(BorderFactory.createEtchedBorder(Color.lightGray,Color.lightGray));
		menu.add(file);
			file.add(newFile);
				newFile.addActionListener(this);
			file.add(saveFile);
				saveFile.addActionListener(this);
			file.add(delFile);
				delFile.addActionListener(this);
			file.addSeparator();
			file.add(logout);
				logout.addActionListener(this);
			file.add(exit);
				exit.addActionListener(this);
		menu.add(bugs);
			bugs.add(bugItem);
				bugItem.addActionListener(this);
			bugs.add(newBugItem);
				newBugItem.addActionListener(this);
		menu.add(myRepository);
			myRepository.add(myItem);
				myItem.addActionListener(this);
			myRepository.add(commitItem);
				commitItem.addActionListener(this);
			myRepository.add(pushItem);
				pushItem.addActionListener(this);
			myRepository.add(historyItem);
				historyItem.addActionListener(this);
		menu.add(baseRepository);
			baseRepository.add(updateItem);
				updateItem.addActionListener(this);
		if (this.isManager) {
			baseRepository.add(baseItem);
				baseItem.addActionListener(this);
		}
		if (this.isManager) {
			menu.add(teamAdmin);
				teamAdmin.add(teamItem);
					teamItem.addActionListener(this);
		}

	}

	/**
	 * Αρχικοποιεί την <code>Toolbar</code> της οθόνης. Καλείται από τον κατασκευαστή.
	 */
	public void initiateToolbar() {

		toolbar.add(backb);
			backb.setIcon(backIcon);
			backb.setToolTipText("Back to Home Screen");
			//backb.setBackground(Color.lightGray);
			backb.setBackground(Color.white);
				backb.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(home);
			home.setIcon(homeIcon);
			home.setToolTipText("MyRepository");
			//home.setBackground(Color.lightGray);
			home.setBackground(Color.white);
				home.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(commit);
			commit.setIcon(commitIcon);
			commit.setToolTipText("Commit");
			//commit.setBackground(Color.lightGray);
			commit.setBackground(Color.white);
				commit.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(push);
			push.setIcon(pushIcon);
			push.setToolTipText("Push");
			//push.setBackground(Color.lightGray);
			push.setBackground(Color.white);
				push.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(update);
			update.setIcon(updateIcon);
			update.setToolTipText("Update");
			//update.setBackground(Color.lightGray);
			update.setBackground(Color.white);
				update.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(deleteFile);
			deleteFile.setIcon(deleteIcon);
			deleteFile.setToolTipText("Delete File");
			//deleteFile.setBackground(Color.lightGray);
			deleteFile.setBackground(Color.white);
				deleteFile.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(myHistory);
			myHistory.setIcon(historyIcon);
			myHistory.setToolTipText("My History");
			//myHistory.setBackground(Color.lightGray);
			myHistory.setBackground(Color.white);
				myHistory.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(bugb);
			bugb.setIcon(bugbIcon);
			bugb.setToolTipText("View bugs");
			//bugb.setBackground(Color.lightGray);
			bugb.setBackground(Color.white);
				bugb.addActionListener(this);

	}

	/**
	 * Επανεκκινεί την <code>Toolbar</code> της οθόνης, δλδ την επαναφέρει
	 * στην αρχική της κατάσταση.
	 */
	public void resetToolbar() {

		toolbar.removeAll();
		toolbar.add(backb);
		toolbar.addSeparator();
		toolbar.add(home);
		toolbar.addSeparator();
		toolbar.add(commit);
		toolbar.addSeparator();
		toolbar.add(push);
		toolbar.addSeparator();
		toolbar.add(update);
		toolbar.addSeparator();
		toolbar.add(deleteFile);
		toolbar.addSeparator();
		toolbar.add(myHistory);
		toolbar.addSeparator();
		toolbar.add(bugb);

	}

	/**
	 * Μετατρέπει την <code>Toolbar</code> και περιορίζει τις διαθέσιμες από αυτήν
	 * λειτουργίες για διαχείριση των <code>bug</code>.
	 */
	public void bugToolbar() {

		toolbar.removeAll();
		toolbar.add(backb);
		toolbar.addSeparator();
		toolbar.add(home);
		toolbar.addSeparator();
		toolbar.add(bugb);

	}

	/**
	 * Αρχικοποιεί το δέντρο με τα αρχεία της τοπικής αποθήκης λογισμικού.
	 * Καλείται από τον κατασκευαστή.
	 */
	public void initiateTreeFile() {

		topfile = new DefaultMutableTreeNode("File List");
		treefile = new JTree(topfile);
		treefile.setPreferredSize(new Dimension(150,50));
		treefile.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.darkGray));
		JScrollPane jsp = new JScrollPane(treefile);
		jsp.setPreferredSize(new Dimension(150,50));
		this.updateTreeFile();
		treefile.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = treefile.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					int numberofclicks = me.getClickCount();
					if (m != null && numberofclicks == 1) {
						fileclicked = m.toString();
						editor.setText("");
						try {
							BufferedReader bufferfile = new BufferedReader(new FileReader(rep.getRepo_path() + fileclicked));
							String line ="";
							while ((line = bufferfile.readLine()) != null) {
								editor.append(line);
								editor.append(System.getProperty("line.separator"));
							}
						} catch (Exception a) { a.printStackTrace(); }
			}}}});

	}

	/**
	 * Αρχικοποιεί το δέντρο με τη λίστα των <code>Bug</code> για το συγκεκριμένο
	 * <code>Project</code>. Καλείται από τον κατασκευαστή.
	 */
	public void initiateTreeBug() {

		//Tree Bugs
		topbug = new DefaultMutableTreeNode("Bug List");
		treebug = new JTree(topbug);
		treebug.setPreferredSize(new Dimension(150,50));
		treebug.setBorder(BorderFactory.createEtchedBorder(Color.orange,Color.darkGray));
		JScrollPane jsp2 = new JScrollPane(treebug);
		jsp2.setPreferredSize(new Dimension(150,50));
		this.updateTreeBug();
		treebug.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				TreePath tp = treebug.getPathForLocation(me.getX(), me.getY());
				if ( tp != null && tp.getPathCount() > 1 ) {
					Object m = tp.getPathComponent(1);
					int numberofclicks = me.getClickCount();
					if (m != null && numberofclicks == 1) {
						bugclicked = m.toString();
						editor.setText("");
						try {
							String url = myproj.getBug(bugclicked);
							BugIntrf bg = (BugIntrf) Naming.lookup(url);
							editor.append("Bug Title: " + bg.getName());
							editor.append(System.getProperty("line.separator"));
							editor.append("Author: " + bg.getAuthor());
							editor.append(System.getProperty("line.separator"));
							editor.append("Priority: " + bg.getPriority());
							editor.append(System.getProperty("line.separator"));
							editor.append("Status: " + bg.getStatus());
							editor.append(System.getProperty("line.separator"));
							editor.append("Date of Birth: " + bg.getDateOfCreation());
							editor.append(System.getProperty("line.separator"));
							editor.append("Date of Death: " + bg.getDeadline());
							editor.append(System.getProperty("line.separator"));
							editor.append(System.getProperty("line.separator"));
							editor.append("Message Body:");
							editor.append(System.getProperty("line.separator"));
							editor.append(bg.getDescription());
							editor.append(System.getProperty("line.separator"));
						} catch (RemoteException e) { e.printStackTrace();
						} catch (MalformedURLException e) { e.printStackTrace();
						} catch (NotBoundException e) { e.printStackTrace(); }
					} else if (m != null && numberofclicks == 2) {
						try {
							String url = myproj.getBug(bugclicked);
							BugIntrf bg = (BugIntrf) Naming.lookup(url);
							new BugEditor(myid, bg);
						} catch (RemoteException e) { e.printStackTrace();
						} catch (MalformedURLException e) { e.printStackTrace();
						} catch (NotBoundException e) { e.printStackTrace(); }
			}}}});

	}

	/**
	 * Αρχικοποιεί το δέντρο με τις λίστες διαχείρισης χρηστών για το <code>Project</code>.
	 * Καλείται από τον κατασκευαστή μόνο αν ο ενεργός στο σύστημα χρήστης είναι o
	 * <code>manager</code> στο ενεργό <code>Project</code>.
	 * <p>
	 * Αρχικά ενημερώνονται οι λίστες με συνεπή δεδομένα από το <code>server</code>.
	 * Έπειτα, ενεργοποιούνται τα γραφικά αντικείμενα πάνω στα οποία θα παρουσιαστεί
	 * η πληροφορία σχετικά με τη διαχείριση και την ενεργοποίηση των χρηστών.
	 * Τέλος, για όλους τους χρήστες δημιουργείται ένα <code>CheckBox</code> κι ένα
	 * <code>RadioButton</code>. Το <code>CheckBox</code> τικάρεται για κάθε χρήστη,
	 * ο οποίος είναι δηλωμένος στο <code>Project</code> και αντίστοιχα ενεργοποιείται
	 * το <code>RadioButton</code> δίπλα του για να οριστεί το δικαίωμα εγγραφής για
	 * αυτόν τον χρήστη στη βασική αποθήκη λογισμικού που υπάρχει στο <code>server</code>.
	 * 
	 * @see dbms.ifaces.UserDB
	 */
	public void initiateAdminLists() {

		try {
			this.ulist = this.myuser.getUsers();
			this.uplist = this.myproj.getUsers();
			if (this.uplist!=null)
				for (int i=0; i<uplist.size(); i++) {
					this.upwlist.add(this.myproj.getWrite(this.uplist.get(i)));
				}
		} catch (RemoteException e) { e.printStackTrace(); }

		JPanel contmng = new JPanel();
		contmng.setLayout(new GridLayout(0,1));
		JScrollPane jsp = new JScrollPane(contmng);
		int j=0;
		if (this.ulist!=null)
		for (int i=0; i<this.ulist.size(); i++) {

			JPanel rows = new JPanel();
			rows.setLayout(new GridLayout(1,2));
			rows.setBackground(Color.WHITE);

			JCheckBox userCheck = new JCheckBox(this.ulist.get(i));
			userCheck.setBackground(Color.WHITE);
			userList.add(userCheck);

			JRadioButton right = new JRadioButton("Write");
			if (this.uplist.contains(this.ulist.get(i))) {
				right.setEnabled(true);
				userCheck.setSelected(true);
				if (this.upwlist.get(j)==1) right.setSelected(true);
				else right.setSelected(false);
				j++;
			} else {
				right.setSelected(false);
				right.setEnabled(false);
			}
			writeList.add(right);

			rows.add(userCheck);
			rows.add(right);

			contmng.add(rows);

			userCheck.addItemListener(this);
			right.addItemListener(this);

		}
		card3.add(jsp);
	}

	public void itemStateChanged(ItemEvent e) {
		for (int i=0; i<userList.size(); i++) {
			JCheckBox userC = userList.get(i);
			JRadioButton userW = writeList.get(i);
			try {
				if (e.getSource() == userC) {
					if (userC.isSelected()) {
						userW.setEnabled(true);
						myproj.addUser(ulist.get(i));
						myproj.setWrite(ulist.get(i), 0);
					} else {
						userW.setEnabled(false);
						myproj.removeUser(ulist.get(i));
					}
				} else if (e.getSource() == userW) {
					if (userW.isSelected()) {
						myproj.setWrite(ulist.get(i), 1);
					} else {
						myproj.setWrite(ulist.get(i), 0);
					}
				}
			} catch (RemoteException e1) { e1.printStackTrace(); }
		}//end for
	}

}