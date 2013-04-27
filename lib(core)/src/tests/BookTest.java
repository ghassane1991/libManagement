/*
 * @author M.T. Segarra
 * @version 0.0.1
 */
package tests;

import static org.junit.Assert.*;
import org.junit.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import subscribersManagement.Subscriber;
import loansManagement.Loan;
import booksManagement.Book;
import exceptions.*;

/**
 * @author  user
 */
public class BookTest {

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
	
	@Before
	public void beforeTests() throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		s = new Subscriber("Mayte", "Segarra", new GregorianCalendar(),"m.s@tb.eu");
		
		authors = new ArrayList<String>();
		authors.add("Corral");
		b = new Book("Noche", authors, new GregorianCalendar(), "026765415");
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
	public void testOKBook() throws BadParametersException {
		new Book("Noche", authors, new GregorianCalendar(), "020178515");
	}
	
	@Test(expected = BadParametersException.class)
	public void testNullBook() throws BadParametersException{
		new Book("Noche", authors, new GregorianCalendar(), null);
		/*authors = null;
		new Book(null, authors, new GregorianCalendar(), "020178515");
		new Book("Noche", authors, null, "020178515");
		new Book("Noche", authors, new GregorianCalendar(), null);*/
	}

}
