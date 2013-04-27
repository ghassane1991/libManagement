/*
 * Class representing a loan of a book by a subscriber
 * @author M.T. Segarra
 * @version 0.0.1
 */
package loansManagement;


import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import main.Constraints;
import exceptions.*;
import reservationManagement.Reservation;
import subscribersManagement.Subscriber;
import booksManagement.BookCopy;

/**
 * @author  user
 */
@Entity 
//@IdClass(value = LoanID.class) 
@Table(name="loans")
@NamedQuery(name="findAllLoans", query="select l from Loan l")
public class Loan implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * @uml.property  name="loanID"
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long loanID;
	
	/**
	 * @uml.property  name="loanDate"
	 */
	private Calendar loanDate;
	
	/**
	 * @uml.property  name="returnDate"
	 */
	@Column(nullable=false)
	private Calendar returnDate;


	/**
	 * @uml.property  name="bookCopy"
	 * @uml.associationEnd  
	 */
	//@Id
	//@Transient
	@OneToOne 
	@JoinColumn(name = "bookCopy",unique=true)
	private BookCopy bookCopy=null;

	/**
	 * @uml.property  name="lender"
	 * @uml.associationEnd  
	 */
	
	@ManyToOne
	@JoinColumn(name = "lender_ref",nullable=false)
	private Subscriber lender=null;

	/**
	 * @uml.property  name="history"
	 * @uml.associationEnd  
	 */
	@OneToOne
	private History history = null;
	
	public Loan(){
		
	}

	/**
	 * Creates the loan of book for lender
	 * Constraints checking is performed by the caller
	 * @param book the book to be lent
	 * @param lender the subscriber 
	 * @throws BadParametersException 
	 * @throws TooManyLoansException 
	 * @throws LentBookException 
	 * @throws BadReturnDateException 
	 * @throws NullPointerException 
	 * @throws ReservedBookException 
	 * @throws TooManyReservationException 
	 * @throws AlreadyReservedException 
	 */
	public Loan(BookCopy bookCopy, Subscriber lender) throws TooManyLoansException, LentBookException, NullPointerException, BadReturnDateException, ReservedBookException, TooManyReservationException, AlreadyReservedException, BadParametersException{

		setBookCopy(bookCopy);
		setLender(lender);

		for (Loan l:lender.getCurrentLoans()){
			if (l.getBookCopy().getBook().equals(bookCopy.getBook())){
				bookCopy = null;
				lender=null;
				throw new LentBookException("Lender already lent a copy of the same book.");
			}
		}
		
		if (bookCopy.getBook().isReserved()){
	
		   boolean b = false;
		   
//		   for (Reservation r:lender.getCurrentReservation()){
//				if(r.equals(new Reservation(bookCopy.getBook(),lender))){
//					b = true;
//				}
//		   }
		   
		   //Search if the subcriber has reserved this book
		   if(bookCopy.getBook().getReservationList().contains(lender.getNumber())){
				b=true;
			}
		   
		   if (!b){
			   //If the subscriber has not reserved this book he can loan it only if number of copies is higher  than the number of reservations  
				if(bookCopy.getBook().getBookCopy().size()-bookCopy.getBook().getNumReservation()<=0)
				{
					 throw new ReservedBookException();
				}
		   }
		   else{
			 //The subscriber is placed on a waiting list, he can only lend the book if the reservations of the first persons to reserve are finished or out dated.
				if(bookCopy.getBook().getBookCopy().size()-bookCopy.getBook().getReservationList().indexOf(lender.getNumber())<=0)
				{
					 throw new ReservedBookException();
				}
		   }
		}

		Calendar rDate = new GregorianCalendar();
		rDate.add(Calendar.DAY_OF_MONTH, Constraints.loanDELAY);
		setReturnDate(rDate);
//		System.out.println(this.returnDate.getTime());
		loanDate = new GregorianCalendar();
		
		
		// Update the subscriber if he can lend
		lender.lend(this);
		// Update the book copy if not already lent
		bookCopy.lend(this);
		
		
	//	setBookCopyId(bookCopy.getId());
		this.history = new History(lender, bookCopy);
       // new VerifyThread(this).start();
	}

	/**
	 * Two loans are equal is they concern the same book
	 * the same lender and return date
	 * @return true if the current loan is the same as the parameter
	 */
	@Override
	public boolean equals(Object loan){
		if (loan == null)
			return false;
		Loan l = (Loan)loan;
		boolean res = this.bookCopy.equals(l.bookCopy) 
			&& this.lender.equals(l.lender)
			&& (this.returnDate.equals(l.returnDate));
		return res;	
	}
	/**
	 * Getter of the property <tt>bookCopy</tt>
	 * @return   Returns the bookCopy.
	 * @uml.property   name="bookCopy"
	 */
	public BookCopy getBookCopy() {
		return bookCopy;
	}
//	//@Id
//	public String getBookCopyId() {
//		return this.bookCopyId;
//	}
	
	/**
	 * @return
	 * @uml.property  name="loanID"
	 */
	public long getLoanID() {
		return this.loanID;
	}
	/**
	 * Getter of the property <tt>history</tt>
	 * @return  Returns the history.
	 * @uml.property  name="history"
	 */
	public History getHistory() {
		return history;
	}
	
	/**
	 * Getter of the property <tt>lender</tt>
	 * @return  Returns the lender.
	 * @uml.property  name="lender"
	 */
	public Subscriber getLender() {
		return lender;
	}
		
	/**
	 * @return
	 * @uml.property  name="loanDate"
	 */
	public Calendar getLoanDate() {
		return returnDate;
	}


	/**
	 * Getter of the property <tt>returnDate</tt>
	 * @return  Returns the returnDate.
	 * @uml.property  name="returnDate"
	 */
	public Calendar getReturnDate() {
		return returnDate;
	}
	
	/**
	 * Called by the lent book
	 * Clear all the information contained in this loan
	 * if the lender is ok
	 * @throws BadParametersException 
	 * @throws LentBookException 
	 * @throws BadReturnDateException 
	 * @throws ReturnReturnedBook 
	 */
	public void returnBook() throws BadParametersException, 
	LentBookException, BadReturnDateException{
		// Ask the lender to return the book
		if(lender == null || bookCopy==null){
			throw new LentBookException("Book copy already lent.");
		}
		lender.returnBook(this);
		
		bookCopy.returnBookCopy();

		this.history.setReturnDate(new GregorianCalendar());
//		if (new GregorianCalendar().after(this.returnDate)){
//			this.history.setIsDelayExceeded(true);
//		}
		
	
		
		// Clear lender, book, and return date
		//lender = null;
		bookCopy = null;
		//returnDate = null;
	}

	/**
	 * Setter of the property <tt>bookCopy</tt>
	 * @param bookCopy   The bookCopy to set.
	 * @uml.property   name="bookCopy"
	 */
	public void setBookCopy(BookCopy bookCopy) throws NullPointerException {
		if (bookCopy == null) {
			throw new NullPointerException();
		}
		this.bookCopy = bookCopy;
	}
	
//	public void setBookCopyId(String id) {
//		if (id == null){
//			throw new NullPointerException();
//		}
//		this.bookCopyId = id;
//	}
	
	/**
	 * @param id
	 * @uml.property  name="loanID"
	 */
	public void setLoanID(long id) {
		this.loanID = id;
	}
	/**
	 * Setter of the property <tt>history</tt>
	 * @param history  The history to set.
	 * @uml.property  name="history"
	 */
	public void setHistory(History history) {
		this.history = history;
	}
	/**
	 * Setter of the property <tt>lender</tt>
	 * @param lender  The lender to set.
	 * @uml.property  name="lender"
	 */
	public void setLender(Subscriber lender) throws NullPointerException{
		if (lender == null) {
			throw new NullPointerException();
		}
		this.lender = lender;
	}
	/**
	 * @param loanDate
	 * @uml.property  name="loanDate"
	 */
	public void setLoanDate(Calendar loanDate){
		this.loanDate = loanDate;
	}

	/**
	 * Setter of the property <tt>returnDate</tt>
	 * @param returnDate  The returnDate to set.
	 * @throws BadReturnDateException 
	 * @uml.property  name="returnDate"
	 */
	public void setReturnDate(Calendar returnDate) throws NullPointerException, BadReturnDateException{
		if (returnDate == null) {
			throw new NullPointerException();
		}
		if (returnDate.before(new GregorianCalendar())) {
			throw new BadReturnDateException();
		}
		this.returnDate = returnDate;
	}
	public String toString(){
		return this.bookCopy.getId() + "-->" + this.lender.getFirstName() + " " + this.lender.getLastName();
	}


}
