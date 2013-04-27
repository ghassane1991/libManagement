package main;

/*
 * Class offering the main functions of the library
 * @author M.T. Segarra
 * @version 0.0.1
 */

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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

	public  void cancelReservation(Shell shell,long subscriberID, String  isbn ){
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
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			 messageBox.setMessage("Reservation is canceled");
			 messageBox.setText("");
			 messageBox.open();
		} catch (BookExistsException e) {
			//System.out.println("Aucun livre n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Book not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (ReservationExistsException e) {
			//System.out.println("Aucune réservation n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Reservation not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (ReserveBookException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées!!!!!!!");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (NullPointerException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException e) {
			//System.out.println("Aucun abonné n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Subscriber not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
			//System.err.println("Exception when persisting data");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}
			tx.commit();
			JPAUtil.closeEntityManager();
	
		
	}
	
	public Reservation reserve(Shell shell,long subsID, String  isbn ){
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
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			 messageBox.setMessage(r.getFinishDate().getTime().toString());
			 messageBox.setText("");
			 messageBox.open();
		}catch (TooManyReservationException aaaaaaaaaae){
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota de reservation");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") reached its quota of reservation");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (AlreadyReservedException re){
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja reservé ce livre");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") already reserve this book");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
			//System.out.println("Aucun abonné n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Subscriber not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (BookExistsException pe) {
			//System.out.println("Aucun livre n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Book not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
	    }catch (NullPointerException e) {
		    // System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	    	 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
	    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
	    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
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
	
	public void returnBookCopy(Shell shell,String  copyID){
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
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Book Copy not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (LoanExistsException e) {
			//System.out.println("Aucun emprunt n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Loan not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Error when entering data!");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (LentBookException e) {
			//System.out.println(e.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage(e.getMessage());
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (NullPointerException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException e) {
			//System.out.println("Aucun abonné n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Subscriber not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
			//System.err.println("Exception when persisting data");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
	    }
		
			tx.commit();
			JPAUtil.closeEntityManager();
	
		
	}
	public  Loan loan(Shell shell,long lenderID, String  copyID ){
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
			
			//System.out.println(l.getReturnDate().getTime());
			
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			messageBox.setMessage(l.getReturnDate().getTime().toString());
			messageBox.setText("Error");
			messageBox.open();
			 
		}catch (TooManyLoansException aaaaaaaaaae){
			//System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") attient son quota d'emprunt");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") reached its quota of loan");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (LentBookException le){
//			System.out.println(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") a déja emprunté ce livre");
			//System.out.println(le.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 //messageBox.setMessage(s.getNumber() + "(" + s.getFirstName() + " " +  s.getLastName()+") already loans a copy of this book");
			messageBox.setMessage(le.getMessage());
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (ReservedBookException re){
			//System.out.println("Le nombre de réservation est suppérieur au nombre des exemplaires");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Reservations number is higher than book copies number !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
//			System.out.println("Aucun abonné n'est trouvé");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Subscriber not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (BookCopyExistsException pe) {
			//System.out.println("Aucun exemplaire n'est trouvé");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			messageBox.setMessage("Book Copy not found !");
			messageBox.setText("Error");
			messageBox.open();
			JPAUtil.closeEntityManager();
	    }catch (BadParametersException pe) {
	    	//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
	    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Error when entering data!");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		 }catch (NullPointerException e) {
		     //System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (Exception pe) {
		//System.err.println("Exception when persisting data");
	    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}

			tx.commit();
			JPAUtil.closeEntityManager();
		
		
		
		return l;
	}
	
	
	///////////////////////jalon 1///////////////////////
	public  AgeCategory addAgeCategory(Shell shell,String name,int min,int max){
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
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			messageBox.setMessage("Age Category "+name+" is created");
			messageBox.setText("");
			messageBox.open();
			
		} catch (BadStringException e) {
			//System.out.println("Imbrication avec une catégorie déjà existante.");
	    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		  messageBox.setMessage("Wrong age category name !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	     }  catch (AgeCategoryOverLapException e) {
				//System.out.println("Imbrication avec une catégorie déjà existante.");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Over lap with an existing age category !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (AgeCategoryExistsException e) {
				//System.out.println("Age category not found !");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Age category not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (BadParametersException e) {
				//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Incorrect information!");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (Exception pe) {
		//System.err.println("Exception when persisting data");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }

			tx.commit();
			JPAUtil.closeEntityManager();
	
		
		
		return ageCategory;
	}
	
	
	
	
	public  void modifyAgeCategory(Shell shell,String name,int min,int max){
		AgeCategoryDAO ageCategoryDAO=new AgeCategoryDAO();
		EntityManager em;
		EntityTransaction tx;

		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			ageCategoryDAO.modify(name, max, min);
		//	System.out.println("Modification de la catégorie d'age "+name);
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			 messageBox.setMessage(name + " is modified");
			 messageBox.setText("");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch(BadParametersException e){
			//System.out.println("Max et inferieur au Min");
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Data is not complete !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}
		catch (AgeCategoryOverLapException e) {
			//	System.out.println("Imbrication avec une catégorie déjà existante.");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Over lap with an existing age category !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		} catch (AgeCategoryExistsException e) {
			//System.out.println("Catégorie d'age non existante");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Age category not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
	    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
	    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Exception when persisting data");
			 messageBox.setText("Error");
			 messageBox.open();
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	    }
			tx.commit();
			JPAUtil.closeEntityManager();
	
	}
	
public void removeAgeCategory(Shell shell,String name){
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
			 MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
			 messageBox.setMessage(name + " is removed");
			 messageBox.setText("");
			 messageBox.open();
		}catch (AgeCategoryExistsException pe) {
			//System.out.println("Système ne trouve pas la catégorie " + name);
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Age category not found !");
			 messageBox.setText("Error");
			 messageBox.open();
			 JPAUtil.closeEntityManager();
		}catch (SubscriberExistsException pe) {
				//System.out.println("Au moins un abonné existe dans la catégorie "+name);
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("At least one subscriber exist in the age category "+name);
			 messageBox.setText("Error");
			 messageBox.open();
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
public  Subscriber addSubscriber(Shell shell,String firstName, String lastName, Calendar bornDate,String email){
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
		 MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage(subscriber.getNumber() + " is created");
		 messageBox.setText("");
		 messageBox.open();
	}
    catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not complete !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }
	catch(EmailExistsException e){
		//System.out.println("Email existe déja");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Email already associed for a subscriber !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("First name or last name or email have a bad format !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Birth date is not correct !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch (SubscriberExistsException e) {
			//System.out.println("Abonné déjà existant.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber already exist !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
		 
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
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
public  Entitled addEntitled(Shell shell,String firstName, String lastName, Calendar bornDate,String email,long s){
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
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("First name or last name have a bad format");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }catch(EmailExistsException e){
		//System.out.println("Email existe déja");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Email already associed for a subscriber !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("First name or last name have a bad format !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Birth date is not correct !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}catch (SubscriberExistsException e) {
			//System.out.println("Abonné déjà existant.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber already exists !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
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
public void modifySubscriber(Shell shell,long number, String firstName,String lastName, Calendar bornDate,String email, String ageCategory){
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Subscriber ("+ firstName + " " + lastName+") is modified");
		 messageBox.setText("");
		 messageBox.open();
	}catch (AgeCategoryExistsException e) {
		//System.out.println("Catégorie non existante.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Age category not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }
	catch(EmailExistsException e){
		//System.out.println("Email existe déja");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Email already associeted for a subscriber !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("First name or last name have a bad format !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");.
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Birth date is not correct !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (SubscriberExistsException e) {
			//System.out.println("Abonné non existant.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	//System.err.println("Exception when persisting data");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}


public void modifySubscriber(Shell shell,long number, String firstName,String lastName, Calendar bornDate,String email){
	SubscriberDAO subscriberDAO=new SubscriberDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		subscriberDAO.modify(number, firstName, lastName, bornDate, email);
		//System.out.println("Modification de l'abonné ("+ firstName + " " + lastName+") est réussie");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Subscriber ("+ firstName + " " + lastName+") is modified");
		 messageBox.setText("");
		 messageBox.open();
	}catch (AgeCategoryExistsException e) {
		//System.out.println("Catégorie non existante.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Age category not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}catch(NullPointerException e){
    	//System.out.println("Une ou plusieurs entrées sont nulles");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }
	catch(EmailExistsException e){
		//System.out.println("Email existe déja");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Email already associeted for a subscriber !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadStringException e){
		//System.out.println("Format nom ou prénom non valide");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("First name or last name have a bad format !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}
	catch(BadBornDateException e){
		//System.out.println("Date de naissance incorrecte");.
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Birth date is not correct !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (SubscriberExistsException e) {
			//System.out.println("Abonné non existant.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	//System.err.println("Exception when persisting data");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}


public void removeSubscriber(Shell shell,long id){
	Subscriber s = new Subscriber();
	SubscriberDAO subsDAO=new SubscriberDAO();
	HistoryDAO hDAO = new HistoryDAO();
	
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		 s = subsDAO.get(id);

		 for (History h : hDAO.getContent()){
			 if(h.getLender().equals(s)){
					 hDAO.remove(h);
			 }
		}
		
	    subsDAO.remove(s);
	    
		//System.out.println("Suppression de l'abonné ("+ s.getFirstName() + " " + s.getLastName()+") est réussie");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		messageBox.setMessage("Subscriber ("+ s.getFirstName() + " " + s.getLastName()+") is removed");
		messageBox.setText("");
		messageBox.open();
		tx.commit();
	}catch (SubscriberExistsException pe) {
		//System.out.println("Système ne trouve pas l'abonné " + id);
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber "+id+" not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 tx.rollback();
		 JPAUtil.closeEntityManager();
	}catch (SubscriberWithLoansException pe) {
		//	System.out.println("L'abonné "+ "(" + s.getFirstName() + " " + s.getLastName()+") a des emprunts en cours ");
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
			 messageBox.setMessage("Subscriber"+ "(" + s.getFirstName() + " " + s.getLastName()+") with exisiting loans");
			 messageBox.setText("Error");
			 messageBox.open();
			 tx.rollback();
			 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
//		System.err.println("Exception when persisting data");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();
		 tx.rollback();
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}
	
		
	
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

public  Subscriber SearchSubscriber(Shell shell,long id){
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Subscriber not found !");
		 messageBox.setText("Error");
		 messageBox.open();
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
public  BookCopy addBookCopy(Shell shell,String isbn){
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Book copy "+b.getId()+"is created");
		 messageBox.setText("");
		 messageBox.open();
		
	}  catch (BookExistsException e) {
			//System.out.println("Livre n'existe pas.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Book not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (Exception pe) {
	//System.err.println("Exception when persisting data");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();	
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}


public void removeBookCopy(Shell shell,String id){
	BookCopy b = new BookCopy();
	BookCopyDAO bookCopyDAO=new BookCopyDAO();
	HistoryDAO hDAO = new HistoryDAO();
	
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		b = bookCopyDAO.get(id);
		
		for (History h : hDAO.getContent()){
			 if(h.getCopy().equals(b)){
					 hDAO.remove(h);
			 }
		}
		
		bookCopyDAO.remove(b);
		
		//System.out.println("Suppression de l'exemplaire "+id+" est réussie");
		
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		messageBox.setMessage("Book copy "+b.getId()+"is removed");
		messageBox.setText("");
		messageBox.open();
		
		tx.commit();
	}catch (BookCopyExistsException pe) {
		//System.out.println("Système ne trouve pas l'exemplaire");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Book copy not found !");
		 messageBox.setText("Error");
		 messageBox.open();
		 tx.rollback();
		 JPAUtil.closeEntityManager();
    }catch (LentBookException pe) {
		//System.out.println("Système ne trouve pas l'exemplaire");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage(pe.getMessage());
		 messageBox.setText("Error");
		 messageBox.open();
		 tx.rollback();
		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();	
		 tx.rollback();
		JPAUtil.closeEntityManager();
		pe.printStackTrace();
	}

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

public Book addBook(Shell shell,String title, ArrayList<String> authors, Calendar editionDate, String isbn){
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Book "+isbn+" is created");
		 messageBox.setText("");
		 messageBox.open();
	} catch (BookExistsException e) {
			//System.out.println("Livre déjà existant.");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Book already exists !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (BadParametersException e) {
			//System.out.println("Une ou plusieurs entrées sont manquantes ou erronées");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	} catch (NullPointerException e) {
		//System.out.println("Une ou plusieurs entrées sont nulles");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Data is not completed !");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
   }  catch (Exception pe) {
	//System.err.println("Exception when persisting data");
	   MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();	
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
	return b;
}

public void modifyBook(Shell shell, String isbn,ArrayList<String> authors, String title, Calendar editionDate){
	BookDAO bookDAO=new BookDAO();
	EntityManager em;
	EntityTransaction tx;

	em = JPAUtil.getEntityManager();
	tx = em.getTransaction();
	tx.begin();
	try {
		bookDAO.modify(isbn,authors,title,editionDate);
		//System.out.println("Modification du livre ("+isbn+","+title+") est réussie");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Book ("+isbn+","+title+") is modified");
		 messageBox.setText("");
		 messageBox.open();
	}catch (BookExistsException e) {
		//System.out.println("livre non existant");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Book not found!");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
	//System.err.println("Exception when persisting data");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();	
	JPAUtil.closeEntityManager();
	pe.printStackTrace();
    }
	
	tx.commit();
	JPAUtil.closeEntityManager();
	
}

public void removeBook(Shell shell, String isbn){
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION| SWT.OK);
		 messageBox.setMessage("Book "+isbn+" is created");
		 messageBox.setText("");
		 messageBox.open();
	}catch (BookExistsException pe) {
		//System.out.println("Système ne trouve pas le livre");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Book not found!");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
	}catch (LentBookException pe) {
			//System.out.println("Au moins un exemplaire est emprunté");
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("At least a book copy is lent");
		 messageBox.setText("Error");
		 messageBox.open();
		 JPAUtil.closeEntityManager();
    }catch (Exception pe) {
		//System.err.println("Exception when persisting data");
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR| SWT.OK);
		 messageBox.setMessage("Exception when persisting data");
		 messageBox.setText("Error");
		 messageBox.open();	
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

public Subscriber SearchSubscriber(Long id) {
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
}
