package basic._1;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Basic SWT Controls example.
 * Shows use of basic input controls like Text and Buttons.
 * Shows basic LayoutManager use.
 *
 * @author barryf
 */
public class Basic1 extends Composite {

    // input fields;  members so they can be referenced from event handlers
    Text nameField;
    Text addrField;
    Text phoneField;

    ArrayList fields = new ArrayList();     // all fields

    // reset all registered fields
    protected void clearFields() {
        for (Iterator i = fields.iterator(); i.hasNext();) {
            ((Text)i.next()).setText("");
        }
    }

    /**
     * Constructor.
     */
    public Basic1(Composite parent) {
        this(parent, SWT.NONE);  // must always supply parent
    }
    /**
     * Constructor.
     */
    public Basic1(Composite parent, int style) {
        super(parent, style);   // must always supply parent and style
        createGui();
    }

    // GUI creation helpers

    protected Text createLabelledText(Composite parent, String label) {
        return createLabelledText(parent, label, 20, null);
    }
    protected Text createLabelledText(Composite parent, String label, int limit, String tip) {
        Label l = new Label(parent, SWT.LEFT);
        l.setText(label);
        Text text  = new Text(parent, SWT.SINGLE);
        if (limit > 0) {
            text.setTextLimit(limit);
        }
        if (tip != null) {
            text.setToolTipText(tip);
        }
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        fields.add(text);
        return text;
    }

    protected Button createButton(Composite parent, String label, SelectionListener l) {
        return createButton(parent, label, l);
    }
    protected Button createButton(Composite parent, String label, String tip, SelectionListener l) {
        Button b = new Button(parent, SWT.NONE);
        b.setText(label);
        if (tip != null) {
            b.setToolTipText(tip);
        }
        if (l != null) {
            b.addSelectionListener(l);
        }
        return b;
    }

    // partial selection listener
    class MySelectionAdapter implements SelectionListener {
        public void widgetSelected(SelectionEvent e) {
            // default is to do nothing
        }
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    };

    protected void createGui() {
        setLayout(new GridLayout(1, true));

        // create the input area

        Group entryGroup = new Group(this, SWT.NONE);
        entryGroup.setText("Input Values");
        // use 2 columns, not same width
        GridLayout entryLayout = new GridLayout(2, false);
        entryGroup.setLayout(entryLayout);
        entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        nameField    = createLabelledText(entryGroup, "Name: ", 40, "Enter the name");
        addrField    = createLabelledText(entryGroup, "Address: ", 40, "Enter the address");
        phoneField   = createLabelledText(entryGroup, "Phone Number: ", 20, "Enter the phone number");

        // create the button area

        Composite buttons = new Composite(this, SWT.NONE);
        buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        // make all buttons the same size
        FillLayout buttonLayout = new FillLayout();
        buttonLayout.marginHeight = 2;
        buttonLayout.marginWidth = 2;
        buttonLayout.spacing = 5;
        buttons.setLayout(buttonLayout);

        // OK button prints input values
        Button okButton = createButton(buttons, "&Ok", "Process input",
                                       new MySelectionAdapter() {
                                           public void widgetSelected(SelectionEvent e) {
                                               System.out.println("Name:         " + nameField.getText());
                                               System.out.println("Address:      " + addrField.getText());
                                               System.out.println("Phone number: " + phoneField.getText());
                                           }
                                       });

        // Clear button resets input values
        Button clearButton = createButton(buttons, "&Clear", "clear inputs",
                                          new MySelectionAdapter() {
                                              public void widgetSelected(SelectionEvent e) {
                                                  clearFields();
                                                  nameField.forceFocus();
                                              }
                                          });
    }

    /**
     * Main driver program.
     */
    public static void main(String[] args) {
        // the display allows access to the host display device
        final Display display = new Display();

        // the shell is the top level window (AKA frame)
        final Shell shell = new Shell(display);
        shell.setText("Example SWT Shell");   // Give me a title

        // all children split space equally
        shell.setLayout( new FillLayout());

        Basic1 basic = new Basic1(shell);

        shell.pack();       // make shell take minimum size
        shell.open();       // open shell for user access

        // process all user input events until the shell is disposed (i.e., closed)
        while ( !shell.isDisposed()) {
            if (!display.readAndDispatch()) {  // process next message
                display.sleep();              // wait for next message
            }
        }
        display.dispose();  // must always clean up
    }
}