package ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jpaUtils.JPAUtil;

import loansManagement.Loan;
import main.Constraints;
import main.Library;
import main.Verify;
import main.VerifyReservation;
import main.VerifyThread;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import subscribersManagement.Subscriber;
import subscribersManagement.SubscriberDAO;
import swing2swt.layout.BoxLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Spinner;

import exceptions.BookCopyExistsException;
import exceptions.SubscriberExistsException;

import booksManagement.BookCopyDAO;

/**
 * @author  user
 */
public class LibUI {
	
	protected Shell shlBreizhLibraryV;
	private Text copyID;
	private Text returnCopyID;
	private Table loans;
	private Spinner subsNum;
	/**
	 * @uml.property  name="lib"
	 * @uml.associationEnd  
	 */
	private Library lib = new Library();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LibUI window = new LibUI();
			
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Fill table with existing loans
	 * @param args
	 */
	private void fillTable(){
		List<Loan> ls = lib.getLoans();
		
		/* If exception comment this code*/
		List<Subscriber> ss = lib.SubscriberList();
		
		for (Subscriber s : ss){
			List<Loan> lls = s.getLoans();
			for (Loan l : lls){
				boolean isRemoved = true;
				for(Loan ll : ls){
					if(l.equals(ll)){
						isRemoved = false;
						break;
					}
				}
				if(isRemoved){
					BookCopyDAO bcDAO = new BookCopyDAO();
					SubscriberDAO sDAO = new SubscriberDAO();
					
					l.getBookCopy().setLoan(null);
					s.getCurrentLoans().remove(l);
					
					EntityManager em;
					EntityTransaction tx;
					
					em = JPAUtil.getEntityManager();
					tx = em.getTransaction();
					tx.begin();
					
					try {
						
						 booksManagement.BookCopy bc = bcDAO.get(l.getBookCopy().getId());
						 bc.setLoan(null);
						 em.merge(bc);
						   
						 Subscriber sss = sDAO.get(l.getLender().getNumber());
						 sss.getCurrentLoans().remove(l);
						 em.merge(sss);
						 
					} catch (BookCopyExistsException er) {
						// TODO Auto-generated catch block
						er.printStackTrace();
					} catch (SubscriberExistsException ey) {
						// TODO Auto-generated catch block
						ey.printStackTrace();
					}
						
					tx.commit();
					JPAUtil.closeEntityManager();
				}
			}
			
		}
		
		/*----*/
		
		//List<Loan> ls = lib.getLoans();		
		 
		loans.removeAll();
		loans.setRedraw(false);
		 
	    for(Iterator iterator = ls.iterator();iterator.hasNext();){
	    	Loan l = (Loan) iterator.next();
	    	TableItem item = new TableItem(loans, SWT.NONE);
	    	int c = 0;
	    	item.setText(c++, String.valueOf(l.getLender().getNumber()));
	    	item.setText(c++, String.valueOf(l.getLender().getFirstName() + " " + l.getLender().getLastName()));
	    	item.setText(c++, l.getBookCopy().getId());
	    	item.setText(c++, l.getBookCopy().getBook().getTitle());
	    	item.setText(c++, l.getLoanDate().getTime().toString());
	    	item.setText(c++, l.getReturnDate().getTime().toString());
		}
		  
	   loans.setRedraw(true);
	}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		// Center the window
		Rectangle bounds = display.getBounds();
		Rectangle rect = shlBreizhLibraryV.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shlBreizhLibraryV.setLocation (x, y);
		//
		
		shlBreizhLibraryV.open();
		shlBreizhLibraryV.layout();
		while (!shlBreizhLibraryV.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlBreizhLibraryV = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		
		shlBreizhLibraryV.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				//new Verify().start();
				//Timer t = new Timer();
				//t.schedule(new VerifyThread(),5000,86400000);
				//t.schedule(new VerifyThread(),5000,5000);
				
				//Start verification thread to cancel reservation automatically if its delay is exceeded 
				//shlBreizhLibraryV.getDisplay().timerExec(5000, new VerifyReservation());
				
				
				
				fillTable();
				Constraints.update();
			}
		});
		shlBreizhLibraryV.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/bl.ico"));
		shlBreizhLibraryV.setSize(780, 529);
		shlBreizhLibraryV.setText("Breizh Library V1");
		shlBreizhLibraryV.setLayout(new FormLayout());
		
		ToolBar toolBar = new ToolBar(shlBreizhLibraryV, SWT.FLAT | SWT.RIGHT);
		FormData fd_toolBar = new FormData();
		fd_toolBar.bottom = new FormAttachment(0, 44);
		fd_toolBar.right = new FormAttachment(0, 778);
		fd_toolBar.top = new FormAttachment(0);
		fd_toolBar.left = new FormAttachment(0);
		toolBar.setLayoutData(fd_toolBar);
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// Verify if the window is already opened if not a new window will be opened
				if(!SubscriberManagement.opened){
					Display display = Display.getDefault();
					SubscriberManagement sm = new SubscriberManagement(display);
					SubscriberManagement.opened = true;
					
					//center the shell
					Rectangle bounds = display.getBounds();
					Rectangle rect = sm.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					sm.setLocation (x, y);
					
					sm.open();
				}
				
			}
		});
		tltmNewItem.setToolTipText("Subscriber management");
		tltmNewItem.setWidth(5);
		tltmNewItem.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/contact-new-2.png"));
		
		ToolItem tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!BookUI.opened){
					Display d=Display.getDefault();
					BookUI h=new BookUI(d);
					BookUI.opened=true;
					//center the shell
					Rectangle bounds = d.getBounds();
					Rectangle rect = h.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					h.setLocation (x, y);
					
					h.open();
			}
			}
		});
		tltmNewItem_1.setToolTipText("Book Management");
		tltmNewItem_1.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/bookmarks-organize.png"));
		
		ToolItem tltmNewItem_3 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!ReservationUI.opened){
					Display d=Display.getDefault();
					ReservationUI h=new ReservationUI(d);
					ReservationUI.opened=true;
					//center the shell
					Rectangle bounds = d.getBounds();
					Rectangle rect = h.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					h.setLocation (x, y);
					
					h.open();
			}
			}});
		tltmNewItem_3.setToolTipText("Reservation management");
		tltmNewItem_3.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/services.png"));
		
		ToolItem tltmNewItem_4 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!History.opened){
					Display d=Display.getDefault();
					History h=new History(d);
					History.opened=true;
					//center the shell
					Rectangle bounds = d.getBounds();
					Rectangle rect = h.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					h.setLocation (x, y);
					
					h.open();
					
				}
			}
		});
		tltmNewItem_4.setToolTipText("View history");
		tltmNewItem_4.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/view-calendar-day-2.png"));
		
		ToolItem tltmNewItem_5 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Retard.opened){
					Display d=Display.getDefault();
					Retard h=new Retard(d);
					Retard.opened=true;
					//center the shell
					Rectangle bounds = d.getBounds();
					Rectangle rect = h.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					h.setLocation (x, y);
					
					h.open();
					
				}
			}
		});
		tltmNewItem_5.setToolTipText("View delay exceeded loans");
		tltmNewItem_5.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/view-calendar-upcoming-events.png"));
		
		ToolItem tltmNewItem_6 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Settings.opened){
					Display d=Display.getDefault();
					Settings h=new Settings(d);
					Settings.opened=true;
					//center the shell
					Rectangle bounds = d.getBounds();
					Rectangle rect = h.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					h.setLocation (x, y);
					
					h.open();
					
				}
			}
		});
		tltmNewItem_6.setToolTipText("Settings");
		tltmNewItem_6.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/preferences.png"));
		
		ToolItem tltmNewItem_7 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				About a = new About(display);
				if (!About.opened){
					About.opened = true;
					//center the shell
					Rectangle bounds = display.getBounds();
					Rectangle rect = a.getBounds ();
					int x = bounds.x + (bounds.width - rect.width) / 2;
					int y = bounds.y + (bounds.height - rect.height) / 2;
					a.setLocation (x, y);
					
					a.open();
				}
			}
		});
		tltmNewItem_7.setToolTipText("About");
		tltmNewItem_7.setImage(SWTResourceManager.getImage(LibUI.class, "/icons/help-about-3.png"));
		
		TabFolder tabFolder = new TabFolder(shlBreizhLibraryV, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(0, 50);
		fd_tabFolder.right = new FormAttachment(0, 778);
		fd_tabFolder.left = new FormAttachment(0);
		tabFolder.setLayoutData(fd_tabFolder);
		
		TabItem newLoan = new TabItem(tabFolder, SWT.NONE);
		newLoan.setText("New loan");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		newLoan.setControl(composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(61, 24, 147, 16);
		lblNewLabel.setText("Subscriber number:");
		
		Label lblBookCopyId = new Label(composite, SWT.NONE);
		lblBookCopyId.setBounds(61, 61, 119, 16);
		lblBookCopyId.setText("Book copy ID:");
		
		copyID = new Text(composite, SWT.BORDER);
		copyID.setBounds(265, 56, 138, 26);
		
		Button accept = new Button(composite, SWT.NONE);
		accept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				long lenderID = (long)subsNum.getSelection();
				lib.loan(shlBreizhLibraryV,lenderID, copyID.getText());
			    fillTable();
			}
		});
		accept.setBounds(446, 54, 85, 28);
		accept.setText("Accept");
		
		subsNum = new Spinner(composite, SWT.BORDER);
		subsNum.setMaximum(9999999);
		subsNum.setBounds(268, 24, 135, 26);
		
		TabItem returnCopy = new TabItem(tabFolder, SWT.NONE);
		returnCopy.setText("Return copy");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		returnCopy.setControl(composite_1);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("Book copy ID:");
		label_1.setBounds(58, 28, 119, 16);
		
		returnCopyID = new Text(composite_1, SWT.BORDER);
		returnCopyID.setBounds(183, 23, 138, 26);
		
		Button btnReturn = new Button(composite_1, SWT.NONE);
		btnReturn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.returnBookCopy(shlBreizhLibraryV,returnCopyID.getText());
				fillTable();
			}
		});
		btnReturn.setText("Return");
		btnReturn.setBounds(362, 21, 85, 28);
		
		loans = new Table(shlBreizhLibraryV, SWT.BORDER | SWT.FULL_SELECTION);
		fd_tabFolder.bottom = new FormAttachment(loans, -6);
		FormData fd_loans = new FormData();
		fd_loans.top = new FormAttachment(0, 203);
		fd_loans.bottom = new FormAttachment(0, 498);
		fd_loans.right = new FormAttachment(0, 778);
		fd_loans.left = new FormAttachment(0);
		loans.setLayoutData(fd_loans);
		loans.setHeaderVisible(true);
		loans.setLinesVisible(true);
		
		TableColumn tblclmnSubscriberNumber = new TableColumn(loans, SWT.NONE);
		tblclmnSubscriberNumber.setWidth(127);
		tblclmnSubscriberNumber.setText("Subscriber number");
		
		TableColumn tblclmnSubscriberName = new TableColumn(loans, SWT.NONE);
		tblclmnSubscriberName.setWidth(138);
		tblclmnSubscriberName.setText("Subscriber name");
		
		TableColumn tblclmnCopyId = new TableColumn(loans, SWT.NONE);
		tblclmnCopyId.setWidth(100);
		tblclmnCopyId.setText("Copy ID");
		
		TableColumn tblclmnBookTitle = new TableColumn(loans, SWT.NONE);
		tblclmnBookTitle.setWidth(159);
		tblclmnBookTitle.setText("Book title");
		
		TableColumn tblclmnLoanDate = new TableColumn(loans, SWT.NONE);
		tblclmnLoanDate.setWidth(100);
		tblclmnLoanDate.setText("Loan date");
		
		TableColumn tblclmnReturnDate = new TableColumn(loans, SWT.NONE);
		tblclmnReturnDate.setWidth(100);
		tblclmnReturnDate.setText("Return date");

		
		fillTable();
	}
}
