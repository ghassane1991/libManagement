package booksManagement;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import exceptions.BadParametersException;
import exceptions.LentBookException;
import exceptions.ReturnReturnedBook;
import loansManagement.Loan;

@Entity
@Table(name = "bookCopies")
@NamedQuery(name = "findAllBookCopies", query = "select bc from BookCopy bc")
public class BookCopy implements Serializable,Comparable{

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property   name="id"
	 */
	@Id
	@Column(name = "bc_id")
	private String id;
	
	/**
	 * @uml.property   name="book"
	 * @uml.associationEnd   inverse="bookCopy:booksManagement.Book"
	 */
	@ManyToOne
	@JoinColumn(name="book_isbn")
	private Book book;

	/**
	 * @uml.property   name="loan"
	 * @uml.associationEnd   inverse="bookCopy:loansManagement.Loan"
	 */

	//@Transient
	//@OneToOne 
	private Loan loan=null;

	public BookCopy(){
		
	}
	/**
	 * @throws BadParametersException
	 */
	public BookCopy(Book book) throws BadParametersException {
		if (book == null) {
			throw new BadParametersException();
		}
		this.book = book;
		
		Collections.sort(this.book.getBookCopy(),Collections.reverseOrder());
		boolean isEnd = true;
		for (int j = 0;j<this.book.getBookCopy().size()-1;j++){
			String[] s1 = this.book.getBookCopy().get(j).getId().split("/");
			String[] s2 = this.book.getBookCopy().get(j+1).getId().split("/");
			int nj = Integer.parseInt(s1[1]);
			int njp = Integer.parseInt(s2[1]);
			System.out.println(nj);
			System.out.println(njp);
			if (njp != nj+1){
				Integer i = new Integer(nj+1);
				this.id = book.getIsbn() + "/" + i.toString();
				isEnd = false;
				break;
			}
		}
		if (isEnd){
			Integer i = new Integer(book.getNumberOfCopies());
			i = i + 1;
			this.id = book.getIsbn() + "/" + i.toString();
		}
		book.createCopy(this);
	}

	/**
	 * two bookCopies are equal if they are copies of the same book
	 */
	public boolean equals(Object bookCopy) {
		if (bookCopy == null) {
			return false;
		} 
		else {
			BookCopy bc = (BookCopy) bookCopy;
//			if (this.getBook().equals(bc.getBook())) {
//				return true;
//			} else {
//				return false;
//			}
			if (this.id.equals(bc.getId())) {
				return true;
			} 
			else {
				return false;
			}
		}
	}

	/**
	 * Getter of the property <tt>book</tt>
	 * 
	 * @return Returns the book.
	 * @uml.property name="book"
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Getter of the property <tt>id</tt>
	 * 
	 * @return Returns the id.
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter of the property <tt>loan</tt>
	 * 
	 * @return Returns the loan.
	 * @uml.property name="loan"
	 */
	public Loan getLoan() {
		return loan;
	}

	public boolean isLent() {
		return (loan != null);
	}

	public void lend(Loan loan) throws LentBookException {
		// Checks if the book copy is available
		if (this.isLent())
			throw new LentBookException("Book copy already lent.");

		this.loan = loan;
	}

	public void returnBookCopy() throws BadParametersException,
			LentBookException {
		if (!isLent())
			throw new LentBookException("The book copy is not lent");

		//loan.returnBook();

		this.loan = null;
	}

	/**
	 * Setter of the property <tt>book</tt>
	 * 
	 * @param book
	 *            The book to set.
	 * @uml.property name="book"
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * Setter of the property <tt>id</tt>
	 * 
	 * @param id
	 *            The id to set.
	 * @uml.property name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Setter of the property <tt>loan</tt>
	 * 
	 * @param loan
	 *            The loan to set.
	 * @uml.property name="loan"
	 */
	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	/**
							 */
	public String toString() {
		return "Copy of " + book.getTitle() + " id = " + id;
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		BookCopy bc = (BookCopy) o;
		String[] s1 = bc.getId().split("/");
		String[] s2 = this.getId().split("/");
		
		return s1[1].compareTo(s2[1]);
	}

}
