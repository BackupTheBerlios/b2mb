import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author B2MB
 * Graphical component which represents a scrollable JTable 
 */
public class VTable extends JScrollPane{
    private InternalTable internalTable;
	
    /**
     * @author B2MB
     * Creates ConnectedServent component
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
     * @author B2MB
     * @param row : a list of the object to add
     * Add a row at the end of the internal table in the scrollpane
     */
    public void addRow(Object[] row){
	internalTable.addRow(row);
    }

    /***********************************************************/
    /**
     * @author B2MB
     * Table component inclued in the ConnectedServent component 
     */
    private static class InternalTable extends JTable{
	private DefaultTableModel tableModel;
	
	/**
	 * @author B2MB
	 * Creates an empty table
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
	 * @author B2MB
	 * @param row : a list of the object to add
	 * Add a row at the end of the table model 
	 */
	public void addRow(Object[] row){
	    tableModel.addRow(row);
	}
    }
}




