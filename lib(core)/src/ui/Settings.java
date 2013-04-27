package ui;

import main.Constraints;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class Settings extends Shell {
	public static boolean opened= false;
	private Spinner ml;
	private Spinner mr;
	private Spinner ld;
	private Spinner rd;

	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Settings shell = new Settings(display);
			
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
	public Settings(Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				opened=false;
			}
			@Override
			public void shellActivated(ShellEvent e) {
				//Constraints.update();	
				ml.setSelection(Constraints.maxLOANS);
				rd.setSelection(Constraints.reservationDELAY);
				ld.setSelection(Constraints.loanDELAY);
				mr.setSelection(Constraints.maxRESERVATIONS);
			}
		});
		setLayout(null);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(10, 15, 164, 16);
		lblNewLabel.setText("Max loans");
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setBounds(10, 60, 164, 16);
		lblNewLabel_1.setText("Max reservations");
		
		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setBounds(10, 111, 164, 16);
		lblNewLabel_2.setText("Loans delay (by days)");
		
		Label lblNewLabel_3 = new Label(this, SWT.NONE);
		lblNewLabel_3.setBounds(10, 156, 164, 16);
		lblNewLabel_3.setText("Reservation delay (by days)");
		
		ml = new Spinner(this, SWT.BORDER);
		ml.setBounds(195, 10, 50, 26);
		ml.setMaximum(2000);
		
		mr = new Spinner(this, SWT.BORDER);
		mr.setBounds(195, 55, 50, 26);
		mr.setMaximum(2000);
		
		ld = new Spinner(this, SWT.BORDER);
		ld.setBounds(195, 106, 50, 26);
		ld.setMaximum(365);
		
		rd = new Spinner(this, SWT.BORDER);
		rd.setBounds(195, 151, 50, 26);
		rd.setMaximum(365);
		
		Button button = new Button(this, SWT.NONE);
		button.setBounds(10, 192, 235, 41);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Constraints.setLoanDELAY(ld.getSelection());
				Constraints.setReservationDELAY(rd.getSelection());
				Constraints.setMaxLOANS(ml.getSelection());
				Constraints.setMaxRESERVATIONS(mr.getSelection());
				Constraints.update();
			}
		});
		button.setImage(SWTResourceManager.getImage(Settings.class, "/icons/dialog-apply.png"));
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("settings");
		setSize(264, 275);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
