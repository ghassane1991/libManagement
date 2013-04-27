/**
 * 
 */
package subscribersManagement;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.persistence.*;

import exceptions.BadBornDateException;
import exceptions.BadParametersException;
import exceptions.BadStringException;

/** 
 * @author user
 */
@Entity
@PrimaryKeyJoinColumn(name="entitled_id")
@Table(name="entitled")
public class Entitled extends Subscriber implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public Entitled() {
		// TODO Auto-generated constructor stub
		super();
	}

/**
 * Constructor of a subscriber Generates the number of the subscriber
 * 
 * @params firstName first name of the subscriber
 * @params lastName last name of the subscriber
 * @params bornDate born date of the subscriber
 * @throws BadParametersException
 * @throws NoSuchAlgorithmException 
 * @throws BadBornDateException 
 * @throws BadStringException 
 * @throws NullPointerException 
 */

	public Entitled(String firstName, String lastName, Calendar bornDate, String email, Subscriber subscriber)
			throws BadParametersException, NoSuchAlgorithmException, NullPointerException, BadStringException, BadBornDateException {
		super (firstName, lastName, bornDate, email);
		if (subscriber == null)
			throw new BadParametersException();
		this.mainSubscriber=subscriber;
	}


	/** 
	 * @uml.property name="mainSubscriber"
	 * @uml.associationEnd inverse="entitles:subscribersManagement.Subscriber"
	 * @uml.association name="subscriberEntitled"
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "subscriber_ref")
	private Subscriber mainSubscriber;

	public String toString() {
		return super.toString() + " as entitled of " + this.mainSubscriber.toString();
	}

	/** 
	 * Getter of the property <tt>mainSubscriber</tt>
	 * @return  Returns the subscriber.
	 * @uml.property  name="mainSubscriber"
	 */
	public Subscriber getMainSubscriber() {
		return mainSubscriber;
	}

	/** 
	 * Setter of the property <tt>mainSubscriber</tt>
	 * @param mainSubscriber  The subscriber to set.
	 * @uml.property  name="mainSubscriber"
	 */
	public void setMainSubscriber(Subscriber mainSubscriber) {
		if (this.mainSubscriber != null) {
			if (this.mainSubscriber == mainSubscriber) return;
			else {
				// this.mainSubscriber.removeEntitled(this);
			}
		}
		this.mainSubscriber = mainSubscriber;
		//this.mainSubscriber.addEntitled(this);
		
	}

}
