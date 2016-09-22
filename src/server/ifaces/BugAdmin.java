/**
 * @file BugAdmin.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με την υπηρεσία διαχείρισης <code>bug</code>. Περιέχει μεθόδους
 * κατασκευής και καταστροφής αντικειμένων τύπου <code>bug</code>. Οι μέθοδοι αυτές
 * φροντίζουν και για τις κατάλληλες αρχικοποιήσεις ώστε κάθε νέο αντικείμενο
 * να είναι έτοιμο να δεχθεί απομακρυσμένες κλήσεις σε ξεχωριστή κατάλληλα
 * μορφοποιημένη διεύθυνση σε τοπική <code>rmiregistry</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.BugIntrf
 */
public interface BugAdmin extends Remote {

	/**
	 * Δημιουργεί ένα νέο αντικείμενο τύπου <code>bug</code> και το προσθέτει στην
	 * <code>rmiregistry</code> σε σχετική διεύθυνση με το <code>project</code> στο
	 * οποίο αναφέρεται.
	 * 
	 * @param n το όνομα του <code>bug</code> που θα δημιουργηθεί
	 * @param p το όνομα του <code>project</code> στο οποίο αναφέρεται.
	 * @throws RemoteException
	 */
	public void newBug(String n, String p) throws RemoteException;

	/**
	 * Διαγράφει το <code>bug</code> με όνομα <code>name</code> από την <code>rmiregistry</code>.
	 * 
	 * @param n το όνομα του <code>bug</code> προς διαγραφή
	 * @param p το όνομα του <code>project</code> στο οποίο αναφέρεται
	 * @throws RemoteException
	 */
	public void deleteBug(String n, String p) throws RemoteException;

}
