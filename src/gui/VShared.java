package fr.umlv.b2mb.gui;

import java.awt.BorderLayout;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author B2MB
 * Graphical component which represents the shared files
 */
public class VShared extends JPanel{
    private JTree tree;
    private DefaultMutableTreeNode root;
    private JScrollPane scrollTree;
    private JPopupMenu popupMenu;
    
    /**
     * @author B2MB
     * @param name of the root
     * Creates Shared component
     */
    public VShared(String name){
	// Set the look & feel
	UIManager.put("Tree.expandedIcon", new ImageIcon("Icons/moins.gif"));
	UIManager.put("Tree.collapsedIcon", new ImageIcon("Icons/plus.gif"));

	// Creation
	root = new DefaultMutableTreeNode(name);
	tree = new JTree(root);
	scrollTree = new JScrollPane(tree);
	
	// Look & feel
	DefaultTreeCellRenderer rendering = new DefaultTreeCellRenderer();
	rendering.setClosedIcon(null); // => Icon a rajouter 
	rendering.setLeafIcon(null); // => Icon a rajouter 
	rendering.setOpenIcon(null); // => Icon a rajouter 
	tree.setCellRenderer(rendering);
	tree.setShowsRootHandles(true);

	createPopupMenu();
	
	// Add
	setLayout(new BorderLayout());
	add(scrollTree, BorderLayout.CENTER);

	// Scanning
	scanDirectory();
    }

    /*******************************************************************/

    /**
     * Permits to scan the shared files' directory and add the files
     * @author B2MB
     */
    public void scanDirectory(){
	cleanRoot();
	scanDirectory(root, new File("Images")); // Path to change -> search it in the config file
	((DefaultTreeModel)tree.getModel()).nodeStructureChanged(root);
    }
    
    /**
     * Permits to scan the a directory and add the files
     * @author B2MB
     */
    private void scanDirectory(DefaultMutableTreeNode fileRoot, File directory){
	File[] all = directory.listFiles();

	for(int i=0;i<all.length;i++){
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(all[i].getName());
	    addFile(fileRoot, node);

	    if(all[i].isDirectory())
		scanDirectory(node, all[i]);
	}
    }
    /*******************************************************************/
    /**
     * Permits to clean all the childs of the root
     * @author B2MB
     */
    private void cleanRoot(){
	while(root.getChildCount() != 0)
	    for(int i=0;i<root.getChildCount();i++)
		root.remove(i);
    }
    
    /*******************************************************************/
    
    /**
     * Permits to add a new file among the shared files
     * @author B2MB
     * @param file to add
     */
    private void addFile(DefaultMutableTreeNode fileRoot, DefaultMutableTreeNode file){
	fileRoot.add(file);
    }

    /**
     * Permits to create a popMenu which will listen to the right click
     * @author B2MB
     */
    private void createPopupMenu(){//RightActionListener ra){
	JMenuItem menuTaskNew = new JMenuItem( "New task" );
	JMenuItem menuDelete = new JMenuItem( "Delete" );
	JMenuItem menuProperty = new JMenuItem( "Property" );
	JMenuItem menuModify = new JMenuItem( "Modify" );
	     
	// Create a popup menu
	popupMenu = new JPopupMenu( "Menu" );
	popupMenu.add( menuTaskNew );
	popupMenu.add( menuDelete );
	popupMenu.add( menuModify );
	popupMenu.add( menuProperty );
	     
	enableEvents( AWTEvent.MOUSE_EVENT_MASK );
	//menuTaskNew.addActionListener(ra);
	//menuProperty.addActionListener(ra);
	//menuDelete.addActionListener(ra);
	//menuModify.addActionListener(ra);
	     
	// And add it
	tree.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e){
		    if (SwingUtilities.isRightMouseButton(e)){
			popupMenu.show(e.getComponent(),
				       e.getX(), e.getY());
		    }
		}
	    });
    }
}




