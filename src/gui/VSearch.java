package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;


/**
 * Graphical component which represents the search main window
 * @author B2MB
 */
public class VSearch extends JPanel{
    /**
     * Creates VSearch component
     */
    VSearch(){
	setLayout(new BorderLayout());
	
	VTopSearchComponent top = new VTopSearchComponent();
	VTable bottom = new VTable(new String[] {"File", "Size", "From", "Time", "Speed", "Status"});

	// TEST
	bottom.addRow(new String[]{"merco.jpg", "10Ko", "10.2.2.2", "30ms", "10Ko/s", "available"});
	bottom.addRow(new String[]{"bmw.jpg", "10Ko", "10.2.2.5", "20ms", "12Ko/s", "busy"});

	add(top, BorderLayout.NORTH);
	add(bottom, BorderLayout.CENTER);
    }
    
    /**************************************************************/
    /**
     * Graphical component which represents the search top component
     * @author B2MB
     */
    private static class VTopSearchComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	/**
	 * Construcsts a VTopSearchComponent 
	 */
	VTopSearchComponent(){
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    
	    setLayout(layout);
	    //addComponent(new JLabel("Search"), 0, 0, 1, 1);
	    addComponent(new JTextField(20),0,0,1,1);
	    addComponent(new JButton("Search"), 0, 1, 1, 1);
	    addComponent(new JButton("Download"), 0, 2, 1, 1);
	}
	
	/**
	 * Permits to add a new component in the Layout container
	 * @param component to add
	 * @param row number
	 * @param column number
	 * @param width of the component in the layout container
	 * @param height of the component in the layout container
	 */
	private void addComponent(Component component, int row, int column, int width, int height)
	{
	    constraints.gridx = column;
	    constraints.gridy = row;
	    
	    constraints.gridwidth = width;
	    constraints.gridheight =  height;
	    constraints.anchor = GridBagConstraints.NORTH;
	    layout.setConstraints(component, constraints);
	    this.add(component);
	}
    }
}

