package network;

import configuration.Setup;
import parser.TCPQueryDescriptor;
import controller.GuiController;
import java.io.IOException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.ListIterator;
import java.net.Socket;

public class Servent
{
    private ClientSystemNetworkPerformer clientSystem;
    private Client clients;
    private Vector knownPeers;
    private Setup configuration;
    
    /**
     * Starts the servent
     * @param config_file_name the name of the file which contains all the informations
     * on the application.
     */
    public void startServent(String config_file_name, GuiController controller) throws Exception
    {
	this.configuration = new Setup(config_file_name);
	this.clientSystem  = new ClientSystemNetworkPerformer(controller, this.configuration);
	ServerPerformerInterface performer = new ServerPerformer(clientSystem);
	Server server      = new Server(this.configuration, performer);
	System.out.println("Launching the server");
	server.begin();
	System.out.println("Server has launched his threads");
	System.out.println("Servent is initializing its clients");
	this.clients       = new Client(this.configuration);
	this.knownPeers    = new Vector();
    }
    

    /**
     * Begin the download.
     * @param host the host that has the wanted files.
     * @param port the port to which this servent can connect for download.
     * @return the download identifier if everything went well, -1 otherwise.
     */
    public int download(String host, int port)
    {
	ClientPerformerInterface client_performer = new ClientPerformer();
	return this.clients.startClient(host, port, client_performer);
    }
    
    
    /**
     * Cancels the download identified by <code>index</code>
     */
    public void cancelDownload(int index)
    { this.clients.endThread(index); }
    
    
    /**
     * Searches buddies in the P2P network. Initiates the ping pong protocol.
     */
    public void searchPeers()
    {
	
    }
    
    public void sendQuery(Socket socket, String search_criteria) throws IOException
    {   /* A MODIFIER */
	byte [] descriptor_id = {12, 4, 5,  8, 1, 6,  5, 7, 8,  6, 4, 0,  5, 88, 56, 4};
	TCPQueryDescriptor query = new TCPQueryDescriptor(descriptor_id, (byte)10, (byte)0,
							  (short)10,   /* A MODIFIER */
							  search_criteria);
	this.clientSystem.sendQuery(socket, query);
    }
    
    
    private ListIterator getAllAddresses()
    {
	ArrayList addressList = new ArrayList();
	File f = new File(Setup.getPath()+File.pathSeparator+"addressFile.bmb");
	BufferedReader in = new BufferedReader(new InputStreamReader(f));
	String address;
	while((address=in.readLine())!= null)
	    addressList.add(address);
	return addressList.listIterator();
    }
    
    
    public void connect()
    {
	ListIterator it = getAllAddresses();
	String [] addr_port;
	while(it.hasNext())
	    {
		addr_port = ((String)it.next()).split(":");
		if(addr_port.length != 2){ System.err.println("Address file corrupted"); return; }
		if(sendDemand2Connect())
	    }
    }
}
