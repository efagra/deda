/**
 * @file PrjAdmin.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.sys.Project;

/**
 * Η διασύνδεση με την υπηρεσία διαχείρισης <code>project</code>. Περιέχει μεθόδους
 * κατασκευής και καταστροφής αντικειμένων τύπου <code>project</code>. Οι μέθοδοι αυτές
 * φροντίζουν και για τις κατάλληλες αρχικοποιήσεις ώστε κάθε νέο αντικείμενο
 * να είναι έτοιμο να δεχθεί απομακρυσμένες κλήσεις σε ξεχωριστή κατάλληλα
 * μορφοποιημένη διεύθυνση σε τοπική <code>rmiregistry</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.ProjIntrf
 */
public interface PrjAdmin extends Remote {

	/**
	 * Δημιουργεί ένα νέο αντικείμενο τύπου <code>project</code> και το προσθέτει στην ενεργή
	 * λίστα με τα <code>project</code> του συστήματος. Αρχικά ελέγχει αν το κατ' απαίτηση νέο
	 * <code>project</code> υπάρχει ήδη στη λίστα. Αν υπάρχει επιστρέφει <code>false</code> και τερματίζει.
	 * Αν δεν υπάρχει, προχωράει στη δημιουργία του, ενημερώνει τη βάση δεδομένων
	 * και το αρχικοποιεί στο δίσκο για κάθε υποπρόγραμμα της εφαρμογής.
	 * Στη συνέχεια προσθέτει και την κατάλληλη καταχώρηση για αυτό στην <code>rmiregistry</code>
	 * για να μπορεί να προσπελαστεί απομακρυσμένα.
	 * 
	 * @param name το όνομα του <code>project</code> που θα δημιουργηθεί
	 * @return <code>true</code> αν το <code>project</code> δεν υπήρχε, <code>false</code> αν κάποιο
	 *          <code>project</code> υπήρχε ήδη στο σύστημα με το όνομα <code>name</code>.
	 * @throws RemoteException
	 */
	public boolean newProject(String name) throws RemoteException;

	/**
	 * Διαγράφει το <code>project</code> με όνομα <code>name</code> από τη λίστα. Πρώτα ελέγχει αν υπάρχει ήδη στη
	 * λίστα. Αν το βρει τότε πρώτα το αφαιρεί από τη λίστα. Έπειτα, το αφαιρεί από τη
	 * βάση δεδομένων. Στη συνέχεια διαγράφει τα αρχεία του αντικειμένου από το σύστημα
	 * αρχείων. Τέλος, αφαιρεί και την ενεργή του καταχώρηση από την <code>rmiregistry</code>.
	 * 	
	 * @param name το όνομα του <code>project</code> προς διαγραφή
	 * @throws RemoteException
	 */
	public void deleteProject(String name) throws RemoteException;

	/**
	 * Βρίσκει και επιστρέφει αναφορά στο <code>project</code> με όνομα <code>name</code>.
	 * Αν το <code>project</code> δεν υπάρχει στη λίστα επιστρέφει null.
	 * 
	 * @param name το όνομα του <code>project</code> προς εύρεση
	 * @return αναφορά στο <code>project</code>
	 * @throws RemoteException
	 */
	public Project findProject(String name) throws RemoteException;

}
