package ui;

import java.util.Iterator;
import java.util.List;

import main.Library;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author  user
 */
public class History extends Shell {
	private Table table;
	/**
	 * @uml.property  name="lib"
	 * @uml.associationEnd  
	 */
	private Library lib = new Library();
	public static boolean opened= false;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			History shell = new History(display);
			
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

	/**
	 * Create the shell.
	 * @param display
	 */
	public History(Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				opened=false;
			}
			@Override
			public void shellActivated(ShellEvent e) {
				List<loansManagement.History> bl = lib.getHistory();
				
				table.removeAll();
				table.setRedraw(false);
			
			    for(Iterator iterator = bl.iterator();iterator.hasNext();){
			    	loansManagement.History h = (loansManagement.History) iterator.next();
			    	
			    	TableItem item = new TableItem(table, SWT.NONE);
			    	int c = 0;
//			    	item.setText(c++, String.valueOf(h.getLender().getNumber()));
//			    	item.setText(c++, h.getLender().getFirstName());
//			    	item.setText(c++, h.getLender().getLastName());
//			    	item.setText(c++, h.getCopy().getBook().getTitle());
//			    	item.setText(c++, h.getCopy().getId());
//			    	item.setText(c++, h.getLoanDate().getTime().toString());
//			    	item.setText(c++, h.getMustReturnDate().getTime().toString());
//			    	item.setText(c++, h.getReturnDate().getTime().toString());
			    	
			    	item.setText(c++, String.valueOf(h.getLender().getNumber()));
			    	item.setText(c++, h.getLender().getFirstName());
			    	item.setText(c++, h.getLender().getLastName());
			    	item.setText(c++, h.getCopy().getBook().getTitle());
			    	item.setText(c++, h.getCopy().getId());
			    	item.setText(c++, h.getLoanDate().getTime().toString());
			    	item.setText(c++, h.getMustReturnDate().getTime().toString());
			    	if (h.getReturnDate() != null)
			    		item.setText(c++, h.getReturnDate().getTime().toString());
			    	else
			    		item.setText(c++, "");
				}
				  
			   table.setRedraw(true);
			}
		});
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 1018, 534);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnSubscriberNum = new TableColumn(table, SWT.NONE);
		tblclmnSubscriberNum.setWidth(117);
		tblclmnSubscriberNum.setText("Subscriber num");
		
		TableColumn tblclmnFirstName = new TableColumn(table, SWT.NONE);
		tblclmnFirstName.setWidth(100);
		tblclmnFirstName.setText("First Name");
		
		TableColumn tblclmnLastName = new TableColumn(table, SWT.NONE);
		tblclmnLastName.setWidth(90);
		tblclmnLastName.setText("Last name");
		
		TableColumn tblclmnBookTitle = new TableColumn(table, SWT.NONE);
		tblclmnBookTitle.setWidth(189);
		tblclmnBookTitle.setText("Book title");
		
		TableColumn tblclmnCopyId = new TableColumn(table, SWT.NONE);
		tblclmnCopyId.setWidth(166);
		tblclmnCopyId.setText("Copy ID");
		
		TableColumn tblclmnLoanDate = new TableColumn(table, SWT.NONE);
		tblclmnLoanDate.setWidth(107);
		tblclmnLoanDate.setText("Loan date");
		
		TableColumn tblclmnIsDelayExceeded = new TableColumn(table, SWT.NONE);
		tblclmnIsDelayExceeded.setWidth(129);
		tblclmnIsDelayExceeded.setText("Must return date");
		
		TableColumn tblclmnReturnDate = new TableColumn(table, SWT.NONE);
		tblclmnReturnDate.setWidth(92);
		tblclmnReturnDate.setText("return date");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("History");
		setSize(1040, 585);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
