package main;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jpaUtils.JPAUtil;
import reservationManagement.ReservationDAO;
import exceptions.ReserveBookException;

/**
 * @author  user
 */
public class VerifyReservation implements Runnable {
	/**
	 * @uml.property  name="r"
	 * @uml.associationEnd  
	 */
	ReservationDAO r = new ReservationDAO();
	@Override
	public void run() {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		
		try {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			r.verify();
			
		} catch (ReserveBookException e) {
			e.printStackTrace();
		}catch (Exception pe) {
			System.err.println("Exception when persisting data");
			JPAUtil.closeEntityManager();
			pe.printStackTrace();
		}
		
		tx.commit();
		JPAUtil.closeEntityManager();
	}

}
