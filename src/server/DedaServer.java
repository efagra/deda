/**
 * @file DedaServer.java
 */
package server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.deps.CurrentDate;
import server.deps.Globals;
import server.sys.BaseActivator;
import server.sys.BugList;
import server.sys.ProjectList;
import server.sys.UserList;

import dbms.ifaces.Dbms;
import dbms.ifaces.UserDB;

/**
 * Η κεντρική κλάση του <code>server</code>.
 * Κάνει τις απαραίτητες αρχικοποιήσεις, τη διασύνδεση με το <code>dbms</code>
 * και αν η σύνδεση είναι επιτυχημένη, ενεργοποιεί μία μία τις υπηρεσίες που
 * υλοποιεί η εφαρμογή.
 * 
 * @author Team13
 * @version 1.0
 */
public class DedaServer {

	/**
	 * Το <code>port</code> μέσω του οποίου επικοινωνεί
	 * ο <code>server</code> με τo <code>dbms</code>.
	 */
	public static final int port = 4541;

	/**
	 * Το <code>port</code> στο οποίο ανεβαίνουν τα <code>service</code>
	 */
	public static int ports = Globals.PORT;

	/**
	 * Η διεύθυνση του <code>server</code>
	 */
	public static final String dbms = Globals.SERVER;

	/**
	 * Η ονομασία του <code>server</code> περιέχει τη βάση δεδομένων.
	 */
	public static final String dbmsname = Globals.SERVERNAME;

	/**
	 * Η ονομασία της υπηρεσίας για τη βάση δεδομένων.
	 */
	public static final String dbmsservice = Globals.SERVERDB;

	/**
	 * Η διεύθυνση του <code>server</code>
	 */
	public static final String server = Globals.SERVER;

	/**
	 * Η ονομασία της υπηρεσίας αυθεντικοποίησης στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String loginService = Globals.LOGINSERVICE;

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>project</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String projectService = Globals.PROJECTSERVICE;

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>bug</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String bugService = Globals.BUGSERVICE;

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>repository</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String repoService = Globals.REPOSERVICE;

	/**
	 * Ο <code>logger</code> της <code>java</code> μέσω του οποίου γίνονται οι καταγραφές συμβάντων.
	 */
	private static final Logger logger = Logger.getLogger(DedaServer.class.getName());

	/**
	 * Η σχετική διαδρομή προς το φάκελο με τα log
	 */
	private static final String logpath = Globals.PATH_2_LOG;

	/**
	 * Η ονομασία του αρχείου καταγραφής γεγονότων.
	 */
	private static final String logfile = "server_history";

	/**
	 * Αρχικοποιεί κάποιες ιδιότητες του logger.
	 * Μετά την αρχικοποίηση, η καταγραφή γεγονότων γίνεται κανονικά και
	 * αποθηκεύεται σε ορισμένο αρχείο στο σύστημα αρχείων.
	 */
	public static void initlogger() {
		File fs = new File(logpath);
		if (!fs.exists()) { fs.mkdir(); }
		fs = new File(logpath + logfile +".log");
		if (fs.exists()) { fs.renameTo(new File(logpath + logfile + "_" +
											CurrentDate.timestamp() + ".log")); }
		try {
			FileHandler fh = new FileHandler(logpath + logfile + ".log");
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			logger.info("Logging started...");
		} catch (SecurityException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Εκκινεί την <code>rmiregistry</code> στο ορισμένο <code>port</code>. Στη συνέχεια τυπώνει ανάλογο μήνυμα.
	 * Αν η registry έχει ήδη εκκινηθεί τυπώνει ανάλογο μήνυμα. Η κίνηση καταγράφεται
	 * μέσω του logger.
	 * 
	 * @param port το <code>port</code> που σηκώνεται η <code>rmiregistry</code>, δλδ το <code>port</code> στο οποίο ακούει
	 * 				ο <code>server</code> ή το <code>service</code>.
	 */
	public static void startRegistry(int port) {
		try {
			LocateRegistry.createRegistry(port);
			System.out.println(CurrentDate.now() + " RMI registry started on port " + port + ".");
			logger.info("RMI registry started on port " + port + ".");
		} catch (RemoteException e) {
			if (e.getMessage().contains("Port already in use")) {
				logger.info("Port " + port + " already in use.");
				System.out.println(CurrentDate.now() + " RMI registry found.");
				logger.info("RMI registry found on port " + port + ".");
			} else e.printStackTrace();
		}
	}

	/**
	 * Εκτυπώνει στην οθόνη μήνυμα χρήσης του προγράμματος.
	 */
	public static void printUsage() {
		System.out.println("Usage :");
		System.out.println("  java " + DedaServer.class.getName() +
				" hostname port");
		System.out.println("  java " + DedaServer.class.getName() +
				" ipaddress port");
		System.out.println("  java " + DedaServer.class.getName());		
	}

	/**
	 * Η κύρια μέθοδος του κεντρικού <code>server</code> της εφαρμογής.
	 * Ύστερα από τις απαραίτητες αρχικοποιήσεις εκκινεί και τα <code>service</code>.
	 * Δέχεται είτε και τα δύο ορίσματα είτε κανένα. Σε κάθε άλλη περίπτωση
	 * τερματίζει η λειτουργία με την εκτύπωση μηνύματος χρήσης. Αν δεν δοθεί κανένα
	 * όρισμα τότε θεωρείται ότι ο <code>dbms server</code> είναι <code>local</code>.
	 * <p>
	 * Αρχικά ενεργοποιεί ένα διαχειριστή ασφάλειας, μέσω του οποίου ορίζουμε κανόνες
	 * ασφάλειας και δικαιωμάτων που αφορούν το πρόγραμμα, έξω από το πρόγραμμα.
	 * Οι ορισμοί ασφάλειας γίνονται σε ένα αρχείο κειμένου τύπου <code>java.policy</code>.
	 * Στη συνέχεια, αρχικοποιεί τις παραμέτρους καταγραφής γεγονότων. Έπειτα, ελέγχει
	 * αν υπάρχουν ορίσματα και τα εισάγει στο πρόγραμμα, εκκινεί την <code>rmiregistry</code> και
	 * τελικά εκκινεί και τις υπηρεσίας τη μία μετά την άλλη.
	 * 
	 * @param dbms_server_address η διεύθυνση του <code>dbms server</code>
	 * @param port το <code>port</code> στο οποίο ακούει ο <code>dbms server</code>
	 */
	public static void main(String[] args) {

		// initiating security manager
		if (System.getSecurityManager() == null)
		System.setSecurityManager (new RMISecurityManager());

		logger.info("Initialising logger properties");
		initlogger();

		String serverS = dbms;
		String portS = "" + port;
		logger.info("Checking cmd arguments");
		if (args.length == 2) {
			serverS = args[0];
			portS = args[1];
			logger.info("Acquired new dbms address (" + serverS + 
									") and port (" + portS + ")");
		} else if (args.length == 0) {
			logger.info("No cmd arguments," +
						" falling back to defaults..");
		} else {
			logger.info("Unrecognised cmd arguments");
			printUsage();
			System.exit(-1);
		}

		logger.info("Starting rmiregistry");
		startRegistry(ports);
		
		try {
			// Σύνδεση με τον dbms server
			String serverUrl = "//" + serverS + ":" + portS + "/" + dbmsname;
			System.out.println("url = " + serverUrl);
			logger.info("dbms url = " + serverUrl);
			Dbms dbs = (Dbms) Naming.lookup(serverUrl);
			if (dbs.getStatus() == "stopped") { dbs.startService(); } // Χειροκίνητη εκκίνηση του service
			String UserdbUrl = "//" + serverS + ":" + dbs.getPort() + "/" + dbmsservice;
			UserDB usrdb = (UserDB) Naming.lookup(UserdbUrl); // Σύνδεση με το service του dbms server

			// Εκκίνηση της υπηρεσίας αυθεντικοποίησης
			logger.info("Starting login service...");
			UserList loginservice = new UserList(usrdb);
			String urlString = "//"+server+":"+ports+"/"+loginService;
			Naming.rebind (urlString, loginservice);
			logger.info("Login service started");

			// Εκκίνηση της υπηρεσίας διαχείρισης project
			logger.info("Starting project management service...");
			ProjectList prjadmn = new ProjectList(usrdb);
			urlString = "//"+server+":"+ports+"/"+projectService;
			Naming.rebind (urlString, prjadmn);
			logger.info("Project management service started");

			// Εκκίνηση της υπηρεσίας διαχείρισης bug
			logger.info("Starting bug management service...");
			BugList bugadmin = new BugList();
			urlString = "//"+server+":"+ports+"/"+bugService;
			Naming.rebind (urlString, bugadmin);
			logger.info("Bug management service started");

			// Εκκίνηση της υπηρεσίας διαχείρισης βασικών αποθετηρίων
			logger.info("Starting base repository management service...");
			BaseActivator baseadmin = new BaseActivator();
			urlString = "//"+server+":"+ports+"/"+repoService;
			Naming.rebind (urlString, baseadmin);
			logger.info("Base repository management service started");

		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (RemoteException e) {
			if (e.getMessage().contains("Connection refused"))
				System.out.println(e.getMessage());
			else e.printStackTrace();
			logger.severe(e.getMessage());
			System.exit(-1);
		} catch (NotBoundException e) { e.printStackTrace(); }

	}

}
