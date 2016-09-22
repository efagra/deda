/**
 * @file SysDB.java
 */
package dbms.sys;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.ifaces.ProjIntrf;
import server.ifaces.UserAccount;

import dbms.ifaces.UserDB;

/**
 * Υλοποιεί τη διασύνδεση με τη βάση δεδομένων. Περιέχει την υλοποίηση των
 * μεθόδων της διεπαφής {@link dbms.ifaces.UserDB}. Επίσης, περιέχει και μεθόδους ελέγχου
 * και αρχικοποίησης του σχήματος σε περίπτωση που έχουμε την πρώτη εκκίνηση.
 * <p>
 * Η βάση που χρησιμοποιείται είναι η sqlite3. Η χρήση στατικών σταθερών
 * επιτρέπει την εύκολη αλλαγή και τη χρήση οποιουδήποτε άλλου driver για
 * διασύνδεση με άλλο dbms.
 * 
 * @author Team13
 * @version 1.5
 * @see dbms.ifaces.UserDB
 */
@SuppressWarnings("serial")
public class SysDB extends UnicastRemoteObject implements UserDB {

	/**
	 * Το όνομα του driver της sqlite3
	 */
	private static final String DB_CLASS = "org.sqlite.JDBC";

	/**
	 * Το πρωτόκολλο επικοινωνίας με τη βάση
	 */
	private static final String DB_PROTOCOL = "jdbc:sqlite:";

	/**
	 * Η σχετική διαδρομή προς τον κατάλογο δεδομένων
	 */
	private static final String SYS_DIR = "sys";

	/**
	 * Η σχετική διαδρομή προς το αρχείο της βάσης
	 */
	private static final String SYS_BASE_URL = "sys/sqlite.db";

	/**
	 * Δημιουργεί το αντικείμενο με τα χαρακτηριστικά της γονικής κλάσης.
	 * 
	 * @throws RemoteException
	 */
	public SysDB() throws RemoteException { super(); }

	/**
	 * Αρχικοποιεί τη βάση δεδομένων. Πρώτα δημιουργεί τον κατάλογο για τα
	 * δεδομένα, αν αυτός δεν υπάρχει. Στη συνέχεια, δημιουργεί το αρχείο της βάσης,
	 * αν αυτό δεν υπάρχει. Έπειτα, δημιουργεί τους πίνακες και τις κατάλληλες
	 * συσχετίσεις, δλδ αρχικοποιεί το σχήμα με κώδικα SQL. Τέλος, εισάγει τον πρώτο
	 * χρήστη που είναι ο εξ ορισμού διαχειριστής στο σύστημα με όνομα root και
	 * συνθηματικό εισόδο toor.
	 */
	public void init() {
		try {
			File fs = new File(SYS_DIR);
			if (!fs.exists()) { fs.mkdir(); }
			fs = new File(SYS_BASE_URL);
			if (!fs.exists()) { fs.createNewFile(); }
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE users (name VARCHAR(30) primary key," +
													"password VARCHAR(30));");
			stat.executeUpdate("CREATE TABLE projects (pname VARCHAR(30) primary key);");
			stat.executeUpdate("CREATE TABLE participates (" +
												"name VARCHAR(30) ," +
												"pname VARCHAR(30) ," +
												"write INTEGER, " +
												"PRIMARY KEY (name, pname), " +
												"FOREIGN KEY (name) REFERENCES users, " +
												"FOREIGN KEY (pname) REFERENCES projects);");
			stat.executeUpdate("CREATE TABLE manages (" +
												"name VARCHAR(30), " +
												"pname VARCHAR(30), " +
												"PRIMARY KEY (pname), " +
												"FOREIGN KEY (name) REFERENCES users, " +
												"FOREIGN KEY (pname) REFERENCES projects);");
			stat.executeUpdate("INSERT INTO users VALUES('root','toor');");
			conn.close();
		} catch (ClassNotFoundException e1) { e1.printStackTrace();
		} catch (SQLException e2) { e2.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Ελέγχει αν η βάση υπάρχει στο δίσκο. Αν υπάρχει υποθέτουμε ότι είναι
	 * καλώς ορισμένη. Ωστόσο ΔΕΝ γίνεται κάποιος έλεγχος εδώ επ' αυτού.
	 * 
	 * @return <code>true</code> αν υπάρχει, <code>false</code> αν δεν υπάρχει
	 */
	public boolean exists() {
		File db = new File(SYS_BASE_URL);
		return db.exists();
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getManager(java.lang.String)
	 */
	public String getManager(String name) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT name FROM manages WHERE pname = '" +
																				name + "';");
			if (rs.isClosed()) { conn.close(); return null; }
			else { String mngr = rs.getString(1); conn.close(); return mngr; }
		} catch (ClassNotFoundException e) { e.printStackTrace(); return null;
		} catch (SQLException e) { e.printStackTrace(); return null; }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getUsers(java.lang.String)
	 */
	public ArrayList<String> getUsers(String name) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT name,write FROM participates " +
												"WHERE pname = '" + name + "';");
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

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#addUser2Proj(java.lang.String, java.lang.String)
	 */
	public void addUser2Proj(String name, String name2) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("INSERT INTO participates VALUES ('" + name + "', '" +
					name2 + "', 0)");
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#delUserfProj(java.lang.String, java.lang.String)
	 */
	public void delUserfProj(String name, String name2) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("DELETE FROM participates WHERE name = '" + name +
														"' AND pname = '" + name2 + "';");
			conn.close();
		} catch (ClassNotFoundException e1) { e1.printStackTrace();
		} catch (SQLException e2) { e2.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#setMan2Proj(java.lang.String, java.lang.String)
	 */
	public void setMan2Proj(String name, String name2) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT name FROM manages WHERE pname = '" +
																			name2 + "';");
			if (rs.isClosed()) {
				stat.executeUpdate("INSERT INTO manages VALUES ('" + name + "','" +
																			name2 + "');");
			} else {
				stat.executeUpdate("UPDATE manages SET name='" + name +
									"' WHERE pname='" + name2 + "';");
			}
			conn.close();
		} catch (ClassNotFoundException e1) { e1.printStackTrace();
		} catch (SQLException e2) { e2.printStackTrace(); }		
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#save(server.ifaces.ProjIntrf)
	 */
	public void save(ProjIntrf project) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("INSERT INTO projects VALUES ('" + project.getName() + "');");
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#delete(server.ifaces.ProjIntrf)
	 */
	public void delete(ProjIntrf project) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("DELETE FROM projects WHERE pname='" +
															project.getName() + "';");
			stat.executeUpdate("DELETE FROM participates WHERE pname='" +
															project.getName() + "';");
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }		
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#save(server.ifaces.UserAccount)
	 */
	public void save(UserAccount auser) throws RemoteException { 
		synchronized (this) {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT name FROM users WHERE name = '" +
																	auser.getName() + "';");
			if (rs.isClosed()) {
				stat.executeUpdate("INSERT INTO users VALUES ('" + auser.getName() + "','" +
																auser.getPassword() + "');");
			} else {
				stat.executeUpdate("UPDATE users SET password='" + auser.getPassword() +
									"' WHERE name='" + auser.getName() + "';");
			}
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#delete(server.ifaces.UserAccount)
	 */
	public void delete(UserAccount auser) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			stat.executeUpdate("DELETE FROM users WHERE name='" + auser.getName() + "';");
			stat.executeUpdate("DELETE FROM participates WHERE name='" +
																auser.getName() + "';");
			stat.executeUpdate("DELETE FROM manages WHERE name='" +
															auser.getName() + "';");
			conn.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getProjects()
	 */
	public ArrayList<String> getProjects() throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT pname FROM projects;");
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

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#check(java.lang.String, java.lang.String)
	 */
	public boolean check(String name, String pass) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM users WHERE name = '" + name +
												"' AND password = '" + pass + "';");
			if (rs.isClosed()) { conn.close(); return false; }
			else { conn.close(); return true; }
		} catch (ClassNotFoundException e) { e.printStackTrace(); return false;
		} catch (SQLException e) { e.printStackTrace(); return false; }
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getPnames(java.lang.String)
	 */
	public ArrayList<String> getPnames(String name) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT pname FROM participates " +
												"WHERE name = '" + name + "';");
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

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getUsers()
	 */
	public ArrayList<String> getUsers() throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT name FROM users;");
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

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#getWrite(java.lang.String, java.lang.String)
	 */
	public int getWrite(String name, String pname) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT write FROM participates WHERE name='" +
													name + "' AND pname='" + pname + "';");
			if (rs.isClosed()) { conn.close(); return 0; }
			else {
				int tmp = rs.getInt(1);
				conn.close();
				return tmp;
			}
		} catch (ClassNotFoundException e) { e.printStackTrace(); return -1;
		} catch (SQLException e) { e.printStackTrace(); return -2; }
		
	}

	/* (non-Javadoc)
	 * @see dbms.ifaces.UserDB#setWrite(java.lang.String, java.lang.String, int)
	 */
	public void setWrite(String name, String pname, int write) throws RemoteException {
		try {
			Class.forName(DB_CLASS);
			Connection conn = DriverManager.getConnection(DB_PROTOCOL + SYS_BASE_URL);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM participates WHERE name='" +
													name + "' AND pname='" + pname + "';");
			if (rs.isClosed()) {
				stat.executeUpdate("INSERT INTO participates VALUES ('" + name + "', '" +
					pname + "', " + write + ");");
				conn.close();
			} else {
				stat.executeUpdate("UPDATE participates SET write=" + write +
						" WHERE name='" + name + "' AND pname='" + pname + "';");
				conn.close();
			}
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}
}
