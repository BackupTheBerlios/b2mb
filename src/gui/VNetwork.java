package fr.umlv.b2mb.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

/**
 * Graphical component which represents the network main window
 * @author B2MB
 */
public class VNetwork extends JPanel{
    private DefaultListModel model;
    private JList list;
    private VTopComponent top; 
   
    /**
     * Creates Network component
     */
    VNetwork(){
	setLayout(new BorderLayout());
	
	// Defintion of the list
	model = new DefaultListModel();
	list = new JList(model); 

	// TEST
	add("Connected to 10.0.0.2");
	add("Disconnected from 10.0.7.2");
	JScrollPane bottom = new JScrollPane(list);

	
	// Definition of the components
	VMiddleComponent middle = new VMiddleComponent();
	top = new VTopComponent();
	
	// Defintion of the main component
	JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, middle, bottom);
	main.setEnabled(true); // --> Permits to immobilize the divider
	
	add(top, BorderLayout.NORTH);
	add(main, BorderLayout.CENTER);
    }
    
    /**
     * Permits to add a new object in the list of log
     * @param string to add
     */
    public void add(String s){
	if( model.size() >= max_size ){
	    model.removeElementAt(0);
	}
	model.addElement(s);
    }


    /**
     * Permits to display Connected message
     */
    public void connect(){
	top.connect();
    }

    /**
     * Permits to display Disconnected message
     */
    public void disconnect(){
	top.disconnect();
    }

    /**
     * Permits to add a new component in the Layout container
     * @param component which will contain the layout
     * @param layout to add to 
     * @param constraints to add to
     * @param component to add
     * @param row number
     * @param column number
     * @param width of the component in the layout container
     * @param height of the component in the layout container
     */
    private static void addComponent(JPanel main, GridBagLayout layout, GridBagConstraints constraints, Component component, int row, int column, int width, int height)
    {
	constraints.gridx = column;
	constraints.gridy = row;
	
	constraints.gridwidth = width;
	constraints.gridheight =  height;
	constraints.anchor = GridBagConstraints.NORTH;
	layout.setConstraints(component, constraints);
	main.add(component);
    }
    
    /*****************************************************************/
    
    /**
     * Graphical component which represents the network's middle component
     * @author B2MB
     */
    private static class VMiddleComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JTextField ipField = new JTextField(11);
	private JTextField portField = new JTextField(5);
	private JButton button = new JButton("Connect");
	
	/**
	 * Creates VMiddleComponent component
	 */
	VMiddleComponent(){
	    // Layout
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    setLayout(layout);

	    // Components
	    JLabel statusPreview = new JLabel("Your current status is ");
	    JLabel ip = new JLabel("IP");
	    JLabel port = new JLabel("Port");
	    
	    // Add of components
	    VNetwork.addComponent(this, layout, constraints, ip, 0, 0, 1, 1);
	    VNetwork.addComponent(this, layout, constraints, ipField, 0, 1, 1, 1);
	    VNetwork.addComponent(this, layout, constraints, port, 0, 2, 1, 1);
	    VNetwork.addComponent(this, layout, constraints, portField, 0, 3, 1, 1);
	    VNetwork.addComponent(this, layout, constraints, button, 0, 4, 1, 1);
	}
    }

     /*****************************************************************/
    
    /**
     * Graphical component which represents the network's top component
     * @author B2MB
     */
    private static class VTopComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JLabel status = new JLabel();
	
	/**
	 * Creates VTopComponent component
	 */
	VTopComponent(){
	    // Layout
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    setLayout(layout);

	    // Components
	    JLabel statusPreview = new JLabel("Your current status is ");

	    // Add of components
	    VNetwork.addComponent(this, layout, constraints, statusPreview, 0, 0, 2, 1);

	    // Add the status label
	    VNetwork.addComponent(this, layout, constraints, status, 0, 3, 1, 1);
	    disconnect();
	}
	
	/**
	 * Permits to display Connected message
	 */
	public void connect(){
	    // Defintion of the color
	    status.setForeground(Color.GREEN);
	    
	    // Defintion of the text
	    status.setText("Connected");
	}

	/**
	 * Permits to display Disconnected message
	 */
	public void disconnect(){
	     // Defintion of the color
	    status.setForeground(Color.RED);
	    
	    // Defintion of the text
	    status.setText("Disconnected");
	}
    }
}



