package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import cn.edu.nju.cs.navydroid.gui.model.LogListener;
import cn.edu.nju.cs.navydroid.gui.model.ReportListener;
import cn.edu.nju.cs.navydroid.gui.model.TestSubject;

public class TabArea extends Composite {
	
	private TestSubject testSubject;

	public TabArea(CTabFolder tabFolder, TestSubject testSubject) {

		super(tabFolder, SWT.NONE);
		
		this.testSubject = testSubject;

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

		new Label(infoArea, SWT.NONE).setText(testSubject.getSourcePath());

		Composite upButtonArea = new Composite(this, SWT.NONE);
		upButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		upButtonArea.setLayout(new GridLayout(4, false));

//		Button settingButton = new Button(upButtonArea, SWT.PUSH);
//		settingButton.setText("Setting...");
//		settingButton.setEnabled(false);
		
		Button analyzeButton = new Button(upButtonArea, SWT.PUSH);
		analyzeButton.setText("Analyse");
		analyzeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER));
		
		Label paddingLabel = new Label(upButtonArea, SWT.NONE);
		paddingLabel.setVisible(false);
		paddingLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		
		Label seqLabel = new Label(upButtonArea, SWT.NONE);
		seqLabel.setText("event sequence");
		seqLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));
		
		Text seq = new Text(upButtonArea, SWT.BORDER);
		seq.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));

		ProgressBar progressBar = new ProgressBar(this, SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setSelection(100);
		
		Composite outputArea = new Composite(this, SWT.NONE);
		outputArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout logAreaGridLayout = new GridLayout(2, true);
		logAreaGridLayout.marginWidth = 0;
		logAreaGridLayout.marginHeight = 0;
		outputArea.setLayout(logAreaGridLayout);
		
		Label logLabel = new Label(outputArea, SWT.NONE);
		logLabel.setText("Log");
		logLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER));
		
		Label reportLabel = new Label(outputArea, SWT.NONE);
		reportLabel.setText("Report");
		reportLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER));
		
		Text logOutput = new Text(outputArea, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		logOutput.setEditable(true);
		logOutput.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Text reportOutput = new Text(outputArea, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		reportOutput.setEditable(true);
		reportOutput.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite downButtonArea = new Composite(this, SWT.NONE);
		downButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		GridLayout downButtonAreaGridLayout = new GridLayout(4, false);
		downButtonAreaGridLayout.marginHeight = 0;
		downButtonArea.setLayout(downButtonAreaGridLayout);

		Button exportTraceButton = new Button(downButtonArea, SWT.PUSH);
		exportTraceButton.setText("Export trace...");
		exportTraceButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER));
		
		Button paddingButton = new Button(downButtonArea, SWT.NONE);
		paddingButton.setVisible(false);
		paddingButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		
		Button exportReportTextButton = new Button(downButtonArea, SWT.PUSH);
		exportReportTextButton.setText("Export report (text)...");
		exportReportTextButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));		
		
		Button exportReportXmlButton = new Button(downButtonArea, SWT.PUSH);
		exportReportXmlButton.setText("Export report (XML)...");
		exportReportXmlButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));		
		
		analyzeButton.addSelectionListener(Listeners.selection((e) -> {
			logOutput.setText("");
			LogListener logListener = (line) -> {
				Display.getDefault().asyncExec(() -> {
					if (logOutput.isDisposed()) {
						return;
					}
					logOutput.append(line);
					logOutput.append("\n");
				});
			};
			ReportListener reportListener = (line) -> {
				Display.getDefault().asyncExec(() -> {
					if (reportOutput.isDisposed()) {
						return;
					}
					reportOutput.append(line);
					reportOutput.append("\n");
				});
			};
			testSubject.analyze(logListener, reportListener);
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
		Display.getDefault().asyncExec(new Runnable() {
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
				int next = bar.getSelection() >= maximum ? minimum : bar.getSelection() + 1;
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
