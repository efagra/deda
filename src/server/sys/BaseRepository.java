/**
 * @file BaseRepository.java
 */
package server.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.deps.Globals;
import server.ifaces.BaseRep;

/**
 * Υλοποιεί τη διασύνδεση {@link server.ifaces.ProjIntrf} και τη διαχείριση της βασικής
 * αποθήκης λογισμικού. Είναι συγγενική κλάση της {@link client.sys.LocalRepository}.
 * Κρύβει αφαιρετικά όλες τις λεπτομέρειες χρήσης και εκτέλεσης του υποπρογράμματος
 * για το DCVS. Η κλάση αυτή παρέχει πληροφορίες που αφορούν το <code>project</code>,
 * καθώς επίσης και μεθόδους διαχείρισης των εκδόσεων κώδικα για αυτό
 * 
 * @author Team13
 * @version 1.5
 * @see server.ifaces.ProjIntrf
 * @see client.sys.LocalRepository
 */
@SuppressWarnings("serial")
public class BaseRepository extends UnicastRemoteObject implements BaseRep {
	
	/**
	 * Το όνομα του <code>project</code>, για το οποίο διαχειρίζεται κώδικα.
	 */
	private String project;

	/**
	 * Η διαδρομή στο σύστημα αρχείων προς τον κατάλογο της αποθήκης.
	 */
	private String repo_path;

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο για κινήσεις στη βασική αποθήκη.
	 * Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 * 
	 * @param pname το όνομα του <code>project</code> που αποθηκεύεται
	 * @throws RemoteException
	 */
	public BaseRepository(String pname) throws RemoteException {
		this.project = pname;
		this.repo_path = Globals.PATH_2_PROJECTS + this.project + Globals.PATH_2_BASE;
	}

	/**
	 * Ελέγχει αν η αποθήκη υπάρχει στο σύστημα αρχείων, δηλαδή αν είναι νέα ή όχι.
	 * 
	 * @return <code>true</code> αν υπάρχει,
	 * 			<code>false</code> αν δεν υπάρχει
	 */
	public boolean exists() {
		File dir = new File(this.repo_path);
		return dir.exists();
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#init()
	 */
	public void init() throws RemoteException {
		File repodir = new File(this.repo_path);
		if (!repodir.exists()) {
			repodir.mkdir();
			Runtime run = Runtime.getRuntime();
			Process pr;
			try {
				pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.INIT + " " + this.repo_path);
				pr.waitFor();
			} catch (IOException e) { e.printStackTrace();
			} catch (InterruptedException e2) { e2.printStackTrace(); }
		}
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#showHistory()
	 */
	public String showHistory() throws RemoteException {
		String cmd = Globals.MERCURIAL_EXEC + Globals.HISTORY + this.repo_path;
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line,history = "";
			while ( (line=buf.readLine()) != null ) {
				history = history + line + "\n";
			}
			return history;
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }
		return null;
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#revert(int)
	 */
	public void revert(int revision) {

		// 8elei to hg backout
		// pou einai diaforetiko apo to git revert
		// isxiei git revert REV == hg backout (REV+1)

		revision++;
		String[] cmd = {Globals.MERCURIAL_EXEC,Globals.REPOSITORY,this.repo_path,Globals.BACKOUT,Globals.REVISION,Integer.toString(revision),Globals.MESSAGE,Globals.MESSAGE_BODY};
		//String cmd = "hg backout -R " + this.repo_path + " -r " + Integer.toString(revision+1) + " -m " + "\"Reverts to revision " + Integer.toString(revision) + "\"" ;
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor();
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.REPOSITORY + " " + this.repo_path + Globals.UPDATE_CLEAN);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#getProject()
	 */
	public String getProject() { return project; }

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#getUser()
	 */
	public String getUser() { return "BASE"; }

}