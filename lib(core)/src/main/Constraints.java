/*
 * This class contains all the necessary constraints
 * for the library project
 * @author M.T. Segarra
 * @version 0.0.1
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * @author  user
 */
public class Constraints {
	
	/**
	 * @uml.property  name="MAX_LOANS" readOnly="true"
	 */
	public static int maxLOANS = 3;//10arg0
	/**
	 * @uml.property  name="MAX_RESERVES" readOnly="true"
	 */
	public static  int maxRESERVATIONS = 2; //5
	/**
	 * @uml.property  name="LOAN_DELAY" readOnly="true"
	 */
	public static  int loanDELAY = 100; //Days
	/**
	 * @uml.property  name="reservation_DELAY" readOnly="true"
	 */
	public static  int reservationDELAY = 15; //Days
	/**
	 * @uml.property  name="minLengthPassword" readOnly="true"
	 */
	public static final int minLengthPassword = 8;
	
	
	/**
	 * @param a
	 * @uml.property  name="MAX_LOANS"
	 */
	public static void setMaxLOANS(int a){	
		try {
			Properties prop = new Properties();
			  //load a properties file
    		prop.load(new FileInputStream("config.properties"));
			//set the properties value
			prop.setProperty("maxloans", String.valueOf(a));
			
			//save properties to project root folder
			prop.store(new FileOutputStream("config.properties"), null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param a
	 * @uml.property  name="MAX_RESERVES"
	 */
	public static void setMaxRESERVATIONS(int a){
		Properties prop = new Properties();
		try {
			  //load a properties file
    		prop.load(new FileInputStream("config.properties"));
			//set the properties value
			prop.setProperty("maxreservations", String.valueOf(a));
			
			//save properties to project root folder
			prop.store(new FileOutputStream("config.properties"), null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param a
	 * @uml.property  name="LOAN_DELAY"
	 */
	public static void setLoanDELAY(int a){
		try {
			Properties prop = new Properties();
			  //load a properties file
    		prop.load(new FileInputStream("config.properties"));
			//set the properties value
			prop.setProperty("loandelay", String.valueOf(a));
			
			//save properties to project root folder
			prop.store(new FileOutputStream("config.properties"), null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param a
	 * @uml.property  name="reservation_DELAY"
	 */
	public static void setReservationDELAY(int a){
		Properties prop = new Properties();
		try {
			  //load a properties file
    		prop.load(new FileInputStream("config.properties"));
			//set the properties value
			prop.setProperty("reservationdelay", String.valueOf(a));
			
			//save properties to project root folder
			prop.store(new FileOutputStream("config.properties"), null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Update all the constraints from properties file
	public static void update(){
		
		Properties prop = new Properties();
		
		File prp = new File("config.properties");
		if (prp.exists()){
	    	try {
	               //load a properties file
	    		prop.load(new FileInputStream("config.properties"));
	 
	               //get the property value and print it out
	             String ML = prop.getProperty("maxloans");
	             String MR = prop.getProperty("maxreservations");
	             String RD = prop.getProperty("reservationdelay");
	             String LD = prop.getProperty("loandelay");
	             
	             
	//             System.out.println(ML);
	//             System.out.println(MR);
	//             System.out.println(LD);
	//             System.out.println(RD);
	             
	             //update constraints
	             maxLOANS = Integer.parseInt(ML);
	             maxRESERVATIONS = Integer.parseInt(MR);
	             loanDELAY = Integer.parseInt(LD);
	             reservationDELAY = Integer.parseInt(RD);
	 
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
		}
		else{
			try {
				prop.store(new FileOutputStream("config.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//store constraints in the properties file
		public static void store(){
			
			Properties prop = new Properties();
			 
	    	try {
	    		//set the properties value
	    		
	    		
				prop.setProperty("loandelay", String.valueOf(loanDELAY));
				prop.setProperty("reservationdelay", String.valueOf(reservationDELAY));
				prop.setProperty("maxloans", String.valueOf(maxLOANS));
				prop.setProperty("maxreservations", String.valueOf(maxRESERVATIONS));
				
				//save properties to project root folder
				prop.store(new FileOutputStream("config.properties"), null);
	 
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
		}
}
