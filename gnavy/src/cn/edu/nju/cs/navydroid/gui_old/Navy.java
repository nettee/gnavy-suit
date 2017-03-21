package cn.edu.nju.cs.navydroid.gui_old;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class Navy {
	
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			MainShell shell = new MainShell(display, SWT.SHELL_TRIM);
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
}