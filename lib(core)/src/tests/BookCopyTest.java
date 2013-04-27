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
import exceptions.ReturnReturnedBook;
import exceptions.TooManyLoansException;
import exceptions.TooManyReservationException;

/**
 * @author  user
 */
public class BookCopyTest {
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
	private ArrayList<String> authors;
	/**
	 * @uml.property  name="bCopy"
	 * @uml.associationEnd  
	 */
	private BookCopy bCopy;
	@Before
	public void beforeTests() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		s = new Subscriber("Mayte", "SEGARRA", new GregorianCalendar(),"m.s@tb.eu");
	
		authors = new ArrayList<String>();
		authors.add("Corral");
		
		b = new Book("Noche", authors, new GregorianCalendar(), "026765415");
		bCopy = new BookCopy(b);
	}
	@Test
	public void testEqualsObject() throws BadParametersException {
		Book b2 = new Book("Noche", authors, new GregorianCalendar(), "026765415");
		assertTrue(b.equals(b2));
		
		b2 = new Book("Noche", authors, new GregorianCalendar(), "020178517");
		assertFalse(b.equals(b2));
		
		b2 = new Book("El dia", authors, new GregorianCalendar(), "026765415");
		assertTrue(b.equals(b2));
	}

	@Test
	public void testOKBookCopy() throws BadParametersException {
		new BookCopy(new Book("Noche", authors, new GregorianCalendar(), "020178515"));
	}
	
	@Test(expected = BadParametersException.class)
	public void testNullBookCopy() throws BadParametersException{
		new BookCopy(null);
	}
	
	@Test
	public void testLendBook() throws BadParametersException, 
	TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException{
		new Loan(bCopy,s);
		assertTrue(bCopy.getLoan() != null);
	}
	
	@Test(expected = LentBookException.class)
	public void testLendLentBook() throws BadParametersException, 
	TooManyLoansException, LentBookException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException{
		new Loan(bCopy,s);
		s = new Subscriber("Mayte", "SEG", new GregorianCalendar(),"m.s@tb.eu");
		new Loan(bCopy,s);
	}
	
	@Test
	public void testReturnBook() throws BadParametersException, 
	TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReturnReturnedBook, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException{
		new Loan(bCopy,s);
		
		bCopy.returnBookCopy();
		assertTrue(bCopy.getLoan() == null);
	}
	
	@Test
	public void testIslent() throws BadParametersException, 
	TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReturnReturnedBook, ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException{
		new Loan(bCopy,s);

		assertTrue(bCopy.isLent());
		
		bCopy.returnBookCopy();
		assertFalse(bCopy.isLent());
	}
}
