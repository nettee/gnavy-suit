/*
 * Created on May 22, 2005
 *
 */
package basic._3;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


/**
 * Tree Example
 *
 * @author barryf
 */
public class Tree31App extends BasicApplication {
    protected Tree tree;
    protected int mode = 0;

    /** Data Model Object */
    public class Node {
        protected java.util.List children = new ArrayList();
        /**
         * @return Returns the children.
         */
        public java.util.List getChildren() {
            return children;
        }
        /**
         * @param children The children to set.
         */
        public void setChildren(java.util.List children) {
            this.children = children;
        }

        protected String id;
        /**
         * @return Returns the id.
         */
        public String getId() {
            return id;
        }
        /**
         * @param id The id to set.
         */
        public void setId(String id) {
            this.id = id;
        }

        protected String name;
        /**
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }
        /**
         * @param name The name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        protected String address;
        /**
         * @return Returns the address.
         */
        public String getAddress() {
            return address;
        }
        /**
         * @param address The address to set.
         */
        public void setAddress(String address) {
            this.address = address;
        }

        protected String phoneNumber;
        /**
         * @return Returns the phoneNumber.
         */
        public String getPhoneNumber() {
            return phoneNumber;
        }
        /**
         * @param phoneNumber The phoneNumber to set.
         */
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        protected String age;
        /**
         * @return Returns the age.
         */
        public String getAge() {
            return age;
        }
        /**
         * @param age The age to set.
         */
        public void setAge(String age) {
            this.age = age;
        }

        public void addChild(Node node) {
            children.add(node);
        }

        public Node() {
            //this("<unknown>", "<unknown>", "<unknown>", "<unknown>", "<unknown>");
            this("", "", "", "", "");
        }
        public Node(String id, String name, String age, String address, String phoneNumber) {
            setId(id);
            setName(name);
            setAge(age);
            setAddress(address);
            setPhoneNumber(phoneNumber);
        }

        public String toString() {
            //return getClass().getName() + "[" + getName() + "," + getChildren() + "]";
            return "Node" + "[" + getId() + ":" + getName() + "," + getAge() + "," + getAddress() + "," + getPhoneNumber() + "," + getChildren() + "]";
        }
    }

    /**
     * Constructor.
     */
    public Tree31App(Shell shell, int style) {
        super(shell, style);   // must always supply parent and style
    }

    /** Parse any arguments */
    protected void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                String s = arg.substring(1);
                if (s.equalsIgnoreCase("checked")) {
                    mode |= SWT.CHECK;
                }
                else if (s.equalsIgnoreCase("plain")) {
                    mode |= SWT.NONE;
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

    /** Allow subclasses to complete the GUI */
    protected void completeGui(String[] args) {
        parseArgs(args);
        createTree(this);
    }

    /** Create the Tree */
    protected Tree createTree(Composite parent) {
        tree = new Tree(parent, mode | SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        TreeColumn idCol    = new TreeColumn(tree, SWT.LEFT);  idCol.setText("Id");                idCol.setWidth(125);
        TreeColumn nameCol  = new TreeColumn(tree, SWT.LEFT);  nameCol.setText("Name");            nameCol.setWidth(150);
        TreeColumn ageCol   = new TreeColumn(tree, SWT.LEFT);  ageCol.setText("Age");              ageCol.setWidth(50);
        TreeColumn addrCol  = new TreeColumn(tree, SWT.LEFT);  addrCol.setText("Address");         addrCol.setWidth(150);
        TreeColumn phoneCol = new TreeColumn(tree, SWT.LEFT);  phoneCol.setText("Phone Number");   phoneCol.setWidth(100);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.addSelectionListener(new SelectionListener() {
                                      public void widgetSelected(SelectionEvent e) {
                                          TreeItem[] sel = tree.getSelection();
                                          if (sel != null && sel.length > 0) {
                                              System.out.println("Selection:");
                                              for (int i = 0; i < sel.length; i++) {
                                                  System.out.println("   " + sel[i].getText());
                                              }
                                          }
                                      }
                                      public void widgetDefaultSelected(SelectionEvent e) {
                                          widgetSelected(e);
                                      }
                                  });
        return tree;
    }

    /** Set the Tree data model */
    public void setTreeContents(Node root) {
        tree.removeAll();
        TreeItem ti = new TreeItem(tree, SWT.NONE);
        setTreeItemContents(ti, root);

    }

    /** Set a Tree level */
    protected void setTreeItemContents(TreeItem ti, Node node) {
        ti.setText(0, node.getId());
        ti.setText(1, node.getName());
        ti.setText(2, node.getAge());
        ti.setText(3, node.getAddress());
        ti.setText(4, node.getPhoneNumber());
        java.util.List children = node.getChildren();
        if (children != null && children.size() > 0) {
            for (Iterator i = children.iterator(); i.hasNext();) {
                Node n = (Node)i.next();
                TreeItem tix = new TreeItem(ti, SWT.NONE);
                setTreeItemContents(tix, n);
            }
        }
    }

    protected static final String[][] nodes = {
            {"123-45-6789", "John Smith", "50", "123 Any Street", "512-123-4567"},
            {"256-78-8765", "Mary Smith", "49", "123 Any Street", "512-123-4567"},
            {"256-78-8765", "John Smith, Jr.", "10", "123 Any Street", "512-123-4567"},
            {"256-78-1238", "Patty Smith", "13", "123 Any Street", "512-123-4567"},
            {"456-76-1258", "Mark Jones", "22", "567 My Way", "512-770-5553"},
    };

    /** Allow subclasses to initialize the GUI */
    protected void initGui() {

        Node father   = new Node(nodes[0][0], nodes[0][1], nodes[0][2], nodes[0][3], nodes[0][4]);
        Node mother   = new Node(nodes[1][0], nodes[1][1], nodes[1][2], nodes[1][3], nodes[1][4]);
        Node son      = new Node(nodes[2][0], nodes[2][1], nodes[2][2], nodes[2][3], nodes[2][4]);
        Node daughter = new Node(nodes[3][0], nodes[3][1], nodes[3][2], nodes[3][3], nodes[3][4]);

        Node friend   = new Node(nodes[4][0], nodes[4][1], nodes[4][2], nodes[4][3], nodes[4][4]);


        Node root = new Node();
        root.setId("<none>");
        root.setName("<root>");
        root.addChild(father);
        father.addChild(son);
        father.addChild(daughter);
        root.addChild(mother);
        mother.addChild(son);
        mother.addChild(daughter);
        root.addChild(friend);

        setTreeContents(root);
        displayTree(shell);
    }

    /** Main driver */
    public static void main(String[] args) {
        run(Tree31App.class.getName(), "Tree1App Example", SWT.NONE, 600, 300, args);
    }
}