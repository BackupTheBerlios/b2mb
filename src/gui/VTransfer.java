import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/************************************************************************
NB : Status va contenir une JProgressBar
************************************************************************/

/**
 * @author B2MB
 * Graphical component which represents the transfer main window
 */
public class VTransfer extends JPanel{
    private VTable upload;
    private VTable download;
    
    /**
     * @author B2MB
     * Creates Transfer component
     */
    VTransfer(){
	setLayout(new BorderLayout());
	
	// Defintion of the tables
	upload = new VTable(new String[] {"File", "Size", "From", "Speed", "Status"});
	download = new VTable(new String[] {"File", "Size", "From", "Speed", "Status"});
	
	// Defintion of the main component
	JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, download, upload);
	main.setEnabled(true); // --> Permits to immobilize the divider
	main.setDividerLocation(0.5);
	
	add(main);

	// TEST
	add2Download(new String[] {"merco.jpg", "10Ko", "10.2.2.2", "30ms", "10Ko/s", "available"});
    }
    
    /**
     * Permits to add a new line of object to the dowload table 
     * @author B2MB
     * @param objects to add
     */
    public void add2Download(Object[] o){
	download.addRow(o);
    }

    /**
     * Permits to add a new line of object to the upload table 
     * @author B2MB
     * @param objects to add
     */ 
    public void add2Upload(Object[] o){
	upload.addRow(o);
    }    
}



