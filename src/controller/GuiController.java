package controller;

import java.util.ArrayList;
import javax.swing.JTabbedPane;
import gui.*;

/**
 * This class regroups all the methods contained in all the VComponents. 
 * @author B2MB
 */
public class GuiController{
    private JTabbedPane tab;

    /**
     * Contructs a GuiController object
     * @param a JTabbedPane which represents the model
     */
    public GuiController(JTabbedPane tab){
	this.tab = tab;
    }

    /*************************************************/
    // From VShared component
    
    /**
     * Permits to scan the shared files' directory and add the files
     */
    public void scanDirectory(){
	((VShared)tab.getComponentAt(4)).scanDirectory();
    }

    /**
     * Permits to get the list of files from a specified file name
     * @param file name
     * @return a list of file names
     */
    public ArrayList getAllFiles(String s){
	return ((VShared)tab.getComponentAt(4)).getAllFiles(s);
    }

    /*************************************************/
    // From VNetwork

    /**
     * Permits to add a new object in the list of log
     * @param string to add
     */
    public void add2Log(String s){
	((VNetwork)tab.getComponentAt(0)).add(s);
    }
    
    /**
     * Removes the log
     */
    public void removeAllFromLog(){
	((VNetwork)tab.getComponentAt(0)).removeAll();
    }
    
    /**
     * Permits to display Connected message
     */
    public void connect(){
	((VNetwork)tab.getComponentAt(0)).connect();
    }

    /**
     * Permits to display Disconnected message
     */
    public void disconnect(){
	((VNetwork)tab.getComponentAt(0)).disconnect();
    }

    /*************************************************/
    
    
}


