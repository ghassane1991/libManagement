/**
 * 
 */
package tests;

import javax.persistence.*;

import jpaUtils.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import loansManagement.History;
import loansManagement.HistoryDAO;
import loansManagement.HistoryID;
import loansManagement.Loan;
import loansManagement.LoanDAO;
import loansManagement.LoanID;

import org.hibernate.LazyInitializationException;
import org.junit.*;

import booksManagement.*;
import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import reservationManagement.ReservationID;
import subscribersManagement.*;

import exceptions.*;

/**
 * @author  user
 */
public class PersistentLibraryTest {
	/**
	 * @uml.property  name="subscriberDAO"
	 * @uml.associationEnd  
	 */
	private SubscriberDAO subscriberDAO;
	/**
	 * @uml.property  name="bookDAO"
	 * @uml.associationEnd  
	 */
	private BookDAO bookDAO;
	/**
	 * @uml.property  name="loanDAO"
	 * @uml.associationEnd  
	 */
	private LoanDAO loanDAO;
	/**
	 * @uml.property  name="bookCopyDAO"
	 * @uml.associationEnd  
	 */
	private BookCopyDAO bookCopyDAO;
	/**
	 * @uml.property  name="ageCategoryDAO"
	 * @uml.associationEnd  
	 */
	private AgeCategoryDAO ageCategoryDAO;
	/**
	 * @uml.property  name="hisDAO"
	 * @uml.associationEnd  
	 */
	private HistoryDAO hisDAO;
	private ArrayList<String> authors1 = new ArrayList<String>();;
	private ArrayList<String> authors2 = new ArrayList<String>();;
	private ArrayList<String> authors3 = new ArrayList<String>();;
	private ArrayList<String> authors4 = new ArrayList<String>();;
	/**
	 * @uml.property  name="resDAO"
	 * @uml.associationEnd  
	 */
	private ReservationDAO resDAO;

	private void createBooks() throws BadParametersException {
		EntityManager em;
		EntityTransaction tx;
		bookDAO = new BookDAO();
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		if (tx.isActive()) {
			JPAUtil.closeEntityManager();
			em = JPAUtil.getEntityManager();
			tx = em.getTransaction();
		}
		tx.begin();
		if (bookDAO.isEmpty()) {
			System.out.println("===== Create Book data in memory ====");
			authors1.add("Elmasri");
			authors1.add("Navathe");
			authors2.add("Peter Gulutzan");
			authors2.add("Trudy Pelzer");
			authors3.add("eeeeeeeeeeeee");
			authors4.add("aaaaaaaaaaaaa");
			Book b1 = new Book("Fundamentals of Database Systems", authors1,
					new GregorianCalendar(), "0-321-20228-4");
			Book b2 = new Book("SQL-99 Complete, Really", authors2,
					new GregorianCalendar(), "0-87930-568-1");
			Book b3 = new Book("New book", authors3, new GregorianCalendar(),
					"0-87930-700-3");
			Book b4 = new Book("New book2", authors4, new GregorianCalendar(),
					"0-87930-701-3");
			System.out.println("===== Persist book data ====");
			try {
				bookDAO.add(b1);
				bookDAO.add(b2);
				bookDAO.add(b3);
				bookDAO.add(b4);
			} catch (Exception pe) {
				System.err.println("Exception when persisting data");
				JPAUtil.closeEntityManager();
				pe.printStackTrace();
			}
		}
		System.out.println("===== Display book data =====");
		System.out.println(bookDAO.toString());

		tx.commit();
		JPAUtil.closeEntityManager();
	}

	private void createBookCopies() throws BadParametersException,
			BookExistsException {
		EntityManager em;
		EntityTransaction tx;
		bookCopyDAO = new BookCopyDAO();
		System.out.println("===== Create Book Copy data in memory ====");
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		if (bookCopyDAO.isEmpty()) {
			Book b1 = bookDAO.get("0-321-20228-4");
			System.out.println(b1);
			BookCopy bc1 = new BookCopy(b1);
			System.out.println(bc1);
			BookCopy bc2 = new BookCopy(b1);
			System.out.println(bc2);
			BookCopy bc3 = new BookCopy(b1);
			System.out.println(bc3);
			BookCopy bc4 = new BookCopy(b1);
			System.out.println(bc4);
			BookCopy bc5 = new BookCopy(b1);
			System.out.println(bc5);
			BookCopy bc6 = new BookCopy(b1);
			System.out.println(bc6);
			BookCopy bc7 = new BookCopy(b1);
			System.out.println(bc7);
			BookCopy bc8 = new BookCopy(b1);
			System.out.println(bc8);
			BookCopy bc9 = new BookCopy(b1);
			System.out.println(bc9);

			Book b2 = bookDAO.get("0-87930-568-1");
			BookCopy bc10 = new BookCopy(b2);

			// System.out.println(bc9);

			System.out.println("===== Persist book copy data ====");
			try {
				bookCopyDAO.add(bc1);
				bookCopyDAO.add(bc2);
				bookCopyDAO.add(bc3);
				bookCopyDAO.add(bc4);
				bookCopyDAO.add(bc5);
				bookCopyDAO.add(bc6);
				bookCopyDAO.add(bc7);
				bookCopyDAO.add(bc8);
				bookCopyDAO.add(bc9);
				bookCopyDAO.add(bc10);

				em.merge(b1);
			} catch (Exception pe) {
				System.err.println("Exception when persisting data");
				JPAUtil.closeEntityManager();
				pe.printStackTrace();
			}
		}
		tx.commit();
		JPAUtil.closeEntityManager();

		/*
		 * em = JPAUtil.getEntityManager(); tx = em.getTransaction();
		 * tx.begin();
		 * 
		 * 
		 * tx.commit();
		 */

		System.out.println("===== Display book copy data =====");
		System.out.println(bookCopyDAO.toString());

		// JPAUtil.closeEntityManager();
	}

	private void createAgeCategory() throws BadParametersException, EmptyStringException, BadStringException {
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		ageCategoryDAO = new AgeCategoryDAO();
		if (ageCategoryDAO.isEmpty()) {
			System.out.println("===== Create ageCategory data in memory ====");
			AgeCategory ageCategory0 = new AgeCategory("enfant", 0, 18);
			AgeCategory ageCategory1 = new AgeCategory("adulte", 19, 100);
			System.out.println("===== Persist ageCategory data ====");
			try {
				ageCategoryDAO.add(ageCategory0);
				ageCategoryDAO.add(ageCategory1);

			} catch (Exception pe) {
				System.err.println("Exception when persisting data");
				JPAUtil.closeEntityManager();
				pe.printStackTrace();
			}
		}

		System.out.println("===== Display AgeCategory data =====");
		System.out.println(ageCategoryDAO.toString());

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	private void createSubscribers() throws BadParametersException,
			NoSuchAlgorithmException, NullPointerException, BadStringException,
			BadBornDateException {
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		subscriberDAO = new SubscriberDAO();
		if (subscriberDAO.isEmpty()) {
			System.out.println("===== Create Subscriber data in memory ====");
			Subscriber s1 = new Subscriber("Vincent", "AIRO",
					new GregorianCalendar(1960, 12, 13), "s1@tb.eu");
			Subscriber s2 = new Subscriber("Betina", "CHTILFIZ",
					new GregorianCalendar(1965, 02, 25), "s2@tb.eu");
			Subscriber s5 = new Subscriber("Raaaa", "LGUSSS",
					new GregorianCalendar(1991, 12, 13), "s5@tb.eu");
			Subscriber s6 = new Subscriber("Raaaaa", "LGUSSSS",
					new GregorianCalendar(1991, 12, 20), "s6@tb.eu");
			Entitled s3 = new Entitled("Victor", "CHTILFIZ",
					new GregorianCalendar(1997, 02, 20), "s3@tb.eu", s2);
			Entitled s4 = new Entitled("Luisa", "CHTILFIZ",
					new GregorianCalendar(1993, 07, 30), "s4@tb.eu", s2);
			System.out.println("===== Persist Subscriber data ====");
			try {
				subscriberDAO.add(s1);
				subscriberDAO.add(s2);
				subscriberDAO.add(s3);
				subscriberDAO.add(s4);
				subscriberDAO.add(s5);
				subscriberDAO.add(s6);
			} catch (Exception pe) {
				System.err.println("Exception when persisting data");
				JPAUtil.closeEntityManager();
				pe.printStackTrace();
			}
		}
		System.out.println("===== Display Subscriber data =====");
		System.out.println(subscriberDAO.toString());

		tx.commit();
		JPAUtil.closeEntityManager();
	}

	@Before
	public void beforeTests() throws BadParametersException,
			BookExistsException, NoSuchAlgorithmException,
			NullPointerException, BadStringException, BadBornDateException, EmptyStringException {
		createBooks();
		createBookCopies();
		createSubscribers();
		createAgeCategory();
		resDAO = new ReservationDAO();
		loanDAO = new LoanDAO();
		hisDAO = new HistoryDAO();
	}

	@Test
	public void testNoTATestButStoresData() {
		System.out.println("***************Store data***************");
	}

	@Test
	public void testRemoveBookCopy() throws BookExistsException,
			BadParametersException, LentBookException, BookCopyExistsException {
		EntityManager em;
		EntityTransaction tx;
		// bookCopyDAO = new BookCopyDAO();
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		Book b1 = bookDAO.get("0-321-20228-4");
		bookCopyDAO.remove(b1.removeCopy("0-321-20228-4/2"));
		em.merge(b1);
		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new book copy data =====");
		System.out.println(bookCopyDAO.toString());

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		BookCopy bc1 = new BookCopy(b1);
		BookCopy bc2 = new BookCopy(b1);
		try {
			bookCopyDAO.add(bc1);
			bookCopyDAO.add(bc2);
			em.merge(b1);
		} catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new book copy data =====");
		System.out.println(bookCopyDAO.toString());

	}

	@Test
	public void testLoanBookCopy() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservedBookException,
			TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		long sNumber = 1;
		Subscriber s;
		BookCopy bc;
		String bookCopyId = "0-321-20228-4/1";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// get a lender, a book and a book copy
		bc = bookCopyDAO.get(bookCopyId);
		s = subscriberDAO.get(sNumber);

		// Create loan object
		Loan l = new Loan(bc, s);
		loanDAO.add(l);
		hisDAO.add(l.getHistory());
		em.merge(s);
		em.merge(bc);
		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		// bc = bookCopyDAO.get(bookCopyId);
		// Loan l2 = loanDAO.get(new LoanID(bc,s));
		Loan l2 = loanDAO.get(l.getLoanID());
		System.out.println(l2.getBookCopy().getId() + "--->"
				+ l2.getLender().getFirstName());
		tx.commit();
		JPAUtil.closeEntityManager();
	}

	@Test(expected = LentBookException.class)
	public void testLoanLentBookCopy() throws BookExistsException,
			BadParametersException, TooManyLoansException, LentBookException,
			SubscriberExistsException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservedBookException,
			TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {

		long sNumber1 = 1;
		long sNumber2 = 2;
		Subscriber s1;
		Subscriber s2;
		BookCopy bc;
		String bookCopyId = "0-321-20228-4/3";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		bc = bookCopyDAO.get(bookCopyId);
		s1 = subscriberDAO.get(sNumber1);

		Loan l1 = new Loan(bc, s1);
		loanDAO.add(l1);
		hisDAO.add(l1.getHistory());
		em.merge(bc);
		em.merge(s1);

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		s2 = subscriberDAO.get(sNumber2);
		tx.commit();
		JPAUtil.closeEntityManager();

		Loan l2 = new Loan(bc, s2);

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		loanDAO.add(l2);
		hisDAO.add(l2.getHistory());
		em.merge(bc);
		em.merge(s2);

		tx.commit();
		JPAUtil.closeEntityManager();
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		// Loan ll1 = loanDAO.get(new LoanID(bc,s1));
		Loan ll1 = loanDAO.get(l1.getLoanID());
		System.out.println(ll1.getBookCopy().getId() + "--->"
				+ ll1.getLender().getFirstName());

		// Loan ll2 = loanDAO.get(new LoanID(bc,s2));
		Loan ll2 = loanDAO.get(l2.getLoanID());
		System.out.println(ll2.getBookCopy().getId() + "--->"
				+ ll2.getLender().getFirstName());
		tx.commit();
		JPAUtil.closeEntityManager();

	}

	@Test
	public void testReturnBook() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReturnReturnedBook, HistoryExistsException,
			ReservedBookException, TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		long sNumber = 1;
		Subscriber s;
		BookCopy bc;
		Loan l;
		// String bookCopyId = "0-321-20228-4/2";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// bc = bookCopyDAO.get(bookCopyId);
		Book b = bookDAO.get("0-321-20228-4");
		bc = new BookCopy(b);
		bookCopyDAO.add(bc);

		s = subscriberDAO.get(sNumber);
		l = new Loan(bc, s);

		loanDAO.add(l);
		hisDAO.add(l.getHistory());
		em.merge(s);
		em.merge(bc);
		tx.commit();
		JPAUtil.closeEntityManager();

		// bc.returnBookCopy();
		l.returnBook();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		em.merge(l);
		em.merge(s);
		em.merge(bc);
		em.merge(l.getHistory());

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

//		History h = hisDAO.get(new HistoryID(l.getHistory().getCopy(), l
//				.getHistory().getLoanDate()));
		History h = hisDAO.get(l.getHistory().getHistoryID());
		System.out.println(h.getReturnDate().getTime());

		tx.commit();
		JPAUtil.closeEntityManager();
	}

	@Test
	public void testAddsubscriberToAgeCategory()
			throws BadAgeCategoryException, AgeCategoryExistsException,
			SubscriberExistsException {
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		AgeCategory a0 = ageCategoryDAO.get("enfant");
		AgeCategory a1 = ageCategoryDAO.get("adulte");

		Subscriber s1 = subscriberDAO.get(1);
		Subscriber s3 = subscriberDAO.get(3);

		a0.addSubscriber(s3);
		a1.addSubscriber(s1);

		em.merge(s3);
		em.merge(s1);
		em.merge(a0);
		em.merge(a1);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new age categories data =====");
		System.out.println(ageCategoryDAO.toString());

	}

	@Test(expected = BadAgeCategoryException.class)
	public void testAddsubscriberToBadAgeCategory()
			throws BadAgeCategoryException, AgeCategoryExistsException,
			SubscriberExistsException {
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		AgeCategory a0 = ageCategoryDAO.get("enfant");

		Subscriber s1 = subscriberDAO.get(1);
		tx.commit();
		JPAUtil.closeEntityManager();

		a0.addSubscriber(s1);

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		em.merge(a0);
		em.merge(s1);
		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new age categories data =====");
		System.out.println(ageCategoryDAO.toString());

	}

	@Test
	public void testAddsubscriberToNewAgeCategory()
			throws BadAgeCategoryException, AgeCategoryExistsException,
			SubscriberExistsException, NullPointerException,
			BadBornDateException {
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		AgeCategory a0 = ageCategoryDAO.get("enfant");
		AgeCategory a1 = ageCategoryDAO.get("adulte");

		Subscriber s3 = subscriberDAO.get(3);

		a0.addSubscriber(s3);

		em.merge(s3);
		em.merge(a0);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new age categories data =====");
		System.out.println(ageCategoryDAO.toString());

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		a0 = ageCategoryDAO.get("enfant");
		a1 = ageCategoryDAO.get("adulte");
		s3 = subscriberDAO.get(3);

		s3.setBornDate(new GregorianCalendar(1980, 02, 20));

		a1.addSubscriber(s3, a0);

		em.merge(s3);
		em.merge(a1);
		em.merge(a0);
		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("===== Display new age categories data =====");
		System.out.println(ageCategoryDAO.toString());

	}

	@Test
	public void testDetachedData1() throws SubscriberExistsException {
		long sNumber = 3;

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Subscriber s = subscriberDAO.get(sNumber);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("\n Subscriber number " + sNumber + " is " + s);

	}

	@Test(expected = LazyInitializationException.class)
	public void testDetachedData2() throws SubscriberExistsException {

		long sNumber = 3;

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Subscriber s = subscriberDAO.get(sNumber);
		Entitled e = (Entitled) s;

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("\n Subscriber number " + sNumber + " is " + e);

		List<Entitled> entitles = e.getMainSubscriber().getEntitles();

		if (entitles == null)
			System.out.println("\n No entitles for " + e.getMainSubscriber());
		else
			System.out.println("\n Entitles of " + e.getMainSubscriber()
					+ " are " + entitles);
	}

	@Test
	public void testUpdateSimpleTypedValue1() throws SubscriberExistsException,
			NullPointerException, BadStringException {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Subscriber subscriber = subscriberDAO
				.GetSingleSubscriberByLastName("AIRO");
		System.out.println("Subscriber.firstName=" + subscriber.getFirstName());
		tx.commit();
		JPAUtil.closeEntityManager();

		subscriber.setFirstName("Mynewfirstname");

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		em.merge(subscriber);

		subscriber = subscriberDAO.GetSingleSubscriberByLastName("AIRO");
		System.out.println("Subscriber.firstname=" + subscriber.getFirstName());

		tx.commit();
		JPAUtil.closeEntityManager();
	}

	@Test
	public void testUpdateSimpleTypedValue2() throws SubscriberExistsException,
			NullPointerException, BadStringException, BadParametersException,
			BadBornDateException {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Subscriber subscriber = subscriberDAO
				.GetSingleSubscriberByLastName("AIRO");
		System.out.println("Before update \n Subscriber variable firstName="
				+ subscriber.getFirstName());
		tx.commit();
		JPAUtil.closeEntityManager();

		Subscriber pers = new Subscriber();
		pers.setNumber(subscriber.getNumber());
		pers.setLastName(subscriber.getLastName());
		pers.setFirstName("Mynewfirstname");
		pers.setBornDate(new GregorianCalendar());
		pers.setEmail("aaa@tb.eu");
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		em.merge(pers);
		System.out.println("After update \n firstname in database ="
				+ subscriberDAO.GetSingleSubscriberByLastName("AIRO")
						.getFirstName());
		System.out.println(" subscriber variable firstname ="
				+ subscriber.getFirstName());
		System.out.println(" pers variable firstname =" + pers.getFirstName());

		em.merge(subscriber);

		System.out.println("After 2nd update \n firstname in database ="
				+ subscriberDAO.GetSingleSubscriberByLastName("AIRO")
						.getFirstName());
		System.out.println(" subscriber variable firstname ="
				+ subscriber.getFirstName());
		System.out.println(" pers variable firstname =" + pers.getFirstName());

		tx.commit();
		JPAUtil.closeEntityManager();
	}

	@Test
	public void testManyToOnePartOfBidirectionalAssociationUpdate1()
			throws SubscriberExistsException {

		EntityManager em;
		EntityTransaction tx;

		Entitled e;
		Subscriber s1, s2;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		e = (Entitled) subscriberDAO.get(3);
		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		System.out.println("Before update\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());
		e.setMainSubscriber(s1);
		System.out.println("After update\n Subscriber " + s1 + " has entitles "
				+ s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("After commit\n Subscriber " + s1 + " has entitles "
				+ s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

	}

	@Test
	public void testManyToOnePartOfBidirectionalAssociationUpdate2()
			throws SubscriberExistsException {

		EntityManager em;
		EntityTransaction tx;

		Entitled e;
		Subscriber s1, s2;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		e = (Entitled) subscriberDAO.get(3);
		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		System.out.println("Before update\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		e.setMainSubscriber(s1);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("After update\n Subscriber " + s1 + " has entitles "
				+ s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		System.out.println("After commit and reload\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	@Test
	public void testOneTOManyPartOfBidirectionalAssociationUpdate()
			throws SubscriberExistsException {

		EntityManager em;
		EntityTransaction tx;

		Entitled e;
		Subscriber s1, s2;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		e = (Entitled) subscriberDAO.get(3);
		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		System.out.println("Before update\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		s1.addEntitled(e);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("After update\n Subscriber " + s1 + " has entitles "
				+ s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		System.out.println("After commit and reload\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	@Test(expected = LazyInitializationException.class)
	public void testAssociationUpdateOutOfTransaction()
			throws SubscriberExistsException {

		EntityManager em;
		EntityTransaction tx;

		Entitled e;
		Subscriber s1, s2;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		e = (Entitled) subscriberDAO.get(3);
		s1 = subscriberDAO.get(1);
		s2 = subscriberDAO.get(2);

		tx.commit();
		JPAUtil.closeEntityManager();

		System.out.println("After commit and Before update\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		e.setMainSubscriber(s1);

		System.out.println("After commit and After update\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();

		em.merge(e);
		em.merge(s1);
		em.merge(s2);

		System.out.println("After commit and reload\n Subscriber " + s1
				+ " has entitles " + s1.getEntitles());
		System.out.println(" Subscriber " + s2 + " has entitles "
				+ s2.getEntitles());

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	/*
	 * @Test public void testReserveBook() throws BookExistsException,
	 * SubscriberExistsException, BadParametersException, TooManyLoansException,
	 * LentBookException, LoanExistsException, BookCopyExistsException,
	 * NullPointerException, BadReturnDateException, ReservationExistsException,
	 * ReserveBookException { long sNumber = 2; Subscriber s; Book b; String
	 * bookISBN = "0-87930-568-1";
	 * 
	 * EntityManager em = JPAUtil.getEntityManager(); EntityTransaction tx =
	 * em.getTransaction();
	 * 
	 * tx.begin();
	 * 
	 * b = bookDAO.get(bookISBN); s = subscriberDAO.get(sNumber);
	 * 
	 * Reservation r = new Reservation(b,s); resDAO.add(r);
	 * 
	 * tx.commit(); JPAUtil.closeEntityManager();
	 * 
	 * 
	 * 
	 * em = JPAUtil.getEntityManager(); tx = em.getTransaction();
	 * 
	 * tx.begin(); Reservation r2 = resDAO.get(new ReservationID(b,s));
	 * System.out.println(r2.toString()); tx.commit();
	 * 
	 * JPAUtil.closeEntityManager(); }
	 * 
	 * @Test public void testCancelReserveBook() throws BookExistsException,
	 * SubscriberExistsException, BadParametersException, TooManyLoansException,
	 * LentBookException, LoanExistsException, BookCopyExistsException,
	 * NullPointerException, BadReturnDateException, ReservationExistsException,
	 * ReserveBookException { long sNumber = 2; Subscriber s; Book b; String
	 * bookISBN = "0-87930-568-1"; Reservation r;
	 * 
	 * EntityManager em = JPAUtil.getEntityManager(); EntityTransaction tx =
	 * em.getTransaction();
	 * 
	 * tx.begin();
	 * 
	 * b = bookDAO.get(bookISBN); s = subscriberDAO.get(sNumber);
	 * 
	 * r = resDAO.get(new ReservationID(b,s));
	 * 
	 * r.cancel();
	 * 
	 * em.merge(s); em.merge(r);
	 * 
	 * tx.commit(); JPAUtil.closeEntityManager();
	 * 
	 * em = JPAUtil.getEntityManager(); tx = em.getTransaction();
	 * 
	 * tx.begin();
	 * 
	 * b = bookDAO.get(bookISBN); s = subscriberDAO.get(sNumber);
	 * 
	 * r = resDAO.get(new ReservationID(b,s));
	 * 
	 * resDAO.remove(r);
	 * 
	 * tx.commit(); JPAUtil.closeEntityManager();
	 * 
	 * 
	 * 
	 * }
	 */
	@Test
	public void testReserveBook() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservationExistsException,
			ReserveBookException, TooManyReservationException, AlreadyReservedException {
		long sNumber = 2;
		Subscriber s;
		Book b;
		String bookISBN = "0-87930-568-1";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		b = bookDAO.get(bookISBN);
		s = subscriberDAO.get(sNumber);

		Reservation r = new Reservation(b, s);
		resDAO.add(r);

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		Reservation r2 = resDAO.get(new ReservationID(b, s));
		em.merge(s);
		em.merge(b);
		System.out.println(r2.toString());
		tx.commit();

		JPAUtil.closeEntityManager();
	}

	@Test(expected = ReservedBookException.class)
	public void loanReservedBook() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservationExistsException,
			ReserveBookException, TooManyReservationException,
			ReservedBookException, AlreadyReservedException, AlreadyLentBookException {
		long sNumber = 6;
		long sNumber2 = 5;
		Subscriber s;
		Subscriber s2;
		Book b;
		BookCopy bc;
		String bookISBN = "0-87930-568-1";
		String bookCopyId = "0-87930-568-1/1";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		b = bookDAO.get(bookISBN);
		s = subscriberDAO.get(sNumber);

		Reservation r = new Reservation(b, s);
		resDAO.add(r);

		em.merge(s);
		em.merge(b);
		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		bc = bookCopyDAO.get(bookCopyId);
		s2 = subscriberDAO.get(sNumber2);

		Loan l = new Loan(bc, s2);
		loanDAO.add(l);
		hisDAO.add(l.getHistory());
		em.merge(s2);
		em.merge(bc);

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	@Test
	public void testCancelReserveBook() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservationExistsException,
			ReserveBookException {
		long sNumber = 2;
		Subscriber s;
		Book b;
		String bookISBN = "0-87930-568-1";
		Reservation r;

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		b = bookDAO.get(bookISBN);
		s = subscriberDAO.get(sNumber);

		r = resDAO.get(new ReservationID(b, s));

		r.cancel();

		em.merge(s);
		em.merge(r);

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		b = bookDAO.get(bookISBN);
		s = subscriberDAO.get(sNumber);

		r = resDAO.get(new ReservationID(b, s));
		em.merge(b);
		em.merge(s);
		resDAO.remove(r);

		tx.commit();
		JPAUtil.closeEntityManager();

	}

	@Test(expected = ReserveBookException.class)
	public void testCancelCanceledReservation() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservationExistsException,
			ReserveBookException, TooManyReservationException, AlreadyReservedException {
		long sNumber = 2;
		Subscriber s;
		Book b;
		String bookISBN = "0-87930-568-1";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		b = bookDAO.get(bookISBN);
		s = subscriberDAO.get(sNumber);

		Reservation r = new Reservation(b, s);
		resDAO.add(r);

		tx.commit();
		JPAUtil.closeEntityManager();

		r.cancel();
		r.cancel();

	}

	@Test(expected = TooManyReservationException.class)
	public void testMaxReserveBook() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservationExistsException,
			ReserveBookException, TooManyReservationException, AlreadyReservedException {
		long sNumber = 2;
		Subscriber s;
		Book b1, b2, b3;
		String bookISBN1 = "0-87930-700-3";
		String bookISBN2 = "0-321-20228-4";
		String bookISBN3 = "0-87930-701-3";

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		b1 = bookDAO.get(bookISBN1);
		s = subscriberDAO.get(sNumber);

		Reservation r = new Reservation(b1, s);
		resDAO.add(r);
		em.merge(s);
		em.merge(b1);
		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		b2 = bookDAO.get(bookISBN2);
		s = subscriberDAO.get(sNumber);

		System.out.println(r);

		Reservation r2 = new Reservation(b2, s);

		resDAO.add(r2);
		em.merge(s);
		em.merge(b2);
		tx.commit();

		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();

		b3 = bookDAO.get(bookISBN3);
		s = subscriberDAO.get(sNumber);

		Reservation r3 = new Reservation(b2, s);

		resDAO.add(r3);
		em.merge(s);
		em.merge(b3);
		tx.commit();

		JPAUtil.closeEntityManager();
	}

	@Test(expected = TooManyLoansException.class)
	public void testMaxLoan() throws BookExistsException,
			SubscriberExistsException, BadParametersException,
			TooManyLoansException, LentBookException, LoanExistsException,
			BookCopyExistsException, NullPointerException,
			BadReturnDateException, ReservedBookException,
			TooManyReservationException, AlreadyReservedException, AlreadyLentBookException {
		long sNumber = 5;
		Subscriber s;
		BookCopy bc1, bc2, bc3, bc4;
		String bookCopyId1 = "0-321-20228-4/7";
		String bookCopyId2 = "0-321-20228-4/8";
		String bookCopyId3 = "0-321-20228-4/5";
		String bookCopyId4 = "0-321-20228-4/6";
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// get a lender, a book and a book copy
		bc1 = bookCopyDAO.get(bookCopyId1);
		s = subscriberDAO.get(sNumber);

		// Create loan object
		Loan l = new Loan(bc1, s);
		loanDAO.add(l);
		hisDAO.add(l.getHistory());
		em.merge(s);
		em.merge(bc1);
		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		bc2 = bookCopyDAO.get(bookCopyId2);
		s = subscriberDAO.get(sNumber);

		Loan l2 = new Loan(bc2, s);
		loanDAO.add(l2);
		hisDAO.add(l2.getHistory());
		em.merge(s);
		em.merge(bc2);

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		bc3 = bookCopyDAO.get(bookCopyId3);
		s = subscriberDAO.get(sNumber);

		Loan l3 = new Loan(bc3, s);
		loanDAO.add(l3);
		hisDAO.add(l3.getHistory());
		em.merge(s);
		em.merge(bc3);

		tx.commit();
		JPAUtil.closeEntityManager();

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();

		tx.begin();
		bc4 = bookCopyDAO.get(bookCopyId4);
		s = subscriberDAO.get(sNumber);

		Loan l4 = new Loan(bc4, s);
		loanDAO.add(l4);
		hisDAO.add(l4.getHistory());
		em.merge(s);
		em.merge(bc4);

		tx.commit();
		JPAUtil.closeEntityManager();
	}

}
