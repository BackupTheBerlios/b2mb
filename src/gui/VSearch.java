package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import parser.ResultSet;
import controller.GuiController;


/**
 * Graphical component which represents the search main window
 * @author B2MB
 */
public class VSearch extends JPanel{
    private GuiController controller;
    private ResultSet resultSet;
    
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
    
    /**
     * Sets the controller.
     * @param controller the needed controller.
     */
    public void setController(GuiController controller)
    { this.controller = controller; }
    
    
    /**************************************************************/
    /**
     * Graphical component which represents the search top component
     * @author B2MB
     */
    private class VTopSearchComponent extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JButton search;
	private JTextField searchField;

	/**
	 * Construcsts a VTopSearchComponent 
	 */
	VTopSearchComponent(){
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    
	    setLayout(layout);
	    this.search = new JButton("Search");
	    this.searchField = new JTextField(20);
	    
	    search.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    if(controller == null) System.out.println("controller est null");
		    if(searchField == null) System.out.println("searchField est null");
		    
		    String content = searchField.getText();
		    if(content == null) System.out.println("content est null");
		    if(content != null && !content.equals(""))
			resultSet = controller.search(content);
		}
	    });
	    
	    //addComponent(new JLabel("Search"), 0, 0, 1, 1);
	    addComponent(searchField,0,0,1,1);
	    addComponent(search, 0, 1, 1, 1);
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

