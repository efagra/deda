/**
 * BaseActivator.java
 */
package server.sys;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.deps.Globals;
import server.ifaces.BaseAdmin;

/**
 * Υλοποιεί τη διασύνδεση με την υπηρεσία διαχείρισης των <code>repository</code>.
 * Η κλάση αυτή αποτελεί μια κλάση <code>container</code> για αντικείμενα τύπου
 * <code>BaseRepository</code>. Διαθέτει μεθόδους προσθαφαίρεσης και χρησιμοποιείται
 * για την απομακρυσμένη ενεργοποίηση και απενεργοποίηση των αντικειμένων τύπου
 * <code>BaseRepository</code>.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.BaseAdmin
 * @see server.sys.BaseRepository
 */
@SuppressWarnings("serial")
public class BaseActivator extends UnicastRemoteObject implements BaseAdmin {

	/**
	 * Η διεύθυνση του <code>server</code> που περιέχει την <code>rmiregistry</code>.
	 */
	public static final String server = "127.0.0.1";

	/**
	 * Η πόρτα στην οποία ακούει η <code>rmiregistry</code> για συνδέσεις.
	 */
	public static int port = 1099;

	/**
	 * Ο κατασκευαστής αρχικοποιεί το αντικείμενο
	 * με τις ιδιότητες του <code>UnicastRemoteObject</code>.
	 */
	public BaseActivator() throws RemoteException { super(); }

	/* (non-Javadoc)
	 * @see server.ifaces.BaseAdmin#newBase(java.lang.String)
	 */
	public void newBase(String name) throws RemoteException {
		BaseRepository brep = new BaseRepository(name);
		String url = "//" + server + ":" + port + "/projects/" + name + Globals.PATH_2_BASE;
		try { Naming.bind (url, brep); }
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (AlreadyBoundException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseAdmin#deleteBase(java.lang.String)
	 */
	public void deleteBase(String name) throws RemoteException {
		BaseRepository brep = new BaseRepository(name);
		String url = "//" + server + ":" + port + "/projects/" + name + Globals.PATH_2_BASE;
		try { Naming.unbind (url); UnicastRemoteObject.unexportObject(brep, false); }
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (NotBoundException e) { e.printStackTrace(); }
	}

}
