package reservationManagement;

import booksManagement.Book;
import booksManagement.BookCopy;
import subscribersManagement.Subscriber;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import exceptions.AlreadyReservedException;
import exceptions.BadReturnDateException;
import exceptions.ReserveBookException;
import exceptions.TooManyReservationException;

import main.Constraints;

@Entity
@IdClass(value = ReservationID.class)
@Table(name = "reservations")
@NamedQuery(name = "findAllReservations", query = "select r from Reservation r")
public class Reservation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="book"
	 * @uml.associationEnd  
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "book_isbn")
	private Book book = null;

	/**
	 * @uml.property  name="subscriber"
	 * @uml.associationEnd  
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "subscriber")
	private Subscriber subscriber = null;

	/**
	 * @uml.property  name="finishDate"
	 */
	private Calendar finishDate;

	/**
	 * @uml.property  name="startDate"
	 */
	private Calendar startDate;

	/**
	 * Default constructor
	 */
	public Reservation() {
	}

	/**
	 * @throws BadReturnDateException
	 * @throws AlreadyReservedException 
	 * @throws ReserveBookException 
	 */
	public Reservation(Book book, Subscriber subscriber)
			throws BadReturnDateException, TooManyReservationException, AlreadyReservedException {
		setBook(book);
		setSubscriber(subscriber);

		this.startDate = new GregorianCalendar();

		Calendar fDate = new GregorianCalendar();
		fDate.add(Calendar.DAY_OF_MONTH, Constraints.reservationDELAY);
		setFinishDate(fDate);
		
		book.setReserved(true);
		int temp = book.getNumReservation();
		temp++;
		book.setNumReservation(temp);
		
		book.addSubscriber(subscriber);
		subscriber.reserve(this);
	}

	/**
	 * @throws ReserveBookException 
	 */
	public void cancel() throws ReserveBookException {
		if (book==null || subscriber==null)	
			throw new ReserveBookException();
		
		if (book.getNumReservation()==1){ 
			this.book.setReserved(false); // If there are other subscribers who reserved this book, "book.reserved" field must stay true
		}
		int temp = book.getNumReservation();
		temp--;
		book.setNumReservation(temp);
		book.removeSubscriber(this.subscriber);
		subscriber.cancelReservation(this);
		
//		book = null;
//		subscriber = null;
	}

	/**
	 * Getter of the property <tt>book</tt>
	 * 
	 * @return Returns the book.
	 * @uml.property name="book"
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Getter of the property <tt>finishDate</tt>
	 * 
	 * @return Returns the finishDate.
	 * @uml.property name="finishDate"
	 */
	public Calendar getFinishDate() {
		return finishDate;
	}

	/**
	 * Getter of the property <tt>startDate</tt>
	 * 
	 * @return Returns the startDate.
	 * @uml.property name="startDate"
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * Getter of the property <tt>subscriber</tt>
	 * 
	 * @return Returns the subscriber.
	 * @uml.property name="subscriber"
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 */
	public boolean isAvailable() {
		// Verify if at least one copy exist
		if (book.getBookCopy().size() == 0) {
			return false;
		}
		// Verify if at least one copy is not lent
		for (BookCopy bc : book.getBookCopy()) {
			if (!bc.isLent()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Setter of the property <tt>book</tt>
	 * 
	 * @param book
	 *            The book to set.
	 * @uml.property name="book"
	 */
	public void setBook(Book book) {
		if (book == null) {
			throw new NullPointerException();
		}

		this.book = book;
	}

	/**
	 * Setter of the property <tt>finishDate</tt>
	 * 
	 * @param finishDate
	 *            The finishDate to set.
	 * @throws BadReturnDateException
	 * @uml.property name="finishDate"
	 */
	public void setFinishDate(Calendar finishDate)
			throws BadReturnDateException {
		if (finishDate == null) {
			throw new NullPointerException();
		}
		if (finishDate.before(new GregorianCalendar())) {
			throw new BadReturnDateException();
		}

		this.finishDate = finishDate;
	}

	/**
	 * Setter of the property <tt>subscriber</tt>
	 * 
	 * @param subscriber
	 *            The subscriber to set.
	 * @uml.property name="subscriber"
	 */
	public void setSubscriber(Subscriber subscriber) {
		if (subscriber == null) {
			throw new NullPointerException();
		}
		this.subscriber = subscriber;
	}

	public boolean equals(Object reservation){
		if (reservation == null)
			return false;
		
		Reservation r = (Reservation)reservation;
		boolean res = this.book.equals(r.book) && this.subscriber.equals(r.subscriber);
		
		return res;	
	}
	
	public String toString(){
		return book.getIsbn() + "----reserve------>" + subscriber.getNumber() + "(" + subscriber.getFirstName() + " "+
			   subscriber.getLastName()+ ")";
	}
}
