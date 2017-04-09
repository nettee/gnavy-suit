package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class TabArea extends Composite {
	
	public TabArea(CTabFolder tabFolder, String location) {
		
		super(tabFolder, SWT.NONE);
		
		GridLayout tabControlLayout = new GridLayout(1, false);
		tabControlLayout.marginWidth = 0;
		tabControlLayout.marginHeight = 0;
		this.setLayout(tabControlLayout);
		
		Composite infoArea = new Composite(this, SWT.NONE);
		infoArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(2, false);
		layout.marginTop = 10;
		layout.horizontalSpacing = 20;
		infoArea.setLayout(layout);
		
		
		Label label = new Label(infoArea, SWT.NONE);
		label.setText("Location");
		
		// set bold font (verbose...)
		FontData fd = label.getFont().getFontData()[0];
		Font f = new Font(Display.getDefault(), new FontData(fd.getName(), fd.getHeight(), SWT.BOLD));
		label.setFont(f);
		
		new Label(infoArea, SWT.NONE).setText(location);
		
		Composite upButtonArea = new Composite(this, SWT.NONE);
		upButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		upButtonArea.setLayout(new RowLayout());
		
		Button settingButton = new Button(upButtonArea, SWT.PUSH);
		settingButton.setText("Setting...");
		Button analyseButton = new Button(upButtonArea, SWT.PUSH);
		analyseButton.setText("Analyse");
		
		ProgressBar progressBar = new ProgressBar(this, SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		
		new Thread(new IncreasingOperator(progressBar)).start();
		
		analyseButton.addSelectionListener(Listeners.selection((e) -> {
			new Thread(new IncreaseOperator(progressBar)).start();
		}));
		
		Text log = new Text(this, SWT.MULTI | SWT.BORDER);
		log.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite downButtonArea = new Composite(this, SWT.NONE);
		downButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		downButtonArea.setLayout(new RowLayout());
		
		Button viewInBrowserButton = new Button(downButtonArea, SWT.PUSH);
		viewInBrowserButton.setText("View in browser");
		Button exportButton = new Button(downButtonArea, SWT.PUSH);
		exportButton.setText("Export...");
	}
	
}
