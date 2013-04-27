package booksManagement;

import java.util.List;

import javax.persistence.EntityManager;

import jpaUtils.JPAUtil;
import exceptions.BookCopyExistsException;
import exceptions.BookExistsException;

public class BookCopyDAO {
	
	public boolean isEmpty(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllBookCopies").getResultList().isEmpty())
			return true;
		else
			return false;	
	}


	/**
	 */
	public List<BookCopy> getContent(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		return ((List<BookCopy>)entityManager.createNamedQuery("findAllBookCopies").getResultList());
	}


	public boolean add(BookCopy bookCopy) throws BookExistsException{
			EntityManager entityManager=JPAUtil.getEntityManager();
			BookDAO bDAO = new BookDAO();
			if (bDAO.contains(bookCopy.getBook())==false){ //Verify if the book exists in the database
				throw new BookExistsException();
			}
//			if (contains(bookCopy)){
//				throw new BookCopyExistsException();
//			}
	try{
				entityManager.persist(bookCopy);
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
			List<BookCopy> theBookCopies = (List<BookCopy>) entityManager.createNamedQuery("findAllBookCopies").getResultList();
			for (BookCopy b : theBookCopies)
				result += b.toString()+ "\n";
					return result;
	}


	public BookCopy get(String bookCopyId) throws BookCopyExistsException {
			if (bookCopyId == null){
				throw new NullPointerException();
			}
			EntityManager entityManager=JPAUtil.getEntityManager();
			BookCopy bc = (BookCopy) entityManager.find(BookCopy.class, bookCopyId);
			if (bc ==null) throw new BookCopyExistsException();
			else return bc;
	}


		/**
		 */
	public long size(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			return (entityManager.createNamedQuery("findAllBookCopies").getResultList().size());
	}


		/**
		 */
	public boolean remove(BookCopy bookCopy) throws BookCopyExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			if (bookCopy== null) 
				throw new BookCopyExistsException(); 
			else if(!contains(bookCopy)){
				throw new BookCopyExistsException(); //Verify if the book Copy already exists in the database
			}
			else 
				try{
					entityManager.remove(bookCopy);
				} catch (Exception pe) {
					System.err.println("problem when deleting an entity ");
					pe.printStackTrace();
					return false;
				}
			return true;	
	}



	public boolean contains(BookCopy bookCopy) throws BookCopyExistsException {
				EntityManager entityManager=JPAUtil.getEntityManager();
				if (bookCopy == null) throw new BookCopyExistsException();
				BookCopy bc = (BookCopy) entityManager.find(BookCopy.class, bookCopy.getId());
				if (bc ==null) return false;
				else return bc.equals(bookCopy);	
	}

}
