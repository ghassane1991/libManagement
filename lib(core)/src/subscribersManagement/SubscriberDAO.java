package subscribersManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import exceptions.*;
import subscribersManagement.Subscriber;
import javax.persistence.*;

import jpaUtils.JPAUtil;

public class SubscriberDAO {

	/** 
		 */
	public boolean isEmpty() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllSubscribers")
				.getResultList().isEmpty())
			return true;
		else
			return false;
	}

	public boolean add(Subscriber subscriber) throws SubscriberExistsException, EmailExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if(contains(subscriber)) { //Verify if the subscriber already exists
			throw new SubscriberExistsException();
		}
		for (Subscriber s : getContent()){
			if (s.getEmail().equals(subscriber.getEmail())){
				throw new EmailExistsException();
			}
		}
		try {
			entityManager.persist(subscriber);
		} catch (Exception e) {
			System.err.println("Problem when saving");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean add(Entitled entitled) throws SubscriberExistsException, EmailExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if(contains(entitled)){//Verify if the entitled already exists
			throw new SubscriberExistsException();
		}
		for (Subscriber s : getContent()){
			if (s.getEmail().equals(entitled.getEmail())){
				throw new EmailExistsException();
			}
		}
		try {
			entityManager.persist(entitled);
		} catch (Exception e) {
			System.err.println("Problem when saving");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String toString() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		String result = "";
		List<Subscriber> theSubscribers = (List<Subscriber>) entityManager
				.createNamedQuery("findAllSubscribers").getResultList();
		for (Subscriber s : theSubscribers)
			result += s.toString() + "\n";
		return result;
	}

	/**
		 */
	public Subscriber get(long subscriberId) throws SubscriberExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		Subscriber s = (Subscriber) entityManager.find(Subscriber.class,
						subscriberId);
		if (s == null)
			throw new SubscriberExistsException();
		else
			return s;
	}

	// Search for the subscribers with the same first and last name.  
	public List<Subscriber> get(String firstName, String lastName, Calendar bornDate) throws SubscriberExistsException {
		List<Subscriber> subs = new ArrayList<Subscriber>();
		for (Subscriber s : getContent()){
			if(s.getFirstName().equals(firstName) && s.getLastName().equals(lastName) && s.getBornDate().equals(bornDate)){ 
				subs.add(s);
			}
			
		}
		if (subs.size() == 0)
			throw new SubscriberExistsException();
		else
			return subs;
	}
	/**
		 */
	public long size() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		return (entityManager.createNamedQuery("findAllSubscribers")
				.getResultList().size());
	}

	/**
	 * @throws SubscriberWithLoansException 
		 */
	public boolean remove(Subscriber subscriber)
			throws SubscriberExistsException, SubscriberWithLoansException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (subscriber == null)
			throw new SubscriberExistsException();
		if (contains(subscriber) == false) // Make sure the subscriber exists in the database
			throw new SubscriberExistsException();
		else if (subscriber.existingLoans() == true) //Verify if the subscriber has no loans
			throw new SubscriberWithLoansException();
		else
			try {
				entityManager.remove(subscriber);
			} catch (Exception pe) {
				System.err.println("problem when deleting an entity ");
				pe.printStackTrace();
				return false;
			}
		return true;
	}

	/**
			 */
	public boolean contains(Subscriber subscriber)
			throws SubscriberExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (subscriber == null)
			throw new SubscriberExistsException();
		Subscriber s = (Subscriber) entityManager.find(Subscriber.class,
				subscriber.getNumber());
		if (s == null)
			return false;
		else
			return s.equals(subscriber);
	}

	/**
			 */
	public List<Subscriber> getContent() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		return ((List<Subscriber>) entityManager.createNamedQuery(
				"findAllSubscribers").getResultList());
	}

	/**
				 */
	public Subscriber GetSingleSubscriberByLastName(String lastName)
			throws SubscriberExistsException {
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em
				.createQuery("select s from Subscriber s where s.lastName='"
						+ lastName + "'");
		Subscriber subscriber = (Subscriber) query.getSingleResult();
		if (subscriber == null)
			throw new SubscriberExistsException();
		else
			return subscriber;
	}
	
	// Modify an subscriber
	
	public void modify(long number, String firstName,String lastName, Calendar bornDate,String email, AgeCategory ageCategory) throws  SubscriberExistsException, BadAgeCategoryException, AgeCategoryExistsException, BadParametersException, NullPointerException, BadStringException, BadBornDateException, EmailExistsException {
			EntityManager entityManager=JPAUtil.getEntityManager();
			Subscriber s = (Subscriber) entityManager.find(Subscriber.class,number);
			
			if (s == null)
				throw new SubscriberExistsException();
		
			for (Subscriber sa : getContent()){
				if (sa.getEmail().equals(email) && !s.getEmail().equals(email)){
					throw new EmailExistsException();
				}
			}

				AgeCategoryDAO acDAO =  new AgeCategoryDAO();
				AgeCategory ac1 = null;
				if (s.getAgeCategory() != null){
					ac1 = acDAO.get(s.getAgeCategory().getName());
				}
				AgeCategory ac2 = acDAO.get(ageCategory.getName());
			
			

			s.setNumber(number);
			s.setFirstName(firstName);
			s.setEmail(email);
			s.setLastName(lastName);
			s.setBornDate(bornDate);
						
						
			if (ac1 != null){
					ac1.removeSubscriber(s);
			}
			s.setAgeCategory(ac2);
			ac2.addSubscriber(s);
						
		
			try{ 
						entityManager.merge(s);
						
						if (ac1 != null){
							entityManager.merge(ac1);
						}
						
						entityManager.merge(ac2);
			} catch (Exception e) {
					    System.err.println("Problem when saving");
						e.printStackTrace();
						
			}
	}
	
	public void modify(long number, String firstName,String lastName, Calendar bornDate,String email) throws  SubscriberExistsException, BadAgeCategoryException, AgeCategoryExistsException, BadParametersException, NullPointerException, BadStringException, BadBornDateException, EmailExistsException {
		EntityManager entityManager=JPAUtil.getEntityManager();
		Subscriber s = (Subscriber) entityManager.find(Subscriber.class,number);
		
		if (s == null)
			throw new SubscriberExistsException();
	
		for (Subscriber sa : getContent()){
			if (sa.getEmail().equals(email) && !s.getEmail().equals(email)){
				throw new EmailExistsException();
			}
		}


		s.setNumber(number);
		s.setFirstName(firstName);
		s.setEmail(email);
		s.setLastName(lastName);
		s.setBornDate(bornDate);
			
		try{ 
					entityManager.merge(s);			
		} catch (Exception e) {
				    System.err.println("Problem when saving");
					e.printStackTrace();
					
		}
}
}
