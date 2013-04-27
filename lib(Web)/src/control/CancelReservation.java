package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jpaUtils.JPAUtil;

import exceptions.AlreadyReservedException;
import exceptions.BadReturnDateException;
import exceptions.BookExistsException;
import exceptions.ReservationExistsException;
import exceptions.ReserveBookException;
import exceptions.SubscriberExistsException;
import exceptions.TooManyReservationException;

import reservationManagement.ReservationDAO;
import reservationManagement.ReservationID;
import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;

import booksManagement.Book;
import booksManagement.BookDAO;

/**
 * Servlet implementation class CancelReservation
 */
@WebServlet("/CancelReservation")
public class CancelReservation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelReservation() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("ID") == null)
			this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
		else{
			int subsNum =  (int) session.getAttribute("ID");
			SubscriberDAO subDAO= new SubscriberDAO();
			ReservationDAO bdao = new ReservationDAO();
			List<Book> rs = new ArrayList<Book>();
			try {
				rs = bdao.getReservedBook(subDAO.get(subsNum));
			} catch (SubscriberExistsException e) {

			}
			request.setAttribute("bookList", rs);
			this.getServletContext().getRequestDispatcher( "/WEB-INF/cancelreservation.jsp" ).forward( request, response );
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String book = request.getParameter("book");
		String[] bb = book.split(" ");
		String isbn = bb[0];
		
		HttpSession session = request.getSession();
		int subsNum =  (int) session.getAttribute("ID");
		
		BookDAO bdao = new BookDAO();
		SubscriberDAO sdao = new SubscriberDAO();
		
		ReservationDAO rdao = new ReservationDAO();
		
		EntityManager em;
		EntityTransaction tx;
		
		em = JPAUtil.getEntityManager();
		tx = em.getTransaction();
		tx.begin();
		
		try {
				Subscriber s = sdao.get(subsNum);
				Book b = bdao.get(isbn);
				
				reservationManagement.Reservation reservation=rdao.get(new ReservationID(b, s));
				reservation.cancel();
				rdao.remove(reservation);
				em.merge(s);
				em.merge(b);
		} catch (BookExistsException e) {
			this.getServletContext().getRequestDispatcher( "/messagebox.jsp?message=\"Book not found\"").forward( request, response );

			JPAUtil.closeEntityManager();
		} catch (SubscriberExistsException e) {
			this.getServletContext().getRequestDispatcher( "/messagebox.jsp?message=\"Subscriber not found\"").forward( request, response );

			JPAUtil.closeEntityManager();
		} catch (ReservationExistsException e) {
			this.getServletContext().getRequestDispatcher( "/messagebox.jsp?message=\"Reservation not found\"").forward( request, response );

			JPAUtil.closeEntityManager();
		} catch (ReserveBookException e) {
			this.getServletContext().getRequestDispatcher( "/messagebox.jsp?message=\"Book not reserved\"").forward( request, response );

			JPAUtil.closeEntityManager();
		}
		tx.commit();
		JPAUtil.closeEntityManager();
		this.getServletContext().getRequestDispatcher( "/messagebox.jsp?message=\"Reservation canceled\"").forward( request, response );

		this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );

	}

}
