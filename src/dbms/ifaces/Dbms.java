/**
 * @file Dbms.java
 */
package dbms.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με τo <code>server</code> όπου είναι εγκατεστημένη η βάση δεδομένων του
 * συστήματος. Η διεπαφή αυτή ορίζει τις υπογραφές των μεθόδων, οι οποίες διαχειρίζονται την
 * επικοινωνία με το <code>server</code>. Η ίδια η βάση αντιμετωπίζεται ως ένα <code>service</code> και
 * η διαχείριση του <code>service</code> γίνεται μέσω αυτής της διασύνδεσης. Ορίζει μεθόδους
 * εκκίνησης, τερματισμού και επανεκκίνησης του <code>service</code> απομακρυσμένα. Επίσης, ορίζει
 * μεθόδους λήψης στατιστικών και απομακρυσμένου τερματισμού του <code>server</code>. 
 * <p>
 * Ο <code>server</code> μπορεί να προσπελαστεί μέσω μιας <code>rmiregistry</code> και είναι απομακρυσμένος
 * από το ίδιο το σύστημα ως προς την επικοινωνία, υλοποιώντας την αρχιτεκτονική 3-tier.
 * 
 * @author Team13
 * @version 1.0
 */
public interface Dbms extends Remote {

	/**
	 * Εκκινεί το <code>service</code> για τη βάση και το συνδέει με την <code>rmiregistry</code>.
	 * Η <code>rmiregistry</code> πρέπει να είναι up n running.
	 * 
	 * @throws RemoteException
	 */
	public void startService() throws RemoteException;

	/**
	 * Αναστέλλει τη λειτουργία του <code>server</code> για τη βάση. Η βάση δεδομένων, μετά την κλήση
	 * αυτής της μεθόδου, δεν απαντά σε κανένα ερώτημα και το αντικείμενο, μέσω του οποίου
	 * υλοποιείται το <code>service</code>, καταστρέφεται.
	 * 
	 * @throws RemoteException
	 */
	public void stopService() throws RemoteException;

	/**
	 * Επανεκκινεί τη λειτουργία του <code>service</code> για τη βάση δεδομένων.
	 * Πρώτα κάνει τερματισμό της υπηρεσίας και στη συνέχεια εκκίνηση.
	 * 
	 * @throws RemoteException
	 */
	public void restartService() throws RemoteException;

	/**
	 * Τερματίζει τη λειτουργία του <code>server</code> απομακρυσμένα. Πρώτα τερματίζει το <code>service</code>,
	 * στη συνέχεια σταματάει την αποδοχή των εισερχόμενων κλήσεων στις μεθόδους του
	 * <code>server</code> και τέλος τερματίζει την εφαρμογή.
	 * 
	 * @throws RemoteException
	 */
	public void exit() throws RemoteException;

	/**
	 * Επιστρέφει το port στο οποίο ανεβαίνει το <code>service</code>. Το port αυτό ενδεχομένως να
	 * ορίζεται και να παρακάμπτει το αρχικά ορισμένο. Ο κεντρικός <code>server</code> πρέπει να
	 * καλεί αυτή τη μέθοδο για να μαθαίνει σε πιο port ακούει το <code>service</code> της βάσης δεδομένων.
	 * 
	 * @return το port στο οποίο ακούει η βάση δεδομένων του συστήματος
	 * @throws RemoteException
	 */
	public int getPort() throws RemoteException;

	/**
	 * Επιστρέφει το status του <code>service</code>. Ο κεντρικός <code>server</code> πρέπει να καλεί αυτή τη μέθοδο
	 * για να μαθαίνει αν η βάση δεδομένων είναι έτοιμη για να δεχθεί ερωτήματα. Σε περίπτωση
	 * που δεν είναι, πρέπει να καλέσει τη μέθοδο εκκίνησης του <code>service</code> και μετά να ξαναδοκιμάσει
	 * να συνδεθεί.
	 * 
	 * @return την κατάσταση του <code>service</code>.
	 * 			<code>stopped</code> αν είναι σταματημένο
	 * 			<code>started</code> αν έχει εκκινηθεί και είναι έτοιμο
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;

}
