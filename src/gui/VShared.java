package gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
import parser.Result;
import parser.ResultSet;

/**
 * Graphical component which represents the shared files
 * @author B2MB
 */
public class VShared extends JPanel{
    private JTree tree;
    private DefaultMutableTreeNode root;
    private JScrollPane scrollTree;
    private JPopupMenu popupMenu;
    private HashMap index;
    
    /**
     * Creates Shared component
     * @param name of the root
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
     */
    public void scanDirectory(){
	cleanRoot();
	updateIndex();
	scanDirectory(root, new File("Images")); // Path to change -> search it in the config file
	((DefaultTreeModel)tree.getModel()).nodeStructureChanged(root);
    }

    /**
     * Updates the indexes
     */
    private void updateIndex(){
	index = new HashMap();
	File[] all = (new File("Images")).listFiles(); // Path to change
	for(int i=0;i<all.length;i++)
	    index.put(new Integer(i+1), all[i].getName());
    }

    /**
     * Search the key of a value in the index table
     * @param element
     * @return the key of the element if exists, -1 otherwise.
     */
    private int getKey(String element){
	for(int i=1;i<=index.size();i++){
	    String s = (String)index.get(new Integer(i));
	    if( s.equals(element))
		return i;
	}
	return -1;
    }
    
    /**
     * Permits to scan the a directory and add the files
     * @param fileRoot
     * @param directory the directory 
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
    
    /**
     * Returns the size of the directory, ie the sum of all the files contained in it.
     */
    private int getFileSize(DefaultMutableTreeNode root)
    {
	Enumeration e = root.depthFirstEnumeration();
	int fileSize=0;
	String name;
	File file;
	while(e.hasMoreElements())
	    {
		name = (String)e.nextElement();
		file = new File(VMainComponent.setup.getPath()+name);
		if(!file.isDirectory())
		    fileSize += file.length();
	    }
	return fileSize;
    }
    
    /*******************************************************************/
    /**
     * Permits to get the list of files from a specified file name
     * @param file name
     * @return a list of file names
     */
    public ArrayList getAllFiles(String s){
	ArrayList names = new ArrayList();
	
	Enumeration e = root.preorderEnumeration();
	DefaultMutableTreeNode searchedNode = null;
	DefaultMutableTreeNode node = null;
	while(e.hasMoreElements()){
	    node = (DefaultMutableTreeNode)e.nextElement();
	    // Get the searched node
	    if( ((String)node.getUserObject()).compareTo(s) == 0 )
		searchedNode = node;
	    
	    if( (searchedNode != null) && (node != searchedNode) && node.isNodeAncestor(searchedNode) && node.isLeaf() )
		names.add(node.getUserObject());
	}
	return names;
    }

    /*******************************************************************/
    /**
     * Permits to get similar name to the given name
     * @param file name
     * @return a list of file names
     * @return null if nothing corresponds
     */
    public ResultSet searchFiles(String s){
	ResultSet names = new ResultSet();
	
	Enumeration e = root.children();
	
	DefaultMutableTreeNode node = null;
	while(e.hasMoreElements()){
	    node = (DefaultMutableTreeNode)e.nextElement();
	    String nodeName = (String)node.getUserObject();
	    if( ((String)node.getUserObject()).lastIndexOf(s) != -1 ){
		int fileIndex = getKey(nodeName);
		int fileSize  = getFileSize(node);
		names.addResult(new Result(fileIndex, fileSize, nodeName));
	    }
	}
	return names;
    }

    /*******************************************************************/
    /**
     * Permits to clean all the childs of the root
     */
    private void cleanRoot(){
	while(root.getChildCount() != 0)
	    for(int i=0;i<root.getChildCount();i++)
		root.remove(i);
    }
    
    /*******************************************************************/
    
    /**
     * Permits to add a new file among the shared files
     * @param the root 
     * @param file to add
     */
    private void addFile(DefaultMutableTreeNode fileRoot, DefaultMutableTreeNode file){
	fileRoot.add(file);
    }

    /**
     * Permits to create a popMenu which will listen to the right click
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




