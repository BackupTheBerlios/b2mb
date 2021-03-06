package network;

import configuration.Setup;
import parser.ResultSet;
import parser.TCPQueryDescriptor;
import controller.GuiController;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;
import java.net.Socket;
import java.net.InetAddress;


/**
 * A Servent is a mix of clients and servers. This class contains a set of server, each
 * of them possessing a client, a ClientSystemNetworkPerformer object. The server is destined
 * to listen for a connection. The client, in the case of B2MB project, is charged to send
 * discovery request like ping/pong, and also queries/query hits.
 * This class is an interface between the GUI and the servent system: when the user asks something,
 * the controller must call only this class.
 */
public class Servent
{
    private ClientSystemNetworkPerformer [] clientSystem;
    private Vector knownPeers;
    private Setup configuration;
    private GuiController controller;
    private Hashtable listClientsServers;
    private ResultSet resultSet;
    
        
    /**
     * Initiates the performers.
     * @param config_file_name the file name that contains all the informations on the 
     * application's property.
     */
    private ServerPerformerInterface [] initPerformers(String config_file_name) throws IOException
    {	//init setup
	this.configuration = new Setup(config_file_name);
	int nbproc = this.configuration.getNbProcessus();
	//allocation
	ServerPerformerInterface [] performers = new ServerPerformerInterface[nbproc];
	this.clientSystem  = new ClientSystemNetworkPerformer[nbproc];
	this.listClientsServers = new Hashtable();
	//initialisation
	for(int i=0; i<clientSystem.length; i++){
	    this.clientSystem[i] =
		new ClientSystemNetworkPerformer(controller, this.configuration, this);
	    performers[i] = new ServerPerformer(this.clientSystem[i]);
	    this.listClientsServers.put(this.clientSystem[i], performers[i]);
	}
	return performers;
    }
    
    /**
     * Starts the servent
     * @param config_file_name the name of the file which contains all the informations
     * on the application.
     */
    public void startServent(String config_file_name) throws Exception
    {
	try{
	    ServerPerformerInterface [] performers = initPerformers(config_file_name);
	    Server server   = new Server(this.configuration, performers);
	    System.out.println("Lancement du Serveur");
	    server.begin();
	    System.out.println("Le Serveur a d�ploy� ses �couteurs");
	    this.knownPeers = new Vector();
	}
	catch(java.net.BindException be)
	    { System.err.println("Unable to start the servent: "+be.getMessage()); }
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
    
    /**
     * Sets the controller.
     * @param controller the needed controller.
     */
    public void setController(GuiController controller)
    { this.controller = controller; }
    
    
    
    /**
     * Begins the download.
     * @param host the host that has the wanted files.
     * @param port the port to which this servent can connect for download.
     * @return the download identifier if everything went well, -1 otherwise.
     */
    public int download(String host, int port)
    {
	return 0;
    }
    
    
    
    /**
     * Kills the client identified by <code>index</code>
     */
    public void killClient(int index)
    { this.clientSystem[index].setActive(false); }
    
    /**
     * Kills all the client
     */
    public void killAllClients()
    {
	for(int i=0; i<clientSystem.length; i++)
	    this.clientSystem[i].setActive(false);
	System.out.println("D�connect�");
    }
    
    
        
    /**
     * Sends a search query to the connected peer.
     * @param search_criteria the query's search criteria
     */
    public void sendQuery(String search_criteria) throws IOException
    {   /* A MODIFIER */
	this.resultSet = null; //initialisation of the result set
	byte [] descriptor_id = {12, 4, 5,  8, 1, 6,  5, 7, 8,  6, 4, 0,  5, 88, 56, 4};
	TCPQueryDescriptor query = new TCPQueryDescriptor(descriptor_id, (byte)10, (byte)0,
							  (short)10,   /* A MODIFIER */
							  search_criteria);
	int index_client = getUnoccupiedClient();
	if(index_client != -1)
	    this.clientSystem[index_client].sendQuery(query);
	else
	    System.err.println("Servent did not send your query: all the clients were occupied.");
    }
    
    
    
    /**
     * Returns the id of an unoccupied client.
     */
    private int getUnoccupiedClient()
    {
	int i;
	for(i=0; i<this.clientSystem.length; i++)
	    if(!this.clientSystem[i].isOccupied())
		break;
	if(i!=this.clientSystem.length)
	    return i;
	return -1;
    }
    
    /**
     * @return the list of all the known addresses.
     */
    private ListIterator getAllAddresses()
    {
	ArrayList addressList = new ArrayList();
	String file_name =
	    this.configuration.getPath()+File.separator+"configuration"+File.separator+"addressFile.bmb";
	try{
	    BufferedReader in = new BufferedReader(new FileReader(file_name));
	    String address;
	    String localAddress = InetAddress.getLocalHost().getHostAddress();
	    
	    while((address=in.readLine())!= null){
		if(!address.equals(localAddress))
		    addressList.add(address);
	    }
	    in.close();
	}
	catch(FileNotFoundException fnfe)
	    { System.err.println(file_name+": File not found");}
	catch(IOException ioe)
	    { System.err.println(ioe.getMessage()); ioe.printStackTrace(); }
	if(addressList.size()==0)System.err.println("Could not connect to any known peer.");
	return addressList.listIterator();
    }
    
    /**
     * Connects to a known peer.
     */
    public boolean connect()
    {
	ListIterator it = getAllAddresses();
	String addressIP;
	int index_client = getUnoccupiedClient();
	if(index_client!=-1){
	    while(it.hasNext())
		{
		    addressIP = (String)it.next();
		    try{
			if(this.clientSystem[index_client].sendDemand2Connect(addressIP,
									      configuration.getPort()))
			    return true;
		    }
		    catch(IOException ioe)
			{ System.err.println(ioe.getMessage()); ioe.printStackTrace(); }
		}
	}
	return false;
    }
    
    
    
    /**
     * When a client sends a query, it must ask to his server to listen for a response.
     */
    protected void activateListening(ClientSystemNetworkPerformer client)
    {
	ServerPerformerInterface server =
	    (ServerPerformerInterface)this.listClientsServers.get(client);
	try{
	    server.perform(client.getSocket());
	}catch(IOException ioe)
	    { System.err.println(ioe.getMessage()); ioe.printStackTrace(); }
    }
    
    
    
    /**
     * Returns the result of a previous search.
     */
    public ResultSet getSearchResults()
    { return this.resultSet; }
}
