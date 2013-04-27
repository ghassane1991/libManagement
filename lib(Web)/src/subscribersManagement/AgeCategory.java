package subscribersManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import exceptions.BadAgeCategoryException;
import exceptions.BadParametersException;
import exceptions.EmptyStringException;

@Entity
@Table(name = "ageCategories")
@NamedQuery(name = "findAllAgeCategories", query = "select ac from AgeCategory ac")
public class AgeCategory {

	/**
	 * @uml.property name="name"
	 */
	@Id //Name define the age category
	@Column(name = "Cat_name",nullable=false)
	private String name; 

	/**
	 * @uml.property name="min"
	 */
	@Column(name = "min",nullable=false)
	private int min;

	/**
	 * @uml.property name="max"
	 */
	@Column(name = "max",nullable=false)
	private int max;

	/** 
	 * @uml.property name="subscribers"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true" inverse="ageCategory:subscribersManagement.Subscriber"
	 */
	@OneToMany(mappedBy = "ageCategory")
	private List<Subscriber> subscribers;

	/**
	 * @throws EmptyStringException 
		 */
	public AgeCategory(String name, int min, int max) throws BadParametersException, EmptyStringException{
		if (name == null || min < 0 || max < min ){
			throw new BadParametersException();
		}
		setName(name);
		this.min = min;
		this.max = max;
		this.subscribers = new ArrayList<Subscriber>();
	}

	public AgeCategory() {

	}

	/**
	 * Getter of the property <tt>max</tt>
	 * 
	 * @return Returns the max.
	 * @uml.property name="max"
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Getter of the property <tt>min</tt>
	 * 
	 * @return Returns the min.
	 * @uml.property name="min"
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Getter of the property <tt>name</tt>
	 * 
	 * @return Returns the name.
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/** 
	 * Getter of the property <tt>subscribers</tt>
	 * @return Returns the subscribers.
	 * @uml.property name="subscribers"
	 */
	public List getSubscribers() {
		return subscribers;
	}

	/**
	 * Setter of the property <tt>max</tt>
	 * 
	 * @param max
	 *            The max to set.
	 * @uml.property name="max"
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Setter of the property <tt>min</tt>
	 * 
	 * @param min
	 *            The min to set.
	 * @uml.property name="min"
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * 
	 * @param name
	 *            The name to set.
	 * @throws EmptyStringException 
	 * @uml.property name="name"
	 */
	public void setName(String name) throws EmptyStringException {
		if (name.length() == 0)
			throw new EmptyStringException();
		this.name = name;
	}

	/**
	 * Add new subscriber to the age category after calculating his age
	 * @throws BadAgeCategoryException
	 * @param subscriber
	 *            The subscribers to add.
	 */
	public void addSubscriber(Subscriber subscriber) //
			throws BadAgeCategoryException {
		Calendar c = new GregorianCalendar();
		int age = c.get(Calendar.YEAR)
				- subscriber.getBornDate().get(Calendar.YEAR);
		if (age < max && age >= min) {
			this.subscribers.add(subscriber);
			subscriber.setAgeCategory(this);
		} else {
			throw new BadAgeCategoryException();
		}
	}
	
	/**
	 * Move the subscriber to the new age category after calculating his age
	 * @throws BadAgeCategoryException
	 * @param subscriber
	 *            The subscribers to add.
	 * @param ageCategory
	 *            Subscriber's old age category.
	 */
	public void addSubscriber(Subscriber subscriber, AgeCategory ageCategory)
			throws BadAgeCategoryException {
		Calendar c = new GregorianCalendar();
		int age = c.get(Calendar.YEAR)
				- subscriber.getBornDate().get(Calendar.YEAR);
		if (age < max && age >= min) {
			ageCategory.removeSubscriber(subscriber);
			subscriber.setAgeCategory(this);
			this.subscribers.add(subscriber);
		} else {
			throw new BadAgeCategoryException();
		}
	}
	/**
	 * Remove subscriber from the age category 
	 * @throws BadAgeCategoryException
	 * @param subscriber
	 *            The subscribers to remove.
		 */
	public void removeSubscriber(Subscriber subscriber) {
		subscribers.remove(subscribers.indexOf(subscriber));
	}
	
	/**
	 * Returns a description of the age category
	 */
	public String toString(){
		String s =  name + ": "+ new Integer(min).toString() +"-> "+ new Integer(max).toString();
		for (Subscriber  sub : subscribers){
			s = s + "   " + sub.toString();
		}
		return s;
	}
    
	public boolean equals(Object ageCategory){
		AgeCategory ag = (AgeCategory) ageCategory;
		if (ag.getName().equals(this.getName())){
			return true;
		}
		else
		{
			return false;
		}
	}

	/** 
	 * Setter of the property <tt>subscribers</tt>
	 * @param subscribers The subscribers to set.
	 * @uml.property  name="subscribers"
	 */
	public void setSubscribers(List subscribers) {
		this.subscribers = subscribers;
	}
}
