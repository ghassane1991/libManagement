package ui;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jpaUtils.JPAUtil;

import loansManagement.Loan;
import main.Library;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;

import exceptions.BookExistsException;
import exceptions.ReserveBookException;

import booksManagement.BookDAO;

import reservationManagement.Reservation;
import reservationManagement.ReservationDAO;
import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;

/**
 * @author  user
 */
public class ReservationUI extends Shell {
	private Text reservbookID;
	private Text reservCancelBookID;
	private Table table;
	private Spinner reservSubsNum;
	/**
	 * @uml.property  name="lib"
	 * @uml.associationEnd  
	 */
	private Library lib = new Library();
	private Spinner reservCancelSpin;
	public static boolean opened= false;
	
	private final Shell s = this;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ReservationUI shell = new ReservationUI(display);
			
			Rectangle bounds = display.getBounds();
			Rectangle rect = shell.getBounds ();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;
			shell.setLocation (x, y);
			
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void fillTable(){
		List<Reservation> rs = lib.getReservations();
		List<Subscriber>  ls = lib.SubscriberList();
		List<booksManagement.Book> bs = lib.bookList();
		
		
		for (booksManagement.Book b : bs){
			 for(Long subsNum : b.getReservationList()){
				 Subscriber subs = lib.SearchSubscriber(s, subsNum);
				 if (subs.getCurrentReservation().isEmpty()){
					 b.removeSubscriber(subs);
					 
					EntityManager em;
					EntityTransaction tx;
					
					em = JPAUtil.getEntityManager();
					tx = em.getTransaction();
					tx.begin();
						
					BookDAO bdao = new BookDAO();
					try {
						booksManagement.Book bbb = bdao.get(b.getIsbn());
						bbb.removeSubscriber(subs);
						em.merge(bbb);
					} catch (BookExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					tx.commit();
					JPAUtil.closeEntityManager();
				 }
			 }
		}
		
		
		//List<Reservation> rs = lib.getReservations();
		
		table.removeAll();
		table.setRedraw(false);
		 
	    for(Iterator iterator = rs.iterator();iterator.hasNext();){
	    	Reservation r = (Reservation) iterator.next();
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	int c = 0;
	    	item.setText(c++, String.valueOf(r.getSubscriber().getNumber()));
	    	item.setText(c++, String.valueOf(r.getSubscriber().getFirstName() + " " + r.getSubscriber().getLastName()));
	    	item.setText(c++, r.getBook().getIsbn());
	    	item.setText(c++, r.getBook().getTitle());
	    	item.setText(c++, r.getStartDate().getTime().toString());
	    	item.setText(c++, r.getFinishDate().getTime().toString());
		}
		  
	   table.setRedraw(true);
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ReservationUI(Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		addShellListener(new ShellAdapter() {
		
			@Override
			public void shellClosed(ShellEvent e) {
				opened=false;
			}
			@Override
			public void shellActivated(ShellEvent e) {	
				fillTable();
			}
		});
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(10, 27, 910, 147);
		
		TabItem tbtmAddReservation = new TabItem(tabFolder, SWT.NONE);
		tbtmAddReservation.setText("add reservation");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmAddReservation.setControl(composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Subscriber number:");
		label.setBounds(101, 31, 147, 16);
		
		reservSubsNum = new Spinner(composite, SWT.BORDER);
		reservSubsNum.setMaximum(9999999);
		reservSubsNum.setBounds(308, 31, 135, 26);
		
		Label lblBookId = new Label(composite, SWT.NONE);
		lblBookId.setText("Book ISBN:");
		lblBookId.setBounds(101, 68, 119, 16);
		
		reservbookID = new Text(composite, SWT.BORDER);
		reservbookID.setBounds(305, 63, 138, 26);
		
		Button reserveButton = new Button(composite, SWT.NONE);
		reserveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				long subscriberID = (long)reservSubsNum.getSelection();
				lib.reserve(s,subscriberID, reservbookID.getText());
			    fillTable();
			}
		});
		reserveButton.setText("Reserve");
		reserveButton.setBounds(486, 61, 85, 28);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Cancel reservation");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_1);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("Subscriber number:");
		label_1.setBounds(98, 30, 147, 16);
		
		reservCancelSpin = new Spinner(composite_1, SWT.BORDER);
		reservCancelSpin.setMaximum(9999999);
		reservCancelSpin.setBounds(305, 30, 135, 26);
		
		Label lblBookIsbn = new Label(composite_1, SWT.NONE);
		lblBookIsbn.setText("Book ISBN:");
		lblBookIsbn.setBounds(98, 67, 119, 16);
		
		reservCancelBookID = new Text(composite_1, SWT.BORDER);
		reservCancelBookID.setBounds(302, 62, 138, 26);
		
		Button reservCancel = new Button(composite_1, SWT.NONE);
		reservCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				long subscriberID = (long)reservCancelSpin.getSelection();
				lib.cancelReservation(s,subscriberID, reservCancelBookID.getText());
			    fillTable();
			}
		});
		reservCancel.setText("Cancel");
		reservCancel.setBounds(483, 60, 85, 28);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 190, 910, 370);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnSubscriberNum = new TableColumn(table, SWT.NONE);
		tblclmnSubscriberNum.setWidth(109);
		tblclmnSubscriberNum.setText("Subscriber num");
		
		TableColumn tblclmnSubscriberIdentity = new TableColumn(table, SWT.NONE);
		tblclmnSubscriberIdentity.setWidth(157);
		tblclmnSubscriberIdentity.setText("Subscriber identity");
		
		TableColumn tblclmnBookIsbn = new TableColumn(table, SWT.NONE);
		tblclmnBookIsbn.setWidth(140);
		tblclmnBookIsbn.setText("Book isbn");
		
		TableColumn tblclmnBookTitle = new TableColumn(table, SWT.NONE);
		tblclmnBookTitle.setWidth(250);
		tblclmnBookTitle.setText("Book title");
		
		TableColumn tblclmnStartDate = new TableColumn(table, SWT.NONE);
		tblclmnStartDate.setWidth(143);
		tblclmnStartDate.setText("Start date");
		
		TableColumn tblclmnFinishDate = new TableColumn(table, SWT.NONE);
		tblclmnFinishDate.setWidth(100);
		tblclmnFinishDate.setText("Finish date");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Reservation management");
		setSize(932, 601);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
