package fr.umlv.b2mb.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
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

    /***********************************************************/
    /**
     * Table component inclued in the ConnectedServent component 
     * @author B2MB
     */
    private static class InternalTable extends JTable{
	private DefaultTableModel tableModel;
	
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
    }
}




