/*
 * Class representing a subscriber of a library
 * A subscriber knows his first and last name,
 * his born date and his number in the library
 * @author M.T. Segarra
 * @version 0.0.1
 */
package subscribersManagement;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loansManagement.Loan;
import main.Constraints;
import exceptions.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import subscribersManagement.Entitled;
import reservationManagement.Reservation;

/**
 * Entity implementation class for Entity: Subscriber
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "subscribers")
@NamedQuery(name = "findAllSubscribers", query = "select s from Subscriber s")
public class Subscriber implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="firstName"
	 */
	@Column(nullable = false)
	private String firstName;

	/**
	 * @uml.property  name="lastName"
	 */
	@Column(nullable = false)
	private String lastName;

	/**
	 * @uml.property  name="bornDate"
	 */
	@Column(nullable = false)
	private Calendar bornDate;

	/**
	 * @uml.property  name="currentLoans"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" inverse="lender:java.util.ArrayList"
	 */
	// @Transient
	private ArrayList currentLoans;
	

	/**
	 * @uml.property  name="email"
	 */
	@Column(unique = true, nullable = false)
	private String email;

	/**
	 * @uml.property  name="password"
	 */
	private String password;

	@Transient
	private String NotCryptPassword;
	/**
	 * @uml.property  name="number"
	 */
	@Id
	// Subscribers are defined by their numbers
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private long number;

	/**
	 * @uml.property  name="entitles"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" inverse="mainSubscriber:subscribersManagement.Entitled"
	 * @uml.association  name="subscriberEntitled"
	 */

	@OneToMany(mappedBy = "mainSubscriber",cascade = CascadeType.ALL)
	// One subscriber to many entitled
	private List<Entitled> entitles;

	/**
	 * @uml.property  name="ageCategory"
	 * @uml.associationEnd  
	 */
	@ManyToOne
	@JoinColumn(name = "ageCategory")
	private AgeCategory ageCategory = null;

	// @Transient
//	 private static int numberCreated=0;

	/**
	 * @uml.property  name="currentReservation"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" inverse="subscriber:reservationManagement.Reservation"
	 */
	//,fetch=FetchType.EAGER
	@OneToMany(mappedBy = "subscriber")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Reservation> currentReservation;

	public Subscriber() {
	}

	/**
	 * Constructor of a subscriber Generates the number of the subscriber
	 * 
	 * @params firstName first name of the subscriber
	 * @params lastName last name of the subscriber
	 * @params bornDate born date of the subscriber
	 * @throws BadParametersException
	 * @throws NoSuchAlgorithmException
	 * @throws BadStringException
	 * @throws NullPointerException
	 * @throws BadBornDateException
	 */
	public Subscriber(String firstName, String lastName, Calendar bornDate,
			String email) throws BadParametersException,
			NoSuchAlgorithmException, NullPointerException, BadStringException,
			BadBornDateException {

		setFirstName(firstName);
		setLastName(lastName);
		setBornDate(bornDate);
		this.setEmail(email);
		this.NotCryptPassword = generatePassword();

		try {
			this.setPassword(MD5(NotCryptPassword));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (BadStringException e) {
			System.err.println("MinLengthPassword must be under 25 chars");
		}
//		 this.number = numberCreated+1;
//		 numberCreated++;
		this.currentLoans = new ArrayList<Loan>();
		this.entitles = new ArrayList<Entitled>();
		this.currentReservation = new ArrayList<Reservation>();

	}

	public void addEntitled(Entitled entitled) {
		this.entitles.add(entitled);
	}

	/**
	 * @throws ReserveBookException
	 */
	public void cancelReservation(Reservation reservation)
			throws ReserveBookException {
		if (reservation == null)
			throw new NullPointerException();

		if (!currentReservation.remove(reservation))
			throw new ReserveBookException();
	}

	/**
	 * @return true if the subscriber can lend books
	 */
	public boolean canLend() {
		return currentLoans.size() < Constraints.maxLOANS;
	}

	/**
	 */
	public boolean canReserve() {
		return currentReservation.size() < Constraints.maxRESERVATIONS;
	}

	/***
	 * Decides if the subscriber object (parameter) is the same subscriber as
	 * this one Same first name, last name, and born date
	 * 
	 * @param subscriber
	 *            object to be compared with this one
	 * 
	 * @return true if parameter object and this one are equal
	 * 
	 * @return false if parameter object is null or different from this one
	 */
	@Override
	public boolean equals(Object subscriber) {
		if (subscriber == null)
			return false;
		Subscriber s = (Subscriber) subscriber;

		if (s.number == number)
			return true;

		if ((s.firstName == null) || (s.lastName == null)
				|| (s.bornDate == null))
			return false;

		if ((this.firstName == null) || (this.lastName == null)
				|| (this.bornDate == null))
			return false;

		boolean res = (s.firstName.equals(firstName))
				&& (s.lastName.equals(lastName))
				&& (s.bornDate.equals(bornDate));

		return res;
	}

	/**
	 * @return true if the subscriber has loans
	 */
	public boolean existingLoans() {
		return currentLoans.size() > 0;
	}

	/**
	 * Generates a 26 chars password
	 * 
	 */
	private String generatePassword() {
		return new BigInteger(130, new Random()).toString(32);
	}

	/**
	 * Getter of the property <tt>ageCategory</tt>
	 * 
	 * @return Returns the ageCategory.
	 * @uml.property name="ageCategory"
	 */
	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	/**
	 * Getter of the property <tt>bornDate</tt>
	 * 
	 * @return Returns the bornDate.
	 * @uml.property name="bornDate"
	 */
	public Calendar getBornDate() {
		return bornDate;
	}

	/**
	 * Getter of the property <tt>currentLoans</tt>
	 * 
	 * @return Returns the currentLoans.
	 * @uml.property name="currentLoans"
	 */
	public ArrayList<Loan> getCurrentLoans() {
		return currentLoans;
	}
	
	public ArrayList<Loan> getLoans() {
		return (ArrayList<Loan>) currentLoans.clone();
	}
	
	/**
	 * Getter of the property <tt>currentReservation</tt>
	 * 
	 * @return Returns the currentReservation.
	 * @uml.property name="currentReservation"
	 */
	public List<Reservation> getCurrentReservation() {
		return currentReservation;
	}

	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Getter of the property <tt>entitles</tt>
	 * 
	 * @return Returns the entitleds.
	 * @uml.property name="entitles"
	 */
	public List getEntitles() {
		return this.entitles;
	}

	/**
	 * Getter of the property <tt>firstName</tt>
	 * 
	 * @return Returns the firstName.
	 * @uml.property name="firstName"
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter of the property <tt>lastname</tt>
	 * 
	 * @return Returns the lastname.
	 * @uml.property name="lastName"
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter of the property <tt>number</tt>
	 * 
	 * @return Returns the number.
	 * @uml.property name="number"
	 */
	public long getNumber() {
		return number;
	}

	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Add a new loan to the subscriber if quota ok
	 * 
	 * @param loan
	 *            the loan to add to the subscriber
	 * @throws TooManyLoansException
	 * @throws BadParametersException
	 */
	public void lend(Loan loan) throws TooManyLoansException,
			BadParametersException {
		if (loan == null)
			throw new BadParametersException();

		if (!canLend())
			throw new TooManyLoansException();

		currentLoans.add(loan);
	}

	/**
	 * crypting the String by md5 algorithm
	 * 
	 * @param p
	 *            String to crypt
	 * @return the crypted string
	 */
	private String MD5(String p) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(p.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);

		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}

	public void removeEntitled(Entitled entitled) {
		this.entitles.remove(entitled);
	}

	/**
	 * @throws ReserveBookException 
		 */
	public void reserve(Reservation reservation) throws TooManyReservationException {
		if (reservation == null) {
			throw new NullPointerException();
		}
		if (!canReserve()){
			throw new TooManyReservationException();
		}
		
		this.currentReservation.add(reservation);
	}

	/**
	 * Remove the loan in parameter from the list of current loans of the
	 * subscriber
	 * 
	 * @param loan
	 *            the loan to be finished
	 * @throws BadParametersException
	 * @throws LentBookException
	 *             if loan not found in the list
	 */
	public void returnBook(Loan loan) throws BadParametersException,
			LentBookException {
		if (loan == null)
			throw new BadParametersException();

		if (!currentLoans.remove(loan))
			throw new LentBookException("The lender does not contain the loan");
	}

	public void sendMail() {
		String to = this.email;
		String from = "biblio@library.com";
		String host = "localhost";

		Properties properties = System.getProperties();

		properties.setProperty("mail.smtp.host", host);

		javax.mail.Session session = javax.mail.Session
				.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			message.setSubject("Your password");

			message.setText("This is your password:" + this.NotCryptPassword);

			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	/**
	 * Setter of the property <tt>ageCategory</tt>
	 * 
	 * @param ageCategory
	 *            The ageCategory to set.
	 * @uml.property name="ageCategory"
	 */
	public void setAgeCategory(AgeCategory ageCategory)
			throws NullPointerException {
		if (ageCategory == null) {
			throw new NullPointerException();
		}
		this.ageCategory = ageCategory;
	}

	/**
	 * Setter of the property <tt>bornDate</tt>
	 * 
	 * @param bornDate
	 *            The bornDate to set.
	 * @uml.property name="bornDate"
	 */
	public void setBornDate(Calendar bornDate) throws NullPointerException,
			BadBornDateException {
		if (bornDate == null) {
			throw new NullPointerException();
		}
		if (bornDate.after(new GregorianCalendar())) {
			throw new BadBornDateException();
		}
		this.bornDate = bornDate;
	}

	/**
	 * Setter of the property <tt>currentLoans</tt>
	 * 
	 * @param currentLoans
	 *            The currentLoans to set.
	 * @uml.property name="currentLoans"
	 */
	public void setCurrentLoans(ArrayList<Loan> currentLoans)
			throws NullPointerException {
		if (currentLoans == null) {
			throw new NullPointerException();
		}
		this.currentLoans = currentLoans;
	}

	/**
	 * Setter of the property <tt>currentReservation</tt>
	 * 
	 * @param currentReservation
	 *            The currentReservation to set.
	 * @uml.property name="currentReservation"
	 */
	public void setCurrentReservation(ArrayList currentReservation) {
		this.currentReservation = currentReservation;
	}

	/**
	 * @param email
	 * @throws BadStringException
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) throws BadStringException {
		if (email == null) {
			throw new NullPointerException();
		}

		Pattern p = Pattern.compile("^[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
		Matcher m = p.matcher(email);

		if (m.find()) {
			this.email = email;
		} else {
			throw new BadStringException();
		}
	}

	/**
	 * Setter of the property <tt>entitles</tt>
	 * 
	 * @param entitles
	 *            The entitleds to set.
	 * @uml.property name="entitles"
	 */
	public void setEntitles(List<Entitled> entitles)
			throws NullPointerException {
		if (entitles == null) {
			throw new NullPointerException();
		}
		this.entitles = entitles;
	}

	/**
	 * Setter of the property <tt>firstName</tt>
	 * 
	 * @param firstName
	 *            The firstName to set.
	 * @uml.property name="firstName"
	 */
	public void setFirstName(String firstName) throws NullPointerException,
			BadStringException {
		if (firstName == null) {
			throw new NullPointerException();
		}

		Pattern p = Pattern.compile("^[A-Z|ÀÂÉÈÔÙÛÇ][^0-9^A-Z^ÀÂÉÈÔÙÛÇ]*$");
		Matcher m = p.matcher(firstName);

		if (m.find()) {
			this.firstName = firstName;
		} else {
			throw new BadStringException();
		}
	}

	/**
	 * Setter of the property <tt>lastname</tt>
	 * 
	 * @param lastname
	 *            The lastname to set.
	 * @uml.property name="lastName"
	 */
	public void setLastName(String lastName) throws NullPointerException,
			BadStringException {

		if (lastName == null) {
			throw new NullPointerException();
		}

		Pattern p = Pattern.compile("^[A-ZÀÂÉÈÔÙÛÇ]+[\\s-_'A-ZÀÂÉÈÔÙÛÇ]*$");
		Matcher m = p.matcher(lastName);

		if (m.find()) {
			this.lastName = lastName;
		} else {
			throw new BadStringException();
		}
	}

	/**
	 * Setter of the property <tt>number</tt>
	 * 
	 * @param number
	 *            The number to set.
	 * @throws BadParametersException
	 * @uml.property name="number"
	 */
	public void setNumber(long number) throws BadParametersException {
		if (number < 0) {
			throw new BadParametersException();
		}
		this.number = number;
	}

	/**
	 * @param password
	 * @throws NullPointerException
	 * @throws BadStringException
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) throws NullPointerException,
			BadStringException {
		if (password == null) {
			throw new NullPointerException();
		}
		if (password.length() < Constraints.minLengthPassword) {
			throw new BadStringException();
		}
		this.password = password;
	}

	/**
	 * Returns a description of the subscriber
	 */
	@Override
	public String toString() {
		return this.getNumber() + " " + this.firstName + ", " + this.lastName;
	}
}
