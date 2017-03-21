package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.widgets.Display;

public class Navy2 {
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setBlockOnOpen(true);
		window.open();
		Display display = Display.getCurrent();
		display.dispose();
	}

}
