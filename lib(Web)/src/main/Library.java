package main;

/*
 * Class offering the main functions of the library
 * @author M.T. Segarra
 * @version 0.0.1
 */

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;



import jpaUtils.JPAUtil;

import loansManagement.History;
import loansManagement.HistoryDAO;
import loansManagement.Loan;
import loansManagement.LoanDAO;
import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import reservationManagement.ReservationID;
import subscribersManagement.AgeCategory;
import subscribersManagement.AgeCategoryDAO;
import subscribersManagement.Entitled;
import subscribersManagement.Subscriber;
import booksManagement.Book;
import booksManagement.BookCopy;
import booksManagement.BookCopyDAO;
import exceptions.*;
import subscribersManagement.SubscriberDAO;
import booksManagement.BookDAO;

public class Library {

	public  void cancelReservation(long subscriberID, String  isbn ){
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
			///System.out.println(rDAO.remove(r));
			rDAO.remove(r);
		} catch (BookExistsException e) {
			//System.out.println("Aucun livre n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (ReservationExistsException e) {
			//System.out.println("Aucune réservation n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (ReserveBookException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées!!!!!!!");
			 JPAUtil.closeEntityManager();
		} catch (NullPointerException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées")
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException e) {
			//System.out.println("Aucun abonné n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
			//System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}
			tx.commit();
			JPAUtil.closeEntityManager();
	
		
	}
	
	public Reservation reserve(long subsID, String  isbn ){
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
			
			//System.out.println(r.getFinishDate().getTime());
		}catch (TooManyReservationException aaaaaaaaaae){
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota de reservation");
			
			 JPAUtil.closeEntityManager();
		}catch (AlreadyReservedException re){
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja reservé ce livre");
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
			//System.out.println("Aucun abonné n'est trouvé");
			 JPAUtil.closeEntityManager();
		}catch (BookExistsException pe) {
			//System.out.println("Aucun livre n'est trouvé");
			 JPAUtil.closeEntityManager();
	    }catch (NullPointerException e) {
		    // System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
	    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
		}

			tx.commit();
			JPAUtil.closeEntityManager();
		
		
		return r;
	}
	
	public List<Reservation> getReservations(){
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
	
	public List<History> getHistory(){
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
	
	public  List<Loan> getLoans(){
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
	
	public  List<AgeCategory> getAgeCategories(){
		AgeCategoryDAO agDAO = new AgeCategoryDAO();
		List<AgeCategory> ags = new ArrayList<AgeCategory>();
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
			ags = agDAO.getContent();
		tx.commit();
		JPAUtil.closeEntityManager();
		return ags;
	}
	
	public  List<History> getRetard(){
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
	
	public void returnBookCopy(String  copyID){
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
			//System.out.println("Aucun exemplaire n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (LoanExistsException e) {
			//System.out.println("Aucun emprunt n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
		} catch (NullPointerException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
		}catch (LentBookException e) {
			//System.out.println(e.getMessage());
			 JPAUtil.closeEntityManager();
		} catch (SubscriberExistsException e) {
			//System.out.println("Aucun abonné n'est trouvé");
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
			//System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
	    }
		
			tx.commit();
			JPAUtil.closeEntityManager();
	
		
	}
	public  Loan loan(long lenderID, String  copyID ){
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
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota d'emprunt");
			 JPAUtil.closeEntityManager();
		}catch (LentBookException le){
//			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja emprunté ce livre");
			//System.out.println(le.getMessage());
			 JPAUtil.closeEntityManager();
		}catch (ReservedBookException re){
			//System.out.println("Le nombre de réservation est suppérieur au nombre des exemplaires");
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
//			System.out.println("Aucun abonné n'est trouvé");
			 JPAUtil.closeEntityManager();
		}catch (BookCopyExistsException pe) {
			//System.out.println("Aucun exemplaire n'est trouvé");
			JPAUtil.closeEntityManager();
	    }catch (BadParametersException pe) {
	    	//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
		 }catch (NullPointerException e) {
		     //System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
	
	    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
		}

			tx.commit();
			JPAUtil.closeEntityManager();
		
		
		
		return l;
	}
	
	
	///////////////////////jalon 1///////////////////////
	public  AgeCategory addAgeCategory(String name,int min,int max){
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
			//System.out.println("La catégorie d'age "+name+" est crée");
			
		} catch (EmptyStringException e) {
			//System.out.println("Imbrication avec une catégorie déjà existante.");

		 JPAUtil.closeEntityManager();
	     }  catch (AgeCategoryOverLapException e) {
				//System.out.println("Imbrication avec une catégorie déjà existante.");
			 JPAUtil.closeEntityManager();
		} catch (AgeCategoryExistsException e) {
				//System.out.println("Age category not found !");
			 JPAUtil.closeEntityManager();
		} catch (BadParametersException e) {
				//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
		//System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }

			tx.commit();
			JPAUtil.closeEntityManager();
	
		
		
		return ageCategory;
	}
	
	
	
	
	public  void modifyAgeCategory(String name,int min,int max){
		AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			ageCategoryDAO.modify(name, max, min);
		//	System.out.println("Modification de la catégorie d'age "+name);
			 JPAUtil.closeEntityManager();
		}catch(BadParametersException e){
			//System.out.println("Max et inferieur au Min");
			 JPAUtil.closeEntityManager();
		}
		catch (AgeCategoryOverLapException e) {
			//	System.out.println("Imbrication avec une catégorie déjà existante.");
			 JPAUtil.closeEntityManager();
		} catch (AgeCategoryExistsException e) {
			//System.out.println("Catégorie d'age non existante");
			 JPAUtil.closeEntityManager();
	    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }
			tx.commit();
			JPAUtil.closeEntityManager();
	
	}
	
public void removeAgeCategory(String name){
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
			//System.out.println("Suppression de la catégorie d'age "+name);
		}catch (AgeCategoryExistsException pe) {
			//System.out.println("Système ne trouve pas la catégorie " + name);
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
				//System.out.println("Au moins un abonné existe dans la catégorie "+name);
			 JPAUtil.closeEntityManager();
	    }catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}

		tx.commit();
		JPAUtil.closeEntityManager();
	}
public  AgeCategory SearchAgeCategory(String name){
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
public  Subscriber addSubscriber(String firstName, String lastName, Calendar bornDate,String email){
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
		//System.out.println("L'abonnée "+subscriber.getNumber()+" est crée");
	}
    catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");
		 JPAUtil.closeEntityManager();
    }
	catch(EmailExistsException e){
		//System.out.println("Email existe déja");
		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");
		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");
		 JPAUtil.closeEntityManager();
	}
	catch (SubscriberExistsException e) {
			//System.out.println("Abonné déjà existant.");
		 JPAUtil.closeEntityManager();
		 
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return subscriber;
}
public  Entitled addEntitled(String firstName, String lastName, Calendar bornDate,String email,long s){
	Entitled subscriber = new Entitled();
	SubscriberDAO subscriberDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		Subscriber ss = subscriberDAO.get(s);
		subscriber = new Entitled(firstName, lastName, bornDate, email,ss);
		subscriberDAO.add(subscriber);
	} catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");

		 JPAUtil.closeEntityManager();
    }catch(EmailExistsException e){
		//System.out.println("Email existe déja");

		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");

		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");

		 JPAUtil.closeEntityManager();
	}catch (SubscriberExistsException e) {
			//System.out.println("Abonné déjà existant.");

		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");

		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	System.err.println("Exception when persisting data");
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return subscriber;
}	
public void modifySubscriber(long number, String firstName,String lastName, Calendar bornDate,String email, String ageCategory){
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
		//System.out.println("Modification de l'abonné ("+ firstName + " " + lastName+") est réussie");

	}catch (AgeCategoryExistsException e) {
		//System.out.println("Catégorie non existante.");

		 JPAUtil.closeEntityManager();
	}catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");

		 JPAUtil.closeEntityManager();
    }
	catch(EmailExistsException e){
		//System.out.println("Email existe déja");

		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");

		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");.

		 JPAUtil.closeEntityManager();
	} catch (SubscriberExistsException e) {
			//System.out.println("Abonné non existant.");

		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	//System.err.println("Exception when persisting data");

	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}

public void removeSubscriber(long id){
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
		//System.out.println("Suppression de l'abonné ("+ s.getFirstName() + " " + s.getLastName()+") est réussie");

	}catch (SubscriberExistsException pe) {
		//System.out.println("Système ne trouve pas l'abonné " + id);

		 JPAUtil.closeEntityManager();
	}catch (SubscriberWithLoansException pe) {
		//	System.out.println("L'abonné "+ "(" + s.getFirstName() + " " + s.getLastName()+") a des emprunts en cours ");

			 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
//		System.err.println("Exception when persisting data");

		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}	
public ArrayList<Subscriber> SubscriberList(){
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

public  Subscriber SearchSubscriber(long id){
	Subscriber s = null;
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
		//System.out.println("Abonné n'existe pas");

		 JPAUtil.closeEntityManager();
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

public ArrayList<Subscriber> SearchSubscriber(String firstName,String lastName, Calendar bornDate){
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
public  BookCopy addBookCopy(String isbn){
	BookCopy b = new BookCopy();
	BookCopyDAO bookCopyDAO=new BookCopyDAO();
	BookDAO bdao = new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		Book book = bdao.get(isbn);
		b= new BookCopy(book);
		bookCopyDAO.add(b);
		//System.out.println("L'exemplaire "+b.getId()+"est crée");

		
	}  catch (BookExistsException e) {
			//System.out.println("Livre n'existe pas.");

		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");

		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	//System.err.println("Exception when persisting data");

	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}


public void removeBookCopy(String id){
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
		//System.out.println("Suppression de l'exemplaire "+id+" est réussie");

	}catch (BookCopyExistsException pe) {
		//System.out.println("Système ne trouve pas l'exemplaire");

		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");

		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}
public BookCopy SearchBookCopy(String id){
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

public Book addBook(String title, ArrayList<String> authors, Calendar editionDate, String isbn){
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
		//System.out.println("Ajout du livre "+isbn+" est réussie");

	} catch (BookExistsException e) {
			//System.out.println("Livre déjà existant.");

		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");

		 JPAUtil.closeEntityManager();
	} catch (NullPointerException e) {
		//System.out.println("Une ou plusieurs entrées sont nulles");

		 JPAUtil.closeEntityManager();
   }  catch (Exception pe) {
	//System.err.println("Exception when persisting data");

	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}

public void modifyBook(String isbn,ArrayList<String> authors, String title, Calendar editionDate){
	BookDAO bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		bookDAO.modify(isbn,authors,title,editionDate);
		//System.out.println("Modification du livre ("+isbn+","+title+") est réussie");

	}catch (BookExistsException e) {
		//System.out.println("livre non existant");

		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
	//System.err.println("Exception when persisting data");
 
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}

public void removeBook( String isbn){
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
		//System.out.println("Suppression du livre ("+isbn+","+b.getTitle()+") est réussie");

	}catch (BookExistsException pe) {
		//System.out.println("Système ne trouve pas le livre");

		 JPAUtil.closeEntityManager();
	}catch (LentBookException pe) {
			//System.out.println("Au moins un exemplaire est emprunté");

		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");

		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}
public Book SearchBook(String isbn){
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

public ArrayList<Book> SearchBookByTitle(String titre){
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

public ArrayList<Book> SearchBookByAuthor(String author){
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

public ArrayList<Book> bookList(){
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
