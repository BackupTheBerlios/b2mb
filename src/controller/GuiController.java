package controller;

import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JTabbedPane;
import gui.VMainComponent;
import gui.*;
import parser.ResultSet;
import network.Servent;

/**
 * This class regroups all the methods contained in all the VComponents. 
 * @author B2MB
 */
public class GuiController{
    private JTabbedPane tab;
    private Servent servent;

    /**
     * Contructs a GuiController object
     */
    public GuiController(){
    }

    /*************************************************/
    /**
     * Set the view
     * @param 
     */
    public void setView(VMainComponent m){
	this.tab = m.getPane();
    }

    /**
     * Set the model
     * @param 
     */
    public void setModel(Servent servent){
	this.servent = servent;
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
    
    /**
     * Permits to get similar name to the given name
     * @param file name
     * @return a list of file names
     * @return null if nothing corresponds
     */
    public ResultSet searchFiles(String s){
	return ((VShared)tab.getComponentAt(4)).searchFiles(s);
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
	if(this.servent.connect())
	    ((VNetwork)tab.getComponentAt(0)).connect();
    }

    /**
     * Permits to display Disconnected message
     */
    public void disconnect(){
	this.servent.killAllClients();
	((VNetwork)tab.getComponentAt(0)).disconnect();
    }

    /*************************************************/
    // From VSearch
    /**
     * Activates the search for a file.
     * @return the result set.
     */
    public ResultSet search(String content)
    {
	try{
	    ResultSet results;
	    this.servent.sendQuery(content);
	    for(int i=0; i<80; i++) {
		if((results = this.servent.getSearchResults())!=null)
		    return results;
		try{ Thread.sleep(500); } catch(InterruptedException ie){}
	    }
	}catch(IOException ioe){ioe.printStackTrace();}
	return null;
    }
    
}
