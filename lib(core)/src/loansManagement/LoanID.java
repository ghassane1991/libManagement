package loansManagement;

import java.io.Serializable;

import subscribersManagement.Subscriber;

import booksManagement.Book;
import booksManagement.BookCopy;


/**
 * @author  user
 */
public class LoanID implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="bookCopy"
	 * @uml.associationEnd  
	 */
	private BookCopy bookCopy;
	/**
	 * @uml.property  name="lender"
	 * @uml.associationEnd  
	 */
	private Subscriber lender;
	
	public LoanID(){
		
	}
	
	public LoanID(BookCopy bc,Subscriber s){
		bookCopy = bc;
		lender = s;
	}
}
