package cn.edu.nju.cs.navydroid.gui;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.nju.cs.navydroid.gui.model.Property;

public class NewTestSubjectDialog extends TitleAreaDialog {

	private static final Point minSize = new Point(800, 750);

	private Text mTestSubjectName;
	private SourceLine mApkSource;
	private SourceLine mDirSource;
	private Map<Integer, PropertyLine> mProperties;

	public NewTestSubjectDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Test Subject");
		newShell.setMinimumSize(minSize);
	}

	@Override
	protected Point getInitialSize() {
		return minSize;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Select");
	}

	class SourceLine {

		Button radio;
		Text location;
		Button browse;

		boolean hasError = false;
		private String info;
		private String failMessageForNoExist;

		SourceLine(Composite parent, String text, boolean isDefault, String info, String failMessageForNoExist) {
			
			this.info = info;
			this.failMessageForNoExist = failMessageForNoExist;
			
			radio = new Button(parent, SWT.RADIO);
			location = new Text(parent, SWT.NONE);
			browse = new Button(parent, SWT.PUSH);

			radio.setText(text);
			location.setText("");
			browse.setText("Browse...");

			radio.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			location.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			browse.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

			radio.setSelection(isDefault);
			location.setEnabled(isDefault);
			browse.setEnabled(isDefault);
			
			location.addFocusListener(Listeners.gainFocus((e) -> {
				updateView();
			}));
			location.addModifyListener((e) -> {
				updateView();
			});
		}

		void addRadioSelectionListener(SourceLine... others) {
			radio.addSelectionListener(Listeners.selection((e) -> {
				location.setEnabled(true);
				location.setFocus();
				browse.setEnabled(true);
				for (SourceLine line : others) {
					line.location.setEnabled(false);
					line.browse.setEnabled(false);
				}
			}));
		}

		boolean checkValid() {
			if (!radio.getSelection()) {
				return false;
			}
			hasError = false;
			String text = location.getText();
			if (!text.isEmpty()) {
				File file = new File(text);
				if (!file.exists()) {
					setMessage(failMessageForNoExist, IMessageProvider.ERROR);
					hasError = true;
				}
			}
			if (!hasError) {
				setMessage(info, IMessageProvider.INFORMATION);
			}
			return !text.isEmpty() && !hasError;
		}
	}

	class PropertyLine {
		
		static final int FILE = 0;
		static final int DIRECTORY = 1;
		static final int VALUE = 2;
		
		private String propertyName;
		private int type;
		private String defaultDirectoryPath;
		private boolean hasError = false;
		
		Label label;
		Text value;
		Button browse;
		
		public PropertyLine(Composite parent, String propertyName, int type) {
			
			this.type = type;
			this.propertyName = propertyName;
			
			label = new Label(parent, SWT.NONE);
			value = new Text(parent, SWT.NONE);
			browse = new Button(parent, SWT.PUSH);
			
			String labelText = String.format("%c%s:", 
					Character.toUpperCase(propertyName.charAt(0)),
					propertyName.substring(1));
			label.setText(labelText);
			browse.setText("Browse...");
			
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			browse.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			
			value.setEnabled(false);
			browse.setEnabled(false);

			if (type == FILE) {
				browse.addSelectionListener(Listeners.selection((e) -> {
					FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
					fd.setFilterPath(defaultDirectoryPath);
					fd.setText("Select file");
					String selectedFilename = fd.open();
					if (selectedFilename != null) {
						value.setText(selectedFilename);
					}
				}));
			} else if (type == DIRECTORY) {
				browse.addSelectionListener(Listeners.selection((e) -> {
					DirectoryDialog dd = new DirectoryDialog(getShell());
					dd.setFilterPath(defaultDirectoryPath);
					dd.setText("Select directory");
					String selectedPath = dd.open();
					if (selectedPath != null) {
						value.setText(selectedPath);
					}
				}));
			} else if (type == VALUE) {
				browse.setVisible(false);
			}
			
			value.addFocusListener(Listeners.gainFocus((e) -> {
				updateView();
			}));
			
			value.addModifyListener((e) -> {
				updateView();
			});
		}
		
		void setDefaultDirectory(String directoryPath) {
			this.defaultDirectoryPath = directoryPath;
		}
		
		boolean checkValid() {
			hasError = false;
			String text = value.getText();
			if (!text.isEmpty()) {
				if (type == FILE) {
					File file = new File(text);
					if (!file.exists() || !file.isFile()) {
						String msg = String.format("Invalid file path for %s.", propertyName);
						setMessage(msg, IMessageProvider.ERROR);
						hasError = true;
					}
				} else if (type == DIRECTORY) {
					File file = new File(text);
					if (!file.exists() || !file.isDirectory()) {
						String msg = String.format("Invalid directory path for %s.", propertyName);
						setMessage(msg, IMessageProvider.ERROR);
						hasError = true;
					}
				}
			}
			if (!hasError) {
				String msg = String.format("Enter the property %s.", propertyName);
				setMessage(msg, IMessageProvider.INFORMATION);
			}
			return !text.isEmpty() && !hasError;
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 12;
		layout.marginHeight = 0;
		layout.verticalSpacing = 12;
		container.setLayout(layout);

		Composite testSubjectNameLine = new Composite(container, SWT.NONE);
		testSubjectNameLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testSubjectNameLine.setLayout(new GridLayout(2, false));

		Label label = new Label(testSubjectNameLine, SWT.NONE);
		label.setText("Test subject name:");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		mTestSubjectName = new Text(testSubjectNameLine, SWT.NONE);
		mTestSubjectName.setFocus();
		mTestSubjectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Group source = new Group(container, SWT.NONE);
		source.setText("Source");
		source.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		source.setLayout(new GridLayout(3, false));

		mApkSource = new SourceLine(source, "Select an APK file:", true, "Select an APK file.", "APK file does not exist.");
		Text apkLocation = mApkSource.location;
		Button apkBrowse = mApkSource.browse;

		mDirSource = new SourceLine(source, "Select Android project:", false, "Select a project directory.", "Project directory does not exist.");
		Text dirLocation = mDirSource.location;
		Button dirBrowse = mDirSource.browse;

		Group properties = new Group(container, SWT.NONE);
		properties.setText("Properties");
		properties.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		properties.setLayout(new GridLayout(3, false));
		
		mProperties = new LinkedHashMap<Integer, PropertyLine>() {
			private static final long serialVersionUID = 1L;
			{
				put(Property.SOURCEPATH, new PropertyLine(properties, "sourcepath", PropertyLine.DIRECTORY));
				put(Property.CLASSPATH, new PropertyLine(properties, "classpath", PropertyLine.DIRECTORY));
				put(Property.ENTRY_ACTIVITY, new PropertyLine(properties, "entry activity", PropertyLine.VALUE));
				put(Property.R_ID_CLASS, new PropertyLine(properties, "R.id class", PropertyLine.VALUE));
				put(Property.R_LAYOUT_CLASS, new PropertyLine(properties, "R.layout class", PropertyLine.VALUE));
				put(Property.PACKAGE_NAME, new PropertyLine(properties, "package name", PropertyLine.VALUE));
				put(Property.LAYOUT_DIRECTORY, new PropertyLine(properties, "layout directory", PropertyLine.DIRECTORY));
				put(Property.STRINGS_XML_FILE, new PropertyLine(properties, "strings XML file", PropertyLine.FILE));
				put(Property.MANIFEST_XML_FILE, new PropertyLine(properties, "manifest XML file", PropertyLine.FILE));
//				put(Property.OUTPUT_FILE, new PropertyLine(properties, "output file", PropertyLine.FILE));
			}
		};

		mTestSubjectName.addFocusListener(Listeners.gainFocus((e) -> {
			setMessage("Enter a test subject name.", IMessageProvider.INFORMATION);
			updateView();
		}));

		mTestSubjectName.addModifyListener((e) -> {
			setMessage("Enter a test subject name.", IMessageProvider.INFORMATION);
			updateView();
		});

		mApkSource.addRadioSelectionListener(mDirSource);
		mDirSource.addRadioSelectionListener(mApkSource);

		apkBrowse.addSelectionListener(Listeners.selection((e) -> {
			FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
			fd.setText("Select an APK file");
			fd.setFilterExtensions(new String[] { "*.jar", "*.apk" });
			String selectedFilename = fd.open();
			if (selectedFilename != null) {
				apkLocation.setText(selectedFilename);
			}
		}));

		dirBrowse.addSelectionListener(Listeners.selection((e) -> {
			DirectoryDialog dd = new DirectoryDialog(getShell());
			dd.setText("Select a project directory");
			dd.setFilterPath("/home/william/projects/GreenDroid2/testSubjects"); // default directory
			String selectedPath = dd.open();
			if (selectedPath != null) {
				dirLocation.setText(selectedPath);
			}
		}));
		
		apkLocation.addModifyListener((e) -> {
			for (PropertyLine propertyLine : mProperties.values()) {
				propertyLine.value.setEnabled(true);
				propertyLine.value.setText("modified");
				propertyLine.browse.setEnabled(true);
			}
		});
		
		dirLocation.addModifyListener((e) -> {
			String directoryPath = dirLocation.getText();
			if (directoryPath.isEmpty()) {
				for (PropertyLine propertyLine : mProperties.values()) {
					propertyLine.value.setEnabled(false);
					propertyLine.browse.setEnabled(false);
				}
			} else {
				for (PropertyLine propertyLine : mProperties.values()) {
					propertyLine.value.setEnabled(true);
					propertyLine.browse.setEnabled(true);
					propertyLine.setDefaultDirectory(directoryPath);
				}
			}
		});
		
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		Button backButton = createButton(parent, IDialogConstants.BACK_ID, "<Back", false);
//		Button nextButton = createButton(parent, IDialogConstants.NEXT_ID, "Next>", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
		Button finishButton = createButton(parent, IDialogConstants.FINISH_ID, "Finish", true);
//		backButton.setEnabled(false);
//		nextButton.setEnabled(false);
		finishButton.setEnabled(false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		close();
	}

	private void updateView() {
		Button finishButton = getButton(IDialogConstants.FINISH_ID);
		finishButton.setEnabled(checkReadyToFinish());
	}
	
	private boolean checkTestSubjectName() {
		boolean isValid = true;
		String testSubjectName = mTestSubjectName.getText();
		// TODO examine whether the name is conflict with existing ones
		if (testSubjectName.isEmpty()) {
			setMessage("Test subject name is empty.", IMessageProvider.ERROR);
			isValid = false;
		}
		return isValid;
	}
	
	private boolean checkReadyToFinish() {
		boolean isTestSubjectNameValid = checkTestSubjectName();
		if (!isTestSubjectNameValid) {
			return false;
		}
		if (!mApkSource.checkValid() && !mDirSource.checkValid()) {
			return false;
		}
		for (PropertyLine propertyLine : mProperties.values()) {
			if (!propertyLine.checkValid()) {
				return false;
			}
		}
		return true;
	}

}
