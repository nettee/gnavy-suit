package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class Labels {

	public static void setFontStyle(Label label, int style) {
		FontData fd = label.getFont().getFontData()[0];
		Font f = new Font(Display.getDefault(), new FontData(fd.getName(), fd.getHeight(), SWT.BOLD));
		label.setFont(f);
	}

}
