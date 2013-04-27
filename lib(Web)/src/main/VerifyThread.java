package main;


import java.util.TimerTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import exceptions.ReserveBookException;

import reservationManagement.ReservationDAO;

import jpaUtils.JPAUtil;
import loansManagement.HistoryDAO;
// This class represents a TimerTask that verify if there is some loans which exceeded the delays and if some reservation which finished
public class VerifyThread extends TimerTask {
	
	private HistoryDAO h = new HistoryDAO();
	/*
	 * This task will be used in the Main program, it will be executed every day
	 * 
	 * Main:
	 * Timer t = new Timer();
	 * t.schedule(new VerifyThread(),5000,86400000);
	 */
	public void run(){
			EntityManager em = JPAUtil.getEntityManager();
			EntityTransaction tx = em.getTransaction();
			
			tx.begin();
			try {
				new ReservationDAO().verify();
				h.verify();
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
