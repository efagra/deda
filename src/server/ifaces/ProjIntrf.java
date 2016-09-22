/**
 * @file ProjIntrf.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Η διασύνδεση με το απομακρυσμένο αντικείμενο <code>project</code>, το οποίο βρίσκεται
 * στο <code>server</code>. Περιέχει τις υπογραφές των μεθόδων που ο <code>client</code>
 * μπορεί να καλέσει απομακρυσμένα.
 * 
 * @author Team13
 * @version 1.0
 */
public interface ProjIntrf extends Remote {

	/**
	 * Επιστρέφει αναφορά στο αντικείμενο <code>bug</code> του οποίου το όνομα είναι <code>name</code>.
	 * Αρχικά ελέγχει αν το <code>bug</code> είναι ήδη ενεργό και δέχεται απομακρυσμένες συνδέσεις.
	 * Αν ισχύει η προηγούμενη πρόταση, τότε επιστρέφει τη διεύθυνση στην οποία ακούει για
	 * συνδέσεις. Διαφορετικά, το ενεργοποιεί και επιστρέφει τη νέα διεύθυνση στην οποία
	 * το αντικείμενο ανέβηκε και ακούει για συνδέσεις.
	 * 
	 * @param name το όνομα του <code>bug</code>
	 * @return τη διεύθυνση στην οποία ακούει για συνδέσεις
	 * @throws RemoteException
	 */
	public String getBug(String name) throws RemoteException;

	/**
	 * Προσθέτει το <code>bug</code> με όνομα <code>name</code> στη λίστα.
	 * Επίσης φροντίζει και για την αρχικοποίηση της λίστας αν αυτή δεν έχει
	 * αρχικοποιηθεί.
	 * 
	 * @param bug το όνομα του <code>bug</code>
	 * @throws RemoteException
	 */
	public void addBug(String bug) throws RemoteException;

	/**
	 * Επιστρέφει μια λίστα με τα ονόματα των <code>bug</code> που έχουν αναφερθεί
	 * για αυτό το <code>project</code>.
	 * 
	 * @return η λίστα με τα ονόματα των <code>bug</code>
	 * @throws RemoteException
	 */
	public ArrayList<String> getBugNames() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα του <code>project</code>.
	 * 
	 * @return το όνομα του <code>project</code>
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα του υπεύθυνου πλήρωσης για αυτό το έργο.
	 * 
	 * @return το όνομα του <code>manager</code>
	 * @throws RemoteException
	 */
	public String getManager() throws RemoteException;

	/**
	 * Αναθέτει και το χρήστη <code>user</code> στο project. Πρώτα ελέγχει αν υπάρχει
	 * ήδη στην ενεργή λίστα και αν δεν υπάρχει τον προσθέτει. Τέλος, ενημερώνει τη βάση.
	 * 
	 * @throws RemoteException
	 */
	public void addUser(String username) throws RemoteException;

	/**
	 * Επιστρέφει μια λίστα με τους χρήστες που έχουν οριστεί
	 * να δουλέψουν στο συγκεκριμένο <code>project</code>.
	 * 
	 * @return τη λίστα με τα ονόματα των χρηστών
	 * @throws RemoteException
	 */
	public ArrayList<String> getUsers() throws RemoteException;

	/**
	 * Αφαιρεί το χρήστη <code>user</code> από το <code>project</code>.
	 * Πρώτα ελέγχει αν υπάρχει στην ενεργή λίστα και αν υπάρχει τον αφαιρεί.
	 * Τέλος, ενημερώνει και τη βάση.
	 * 
	 * @throws RemoteException
	 */
	public void removeUser(String user) throws RemoteException;

	/**
	 * Ορίζει το χρήστη με όνομα <code>name</code> ως <code>manager</code> στο συγκεκριμένο
	 * <code>project</code>. Πρώτα ορίζει την ιδιότητα στο αντικείμενο και στη συνέχεια
	 * ενημέρωνει και τη βάση.
	 * 
	 * @throws RemoteException
	 */
	public void setManager(String name) throws RemoteException;

	/**
	 * Επιστρέφει το δικαίωμα του ορισμένου χρήστη σε αυτό το <code>project</code>.
	 * Η τιμή που επιστρέφεται είναι η αυτούσια αποθηκευμένη στη βάση δεδομένων.
	 * 
	 * @param name το όνομα του χρήστη
	 * @return 1 αν μπορεί να γράψει,
	 * 			0 αν δεν μπορεί να γράψει στη <code>base</code>
	 * @throws RemoteException
	 * @see dbms.ifaces.UserDB#getWrite
	 */
	public int getWrite(String name) throws RemoteException;

	/**
	 * Ορίζει το δικαίωμα του ορισμένου χρήστη σε αυτό το <code>project</code>.
	 * Η τιμή που ορίζεται για το δικαίωμα είναι αποθηκεύεται αυτούσια στη βάση δεδομένων
	 * χωρίς να ελέγχεται.
	 * 
	 * @param name Το όνομα του χρήστη
	 * @param write Το δικαίωμα εγγραφής στη base. 1=μπορεί, 0=δεν μπορεί
	 * @throws RemoteException
	 * @see dbms.ifaces.UserDB#setWrite
	 */
	public void setWrite(String name, int write) throws RemoteException;

}
