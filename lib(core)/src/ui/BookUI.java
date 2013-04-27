package ui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import main.Library;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;

import reservationManagement.Reservation;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

import booksManagement.Book;
import booksManagement.BookCopy;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author  user
 */
public class BookUI extends Shell {
	/**
	 * @uml.property  name="lib"
	 * @uml.associationEnd  
	 */
	private Library lib = new Library();
	private Text addIsbn;
	private Text addTitle;
	private Text addauthors;
	private Text modifyAuthors;
	private Text modifyTitle;
	private Text modifyIsbn;
	private Text removeIsbn;
	private Table table;
	private Text removecopy;
	private DateTime addeditionDate;
	private DateTime modifyEditionDate;
	public static boolean opened= false;
	private Text addcopy;

	private final Shell s = this;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			BookUI shell = new BookUI(display);
			
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
		List<Book> bl = lib.bookList();
		
		table.removeAll();
		table.setRedraw(false);
		 
	    for(Iterator iterator = bl.iterator();iterator.hasNext();){
	    	Book b = (Book) iterator.next();
	    	
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	int c = 0;
	    	item.setText(c++, String.valueOf(b.getIsbn()));
	    	item.setText(c++, b.getTitle());
	    	item.setText(c++, b.getAuthors().toString());
	    	item.setText(c++, b.getEditionDate().getTime().toString());
//	    	if (r.getBookCopy()==null){
//	    		item.setText(c++, String.valueOf(0));
//	    	}
//	    	else{
	    	item.setText(c++, String.valueOf(b.getNumberOfCopies()));
//	    	}
	    
		}
		  
	   table.setRedraw(true);
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public BookUI(Display display) {
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
		tabFolder.setBounds(0, 10, 863, 261);
		
		TabItem tbtmAddBook = new TabItem(tabFolder, SWT.NONE);
		tbtmAddBook.setText("Add book");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmAddBook.setControl(composite);
		
		Label lblIsbn = new Label(composite, SWT.NONE);
		lblIsbn.setText("ISBN");
		lblIsbn.setBounds(55, 15, 63, 16);
		
		addIsbn = new Text(composite, SWT.BORDER);
		addIsbn.setBounds(198, 10, 589, 26);
		
		addTitle = new Text(composite, SWT.BORDER);
		addTitle.setBounds(198, 48, 589, 26);
		
		Label lblTitle = new Label(composite, SWT.NONE);
		lblTitle.setText("Title");
		lblTitle.setBounds(55, 53, 63, 16);
		
		Label lblAuthors = new Label(composite, SWT.NONE);
		lblAuthors.setText("Authors\n(One author per line)");
		lblAuthors.setBounds(55, 91, 137, 41);
		
		addauthors = new Text(composite, SWT.BORDER | SWT.MULTI);
		addauthors.setBounds(198, 86, 589, 59);
		
		Label lblEditionDate = new Label(composite, SWT.NONE);
		lblEditionDate.setText("Edition date");
		lblEditionDate.setBounds(55, 167, 90, 16);
		
		Button button = new Button(composite, SWT.NONE);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<String> authors = new ArrayList<String>();
				String[] lines = addauthors.getText().split("\\n");
				for (String line : lines){
					authors.add(line);
				}
				lib.addBook(s,addTitle.getText(), authors, new GregorianCalendar(addeditionDate.getYear(),addeditionDate.getMonth(),addeditionDate.getDay()), addIsbn.getText());
			    fillTable();
			}
		});
		button.setImage(SWTResourceManager.getImage(BookUI.class, "/icons/list-add-4.png"));
		button.setBounds(724, 167, 63, 41);
		
		addeditionDate = new DateTime(composite, SWT.BORDER);
		addeditionDate.setBounds(198, 154, 119, 29);
		
		TabItem tbtmModifyBook = new TabItem(tabFolder, SWT.NONE);
		tbtmModifyBook.setText("Modify book");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmModifyBook.setControl(composite_1);
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<String> authors = new ArrayList<String>();
				String[] lines = modifyAuthors.getText().split("\\n");
				for (String line : lines){
					authors.add(line);
				}
				
				lib.modifyBook(s,modifyIsbn.getText(), authors,modifyTitle.getText() , new GregorianCalendar(modifyEditionDate.getYear(),modifyEditionDate.getMonth(),modifyEditionDate.getDay()));
			    fillTable();
			}
		});
		button_1.setImage(SWTResourceManager.getImage(BookUI.class, "/icons/list-add-4.png"));
		button_1.setBounds(705, 167, 63, 41);
		
		modifyEditionDate = new DateTime(composite_1, SWT.BORDER);
		modifyEditionDate.setBounds(179, 154, 119, 29);
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setText("Edition date");
		label.setBounds(36, 167, 90, 16);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("Authors\n(One author per line)");
		label_1.setBounds(36, 91, 137, 41);
		
		modifyAuthors = new Text(composite_1, SWT.BORDER | SWT.MULTI);
		modifyAuthors.setBounds(179, 86, 589, 59);
		
		modifyTitle = new Text(composite_1, SWT.BORDER);
		modifyTitle.setBounds(179, 48, 589, 26);
		
		modifyIsbn = new Text(composite_1, SWT.BORDER);
		modifyIsbn.setBounds(179, 10, 589, 26);
		
		Label label_2 = new Label(composite_1, SWT.NONE);
		label_2.setText("Title");
		label_2.setBounds(36, 53, 63, 16);
		
		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setText("ISBN");
		label_3.setBounds(36, 15, 63, 16);
		
		TabItem tbtmRemoveBook = new TabItem(tabFolder, SWT.NONE);
		tbtmRemoveBook.setText("Remove book");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRemoveBook.setControl(composite_2);
		
		removeIsbn = new Text(composite_2, SWT.BORDER);
		removeIsbn.setBounds(198, 50, 589, 26);
		
		Label label_7 = new Label(composite_2, SWT.NONE);
		label_7.setText("ISBN");
		label_7.setBounds(55, 55, 63, 16);
		
		Button button_2 = new Button(composite_2, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.removeBook(s,removeIsbn.getText());
				fillTable();
			}
		});
		button_2.setImage(SWTResourceManager.getImage(BookUI.class, "/icons/dialog-cancel-.png"));
		button_2.setBounds(737, 82, 50, 41);
		
		TabItem tbtmCopies = new TabItem(tabFolder, SWT.NONE);
		tbtmCopies.setText("add copy");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmCopies.setControl(composite_4);
		
		addcopy = new Text(composite_4, SWT.BORDER);
		addcopy.setBounds(115, 26, 607, 26);
		
		Label lblBookIsbn = new Label(composite_4, SWT.NONE);
		lblBookIsbn.setText("Book ISBN");
		lblBookIsbn.setBounds(23, 31, 86, 16);
		
		Button button_4 = new Button(composite_4, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.addBookCopy(s,addcopy.getText());
				fillTable();
			}
		});
		button_4.setImage(SWTResourceManager.getImage(BookUI.class, "/icons/dialog-apply.png"));
		button_4.setBounds(745, 25, 50, 41);
		
		Label label_6 = new Label(composite_4, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(-75, 90, 922, 2);
		
		TabItem tbtmRemoveCopy = new TabItem(tabFolder, SWT.NONE);
		tbtmRemoveCopy.setText("Remove copy");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmRemoveCopy.setControl(composite_3);
		
		Label lblBookcopyId = new Label(composite_3, SWT.NONE);
		lblBookcopyId.setText("BookCopy ID");
		lblBookcopyId.setBounds(20, 28, 86, 16);
		
		removecopy = new Text(composite_3, SWT.BORDER);
		removecopy.setBounds(112, 23, 607, 26);
		
		Button button_3 = new Button(composite_3, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.removeBookCopy(s,removecopy.getText());
				fillTable();
			}
		});
		button_3.setImage(SWTResourceManager.getImage(BookUI.class, "/icons/dialog-apply.png"));
		button_3.setBounds(742, 22, 50, 41);
		
		Label label_5 = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 83, 837, 2);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 277, 863, 272);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnIsbn = new TableColumn(table, SWT.NONE);
		tblclmnIsbn.setWidth(100);
		tblclmnIsbn.setText("ISBN");
		
		TableColumn tblclmnTitle = new TableColumn(table, SWT.NONE);
		tblclmnTitle.setWidth(177);
		tblclmnTitle.setText("Title");
		
		TableColumn tblclmnAuthors = new TableColumn(table, SWT.NONE);
		tblclmnAuthors.setWidth(202);
		tblclmnAuthors.setText("Authors");
		
		TableColumn tblclmnEditionDate = new TableColumn(table, SWT.NONE);
		tblclmnEditionDate.setWidth(251);
		tblclmnEditionDate.setText("Edition date");
		
		TableColumn tblclmnCopyNumber = new TableColumn(table, SWT.NONE);
		tblclmnCopyNumber.setWidth(100);
		tblclmnCopyNumber.setText("Copy number");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Book management");
		setSize(865, 590);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
