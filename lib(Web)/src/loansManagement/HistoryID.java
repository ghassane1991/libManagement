package loansManagement;

import java.io.Serializable;
import java.util.Calendar;

import subscribersManagement.Subscriber;
import booksManagement.BookCopy;

public class HistoryID implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BookCopy copy;
	private Calendar loanDate;
	
	public HistoryID(){
		
	}
	
	public HistoryID(BookCopy bc,Calendar loanDate){
		copy = bc;
		this.loanDate = loanDate;
	}
}
