/**
 * @file DedaAdmin.java
 */
package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import server.deps.Globals;
import server.gui.Login;
import server.ifaces.UserLogin;

/**
 * Η εφαρμογή διαχείρισης του συστήματος. Συνδέεται με το <code>server</code> σε
 * τοπικό επίπεδο για λειτουργίες διαχείρισης και ρύθμισης. Κάνει τις απαραίτητες
 * αρχικοποιήσεις, τη διασύνδεση με το <code>server</code> και αν η σύνδεση είναι
 * επιτυχημένη περνά την εκτέλεση του προγράμματος στην πρώτη οθόνη που βλέπει ο
 * χρήστης, την {@link server.gui.Login}.
 * 
 * @author Team13
 * @version 1.0
 */
public class DedaAdmin {

	/**
	 * Το <code>port</code> στο οποίο ακούει η <code>rmiregistry</code>.
	 */
	public static int port = 1099;

	/**
	 * Η διεύθυνση του <code>server</code> που περιέχει την <code>rmiregistry</code>.
	 */
	public static final String server = "127.0.0.1";

	/**
	 * Η ονομασία της υπηρεσίας αυθεντικοποίησης στο <code>server</code>.
	 * Αποτελεί το τελευταίο τμήμα της διεύθυνσης του αντικειμένου που
	 * υλοποιεί την υπηρεσία στην <code>rmiregistry</code>.
	 */
	public static final String loginService = Globals.LOGINSERVICE;

	/**
	 * Η κύρια μέθοδος εκκινεί το πρόγραμμα διαχείρισης του <code>server</code>.
	 * Αρχικά προσπαθεί να συνδεθεί στο <code>server</code>. Αν η σύνδεση γίνει
	 * με επιτυχία, περνά τη σύνδεση στην οθόνη αυθεντικοποίησης καθώς επίσης και
	 * τη ροή του προγράμματος.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Σύνδεση με τον DedaServer LoginService
		String serverUrl = "//" + server + ":" + port + "/" + loginService;
		try {
			UserLogin usr = (UserLogin) Naming.lookup(serverUrl);
			new Login(usr);
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (RemoteException e) {
			if (e.getMessage().contains("Connection refused"))
				System.out.println(e.getMessage());
			else e.printStackTrace();
		} catch (NotBoundException e) { e.printStackTrace(); }
	}

}
