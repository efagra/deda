/**
 * @file Bug.java
 */
package server.sys;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.dbs.TracDB;
import server.deps.CurrentDate;
import server.ifaces.BugIntrf;

/**
 * Υλοποιεί τη διασύνδεση {@link server.ifaces.BugIntrf}.
 * Η κλάση αυτή παρέχει πληροφορίες που αφορούν το <code>bug</code>, καθώς επίσης
 * και επιπλέον μεθόδους διαχείρισής του αυτόματα από το σύστημα. Αποθηκεύει το όνομα
 * του <code>bug</code>, το όνομα (όχι όλο το αντικείμενο) του <code>project</code>
 * στο οποίο ανήκει, το όνομα του συγγραφέα, τον τίτλο, το <code>status</code> (<code>open
 * </code>, <code>closed</code>, <code>abandoned</code>, <code>reopened</code>), την
 * προτεραιότητα, την ημερομηνία δημιουργίας, την καταληκτική ημερομηνία και την περιγραφή,
 * δλδ το σώμα κειμένου.
 * 
 * @author Team13
 * @version 1.5
 * @see server.ifaces.BugIntrf
 */
@SuppressWarnings("serial")
public class Bug extends UnicastRemoteObject implements BugIntrf {

	/**
	 * Το όνομα (όχι όλο το αντικείμενο) του <code>project</code> στο οποίο ανήκει.
	 */
	private String project;

	/**
	 * Το όνομα (όχι όλο το αντικείμενο) του χρήστη που το συνέταξε.
	 */
	private String author;

	/**
	 * Ο τίτλος ή αλλιώς σύνοψη του <code>bug</code>.
	 */
	private String title;

	/**
	 * Tο <code>status</code> (<code>open</code>, <code>closed</code>, <code>abandoned</code>,
	 * <code>reopened</code>)status (open, closed, abandoned, reopened).
	 */
	private String status;

	/**
	 * Η προτεραιότητα.
	 */
	private String priority;

	/**
	 * Η ημερομηνία δημιουργίας (δεν υπάρχει περιορισμός στη μορφοποίηση).
	 */
	private String dateOfCreation;

	/**
	 * H καταληκτική ημερομηνία (δεν υπάρχει περιορισμός στη μορφοποίηση).
	 */
	private String deadline;

	/**
	 * Η περιγραφή, δλδ το σώμα κειμένου.
	 */
	private String description;

	/**
	 * Ο κατασκευαστής ενργοποιεί το αντικείμενο και γεμίζει τις πληροφορίες από τη βάση.
	 * Δέχεται το όνομα του <code>bug</code> και το όνομα του <code>project</code> για το
	 * οποίο έχει αναφερθεί. Στη συνέχεια με τις κατάλληλες κλήσεις στη βαση γεμίζουν και
	 * οι υπόλοιπες πληροφορίες. Σε περίπτωση που το <code>bug</code> δεν υπάρχει στη βάση,
	 * τότε πρόκειται για ένα νέο αντικείμενο. Οι ιδιότητες αρχικοποιούνται με τις προεπιλεγμένες
	 * τιμές και μπορούν να πάρουν τα πραγματικά δεδομένα τους μεσω των δημοσίων μεθόδοων
	 * της συγκεκριμένησ κλάσης. Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 */
	public Bug(String name, String project) throws RemoteException {
		Bug abug = new TracDB(project).getBug(name);
		if (abug!=null) {
			this.project = abug.getProject();
			this.author = abug.getAuthor();
			this.status = abug.getStatus();
			this.priority = abug.getPriority();
			this.dateOfCreation = abug.getDateOfCreation();
			this.deadline = abug.getDeadline();
			this.description = abug.getDescription();
		} else {
			this.project = project;
	    	this.title = name;
	    	this.author = null;
	    	this.status = "opened";
	    	this.priority = "";
	    	this.deadline = "";
	    	this.description = "";
	    	this.dateOfCreation = CurrentDate.now();
		}
	}

	/**
	 * Ο κατασκευαστής με ορίσματα για όλες τις ιδιότητες ενργοποιεί το αντικείμενο και
	 * χρησιμοποιείται μόνο από τη βάση η οποία μπορεί να κάνει αυτήν την κλήση απευθείας
	 * χωρίς απώλεια της συνέπειας των δεδομένων. Σε κάθε άλλη περίπτωση πρέπει να καλείται
	 * ο κατασκευαστής {@link server.sys.Bug#Bug(java.lang.String, java.lang.String)}.
	 * Δεν ανεβάζει το αντικείμενο στην <code>rmiregistry</code>.
	 */
	public Bug(String project, String author, String title,
    		String status, String priority,String deadline,
    		String description, String doc) throws RemoteException {
    	this.project = project;
    	this.author = author;
    	this.title = title;
    	this.status = status;
    	this.priority = priority;
    	this.deadline = deadline;
    	this.description = description;
    	this.dateOfCreation = doc;
    }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#save()
	 */
	public void save() throws RemoteException { new TracDB(project).save(this); }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getProject()
	 */
	public String getProject() throws RemoteException { return project; }

	/**
	 * Ορίζει το όνομα του <code>project</code> στο οποίο ανήκει αυτό το αντικείμενο.
	 * 
	 * @param project το όνομα του <code>project</code>
	 */
	public void setProject(String project) { this.project = project; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getAuthor()
	 */
	public String getAuthor() throws RemoteException { return author; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#setAuthor(java.lang.String)
	 */
	public void setAuthor(String author) throws RemoteException { this.author = author; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getPriority()
	 */
	public String getPriority() throws RemoteException { return priority; }

    /* (non-Javadoc)
     * @see server.ifaces.BugIntrf#setPriority(java.lang.String)
     */
	public void setPriority(String priority) throws RemoteException { this.priority = priority; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getStatus()
	 */
	public String getStatus() throws RemoteException { return status; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#setStatus(java.lang.String)
	 */
	public void setStatus(String stat) throws RemoteException { this.status = stat; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getDeadline()
	 */
	public String getDeadline() throws RemoteException { return deadline; }

    /* (non-Javadoc)
     * @see server.ifaces.BugIntrf#setDeadLine(java.lang.String)
     */
    public void setDeadLine(String line) throws RemoteException { this.deadline = line; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getDateOfCreation()
	 */
	public String getDateOfCreation() throws RemoteException { return dateOfCreation; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getName()
	 */
	public String getName() throws RemoteException { return title; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#getDescription()
	 */
	public String getDescription() throws RemoteException { return description; }

	/* (non-Javadoc)
	 * @see server.ifaces.BugIntrf#setDescription(java.lang.String)
	 */
	public void setDescription(String description) throws RemoteException { this.description = description; }

}
