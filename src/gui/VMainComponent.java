package fr.umlv.b2mb.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JToolBar.Separator;
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

    // Tabs component
    private VNetwork network = new VNetwork();
    private VShared sharedFile = new VShared("My folder");
    private VTransfer transfer = new VTransfer();
    private VSearch search = new VSearch();
    private VDisplay display = new VDisplay();
    
    /**
     * Creates a VMainComponent
     * @author B2MB
     */
    VMainComponent(){
	super("B2M2 Peer to Peer");
	
	// Menu
	//initMenuBar();
	
	// Toolbar
	initToolBar();
	
	// Create JTabbPane with a default tab placement of JTabbedPane.TOP
	tabbedPane = new JTabbedPane();
	// And add all the tabs
	initTabs();

	// Default Settings
	// setSize(new Dimension(500, 500));
	setSize(Toolkit.getDefaultToolkit().getScreenSize());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Init the application's menu bar
     * @author B2MB
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
     * @author B2MB
     */
    private void initToolBar(){
	Dimension dim =  new Dimension(50, 10);
	toolBar = new JToolBar();

	JButton connect = new JButton ("Connect");
	connect.setToolTipText("Get connected");
	connect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    network.connect();
		}
	    });
	toolBar.add(connect);

	toolBar.add(new JToolBar.Separator(dim));
	
	JButton disconnect = new JButton ("Disconnect");
	disconnect.setToolTipText("Get disconnected");
	disconnect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    network.disconnect();
		}
	    });
	toolBar.add(disconnect);

	toolBar.add(new JToolBar.Separator(dim));

	JButton scan = new JButton ("Scan");
	scan.setToolTipText("Scan the shared files' folder");
	scan.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    sharedFile.scanDirectory();
		}
	    });
	toolBar.add(scan);

	toolBar.add(new JToolBar.Separator(dim));
	
	JButton exit = new JButton ("Quit");
	//exit.setIcon(new ImageIcon("Icons/Paste24.gif"));
	exit.setToolTipText("Exit the application");
	exit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    System.exit(0);
		}
	    });
	toolBar.add(exit);
	
	getContentPane().add(toolBar,BorderLayout.NORTH);
    }
    
    /**
     * Inits the application's tabs
     * @author B2MB
     */
    private void initTabs(){
	// addTab(String title, Icon icon, Component component, String tip);
	
	tabbedPane.addTab("Network", null, network, "Your network's connexion");
	
	VTable servent = new VTable(new String[]{"IP", "Port", "Toto"});
	servent.addRow(new Object[] {new Integer(123), "IO", "5 mn"});
	tabbedPane.addTab("Servents", null, servent, "Connected Servents");
	
	tabbedPane.addTab("Transfer", null, transfer, "Transfered files");

	tabbedPane.addTab("Search", null, search, "Search files");

	tabbedPane.addTab("Shared Files", null, sharedFile, "Shared files");
	
	tabbedPane.addTab("Prewiew", null, display, "Display the current downloading file");
	
	getContentPane().add(tabbedPane);
    }

    /******************************************/
    /**
     * Internal class which represents a specific MouseAdapter
     */
    private class InternalMouseListener extends MouseAdapter{
	/**
	 * Creates a new InternalMouseListner 
	 * @author B2MB
	 */
	InternalMouseListener(){
	    super();
	}

	/**
	 * Invoked when the mouse has been pressed on a component.
	 * @author B2MB
	 */
	public void mousePressed(MouseEvent e){
	    System.exit(0);
	}
    }
}



