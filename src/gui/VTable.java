package gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Graphical component which represents a scrollable JTable 
 * @author B2MB
 */
public class VTable extends JScrollPane{
    private InternalTable internalTable;
	
    /**
     * Creates ConnectedServent component
     * @param a table of string respresenting the headers 
     */
    VTable(String[] headers){
	super();
	// Create and set an internal table 
	internalTable = new InternalTable(headers);
	setViewportView(internalTable);
	internalTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
	// setBackground(Color.white);
    }
    
    /**
     * Add a row at the end of the internal table in the scrollpane
     * @param row : a list of the object to add
     */
    public void addRow(Object[] row){
	internalTable.addRow(row);
    }

    /**
     * Adds a listener on the table with a popupmenu
     * Permits to switch at the given tab
     * @param the tabbedpane to switch
     * @param the index of the tab
     */
    public void addListener(JTabbedPane tabbedPane, int index){
	internalTable.createPopupMenu(tabbedPane, index);
    }

    /***********************************************************/
    /**
     * Table component inclued in the ConnectedServent component 
     * @author B2MB
     */
    private static class InternalTable extends JTable{
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	
	/**
	 * Creates an empty table
	 * @param a table of string respresenting the headers 
	 */
	InternalTable(String[] headers){
	    tableModel = new DefaultTableModel(headers, 0);
	    setModel(tableModel);

	    /*
	    // Modify by
	    TableSorter sorter = new TableSorter(myModel); //ADDED THIS
	    //JTable table = new JTable(myModel);          //OLD
	    JTable table = new JTable(sorter);             //NEW
	    sorter.addMouseListenerToHeaderInTable(table); //ADDED THIS
	    */
	}
	
	/**
	 * Add a row at the end of the table model 
	 * @param row : a list of the object to add
	 */
	public void addRow(Object[] row){
	    tableModel.addRow(row);
	}

	/**
	 * Permits to create a popMenu which will listen to the right click
	 * @param the tabbedpane to switch
	 * @param the index of the tab
	 */
	private void createPopupMenu(final JTabbedPane tabbedPane, final int index){//RightActionListener ra){
	    JMenuItem menuDisplay = new JMenuItem( "Display" );
	    
	    // Create a popup menu
	    popupMenu = new JPopupMenu( "Menu" );
	    popupMenu.add( menuDisplay );
	    
	    enableEvents( AWTEvent.MOUSE_EVENT_MASK );
	    menuDisplay.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent event){
			if( (getSelectedRow() >= 0) && (getSelectedRow() < getRowCount()) ){
			    tabbedPane.setSelectedIndex(index);
			    String s = (String)getValueAt(getSelectedRow(), 0);
			    System.out.println("Index "+s);
			    VDisplay display = (VDisplay)tabbedPane.getComponentAt(index);
			    display.setImage(s);

			}
		    }
		});
	    
	    // And add it
	    this.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e){
			if (SwingUtilities.isRightMouseButton(e)){
			    popupMenu.show(e.getComponent(),
					   e.getX(), e.getY());
			}
		    }
		});
	}
    }
}




