package loansManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import main.Constraints;

import jpaUtils.JPAUtil;
import subscribersManagement.Subscriber;
import booksManagement.BookCopy;
import exceptions.BookCopyExistsException;
import exceptions.HistoryExistsException;
import exceptions.LoanExistsException;
import exceptions.SubscriberExistsException;

public class HistoryDAO {

	public boolean isEmpty(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findHistory").getResultList().isEmpty())
			return true;
		else
			return false;	
	}
	
	public List<History> getContent(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		return ((List<History>)entityManager.createNamedQuery("findHistory").getResultList());
	}
	
	public List<History> getRetard(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<History> hs = new ArrayList<History>();
//		Query query = entityManager.createQuery("select h from History h where h.isDelayExceeded=TRUE");
//		List<History> hs = (List<History>) query.getResultList();
		for (History h:getContent()){
//			if (h.getReturnDate() != null){
				if (new GregorianCalendar().after(h.getMustReturnDate())){
					hs.add(h);
				}
//			}
		}
		return hs;
	}
	
	public boolean add(History history){
			EntityManager entityManager=JPAUtil.getEntityManager();
	try{
				entityManager.persist(history);
				
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
			List<History> theHistories = (List<History>) entityManager.createNamedQuery("findHistory").getResultList();
			for (History h : theHistories)
				result += h.toString()+ "\n";
					return result;
		}

//	public History get(HistoryID hid) throws HistoryExistsException {
//		EntityManager entityManager=JPAUtil.getEntityManager();
//		History  h = (History) entityManager.find(History.class, hid);
//		if (h==null) throw new HistoryExistsException();
//		else return h;
//	}
	public History get(long hid) throws HistoryExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			History  h = (History) entityManager.find(History.class, hid);
			if (h==null) throw new HistoryExistsException();
			else return h;
   }

	public long size(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			return (entityManager.createNamedQuery("findHistory").getResultList().size());
		}

	public boolean remove(History history) throws HistoryExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			if (history== null) 
				throw new HistoryExistsException(); 
			else 
				try{
					entityManager.remove(history);
				} catch (Exception pe) {
					System.err.println("problem when deleting an entity ");
					pe.printStackTrace();
					return false;
				}
			return true;	
		}
	
//	public boolean contains(History history) throws LoanExistsException {
//		EntityManager entityManager=JPAUtil.getEntityManager();
//		if (history == null) throw new LoanExistsException();
//		History h = (History) entityManager.find(History.class, new HistoryID(history.getCopy(),history.getLoanDate()));
//		if (h ==null) return false;
//		else return h.equals(history);	
//	}
	
	public boolean contains(History history) throws LoanExistsException {
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (history == null) throw new LoanExistsException();
		History h = (History) entityManager.find(History.class, history.getHistoryID());
		if (h ==null) return false;
		else return h.equals(history);	
	}
	
	
	public List<BookCopy> getLentBook(Subscriber s) throws SubscriberExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<BookCopy> lb = new ArrayList<BookCopy>();
		
		if (s == null)
			throw new SubscriberExistsException();
		
		Query query = entityManager.createQuery("select h from History h where h.lender=" + s.getNumber() + "");
		List<History> hs = (List<History>) query.getResultList();
		
		for (History h:hs){
			lb.add(h.getCopy());
		}
		
		return lb;
	}
	
	public List<Subscriber> getLender(BookCopy b) throws BookCopyExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<Subscriber> ls = new ArrayList<Subscriber>();
		
		if (b == null)
			throw new BookCopyExistsException();
		
		Query query = entityManager.createQuery("select h from Loan h where h.Copy=" + b.getId() + "");
		List<History> lh = (List<History>) query.getSingleResult();
		
		for (History h: lh){
			ls.add(h.getLender());
		}
		
		return ls;
	}
	// verify if there is some loans which exceeded the delays
	public void verify(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		for (History h : getContent()){
//			Calendar rDate = h.getLoanDate();
//			rDate.add(Calendar.DAY_OF_MONTH, Constraints.loanDELAY);
			if (new GregorianCalendar().after(h.getMustReturnDate())){
				entityManager.merge(h);
			}
		}
	}

}
