package loansManagement;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import subscribersManagement.Subscriber;
import booksManagement.BookCopy;
import exceptions.BadReturnDateException;

@Entity 
@IdClass(value = HistoryID.class)
@Table(name="history")
@NamedQuery(name="findHistory", query="select h from History h")
public class History implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="copy"
	 */
	@Id
	@OneToOne
	@JoinColumn(name = "bookCopy_id")
	private BookCopy copy=null;

	/** 
	 * @uml.property name="lender"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="history:subscribersManagement.Subscriber"
	 */
	@OneToOne
	@JoinColumn(name = "lender_ref")
	private Subscriber lender = null;

	/**
	 * @uml.property  name="loanDate"
	 */
	@Id
	@Column(name = "loanDate")
	private Calendar loanDate;

	/**
	 * @uml.property  name="returnDate"
	 */
	private Calendar returnDate;

	private Calendar mustReturnDate;
	
//	/**
//	 * @uml.property  name="isDelayExceeded"
//	 */
//	private Boolean isDelayExceeded;

	/**
	 */
public History(){
}

	/**
	 */
public History(Subscriber lender, BookCopy copy){
	
	setLender(lender);
	setCopy(copy);
	loanDate = new GregorianCalendar();
//	setIsDelayExceeded(false);
	returnDate = null;
	mustReturnDate = copy.getLoan().getReturnDate();
}


	/**
	 * Getter of the property <tt>copy</tt>
	 * @return   Returns the copy.
	 * @uml.property   name="copy"
	 */
	public BookCopy getCopy() {
		return copy;
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
	 * Getter of the property <tt>loanDate</tt>
	 * @return  Returns the loanDate.
	 * @uml.property  name="loanDate"
	 */
	public Calendar getLoanDate() {
		return loanDate;
	}

	/**
	 * Getter of the property <tt>returnDate</tt>
	 * @return  Returns the returnDate.
	 * @uml.property  name="returnDate"
	 */
	public Calendar getReturnDate() {
		return returnDate;
	}

//	/**
//	 * Getter of the property <tt>isDelayExceeded</tt>
//	 * @return  Returns the isDelayExceeded.
//	 * @uml.property  name="isDelayExceeded"
//	 */
//	public Boolean isIsDelayExceeded() {
//		return isDelayExceeded;
//	}

		
		/*
		 * Setter of the property <tt>loanDate</tt>
		 * @param loanDate  The loanDate to set.
		 * @uml.property  name="loanDate"
		 */
	/*
		public void setLoanDate(Calendar loanDate) {
			if (loanDate == null){
				throw new NullPointerException();
			}
	
			this.loanDate = loanDate;
		}
*/
			
		
			/**
			 * Setter of the property <tt>copy</tt>
			 * @param copy   The copy to set.
			 * @uml.property   name="copy"
			 */
			public void setCopy(BookCopy copy) {
				if (copy == null) {
					throw new NullPointerException();
				}
				this.copy = copy;
			}



//			/**
//			 * Setter of the property <tt>isDelayExceeded</tt>
//			 * @param isDelayExceeded  The isDelayExceeded to set.
//			 * @uml.property  name="isDelayExceeded"
//			 */
//			public void setIsDelayExceeded(Boolean isDelayExceeded) {
//				this.isDelayExceeded = isDelayExceeded;
//			}

			/** 
			 * Setter of the property <tt>lender</tt>
			 * @param lender  The lender to set.
			 * @uml.property  name="lender"
			 */
			public void setLender(Subscriber lender) {
				this.lender = lender;
			}

			/**
			 * Setter of the property <tt>returnDate</tt>
			 * @param returnDate  The returnDate to set.
			 * @throws BadReturnDateException 
			 * @uml.property  name="returnDate"
			 */
			public void setReturnDate(Calendar returnDate) throws BadReturnDateException {
				if (returnDate == null) {
					throw new NullPointerException();
				}
				if (returnDate.before(new GregorianCalendar())) {
					throw new BadReturnDateException();
				}
				
				this.returnDate = returnDate;
			}
			
			public void setMustReturnDate(Calendar returnDate){
				this.mustReturnDate = returnDate;
			}
			public Calendar getMustReturnDate(){
				return this.mustReturnDate;
			}
			public String toString(){
				return this.getCopy().getId() + "-->" + this.getLender().getFirstName() + " " + this.getLender().getLastName() + " | " + this.loanDate.getTime() + "|" + this.returnDate.getTime();
			}
}
