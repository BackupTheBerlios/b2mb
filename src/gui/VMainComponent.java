import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * This class represents the graphical view's main component of the application 
 */
public class VMainComponent extends JFrame{
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private int nbTabs = 0;
    
    /**
     * @author B2MB
     * Creates a VMainComponent
     */
    VMainComponent(){
	super("B2M2 Peer to Peer");
	
	// Menu
	initMenuBar();
	
	// Toolbar
	initToolBar();
	
	// Create JTabbPane with a default tab placement of JTabbedPane.TOP
	tabbedPane = new JTabbedPane();
	// And add all the tabs
	initTabs();

	// Default Settings
	//setSize(Toolkit.getDefaultToolkit().getScreenSize());
	setSize(new Dimension(500, 500));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * @author B2MB
     * Init the application's menu bar
     */
    private void initMenuBar(){
	menuBar = new JMenuBar();

	JMenu file = new JMenu ("File");	
	file.add(new JMenuItem("Open"));

	file.add(new JSeparator());
	file.add(new JMenuItem("Save"));

	file.add(new JSeparator());
	file.add(new JMenuItem("Disconect"));

	JMenuItem exit = new JMenuItem("Exit"); 
	exit.addMouseListener(new InternalMouseListner());
	file.add(new JSeparator());
	file.add(exit);

	menuBar.add(file);
	
	this.setJMenuBar(menuBar);
    }
    
    /**
     * @author B2MB
     * Inits the application's toolbar
     */
    private void initToolBar(){
	toolBar = new JToolBar();
	
	JButton copy = new JButton ();
	copy.setIcon(new ImageIcon("Icons/Copy24.gif"));
	copy.setToolTipText("Copier");
	toolBar.add(copy);

	JButton cut = new JButton ();
	cut.setIcon(new ImageIcon("Icons/Cut24.gif"));
	toolBar.add(cut);

	JButton paste = new JButton ();
	paste.setIcon(new ImageIcon("Icons/Paste24.gif"));
	toolBar.add(paste);

	getContentPane().add(toolBar,BorderLayout.NORTH);
    }
    
    /**
     * @author B2MB
     * Inits the application's tabs
     */
    private void initTabs(){
	// addTab(String title, Icon icon, Component component, String tip);
	
	tabbedPane.addTab("Network", null, new VNetwork(), "Your network's connexion");
	
	VTable servent = new VTable(new String[]{"IP", "Port", "Toto"});
	servent.addRow(new Object[] {new Integer(123), "IO", "5 mn"});
	tabbedPane.addTab("Servents", null, servent, "Connected Servents");
	
	tabbedPane.addTab("Transfer", null, new VTransfer(), "Transfered files");

	tabbedPane.addTab("Search", null, new VSearch(), "Search files");
	
	getContentPane().add(tabbedPane);
    }

    /******************************************/
    /**
     * Internal class which represents a specific MouseAdapter
     */
    private class InternalMouseListner extends MouseAdapter{
	/**
	 * Creates a new InternalMouseListner 
	 */
	InternalMouseListner(){
	    super();
	}

	/**
	 * @author B2MB
	 *   Invoked when the mouse has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e){
	    System.exit(0);
	}
    }
}



