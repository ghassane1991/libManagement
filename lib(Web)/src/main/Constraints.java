/*
 * This class contains all the necessary constraints
 * for the library project
 * @author M.T. Segarra
 * @version 0.0.1
 */
package main;


public class Constraints {
	
	/**
	 * @uml.property  name="MAX_LOANS" readOnly="true"
	 */
	public static final int maxLOANS = 3;//10arg0
	/**
	 * @uml.property  name="MAX_RESERVES" readOnly="true"
	 */
	public static final int maxRESERVATIONS = 2; //5
	/**
	 * @uml.property  name="LOAN_DELAY" readOnly="true"
	 */
	public static final int loanDELAY = 100; //Days
	/**
	 * @uml.property  name="reservation_DELAY" readOnly="true"
	 */
	public static final int reservationDELAY = 15; //Days
	/**
	 * @uml.property  name="minLengthPassword" readOnly="true"
	 */
	public static final int minLengthPassword = 8;
}
