package reservationManagement;

import java.io.Serializable;

import subscribersManagement.Subscriber;

import booksManagement.Book;
//The composite PK of Reservation
/**
 * @author  user
 */
public class ReservationID implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="book"
	 * @uml.associationEnd  
	 */
	private Book book;
	/**
	 * @uml.property  name="subscriber"
	 * @uml.associationEnd  
	 */
	private Subscriber subscriber;
	
	public ReservationID(){
		
	}
	
	public ReservationID(Book b,Subscriber s){
		book = b;
		subscriber  = s;
	}
}
