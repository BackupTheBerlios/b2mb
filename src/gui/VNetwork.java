import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
 * @author B2MB
 * Graphical component which represents the network main window
 */
public class VNetwork extends JPanel{
    private DefaultListModel model;
    private JList list;
    // Only max_size elements on the list
    private int max_size = 10;
    
    /**
     * @author B2MB
     * Creates Network component
     */
    VNetwork(){
	setLayout(new BorderLayout());
	
	// Defintion of the list
	model = new DefaultListModel();
	model.setSize(max_size);
	list = new JList(model); 

	// TEST
	add("Connected to 10.0.0.2");
	add("Disconnected from 10.0.7.2");
	JScrollPane bottom = new JScrollPane(list);

	
	// Definition of the components
	VMiddleComponent middle = new VMiddleComponent();
	VTopComponent top = new VTopComponent();
	
	// Defintion of the main component
	JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, middle, bottom);
	main.setEnabled(true); // --> Permits to immobilize the divider
	
	add(top, BorderLayout.NORTH);
	add(main, BorderLayout.CENTER);
    }
    
    /**
     * Permits to add a new object in the list of log
     * @author B2MB
     * @param string to add
     */
    public void add(String s){
	if( model.size() >= max_size ){
	    model.removeElementAt(0);
	}
	model.addElement(s);
    }

    /**
     * Permits to add a new component in the Layout container
     * @author B2MB
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
     * @author B2MB
     * Graphical component which represents the network's middle component
     */
    private static class VMiddleComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JTextField ipField = new JTextField(11);
	private JTextField portField = new JTextField(5);
	private JButton button = new JButton("Connect");
	
	/**
	 * @author B2MB
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
     * @author B2MB
     * Graphical component which represents the network's top component
     */
    private static class VTopComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JLabel connected = new JLabel("Connected");
	private JLabel disconnected = new JLabel("Disconnected");

	/**
	 * @author B2MB
	 * Creates VTopComponent component
	 */
	VTopComponent(){
	    // Layout
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    setLayout(layout);

	    // Components
	    JLabel statusPreview = new JLabel("Your current status is ");
	    
	    // Defintion of the colors
	    connected.setForeground(Color.GREEN);
	    disconnected.setForeground(Color.RED);

	    // Add of components
	    VNetwork.addComponent(this, layout, constraints, statusPreview, 0, 0, 2, 1);
	    connect();
	}
	
	/**
	 * Permits to display Connected message
	 * @author B2MB
	 */
	public void connect(){
	    VNetwork.addComponent(this, layout, constraints, connected, 0, 2, 1, 1);
	}

	/**
	 * Permits to display Disconnected message
	 * @author B2MB
	 */
	public void disconnect(){
	    VNetwork.addComponent(this, layout, constraints, disconnected, 0, 3, 1, 1);
	}
    }
}



