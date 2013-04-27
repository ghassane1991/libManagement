
package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import loansManagement.Loan;
import org.junit.Before;
import org.junit.Test;
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
import exceptions.ReservedBookException;
import exceptions.TooManyLoansException;
import exceptions.TooManyReservationException;

/**
 * @author  user
 */
public class SubscriberTest {

	/**
	 * @uml.property  name="s"
	 * @uml.associationEnd  
	 */
	private Subscriber s;

	@Before
	public void beforeTests() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		this.s = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
	}

	@Test
	public void testCanLend() {
		assertTrue(this.s.canLend());
	}

	@Test
	public void testEqualsObject() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {

		// Compare subscribers with data
		// Modify Subscriber.java : delete comment for lines where writed "numberCreated"
		Subscriber s2 = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
		assertTrue(this.s.equals(s2));

		s2 = new Subscriber("Mayte", "SEG", new GregorianCalendar(),"m.s@tb.eu");
		System.out.println(s);
		System.out.println(this.s.equals(s2));
		assertFalse(this.s.equals(s2));

		s2 = new Subscriber("Mts", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
		assertFalse(this.s.equals(s2));

		s2 = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(2000, 12, 13),"m.s@tb.eu");
		assertFalse(this.s.equals(s2));

		
	}

	@Test
	public void testExistingLoans() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		
		assertFalse(this.s.existingLoans());
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("aaa");
		Book b = new Book("La noche", authors, new GregorianCalendar(), "23456566");
		BookCopy bc= new BookCopy(b);
		new Loan(bc, this.s);

		assertTrue(this.s.existingLoans());
	}

	@Test
	public void testLend() throws BadParametersException, TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("aaa");
		Book b = new Book("La noche", authors, new GregorianCalendar(), "23456566");
		BookCopy bc= new BookCopy(b);
		new Loan(bc, this.s);
		assertTrue(this.s.getCurrentLoans().size() == 1);
	}

	@Test(expected = NullPointerException.class)
	public void testNullSubscriber() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		new Subscriber("Mayte", "SEGARRA", null,"m.s@tb.eu");
	    new Subscriber("Mayte", null, new GregorianCalendar(),"m.s@tb.eu");
		new Subscriber(null, "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
		new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),null);
	}

	@Test
	public void testOKSubscriber() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		Subscriber s = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
		assertTrue(s.getCurrentLoans().size() == 0);
	}

	
	@Test
	public void testReturnBook() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("aaa");
		Book b = new Book("La noche", authors, new GregorianCalendar(), "23456566");
		BookCopy bc= new BookCopy(b);
		Loan l = new Loan(bc, this.s);

		this.s.returnBook(l);
		assertFalse(this.s.existingLoans());
	}

	@Test(expected = TooManyLoansException.class)
	public void testTooManyLoans() throws BadParametersException, TooManyLoansException,
			LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		
		// Modify Constraints.java : maxLOANS = 1
		ArrayList<String> authors = new ArrayList<String>();
		Book b = new Book("La noche", authors, new GregorianCalendar(), "23456566");
		BookCopy bc= new BookCopy(b);
		Loan l = new Loan(bc, this.s);
		this.s.lend(l);
		assertTrue(this.s.getCurrentLoans().size() == 1);
		
		b = new Book("Luna", authors, new GregorianCalendar(), "23457766");
		bc= new BookCopy(b);
		l = new Loan(bc, this.s);
		this.s.lend(l);
	}
}