package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;

public class Book extends Shell {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_8;
	private Table table;
	private Text text_6;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Book shell = new Book(display);
			
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
	public Book(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(0, 10, 863, 261);
		
		TabItem tbtmAddBook = new TabItem(tabFolder, SWT.NONE);
		tbtmAddBook.setText("Add book");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmAddBook.setControl(composite);
		
		Label lblIsbn = new Label(composite, SWT.NONE);
		lblIsbn.setText("ISBN");
		lblIsbn.setBounds(55, 15, 63, 16);
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(198, 10, 589, 26);
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setBounds(198, 48, 589, 26);
		
		Label lblTitle = new Label(composite, SWT.NONE);
		lblTitle.setText("Title");
		lblTitle.setBounds(55, 53, 63, 16);
		
		Label lblAuthors = new Label(composite, SWT.NONE);
		lblAuthors.setText("Authors\n(One author per line)");
		lblAuthors.setBounds(55, 91, 137, 41);
		
		text_2 = new Text(composite, SWT.BORDER | SWT.MULTI);
		text_2.setBounds(198, 86, 589, 59);
		
		Label lblEditionDate = new Label(composite, SWT.NONE);
		lblEditionDate.setText("Edition date");
		lblEditionDate.setBounds(55, 167, 90, 16);
		
		Button button = new Button(composite, SWT.NONE);
		button.setImage(SWTResourceManager.getImage(Book.class, "/icons/list-add-4.png"));
		button.setBounds(724, 167, 63, 41);
		
		DateTime dateTime = new DateTime(composite, SWT.BORDER);
		dateTime.setBounds(198, 154, 119, 29);
		
		TabItem tbtmModifyBook = new TabItem(tabFolder, SWT.NONE);
		tbtmModifyBook.setText("Modify book");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmModifyBook.setControl(composite_1);
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.setImage(SWTResourceManager.getImage(Book.class, "/icons/list-add-4.png"));
		button_1.setBounds(705, 167, 63, 41);
		
		DateTime dateTime_1 = new DateTime(composite_1, SWT.BORDER);
		dateTime_1.setBounds(179, 154, 119, 29);
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setText("Edition date");
		label.setBounds(36, 167, 90, 16);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("Authors\n(One author per line)");
		label_1.setBounds(36, 91, 137, 41);
		
		text_3 = new Text(composite_1, SWT.BORDER | SWT.MULTI);
		text_3.setBounds(179, 86, 589, 59);
		
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setBounds(179, 48, 589, 26);
		
		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(179, 10, 589, 26);
		
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
		
		text_8 = new Text(composite_2, SWT.BORDER);
		text_8.setBounds(198, 50, 589, 26);
		
		Label label_7 = new Label(composite_2, SWT.NONE);
		label_7.setText("ISBN");
		label_7.setBounds(55, 55, 63, 16);
		
		Button button_2 = new Button(composite_2, SWT.NONE);
		button_2.setImage(SWTResourceManager.getImage(Book.class, "/icons/dialog-cancel-.png"));
		button_2.setBounds(737, 82, 50, 41);
		
		TabItem tbtmCopies = new TabItem(tabFolder, SWT.NONE);
		tbtmCopies.setText("Copies");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmCopies.setControl(composite_3);
		
		Label label_4 = new Label(composite_3, SWT.NONE);
		label_4.setText("ISBN");
		label_4.setBounds(64, 27, 63, 16);
		
		text_6 = new Text(composite_3, SWT.BORDER);
		text_6.setBounds(113, 22, 607, 26);
		
		Button button_3 = new Button(composite_3, SWT.NONE);
		button_3.setImage(SWTResourceManager.getImage(Book.class, "/icons/dialog-apply.png"));
		button_3.setBounds(742, 22, 50, 41);
		
		Label label_5 = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 83, 837, 2);
		
		Label lblCopiesNumber = new Label(composite_3, SWT.NONE);
		lblCopiesNumber.setBounds(361, 91, 113, 16);
		lblCopiesNumber.setText("Number of copies");
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(384, 134, 63, 16);
		lblNewLabel.setText("New Label");
		
		Button button_4 = new Button(composite_3, SWT.NONE);
		button_4.setImage(SWTResourceManager.getImage(Book.class, "/icons/list-add-4.png"));
		button_4.setBounds(486, 121, 63, 41);
		
		Button button_5 = new Button(composite_3, SWT.NONE);
		button_5.setImage(SWTResourceManager.getImage(Book.class, "/icons/dialog-fewer.png"));
		button_5.setBounds(255, 121, 63, 41);
		
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
