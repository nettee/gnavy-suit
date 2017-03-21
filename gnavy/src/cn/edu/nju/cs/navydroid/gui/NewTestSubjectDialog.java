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

	private static final Point minSize = new Point(600, 750);

	private Text mTestSubjectName;
	private SourceLine mApkSource;
	private SourceLine mDirSource;
	private Map<Integer, PropertyLine> mProperties;

	private boolean isTestSubjectNameValid = false;

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

		SourceLine(Composite parent, String text, boolean isDefault) {
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

		void addMessages(String info, String failMessageForNoExist) {
			location.addFocusListener(Listeners.gainFocus((e) -> {
				checkText(info, failMessageForNoExist);
				updateView();
			}));
			location.addModifyListener((e) -> {
				checkText(info, failMessageForNoExist);
				updateView();
			});
		}

		private void checkText(String info, String failMessage) {
			hasError = false;
			String text = location.getText();
			if (text.isEmpty()) {
				setMessage(info, IMessageProvider.INFORMATION);
			} else {
				File file = new File(text);
				if (!file.exists()) {
					setMessage(failMessage, IMessageProvider.ERROR);
					hasError = true;
				}
			}
		}

		boolean isValid() {
			return radio.getSelection() && !location.getText().isEmpty() && !hasError;
		}
	}

	class PropertyLine {
		
		Label label;
		Text value;
		Button browse;
		
		public PropertyLine(Composite parent, String labelText, boolean hasBrowse) {
			label = new Label(parent, SWT.NONE);
			value = new Text(parent, SWT.NONE);
			browse = new Button(parent, SWT.PUSH);
			
			label.setText(labelText);
			browse.setText("Browse...");
			
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			browse.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			
			value.setEnabled(false);
			browse.setEnabled(false);
			
			if (!hasBrowse) {
				browse.setVisible(false);
			}
			
			browse.addSelectionListener(Listeners.selection((e) -> {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				fd.setText("Select file");
				String selectedFilename = fd.open();
				if (selectedFilename != null) {
					value.setText(selectedFilename);
				}
			}));
			
			value.addModifyListener((e) -> {
				updateView();
			});
		}
		
		boolean isValid() {
			return !value.getText().isEmpty();
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

		mApkSource = new SourceLine(source, "Select an APK file:", true);
		Text apkLocation = mApkSource.location;
		Button apkBrowse = mApkSource.browse;

		mDirSource = new SourceLine(source, "Select Android project:", false);
		Text dirLocation = mDirSource.location;
		Button dirBrowse = mDirSource.browse;

		Group properties = new Group(container, SWT.NONE);
		properties.setText("Properties");
		properties.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		properties.setLayout(new GridLayout(3, false));
		
		mProperties = new LinkedHashMap<Integer, PropertyLine>() {
			private static final long serialVersionUID = 1L;
			{
				put(Property.SOURCEPATH, new PropertyLine(properties, "Sourcepath:", true));
				put(Property.CLASSPATH, new PropertyLine(properties, "Classpath", true));
				put(Property.ENTRY_ACTIVITY, new PropertyLine(properties, "Entry activity", false));
				put(Property.R_ID_CLASS, new PropertyLine(properties, "R.id class", false));
				put(Property.R_LAYOUT_CLASS, new PropertyLine(properties, "R.layout class", false));
				put(Property.PACKAGE_NAME, new PropertyLine(properties, "Package name", false));
				put(Property.LAYOUT_DIRECTORY, new PropertyLine(properties, "Layout directory", true));
				put(Property.STRINGS_XML_FILE, new PropertyLine(properties, "Strings XML file", true));
				put(Property.MANIFEST_XML_FILE, new PropertyLine(properties, "Manifest XML file", true));
				put(Property.OUTPUT_FILE, new PropertyLine(properties, "Output file", true));
			}
		};

		mTestSubjectName.addFocusListener(Listeners.gainFocus((e) -> {
			setMessage("Enter a test subject name.", IMessageProvider.INFORMATION);
		}));

		mTestSubjectName.addModifyListener((e) -> {
			isTestSubjectNameValid = true;
			String testSubjectName = mTestSubjectName.getText();
			if (testSubjectName.isEmpty()) {
				setMessage("Test subject name is empty.", IMessageProvider.ERROR);
				isTestSubjectNameValid = false;
			}
			// TODO examine whether the name is conflict with existing ones
			updateView();
		});

		mApkSource.addRadioSelectionListener(mDirSource);
		mDirSource.addRadioSelectionListener(mApkSource);

		mApkSource.addMessages("Select an APK file.", "APK file does not exist.");
		mDirSource.addMessages("Select a project directory.", "Project directory does not exist.");

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
			for (PropertyLine propertyLine : mProperties.values()) {
				propertyLine.value.setEnabled(true);
				propertyLine.value.setText("modified");
				propertyLine.browse.setEnabled(true);
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
		finishButton.setEnabled(isReadyToFinish());
	}

	private boolean isReadyToFinish() {
		if (!isTestSubjectNameValid) {
			return false;
		}
		if (!mApkSource.isValid() && !mDirSource.isValid()) {
			return false;
		}
		for (PropertyLine propertyLine : mProperties.values()) {
			if (!propertyLine.isValid()) {
				return false;
			}
		}
		return true;
	}

}
