package subscribersManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import booksManagement.Book;

import exceptions.AgeCategoryExistsException;
import exceptions.AgeCategoryOverLapException;
import exceptions.BadParametersException;
import exceptions.BadStringException;
import exceptions.BookExistsException;
import exceptions.EmptyStringException;
import exceptions.SubscriberExistsException;

import jpaUtils.JPAUtil;

public class AgeCategoryDAO {
	public boolean isEmpty() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (entityManager.createNamedQuery("findAllAgeCategories")
				.getResultList().isEmpty())
			return true;
		else
			return false;
	}

	public List<AgeCategory> getContent() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		return ((List<AgeCategory>) entityManager.createNamedQuery("findAllAgeCategories").getResultList());
	}

	public boolean add(AgeCategory ageCategory) throws AgeCategoryOverLapException, AgeCategoryExistsException{
		EntityManager entityManager = JPAUtil.getEntityManager();
		
		if(contains(ageCategory)){
			throw new AgeCategoryExistsException(); //Verify if the book already exists in the database
		}
		//Make sure if there is no overlap
		for (AgeCategory ag : getContent()){ 
			if((ag.getMin() < ageCategory.getMax() && ageCategory.getMax() < ag.getMax()) || (ag.getMin() < ageCategory.getMin() && ageCategory.getMin() < ag.getMax()) ){
				throw new AgeCategoryOverLapException(); 
			}
		}
		
		try {
			entityManager.persist(ageCategory);
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
		List<AgeCategory> theAgeCategories = (List<AgeCategory>) entityManager.createNamedQuery("findAllAgeCategories").getResultList();
		for (AgeCategory ac : theAgeCategories)
			result += ac.toString() + "\n";
		return result;
	}
// search for the age category by name
	public AgeCategory get(String cat_name) throws AgeCategoryExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		AgeCategory ac = (AgeCategory) entityManager.find(AgeCategory.class, cat_name);
		if (ac == null)
			throw new AgeCategoryExistsException();
		else
			return ac;
	}

	/**
		 */
	public long size() {
		EntityManager entityManager = JPAUtil.getEntityManager();
		return (entityManager.createNamedQuery("findAllAgeCategories")
				.getResultList().size());
	}

	/**
	 * @throws SubscriberExistsException 
		 */
	public boolean remove(AgeCategory ageCategory) throws AgeCategoryExistsException, SubscriberExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (ageCategory == null)
			throw new AgeCategoryExistsException();
		else if(contains(ageCategory) == false) //Verify if the age category already exists
			throw new AgeCategoryExistsException();
		else if (ageCategory.getSubscribers().size() > 0){ //Verify if no subscribers in the age category
			throw new SubscriberExistsException();
		}
		else
			try {
				entityManager.remove(ageCategory);
			} catch (Exception pe) {
				System.err.println("problem when deleting an entity ");
				pe.printStackTrace();
				return false;
			}
		return true;
	}

	public boolean contains(AgeCategory ageCategory) throws AgeCategoryExistsException {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if (ageCategory == null)
			throw new AgeCategoryExistsException();
		AgeCategory ac = (AgeCategory) entityManager.find(AgeCategory.class,
				ageCategory.getName());
		if (ac == null)
			return false;
		else
			return ac.equals(ageCategory);
	}

	
	// Modify an age category 
	
			public void modify(String name, int max,int min) throws AgeCategoryExistsException, AgeCategoryOverLapException, BadParametersException, EmptyStringException, BadStringException {
				EntityManager entityManager=JPAUtil.getEntityManager();
				AgeCategory ac = (AgeCategory) entityManager.find(AgeCategory.class, name);
				if (ac == null)
					throw new AgeCategoryExistsException();
				if(max <= min){
					throw new BadParametersException();
				}
				else {
					
					//Make sure if there is no overlap
					for (AgeCategory ag : getContent()){ 
						if(ag.getMax() > min && max > ag.getMax()){
							throw new AgeCategoryOverLapException(); 
						}
					}
					ac.setName(name);
					ac.setMax(max);
					ac.setMin(min);
				}
				try{ 
					entityManager.merge(ac);
				} catch (Exception e) {
					System.err.println("Problem when saving");
					e.printStackTrace();
					
				}
			}
}
