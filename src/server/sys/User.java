/**
 * @file User.java
 */
package server.sys;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dbms.ifaces.UserDB;
import server.ifaces.UserAccount;

/**
 * Υλοποιεί τη διασύνδεση {@link server.ifaces.UserAccount}.
 * Η κλάση αυτή παρέχει πληροφορίες που αφορούν τον αυθεντικοποιημένο <code>user</code>,
 * καθώς επίσης και επιπλέον μεθόδους διαχείρισής του αυτόματα από το σύστημα. Αποθηκεύει
 * το όνομα του <code>user</code> το οποίο πρέπει να είναι μοναδικό, το συνθηματικό
 * πρόσβασης, το δικαίωμα εγγραφής στο <code>project</code> και μια ενεργή σύνδεση με τη
 * βάση δεδομένων από την οποία αντλεί και στην οποία στέλνει πληροφορίες.
 * 
 * @author Team13
 * @version 1.5
 * @see server.ifaces.ProjIntrf
 */
@SuppressWarnings("serial")
public class User extends UnicastRemoteObject implements UserAccount, Serializable {

	/**
	 * Το όνομα του χρήστη, το οποίο είναι μοναδικό για κάθε χρήστη.
	 */
	private String name;

	/**
	 * Το συνθηματικό αυθεντικοποίησης του χρήστη.
	 */
	private String password;

	/**
	 * Η ιδιότητα <code>write</code> χρησιμοποιείται μόνο στη δημιουργία της λίστας
	 * με τους <code>user</code> στην <code>projectList</code>. Για το περιεχόμενο
	 * της ιδιότητας και έγκυρες τιμές δες {@link dbms.sys.SysDB#getWrite(String, String)}.
	 */
	private int write;

	/**
	 * Η ενεργή σύνδεση με τη βάση δεδομένων.
	 */
	private UserDB SysDB;

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο και αρχικοποιεί τις ορισμένες ιδιότητες.
	 * Απαραίτητη είναι η ενεργή σύνδεση με τη βάση δεδομένων στην οποία αποθηκεύονται μόνιμα
	 * οι πληροφορίες. Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 */
	public User(UserDB db, String name) throws RemoteException {
		super();
		this.name = name;
		this.SysDB = db;
	}

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο και αρχικοποιεί τις ορισμένες ιδιότητες.
	 * Απαραίτητη είναι η ενεργή σύνδεση με τη βάση δεδομένων στην οποία αποθηκεύονται μόνιμα
	 * οι πληροφορίες. Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 */
	public User(UserDB db, String name,String password) throws RemoteException {
		super();
		this.name = name;
		this.password = password;
		this.SysDB = db;
	}

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο και αρχικοποιεί τις ορισμένες ιδιότητες.
	 * Απαραίτητη είναι η ενεργή σύνδεση με τη βάση δεδομένων στην οποία αποθηκεύονται μόνιμα
	 * οι πληροφορίες. Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 */
	public User(UserDB db, String name,String password, int write) throws RemoteException {
		this.name = name;
		this.password = password;
		this.write = write;
		this.SysDB = db;
	}

	/**
	 * Αποθηκεύει αυτό το <code>user</code> στη βάση δεδομένων.
	 * 
	 * @throws RemoteException
	 */
	public void save() throws RemoteException { SysDB.save(this); }

	/**
	 * Διαγράφει αυτό το <code>user</code> από τη βάση δεδομένων.
	 * 
	 * @throws RemoteException
	 */
	public void remove() throws RemoteException { SysDB.delete(this); }

	/* (non-Javadoc)
	 * @see server.ifaces.UserAccount#getName()
	 */
	public String getName() throws RemoteException { return this.name; }

	/**
	 * Επιστρέφει το δικαίωμα του χρήστη για αυτό το αντικείμενο.
	 * 
	 * @return το δικαίωμα εγγραφής στη <code>BaseRepository</code>
	 */
	public int getWrite() { return write; }

	/**
	 * Ορίζει το δικαίωμα του χρήστη για αυτό το αντικείμενο.
	 * 
	 * @param write το δικαίωμα εγγραφής στη <code>BaseRepository</code>
	 */
	public void setWrite(int write) { this.write = write; }

	/* (non-Javadoc)
	 * @see server.ifaces.UserAccount#getPassword()
	 */
	public String getPassword() throws RemoteException { return password; }

	/* (non-Javadoc)
	 * @see deda.ifaces.UserIface#setPassword(java.lang.String, java.lang.String)
	 */
	public boolean setPassword(String oldpass, String newpass) throws RemoteException {

		if (oldpass.equals(this.password)) {
			this.password = newpass;
			this.save();
			return true;
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see server.ifaces.UserAccount#getPrjnames()
	 */
	public ArrayList<String> getPrjnames() throws RemoteException {
		return this.SysDB.getPnames(this.name);
	}

	/* (non-Javadoc)
	 * @see server.ifaces.UserAccount#getUsers()
	 */
	public ArrayList<String> getUsers() throws RemoteException {
		return this.SysDB.getUsers();
	}

}
