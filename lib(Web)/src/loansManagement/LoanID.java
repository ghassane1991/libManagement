package loansManagement;

import java.io.Serializable;

import subscribersManagement.Subscriber;

import booksManagement.Book;
import booksManagement.BookCopy;


public class LoanID implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BookCopy bookCopy;
	private Subscriber lender;
	
	public LoanID(){
		
	}
	
	public LoanID(BookCopy bc,Subscriber s){
		bookCopy = bc;
		lender = s;
	}
}
