/**
 * @file UserLogin.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με την υπηρεσία αυθεντικοποίησης. Περιέχει μεθόδους
 * κατασκευής και καταστροφής αντικειμένων τύπου <code>project</code>.
 * Οι μέθοδοι αυτές φροντίζουν και για την αυθεντικοποίηση του χρήστη
 * πρωτού προβούν σε καταστροφικές ενέργειες. Επίσης, πραγματοποιούν
 * τις κατάλληλες αρχικοποιήσεις ώστε κάθε νέο αντικείμενο να είναι
 * έτοιμο να δεχθεί απομακρυσμένες κλήσεις σε ξεχωριστή κατάλληλα
 * μορφοποιημένη διεύθυνση σε τοπική <code>rmiregistry</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.UserAccount
 */
public interface UserLogin extends Remote {

	/**
	 * Ελέγχει αν ο χρήστης μπορεί να συνδεθεί στο σύστημα. Πρώτα ελέγχει αν είναι ήδη
	 * συνδεδεμένος, στη συνέχεια ελέγχει το συνθηματικό του στη βάση και τέλος συνδέει
	 * το χρήστη αν πληρούνται όλες οι προυποθέσεις. Για κάθε χρήστη που συνδέεται
	 * δημιουργείται νέα αναφορά στην rmiregisty με το όνομά του.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param pass το συνθηματικό του χρήστη
	 * @return αναφορά στο αντικείμενο,
	 * 			<code>null</code> αν η αυθεντικοποίηση του χρήστη δεν ήταν επιτυχής
	 */
	public UserAccount getAuthority(String name, String pass) throws RemoteException;

	/**
	 * Αφαιρεί το χρήστη με όνομα <code>name</code> από τη λίστα με τους ενεργούς χρήστες.
	 * Το συνθηματικό πρόσβασης <code>pass</code> χρειάζεται για την αυθεντικοποίηση και
	 * της λειτουργίας αποσύνδεσης από το σύστημα.
	 * 
	 * @param name το όνομα του χρήστη
	 * @param pass το συνθηματικό πρόσβασης του χρήστη
	 * @throws RemoteException
	 */
	public void deleteUser(String name, String pass) throws RemoteException;

}
