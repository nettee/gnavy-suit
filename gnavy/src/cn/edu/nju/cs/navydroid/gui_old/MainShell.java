package cn.edu.nju.cs.navydroid.gui_old;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import cn.edu.nju.cs.navydroid.gui.NewTestSubjectDialog;
import cn.edu.nju.cs.navydroid.gui.Listeners;

public class MainShell extends Shell {

	private static final String title = "NavyDroid 0.1 beta";
	private static final Point minSize = new Point(1000, 700);

	private MenuItem mExitMenuItem;
	private MenuItem mAddTestSubjectMenuItem;

	private List mAppList;
	private CTabFolder mTabFolder;

	public MainShell(Display display, int style) {
		super(display, style);

		setText(title);
		setMinimumSize(minSize);
		setSize(minSize);

		createViews();
		createControllers();
	}

	protected void createViews() {

		Menu menuBar = new Menu(this, SWT.BAR | SWT.LEFT_TO_RIGHT);
		setMenuBar(menuBar);

		MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuItem.setText("&File");

		Menu fileMenu = new Menu(this, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);

		mAddTestSubjectMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		mAddTestSubjectMenuItem.setText("&Add Test Subject...");

		new MenuItem(fileMenu, SWT.SEPARATOR);

		mExitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		mExitMenuItem.setText("E&xit");

		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);

		Composite leftArea = new Composite(this, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 300;
		gridData.minimumWidth = 300;
		leftArea.setLayoutData(gridData);

		leftArea.setLayout(new FillLayout());

		mAppList = new List(leftArea, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i = 0; i < 50; i++) {
			mAppList.add("Item " + (i + 1));
		}

		Composite rightArea = new Composite(this, SWT.NONE);
		rightArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		rightArea.setLayout(new FillLayout());

		mTabFolder = new CTabFolder(rightArea, SWT.TOP | SWT.CLOSE);
		mTabFolder.setSimple(false);

		CTabItem welcomeTab = new CTabItem(mTabFolder, SWT.NONE);
		welcomeTab.setText("Welcome");
		Composite welcomeArea = new Composite(mTabFolder, SWT.NONE);
		welcomeTab.setControl(welcomeArea);

		welcomeArea.setLayout(new RowLayout());
		Button button2 = new Button(welcomeArea, SWT.PUSH);
		button2.setText("welcome!");

		mTabFolder.setSelection(welcomeTab);

	}

	protected void createControllers() {

		mExitMenuItem.addSelectionListener(Listeners.selection((e) -> {
			close();
		}));

		mAddTestSubjectMenuItem.addSelectionListener(Listeners.selection((e) -> {
		}));
		
		mAppList.addMouseListener(Listeners.mouseDoubleClick((e) -> {
			int index = mAppList.getSelectionIndex();
			String item = mAppList.getItem(index);
			System.out.printf("double click list item [%d] %s\n", index, item);
			CTabItem tab = new CTabItem(mTabFolder, SWT.NONE);
			tab.setText(item);
			mTabFolder.setSelection(tab);
		}));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
