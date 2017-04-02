package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import cn.edu.nju.cs.navydroid.gui.model.TestSubject;
import cn.edu.nju.cs.navydroid.gui.model.TestSubjectManager;

public class MainWindow extends ApplicationWindow {

	private static final Point minSize = new Point(1000, 700);

	private List mAppList;
	private CTabFolder mTabFolder;

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

		leftArea.setLayout(new FillLayout());

		mAppList = new List(leftArea, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

		Composite rightArea = new Composite(content, SWT.NONE);
		rightArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		rightArea.setLayout(new FillLayout());

		mTabFolder = new CTabFolder(rightArea, SWT.TOP | SWT.CLOSE);

		CTabItem welcomeTab = new CTabItem(mTabFolder, SWT.NONE);
		welcomeTab.setText("Welcome");
		Composite welcomeArea = new Composite(mTabFolder, SWT.NONE);
		welcomeTab.setControl(welcomeArea);

		welcomeArea.setLayout(new RowLayout());
		Button button2 = new Button(welcomeArea, SWT.PUSH);
		button2.setText("welcome!");

		mTabFolder.setSelection(welcomeTab);

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
				TestSubjectManager tsm = TestSubjectManager.getInstance();
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
		TestSubjectManager tsm = TestSubjectManager.getInstance();
		for (TestSubject ts : tsm.getTestSubjects()) {
			mAppList.add(ts.getName());
		}
		for (int i = 0; i < 50; i++) {
			mAppList.add("Item " + (i + 1));
		}
	}

}
