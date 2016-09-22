/**
 * @file DeleteDir.java
 */
package server.deps;

import java.io.File;

/**
 * Βοηθητική κλάση-βιβλιοθήκη σχετική με τη διαγραφή φακέλου. Περιέχει μόνο μια
 * στατική μέθοδο η οποία διαγράφει μια ολόκληρη ιεραρχία φακέλων. Δηλαδή, διαγράφει
 * το φάκελο που της ορίζεται και όλους τους υποφακέλους με τα περιεχόμενά τους.
 * 
 * @author Unknown (Παραδείγματα από Internet)
 */
public class DeleteDir {

	/**
	 * Διαγράφει μια ολόκληρη ιεραρχία καταλόγων. Διαγράφει τα περιεχόμενα του
	 * καταλόγου που της ορίζεται καθώς επίσης και τους υποκαταλόγους με τα δικά τους
	 * περιεχόμενα. Η διαγραφή υλοποιείται με αναδρομικό τρόπο. Επιστρέφει <code>true</code>
	 * ή <code>false</code> ανάλογα με το αν ήταν δυνατή η διαγραφή.
	 * 
	 * @param path η διαδρομή για τον κατάλογο προς διαγραφή
	 * @return <code>true</code> όταν η διαγραφή ήταν επιτυχημένη,
	 *			<code>false</code> όταν η διαγραφή δεν ήταν επιτυχημένη.
	 */
	static public boolean deleteDirectory(File path) {

		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}

		return( path.delete() );

	}

}
