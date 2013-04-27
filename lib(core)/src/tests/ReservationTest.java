package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import loansManagement.Loan;

import org.junit.Before;
import org.junit.Test;

import reservationManagement.Reservation;
import subscribersManagement.Entitled;
import subscribersManagement.Subscriber;
import booksManagement.Book;
import booksManagement.BookCopy;
import exceptions.AlreadyLentBookException;
import exceptions.AlreadyReservedException;
import exceptions.BadBornDateException;
import exceptions.BadParametersException;
import exceptions.BadReturnDateException;
import exceptions.BadStringException;
import exceptions.LentBookException;
import exceptions.ReserveBookException;
import exceptions.ReservedBookException;
import exceptions.TooManyLoansException;
import exceptions.TooManyReservationException;

/**
 * @author  user
 */
public class ReservationTest {
	/**
	 * @uml.property  name="r"
	 * @uml.associationEnd  
	 */
	private Reservation r;
	/**
	 * @uml.property  name="b"
	 * @uml.associationEnd  
	 */
	private Book b;
	/**
	 * @uml.property  name="bc1"
	 * @uml.associationEnd  
	 */
	private BookCopy bc1;
	/**
	 * @uml.property  name="bc2"
	 * @uml.associationEnd  
	 */
	private BookCopy bc2;
	/**
	 * @uml.property  name="s"
	 * @uml.associationEnd  
	 */
	private Subscriber s;
	/**
	 * @uml.property  name="s1"
	 * @uml.associationEnd  
	 */
	private Subscriber s1;
	/**
	 * @uml.property  name="s2"
	 * @uml.associationEnd  
	 */
	private Subscriber s2;
	/**
	 * @uml.property  name="s3"
	 * @uml.associationEnd  
	 */
	private Subscriber s3;
	
	
//  Remove commentary from the variable number created in Subscriber and also in the following lines
//	this.number = numberCreated+1;
//	numberCreated++;
		
		@Before
		public void beforeTests() throws NoSuchAlgorithmException, NullPointerException, BadParametersException, BadStringException, BadBornDateException{
			this.s = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
			
			this.s1 = new Subscriber("Vincent", "AIRO",
					new GregorianCalendar(1960, 12, 13), "s1@tb.eu");
			this.s2 = new Subscriber("Betina", "CHTILFIZ",
					new GregorianCalendar(1965, 02, 25), "s2@tb.eu");
			this.s3 = new Entitled("Victor", "CHTILFIZ",
					new GregorianCalendar(1997, 02, 20), "s3@tb.eu", s2);
			ArrayList<String> authors = new ArrayList<String>();
			authors.add("aaaaa");
			this.b = new Book("Noche", authors, new GregorianCalendar(), "026765415");
			bc1 = new BookCopy(b);
			bc2 = new BookCopy(b);
		}


		
		@Test
		public void testReservation() throws BadParametersException, TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException {
			Reservation r2 = new Reservation(b,s);
			assertTrue(r2.getBook().equals(b) && r2.getSubscriber().equals(s));
		}
		@Test (expected=AlreadyReservedException.class)
		public void testSameReservation() throws BadParametersException, BadReturnDateException, TooManyReservationException, AlreadyReservedException{			
			// The same subscriber reserve the same book
			r = new Reservation(b, s);	
			Reservation r2=new Reservation(b,s);
			
		}
		@Test
		public void testCancelReservation() throws BadReturnDateException, TooManyReservationException, ReserveBookException, AlreadyReservedException{
			r = new Reservation(b, s);	
			r.cancel();
		}
		
		@Test
		public void testIsAvailable() throws BadReturnDateException, TooManyReservationException, AlreadyReservedException{
			r = new Reservation(b, s);
			assertTrue(r.isAvailable());	
		}
		@Test (expected=ReservedBookException.class)
		public void testWaitingList() throws NoSuchAlgorithmException, NullPointerException, BadParametersException, BadStringException, BadBornDateException, BadReturnDateException, TooManyReservationException, AlreadyReservedException, TooManyLoansException, LentBookException, ReservedBookException, AlreadyLentBookException{

			Reservation r1=new Reservation(b,s1);
			Reservation r2=new Reservation(b,s2);
			Reservation r3=new Reservation(b,s3);
	
			Loan l=new Loan(bc1,s3);
		}
		
}
	




