package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.nju.cs.navydroid.gui.model.TestSubject;
import cn.edu.nju.cs.navydroid.gui.model.TestSubjectManager;

public class MainWindow extends ApplicationWindow {

	private static final Point minSize = new Point(1000, 700);

	private List mAppList;
	private CTabFolder mTabFolder;
	
	private TestSubjectManager tsm = TestSubjectManager.getInstance();

	public MainWindow() {
		super(null); // no parent shell
		addMenuBar();
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menubar = new MenuManager();

		MenuManager fileMenu = new MenuManager("&File");
		menubar.add(fileMenu);

		fileMenu.add(new NewTestSubject());
		fileMenu.add(new Separator());
		fileMenu.add(new Exit());

		MenuManager helpMenu = new MenuManager("&Help");
		menubar.add(helpMenu);

		helpMenu.add(new About());

		return menubar;

	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("NavyDroid");
		shell.setMinimumSize(minSize);
	}

	@Override
	protected Point getInitialSize() {
		return minSize;
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));

		Composite leftArea = new Composite(content, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 300;
		gridData.minimumWidth = 300;
		leftArea.setLayoutData(gridData);

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		leftArea.setLayout(layout);
		
		Label appListLabel = new Label(leftArea, SWT.NONE);
		appListLabel.setText("Test Subjects");
		appListLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));

		mAppList = new List(leftArea, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		mAppList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite appListButtonArea = new Composite(leftArea, SWT.NONE);
		appListButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginLeft = 0;
		rowLayout.marginRight = 0;
		rowLayout.pack = false; // all widgets have the same size
		appListButtonArea.setLayout(rowLayout);
		
		Button addTestSubjectButton = new Button(appListButtonArea, SWT.PUSH);
		addTestSubjectButton.setText("Add...");
		addTestSubjectButton.addSelectionListener(Listeners.selection((e) -> {
			Action newTestSubject = new NewTestSubject();
			newTestSubject.run();
		}));
		
		Button delTestSubjectButton = new Button(appListButtonArea, SWT.PUSH);
		delTestSubjectButton.setText("Delete");

		Composite rightArea = new Composite(content, SWT.NONE);
		rightArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		rightArea.setLayout(new FillLayout());

		mTabFolder = new CTabFolder(rightArea, SWT.TOP | SWT.CLOSE);
		Color backgroundColor = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		mTabFolder.setSelectionBackground(new Color[] { backgroundColor, backgroundColor},
				new int[] {100}, true);

		CTabItem welcomeTab = new CTabItem(mTabFolder, SWT.NONE);
		welcomeTab.setText("Welcome");
		Composite welcomeArea = new Composite(mTabFolder, SWT.NONE);
		welcomeTab.setControl(welcomeArea);

		welcomeArea.setLayout(new RowLayout());
		Button button2 = new Button(welcomeArea, SWT.PUSH);
		button2.setText("welcome!");
		
		CTabItem tab1 = new CTabItem(mTabFolder, SWT.NONE);
		tab1.setText("AndTweet");
		Composite tab1Area = new Composite(mTabFolder, SWT.NONE);
		tab1.setControl(tab1Area);
		
		GridLayout tabControlLayout = new GridLayout(1, false);
		tabControlLayout.marginWidth = 0;
		tabControlLayout.marginHeight = 0;
		tab1Area.setLayout(tabControlLayout);
		
		Composite infoArea = new Composite(tab1Area, SWT.NONE);
		infoArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		infoArea.setLayout(new GridLayout(2, false));
		
		new Label(infoArea, SWT.NONE).setText("Location");
		new Label(infoArea, SWT.NONE).setText("/fakepath/andtweet/");
		new Label(infoArea, SWT.NONE).setText("Other info");
		new Label(infoArea, SWT.NONE).setText("fake info");
		
		Composite upButtonArea = new Composite(tab1Area, SWT.NONE);
		upButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		upButtonArea.setLayout(new RowLayout());
		
		Button settingButton = new Button(upButtonArea, SWT.PUSH);
		settingButton.setText("Setting...");
		Button analyseButton = new Button(upButtonArea, SWT.PUSH);
		analyseButton.setText("Analyse");
		
		ProgressBar progressBar = new ProgressBar(tab1Area, SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		
		new Thread(new IncreasingOperator(progressBar)).start();
		
		analyseButton.addSelectionListener(Listeners.selection((e) -> {
			new Thread(new IncreaseOperator(progressBar)).start();
		}));
		
		Text log = new Text(tab1Area, SWT.MULTI | SWT.BORDER);
		log.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite downButtonArea = new Composite(tab1Area, SWT.NONE);
		downButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		downButtonArea.setLayout(new RowLayout());
		
		Button viewInBrowserButton = new Button(downButtonArea, SWT.PUSH);
		viewInBrowserButton.setText("View in browser");
		Button exportButton = new Button(downButtonArea, SWT.PUSH);
		exportButton.setText("Export...");

		mTabFolder.setSelection(tab1);
		mTabFolder.pack();

		mAppList.addMouseListener(Listeners.mouseDoubleClick((e) -> {
			int index = mAppList.getSelectionIndex();
			String item = mAppList.getItem(index);
			System.out.printf("double click list item [%d] %s\n", index, item);
			CTabItem tab = new CTabItem(mTabFolder, SWT.NONE);
			tab.setText(item);
			mTabFolder.setSelection(tab);
		}));

		return content;
	}

	private class NewTestSubject extends Action {

		public NewTestSubject() {
			super("&New Test Subject...", AS_PUSH_BUTTON);
		}

		@Override
		public void run() {
			NewTestSubjectDialog newTestSubjectDialog = new NewTestSubjectDialog(getShell());
			newTestSubjectDialog.setBlockOnOpen(true);
			int returnCode = newTestSubjectDialog.open();
			if (returnCode == IDialogConstants.FINISH_ID) {
				TestSubject testSubject = newTestSubjectDialog.getTestSubject();
				tsm.addTestSubject(testSubject);
				updateView();
			}
		}
	}

	private class Exit extends Action {

		public Exit() {
			super("E&xit", AS_PUSH_BUTTON);
		}

		@Override
		public void run() {
			getShell().close();
		}
	}

	private class About extends Action {

		public About() {
			super("&About", AS_PUSH_BUTTON);
			setEnabled(false);
		}

		@Override
		public void run() {
			// TODO
		}
	}
	
	private void updateView() {
		mAppList.removeAll();
		for (TestSubject ts : tsm.getTestSubjects()) {
			mAppList.add(ts.getName());
		}
//		for (int i = 0; i < 50; i++) {
//			mAppList.add("Item " + (i + 1));
//		}
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
