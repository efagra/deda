/**
 * @file BugList.java
 */
package server.sys;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.ifaces.BugAdmin;

/**
 * Υλοποιεί τη διασύνδεση με την υπηρεσία διαχείρισης των <code>bug</code>. Η κλάση αυτή
 * αποτελεί μια κλάση <code>container</code> για αντικείμενα τύπου <code>bug</code>.
 * Διαθέτει μεθόδους κατασκευής και καταστροφής.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.BugAdmin
 * @see server.sys.Bug
 */
@SuppressWarnings("serial")
public class BugList extends UnicastRemoteObject implements BugAdmin {

	/**
	 * Η διεύθυνση του <code>server</code> που περιέχει την <code>rmiregistry</code>.
	 */
	public static final String server = "127.0.0.1";

	/**
	 * Η πόρτα στην οποία ακούει η <code>rmiregistry</code> για συνδέσεις.
	 */
	public static int port = 1099;

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο με τα χαρακτηριστικά της γονικής
	 * κλάσης, έτσι ώστε να μπορεί να ανέβει σε μια <code>rmiregistry</code>.
	 */
	public BugList() throws RemoteException { super(); }

	/* (non-Javadoc)
	 * @see server.ifaces.BugAdmin#newBug(java.lang.String, java.lang.String)
	 */
	public void newBug(String n, String p) throws RemoteException {
		Bug a = new Bug(n,p);
		String url = "//" + server + ":" + port + "/projects/" + p + "/bugs/" + n;
		try { Naming.rebind (url, a); }
		catch (MalformedURLException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BugAdmin#deleteBug(java.lang.String, java.lang.String)
	 */
	public void deleteBug(String n, String p) throws RemoteException {
		Bug a = new Bug(n, p);
		String url = "//" + server + ":" + port + "/projects/" + p + "/bugs/" + n;
		try { Naming.unbind (url); UnicastRemoteObject.unexportObject(a, false);}
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (NotBoundException e) { e.printStackTrace(); }
	}

}
