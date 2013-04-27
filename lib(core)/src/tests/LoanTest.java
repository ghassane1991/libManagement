/*
 * Unit test for the Loan class
 * @author M.T. Segarra
 * @version 0.0.1
 */
package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import loansManagement.Loan;
import subscribersManagement.Subscriber;
import booksManagement.Book;
import booksManagement.BookCopy;
import exceptions.*;

/**
 * @author  user
 */
public class LoanTest {

	/**
	 * @uml.property  name="l"
	 * @uml.associationEnd  
	 */
	private Loan l;
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
	 * @uml.property  name="b"
	 * @uml.associationEnd  
	 */
	private Book b;
	
	@Before
	public void beforeTests() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		this.s = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("aaaaa");
		this.b = new Book("Noche", authors, new GregorianCalendar(), "026765415");
		bc1 = new BookCopy(b);
		bc2 = new BookCopy(b);
	}

	@Test
	public void testEquals() throws BadParametersException, TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, NoSuchAlgorithmException, BadStringException, BadBornDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("bbbb");
		Book b2 = new Book("Noche", authors, new GregorianCalendar(), "026765417");
		BookCopy bc3 = new BookCopy(b2);
		// The same subscriber loans two books
		l = new Loan(bc1, s);	
		Loan l2 = new Loan(bc3, s);
		assertFalse(l.equals(l2));
		
		// Different subscribers loan different books
		//authors = new ArrayList<String>();
		BookCopy bc4 = new BookCopy(b2);
		this.s = new Subscriber("Ma", "SEG", new GregorianCalendar(), "m.s@tb.eu");
		Loan l3 = new Loan(bc4, s);
		assertTrue(!l.equals(l3));
		
		// BookCopy unit test tests if loaning twice the same book
	}
	
	@Test
	public void testLoan() throws BadParametersException, TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		l = new Loan(bc1, s);
		assertTrue(l.getBookCopy().equals(bc1) && l.getLender().equals(s));
	}

	@Test(expected = NullPointerException.class)
	public void testNullLoan() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		new Loan(null, null);
		new Loan(bc2, null);
		new Loan(null, s);
	}

	@Test
	public void testReturnBook() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		l = new Loan(bc2, s);
		l.returnBook();

		assertTrue(l.getBookCopy() == null);
		assertTrue(l.getLender() == null);
		assertTrue(l.getReturnDate() == null);
	}
	
	@Test(expected = LentBookException.class)
	public void testReturnReturnedBook() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		l = new Loan(bc1, s);
		l.returnBook();
		l.returnBook();
	}
	
	@Test(expected = LentBookException.class)
	public void testLendLentBookCopy() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		l = new Loan(bc1, s);
		l = new Loan(bc2, s);
		
		assertTrue(l.getBookCopy() == null);
		assertTrue(l.getLender() == null);
		assertTrue(l.getReturnDate() == null);
	}
}
