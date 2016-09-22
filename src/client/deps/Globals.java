/**
 * @file Globals.java
 */
package client.deps;

/**
 * Κλάση παγκόσμιων (καθολικών) μεταβλητών για τον <code>client</code>.
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
	 * Η σχετική διαδρομή προς το φάκελο με τα project
	 */
	public static final String PATH_2_PROJECTS = "data/";

	/**
	 * Η σχετική διαδρομή προς το φάκελο με τα log
	 */
	public static final String PATH_2_LOG = "log/";

	/**
	 * Το εκτελέσιμο του DCVS (εδώ το mercurial)
	 */
	public static final String MERCURIAL_EXEC = "hg";

	/* ----------------- Ορίσματα για το mercurial ----------------- */

	/**
	 * Η σχετική διαδρομή της <code>base</code> από το φάκελο του project
	 */
	public static final String PATH_2_BASE = "/base/";

	/**
	 * Εμφάνιση ιστορικού από commit
	 */
	public static final String HISTORY = " log -R ";

	/**
	 * Αποθετήριο (ακολουθεί διαδρομή)
	 */
	public static final String REPOSITORY = "-R";

	/**
	 * Θέλει το hg backout που είναι διαφορετικό από το git revert.
	 * Ισχύει git revert REV == hg backout (REV+1).
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

	/**
	 * Αντιγραφή ενός αποθετηρίου σε νέο κατάλογο
	 */
	public static final String CLONE = "clone";

	/**
	 * νιαου νιαου
	 */
	public static final String ADD = "add";

	/**
	 * νιαου νιαου
	 */
	public static final String REMOVE = "remove";

	/**
	 * νιαου νιαου
	 */
	public static final String FORCE = "-f";

	/**
	 * Αποστολή από το τοπικό στο βασικό αποθετήριο
	 */
	public static final String PUSH = "push";

	/**
	 * Αποστολή από το βασικό στο τοπικό αποθετήριο
	 */
	public static final String PULL = "pull";

	/**
	 * Ενημέρωση τοπικού αποθετηρίου για τις αλλαγές
	 */
	public static final String COMMIT = "commit";

	/**
	 * Update χωρίς διαγραφή των τροποποιημένων αρχείων
	 */
	public static final String UPDATE = "update";



	/* --------------- Σταθερές που αφορούν τον client ------------- */

	/**
	 * Η σχετική διαδρομή προς στο το φάκελο με τις εικόνες για το γραφικό
	 */
	public static final String IMG = "sys/img/";



	/* --------------- Σταθερές που αφορούν τον server ------------- */

	/**
	 * Το port στο οποίο ανεβαίνουν τα service
	 */
	public static final int PORT = 1099;

	/**
	 * Η διεύθυνση του server
	 */
	public static final String SERVER = "127.0.0.1";

	/**
	 * Η ονομασία της υπηρεσίας αυθεντικοποίησης στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String LOGINSERVICE = "Login";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>project</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String PROJECTSERVICE = "ProjectManagement";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>bug</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String BUGSERVICE = "BugManagement";

	/**
	 * Η ονομασία της υπηρεσίας διαχείρισης <code>repository</code> στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String REPOSERVICE = "RepoManagement";

}
