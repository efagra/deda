/**
 * @file UserList.java
 */
package server.sys;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dbms.ifaces.UserDB;

import server.ifaces.UserLogin;

/**
 * Υλοποιεί τη διασύνδεση με την υπηρεσία αυθεντικοποίησης. Ουσιαστικά, η κλάση αυτή
 * αποτελεί μια κλάση <code>container</code> για αντικείμενα τύπου <code>user</code>.
 * Διαθέτει μεθόδους προσθαφαίρεσης και αναζήτησης βάση κριτηρίου.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.UserLogin
 * @see server.sys.User
 */
@SuppressWarnings("serial")
public class UserList extends UnicastRemoteObject implements UserLogin {

	/**
	 * Περιέχει όλους τους χρήστες που έχουν ήδη συνδεθεί στο σύστημα.
	 */
	private transient ArrayList<User> openedSessions;

	/**
	 * Η ενεργή σύνδεση με τη βάση δεδομένων.
	 */
	private UserDB SysDB;

	/**
	 * Η διεύθυνση του <code>server</code> που περιέχει την <code>rmiregistry</code>.
	 */
	public static final String server = "127.0.0.1";

	/**
	 * Η πόρτα στην οποία ακούει η <code>rmiregistry</code> για συνδέσεις.
	 */
	public static int port = 1099;
	
	/**
	 * Ο κατασκευαστής αρχικοποιεί τη λίστα και τη σύνδεση με τη βάση δεδομένων.
	 * 
	 * @param dbms η ενεργή σύνδεση με τη βάση δεδομένων
	 * @throws RemoteException
	 */
	public UserList(UserDB dbms) throws RemoteException {
		openedSessions = new ArrayList<User>();
		this.SysDB = dbms;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.UserLogin#deleteUser(java.lang.String, java.lang.String)
	 */
	public void deleteUser(String name, String pass) throws RemoteException {
		User auser = findUser(name);
		openedSessions.remove(auser);
		String url = "//" + server + ":" + port + "/users/" + name;
		try { Naming.unbind (url); UnicastRemoteObject.unexportObject(auser, false);}
		catch (MalformedURLException e) { e.printStackTrace(); }
		catch (NotBoundException e) { e.printStackTrace(); }
	}

	/**
	 * Εντοπίζει αν ο χρήστης με όνομα <code>name</code> έχει συνδεθεί στο σύστημα.
	 * 
	 * @param name το όνομα του χρήστη
	 * @return την αναφορά στο αντικείμενο που αντιπροσωπεύει το χρήστη
	 * @throws RemoteException
	 */
	public User findUser(String name) throws RemoteException {
		
		for (int i=0; i<openedSessions.size(); i++){
			if (openedSessions.get(i).getName().equals(name))
				return openedSessions.get(i);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.UserLogin#getAuthority(java.lang.String, java.lang.String)
	 */
	public User getAuthority(String name, String pass) throws RemoteException {
		synchronized (this) {
			User a = findUser(name);
			if (a != null) return null;
			else if (SysDB.check(name, pass)) {
				a = new User(SysDB, name, pass);
				String url = "//" + server + ":" + port + "/users/" + name;
				try { Naming.rebind (url, a); }
				catch (MalformedURLException e) { e.printStackTrace(); }
				openedSessions.add(a);
				return a;
			} else return null;
		}
	}

}
