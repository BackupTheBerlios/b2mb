package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Graphical component which represents the preview window
 * @author B2MB
 */
public class VDisplay extends JPanel{
    //private ServentLinear servent;
    private ServentLinearBuffer servent;
    //private ServentLinearByTen servent;
    // TMP
    VButtonTest top;
    
    /**
     * Creates VDisplay component
     */
    VDisplay(){
	setLayout(new BorderLayout());
	
	top = new VButtonTest();
	//servent = new ServentLinear();
	servent = new ServentLinearBuffer();
	//servent = new ServentLinearByTen();
	
	add(top, BorderLayout.NORTH);
	add(servent, BorderLayout.CENTER);
    }

    // TMP
    public void setImage(String image){
	top.setImage(image);
    }

    /**************************************************************/
    /**
     * Graphical component which represents the button test component
     * @author B2MB
     */
    private class VButtonTest extends JPanel{
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	final private JLabel clientLabel = new JLabel("Client");
	final private JLabel serverLabel = new JLabel("Server");
	final private JLabel widthLabel = new JLabel("Width");
	final private JLabel heightLabel = new JLabel("Height");
	final private JTextField widthField = new JTextField(4);
	final private JTextField heightField = new JTextField(4);
	final private JTextField fileField = new JTextField(10);
	final private JButton clientStart = new JButton("Start");
	final private JButton serverStart = new JButton("Start");
	
	/**
	 * Constructs a VButtonTest 
	 */
	VButtonTest(){
	    layout = new GridBagLayout();
	    constraints = new GridBagConstraints(); 
	    
	    this.setLayout(layout);

	    serverStart.setEnabled(false);
	    
	    // Add the actions
	    clientStart.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			VDisplay.this.repaint();
			servent.startClient(widthField.getText(), heightField.getText());
			serverStart.setEnabled(true);
		    }
		});
	    serverStart.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			servent.startServer(fileField.getText());
		    }
		});
	    
	    // Add the components
	    addComponent(clientLabel, 0, 0, 1, 1);
	    addComponent(widthLabel, 0, 1, 1, 1);
	    addComponent(widthField, 0, 2, 1, 1);
	    addComponent(heightLabel, 0, 3, 1, 1);
	    addComponent(heightField, 0, 4, 1, 1);
	    addComponent(clientStart, 0, 5, 1, 1);
	    
	    addComponent(serverLabel, 1, 0, 1, 1);
	    addComponent(fileField, 1, 1, 1, 1);
	    addComponent(serverStart, 1, 2, 1, 1);
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

	// TMP
	public void setImage(String image){
	    fileField.setText(image);
	}
    }
}


