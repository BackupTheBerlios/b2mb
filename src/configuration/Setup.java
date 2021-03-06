package configuration;

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
     * Creates a configuration object
     */
    public Setup(String fileName) throws IOException, FileNotFoundException{
	file = fileName;

	load();
    }

    /****************************************************************************/
    /**
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
     * @return path
     */
    public String getPath(){
	return prop.getProperty("path");
    }

    /**
     * Permit to get the number of processus used by the application
     * @return number 
     */
    public int getNbProcessus(){
	return Integer.parseInt(prop.getProperty("nb_processus"));
    }

    /**
     * Permit to get the number of clients authorized by the application
     * @return number 
     */
    public int getNbClients(){
	return Integer.parseInt(prop.getProperty("nb_clients"));
    }
    
    /**
     * Permit to get the available port.
     * @return the port
     */
    public int getPort(){
	return Integer.parseInt(prop.getProperty("port"));
    }
    
    /****************************************************************************/
    /**
     * Permit to set the available port
     * @param port
     */
    public void setPort(int port){
	prop.setProperty("port", String.valueOf(port));
    }
    
    /**
     * Permit to set the path of the shared files
     * @param path 
     */
    public void setPath(String path){
	prop.setProperty("path", path);
    }

    /**
     * Permit to set the number of processus used by the application
     * @param value 
     */
    public void setNbProcessus(int value){
	prop.setProperty("nb_processus", String.valueOf(value));
    }

    /**
     * Permit to set the number of clients authorized by the application
     * @param value 
     */
    public void setNbClients(int value){
	prop.setProperty("nb_clients", String.valueOf(value));
    }
}


