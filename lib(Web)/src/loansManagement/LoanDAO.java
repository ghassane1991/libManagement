package loansManagement;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import booksManagement.Book;
import booksManagement.BookCopy;

import jpaUtils.JPAUtil;
import exceptions.BadParametersException;
import exceptions.BadReturnDateException;
import exceptions.BookCopyExistsException;
import exceptions.BookExistsException;
import exceptions.LentBookException;
import exceptions.LoanExistsException;
import exceptions.ReservedBookException;
import exceptions.SubscriberExistsException;
import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import subscribersManagement.Subscriber;

public class LoanDAO {
	
	
	public boolean isEmpty(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllLoans").getResultList().isEmpty())
			return true;
		else
			return false;	
	}
	
	public List<Loan> getContent(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		return ((List<Loan>)entityManager.createNamedQuery("findAllLoans").getResultList());
	}
	
	public boolean add(Loan loan){
			EntityManager entityManager=JPAUtil.getEntityManager();
			//History h = new History(loan.getLender(), loan.getBookCopy());
			//HistoryDAO hdao = new HistoryDAO();
//			ReservationDAO rdao = new ReservationDAO();
//			for (Reservation r: rdao.getContent()){
//				if (r.getBook().equals(loan.getBookCopy().getBook())){
//					if(!r.getSubscriber().equals(loan.getLender())){
//						loan.returnBook();
//						throw new ReservedBookException();
//					}
//				}
//			}
	try{
				entityManager.persist(loan);
			//	hdao.add(h);
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
			List<Loan> theLoans = (List<Loan>) entityManager.createNamedQuery("findAllLoans").getResultList();
			for (Loan l : theLoans)
				result += l.toString()+ "\n";
					return result;
		}

	public long size(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			return (entityManager.createNamedQuery("findAllLoans").getResultList().size());
		}

//	public Loan get(LoanID lid) throws LoanExistsException {
//		EntityManager entityManager=JPAUtil.getEntityManager();
//		Loan l = (Loan) entityManager.find(Loan.class, lid);
//		if (l ==null) throw new LoanExistsException();
//		else return l;
//	}
	public Loan get(long lid) throws LoanExistsException {
		EntityManager entityManager=JPAUtil.getEntityManager();
		Loan l = (Loan) entityManager.find(Loan.class, lid);
		if (l ==null) throw new LoanExistsException();
		else return l;
	}
	public boolean remove(Loan loan) throws LoanExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			if (loan== null) 
				throw new LoanExistsException(); 
			else 
				try{
					entityManager.remove(loan);
				} catch (Exception pe) {
					System.err.println("problem when deleting an entity ");
					pe.printStackTrace();
					return false;
				}
			return true;	
		}
	
//	public boolean contains(Loan loan) throws LoanExistsException {
//		EntityManager entityManager=JPAUtil.getEntityManager();
//		if (loan == null) throw new LoanExistsException();
//		Loan l = (Loan) entityManager.find(Loan.class, new LoanID(loan.getBookCopy(),loan.getLender()));
//		if (l ==null) return false;
//		else return l.equals(loan);	
//	}

	public boolean contains(Loan loan) throws LoanExistsException {
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (loan == null) throw new LoanExistsException();
		Loan l = (Loan) entityManager.find(Loan.class, loan.getLoanID());
		if (l ==null) return false;
		else return l.equals(loan);	
	}
	public List<BookCopy> getLentBook(Subscriber s) throws SubscriberExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		List<BookCopy> lb = new ArrayList<BookCopy>();
		if (s == null)
			throw new SubscriberExistsException();
		
		Query query = entityManager.createQuery("select l from Loan l where l.lender=" + s.getNumber() + "");
		List<Loan> ls = (List<Loan>) query.getResultList();
		
		for (Loan l:ls){
			lb.add(l.getBookCopy());
		}
		
		return lb;
	}
	
	public Subscriber getLender(BookCopy b) throws BookCopyExistsException{
		EntityManager entityManager=JPAUtil.getEntityManager();
		
		if (b == null)
			throw new BookCopyExistsException();
		
		Query query = entityManager.createQuery("select l from Loan l where l.bookCopy=" + b.getId() + "");
		Loan l = (Loan) query.getSingleResult();
		
		return l.getLender();
	}
}
