/**
 * @file CurrentDate.java
 */
package client.deps;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Βοηθητική κλάση-βιβλιοθήκη σχετική με ημερομηνίες. Αποτελεί μια συλλογή μεθόδων
 * για τη διαχείριση ημερομηνιών. Περιέχει μόνο στατικές μεθόδους.
 * 
 * @author Unknown (Παραδείγματα από Internet)
 */
public class CurrentDate {

	/**
	 * Αλφαριθμητικό-μεταβλητή φορμαρίσματος στιγμιαίας ημερομηνίας
	 */
	public static final String DATE_FORMAT_NOW = "[dd/MM/yyyy HH:mm:ss]";	
	//public static final String DATE_FORMAT_NOW = "dd.MM.yy";

	/**
	 * Αλφαριθμητικό-μεταβλητή φορμαρίσματος στιγμιαίας ημερομηνίας που χρησιμεύει
	 * για χρονική υπογραφή προσπέλασης
	 */
	public static final String STAMP = "ddMMyyyy-HHmmss";

	/**
	 * Επιστρέφει την ημερομηνία συστήματος, τη χρονική στιγμή κλήσης της μεθόδου.
	 * 
	 * @return την ημερομηνία φορμαρισμένη σύμφωνα με τη σταθερά {@link client.deps.CurrentDate#DATE_FORMAT_NOW}
	 */
	public static String now() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/**
	 * Επιστρέφει την ημερομηνία συστήματος, τη χρονική στιγμή κλήσης της μεθόδου, για χρήση
	 * time stamping.
	 * 
	 * @return την ημερομηνία φορμαρισμένη σύμφωνα με τη σταθερά {@link client.deps.CurrentDate#STAMP}
	 */
	public static String timestamp() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(STAMP);
		return sdf.format(cal.getTime());
	}

}
