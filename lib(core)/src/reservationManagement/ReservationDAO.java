package reservationManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import booksManagement.Book;

import jpaUtils.JPAUtil;
import loansManagement.History;
import main.Constraints;
import subscribersManagement.Subscriber;
import exceptions.BookExistsException;
import exceptions.ReservationExistsException;
import exceptions.ReserveBookException;
import exceptions.SubscriberExistsException;

public class ReservationDAO {

	public boolean isEmpty(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllReservations").getResultList().isEmpty())
			return true;
		else
			return false;	
	}
	
	public List<Reservation> getContent(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		return ((List<Reservation>)entityManager.createNamedQuery("findAllReservations").getResultList());
	}
	
	public boolean add(Reservation reservation) throws ReservationExistsException{
			EntityManager entityManager=JPAUtil.getEntityManager();
			if(contains(reservation)){
				throw new ReservationExistsException(); 
			}
	try{
				entityManager.persist(reservation);
			} catch (Exception e) {
				System.err.println("Problem when saving");
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
	
	public String toString(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			String result="";
			List<Reservation> theReservations = (List<Reservation>) entityManager.createNamedQuery("findAllReservations").getResultList();
			for (Reservation l : theReservations)
				result += l.toString()+ "\n";
					return result;
		}

	public Reservation get(ReservationID rid) throws ReservationExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			Reservation l = (Reservation) entityManager.find(Reservation.class, rid);
			if (l ==null) throw new ReservationExistsException();
			else return l;
		}

	public long size(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			return (entityManager.createNamedQuery("findAllReservations").getResultList().size());
		}

	public boolean remove(Reservation reservation) throws ReservationExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			if (reservation== null) 
				throw new ReservationExistsException(); 
			else 
				try{
					entityManager.remove(reservation);
				} catch (Exception pe) {
					System.err.println("problem when deleting an entity ");
					pe.printStackTrace();
					return false;
				}
			return true;	
		}
	
	public boolean contains(Reservation reservation) throws ReservationExistsException {
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (reservation == null) throw new ReservationExistsException();
		Reservation r = (Reservation) entityManager.find(Reservation.class, new ReservationID(reservation.getBook(),reservation.getSubscriber()));
		if (r ==null) return false;
		else return r.equals(reservation);	
	}
	
	public List<Book> getReservedBook(Subscriber s) throws SubscriberExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<Book> lb = new ArrayList<Book>();
		if (s == null)
			throw new SubscriberExistsException();
		
		Query query = entityManager.createQuery("select r from Reservation r where r.subscriber=" + s.getNumber() + "");
		List<Reservation> rs = (List<Reservation>) query.getResultList();
		
		for (Reservation r:rs){
			lb.add(r.getBook());
		}
		
		return lb;
	}
	
	public List<Subscriber> getReserver(Book b) throws BookExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<Subscriber> ls = new ArrayList<Subscriber>();
		if (b == null)
			throw new BookExistsException();
		
		Query query = entityManager.createQuery("select r from Reservation r where r.book=" + b.getIsbn() + "");
		List<Reservation> rs = (List<Reservation>) query.getSingleResult();
		

		for (Reservation r : rs){
			ls.add(r.getSubscriber());
		}
		
		return ls;
	}
	// verify if some reservation which finished
	public void verify() throws ReserveBookException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		for (Reservation r : getContent()){
			if (new GregorianCalendar().after(r.getFinishDate())){
				r.cancel();
				//entityManager.merge(r);
				entityManager.remove(r);
			}
		}
	}
}
