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

import cn.edu.nju.cs.navydroid.gui.model.TestSubject;
import cn.edu.nju.cs.navydroid.gui.model.TestSubjectManager;

public class MainWindow extends ApplicationWindow {

	private static final Point minSize = new Point(1000, 700);

	private List mTestSubjectList;
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
		
		Label testSubjectListLabel = new Label(leftArea, SWT.NONE);
		testSubjectListLabel.setText("  Test Subjects");
		Labels.setFontStyle(testSubjectListLabel, SWT.BOLD); 
		testSubjectListLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));

		mTestSubjectList = new List(leftArea, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		mTestSubjectList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite testSubjectListButtonArea = new Composite(leftArea, SWT.NONE);
		testSubjectListButtonArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		GridLayout testSubjectListButtonAreaGridLayout = new GridLayout(2, false);
		testSubjectListButtonAreaGridLayout.marginHeight = 0;
		testSubjectListButtonArea.setLayout(testSubjectListButtonAreaGridLayout);
		
		Button addTestSubjectButton = new Button(testSubjectListButtonArea, SWT.PUSH);
		addTestSubjectButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER));
		addTestSubjectButton.setText("Add...");
		addTestSubjectButton.addSelectionListener(Listeners.selection((e) -> {
			Action newTestSubject = new NewTestSubject();
			newTestSubject.run();
		}));
		
		Button delTestSubjectButton = new Button(testSubjectListButtonArea, SWT.PUSH);
		delTestSubjectButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER));
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
		welcomeTab.setControl(new WelcomeTabArea(mTabFolder));
		
		mTabFolder.setSelection(welcomeTab);

		mTestSubjectList.addMouseListener(Listeners.mouseDoubleClick((e) -> {
			int index = mTestSubjectList.getSelectionIndex();
			String testSubjectName = mTestSubjectList.getItem(index);
			
			TestSubject ts = tsm.getTestSubject(testSubjectName);
			if (ts == null) {
				System.err.printf("Error: test subject %s not found\n", testSubjectName);
				return;
			}
			
			CTabItem tab = new CTabItem(mTabFolder, SWT.NONE);
			tab.setText(testSubjectName);
			tab.setControl(new TabArea(mTabFolder, ts));
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
		mTestSubjectList.removeAll();
		for (TestSubject ts : tsm.getTestSubjects()) {
			mTestSubjectList.add(ts.getName());
		}
	}

}
