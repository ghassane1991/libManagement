package reservationManagement;

import java.io.Serializable;

import subscribersManagement.Subscriber;

import booksManagement.Book;
//The composite PK of Reservation
public class ReservationID implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Book book;
	private Subscriber subscriber;
	
	public ReservationID(){
		
	}
	
	public ReservationID(Book b,Subscriber s){
		book = b;
		subscriber  = s;
	}
}
