package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.SubscriberExistsException;

import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;

/**
 * Servlet implementation class Index
 */
@WebServlet("/Index.jsp")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("ID") == null)
			this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
		else
			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SubscriberDAO sDAO=new SubscriberDAO();
		Subscriber s = null;
		String n = request.getParameter("name");
		PrintWriter out = response.getWriter();

		if (n==""){
			out.println("<script type=\"text/javascript\"> ");
			out.println("alert(\"Wrong user name\");");
			out.println("window.location = \"connexion.jsp\";");
			out.println("</script> ");
			return;
			//this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
		}
		
		if (!isNumeric(n)){
			out.println("<script type=\"text/javascript\"> ");
			out.println("alert(\"Wrong user name\");");
			out.println("window.location = \"connexion.jsp\";");
			out.println("</script> ");
			return;
			//this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
		}
		int userID = Integer.parseInt(n);
		String userPass = request.getParameter("pass");
		
		try {
			s = sDAO.get(userID);
			if (!MD5(userPass).equals(s.getPassword())){
				out.println("<script type=\"text/javascript\"> ");
				out.println("alert(\"Wrong password\");");
				out.println("window.location = \"connexion.jsp\";");
				out.println("</script> ");
				//this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
			}
			else{
				HttpSession session = request.getSession();
				session.setAttribute("ID",userID);
				
				out.println("<script type=\"text/javascript\"> ");
				out.println("window.location = \"index.jsp\";");
				out.println("</script> ");
			}
			
		} catch (SubscriberExistsException e) {
			out.println("<script type=\"text/javascript\"> ");
			out.println("alert(\"Wrong user name\");");
			out.println("window.location = \"connexion.jsp\";");
			out.println("</script> ");
			//this.getServletContext().getRequestDispatcher( "/connexion.jsp" ).forward( request, response );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
		
	}
	private boolean isNumeric(String s){
		try{
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
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
}
