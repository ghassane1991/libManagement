package control;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jpaUtils.JPAUtil;

public class HibernateListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		JPAUtil.getEntityManagerFactory().close(); // Free all resources
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		JPAUtil.getEntityManagerFactory(); // Just call the static initializer of that class  
	}

}
