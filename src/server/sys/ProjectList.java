/**
 * @file ProjectList.java
 */
package server.sys;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import server.deps.DeleteDir;
import server.deps.Globals;
import server.ifaces.PrjAdmin;

import dbms.ifaces.UserDB;

/**
 * Υλοποιεί τη διασύνδεση με την υπηρεσία διαχείρισης των <code>project</code>. Η κλάση αυτή
 * αποτελεί μια κλάση <code>container</code> για αντικείμενα τύπου <code>project</code>. Διαθέτει μεθόδους
 * προσθαφαίρεσης και αναζήτησης βάση κριτηρίου.
 * 
 * @author Team13
 * @version 1.0
 * @see server.ifaces.PrjAdmin
 * @see server.sys.Project
 */
@SuppressWarnings("serial")
public class ProjectList extends UnicastRemoteObject implements PrjAdmin {

	/**
	 * Η λίστα πάνω στην οποία προσκολλάται κάθε νέο αντικείμενο τύπου <code>project</code>
	 */
	private ArrayList<Project> projects;
	/**
	 * Η ενεργή σύνδεση με τη βάση δεδομένων
	 */
	private UserDB SysDB;
	/**
	 * Η διεύθυνση του <code>server</code> που με την <code>rmiregistry</code>
	 */
	private static final String server = "127.0.0.1";
	/**
	 * Η πόρτα στην οποία δέχεται συνδέσεις η <code>rmiregistry</code> στο <code>server</code>
	 */
	private static int port = 1099;

	/**
	 * Ο κατασκευαστής αρχικοποιεί τη λίστα παίρνοντας τα <code>project</code> από τη βάση δεδομένων.
	 * Στη συνέχεια ανεβάζει κάθε <code>project</code> σε ξεχωριστή διεύθυνση στην <code>rmiregistry</code> κατάλληλα
	 * μορφοποιημένη με το όνομα του <code>project</code>. Ύστερα από αυτό, κάθε <code>project</code> μπορεί να
	 * προσπελαστεί απομακρυσμένα από κάποιον client.
	 */
	public ProjectList(UserDB dbs) throws RemoteException {
		super();
		this.SysDB = dbs;
		ArrayList<String> plist = SysDB.getProjects();
		this.projects = new ArrayList<Project>();
		if ( plist == null ) { /* Do nothing! */ }
		else {
			for (int i=0; i<plist.size(); i++) {
				String url = "//" + server + ":" + port + "/projects/" + plist.get(i);
				Project prj = new Project(SysDB, plist.get(i));
				try { Naming.rebind (url, prj); }
				catch (MalformedURLException e) { e.printStackTrace(); }
				this.projects.add(prj);
			}
		}
	}

	/* (non-Javadoc)
	 * @see server.ifaces.PrjAdmin#newProject(java.lang.String)
	 */
	public boolean newProject(String name) throws RemoteException {
		if (this.findProject(name) == null) {
			Project prj = new Project(SysDB, name);
			String url = "//" + server + ":" + port + "/projects/" + name;
			try { Naming.rebind(url, prj); }
			catch (MalformedURLException e) { e.printStackTrace(); }
			this.projects.add(prj);
			prj.init();
			prj.save();
			return true;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.PrjAdmin#deleteProject(java.lang.String)
	 */
	public void deleteProject(String name) throws RemoteException {
		Project a = findProject(name);
		if (a!=null) {
			this.projects.remove(a);
			a.remove();
			DeleteDir.deleteDirectory(new File(Globals.PATH_2_PROJECTS + name));
			String url = "//" + server + ":" + port + "/projects/" + name;
			try { Naming.unbind (url); UnicastRemoteObject.unexportObject(a, true);}
			catch (MalformedURLException e) { e.printStackTrace(); }
			catch (NotBoundException e) { e.printStackTrace(); }
		}
	}

	/* (non-Javadoc)
	 * @see server.ifaces.PrjAdmin#findProject(java.lang.String)
	 */
	public Project findProject(String name) throws RemoteException {
		for(int i=0;i<this.projects.size();i++){
			if(name.equals(this.projects.get(i).getName())) {
				return this.projects.get(i);
			}
		}
		return null;
	}

}
