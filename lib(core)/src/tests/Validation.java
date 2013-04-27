package tests;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.hibernate.exception.ConstraintViolationException;

import jpaUtils.JPAUtil;
import exceptions.AgeCategoryExistsException;
import exceptions.AgeCategoryOverLapException;
import exceptions.AlreadyReservedException;
import exceptions.BadAgeCategoryException;
import exceptions.BadBornDateException;
import exceptions.BadParametersException;
import exceptions.BadReturnDateException;
import exceptions.BadStringException;
import exceptions.BookCopyExistsException;
import exceptions.BookExistsException;
import exceptions.EmailExistsException;
import exceptions.LentBookException;
import exceptions.LoanExistsException;
import exceptions.ReservationExistsException;
import exceptions.ReserveBookException;
import exceptions.ReservedBookException;
import exceptions.SubscriberExistsException;
import exceptions.SubscriberWithLoansException;
import exceptions.TooManyLoansException;
import exceptions.TooManyReservationException;
import loansManagement.History;
import loansManagement.HistoryDAO;
import loansManagement.Loan;
import loansManagement.LoanDAO;
import main.VerifyThread;
import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import reservationManagement.ReservationID;
import subscribersManagement.AgeCategory;
import subscribersManagement.AgeCategoryDAO;
import subscribersManagement.Entitled;
import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;
import booksManagement.Book;
import booksManagement.BookCopy;
import booksManagement.BookCopyDAO;
import booksManagement.BookDAO;

public class Validation {


	/**
	 * @param args
	 * @throws BadParametersException 
	 */
	public static void main(String[] args){
		
//		Timer t = new Timer();
//		t.schedule(new VerifyThread(),5000,5000);
		//addAgeCategory();
		//modifierCategory();
		//supprimerAgeCategory();
		
		addSubscriber();
		//modifierSubscriber();
		//supprimerSubscriber();
		//
		//ListerSubscriber();
		
     	addBook();
		//supprimerBook();
		//modifierBook();
		//listerBook();
		//rechercherBook();
		
	    addCopy();
	    //reservations();
	//	supprimerCopy();
		//rechercherCopy();
	    loans();
//		returnLoan();
//		listOfRetard();
//	    listOfHistory();
//		listOfLoans();
//		listOfRetard();
		
//		reservations();
//		listOfReservations();
//		cancelReservation();

		
	}
	public static void loans(){
		loan(3,"0-321-20228-4/1");

	}
	public static void returnLoan(){
		listOfLoans();
		returnBookCopy(null);
		returnBookCopy("0-87930-568-1/10000");
		returnBookCopy("0-87930-568-1/1");
		returnBookCopy("0-87930-568-1/1");
		returnBookCopy("");
	
		listOfLoans();
	}
	
	public static void listOfRetard(){
		for (History h : getRetard()){
			System.out.println(h);
		}
	}

	public static void listOfHistory(){
		for (History h : getHistory()){
			System.out.println(h);
		}
	}
	public static void listOfLoans(){
		for (Loan h : getLoans()){
			System.out.println(h);
		}
	}
	public static void reservations(){
		reserve(1,"0-321-20228-4");
		reserve(2,"0-321-20228-4");
	}
	public static void cancelReservation(){
		cancelReservation(2,"0-87930-568-1");
		cancelReservation(1,"0-87930-568-1");
		cancelReservation(10000000,"0-87930-568-1");
		cancelReservation(10000000,null);
	}
	public static void listOfReservations(){
		for (Reservation h : getReservations()){
			System.out.println(h);
		}
	}
	
	/////////////////////////////////////////////// Jalon 2 ////////////////////////////////////////////////////////
	public static void cancelReservation(long subscriberID, String  isbn ){
		Reservation r = null;
		SubscriberDAO sDAO = new SubscriberDAO();
		ReservationDAO rDAO = new ReservationDAO();
		BookDAO bDAO = new BookDAO();

		Subscriber s = null;
		Book b = null;
		
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
	
		try {
			b = bDAO.get(isbn);
			s = sDAO.get(subscriberID);
			r = rDAO.get(new ReservationID(b,s));
			
			r.cancel();
			em.merge(b);
			em.merge(s);
			System.out.println(rDAO.remove(r));
		} catch (BookExistsException e) {
			System.out.println("Aucun livre n'est trouvé");
		} catch (ReservationExistsException e) {
			System.out.println("Aucune réservation n'est trouvé");
		} catch (ReserveBookException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées!!!!!!!");
		} catch (NullPointerException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		}catch (SubscriberExistsException e) {
			System.out.println("Aucun abonné n'est trouvé");
		} catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}
		
		tx.commit();
		JPAUtil.closeEntityManager();
	}
	
	public static Reservation reserve(long subsID, String  isbn ){
		Reservation r = null;
		SubscriberDAO sDAO = new SubscriberDAO();
		ReservationDAO rDAO = new ReservationDAO();
		BookDAO bcDAO = new BookDAO();

		Subscriber s = null;
		Book b = null;
		
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			s = sDAO.get(subsID);
			b = bcDAO.get(isbn);
			r = new Reservation(b,s);
			
			rDAO.add(r);
			em.merge(s);
			em.merge(b);
			
			System.out.println(r.getFinishDate().getTime());
			
		}catch (TooManyReservationException aaaaaaaaaae){
			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota de reservation");
		}catch (AlreadyReservedException re){
			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja reservé ce livre");
		}catch (SubscriberExistsException pe) {
			System.out.println("Aucun abonné n'est trouvé");
		}catch (BookExistsException pe) {
			System.out.println("Aucun livre n'est trouvé");
	    }catch (NullPointerException e) {
		     System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
		}
		
		tx.commit();
		JPAUtil.closeEntityManager();
		
		return r;
	}
	
	public static List<Reservation> getReservations(){
		ReservationDAO rDAO = new ReservationDAO();
		List<Reservation> rs = new ArrayList<Reservation>();
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
			rs = rDAO.getContent();
		tx.commit();
		JPAUtil.closeEntityManager();
		return rs;
	}
	
	public static List<History> getHistory(){
		HistoryDAO hDAO = new HistoryDAO();
		List<History> hs = new ArrayList<History>();
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
			hs = hDAO.getContent();
		tx.commit();
		JPAUtil.closeEntityManager();
		return hs;
	}
	
	public static List<Loan> getLoans(){
		LoanDAO lDAO = new LoanDAO();
		List<Loan> ls = new ArrayList<Loan>();
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
			ls = lDAO.getContent();
		tx.commit();
		JPAUtil.closeEntityManager();
		return ls;
	}
	
	public static List<History> getRetard(){
		HistoryDAO hDAO = new HistoryDAO();
		List<History> hs = new ArrayList<History>();
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
			hs = hDAO.getRetard();
		tx.commit();
		JPAUtil.closeEntityManager();
		return hs;
	}
	
	public static void returnBookCopy(String  copyID){
		SubscriberDAO sDAO = new SubscriberDAO();
		LoanDAO lDAO = new LoanDAO();
		BookCopyDAO bcDAO = new BookCopyDAO();
//		HistoryDAO hDAO = new HistoryDAO();
		Loan l = null;
		Subscriber s = null;
		BookCopy bc = null;
		
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
	
	
		try {
			bc = bcDAO.get(copyID);
			s = sDAO.get(bc.getLoan().getLender().getNumber());
			
			
			l = lDAO.get(bc.getLoan().getLoanID());
			l.returnBook();
			
			
			em.merge(l.getHistory());
			lDAO.remove(l);
			em.merge(s);
			em.merge(bc);
		} catch (BookCopyExistsException e) {
			System.out.println("Aucun exemplaire n'est trouvé");
		} catch (LoanExistsException e) {
			System.out.println("Aucun emprunt n'est trouvé");
		} catch (BadParametersException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		} catch (NullPointerException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		}catch (LentBookException e) {
			System.out.println(e.getMessage());
		} catch (SubscriberExistsException e) {
			System.out.println("Aucun abonné n'est trouvé");
		} catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
	    }
		
		tx.commit();
		JPAUtil.closeEntityManager();
	}
	public static Loan loan(long lenderID, String  copyID ){
		Loan l = null;
		SubscriberDAO sDAO = new SubscriberDAO();
		LoanDAO lDAO = new LoanDAO();
		BookCopyDAO bcDAO = new BookCopyDAO();
		HistoryDAO hDAO = new HistoryDAO();
		Subscriber s = null;
		BookCopy bc = null;
		
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			s = sDAO.get(lenderID);
			bc = bcDAO.get(copyID);
			l = new Loan(bc,s);
			
			lDAO.add(l);
			hDAO.add(l.getHistory());
			em.merge(s);
			em.merge(bc);
			
			System.out.println(l.getReturnDate().getTime());
			
		}catch (TooManyLoansException aaaaaaaaaae){
			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota d'emprunt");
		}catch (LentBookException le){
//			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja emprunté ce livre");
			System.out.println(le.getMessage());
		}catch (ReservedBookException re){
			System.out.println("Le nombre de réservation est suppérieur au nombre des exemplaires");
		}catch (SubscriberExistsException pe) {
			System.out.println("Aucun abonné n'est trouvé");
		}catch (BookCopyExistsException pe) {
			System.out.println("Aucun exemplaire n'est trouvé");
	    }catch (BadParametersException pe) {
	    	System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		 }catch (NullPointerException e) {
		     System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
		}
		
		tx.commit();
		JPAUtil.closeEntityManager();
		
		return l;
	}
	///////////////////////////////////////////// Jalon 1 ///////////////////////////////////////////////////////////////
	public static void addSubscriber(){
		Subscriber s1 = addSubscriber("Vincent", "AIRO",new GregorianCalendar(1960, 15,32),"s1@tb.eu");
		Subscriber s2 = addSubscriber("Betina", "CHTILFIZ",new GregorianCalendar(1963, 02, 25),"s2@tb.eu");
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}

	    Entitled s3 = addEntitled("Victor", "CHTILFIZ",new GregorianCalendar(1997, 02, 20),"s3@tb.eu", s2);
		Entitled s4 = addEntitled("Luisa", "CHTILFIZ",new GregorianCalendar(1993, 07, 30),"s4@tb.eu", s2);
		
		addSubscriber("Vincent", "AAAA" ,null,"s1@tb.eu");
		addSubscriber("Vincent", "AIRO" ,new GregorianCalendar(1960, 12, 13),"s1@tb.eu");
		addSubscriber("Vincent", "AIRO" ,new GregorianCalendar(2050, 12, 10),"s1@tb.eu");
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}
	}
	public static void supprimerSubscriber(){
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}

		removeSubscriber(1);
		removeSubscriber(1);
		removeSubscriber(100000);
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}
	}
  public static void modifierSubscriber(){
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}

		modifySubscriber(1,"AAAA", "BBB",new GregorianCalendar(1960, 12, 13),"yfghfghfgfhfg","adulte");
		modifySubscriber(1000,"Vincent", "AIRO",new GregorianCalendar(1960, 12, 13),"s1@tb.eu","adulte1");
		
		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}
	}
  public static void modifierBook(){
		
		for (Book s : bookList()){
			System.out.println(s);
		}
		ArrayList<String> authors1 = new ArrayList<String>();
		
		authors1.add("Navathe");

		System.out.println("test");
		modifyBook("Fundamentals of Database Systems", authors1,"0-321-20228-4", new GregorianCalendar());
		modifyBook("Fundamentals of Database Systems", authors1,"0-321-20228-4", new GregorianCalendar());
		
		for (Book s : bookList()){
			System.out.println(s);
		}
	}
	public static void ListerSubscriber(){

		for (Subscriber s : SubscriberList()){
			System.out.println(s);
		}

	}
	public static void addAgeCategory(){
		AgeCategory ageCategory0 = addAgeCategory("enfant",0,18);
		addAgeCategory("adulte",18,100);
		
		System.out.println(ageCategory0);

		addAgeCategory(null,0,18);
		addAgeCategory("aaa",20,18);
		addAgeCategory("jeune",15,30);
		addAgeCategory("enfant",0,18);
	}
	public static void modifierCategory(){
		modifyAgeCategory("enfant",17,0);
		modifyAgeCategory("adult",18,200);
	}
	public static void supprimerAgeCategory(){
		removeAgeCategory("yyyy");
		removeAgeCategory("enfant");
		removeAgeCategory("enfant");
	}
	public static void addBook(){
		ArrayList<String> authors1 = new ArrayList<String>();
		ArrayList<String> authors2 = new ArrayList<String>();
		
		authors1.add("Navathe");
		authors2.add("Peter Gulutzan");
		authors2.add("Trudy Pelzer");
		addBook("Fundamentals of Database Systems", authors1,new GregorianCalendar(), "0-321-20228-4");
		addBook("SQL-99 Complete, Really", authors2, new GregorianCalendar(), "0-87930-568-1");
		addBook("SQL-99 Complete, Really", authors2, new GregorianCalendar(), "0-87930-568-1");
		addBook("SQL-99 Complete, Really", null, new GregorianCalendar(), "0-87930-568-1");
	}
	public static void supprimerBook(){
		for (Book s : bookList()){
			System.out.println(s);
		}
		
		removeBook("0-87930-568-1");
		removeBook("0000000000000");
		
		for (Book s : bookList()){
			System.out.println(s);
		}
	}
	public static void listerBook(){
		for (Book s : bookList()){
			System.out.println(s);
		}
	}
	public static void addCopy(){
		ArrayList<String> authors1 = new ArrayList<String>();
		ArrayList<String> authors2 = new ArrayList<String>();
		
		authors1.add("Navathe");
		authors2.add("Peter Gulutzan");
		authors2.add("Trudy Pelzer");
		Book b1 = null;
		Book b2 = null;
		try {
			b1 = new Book("Fundamentals of Database Systems", authors1,new GregorianCalendar(), "0-321-20228-4");
			b2 = new Book("SQL-99 Complete, Really", authors2, new GregorianCalendar(), "0-87930-568-1");
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addBookCopy(b1);
 		addBookCopy(b2);
 		//addBookCopy(b2);
 		addBookCopy(null);
	}
	public static void supprimerCopy(){
		removeBookCopy("0-321-20228-4/2");
 		removeBookCopy("0-321-20228-4/5");
	}
	public static void rechercherCopy(){
		System.out.println(SearchBookCopy("0-321-20228-4/1"));
		System.out.println(SearchBookCopy("0-87930-568-1/1"));
//		System.out.println(SearchBookCopy(null));
		System.out.println(SearchBookCopy(""));
	}
	public static void rechercherBook(){
		System.out.println(SearchBookByAuthor("Navathe"));
		System.out.println(SearchBookByTitle("Fundamentals of Database Systems"));
		System.out.println(SearchBook("0-321-20228-4"));
		
		SearchBookByTitle("aaaa");
		SearchBook("0-87930-568-1");
	}
	/*public static void rechercherSubscriber(){
		System.out.println(SearchSubscriber(2));
		for (Subscriber s : SearchSubscriber("Victor", "Chtilfiz",new GregorianCalendar(1997, 02, 20))){
			System.out.println(s);
		}
	}*/
	
	
	public static AgeCategory addAgeCategory(String name,int min,int max){
		AgeCategory ageCategory=null ;
		AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			ageCategory= new AgeCategory(name, min, max);
			ageCategoryDAO.add(ageCategory);
			System.out.println("La catégorie d'age "+name+" est crée");
		} catch (AgeCategoryOverLapException e) {
				System.out.println("Imbrication avec une catégorie déjà existante.");
		} catch (AgeCategoryExistsException e) {
				System.out.println("Catégorie déjà existante.");
		} catch (BadParametersException e) {
				System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		} catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }
		
		tx.commit();
		JPAUtil.closeEntityManager();
		
		return ageCategory;
	}
	
	public static void modifyAgeCategory(String name,int min,int max){
		AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			ageCategoryDAO.modify(name, max, min);
			System.out.println("Modification de la catégorie d'age "+name);
		}catch(BadParametersException e){
			System.out.println("Max et inferieur au Min");
		}
		catch (AgeCategoryOverLapException e) {
				System.out.println("Imbrication avec une catégorie déjà existante.");
		} catch (AgeCategoryExistsException e) {
			System.out.println("Catégorie d'age non existante");
	    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }
		
		tx.commit();
		JPAUtil.closeEntityManager();
		
	}
	
public static void removeAgeCategory(String name){
		AgeCategory ageCategory = new AgeCategory();
		AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			ageCategory= ageCategoryDAO.get(name);
			ageCategoryDAO.remove(ageCategory);
			System.out.println("Suppression de la catégorie d'age "+name);
		}catch (AgeCategoryExistsException pe) {
			System.out.println("Système ne trouve pas la catégorie " + name);
		}catch (SubscriberExistsException pe) {
				System.out.println("Au moins un abonné existe dans la catégorie "+name);
	    }catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}
		
		tx.commit();
		JPAUtil.closeEntityManager();
		
	}
public static AgeCategory SearchAgeCategory(String name){
	AgeCategory ageCategory = new AgeCategory();
	AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		ageCategory= ageCategoryDAO.get(name);
		
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return ageCategory;
}
public static Subscriber addSubscriber(String firstName, String lastName, Calendar bornDate,String email){
	Subscriber subscriber = new Subscriber();
	SubscriberDAO subscriberDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		subscriber = new Subscriber(firstName, lastName, bornDate, email);
		subscriberDAO.add(subscriber);
		System.out.println("L'abonnée "+subscriber.getNumber()+" est crée");
	}
    catch(NullPointerException e){
    	System.out.println("Une ou plusieurs entrées sont nulles");
    }
	catch(EmailExistsException e){
		System.out.println("Email existe déja");
	}
	catch(BadStringException e){
		System.out.println("Format nom ou prénom non valide");
	}
	catch(BadBornDateException e){
		System.out.println("Date de naissance incorrecte");
	}
	catch (SubscriberExistsException e) {
			System.out.println("Abonné déjà existant.");
	} catch (BadParametersException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return subscriber;
}
public static Entitled addEntitled(String firstName, String lastName, Calendar bornDate,String email,Subscriber s){
	Entitled subscriber = new Entitled();
	SubscriberDAO subscriberDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		Subscriber ss = subscriberDAO.get(s.getNumber());
		subscriber = new Entitled(firstName, lastName, bornDate, email,ss);
		subscriberDAO.add(subscriber);
	} catch(NullPointerException e){
    	System.out.println("Une ou plusieurs entrées sont nulles");
    }catch(EmailExistsException e){
		System.out.println("Email existe déja");
	}
	catch(BadStringException e){
		System.out.println("Format nom ou prénom non valide");
	}
	catch(BadBornDateException e){
		System.out.println("Date de naissance incorrecte");
	}catch (SubscriberExistsException e) {
			System.out.println("Abonné déjà existant.");
	} catch (BadParametersException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return subscriber;
}	
public static void modifySubscriber(long number, String firstName,String lastName, Calendar bornDate,String email, String ageCategory){
	SubscriberDAO subscriberDAO=new SubscriberDAO();
	AgeCategoryDAO agDAO=new AgeCategoryDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		AgeCategory ag = agDAO.get(ageCategory);
		subscriberDAO.modify(number, firstName, lastName, bornDate, email, ag);
		System.out.println("Modification de l'abonné ("+ firstName + " " + lastName+") est réussie");
	}catch (AgeCategoryExistsException e) {
		System.out.println("Catégorie non existante.");
	}catch(NullPointerException e){
    	System.out.println("Une ou plusieurs entrées sont nulles");
    }
	catch(EmailExistsException e){
		System.out.println("Email existe déja");
	}
	catch(BadStringException e){
		System.out.println("Format nom ou prénom non valide");
	}
	catch(BadBornDateException e){
		System.out.println("Date de naissance incorrecte");
	} catch (SubscriberExistsException e) {
			System.out.println("Abonné non existant.");
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}

public static void removeSubscriber(long id){
	Subscriber s = new Subscriber();
	SubscriberDAO subsDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		 s= subsDAO.get(id);
		subsDAO.remove(s);
		System.out.println("Suppression de l'abonné ("+ s.getFirstName() + " " + s.getLastName()+") est réussie");
	}catch (SubscriberExistsException pe) {
		System.out.println("Système ne trouve pas l'abonné " + id);
	}catch (SubscriberWithLoansException pe) {
			System.out.println("L'abonné "+ "(" + s.getFirstName() + " " + s.getLastName()+") a des emprunts en cours ");
    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}	
public static ArrayList<Subscriber> SubscriberList(){
	SubscriberDAO subsDAO=new SubscriberDAO();
	ArrayList<Subscriber> subs = new ArrayList<Subscriber>();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		 subs = (ArrayList<Subscriber>) subsDAO.getContent();
	}catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return subs;
}	

public static Subscriber SearchSubscriber(long id){
	Subscriber s = new Subscriber();
	SubscriberDAO subsDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		s= subsDAO.get(id);
		
	}  catch(SubscriberExistsException pe)
	{
		System.out.println("Abonné n'existe pas");
	}
	catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return s;
}

public static ArrayList<Subscriber> SearchSubscriber(String firstName,String lastName, Calendar bornDate){
	ArrayList<Subscriber> s= new ArrayList<Subscriber>();
	SubscriberDAO subsDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		s= (ArrayList<Subscriber>) subsDAO.get(firstName,lastName,bornDate);
	} catch(SubscriberExistsException pe)
	{
		System.out.println("Abonné n'existe pas");
	}catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return s;
}
public static BookCopy addBookCopy(Book book){
	BookCopy b = new BookCopy();
	BookCopyDAO bookCopyDAO=new BookCopyDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		BookDAO bdao = new BookDAO();
		Book boo = bdao.get(book.getIsbn());
		b= new BookCopy(boo);
		bookCopyDAO.add(b);
		System.out.println("L'exemplaire "+b.getId()+"est crée");
	}  catch (BookExistsException e) {
			System.out.println("Livre n'existe pas.");
	} catch (BadParametersException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	}catch (NullPointerException e) {
		System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
     }  catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}


public static void removeBookCopy(String id){
	BookCopy b = new BookCopy();
	BookCopyDAO bookCopyDAO=new BookCopyDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b = bookCopyDAO.get(id);
		bookCopyDAO.remove(b);
		System.out.println("Suppression de l'exemplaire "+id+" est réussie");
	}catch (BookCopyExistsException pe) {
		System.out.println("Système ne trouve pas l'exemplaire");
    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}
public static BookCopy SearchBookCopy(String id){
	BookCopy b = new BookCopy();
	BookCopyDAO  bookCopyDAO=new BookCopyDAO();
	EntityManager em;
	EntityTransaction tx;
	
	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b= bookCopyDAO.get(id);
		
	} catch (BookCopyExistsException pe) {
		System.out.println("Aucun exemplaire n'est trouvé");
	}catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return b;
}

/*public static ArrayList<BookCopy> bookCopyList(){
	BookCopyDAO bookCopyDAO=new BookCopyDAO();
	ArrayList<BookCopy> b = new ArrayList<BookCopy>();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		 b = (ArrayList<BookCopy>) bookCopyDAO.getContent();
	}catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}	*/

public static Book addBook(String title, ArrayList<String> authors, Calendar editionDate, String isbn){
	Book b = new Book();
	BookDAO bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b= new Book(title,authors,editionDate,isbn);
		bookDAO.add(b);
		System.out.println("Ajout du livre "+isbn+" est réussie");
	} catch (BookExistsException e) {
			System.out.println("Livre déjà existant.");
	} catch (BadParametersException e) {
			System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	} catch (NullPointerException e) {
		System.out.println("Une ou plusieurs entrées sont nulles");
   }  catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}

public static void modifyBook(String isbn,ArrayList<String> authors, String title, Calendar editionDate){
	BookDAO bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		bookDAO.modify(isbn,authors,title,editionDate);
		System.out.println("Modification du livre ("+isbn+","+title+") est réussie");
	}catch (BookExistsException e) {
		System.out.println("livre non existant");
    }catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}

public static void removeBook(String isbn){
	Book b = new Book();
	BookDAO bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b = bookDAO.get(isbn);
		bookDAO.remove(b);
		System.out.println("Suppression du livre ("+isbn+","+b.getTitle()+") est réussie");
	}catch (BookExistsException pe) {
		System.out.println("Système ne trouve pas le livre");
	}catch (LentBookException pe) {
			System.out.println("Au moins un exemplaire est emprunté");
    }catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}
public static Book SearchBook(String isbn){
	Book b = new Book();
	BookDAO  bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;
	
	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b= bookDAO.get(isbn);
		
	} catch (BookExistsException pe) {
		System.out.println("Aucun livre n'est trouvé");
	}catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return b;
}

public static ArrayList<Book> SearchBookByTitle(String titre){
	ArrayList <Book> b = new ArrayList<Book>();
	BookDAO  bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;
	
	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b= (ArrayList<Book>) bookDAO.getByTitre(titre);
		
	} catch (BookExistsException pe) {
		System.out.println("Aucun livre n'est trouvé");
	}catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return b;
}

public static ArrayList<Book> SearchBookByAuthor(String author){
	ArrayList <Book> b = new ArrayList<Book>();
	BookDAO  bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;
	
	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b= (ArrayList<Book>) bookDAO.getByAuthor(author);
	} catch (BookExistsException pe) {
		System.out.println("Aucun livre n'est trouvé");
	}catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	return b;
}

public static ArrayList<Book> bookList(){
	BookDAO bookDAO=new BookDAO();
	ArrayList<Book> b = new ArrayList<Book>();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		 b = (ArrayList<Book>) bookDAO.getContent();
	}catch (Exception pe) {
		System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}	
}
