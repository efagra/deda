/**
 * @file Project.java
 */
package server.sys;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dbms.ifaces.UserDB;

import server.dbs.TracDB;
import server.deps.Globals;
import server.ifaces.ProjIntrf;

/**
 * Υλοποιεί τη διασύνδεση {@link server.ifaces.ProjIntrf}.
 * Η κλάση αυτή παρέχει πληροφορίες που αφορούν το <code>project</code>, καθώς επίσης
 * και επιπλέον μεθόδους διαχείρισής του αυτόματα από το σύστημα. Αποθηκεύει το όνομα
 * του <code>project</code> το οποίο πρέπει να είναι μοναδικό, τον υπεύθυνο πλήρωσης
 * του <code>project</code> (project manager), μια λίστα με τα <code>bug</code> που
 * έχουν αναφερθεί και μια λίστα με τους χρήστες που δουλεύουν στο project.
 * 
 * @author Team13
 * @version 1.5
 * @see server.ifaces.ProjIntrf
 */
@SuppressWarnings("serial")
public class Project extends UnicastRemoteObject implements ProjIntrf {

	/**
	 * Το όνομα του <code>project</code>.
	 */
	private String name;
	/**
	 * To όνομα του υπεύθυνου πλήρωσης του <code>project</code>.
	 */
	private String manager;
	/**
	 * Η λίστα με τα <code>bug</code> που έχουν αναφερθεί.
	 */
	private ArrayList<String> buglist;
	/**
	 * Η λίστα με τους χρήστες που δουλεύουν στο <code>project</code>.
	 */
	private ArrayList<String> userlist;
	/**
	 * Το απομακρυσμένο αντικείμενο της βάσης δεδομένων στην οποία αποθηκεύονται
	 * οι πληροφορίες οι σχετικές με το <code>project</code>
	 */
	private UserDB SysDB;

	/**
	 * Ο κατασκευαστής ενργοποιεί το αντικείμενο και γεμίζει τις πληροφορίες από τη βάση.
	 * Δέχεται μια ενεργή σύνδεση με τη βάση δεδομένων και το όνομα. Από το όνομα με τις
	 * κατάλληλες κλήσεις στη βαση γεμίζουν και οι υπόλοιπες πληροφορίες. Δεν ανεβάζει το
	 * αντικείμενο στην <code>rmiregistry</code>.
	 */
	public Project(UserDB db, String name) throws RemoteException {
		super();
		this.SysDB = db;
		this.name = name;
		this.manager = SysDB.getManager(this.name);
		TracDB tr = new TracDB(name);
		if (tr.exists()) { this.buglist = tr.getBugs(); }
		else { this.buglist = new ArrayList<String>(); }
		this.userlist = SysDB.getUsers(this.name);
	}

	/**
	 * Ελέγχει αν ο κατάλογος του <code>project</code> υπάρχει στο δίσκο. Αν υπάρχει,
	 * υποθέτουμε ότι είναι εντάξει. Ωστόσο ΔΕΝ γίνεται κάποιος έλεγχος εδώ επ' αυτού.
	 * 
	 * @return <code>true</code> αν υπάρχει,
	 * 			<code>false</code> αν δεν υπάρχει.
	 */
	public boolean exists() {
		File dir = new File(Globals.PATH_2_PROJECTS + this.name);
		return dir.exists();
	}

	/**
	 * Αρχικοποιεί το <code>project</code> στο δίσκο αν δεν έχει ήδη αρχικοποιηθεί.
	 * Αρχικά ελέγχει αν υπάρχει ριζικός κατάλογος για το <code>project</code> οπότε
	 * και τον δημιουργεί. Στη συνέχεια, εκτελεί την ίδια διαδικασία για για τους
	 * υποκαταλόγους που απαιτούνται από τα υποσυστήματα που χρησιμοποιούνται.
	 */
	public void init() throws RemoteException {

		File dir = new File(Globals.PATH_2_PROJECTS + this.name);
		if (!this.exists()) { dir.mkdirs(); }

		BaseRepository b = new BaseRepository(this.name);
		if (!b.exists()) { b.init(); }

			/* initialize to trac */
		TracDB tr = new TracDB(this.name);
		if (!tr.exists()) { tr.init(); }

	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#addUser(java.lang.String)
	 */
	public void addUser(String username) throws RemoteException {
		if (this.userlist == null) this.userlist = new ArrayList<String>();
		if (!this.userlist.contains(username)) {
			this.userlist.add(username);
			SysDB.addUser2Proj(username,this.name);
		}
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getUsers()
	 */
	public ArrayList<String> getUsers() throws RemoteException {
		return this.userlist;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#removeUser(java.lang.String)
	 */
	public void removeUser(String user) throws RemoteException {
		if (this.userlist.remove(user)) {
			SysDB.delUserfProj(user,this.name);
		}
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#setManager(java.lang.String)
	 */
	public void setManager(String name) throws RemoteException {
		this.manager = name;
		SysDB.setMan2Proj(this.manager,this.name);
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getBug(java.lang.String)
	 */
	public String getBug(String name) throws RemoteException {
		String url = "//" + Globals.SERVER + ":" + Globals.PORT + "/projects/" + this.name + "/bugs/" + name;
		try {
			Naming.lookup(url);
			return url;
		}
		catch (MalformedURLException e1) { e1.printStackTrace(); }
		catch (NotBoundException e1) {
			Bug a = new Bug(name,this.name);
			try {
				Naming.rebind (url, a);
				return url;
			}
			catch (MalformedURLException e) { e.printStackTrace(); }
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#addBug(java.lang.String)
	 */
	public void addBug(String bug) throws RemoteException {
		if (this.buglist==null) this.buglist = new ArrayList<String>();
		else this.buglist.add(bug);
	}

	/**
	 * Αποθηκεύει αυτό το <code>project</code> στη βάση.
	 */
	public void save() {
		try { SysDB.save(this); }
		catch (RemoteException e) { e.printStackTrace(); }
	}

	/**
	 * Διαγράφει αυτό το <code>project</code> από τη βάση.
	 */
	public void remove() {
		try { SysDB.delete(this); }
		catch (RemoteException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getBugNames()
	 */
	public ArrayList<String> getBugNames() throws RemoteException {
		if (buglist!=null) {
			ArrayList<String> a = new ArrayList<String>(buglist.size());
			for (int i=0;i<buglist.size();i++) { a.add(buglist.get(i)); }
			return a;
		} else return null;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getName()
	 */
	public String getName() throws RemoteException { return this.name; }

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getManager()
	 */
	public String getManager() throws RemoteException { return manager; }

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#getWrite(java.lang.String)
	 */
	public int getWrite(String name) throws RemoteException {
		return this.SysDB.getWrite(name, this.name);
	}

	/* (non-Javadoc)
	 * @see server.ifaces.ProjIntrf#setWrite(java.lang.String, int)
	 */
	public void setWrite(String name, int write) throws RemoteException {
		this.SysDB.setWrite(name, this.name, write);
	}

}