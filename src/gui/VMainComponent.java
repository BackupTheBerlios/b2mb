package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.Toolkit;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar.Separator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import configuration.Setup;
import controller.GuiController;

/**
 * This class represents the graphical view's main component of the application 
 * @author B2MB
 */
public class VMainComponent extends JFrame{
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private int nbTabs = 0;

    // Tabs component
    private VNetwork network = new VNetwork();
    private VShared sharedFile = new VShared("My folder");
    private VTransfer transfer = new VTransfer();
    private VSearch search = new VSearch();
    private VDisplay display = new VDisplay();

    // Controller
    private GuiController controller;
    
    // TEST
    public static Setup setup;
    
    /**
     * Creates a VMainComponent
     */
    public VMainComponent(){
	super("B2MB - Peer to Peer");

	// TEST
	try{
	    setup = new Setup("properties");
	}catch(IOException e){
	    e.printStackTrace();
	}
	
	// Create JTabbPane with a default tab placement of JTabbedPane.TOP
	tabbedPane = new JTabbedPane();
	// And add all the tabs
	initTabs();
	// Init the controller
	
	
	// Toolbar
	initToolBar();
	
	// Default Settings
	setSize(Toolkit.getDefaultToolkit().getScreenSize());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Init the application's menu bar
     */
    private void initMenuBar(){
	menuBar = new JMenuBar();

	JMenu file = new JMenu ("File");	
	file.add(new JMenuItem("Open"));

	file.add(new JSeparator());
	file.add(new JMenuItem("Save"));

	file.add(new JSeparator());
	file.add(new JMenuItem("Disconnect"));

	JMenuItem exit = new JMenuItem("Exit"); 
	exit.addMouseListener(new InternalMouseListener());
	file.add(new JSeparator());
	file.add(exit);

	menuBar.add(file);
	
	this.setJMenuBar(menuBar);
    }
    
    /**
     * Inits the application's toolbar
     */
    private void initToolBar(){
	Dimension dim =  new Dimension(50, 10);
	toolBar = new JToolBar();

	JButton connect = new JButton ("Connect");
	connect.setToolTipText("Get connected");
	connect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    controller.connect();
		}
	    });
	toolBar.add(connect);

	toolBar.add(new JToolBar.Separator(dim));
	
	JButton disconnect = new JButton ("Disconnect");
	disconnect.setToolTipText("Get disconnected");
	disconnect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    controller.disconnect();
		}
	    });
	toolBar.add(disconnect);

	toolBar.add(new JToolBar.Separator(dim));

	JButton scan = new JButton ("Scan");
	scan.setToolTipText("Scan the shared files' folder");
	scan.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    controller.scanDirectory();
		}
	    });
	toolBar.add(scan);

	toolBar.add(new JToolBar.Separator(dim));

	JButton clear = new JButton ("Clear");
	clear.setToolTipText("Clear the log board");
	clear.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    controller.removeAllFromLog();
		}
	    });
	toolBar.add(clear);

	toolBar.add(new JToolBar.Separator(dim));
	
	JButton exit = new JButton ("Quit");
	//exit.setIcon(new ImageIcon("Icons/Paste24.gif"));
	exit.setToolTipText("Exit the application");
	exit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    controller.disconnect();
		    System.out.println("VMainComponent: on se déconnecte");
		    System.exit(0);
		}
	    });
	toolBar.add(exit);
	
	getContentPane().add(toolBar,BorderLayout.NORTH);
    }
    
    /**
     * Inits the application's tabs
     */
    private void initTabs(){
	// addTab(String title, Icon icon, Component component, String tip);
	
	tabbedPane.addTab("Network", null, network, "Your network's connexion");
	
	VTable servent = new VTable(new String[]{"IP", "Port", "Toto"});
	servent.addRow(new Object[] {new Integer(123), "IO", "5 mn"});
	tabbedPane.addTab("Servents", null, servent, "Connected Servents");

	transfer.addListener(tabbedPane, 5);
	tabbedPane.addTab("Transfer", null, transfer, "Transfered files");

	tabbedPane.addTab("Search", null, search, "Search files");

	tabbedPane.addTab("Shared Files", null, sharedFile, "Shared files");
	
	tabbedPane.addTab("Preview", null, display, "Display the current downloading file");
	
	getContentPane().add(tabbedPane);
    }
    
    /**
     * @return the tabbed pane.
     */
    public JTabbedPane getPane()
    { return this.tabbedPane; }
    
    
    /**
     * Sets the controller.
     * @param controller the needed controller.
     */
    public void setController(GuiController controller)
    { this.controller = controller; this.search.setController(controller); }
    
    
    /******************************************/
    /**
     * Internal class which represents a specific MouseAdapter
     * @author B2MB
     */
    private class InternalMouseListener extends MouseAdapter{
	/**
	 * Creates a new InternalMouseListner 
	 */
	InternalMouseListener(){
	    super();
	}

	/**
	 * Invoked when the mouse has been pressed on a component.
	 * @param a MouseEvent
	 */
	public void mousePressed(MouseEvent e){
	    System.exit(0);
	}
    }
}



