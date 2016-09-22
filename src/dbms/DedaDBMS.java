/**
 * @file DedaDBMS.java
 */
package dbms;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import dbms.deps.CurrentDate;
import dbms.ifaces.Dbms;
import dbms.sys.SysDB;

/**
 * Υλοποιεί τη διασύνδεση με το <code>server</code>, που δίνει πρόσβαση στη βάση δεδομένων.
 * Περιέχει την υλοποίηση των μεθόδων της διεπαφής {@link dbms.ifaces.UserDB}.
 * Επίσης, περιέχει μεθόδους οργανωμένου κώδικα που αφορά ορισμένες λειτουργίες,
 * όπως η ανάκτηση, σύνδεση και χρήση μιας υπάρχουσας <code>rmiregistry</code> ή η εκκίνησή της
 * για τη λειτουργία του <code>server</code>.
 * <p>
 * Ο <code>server</code> χρησιμοποιεί κι ένα logger μέσω του οποίου καταγράφει τα συνβάντα
 * λειτουργίας του σε ένα αρχείο κειμένου. Η χρήση ενός έτοιμου API από την java
 * για αυτή τη δουλειά, δίνει τη δυνατότητα επέκτασης αυτής της λειτουργικότητας
 * σε πιο εξεζητημένες καταγραφές πληροφορίας όχι μόνο για αποσφαλμάτωση αλλά και
 * για συλλογή στατιστικών λειτουργίας του <code>service</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see dbms.ifaces.Dbms
 */
public class DedaDBMS implements Dbms {

	/**
	 * Το <code>port</code> στο οποίο ακούει τo dbms (δλδ αυτός ο <code>server</code>) τις εντολές του κεντρικού <code>server</code>
	 */
	public static final int port = 4541;

	/**
	 * Το <code>port</code> στο οποίο ανεβαίνει το <code>service</code> της βάσης σε αυτόν τον <code>server</code> (πρβλ παρακάτω)
	 * Προεπιλεγμένη τιμή = 1099
	 */
	public static int ports = 1099;

	/**
	 * Η διεύθυνση αυτού του <code>server</code>. Εκτός από την τοπική διεύθυνση localhost μπορεί να είναι
	 * και κάποια άλλη διεύθυνση όπου υπάρχει μια <code>rmiregistry</code>.
	 */
	public static final String server = "127.0.0.1";

	/**
	 * Το όνομα αυτού του <code>server</code> που αποτελεί λογικό τμήμα της url στην οποία και περιμένει
	 * συνδέσεις.
	 */
	public static final String servername = "DedaDBMS";

	/**
	 * Το όνομα του <code>service</code> που υλοποιεί και διαχειρίζεται αυτός ο <code>server</code>. Αποτελεί λογικό
	 * τμήμα της url στην οποία περιμένει συνδέσεις.
	 */
	public static final String service = "DEDAUserDB";

	/**
	 * Ο logger της java μέσω του οποίου γίνονται οι καταγραφές συμβάντων.
	 */
	private static final Logger logger = Logger.getLogger(DedaDBMS.class.getName());

	/**
	 * Η διαδρομή (σχετική) του φακέλου στον οποίο αποθηκεύονται τα logs.
	 */
	private static final String logpath = "log/";

	/**
	 * Η ονομασία του αρχείου καταγραφής γεγονότων.
	 */
	private static final String logfile = "dbms_history";

	/**
	 * Το απομακρυσμένο αντικείμενο που μέσω rmi καλούνται οι μέθοδοί του. Αυτό είναι το
	 * <code>service</code> που διαχειρίζεται αυτός ο <code>server</code>.
	 */
	private static SysDB dbms;

	/**
	 * Ιδιότητα που πληροφορεί για το status του <code>service</code>. Όταν παίρνει τιμή <code>stopped</code>,
	 * το <code>service</code> έχει σταματήσει και πρέπει να εκκινηθεί για να υπάρχει πρόσβαση
	 * στη βάση δεδομένων. Όταν παίρνει τιμή <code>started</code>.
	 */
	private String status;

	/**
	 * Ο κατασκευαστής ενεργοποιεί το <code>server</code>, όχι όμως και το <code>service</code> το οποίο δηλώνεται
	 * ως σταματημένο (stopped).
	 * 
	 * @throws RemoteException
	 */
	public DedaDBMS() throws RemoteException { super(); this.status = "stopped"; }

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#getPort()
	 */
	public int getPort() throws RemoteException { return ports; }

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#getStatus()
	 */
	public String getStatus() throws RemoteException { return this.status; }

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
			} else {
				e.printStackTrace();
				logger.severe(e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#startService()
	 */
	public void startService() throws RemoteException {
		try {
			dbms = new SysDB(); // Create an instance of our user server ...
			if (!dbms.exists()) { dbms.init(); }
			String urlString = "//"+server+":"+ports+"/"+service;
			Naming.rebind (urlString, dbms); // ... and bind it with the RMI Registry
			this.status = "started";
			System.out.println (CurrentDate.now() + " Service bound....");
			logger.info("Service bound....");
		} catch (RemoteException e) { e.printStackTrace(); logger.severe(e.getMessage());
		} catch (MalformedURLException e) { e.printStackTrace(); logger.severe(e.getMessage()); }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#stopService()
	 */
	public void stopService() throws RemoteException {
		Registry registry = LocateRegistry.getRegistry();
		try {
			registry.unbind(service);
			this.status = "stopped";
			UnicastRemoteObject.unexportObject(dbms, false);
			dbms = null;
			System.out.println (CurrentDate.now() + " Service stopped....");
			logger.info("Service stopped....");
		} catch (NotBoundException e) {
			logger.fine("Could not unregister service.");
			logger.severe(e.getMessage());
			throw new RemoteException("Could not unregister service, stopping anyway", e);
		}
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#restartService()
	 */
	public void restartService() throws RemoteException {
		logger.info("Restarting service...");
		System.out.println(CurrentDate.now() + " Stopping service...");
		this.stopService();
		System.out.println(CurrentDate.now() + " Starting service...");
		this.startService();
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.Dbms#exit()
	 */
	public void exit() throws RemoteException {
		System.out.println(CurrentDate.now() + " Quiting...");
		logger.info("Quiting...");
		logger.info("Stopping service...");
		this.stopService();

		logger.info("Stopping server...");
		Registry registry = LocateRegistry.getRegistry();
		try {
			registry.unbind(service);
			UnicastRemoteObject.unexportObject(dbms, false);
			dbms = null;
			System.out.println (CurrentDate.now() + " Server stopped....");
			logger.info("Server stopped....");
		} catch (NotBoundException e) {
			logger.fine("Could not unregister server.");
			new Thread() {
				@Override
				public void run() {
					System.out.print("Shutting down...");
					try { sleep(2000); }
					catch (InterruptedException e) {/*I don't care*/}
					System.out.println("done");
					System.exit(0);
				}
			}.start();
			throw new RemoteException("Could not unregister server, stopping anyway", e);
		}

		new Thread() {
			@Override
			public void run() {
				System.out.print("Shutting down...");
				try { sleep(2000); }
				catch (InterruptedException e) {/*I don't care*/}
				System.out.println("done");
				System.exit(0);
			}
		}.start();
	}

	/**
	 * Η κύρια μέθοδος του <code>server</code> που διαχειρίζεται τη βάση δεδομένων του συστήματος.
	 * Ύστερα από τις απαραίτητες αρχικοποιήσεις εκκινεί και το <code>service</code>.
	 * <p>
	 * Αρχικά ενεργοποιεί ένα διαχειριστή ασφάλειας, μέσω του οποίου ορίζουμε κανόνες
	 * ασφάλειας και δικαιωμάτων που αφορούν το πρόγραμμα, έξω από το πρόγραμμα.
	 * Οι ορισμοί ασφάλειας γίνονται σε ένα αρχείο κειμένου τύπου <code>java.policy</code>.
	 * Στη συνέχεια, αρχικοποιεί τις παραμέτρους καταγραφής γεγονότων. Έπειτα, ελέγχει
	 * αν υπάρχουν ορίσματα και τα εισάγει στο πρόγραμμα, εκκινεί την <code>rmiregistry</code> και
	 * τελικά εκκινεί και το <code>service</code> για τη βάση δεδομένων.
	 * 
	 * @param args το <code>port</code> στο οποίο θέλουμε να ακούει το <code>service</code>
	 * 					για τη βάση δεδομένων, αν δεν οριστεί, χρησιμοποιείται το προεπιλεγμένο {@link dbms.DedaDBMS#ports}
	 */
	public static void main(String[] args) {

		// Assign a security manager, in the event that dynamic
		// classes are loaded
		if (System.getSecurityManager() == null)
			System.setSecurityManager (new RMISecurityManager());

		File fs = new File(logpath);
		if (!fs.exists()) { fs.mkdir(); }
		fs = new File(logpath + logfile + ".log");
		if (fs.exists()) { fs.renameTo(new File(logpath + logfile + "_" +
											CurrentDate.timestamp() + ".log")); }

		try {
			FileHandler fh = new FileHandler(logpath + logfile + ".log");
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL); // Ορίζουμε την κατηγορία μηνυμάτων που καταγράφονται
		} catch (SecurityException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }

		// acquiring port for the RMI registry
		if(args.length > 0){
			ports = Integer.parseInt(args[0]);
			System.out.println(CurrentDate.now() + " Registry port acquired");
			logger.info("Service " + service + " will be up on port " + ports);
		}

		startRegistry(port);
		logger.info("RMI registry started on port " + port);
		try {
			DedaDBMS srv = new DedaDBMS();
			try {
				String urlString = "//"+server+":"+port+"/"+servername;
				UnicastRemoteObject.exportObject(srv);
				Naming.rebind(urlString, srv);
				System.out.println (CurrentDate.now() + " Server bound....");
				logger.info("Server bound....");
			} catch (RemoteException e) { e.printStackTrace(); logger.severe(e.getMessage());
			} catch (MalformedURLException e) { e.printStackTrace(); logger.severe(e.getMessage()); }

			startRegistry(ports);
			System.out.println (CurrentDate.now() + " Starting service....");
			logger.info("Starting service....");
			try { srv.startService(); }
			catch (RemoteException e) { e.printStackTrace(); logger.severe(e.getMessage()); }
		} catch (RemoteException e1) { e1.printStackTrace(); logger.severe(e1.getMessage()); }
	}
}
