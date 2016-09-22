/**
 * @file LocalRepository.java
 */
package client.sys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import server.ifaces.BaseRep;

import client.deps.Globals;

/**
 * Υλοποιεί τη διαχείριση της τοπικής αποθήκης λογισμικού.
 * Είναι συγγενική κλάση της {@link server.sys.BaseRepository}. Κρύβει αφαιρετικά
 * όλες τις λεπτομέρειες χρήσης και εκτέλεσης του υποπρογράμματος για το DCVS.
 * Ιεραρχικά σε ένα σύστημα χωρίς αρχιτεκτονική πελάτη-εξυπηρετητή θα μπορούσε να
 * επεκτείνει την <code>BaseRepository</code>, γιατί διαθέτει μεθόδους κοινές στην
 * ονομασία και στη λειτουργικότητα με αυτήν.
 * 
 * @author Team13
 * @version 1.5
 * @see server.sys.BaseRepository
 */
public class LocalRepository implements BaseRep {

	public static final String SLASH = "/";

	/**
	 * Το <code>url</code> του <code>server</code> που περιέχει την βασική αποθήκη.
	 * <p>
	 * Από το manual του mercurial πληροφορούμαστε:<br>
	 * Valid URLs are of the form:<br>
	 * <br>
	 *     local/filesystem/path (or file://local/filesystem/path)<br>
	 *     http://[user@]host[:port]/[path]<br>
	 *     https://[user@]host[:port]/[path]<br>
	 *     ssh://[user@]host[:port]/[path]<br>
	 *     static-http://host[:port]/[path]<br>
	 * <br>
	 * Paths in the local filesystem can either point to Mercurial repositories<br>
	 * or to bundle files (as created by ´hg bundle´ or ´hg incoming --bundle´).<br>
	 * The static-http:// protocol, albeit slow, allows access to a Mercurial<br>
	 * repository where you simply use a web server to publish the .hg directory<br>
	 * as static content.<br>
	 * <br>
	 * Some notes about using SSH with Mercurial:<br>
	 *         - SSH requires an accessible shell account on the destination machine<br>
	 *           and a copy of hg in the remote path or specified with as remotecmd.<br>
	 *         - path is relative to the remote user´s home directory by default.<br>
	 *           Use an extra slash at the start of a path to specify an absolute path:<br>
	 *             ssh://example.com//tmp/repository<br>
	 *         - Mercurial doesn´t use its own compression via SSH; the right thing<br>
	 *           to do is to configure it in your ~/.ssh/config, e.g.:<br>
	 *             Host *.mylocalnetwork.example.com<br>
	 *               Compression no<br>
	 *             Host *<br>
	 *               Compression yes<br>
	 *           Alternatively specify "ssh -C" as your ssh command in your hgrc or<br>
	 *           with the --ssh command line option.<br>
	 */
	private static String sourceServer;

	/**
	 * Το <code>project</code> για το εργάζεται η αποθήκη.
	 */
	private String project;

	/**
	 * Ο χρήστης που ενεργοποιεί και εργλαζεται στην αποθήκη.
	 */
	private String user;

	/**
	 * Η σχετική διαδρομή στο σύστημα αρχείων προς την αποθήκη.
	 */
	private String repo_path;

	/**
	 * Ο κατασκευαστής ενεργοποιεί το αντικείμενο για κινήσεις στην τοπική αποθήκη.
	 * Αρχικοποιεί όλες τις ιδιότητες μόνο με δύο ορίσματα, με τη βάση κανόνων
	 * συσχέτισης των ιδιοτήτων και τον τύπο πληροφορίας που κρατά η καθε μια.
	 * 
	 * @param uname το όνομα του χρήστη της τοπικής αποθήκης
	 * @param pname το όνομα του <code>project</code> που αποθηκεύεται τοπικά
	 */
	public LocalRepository(String uname, String pname) {
		this.user = uname;
		this.project = pname;
		this.repo_path = Globals.PATH_2_PROJECTS  + this.project + SLASH + this.user + SLASH;
		//TODO initialize sourceServer
	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#init()
	 */
	public void init() {

		File repodir = new File(this.repo_path);
		if (!repodir.exists()) {
			Runtime run = Runtime.getRuntime();
			Process pr;
			String cmd = Globals.MERCURIAL_EXEC + " " + Globals.CLONE + " " +
									sourceServer + " " + this.repo_path;
			try {
				// apo to man tou hg douleuei kai to "cp -al REPO REPOCLONE"
				// alla den prepei ekeini tin wra na ginontai allages sto REPO
				pr = run.exec(cmd);
				pr.waitFor();
			} catch (IOException e) { e.printStackTrace();
			} catch (InterruptedException e2) { e2.printStackTrace(); }
		}

	}

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#showHistory()
	 */
	public String showHistory() {

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

		// 8elei to hg backout pou einai diaforetiko apo to git revert
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

	/**
	 * Δημιουργεί και εισάγει ένα νέο αρχείο στην αποθήκη.
	 * Το αρχείο αμέσως μπαίνει σε παρακολούθηση για αλλαγές από DCVS.
	 * 
	 * @param name το όνομα του αρχείου
	 * @param contents τα περιεχόμενα του αρχείου
	 */
	public void createFile(String name, String contents) {

		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(this.repo_path + name));
			out.write(contents);
		    out.close();
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.ADD + " " + Globals.REPOSITORY + " " + this.repo_path + " " + this.repo_path + name);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }

	}

	/**
	 * Διαγράφει το ορισμένο αρχείο από το σύστημα αρχείων.
	 * Το αρχείο αμέσως διαγράφεται και από το DCVS.
	 * 
	 * @param name το όνομα του αρχείου προς διαγραφή
	 */
	public void deleteFile(String name) {

		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.REMOVE + " " + Globals.FORCE
					+ " " + Globals.REPOSITORY + " " + this.repo_path + " " + this.repo_path + name);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }
		File f = new File(this.repo_path + name);
		f.delete();

	}
	
	/**
	 * Αποστέλλει όλες τις τοπικές αλλαγές από την τοπική στη βασική αποθήκη λογισμικού.
	 * Αν έχουν γίνει πολλαπλές αποθηκεύσεις εκδόσεων στην τοπική αποθήκη, αποστέλλεται
	 * η τελευταία έκδοση, η οποία περιέχει το σύνολο των αλλαγών, χωρίς το ιστορικό τους.
	 * <p>
	 * Από το manual του mercurial πληροφορούμαστε:<br>
	 * <br>
	 * push [-f] [-r REV]... [-e CMD] [--remotecmd CMD] [DEST]<br>
	 * <br>
	 *     Push changes from the local repository to the given destination.<br>
	 * <br>
	 *         This is the symmetrical operation for pull. It helps to move<br>
	 *         changes from the current repository to a different one. If the<br>
	 *         destination is local this is identical to a pull in that directory<br>
	 *         from the current one.<br>
	 * <br>
	 *         By default, push will refuse to run if it detects the result would<br>
	 *         increase the number of remote heads. This generally indicates the<br>
	 *         the client has forgotten to pull and merge before pushing.<br>
	 * <br>
	 *         Valid URLs are of the form:<br>
	 * <br>
	 *         local/filesystem/path (or file://local/filesystem/path)<br>
	 *         ssh://[user@]host[:port]/[path]<br>
	 *         http://[user@]host[:port]/[path]<br>
	 *         https://[user@]host[:port]/[path]<br>
	 * <br>
	 *         An optional identifier after # indicates a particular branch, tag,<br>
	 *         or changeset to push. If -r is used, the named changeset and all its<br>
	 *         ancestors will be pushed to the remote repository.<br>
	 * <br>
	 *         Look at the help text for the pull command for important details<br>
	 *         about ssh:// URLs.<br>
	 * <br>
	 *         Pushing to http:// and https:// URLs is only possible, if this<br>
	 *         feature is explicitly enabled on the remote Mercurial server.<br>
	 * <br>
	 *         options:<br>
	 *         -f, --force  force push<br>
	 *         -r, --rev    a specific revision up to which you would like to<br>
	 *                      push<br>
	 *         -e, --ssh    specify ssh command to use<br>
	 *         --remotecmd  specify hg command to run on the remote side<br>
	 */
	public void push() {

		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.REPOSITORY + " " + this.repo_path + " " + Globals.PUSH);
			pr.waitFor();
			// update stin base
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + sourceServer + " " + Globals.UPDATE);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }

	}

	/**
	 * Ενημερώνει την τοπική αποθήκη από τη βασική αποθηκή λογισμικού.
	 * Όλες οι αλλαγές στην τοπική αποθήκη διαγράφονται σε περίπτωση
	 * που έχουμε συγκρούσεις. Ο χρήστης δεν ενημερώνεται για αυτή την
	 * ενέργεια.
	 */
	public void update() {

		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.REPOSITORY + " " + sourceServer + " " + Globals.PULL);
			pr.waitFor();
			pr = run.exec(Globals.MERCURIAL_EXEC + " " + Globals.REPOSITORY + " " + this.repo_path + " " + Globals.UPDATE_CLEAN);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }

	}

	/**
	 * Δημιουργεί μια νέα έκδοση των αλλαγών στην τοπική αποθήκη.
	 * Κρατάει δηλαδή ένα σημείο ελέγχου για επιστροφή σε μια σταθερή έκδοση
	 * κώδικα σε περίπτωση που γίνει κάποια επικίνδυνη αλλαγή από το χρήστη.
	 * Λαμβάνει ένα σχόλιο με το οποίο μαρκάρει το σύνολο των αλλαγών,
	 * ώστε ο χρήστης να θυμάται που βρισκόταν ο κώδικας περίπου σε εκείνο
	 * το σημείο. Καλό είναι αυτό το σχόλιο να είναι συνοπτικό.
	 * 
	 * @param comment το συνοπτικό σχόλιο αλλαγών στην έκδοση κώδικα
	 */
	public void commit(String comment) {

		Runtime run = Runtime.getRuntime();
		String[] cmd = {Globals.MERCURIAL_EXEC,Globals.REPOSITORY,this.repo_path,Globals.COMMIT,Globals.MESSAGE,"'"+ comment +"'"};
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }

	}

	/**
	 * Επιστρέφει τη διαδρομή στο σύστημα αρχείων της αποθήκης.
	 * Η διαδρομή αυτή είναι σχετική, ως προς το ριζικό κατάλογο
	 * εκτέλεσης του προγράμματος. Αναφέρεται στο συγκεκριμένο
	 * <code>project</code> για το συγκεκριμένο χρήστη.
	 * 
	 * @return τη σχετική διαδρομή προς την τοπική αποθήκη του <code>project</code>
	 */
	public String getRepo_path() { return this.repo_path; }

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#getUser()
	 */
	public String getUser() { return this.user; }

	/* (non-Javadoc)
	 * @see server.ifaces.BaseRep#getProject()
	 */
	public String getProject() { return this.project; }

}