package gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/************************************************************************
NB : Status va contenir une JProgressBar
************************************************************************/

/**
 * Graphical component which represents the transfer main window
 * @author B2MB
 */
public class VTransfer extends JPanel{
    private VDULoad upload;
    private VDULoad download;
    
    /**
     * Creates Transfer component
     */
    VTransfer(){
	setLayout(new BorderLayout());
	
	// Defintion of the component
	upload = new VDULoad("Upload", new String[] {"File", "Size", "From", "Speed", "Status"});
	download = new VDULoad("Download", new String[] {"File", "Size", "From", "Speed", "Status"});
	
	// Defintion of the main component
	JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, download, upload);
	main.setEnabled(true); // --> Permits to immobilize the divider
	main.setDividerLocation(0.5);
	
	add(main);

	// TEST
	add2Download(new String[] {"Merco.jpg", "10Ko", "10.2.2.2", "30ms", "10Ko/s", "available"});
	add2Download(new String[] {"linux.jpg", "10Ko", "10.2.2.2", "30ms", "10Ko/s", "available"});
    }
    
    /**
     * Permits to add a new line of object to the dowload table 
     * @param objects to add
     */
    public void add2Download(Object[] o){
	download.add2table(o);
    }

    /**
     * Permits to add a new line of object to the upload table 
     * @param objects to add
     */ 
    public void add2Upload(Object[] o){
	upload.add2table(o);
    }

    /**
     * Adds a listener on the tables with a popupmenu
     * Permits to switch at the given tab
     * @param the tabbedpane to switch
     * @param the index of the tab
     */
    public void addListener(JTabbedPane tabbedPane, int index){
	upload.addListener(tabbedPane, index);
	download.addListener(tabbedPane, index);
    }

    /*****************************************************************************/
    /**
     * Graphical component which represents the [up/down]load component
     * @author B2MB
     */
    private static  class VDULoad extends JPanel{
	private VTable table;
	
	/**
	 * Creates VDULoad component
	 * @param label of the table
	 * @param strings to add to the table
	 */
	VDULoad(String label, String[] s){
	    setLayout(new BorderLayout());
	    
	    table = new VTable(s);

	    addText(label);
	    add(table, BorderLayout.CENTER);
	}

	/**
	 * Permits to add some text and icons to the component
	 * @param title of the component
	 */
	private void addText(String l){
	    JPanel p = new JPanel();
	    p.add(new JLabel(l));
	    add(p, BorderLayout.NORTH);
	}

	/**
	 * Permits to add a new line of object to the table 
	 * @param objects to add
	 */
	public void add2table(Object[] o){
	    table.addRow(o);
	}
	
	/**
	 * Adds a listener on the tables with a popupmenu
	 * Permits to switch at the given tab
	 * @param the tabbedpane to switch
	 * @param the index of the tab
	 */
	public void addListener(JTabbedPane tabbedPane, int index){
	    table.addListener(tabbedPane, index);
	}
    }
}





