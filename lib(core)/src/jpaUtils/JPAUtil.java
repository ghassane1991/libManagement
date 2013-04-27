package jpaUtils;

import javax.persistence.*;

/**
 * Basic JPA helper class, handles EntityManagerFactory, EntityManager and EntityTransaction <p> Uses a static initializer for the initial EntityManagerFactory creation and holds EntityManager and EntityTransaction as singletons. </p> <p> Based on HibernateUtil by Christian Bauer christian@hibernate.org </p>
 * @author  Thomas Werner
 * @author  Maria-Teresa Segarra
 * @version  2012-03-05
 */

public class JPAUtil {

	private final static String PERSISTENCE_UNIT_NAME = "persistentLibrary";
	/**
	 * @uml.property  name="entityManagerFactory"
	 */
	private static EntityManagerFactory entityManagerFactory;
	/**
	 * @uml.property  name="entityManager"
	 */
	private static EntityManager entityManager;

	// Create the initial EntityManagerFactory with default configuration
	static {
		try {
			entityManagerFactory = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns the EntityManagerFactory used for this static class.
	 * @return  EntityManagerFactory
	 * @uml.property  name="entityManagerFactory"
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * Retrieves the current EntityManager. If no Session is open, opens a new EntityManager.
	 * @return  EntityManager
	 * @uml.property  name="entityManager"
	 */
	public static EntityManager getEntityManager() {

		if (entityManager == null) {
			// TODO tom: log.debug("Opening new Session for this thread.");
			entityManager = getEntityManagerFactory().createEntityManager();

		}
		return entityManager;
	}

	/**
	 * Closes the EntityManager.
	 */
	public static void closeEntityManager() {
		//entityManager = null;
		if ((null != entityManager) && entityManager.isOpen()) {
			// TODO tom: log.debug("Closing Session of this thread.");
			entityManager.close();
		}
		entityManager = null;
	}

}
