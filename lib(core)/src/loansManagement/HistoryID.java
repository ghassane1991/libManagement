package loansManagement;

import java.io.Serializable;
import java.util.Calendar;   

import subscribersManagement.Subscriber;
import booksManagement.BookCopy;

/**
 * @author  user
 */
public class HistoryID implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="copy"
	 * @uml.associationEnd  
	 */
	private BookCopy copy;
	private Calendar loanDate;
	
	public HistoryID(){
		
	}
	
	public HistoryID(BookCopy bc,Calendar loanDate){
		copy = bc;
		this.loanDate = loanDate;
	}
	
}
