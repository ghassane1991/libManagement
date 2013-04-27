package ui;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import loansManagement.Loan;
import main.Library;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.DateTime;

import subscribersManagement.AgeCategory;
import subscribersManagement.Entitled;
import subscribersManagement.Subscriber;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author  user
 */
public class SubscriberManagement extends Shell {
	private Table table;
	private Table table_1;
	
	private Label lblMainSubscriber;
	private Label l1; //Age
	private Label l2; // "years old"
	
	private Button btnIsEntitled_1;
	private Button modifySubsbutton; 
	
	private Text addSubsEmail;
	private Text addSubsLast;
	private Text addSubsFirst;
	private Text modifyFirstName;
	private Text modifyLastname;
	private Text modifyEmail;
	private Text ageCategoryName;
	
	private Spinner mainSubsNum;
	private Spinner ageCategoryMin;
	private Spinner ageCategoryMax;
	private Spinner removeSubs;
	private Spinner subsNumModify;
	private Spinner modifyAgeCategoryMin;
	private Spinner modifyAgeCategoryMax;
	
	private DateTime addSubsBirth;
	private DateTime modifyBirthDate;
	
	private Combo ageCategory;
	private Combo removeAge;
	private Combo modifyAgeCat;
	
	/**
	 * @uml.property  name="lib"
	 * @uml.associationEnd  
	 */
	private Library lib=new Library();
	
	private final Shell shell = this;
	
	public static boolean opened = false;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			SubscriberManagement shell = new SubscriberManagement(display);
			
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
				List<Subscriber> subs = lib.SubscriberList();

				table.removeAll();
				table.setRedraw(false);
				 
			    for(Iterator iterator = subs.iterator();iterator.hasNext();){
			    	Subscriber s = (Subscriber) iterator.next();
			    	TableItem item = new TableItem(table, SWT.NONE);
			    	int c = 0;
			    	item.setText(c++, String.valueOf(s.getNumber()));
			    	item.setText(c++, s.getFirstName());
			    	item.setText(c++, s.getLastName());
			    	if (s.getAgeCategory() != null)
			    		item.setText(c++, s.getAgeCategory().getName());
			    	else
			    		item.setText(c++, "");
			    	int age = 0;
			    	age = (int) (new GregorianCalendar().get(Calendar.YEAR) - s.getBornDate().get(Calendar.YEAR));
			    	item.setText(c++, String.valueOf(age));
			    	//Entitled e = (Entitled) s;
			    	if (s instanceof Entitled){
			    		Entitled e = (Entitled) s;
			    		item.setText(c++, String.valueOf(e.getMainSubscriber().getNumber()));
			    	}
			    	else{
			    		item.setText(c++, "");
			    	}
			    	item.setText(c++, "");
				}
				  
			   table.setRedraw(true);
	}
	private void fillCombos(){
		List<AgeCategory> ags = lib.getAgeCategories();
		
		ageCategory.removeAll();
        removeAge.removeAll();
        modifyAgeCat.removeAll();
        
		table_1.removeAll();
		table_1.setRedraw(false);
		
		for(Iterator iterator = ags.iterator();iterator.hasNext();){
	    	AgeCategory ag = (AgeCategory) iterator.next();
	    	ageCategory.add(ag.getName());
	        removeAge.add(ag.getName());
	        modifyAgeCat.add(ag.getName());
	        ageCategory.select(0);
	        removeAge.select(0);
	        modifyAgeCat.select(0);
	        
	        TableItem item = new TableItem(table_1, SWT.NONE);
	        int c = 0;
	        
	        item.setText(c++, ag.getName());
	    	item.setText(c++, String.valueOf(ag.getMin()));
	    	item.setText(c++, String.valueOf(ag.getMax()));	
		}
		
		 table_1.setRedraw(true);
	}
	/**
	 * Create the shell.
	 * @param display
	 */
	public SubscriberManagement(Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				fillTable();
				fillCombos();
				lblMainSubscriber.setVisible(false);
				 mainSubsNum.setVisible(false);
				 l1.setVisible(false);
				 l2.setVisible(false);
				 modifySubsbutton.setEnabled(false);
			}
			@Override
			public void shellClosed(ShellEvent e) {
				opened = false;
			}
		});
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(0, 10, 868, 314);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Add subscriber");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite);
		
		Label lblFirstName = new Label(composite, SWT.NONE);
		lblFirstName.setBounds(32, 27, 63, 16);
		lblFirstName.setText("First name");
		
		Label lblLastName = new Label(composite, SWT.NONE);
		lblLastName.setBounds(32, 65, 63, 16);
		lblLastName.setText("Last name");
		
		Label lblBirthDate = new Label(composite, SWT.NONE);
		lblBirthDate.setBounds(32, 98, 63, 16);
		lblBirthDate.setText("Birth date");
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setBounds(32, 141, 63, 16);
		lblEmail.setText("Email");
		///!!!!!!!!!!
		lblMainSubscriber = new Label(composite, SWT.NONE);
		lblMainSubscriber.setBounds(42, 199, 156, 16);
		lblMainSubscriber.setText("Main subscriber number");
		////!!!!!!!
		btnIsEntitled_1 = new Button(composite, SWT.CHECK);
		btnIsEntitled_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnIsEntitled_1.getSelection()){
				   lblMainSubscriber.setVisible(true);
				   mainSubsNum.setVisible(true);
				}
				else{
					lblMainSubscriber.setVisible(false);
					 mainSubsNum.setVisible(false);
				}
			}
		});
		btnIsEntitled_1.setBounds(32, 174, 106, 22);
		btnIsEntitled_1.setText("Is entitled");
		
		addSubsEmail = new Text(composite, SWT.BORDER);
		addSubsEmail.setBounds(147, 136, 617, 26);
		
		addSubsLast = new Text(composite, SWT.BORDER);
		addSubsLast.setBounds(147, 60, 617, 26);
		
		addSubsFirst = new Text(composite, SWT.BORDER);
		addSubsFirst.setBounds(147, 22, 617, 26);
		
		Button addSubsbutton = new Button(composite, SWT.NONE);
		addSubsbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnIsEntitled_1.getSelection() == false)
					lib.addSubscriber(shell,addSubsFirst.getText(), addSubsLast.getText() , new GregorianCalendar(addSubsBirth.getYear(),addSubsBirth.getMonth(),addSubsBirth.getDay()) , addSubsEmail.getText());
				else
					lib.addEntitled(shell,addSubsFirst.getText(), addSubsLast.getText() , new GregorianCalendar(addSubsBirth.getYear(),addSubsBirth.getMonth(),addSubsBirth.getDay()) , addSubsEmail.getText(),(long)mainSubsNum.getSelection());
				fillTable();
			}
		});
		addSubsbutton.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/list-add-4.png"));
		addSubsbutton.setBounds(701, 179, 63, 41);
		
		 mainSubsNum = new Spinner(composite, SWT.BORDER);
		 mainSubsNum.setMaximum(999999);
		mainSubsNum.setBounds(215, 194, 121, 26);
		
	    addSubsBirth = new DateTime(composite, SWT.BORDER);
		addSubsBirth.setBounds(147, 92, 119, 29);
		
		TabItem tbtmModifySubscriber = new TabItem(tabFolder, SWT.NONE);
		tbtmModifySubscriber.setText("Modify subscriber");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmModifySubscriber.setControl(composite_1);
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setBounds(39, 25, 126, 16);
		lblNewLabel.setText("Subscriber number");
		
		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setBounds(39, 57, 63, 16);
		lblNewLabel_1.setText("First name");
		
		Label lblNewLabel_2 = new Label(composite_1, SWT.NONE);
		lblNewLabel_2.setBounds(39, 95, 63, 16);
		lblNewLabel_2.setText("Last name");
		
		modifyFirstName = new Text(composite_1, SWT.BORDER);
		modifyFirstName.setBounds(171, 52, 576, 26);
		
		modifyLastname = new Text(composite_1, SWT.BORDER);
		modifyLastname.setBounds(171, 90, 576, 26);
		
		Label lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setBounds(39, 131, 63, 16);
		lblNewLabel_3.setText("Email");
		
		modifyEmail = new Text(composite_1, SWT.BORDER);
		modifyEmail.setBounds(171, 126, 576, 26);
		
		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setBounds(39, 201, 126, 16);
		lblNewLabel_4.setText("Age category");
		
		modifyAgeCat = new Combo(composite_1, SWT.NONE);
		modifyAgeCat.setBounds(171, 195, 576, 28);
		
	    modifySubsbutton = new Button(composite_1, SWT.NONE);
		modifySubsbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(modifyAgeCat.getItemCount() == 0){
					lib.modifySubscriber(shell,(long)subsNumModify.getSelection(), modifyFirstName.getText(), modifyLastname.getText(), 
				              new GregorianCalendar(modifyBirthDate.getYear(),modifyBirthDate.getMonth(),modifyBirthDate.getDay()), 
				              modifyEmail.getText());
				}
				else{
					lib.modifySubscriber(shell,(long)subsNumModify.getSelection(), modifyFirstName.getText(), modifyLastname.getText(), 
						              new GregorianCalendar(modifyBirthDate.getYear(),modifyBirthDate.getMonth(),modifyBirthDate.getDay()), 
						              modifyEmail.getText(), modifyAgeCat.getItem(modifyAgeCat.getSelectionIndex()));
				}
//				l1.setVisible(false);
//				l2.setVisible(false);
				fillTable();
			}
		});
		modifySubsbutton.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/list-add-4.png"));
		modifySubsbutton.setBounds(684, 229, 63, 41);
		
		Button choose = new Button(composite_1, SWT.NONE);
		choose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Subscriber s = lib.SearchSubscriber(shell,(long)subsNumModify.getSelection());
				if(s != null){
					System.out.println("S is not null");
					modifyFirstName.setText(s.getFirstName());
					modifyLastname.setText(s.getLastName());
					modifyEmail.setText(s.getEmail());
					modifyBirthDate.setDate(s.getBornDate().get(Calendar.YEAR), s.getBornDate().get(Calendar.MONTH), s.getBornDate().get(Calendar.DAY_OF_MONTH));
					if (s.getAgeCategory() != null){
						for (int i = 0;i<modifyAgeCat.getItemCount();i++){
							if (s.getAgeCategory().getName().equals(modifyAgeCat.getItem(i))){
								modifyAgeCat.select(i);
								break;
							}
						}	
					}
					int age = 0;
			    	age = (int) (new GregorianCalendar().get(Calendar.YEAR) - s.getBornDate().get(Calendar.YEAR));
					l1.setText(String.valueOf(age));
					l1.setVisible(true);
					l2.setVisible(true);
					
					modifySubsbutton.setEnabled(true);
				}
				
			}
		});
		choose.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/dialog-apply.png"));
		choose.setBounds(292, 10, 50, 41);
		
		Label lblNewLabel_5 = new Label(composite_1, SWT.NONE);
		lblNewLabel_5.setBounds(39, 168, 63, 16);
		lblNewLabel_5.setText("Birth date");
		
		subsNumModify = new Spinner(composite_1, SWT.BORDER);
		subsNumModify.setMaximum(999999);
		subsNumModify.setBounds(171, 20, 115, 26);
		
		modifyBirthDate = new DateTime(composite_1, SWT.BORDER);
		modifyBirthDate.setBounds(171, 158, 115, 29);
		
		l1 = new Label(composite_1, SWT.NONE);
		l1.setBounds(292, 168, 63, 16);
		l1.setText("New Label");
		
		l2 = new Label(composite_1, SWT.NONE);
		l2.setBounds(361, 168, 63, 16);
		l2.setText("years old");
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Remove subscriber");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite_2);
		
		Label label = new Label(composite_2, SWT.NONE);
		label.setText("Subscriber number");
		label.setBounds(209, 45, 126, 16);
		
		Button removeSubsbutton = new Button(composite_2, SWT.NONE);
		removeSubsbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.removeSubscriber(shell,(long) removeSubs.getSelection());
				fillTable();
			}
		});
		removeSubsbutton.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/dialog-cancel-.png"));
		removeSubsbutton.setBounds(462, 35, 50, 41);
		
		removeSubs = new Spinner(composite_2, SWT.BORDER);
		removeSubs.setMaximum(999999);
		removeSubs.setBounds(341, 40, 115, 26);
		
		TabItem tbtmAddEntitled = new TabItem(tabFolder, SWT.NONE);
		tbtmAddEntitled.setText("Add age category");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmAddEntitled.setControl(composite_3);
		
		Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setBounds(83, 35, 63, 16);
		
		Label lblName = new Label(composite_3, SWT.NONE);
		lblName.setText("Name");
		lblName.setBounds(21, 43, 35, 16);
		
		ageCategoryName = new Text(composite_3, SWT.BORDER);
		ageCategoryName.setBounds(142, 38, 611, 26);
		
		Label lblMin = new Label(composite_3, SWT.NONE);
		lblMin.setText("Min");
		lblMin.setBounds(21, 147, 63, 16);
		
		Label lblMax = new Label(composite_3, SWT.NONE);
		lblMax.setText("Max");
		lblMax.setBounds(21, 94, 63, 16);
		
		Button addAgeCategoryButton = new Button(composite_3, SWT.NONE);
		addAgeCategoryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.addAgeCategory(shell,ageCategoryName.getText(),ageCategoryMin.getSelection(),ageCategoryMax.getSelection());
				fillCombos();
			}
		});
		addAgeCategoryButton.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/list-add-4.png"));
		addAgeCategoryButton.setBounds(690, 105, 63, 41);
		
		ageCategoryMin = new Spinner(composite_3, SWT.BORDER);
		ageCategoryMin.setMaximum(2000);
		ageCategoryMin.setBounds(142, 137, 158, 26);
		
		ageCategoryMax = new Spinner(composite_3, SWT.BORDER);
		ageCategoryMax.setMaximum(2000);
		ageCategoryMax.setBounds(142, 84, 158, 26);
		
		TabItem tbtmModifyAgeCategory = new TabItem(tabFolder, SWT.NONE);
		tbtmModifyAgeCategory.setText("Modify age category");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		composite_4.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		tbtmModifyAgeCategory.setControl(composite_4);
		
		Label label_2 = new Label(composite_4, SWT.NONE);
		label_2.setText("Min");
		label_2.setBounds(20, 114, 63, 16);
		
		Label label_3 = new Label(composite_4, SWT.NONE);
		label_3.setText("Max");
		label_3.setBounds(20, 157, 63, 16);
		
		Button modifyAgeCategory = new Button(composite_4, SWT.NONE);
		modifyAgeCategory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.modifyAgeCategory(shell,ageCategory.getItem(ageCategory.getSelectionIndex()), modifyAgeCategoryMin.getSelection(), modifyAgeCategoryMax.getSelection());
				fillCombos();
			}
		});
		modifyAgeCategory.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/list-add-4.png"));
		modifyAgeCategory.setBounds(689, 187, 63, 41);
		
		Label label_4 = new Label(composite_4, SWT.NONE);
		label_4.setText("Age category");
		label_4.setBounds(20, 68, 80, 16);
		
		ageCategory = new Combo(composite_4, SWT.NONE);
		ageCategory.setBounds(135, 62, 617, 28);
		
		modifyAgeCategoryMin = new Spinner(composite_4, SWT.BORDER);
		modifyAgeCategoryMin.setMaximum(1000);
		modifyAgeCategoryMin.setBounds(135, 109, 170, 26);
		
		modifyAgeCategoryMax = new Spinner(composite_4, SWT.BORDER);
		modifyAgeCategoryMax.setMaximum(1000);
		modifyAgeCategoryMax.setBounds(135, 152, 170, 26);
		
		TabItem tbtmRemoveAgeCategory = new TabItem(tabFolder, SWT.NONE);
		tbtmRemoveAgeCategory.setText("Remove age category");
		
		Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		composite_5.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		tbtmRemoveAgeCategory.setControl(composite_5);
		
		removeAge = new Combo(composite_5, SWT.NONE);
		removeAge.setBounds(147, 88, 453, 28);
		
		Label label_5 = new Label(composite_5, SWT.NONE);
		label_5.setText("Age category");
		label_5.setBounds(32, 94, 103, 16);
		
		Button removeAgeCategory = new Button(composite_5, SWT.NONE);
		removeAgeCategory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lib.removeAgeCategory(shell,removeAge.getItem(removeAge.getSelectionIndex()));
				fillCombos();
			}
		});
		removeAgeCategory.setImage(SWTResourceManager.getImage(SubscriberManagement.class, "/icons/dialog-cancel-.png"));
		removeAgeCategory.setBounds(627, 77, 50, 41);
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_2.setText("Age Categories list");
		
		table_1 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmNewItem_2.setControl(table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table_1, SWT.NONE);
		tblclmnName.setWidth(313);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnMin = new TableColumn(table_1, SWT.NONE);
		tblclmnMin.setWidth(258);
		tblclmnMin.setText("Min");
		
		TableColumn tblclmnMax = new TableColumn(table_1, SWT.NONE);
		tblclmnMax.setWidth(100);
		tblclmnMax.setText("Max");
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 330, 868, 235);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("Number");
		
		TableColumn tblclmnFirstName = new TableColumn(table, SWT.NONE);
		tblclmnFirstName.setWidth(127);
		tblclmnFirstName.setText("First Name");
		
		TableColumn tblclmnLastName = new TableColumn(table, SWT.NONE);
		tblclmnLastName.setWidth(148);
		tblclmnLastName.setText("Last name");
		
		TableColumn tblclmnAgeCategory = new TableColumn(table, SWT.NONE);
		tblclmnAgeCategory.setWidth(141);
		tblclmnAgeCategory.setText("Age Category");
		
		TableColumn tblclmnAge = new TableColumn(table, SWT.NONE);
		tblclmnAge.setWidth(192);
		tblclmnAge.setText("Age");
		
		TableColumn tblclmnEntitledOf = new TableColumn(table, SWT.NONE);
		tblclmnEntitledOf.setWidth(100);
		tblclmnEntitledOf.setText("Main subscriber");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Subscriber Management");
		setSize(872, 606);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
