/*
 * Created on May 22, 2005
 *
 */

package basic._3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Folder Example
 *
 * @author barryf
 */
public class TabFolder1App extends BasicApplication {
    protected static final String dummyContent = 
"Just plain text!\n" +
"Now is the time for <b>all</b> good men to come to the aid of their country\n" +
"<red><i>To <b>be</b></i> or <i>not to <b>be</b></i>?</red> <blue><u>That</u> is the <so>question</so></blue>\n" +
"That's all folks!\n";

    protected Composite folder;

    /**
     * Constructor.
     */
    public TabFolder1App(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
    }

    protected boolean useCFolder = false;
    protected boolean allowClose = true;
    protected boolean flat = false;
    protected boolean onTop = true;

    /** Parse any arguments */
    protected void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                String s = arg.substring(1);
                if      (s.equalsIgnoreCase("cfolder")) {
                    useCFolder = true;
                }
                else if (s.equalsIgnoreCase("folder")) {
                    useCFolder = false;
                }
                else if (s.equalsIgnoreCase("close")) {
                    allowClose = true;
                }
                else if (s.equalsIgnoreCase("flat")) {
                    flat = true;
                }
                else if (s.equalsIgnoreCase("top")) {
                    onTop = true;
                }
                else if (s.equalsIgnoreCase("bottom")) {
                    onTop = false;
                }
                else {
                    throw new IllegalArgumentException("Invalid switch: " + s);
                }
            }
            else {
                throw new IllegalArgumentException("No argument allowed");
            }
        }
    }

    protected class BarUpdater extends Thread {
        protected int delay;
        protected Display display;

        public BarUpdater(Display display) {
            this.display = display;
        }

        public void run() {
            try {
                while (true) {
                    try {
                        if (!display.isDisposed()) {
                            display.syncExec(new Runnable() {
                                    public void run() {
                                        if (!modeButton.isDisposed() && !scale.isDisposed()) {
                                            delay = modeButton.getSelection() ? scale.getSelection() : -1;
                                        }
                                    }
                            });
                            if (delay >= 0) {
                                //System.out.println("Delaying: " + delay);
                                Thread.sleep(delay);
                                if (!display.isDisposed()) {
                                    display.syncExec(new Runnable() {
                                            public void run() {
                                                if (!bar.isDisposed()) {
                                                    int v = bar.getSelection() + 1;
                                                    if (v > bar.getMaximum()) {
                                                        v = bar.getMinimum();
                                                    }
                                                    bar.setSelection(v);
                                                    if (!slider.isDisposed()) {
                                                        slider.setSelection(v);
                                                    }
                                                    if (!valueLabel.isDisposed()) {
                                                        valueLabel.setText(Integer.toString(v));
                                                    }
                                                }
                                            }
                                    });
                                }
                            }
                        }
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ie) {
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Allow subclasses to complete the GUI */
    protected void completeGui(String[] args) {
        parseArgs(args);
        createFolder(this, 4);
    }


    /** Create the Folder */
    protected Composite createFolder(Composite parent, int tabCount) {
        int modes = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL;
        if (allowClose) {
            modes |= SWT.CLOSE;
        }
        if (flat) {
            modes |= SWT.FLAT;
        }
        if (onTop) {
            modes |= SWT.TOP;
        }
        else {
            modes |= SWT.BOTTOM;
        }
        if (useCFolder) {
            folder = createCTabFolder(parent, modes);
        }
        else {
            folder = createTabFolder(parent, modes);
        }
        for (int i = 0; i < tabCount; i++) {
            Composite body = createComposite(folder, null);
            if (useCFolder) {
                CTabItem ti = createCTabItem((CTabFolder)folder, null, null, body);
                initTab(body, ti, i);
            }
            else {
                TabItem ti = createTabItem((TabFolder)folder, null, null, body);
                initTab(body, ti, i);
            }
        }
        return folder;
    }

    /** Allow subclasses to initialize the GUI */
    protected void initGui() {
        displayTree(shell);
    }

    protected Button modeButton;
    protected Scale scale;
    protected Slider slider;
    protected Spinner spinner;
    protected ProgressBar bar;
    protected Label valueLabel;
    protected Label[] labels;
    protected int currentLabel;
    protected StackLayout stackLayout;
    protected Canvas canvas;
    protected GC gc;
    protected List gcObjects = new ArrayList();
    protected StyledText styledText;

    abstract protected class PaintItem {
        public Color color;
        public void paint(GC gc) {
            gc.setForeground(color);
            gc.setBackground(color);
        }
    }
    abstract protected class BaseRectItem extends PaintItem {
        public boolean fill;
        public Rectangle extent;
    }
    protected class ElipseItem extends BaseRectItem {
        public void paint(GC gc) {
            super.paint(gc);
            if (fill) {
                gc.fillOval(extent.x, extent.y, extent.width, extent.height);
            }
            else {
                gc.drawOval(extent.x, extent.y, extent.width, extent.height);
            }
        }
    }
    protected class RectangleItem extends BaseRectItem {
        public void paint(GC gc) {
            super.paint(gc);
            if (fill) {
                gc.fillRectangle(extent.x, extent.y, extent.width, extent.height);
            }
            else {
                gc.drawRectangle(extent.x, extent.y, extent.width, extent.height);
            }
        }
    }

    protected static int[] colorIds = {
        SWT.COLOR_WHITE, SWT.COLOR_BLUE, SWT.COLOR_CYAN, SWT.COLOR_GRAY, 
        SWT.COLOR_GREEN, SWT.COLOR_MAGENTA, SWT.COLOR_RED, SWT.COLOR_YELLOW, SWT.COLOR_BLACK
    };


    protected static final String[] popupItems = {
        "Select All", "Cut", "Copy", "Paste"
    };

    protected void processPopup() {
        // fake a popup menu with PopupList
        PopupList popup = createPopupList(shell, 50, popupItems);
        popup.select(popupItems[0]);
        Point p = styledText.getLocation();
        p = shell.getDisplay().map(styledText, null, p.x, p.y);
        String choice = popup.open(new Rectangle(p.x + 100, p.y - 100, 100, 200));
        if (choice != null) {
            if      (popupItems[0].equals(choice)) {
                styledText.selectAll();
            }
            else if (popupItems[1].equals(choice)) {
                styledText.cut();
            }
            else if (popupItems[2].equals(choice)) {
                styledText.copy();
            }
            else if (popupItems[3].equals(choice)) {
                styledText.paste();
            }
        }
    }

    public void doSlider() {
        int v = slider.getSelection();
        bar.setSelection(v);
        valueLabel.setText(Integer.toString(v));
    }

    public void doSpinner() {
        int v = spinner.getSelection();
        bar.setSelection(v);
        valueLabel.setText(Integer.toString(v));
    }

    public void doClear() {
        gcObjects.clear();
        canvas.redraw();
    }

    public void doDraw() {
        gcObjects.clear();
        // create a bunch of objects
        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                RectangleItem ri = new RectangleItem();
                ri.extent = new Rectangle(nextInt(500), nextInt(250), nextInt(500), nextInt(250));
                ri.color = clearButton.getDisplay().getSystemColor(colorIds[nextInt(colorIds.length)]);
                ri.fill = i % 3 == 0;
                gcObjects.add(ri);
            }
            else {
                ElipseItem ei = new ElipseItem();
                ei.extent = new Rectangle(nextInt(500), nextInt(250), nextInt(500), nextInt(250));
                ei.color = clearButton.getDisplay().getSystemColor(colorIds[nextInt(colorIds.length)]);
                ei.fill = i % 5 == 0;
                gcObjects.add(ei);
            }
        }
        canvas.redraw();
    }

    protected Button clearButton, drawButton, switch1Button, switch2Button;
    protected Composite clabels;

    public void doNext() {
        ++currentLabel;
        if (currentLabel >= labels.length) {
            currentLabel = 0;
        }
        stackLayout.topControl = labels[currentLabel];
        clabels.layout();
    }
    public void doPrevious() {
        --currentLabel;
        if (currentLabel < 0) {
            currentLabel = labels.length - 1;
        }
        stackLayout.topControl = labels[currentLabel];
        clabels.layout();
    }

    protected void initTab(Composite body,  Item ti, int index) {
        String tabName = "<none>";
        switch (index) {
        case 0:
            tabName = "Progress Bar";  // & slider and scale
            body.setLayout(new FillLayout());
            SashForm sash = createVSashForm(body);

            int min = 0, max = 100;

            Group controls = createGroup(sash, SWT.BORDER, "Control the ProgressBar", new FormLayout());

            modeButton = createCheckBox(controls, "Automatic Update");

            Label sliderLabel = createLabel(controls, "Bar Value: ");

            slider = createHSlider(controls, min, min, max, 1, Math.max(1, (max - min) / 10), 1, "doSlider");

            Label spinnerLabel = createLabel(controls, "Bar Value: ");

            spinner = createSpinner(controls, SWT.NONE, min, min, max, 1, Math.max(1, (max - min) / 10), "doSpinner");

            Label scaleLabel = createLabel(controls, "Update Rate: ");

            scale = createHScale(controls, 0, 0, 500, 10, 100);

            configureLayout(modeButton, new FormAttachment(0, 5), new FormAttachment(0, 5), null, null);
            configureLayout(sliderLabel, new FormAttachment(0, 5), new FormAttachment(modeButton, 5), null, null);
            configureLayout(slider, new FormAttachment(sliderLabel, 5), new FormAttachment(modeButton, 5), new FormAttachment(100, -5), null);
            configureLayout(spinnerLabel, new FormAttachment(0, 5), new FormAttachment(slider, 5), null, null);
            configureLayout(spinner, new FormAttachment(spinnerLabel, 5), new FormAttachment(slider, 5), new FormAttachment(100, -5), null);
            configureLayout(scaleLabel, new FormAttachment(0, 5), new FormAttachment(spinner, 5), null, null);
            configureLayout(scale, new FormAttachment(scaleLabel, 5), new FormAttachment(spinner, 5), new FormAttachment(100, -5), null);

            Composite data = createComposite(sash, new FormLayout());

            valueLabel = createLabel(data, null, null, SWT.CENTER);

            bar = createHProgressBar(data, min, min, max);

            configureLayout(valueLabel, new FormAttachment(0, 5), new FormAttachment(0, 5), new FormAttachment(100, -5), null);
            configureLayout(bar, new FormAttachment(0, 5), new FormAttachment(valueLabel, 5), new FormAttachment(100, -5), null);

            sash.setWeights(new int[] {75, 25});

            BarUpdater barUpdater = new BarUpdater(shell.getDisplay());
            barUpdater.setDaemon(true);
            barUpdater.start();
            break;

        case 1:
            tabName = "Styled Text";
            body.setLayout(new FillLayout());
            styledText = createStyledText(body, SWT.MULTI | SWT.WRAP | SWT.FULL_SELECTION);
            styledText.addMouseListener(new MouseAdapter() {
                    public void mouseDown(MouseEvent e) {
                        if (e.button == 3) {
                            processPopup();
                        }
                    }
            });
            TextContent tc = new TextContent(body.getDisplay());
            tc.setContent(dummyContent);
            styledText.setText(tc.toPlainText());
            styledText.setStyleRanges(tc.getStyleRanges());
            break;

        case 2:
            tabName = "Canvas";
            body.setLayout(new FormLayout());
            clearButton = createPushButton(body, "Clear", null, "doClear");
            configureLayout(clearButton, new FormAttachment(0, 5), new FormAttachment(0, 5), null, null);
            drawButton = createPushButton(body, "Draw", null, "doDraw");
            configureLayout(drawButton, new FormAttachment(clearButton, 5), new FormAttachment(0, 5), null, null);

            canvas = createCanvas(body, new PaintListener() {
                    public void paintControl(PaintEvent e) {
                        GC gc = e.gc;
                        gc.setBackground(canvas.getDisplay().getSystemColor(colorIds[0]));
                        Point cext = canvas.getSize();
                        gc.fillRectangle(0, 0, cext.x, cext.y);
                        for (Iterator i = gcObjects.iterator(); i.hasNext();) {
                            PaintItem pi = (PaintItem)i.next();
                            pi.paint(gc);
                        }
                    }
            });
            configureLayout(canvas, new FormAttachment(0, 5), new FormAttachment(drawButton, 5), new FormAttachment(100, -5), new FormAttachment(100, -5));
            break;

        case 3:
            tabName = "StackLayout";
            body.setLayout(new FormLayout());
            Button switchButton1 = createPushButton(body, "<<", null, "doPrevious");
            configureLayout(switchButton1, new FormAttachment(0, 5), new FormAttachment(0, 5), null, null);
            Button switchButton2 = createPushButton(body, ">>", null, "doNext");
            configureLayout(switchButton2, new FormAttachment(switchButton1, 5), new FormAttachment(0, 5), null, null);

            stackLayout = new StackLayout();
            clabels = createComposite(body, SWT.BORDER, stackLayout);
            configureLayout(clabels, new FormAttachment(0, 5), new FormAttachment(switchButton1, 5), new FormAttachment(100, -5), new FormAttachment(100, -5));
            labels = new Label[5];
            for (int i = 0; i < labels.length; i++) {
                Label xlabel = new Label(clabels, SWT.CENTER);
                xlabel.setText("Stack " + i);
                labels[i] = xlabel;
            }
            stackLayout.topControl = labels[0];
            break;

        default:
            throw new IllegalArgumentException("index out of range");
        }
        ti.setText(tabName);
    }

    protected static final Random rand = new Random();
    protected static int nextInt(int range) {
        return Math.abs(rand.nextInt()) % range;
    }


    /** Main driver */
    public static void main(String[] args) {
        run(TabFolder1App.class.getName(), "TabFolder1App Example", SWT.NONE, 600, 300, args);
    }
}