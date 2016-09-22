/**
 * @file UserAccount.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Η διασύνδεση με το απομακρυσμένο αντικείμενο <code>user</code>, το οποίο βρίσκεται
 * στο <code>server</code>. Περιέχει τις υπογραφές των μεθόδων που ο <code>client</code>
 * μπορεί να καλέσει απομακρυσμένα. Απαραίτητη προϋπόθεση είναι η επιτυχημένη αυθεντικοποίηση
 * του χρήστη, για να χρησιμοποιηθούν οι συγκεκριμένες μέθοδοι.
 * 
 * @author Team13
 * @version 1.0
 */
public interface UserAccount extends Remote {

	/**
	 * Ορίζει νέο συνθηματικό για το χρήστη. Για την επιτυχημένη αλλαγή, πρέπει ο
	 * χρήστης να δηλώσει και το παλιό του συνθηματικό. Στη συνέχεια, γίνεται έλεγχος
	 * αν το παλιό συνθηματικό που δίνει ο χρήστης είναι έγκυρο και αν είναι τότε
	 * πραγματοποιείται η αλλαγή του συνθηματικού και ενημερώνεται και η βάση για
	 * την αλλαγή.
	 * 
	 * @param oldpass το ισχύον συνθηματικό
	 * @param newpass το νέο συνθηματικό
	 * @return <code>true</code> αν η αλλαγή ήταν επιτυχή,
	 *         <code>false</code> αν η αλλαγή ήταν ανεπιτυχή
	 * @throws RemoteException
	 */
	public boolean setPassword(String oldpass, String newpass) throws RemoteException;

	/**
	 * Επιστρέφει το συνθηματικό του χρήστη.
	 * 
	 * @return το συνθηματικό αναγνώρισης του χρήστη από το σύστημα
	 * @throws RemoteException
	 */
	public String getPassword() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα αυτού του χρήστη.
	 * 
	 * @return String - το όνομα του χρήστη
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;

	/**
	 * Επιστρέφει μια λίστα με τα ονόματα των <code>project</code> που έχουν ανατεθεί
	 * στο συγκεκριμένο χρήστη.
	 * 
	 * @return τη λίστα με τα ονόματα των <code>project</code> που έχουν ανατεθεί στο χρήστη
	 * @throws RemoteException
	 */
	public ArrayList<String> getPrjnames() throws RemoteException;

	/**
	 * 
	 * Επιστρέφει μια λίστα με τα ονόματα των <code>user</code> που έχουν δηλωθεί
	 * στο σύστημα.
	 * 
	 * @return τη λίστα με τα ονόματα των χρηστών που έχουν καταχωρηθεί στο σύστημα
	 * @throws RemoteException
	 */
	public ArrayList<String> getUsers() throws RemoteException;
}
