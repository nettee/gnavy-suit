package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
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
		Labels.setFontStyle(label, SWT.BOLD);

		new Label(infoArea, SWT.NONE).setText(location);

		Composite upButtonArea = new Composite(this, SWT.NONE);
		upButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		upButtonArea.setLayout(new RowLayout());

		Button settingButton = new Button(upButtonArea, SWT.PUSH);
		settingButton.setText("Setting...");
		settingButton.setEnabled(false);
		
		Button analyseButton = new Button(upButtonArea, SWT.PUSH);
		analyseButton.setText("Analyse");

		ProgressBar progressBar = new ProgressBar(this, SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);

		Text log = new Text(this, SWT.MULTI | SWT.BORDER);
		log.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite downButtonArea = new Composite(this, SWT.NONE);
		downButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		downButtonArea.setLayout(new RowLayout());

		Button viewInBrowserButton = new Button(downButtonArea, SWT.PUSH);
		viewInBrowserButton.setText("View in browser");
		viewInBrowserButton.setEnabled(false);
		
		Button exportButton = new Button(downButtonArea, SWT.PUSH);
		exportButton.setText("Export...");
		exportButton.setEnabled(false);
		
		analyseButton.addSelectionListener(Listeners.selection((e) -> {
			new Thread(new IncreasingOperator(progressBar)).start();
		}));
	}

}

class IncreaseOperator implements Runnable {
	private ProgressBar bar;

	IncreaseOperator(ProgressBar bar) {
		this.bar = bar;
	}

	@Override
	public void run() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (bar.isDisposed()) {
					return;
				}
				if (bar.getSelection() < bar.getMaximum()) {
					bar.setSelection(bar.getSelection() + 1);
				}
			}
		});
	}
}

class IncreasingOperator extends Thread {
	private ProgressBar bar;
	private int minimum;
	private int maximum;

	IncreasingOperator(ProgressBar bar) {
		this.bar = bar;
		this.minimum = bar.getMinimum();
		this.maximum = bar.getMaximum();
	}

	public void run() {
		while (true) {
			Display.getDefault().asyncExec(() -> {
				if (bar.isDisposed())
					return;
				int next = bar.getSelection() == maximum ? minimum : bar.getSelection() + 1;
				bar.setSelection(next);
			});
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
