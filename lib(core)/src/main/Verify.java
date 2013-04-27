package main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.swt.widgets.Shell;

import booksManagement.Book;
import booksManagement.BookDAO;

import jpaUtils.JPAUtil;
import exceptions.BookExistsException;
import exceptions.ReserveBookException;
import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;

public class Verify extends Thread {
	public void run(){
		
		while(true){
			Library lib = new Library();
			List<Reservation> rs = lib.getReservations();
			List<Subscriber>  ls = lib.SubscriberList();
			List<booksManagement.Book> bs = lib.bookList();
			
			
			for (booksManagement.Book b : bs){
				 for(Long subsNum : b.getReservationList()){
					 Subscriber subs = lib.SearchSubscriber(subsNum);
					 if (subs.getCurrentReservation().isEmpty()){
						 b.removeSubscriber(subs);
						 
						EntityManager em;
						EntityTransaction tx;
						
						em = JPAUtil.getEntityManager();
						tx = em.getTransaction();
						tx.begin();
							
						BookDAO bdao = new BookDAO();
						try {
							booksManagement.Book bbb = bdao.get(b.getIsbn());
							bbb.removeSubscriber(subs);
							em.merge(bbb);
						} catch (BookExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						tx.commit();
						JPAUtil.closeEntityManager();
					 }
				 }
			}
		}
			
			
    }

}
