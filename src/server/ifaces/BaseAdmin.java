/**
 * @file BaseAdmin.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με την υπηρεσία διαχείρισης <code>repository</code>. Περιέχει μεθόδους
 * κατασκευής και καταστροφής αντικειμένων τύπου <code>BaseRepository</code>. Οι μέθοδοι
 * αυτές φροντίζουν και για τις κατάλληλες αρχικοποιήσεις ώστε κάθε νέο αντικείμενο
 * να είναι έτοιμο να δεχθεί απομακρυσμένες κλήσεις σε ξεχωριστή κατάλληλα
 * μορφοποιημένη διεύθυνση σε τοπική <code>rmiregistry</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.BaseRep
 */
public interface BaseAdmin extends Remote {

	/**
	 * Δημιουργεί ένα νέο αντικείμενο τύπου <code>BaseRepository</code> και το προσθέτει
	 * στην <code>rmiregistry</code> με όνομα το <code>name</code> του <code>project</code>.
	 * 
	 * @param name το όνομα του <code>project</code> για το οποίο θα δημιουργηθεί το βασικό αποθετήριο
	 * @throws RemoteException
	 */
	public void newBase(String name) throws RemoteException;

	/**
	 * Διαγράφει το αποθετήριο του <code>project</code> με όνομα <code>name</code> από την
	 * <code>rmiregistry</code>.
	 * 
	 * @param name το όνομα του <code>project</code> προς διαγραφή
	 * @throws RemoteException
	 */
	public void deleteBase(String name) throws RemoteException;

}
