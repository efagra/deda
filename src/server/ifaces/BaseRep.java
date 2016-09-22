/**
 * @file BaseRep.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με το απομακρυσμένο αντικείμενο <code>BaseRepository</code>,
 * το οποίο βρίσκεται στο <code>server</code>. Περιέχει τις υπογραφές των μεθόδων
 * που ο <code>client</code> μπορεί να καλέσει απομακρυσμένα.
 * 
 * @author Team13
 * @version 1.0
 */
public interface BaseRep extends Remote {

	/**
	 * Αρχικοποιεί την αποθήκη λογισμικού. Πρώτα ελέγχει τη διαδρομή για την αποθήκη
	 * και αν η διαδρομή δεν υπάρχει, τότε χρησιμοποιεί την εντολή αρχικοποίησης του
	 * DCVS
	 * 
	 * @throws RemoteException
	 */
	public void init() throws RemoteException;

	/**
	 * Επιστρέφει το ιστορικό των commit στην αποθήκη λογισμικού.
	 * 
	 * @return το ιστορικό στη μορφή ενός <code>String</code>
	 * @throws RemoteException
	 */
	public String showHistory() throws RemoteException;

	/**
	 * Επιστρέφει σε προηγούμενη έκδοση το αποθετήριο λογισμικού.
	 * 
	 * @param revision ο αριθμός έκδοσης στον οποίο θέλουμε να επιστρέψουμε
	 * @throws RemoteException
	 */
	public void revert(int revision) throws RemoteException;

	/**
	 * Επιστρέφει το <code>project</code> για το οποίο η αποθήκη κρατάει το
	 * λογισμικό.
	 * 
	 * @return το όνομα του <code>project</code> που παρακολουθεί αλλαγές
	 * @throws RemoteException
	 */
	public String getProject() throws RemoteException;

	/**
	 * Επιστρέφει το χρήστη που ενεργοποίησε το αντικείμενο της αποθήκης λογισμικού.
	 * Αν η αποθήκη είναι βασική αποθήκη λογισμικού, δηλαδή συγκεντρώνει αλλαγές για
	 * ένα σύνολο χρηστών, επιστρέφει <code>BASE</code>.
	 * 
	 * @return το όνομα του χρήστη που δουλεύει στην αποθήκη λογισμικού,
	 * 			<code>Base</code>, αν πρόκειται για βασική αποθήκη
	 * @throws RemoteException
	 */
	public String getUser() throws RemoteException;

}
