/**
 * @file Globals.java
 */
package server.deps;

/**
 * Κλάση παγκόσμιων (καθολικών) μεταβλητών για τον <code>server</code>.
 * Περιέχει μεταβλητές - σταθερές για όλο το πρόγραμμα. Προς το παρόν
 * όλες οι μεταβλητές είναι δηλωμένες <code>static</code> που σημαίνει
 * ότι πρόκειται για σταθερές κλάσης και <code>final</code> που σημαίνει
 * πως δεν μπορούν να τροποποιηθούν από το πρόγραμμα. Κατασκευαστής δεν
 * υπάρχει καθώς όλες οι σταθερές έχουν λάβει αρχικές τιμές και για αυτό
 * δεν χρειάζεται σε αυτή τη φάση ανάπτυξης και έκδοσης του προγράμματος.
 * <p>
 * Η αρχιτεκτονική του συστήματος με το συγκεκριμένο αρχείο, δίνει τη
 * δυνατότητα σε επιπλέον ανάπτυξη χωρίς τις δυσκολίες ενημέρωσης πολλών
 * αρχείων κώδικα ταυτόχρονα. Επιπλέον, καθιστά εύκολη τη μετάβαση του
 * συστήματος από σύστημα linux σε windows που υπάρχουν διαφορές κυρίως
 * στα μονοπάτια. Επίσης, μπορεί να προστεθεί εύκολα δυναμική παραμετροποίηση
 * του συστήματος, η οποία παραμετροποίηση να γίνεται από τον τελικό χρήστη
 * ακόμα και κατά την εκτέλεση του προγράμματος.
 * 
 * @author Team13
 * @version 1.0
 */
public class Globals {
	
	/**
	 * Uninstantiable - no instances necessary
	 */  
	private Globals() {}

	/**
	 * Το όνομα του <code>driver</code> της <code>sqlite3</code>
	 */
	public static final String DB_CLASS = "org.sqlite.JDBC";

	/**
	 * Το πρωτόκολλο επικοινωνίας με τη βάση
	 */
	public static final String DB_PROTOCOL = "jdbc:sqlite:";

	/**
	 * Η σχετική διαδρομή προς το φάκελο με τα <code>project</code>
	 */
	public static final String PATH_2_PROJECTS = "data/";

	/**
	 * Η σχετική διαδρομή προς το φάκελο με τα log
	 */
	public static final String PATH_2_LOG = "log/";

	/**
	 * Η σχετική διαδρομή από το φάκελο του <code>project</code> στη βάση του <code>trac</code>
	 */
	public static final String TRAC_BASE_URL = "/trac/db/trac.db";

	/**
	 * Το εκτελέσιμο αρχείο του BugTracking υποπρογράμματος (εδώ το <code>trac</code>)
	 */
	public static final String TRAC_EXEC = "trac-admin";


	/* ----------------- Ορίσματα για το trac-admin ---------------- */

	/**
	 * Σε κάθε project, σε αυτό το φάκελο μπαίνουν όλα τα αρχεία του <code>trac</code>
	 */
	public static final String TRAC_ROOT = "/trac";

	/**
	 * Η βάση του <code>trac</code>
	 */
	public static final String TRAC_DB = "sqlite:db/trac.db";

	/**
	 * Τύπος αποθετηρίου
	 */
	public static final String REPOSTYPE = "svn";

	/**
	 * Διαδρομή για τα <code>templates</code> του <code>trac</code>
	 */
	public static final String TEMPLATEPATH = "/usr/share/trac/templates/";

	
	/**
	 * Το εκτελέσιμο του DCVS (εδώ το <code>mercurial</code>)
	 */
	public static final String MERCURIAL_EXEC = "hg";


	/* ----------------- Ορίσματα για το mercurial ----------------- */

	/**
	 * Η σχετική διαδρομή της <code>base</code> από το φάκελο του <code>project</code>
	 */
	public static final String PATH_2_BASE = "/base/";

	/**
	 * Αρχικοποίηση βασικής αποθήκης
	 */
	public static final String INIT = "init";

	/**
	 * Εμφάνιση ιστορικού από <code>commit</code>
	 */
	public static final String HISTORY = " log -R ";

	/**
	 * Αποθετήριο (ακολουθεί διαδρομή)
	 */
	public static final String REPOSITORY = "-R";

	/**
	 * Θέλει το <code>hg backout</code> που είναι διαφορετικό από το <code>git revert</code>.
	 * Ισχύει <code>git revert REV == hg backout (REV+1)</code>.
	 */
	public static final String BACKOUT = "backout";

	/**
	 * Έκδοση (ακολουθεί αριθμός)
	 */
	public static final String REVISION = "-r";

	/**
	 * Μήνυμα (ακολουθεί σχόλιο)
	 */
	public static final String MESSAGE = "-m";

	/**
	 * Το ανωτέρω σχόλιο
	 */
	public static final String MESSAGE_BODY = "'Revertstorevision'";

	/**
	 * Update με διαγραφή των τροποποιημένων αρχείων
	 */
	public static final String UPDATE_CLEAN = "update -C";


	/* --------------- Σταθερές που αφορούν τον server ------------- */

	/**
	 * Το <code>port</code> στο οποίο ανεβαίνουν τα <code>service</code>
	 */
	public static final int PORT = 1099;

	/**
	 * Η διεύθυνση του <code>server</code>
	 */
	public static final String SERVER = "127.0.0.1";

	/**
	 * Η ονομασία του <code>server</code> περιέχει τη βάση δεδομένων.
	 */
	public static final String SERVERNAME = "DedaDBMS";

	/**
	 * Η ονομασία της υπηρεσίας για τη βάση δεδομένων.
	 */
	public static final String SERVERDB = "DEDAUserDB";

	/**
	 * Η ονομασία της υπηρεσίας αυθεντικοποίησης στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String LOGINSERVICE = "Login";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>project</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String PROJECTSERVICE = "ProjectManagement";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>bug</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String BUGSERVICE = "BugManagement";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>repository</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που υλοποιεί την
	 * υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String REPOSERVICE = "RepoManagement";

}
