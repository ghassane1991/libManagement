/**
 * 
 */
package booksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import exceptions.*;
import booksManagement.Book;
import jpaUtils.JPAUtil;
import javax.persistence.EntityManager;

/** 
 * @author user
 */
public class BookDAO {




	public boolean isEmpty(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllBooks").getResultList().isEmpty())
			return true;
		else
			return false;	
	}





	/**
	 */
	public List<Book> getContent(){
		EntityManager entityManager=JPAUtil.getEntityManager();
		return ((List<Book>)entityManager.createNamedQuery("findAllBooks").getResultList());
	}






	public boolean add(Book book) throws BookExistsException{
			EntityManager entityManager=JPAUtil.getEntityManager();
			if(contains(book)){
				throw new BookExistsException(); //Verify if the book already exists in the database
			}
			try{ 
				entityManager.persist(book);
			} catch (Exception e) {
				System.err.println("Problem when saving");
				e.printStackTrace();
				return false;
			}
			return true;
		}

// Modify a book 
	
		public void modify(String isbn, ArrayList<String> authors, String title, Calendar editionDate) throws BookExistsException, BadParametersException{
			EntityManager entityManager=JPAUtil.getEntityManager();
			Book b = (Book) entityManager.find(Book.class,isbn);
			if (b ==null) throw new BookExistsException();
			else {
				b.setAuthors(authors);
				b.setEditionDate(editionDate);
				b.setTitle(title);
				b.setIsbn(isbn);
			}
			try{ 
				entityManager.merge(b);
			} catch (Exception e) {
				System.err.println("Problem when saving");
				e.printStackTrace();
				
			}
		}





		public String toString(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			String result="";
			List<Book> theBooks = (List<Book>) entityManager.createNamedQuery("findAllBooks").getResultList();
			for (Book b : theBooks)
				result += b.toString()+ "\n";
					return result;
		}


		/*
		  public Book get(long bookId) throws BookExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			Book b = (Book) entityManager.find(Book.class, bookId);
			if (b ==null) throw new BookExistsException();
			else return b;
		}
		 */
		
		
		// The previous function is not correct, the primary key is the isbn (String)
		public Book get(String bookId) throws BookExistsException {
			if (bookId == null){
				throw new NullPointerException();
			}
			EntityManager entityManager=JPAUtil.getEntityManager();
			Book b = (Book) entityManager.find(Book.class, bookId);
			if (b ==null) throw new BookExistsException();
			else return b;
		}
		
		// Search the books by author
		public List<Book> getByAuthor(String author) throws BadParametersException, BookExistsException{
			if (author == null){
				throw new BadParametersException();
			}
			
			List<Book> books = new ArrayList<Book>();
			for (Book b : getContent()){
				
				/*for (String au : b.getAuthors()){
					System.out.println("aaa");
					if (au.equals(author)){
						books.add(b);
					}
				}*/
				if(b.getAuthors().contains(author)){
					books.add(b);
				}
			}
			
			if (books.size() == 0) throw new BookExistsException();
			else return books;
		}
		// Search the books by title
		public List<Book> getByTitre(String title) throws BadParametersException, BookExistsException{
			if (title == null){
				throw new BadParametersException();
			}
			List<Book> books = new ArrayList<Book>();
			for (Book b : getContent()){
				if(b.getTitle().equals(title)){
					books.add(b);
				}
			}
			if (books.size() == 0) throw new BookExistsException();
			else return books;
		}

		/**
		 */
		public long size(){
			EntityManager entityManager=JPAUtil.getEntityManager();
			return (entityManager.createNamedQuery("findAllBooks").getResultList().size());
		}






		/**
		 * @throws LentBookException 
		 */
		public boolean remove(Book book) throws BookExistsException, LentBookException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			
			if (book== null) 
				throw new BookExistsException();
			else if(!contains(book)){
				throw new BookExistsException(); //Verify if the book already exists in the database
			}
			else{
				for(BookCopy bc : book.getBookCopy()){ //Verify if at least one BookCopy is already lent
					if(bc.isLent()){
						throw new LentBookException("At least one BookCopy is already lent");
					}
				}
				try{
					BookCopyDAO bcDAO = new BookCopyDAO();
					for (BookCopy bc : book.getBookCopy()){
						BookCopy b = bcDAO.get(bc.getId());
						bcDAO.remove(b);
					}
					entityManager.remove(book);
				} catch (Exception pe) {
					System.err.println("problem when deleting an entity ");
					pe.printStackTrace();
					return false;
				}
				return true;
			}
		}





			
			/**
			 */
			public boolean contains(Book book) throws BookExistsException {
				EntityManager entityManager=JPAUtil.getEntityManager();
				if (book == null) throw new BookExistsException();
				Book b = (Book) entityManager.find(Book.class, book.getIsbn());
				if (b ==null) return false;
				else return b.equals(book);	
			}

}


