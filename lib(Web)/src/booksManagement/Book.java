/*
 * Class representing a book of a library
 * @author M.T. Segarra
 * @version 0.0.1
 */
package booksManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import subscribersManagement.Subscriber;


import exceptions.AlreadyReservedException;
import exceptions.BadParametersException;
import exceptions.LentBookException;

/**
 * Entity implementation class for Entity: Book
 * 
 */
@Entity
@Table(name = "books")
@NamedQuery(name = "findAllBooks", query = "select b from Book b")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property name="title"
	 */
	@Column(name = "book_title", nullable=false)
	private String title;

	/**
	 * @uml.property name="authors"
	 */
	@Column(nullable=false)
	private ArrayList<String> authors;

	/**
	 * @uml.property name="editionDate"
	 */
	@Column(nullable=false)
	private Calendar editionDate;

	/**
	 * @uml.property name="isbn"
	 */
	@Id
	@Column(name = "isbn_id", nullable=false)
	private String isbn;

	private boolean reserved = false;
	
	//@OneToMany
	private LinkedList<Long> reservationList;
	
	private int numReservation = 0;
	/**
	 * @uml.property name="bookCopy"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="book:booksManagement.BookCopy"
	 */
	@OneToMany (mappedBy = "book", fetch=FetchType.EAGER)
	private List<BookCopy> bookCopy;

	public Book() {
	}

	/**
	 * Creates a book All parameters should be given
	 * 
	 * @param title
	 *            the title of the book
	 * @param authors
	 *            the list of the authors (names)
	 * @param editionDate
	 *            date of the edition of the book
	 * @param isbn
	 *            ISBN of the book
	 * @throws BadParametersException
	 */
	public Book(String title, ArrayList<String> authors, Calendar editionDate,
			java.lang.String isbn) throws BadParametersException {
	
		this.setTitle(title);
		this.setAuthors(authors);
		this.setEditionDate(editionDate);
		this.setIsbn(isbn);
		this.bookCopy = new ArrayList<BookCopy>();
		this.reservationList = new LinkedList<Long>();
	}

	/*
	 * Two books are equal if their ISBN is the same
	 */
	@Override
	public boolean equals(Object book) {
		if (book == null)
			return false;
		Book b = (Book) book;

		if (b.getIsbn().equals(isbn))
			return true;

		return false;
	}

	/**
	 * Getter of the property <tt>authors</tt>
	 * 
	 * @return Returns the authors.
	 * @uml.property name="authors"
	 */
	public ArrayList<String> getAuthors() {
		return authors;
	}

	/**
	 * Getter of the property <tt>bookCopy</tt>
	 * 
	 * @return Returns the bookCopy.
	 * @uml.property name="bookCopy"
	 */
	public List<BookCopy> getBookCopy() {
		return bookCopy;
	}

	/**
	 * Getter of the property <tt>editionDate</tt>
	 * 
	 * @return Returns the editionDate.
	 * @uml.property name="editionDate"
	 */
	public Calendar getEditionDate() {
		return editionDate;
	}

	/**
	 * Getter of the property <tt>isbn</tt>
	 * 
	 * @return Returns the isbn.
	 * @uml.property name="isbn"
	 */
	public String getIsbn() {
		return isbn;
	};

	/**
	 * Getter of the property <tt>title</tt>
	 * 
	 * @return Returns the title.
	 * @uml.property name="title"
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter of the property <tt>authors</tt>
	 * 
	 * @param authors
	 *            The authors to set.
	 * @throws BadParametersException 
	 * @uml.property name="authors"
	 */
	public void setAuthors(ArrayList<String> authors) throws BadParametersException {
		if (authors == null)
			throw new NullPointerException();
		if (authors.size() == 0)
			throw new BadParametersException();	
		this.authors = authors;
	}

	/**
	 * Setter of the property <tt>bookCopy</tt>
	 * 
	 * @param bookCopy
	 *            The bookCopy to set.
	 * @uml.property name="bookCopy"
	 */
	public void setBookCopy(ArrayList<BookCopy> bookCopy) {
		this.bookCopy = bookCopy;
	}

	/**
	 * Setter of the property <tt>editionDate</tt>
	 * 
	 * @param editionDate
	 *            The editionDate to set.
	 * @throws BadParametersException 
	 * @uml.property name="editionDate"
	 */
	public void setEditionDate(Calendar editionDate) throws BadParametersException {
		if (editionDate == null)
			throw new BadParametersException();
		
		this.editionDate = editionDate;
	}

	/**
	 * Setter of the property <tt>isbn</tt>
	 * 
	 * @param isbn
	 *            The isbn to set.
	 * @throws BadParametersException 
	 * @uml.property name="isbn"
	 */
	public void setIsbn(String isbn) throws BadParametersException,NullPointerException {
		if (isbn == null)
			throw new NullPointerException();
		if (isbn.length() == 0){
			throw new BadParametersException();
		}
		this.isbn = isbn;
	}

	/**
	 * Setter of the property <tt>title</tt>
	 * 
	 * @param title
	 *            The title to set.
	 * @throws BadParametersException 
	 * @uml.property name="title"
	 */
	public void setTitle(String title) throws BadParametersException {
		if (title == null)
			throw new NullPointerException();
		if (title.length() == 0){
			throw new BadParametersException();
		}
		this.title = title;
	}

	/**
	 * Returns a description of the book
	 */
	public String toString() {
		return this.title + ", " + this.isbn;
	}

	/**
		 */
	public int getNumberOfCopies() {
		return bookCopy.size();
	}

	/**
		 */
	public void addCopy(BookCopy bc) {
		bookCopy.add(bc);
	}

	/**
	 * @throws BadParametersException
	 */
	/*public BookCopy createCopy() throws BadParametersException {
		BookCopy bc = new BookCopy(this);
		bookCopy.add(bc);
		return bc;
	}*/
	
	public void createCopy(BookCopy bc) {
		bookCopy.add(bc);
	}
	/**
		 */
	public BookCopy removeCopy(String copyId) throws LentBookException {
		// for (BookCopy bc: bookCopy)
		for (int j = 0; j < bookCopy.size(); j++) {
			if (bookCopy.get(j).getId().equals(copyId)) {
				if (bookCopy.get(j).isLent()) {
					throw new LentBookException("Removing lent book copy");
				} else {
					/*String[] s = copyId.split("/");

					for (int i = j + 1; i < bookCopy.size(); i++) {
						Integer ii = new Integer(i);
						bookCopy.get(i).setId(s[0] + "/" + ii.toString());
					}
					*/
					return bookCopy.remove(j);// .remove(bc);
				}
			}
		}
		return null;
	}
	
	public void setReserved(boolean r){
		reserved = r;
	}
	public boolean isReserved(){
		return reserved;
	}
	public void setNumReservation(int num){
		this.numReservation = num;
	}

	public int getNumReservation(){
		return this.numReservation;
	}
	
	public LinkedList<Long> getReservationList(){
		return this.reservationList;
	}
	
	public void setReservationList(LinkedList<Long> rl){
		this.reservationList = rl;
	}
	
	public void removeSubscriber(Subscriber s){
		this.reservationList.remove(s.getNumber());
	}
	
	public void addSubscriber(Subscriber s) throws AlreadyReservedException{
		if(this.getReservationList().contains(s.getNumber())){
			throw new AlreadyReservedException();
		}
		this.reservationList.add(s.getNumber());
	}
}
