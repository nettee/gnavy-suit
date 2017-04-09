package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WelcomeTabArea extends Composite {

	WelcomeTabArea(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE);
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		layout.verticalSpacing = 10;
		this.setLayout(layout);
		
		Label label1 = new Label(this, SWT.NONE);
		label1.setText("Navydroid v0.1.0");
		Labels.setFontStyle(label1, SWT.BOLD);
		
		Label label2 = new Label(this, SWT.NONE);
		label2.setText("To start, please add a test subject.");
		
	}

}
