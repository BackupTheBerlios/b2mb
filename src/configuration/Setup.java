//package configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class represents the configuration of the application
 * @author B2MB
 */
public class Setup{
    private String file;
    private Properties prop = null;

    /**
     * @author B2MB
     * Creates a configuration object
     */
    Setup(String fileName) throws IOException, FileNotFoundException{
	file = fileName;

	load();
    }

    /****************************************************************************/
    /**
     * @author B2MB
     * load all the elements contained in a file
     */
    public void load() throws IOException, FileNotFoundException{
	FileInputStream in = new FileInputStream(file);

	if( prop == null )
	    prop = new Properties();

	// load all
	prop.load(in);

	in.close();
    }

     /**
     * @author B2MB
     * save all the elements in a file
     */
    public void save() throws IOException{
	FileOutputStream out = new FileOutputStream(file);
	prop.store(out, "Save");
	out.close();
    }

    /****************************************************************************/

    /**
     * Permit to get the path of the shared files
     * @author B2MB
     * @return path
     */
    public String getPath(){
	return prop.getProperty("path");
    }

     /**
     * Permit to get the number of processus used by the application
     * @author B2MB
     * @return number 
     */
    public int getNbProcessus(){
	return Integer.parseInt(prop.getProperty("nb_processus"));
    }

     /**
     * Permit to get the number of clients authorized by the application
     * @author B2MB
     * @return number 
     */
    public int getNbClients(){
	return Integer.parseInt(prop.getProperty("nb_clients"));
    }
    
    /****************************************************************************/
    /**
     * Permit to set the path of the shared files
     * @author B2MB
     * @param path 
     */
    public void setPath(String path){
	prop.setProperty("path", path);
    }

     /**
     * Permit to set the number of processus used by the application
     * @author B2MB
     * @param value 
     */
    public void setNbProcessus(int value){
	prop.setProperty("nb_processus", String.valueOf(value));
    }

     /**
     * Permit to set the number of clients authorized by the application
     * @author B2MB
     * @param value 
     */
    public void setNbClients(int value){
	prop.setProperty("nb_clients", String.valueOf(value));
    }
}


