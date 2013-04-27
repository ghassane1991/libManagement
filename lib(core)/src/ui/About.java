package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;

public class About extends Shell {
	//If the shell is already opened
	public static boolean opened = false;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			About shell = new About(display);
			
			
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
	public About(Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
			     opened = false;
			}
		});
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage(About.class, "/icons/br.png"));
		lblNewLabel.setBounds(350, 135, 156, 39);
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setBounds(165, 10, 156, 16);
		lblNewLabel_1.setText("Breizh Library V1");
		
		Label lblDevelopedByLatfi = new Label(this, SWT.NONE);
		lblDevelopedByLatfi.setText("Developed by LATFI Ghassane, LGUENSAT Redouane.\n\nBreizhLibrary is distributed under GPL V3 License.\n\nShould you require any information, please do not hesitate to contact us.");
		lblDevelopedByLatfi.setBounds(10, 44, 438, 85);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("About us");
		setSize(508, 204);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
