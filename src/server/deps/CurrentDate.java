/**
 * @file CurrentDate.java
 */
package server.deps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	public static final String DATE_FORMAT_NOW = "dd.MM.yy";
	//public static final String DATE_FORMAT_NOW = "dd/MM/yyyy HH:mm:ss";

	/**
	 * Αλφαριθμητικό-μεταβλητή φορμαρίσματος στιγμιαίας ημερομηνίας που χρησιμεύει
	 * για χρονική υπογραφή προσπέλασης
	 */
	public static final String STAMP = "ddMMyyyy-HHmmss";

	/**
	 * Επιστρέφει την ημερομηνία συστήματος, τη χρονική στιγμή κλήσης της μεθόδου.
	 * 
	 * @return την ημερομηνία φορμαρισμένη σύμφωνα με τη σταθερά {@link server.deps.CurrentDate#DATE_FORMAT_NOW}
	 */
	public static String now() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/**
	 * Επιστρέφει την ημερομηνία συστήματος, τη χρονική στιγμή κλήσης της μεθόδου.
	 * Η επιστρεφόμενη τιμή είναι είναι ένας μεγάλος ακέραιος ο οποίος μετράει τα
	 * δευτερόλεπτα από την ημερομηνία δημιουργίας του <code>UNIX</code>.
	 * 
	 * @return την ημερομηνία σε δευτερόλεπτα από τη δημιουργία του <code>UNIX</code>
	 */
	public static long epochNow() {

		long epoch = System.currentTimeMillis()/1000;
		return epoch;
	}

	/**
	 * Μετατρέπει την ορισμένη ημερομηνία σε δευτερόλεπτα από την ημερομηνία δημιουργίας
	 * του <code>UNIX</code>. Η ορισμένη ημερομηνία είναι ένα αλφαριθμένο φορμαρισμένο
	 * σύμφωνα με τη σταθερά {@link server.deps.CurrentDate#DATE_FORMAT_NOW}.
	 * 
	 * @param date το αλφαριθμητικό της ημερομηνίας
	 * @return τα δευτερόλεπτα από την ημερομηνία δημιουργίας του <code>UNIX</code>
	 */
	public static long string2epoch(String date) {

		long epoch;
		try {
			epoch = new SimpleDateFormat(DATE_FORMAT_NOW).parse(date).getTime();
			return epoch;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Μετατρέπει τον ορισμένο ακέραιο που δηλώνει τα δευτερόλεπτα από την ημερομηνία
	 * δημιουργίας του <code>UNIX</code>, σε αλφαριθμητικό ημερομηνίας. Η επιστρεφόμενη
	 * τιμή είναι φορμαρισμένη σύμφωνα με τη σταθερά {@link server.deps.CurrentDate#DATE_FORMAT_NOW}.
	 * 
	 * @param epoch τα δευτερόλεπτα από την ημερομηνία δημιουργίας του <code>UNIX</code>
	 * @return το αλφαριθμητικό της ημερομηνίας σε αναγνώσιμη μορφή
	 */
	public static String epoch2string(long epoch) {

		String date = new SimpleDateFormat(DATE_FORMAT_NOW).format(new Date (epoch*1000));
		return date;
	}

	/**
	 * Επιστρέφει την ημερομηνία συστήματος, τη χρονική στιγμή κλήσης της μεθόδου, για χρήση
	 * time stamping.
	 * 
	 * @return την ημερομηνία φορμαρισμένη σύμφωνα με τη σταθερά {@link server.deps.CurrentDate#STAMP}
	 */
	public static String timestamp() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(STAMP);
		return sdf.format(cal.getTime());
	}

}
