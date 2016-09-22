/**
 * @file TracDB.java
 */
package server.dbs;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.deps.CurrentDate;
import server.deps.Globals;
import server.sys.Bug;

/**
 * Η κλάση αυτή αποτελεί κλάση διασύνδεσης με τη βάση δεδομένων του trac.
 * Για κάθε <code>project</code> δημιουργείται ξεχωριστή βάση και επομένως και
 * μια ξεχωριστή σύνδεση μαζί της. Διαθέτει καθολικές μεθόδους αποθήκευσης ολόκληρων
 * αντικειμένων για το σύνολο της πληροφορίας που αυτά μπορεί να κρύβουν στην
 * αρχιτεκτονική τους.
 * 
 * @author Team13
 * @version 1.5
 */
public class TracDB {

	/**
	 * Αναφέρεται στο <code>project</code> του οποίου τη βάση επηρεάζει.
	 */
	private String projectName;

	/**
	 * Ο κατασκευαστής αρχικοποιεί την τιμή που αναφέρεται στο όνομα του <code>project</code>,
	 * έτσι ώστε η κλάση να ξέρει ποια βάση να πειράξει.
	 * 
	 * @param projectName το όνομα του <code>project</code>
	 */
	public TracDB(String projectName) { this.projectName = projectName; }

	/**
	 * Δημιουργεί μια λίστα με τα ονόματα των <code>bug</code> που έχουν αναφερθεί
	 * στο συγκεκριμένο <code>project</code>.
	 * 
	 * @return ArrayList<Bug> η λίστα με τα ονόματα των <code>bug</code>
	 */
	public ArrayList<String> getBugs() {

		try {
			Class.forName(Globals.DB_CLASS);
			Connection conn = DriverManager.getConnection(Globals.DB_PROTOCOL +
					Globals.PATH_2_PROJECTS + this.projectName + Globals.TRAC_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT summary FROM ticket;");
			if (rs.isClosed()) { conn.close(); return null; }
			else {
				ArrayList<String> a = new ArrayList<String>();
				while (rs.next()) { a.add(rs.getString(1)); }
				conn.close();
				return a;
			}
		} catch (ClassNotFoundException e) { e.printStackTrace(); return null;
		} catch (SQLException e) { e.printStackTrace(); return null; }

	}

	/**
	 * Ελέγχει αν ο υποκατάλογος του <code>trac</codeo> υπάρχει στο δίσκο.
	 * Αν υπάρχει υποθέτουμε ότι είναι εντάξει. Ωστόσο ΔΕΝ γίνεται κάποιος
	 * έλεγχος εδώ επ' αυτού.
	 * 
	 * @return <code>true</code> αν υπάρχει, <code>false</code> αν δεν υπάρχει
	 */
	public boolean exists() {
		File dir = new File(Globals.PATH_2_PROJECTS + this.projectName + Globals.TRAC_ROOT);
		return dir.exists();
	}

	/**
	 * Αρχικοποιεί τη βάση του <code>trac</cope>, δημιουργώντας τους πίνακες
	 * και τις κατάλληλες συσχετίσεις. Η αρχικοποίηση γίνεται
	 * καλώντας το διαχειριστικό πρόγραμμα του <code>trac</code>, <code>trac-admin</code>.
	 */
	public void init() {
		try {
			String[] cmd = {Globals.TRAC_EXEC,Globals.PATH_2_PROJECTS+this.projectName+
					Globals.TRAC_ROOT,"initenv",this.projectName,Globals.TRAC_DB,
					Globals.REPOSTYPE," ",Globals.TEMPLATEPATH};
			Runtime run = Runtime.getRuntime();
			Process pr;
			pr = run.exec(cmd);
			pr.waitFor();
		} catch (IOException e) { e.printStackTrace();
		} catch (InterruptedException e) { e.printStackTrace(); }
	}

	/**
	 * Αποθηκεύει το αντικείμενο <code>bug</code> στη βάση του <code>trac</code>.
	 * Αν το <code>bug</code> είναι ήδη αποθηκευμένο, τότε γίνεται ανανέωση της
	 * πληροφορίας που βρίσκεται στη βάση.
	 * 
	 * @param bug το αντικείμενο προς αποθήκευση
	 */
	public void save(Bug bug) {
		try {
			Class.forName(Globals.DB_CLASS);
			int id = 1;
			Connection conn = DriverManager.getConnection(Globals.DB_PROTOCOL +
					Globals.PATH_2_PROJECTS + this.projectName + Globals.TRAC_BASE_URL );
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT id,summary FROM ticket WHERE summary='"+
																		bug.getName()+"';");
			if (rs.isClosed()) {
				rs = stat.executeQuery("SELECT max(id) FROM ticket;");
				id = rs.getInt(1) + 1;
				if (rs.wasNull()) {	id = 1; }
				else { id++; }
				stat.executeUpdate(String.format("INSERT INTO ticket VALUES (%d,NULL,%d," +
						"NULL,NULL,NULL,'%s','%s',NULL,NULL,NULL,'%s','%s',NULL,'%s','%s'," +
						"NULL);",id,CurrentDate.string2epoch(bug.getDateOfCreation()),
						bug.getPriority(),bug.getAuthor(),bug.getDeadline(),
						bug.getStatus(),bug.getName(),bug.getDescription()));
			} else {
				id = rs.getInt(1);
				stat.executeUpdate("UPDATE ticket SET priority='" + bug.getPriority() +
						"', changetime=" + CurrentDate.epochNow() +
						",owner='" + bug.getAuthor() +"',milestone='" +
						bug.getDeadline() + "',status='" + bug.getStatus() + "',summary='" +
						bug.getName() + "',description='" + bug.getDescription() +
						"' WHERE id=" + id + ";");
			}
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e2) { e2.printStackTrace();
		} catch (RemoteException e) { e.printStackTrace(); }
	}

	/**
	 * Βρίσκει και επιστρέφει από τη βάση το <code>bug</code> με όνομα <code>name</code>.
	 * Αν το <code>bug</code> δεν υπάρχει, τότε επιστρέφει την τιμή <code>null</code>.
	 * 
	 * @param name το όνομα του <code>bug</code>
	 * @return το αντικείμενο <code>bug</code> αν υπάρχει,
	 * 			<code>null</code> αν δεν υπάρχει
	 */
	public Bug getBug(String name) {
		try {
			Class.forName(Globals.DB_CLASS);
			Connection conn = DriverManager.getConnection(Globals.DB_PROTOCOL +
					Globals.PATH_2_PROJECTS + this.projectName + Globals.TRAC_BASE_URL );
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM ticket WHERE summary='" +
																				name + "';");
			if (rs.next()) {
				Bug b = new Bug(this.projectName, rs.getString(8), name, rs.getString(13), rs.getString(7), rs.getString(12), rs.getString(16), rs.getString(3));
				conn.close();
				return b;
			}
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
		catch (RemoteException e) { e.printStackTrace(); }
		return null;
	}

}
