/**
 * @file UserDB.java
 */
package dbms.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import server.ifaces.ProjIntrf;
import server.ifaces.UserAccount;

/**
 * Η διασύνδεση με τη βάση δεδομένων του συστήματος. Η διεπαφή αυτή
 * ορίζει τις υπογραφές των μεθόδων, οι οποίες διαχειρίζονται την
 * επικοινωνία με τη βάση δεδομένων του συστήματος.
 * <p>
 * Η βάση αυτή μπορεί να προσπελαστεί μέσω μιας rmiregistry
 * και είναι απομακρυσμένη από το ίδιο το σύστημα ως προς την επικοινωνία,
 * υλοποιώντας την αρχιτεκτονική 3-tier.
 * 
 * @author Team13
 * @version 1.0
 */
public interface UserDB extends Remote {

	/**
	 * Επιστρέφει το όνομα του project manager. Κάνει αναζήτηση στη βάση
	 * το όνομα του manager για το project που έχουμε ορίσει. Σε περίπτωση
	 * που δεν έχει οριστεί manager για το συγκεκριμένο project επιστρέφει <code>null</code>.
	 * 
	 * @param name το όνομα του project
	 * @return το όνομα του manager που έχει οριστεί στο project,
	 * 			<code>null</code> αν δεν έχει οριστεί manager στο project,
	 * 			<code>null</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public String getManager(String name) throws RemoteException;

	/**
	 * Επιστρέφει μια <code>ArrayList</code> με τα ονόματα των χρηστών που έχουν ανατεθεί
	 * στο project. Αν το project δεν έχει ανατεθεί πουθενά επιστρέφει <code>null</code>.
	 * 
	 * @param name το όνομα του project
	 * @return μια νέα <code>ArrayList</code> με τα ονόματα των χρηστών για το ορισμένο project,
	 * 			<code>null</code> αν δεν έχει οριστεί κανένας χρήστης στο project,
	 * 			<code>null</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public ArrayList<String> getUsers(String name) throws RemoteException;

	/**
	 * Προσθέτει το χρήστη με όνομα name στο project με όνομα name2.
	 * Ο έλεγχος για το κλειδί γίνεται στο {@link server.sys.Project}, επομένως εδώ είναι περιττός.
	 * Το αρχικό δικαίωμα είναι μόνο ανάγνωσης και όχι εγγραφής στη {@link server.sys.BaseRepository}.
	 * <p>
	 * Δεν χρειάζεται συγχρονισμό, γιατί μόνο ο manager μπορεί να την καλεί για κάθε project.
	 * Ο manager είναι μοναδικός για κάθε project. Κανείς δεν μπορεί να κάνει login στο
	 * σύστημα όταν είναι ήδη ενεργά συνδεδεμένος. Υπάρχει μόνο μια ακραία περίπτωση κινδύνου
	 * για τη συνέπεια: Ο manager είναι ενεργός, ο admin είναι ενεργός, ο admin ορίζει νέο
	 * manager, ο νέος συνδέεται και την καλούν ταυτόχρονα ο νέος και ο παλιός, ο οποίος δεν
	 * παλιός δεν έχει αποσυνδεθεί από το σύστημα.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param name2 το όνομα του project
	 * @throws RemoteException
	 */
	public void addUser2Proj(String name, String name2) throws RemoteException;

	/**
	 * Αφαιρεί το χρήστη με όνομα name από το project με όνομα name2.
	 * Σε περίπτωση που: είτε ο χρήστης, είτε το project δεν υπάρχουν
	 * δεν παράγεται κάποιο σφάλμα, οπότε δεν είναι αναγκαίος ο έλεγχος.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param name2 το όνομα του project
	 * @throws RemoteException
	 */
	public void delUserfProj(String name, String name2) throws RemoteException;

	/**
	 * Ορίζει το χρήστη με όνομα name ως manager στο project με όνομα name2.
	 * Πρώτα γίνεται έλεγχος για το αν υπάρχει ήδη κάποιος manager στο project,
	 * οπότε και γίνεται απλά update στη βάση.
	 * 
	 * @param name το όνομα του χρήστη που θα γίνει manager
	 * @param name2 το όνομα του project που αποκτά manager
	 * @throws RemoteException
	 */
	public void setMan2Proj(String name, String name2) throws RemoteException;

	/**
	 * Αποθηκεύει το αντικείμενο {@link server.sys.Project} στη βάση. Ο έλεγχος για το αν υπάρχει το project
	 * γίνεται από την κλάση container {@link server.sys.ProjectList}, οπότε εδώ είναι περιττός.
	 * 
	 * @param project το project προς αποθήκευση
	 * @throws RemoteException
	 */
	public void save(ProjIntrf project) throws RemoteException;

	/**
	 * Διαγράφει το αντικείμενο {@link server.sys.Project} από τη βάση.
	 * 
	 * @param project το project προς διαγραφή
	 * @throws RemoteException
	 */
	public void delete(ProjIntrf project) throws RemoteException;

	/**
	 * Αποθηκεύει το αντικείμενο {@link server.sys.User} στη βάση. Επίσης, χρησιμεύει και για
	 * update της αποθηκευμένης πληροφορίας στη βάση, που αφορά το χρήστη.
	 * Πρώτα γίνεται έλεγχος αν το αντικείμενο υπάρχει στη βάση οπότε και γίνεται
	 * ανανέωση της πληροφορίας, διαφορέτικά γίνεται μια απλή εισαγωγή.
	 * <p>
	 * Η μέθοδος είναι <code>synchronized</code> γιατί μπορεί ανά πάσα στιγμή να κληθεί
	 * από πολλούς χρήστες ταυτόχρονα και το σύστημα να κρεμάσει από τις πολλές ταυτόχρονες
	 * ενεργές συνδέσεις που θα ανοίξουν με τη βάση. Κίνδυνος ασυνέπειας δεν υπάρχει, λόγω
	 * μοναδικότητας των χρηστών σε κάθε σύνδεση, οπότε ο ίδιος χρήστης δεν μπορεί να την
	 * καλέσει ταυτόχρονα με τον εαυτό του από κάπου αλλού.
	 * 
	 * @param user ο χρήστης προς αποθήκευση
	 * @throws RemoteException
	 */
	public void save(UserAccount user) throws RemoteException;

	/**
	 * Διαγράφει το αντικείμενο {@link server.sys.User} από τη βάση.
	 * 
	 * @param user ο χρήστης προς διαγραφή
	 * @throws RemoteException
	 */
	public void delete(UserAccount user) throws RemoteException;

	/**
	 * Επιστρέφει μια <code>ArrayList</code> με τα ονόματα των project που έχουν
	 * δηλωθεί στο σύστημα. Αν δεν υπάρχουν project επιστρέφει <code>null</code>.
	 * 
	 * @return μια νέα <code>ArrayList</code> με τα ονόματα όλων των project,
	 * 			<code>null</code> αν δεν υπάρχουν project,
	 * 			<code>null</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public ArrayList<String> getProjects() throws RemoteException;

	/**
	 * Επιστρέφει μια <code>ArrayList</code> με τα ονόματα όλων των χρηστών που έχουν
	 * δηλωθεί στο σύστημα και είναι αποθηκευμένοι στη βάση δεδομένων.
	 * 
	 * @return μια νέα <code>ArrayList</code> με τα ονόματα όλων των χρηστών,
	 * 			<code>null</code> αν δεν υπάρχει χρήστης στο σύστημα
	 * 			<code>null</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public ArrayList<String> getUsers() throws RemoteException;

	/**
	 * Ελέγχει την εγκυρότητα του ζεύγους χρήστη-συνθηματικό. Ένα τέτοιο ζεύγος είναι
	 * έγκυρο, αν υπάρχει χρήστης στη βάση με όνομα <code>name</code> και συνθηματικό <code>password.</code>
	 * 
	 * @param name το όνομα του χρήστη
	 * @param pass το συνθηματικό του χρήστη
	 * @return <code>true</code> αν είναι έγκυρο,
	 * 			<code>false</code> αν δεν είναι έγκυρο,
	 * 			<code>false</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public boolean check(String name, String pass) throws RemoteException;

	/**
	 * Επιστρέφει μια <code>ArrayList</code> με τα ονόματα των project στα οποία συμμετέχει
	 * ο χρήστης με όνομα name.
	 * 
	 * @param name το όνομα του χρήστη
	 * @return μια νέα <code>ArrayList</code> με τα ονόματα των project που έχουν ανατεθεί
	 * 			στο χρήστη με όνομα name,
	 * 			<code>null</code> αν δεν υπάρχουν χρήστες στο project,
	 * 			<code>null</code> σε περίπτωση κάποιας εξαίρεσης
	 * @throws RemoteException
	 */
	public ArrayList<String> getPnames(String name) throws RemoteException;

	/**
	 * Επιστρέφει ένα κωδικό που καθορίζει το δικαίωμα του χρήστη στο project.
	 * Το δικαίωμα του χρήστη αναφέρεται στη δυνατότητα εγγραφής ή μόνο ανάγνωσης στη {@link server.sys.BaseRepository}.
	 * Το δικαίωμα της εγγραφής εμπεριέχει και το δικαίωμα της ανάγνωσης, επομένως δεν είναι
	 * απαραίτητος κάποιος επιπλέον διαχωρισμός ως προς την επιστρεφόμενη τιμή.
	 * <p>
	 * Οι επιστρεφόμενες τιμές που αφορούν την ανάγνωση/εγγραφή δεν είναι αυστηρά
	 * καθορισμένες. Σε περίπτωση που στη βάση δεδομένων έχει εισαχθεί κάποιος άλλος
	 * ακέραιος, αυτός και θα επιστραφεί. Δεν γίνεται έλεγχος για την εγκυρότητα των
	 * τιμών που έχουν εισαχθεί και άρα θα επιστραφούν ως έχουν.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param pname το όνομα του project
	 * @return 1 αν μπορεί να γράψει στη <code>BaseRepository</code>,
	 * 			0 αν δεν μπορεί να γράψει στη <code>BaseRepository</code>,
	 * 			0 αν δεν έχει οριστεί το δικαίωμα, επομένως περιορίζεται μόνο σε ανάγνωση,
	 * 			-1 σε περίπτωση ClassNotFoundException,
	 * 			-2 σε περίπτωση SQLException 
	 * @throws RemoteException
	 */
	public int getWrite(String name, String pname) throws RemoteException;

	/**
	 * Αποθηκεύει το δικαίωμα του χρήστη για το project στη βάση. Το δικαίωμα του χρήστη
	 * αναφέρεται στη δυνατότητα εγγραφής ή μόνο ανάγνωσης στη {@link server.sys.BaseRepository}.
	 * Το δικαίωμα της εγγραφής εμπεριέχει και το δικαίωμα της ανάγνωσης, επομένως δεν είναι
	 * απαραίτητος κάποιος επιπλέον διαχωρισμός ως προς την τιμή που ορίζεται.
	 * <p>
	 * Οι τιμές που εισάγονται και αφορούν την ανάγνωση/εγγραφή δεν είναι αυστηρά
	 * καθορισμένες και δεν ελέγχονται. Εφόσον τα δικαιώματα που διαχωρίζονται είναι μόνο
	 * δύο, αρκεί το 0 για μία κατάσταση και οτιδήποτε άλλο για την άλλη. Εδώ, αναμένεται
	 * το 0 για το δικαίωμα μόνο της ανάγνωσης και το 1 για το δικαίωμα της ανάγνωσης/εγγραφής.
	 * Σε περίπτωση που στη μέθοδο δοθεί κάποιος άλλος ακέραιος, αυτός και θα εισαχθεί.
	 * Ενδεχομένως όμως να δημιουργηθούν προβλήματα κατά την ανάγνωση και αποκωδικοποίηση
	 * του δικαιώματος, γι αυτό καλό είναι να διατηρείται η σύμβαση και να γίνεται ο έλεγχος
	 * πριν δοθεί το όρισμα στη μέθοδο.
	 * <p>
	 * Δεν χρειάζεται συγχρονισμό, γιατί μόνο ο manager μπορεί να την καλεί για κάθε project.
	 * Ο manager είναι μοναδικός για κάθε project. Κανείς δεν μπορεί να κάνει login στο
	 * σύστημα όταν είναι ήδη ενεργά συνδεδεμένος. Υπάρχει μόνο μια ακραία περίπτωση κινδύνου
	 * για τη συνέπεια: Ο manager είναι ενεργός, ο admin είναι ενεργός, ο admin ορίζει νέο
	 * manager, ο νέος συνδέεται και την καλούν ταυτόχρονα ο νέος και ο παλιός, ο οποίος δεν
	 * παλιός δεν έχει αποσυνδεθεί από το σύστημα.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param pname το όνομα του project
	 * @param write το δικαίωμα εγγραφής στη base. 1=μπορεί, 0=δεν μπορεί
	 * @throws RemoteException
	 */
	public void setWrite(String name, String pname, int write) throws RemoteException;

}
