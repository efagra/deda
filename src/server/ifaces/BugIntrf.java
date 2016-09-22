/**
 * @file BugIntrf.java
 */
package server.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Η διασύνδεση με το απομακρυσμένο αντικείμενο <code>bug</code>, το οποίο βρίσκεται
 * στο <code>server</code>. Περιέχει τις υπογραφές των μεθόδων που ο <code>client</code>
 * μπορεί να καλέσει απομακρυσμένα.
 * 
 * @author Team13
 * @version 1.0
 */
public interface BugIntrf extends Remote {

	/**
	 * Οι <code>save</code> και <code>update</code> θα υλοποιηθούν σε μία
	 * (τη <code>save</code>) στην {@link server.dbs.TracDB}.
	 * 
	 * @throws RemoteException
	 */
	public void save() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα του <code>project</code> στο οποίο ανήκει το συγκεκριμένο αντικείμενο.
	 * 
	 * @return το όνομα του <code>project</code>
	 * @throws RemoteException
	 */
	public String getProject() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα του συγγραφέα για αυτό το αντικείμενο
	 * 
	 * @return το όνομα του χρήστη-συγγραφέα
	 * @throws RemoteException
	 */
	public String getAuthor() throws RemoteException;

	/**
	 * Ορίζει το όνομα του συγγραφέα για αυτό το αντικείμενο
	 * 
	 * @param author το όνομα του χρήστη-συγγραφέα
	 * @throws RemoteException
	 */
	public void setAuthor(String author) throws RemoteException;

	/**
	 * Επιστρέφει την προτεραιότητα που έχει οριστεί για αυτό το αντικείμενο.
	 * 
	 * @return την προτεραιότητα επίλυσης του <code>bug</code>
	 * @throws RemoteException
	 */
	public String getPriority() throws RemoteException;

	/**
	 * Ορίζει νέα προτεραιότητα για αυτό το αντικείμενο
	 * 
	 * @param priority η νέα προτεραιότητα επίλυσης του <code>bug</code>
	 * @throws RemoteException
	 */
	public void setPriority(String priority) throws RemoteException;

	/**
	 * Επιστρέφει το <code>status</code> αυτού του <code>bug</code>.
	 * 
	 * @return το <code>status</code> αυτή τη χρονική στιγμή
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;

	/**
	 * Ενημερώνει το <code>status</code> αυτού του <code>bug</code>. Με αυτή τη μέθοδο
	 * μπορούν να υλοποιούνται και οι <code>abandon</code>, <code>close</code> και <code>
	 * reopen</code>. π.χ. <code>abug.setStatus("abandoned")</code> και μετά να κάνω <code>save</code>.
	 * 
	 * @param stat το νέο <code>status</code> για αυτό το <code>bug</code>
	 * @throws RemoteException
	 */
	public void setStatus(String stat) throws RemoteException;

	/**
	 * Επιστρέφει την καταληκτική ημερομηνία που έχει οριστεί.
	 * 
	 * @return την καταληκτική ημερομηνία
	 * @throws RemoteException
	 */
	public String getDeadline() throws RemoteException;

	/**
	 * Ορίζει νέα καταληκτική ημερομηνία για το <code>bug</code>.
	 * 
	 * @param line η νέα καταληκτική ημερομηνία
	 * @throws RemoteException
	 */
	public void setDeadLine(String line) throws RemoteException;

	/**
	 * Επιστρέφει την ημερομηνία δημιουργίας του <code>bug</code> μορφοποιημένη κατά
	 * {@link server.deps.CurrentDate#DATE_FORMAT_NOW}.
	 * 
	 * @return την ημερομηνία δημιουργίας αυτού του αντικειμένου
	 * @throws RemoteException
	 */
	public String getDateOfCreation() throws RemoteException;

	/**
	 * Επιστρέφει το όνομα του <code>bug</code>.
	 * 
	 * @return το όνομα του <code>bug</code>
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;

	/**
	 * Επιστρέφει το σώμα κειμένου που αφορά αυτό το <code>bug</code>.
	 * 
	 * @return τη σώμα κειμένου της αναφοράς που έχει γίνει
	 * @throws RemoteException
	 */
	public String getDescription() throws RemoteException;

	/**
	 * Ορίζει νέο κείμενο αναφοράς για το συγκεκριμένο <code>bug</code>.
	 * 
	 * @param description το νέο κείμενο αναφοράς
	 * @throws RemoteException
	 */
	public void setDescription(String description) throws RemoteException;

}
