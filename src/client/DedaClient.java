/**
 * @file DedaClient.java
 */
package client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.ifaces.UserLogin;

import client.deps.CurrentDate;
import client.deps.Globals;
import client.gui.Login;

/**
 * Η κεντρική κλάση του <code>client</code>.
 * Κάνει τις απαραίτητες αρχικοποιήσεις, τη διασύνδεση με το <code>server</code>
 * και αν η σύνδεση είναι επιτυχημένη περνά την εκτέλεση του προγράμματος στην
 * πρώτη οθόνη που βλέπει ο χρήστης, την {@link client.gui.Login}.
 * 
 * @author Team13
 * @version 1.0
 */
public class DedaClient {

	/**
	 * Το port στο οποίο ανεβαίνουν τα service
	 */
	public static int port = Globals.PORT;

	/**
	 * Η διεύθυνση του server
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
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String projectService = Globals.PROJECTSERVICE;

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>bug</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String bugService = Globals.BUGSERVICE;

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>repository</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String repoService = Globals.REPOSERVICE;

	/**
	 * Ο logger της java μέσω του οποίου γίνονται οι καταγραφές συμβάντων.
	 */
	private static final Logger logger = Logger.getLogger(DedaClient.class.getName());

	/**
	 * Η διαδρομή (σχετική) του φακέλου στον οποίο αποθηκεύονται τα logs.
	 */
	private static final String logpath = Globals.PATH_2_LOG;

	/**
	 * Η ονομασία του αρχείου καταγραφής γεγονότων.
	 */
	private static final String logfile = "client_history";

	/**
	 * Η διεύθυνση του <code>server</code>. Η μεταβλητή αυτή υπάρχει για να μπορούμε
	 * να πειράζουμε την κεντρική σταθερά με τιμές εισαγόμενες από το χρήστη.
	 */
	public static String serverS;

	/**
	 * Η πόρτα στην οποία ακούει ο <code>server</code> για συνδέσεις.
	 * Η μεταβλητή αυτή υπάρχει για να μπορούμε να πειράζουμε την
	 * κεντρική σταθερά με τιμές εισαγόμενες από το χρήστη.
	 */
	public static String portS;

	/**
	 * Ο κατασκευαστής κάνει ότι και η κύρια μέθοδος της κλάσης.
	 * Χρησιμοποιέιται μόνο σε περίπτωση επανεκκίνησης του <code>client</code>
	 * από το ίδιο το πρόγραμμα, όταν ο χρήστης τερματίζει τη συνεδρία του,
	 * αλλά δεν τερματίζει την εκτέλεση του προγράμματος. Οι παράμετροι
	 * εκτέλεσης δεν αλλάζουν, συνεπώς ο κώδικας είναι λιγότερος συγκριτικά
	 * με την κύρια μέθοδο του προγράμματος, γιατί περιέχει λιγότερους ελέγχους.
	 */
	public DedaClient() {

		String serverS = server;
		String portS = "" + port;

		// Σύνδεση με τον DedaServer LoginService
		String serverUrl = "//" + serverS + ":" + portS + "/" + loginService;
		logger.info("server url = " + serverUrl);
		try {
			UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
			new Login(usr);
		} catch (MalformedURLException e) { e.printStackTrace(); logger.severe(e.getMessage());
		} catch (RemoteException e) {
			if (e.getMessage().contains("Connection refused")) System.out.println(e.getMessage());
			else e.printStackTrace();
			logger.severe(e.getMessage());
		} catch (NotBoundException e) { e.printStackTrace(); logger.severe(e.getMessage()); }

	}

	/**
	 * Αρχικοποιεί κάποιες ιδιότητες του <code>logger</code>.
	 * Μετά την αρχικοποίηση, η καταγραφή γεγονότων γίνεται κανονικά και
	 * αποθηκεύεται σε ορισμένο αρχείο στο σύστημα αρχείων.
	 */
	public static void initlogger() {

		File fs = new File(logpath);
		if (!fs.exists()) { fs.mkdir(); }
		fs = new File(logpath + logfile + ".log");
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
	 * Εκτυπώνει στην οθόνη μήνυμα χρήσης του προγράμματος.
	 */
	public static void printUsage() {

		System.out.println("Usage :");
		System.out.println("  java " + DedaClient.class.getName() +
				" hostname port");
		System.out.println("  java " + DedaClient.class.getName() +
				" ipaddress port");
		System.out.println("  java " + DedaClient.class.getName());

	}

	/**
	 * Η κύρια μέθοδος του προγράμματος εκκινεί τις διαδικασίες λειτουργίας του <code>client
	 * </code>. Εκτελεί όλες τις διαδικασίες αρχικοποίησης και ανοίγει την πρώτη οθόνη στον
	 * χρήστη, την οθόνη αυθεντικοποίησης χρήστη {@link client.gui.Login}.
	 * <p>
	 * Οι διαδικασίες που εκτελεί κατά σειρά είναι οι εξής:<br>
	 * αρχικοποίηση του <code>logger</code><br>
	 * έλεγχο ορισμάτων γραμμής εντολών<br>
	 * απόδοση ορισμάτων γραμμής εντολών σε μεταβλητές προγράμματος<br>
	 * εκκίνηση ενός <code>security manager</code><br>
	 * σύνδεση με την υπηρεσία αυθεντικοποίησης στο <code>server</code><br>
	 * ενεργοποίηση οθόνης αυθεντικοποίησης στον <code>client</code>
	 * 
	 * @param args η διεύθυνση και η πόρτα σύνδεσης στο <code>server</code>
	 */
	public static void main(String[] args) {

		logger.info("Initialising logger properties");
		initlogger();

		String serverS = server;
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

		// initiating security manager
		logger.info("Initiating Security Manager");
		if (System.getSecurityManager() == null)
		System.setSecurityManager (new RMISecurityManager());

		// Σύνδεση με τον DedaServer LoginService
		String serverUrl = "//" + serverS + ":" + portS + "/" + loginService;
		logger.info("server url = " + serverUrl);
		try {
			UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
			new Login(usr);
		} catch (MalformedURLException e) { e.printStackTrace(); logger.severe(e.getMessage());
		} catch (RemoteException e) {
			if (e.getMessage().contains("Connection refused")) System.out.println(e.getMessage());
			else e.printStackTrace();
			logger.severe(e.getMessage());
		} catch (NotBoundException e) { e.printStackTrace(); logger.severe(e.getMessage()); }

	}

}
