package network;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import parser.TCPDescriptorHeader;
import parser.TCPPongDescriptor;
import parser.TCPPingDescriptor;
import parser.TCPQueryDescriptor;
import parser.TCPQueryHitDescriptor;
import parser.PayloadDescriptor;
import parser.ResultSet;
import controller.GuiController;
import utils.NetworkUtils;
import configuration.Setup;

/**
 * This class processes all the queries directly linked to network queries, like
 * GNUTELLA CONNECT, PING.
 * In B2MB, there are two distinct clients:
 * - the ones that ask to download a animated picture
 * - the one that processes queries(GNUTELLA CONNECT, PING) sent by other peers.
 */
public class ClientSystemNetworkPerformer implements NetworkQueryListener
{
    private Setup conf;
    private GuiController controller;
    private Socket socket;
    private boolean isOccupied;
    private boolean active;
    private Timer timer;
    private Servent servent;
    
    
    
    /**
     * The constructor of this object.
     */
    public ClientSystemNetworkPerformer(GuiController controller, Setup conf, Servent servent)
    { this.conf = conf; this.controller = controller; this.servent = servent; }

    
    //---------------------------------- Processing methods ----------------------------------\\
    
    
    /**
     * Sends a PONG after receiving one, if the server is currently able to process the demand.
     * @param socket the socket which links this client to the other server.
     * @param query the byte array that was received.
     */
    private void sendAPong(byte [] query, Socket socket) throws IOException
    {
	System.out.println("Envoi du pong");
	if(TCPDescriptorHeader.getTtl(query) == 0) return; //It's time to leave for the query...
	TCPPongDescriptor pong = new TCPPongDescriptor(TCPDescriptorHeader.getDescriptorID(query),
						       (byte)(TCPDescriptorHeader.getTtl(query)-1),
						       (byte)(TCPDescriptorHeader.getHops(query)+1),
   						       (short)this.conf.getPort(),
						       InetAddress.getLocalHost().getAddress(),
						       0, 0);
	NetworkUtils.write(socket, pong.getTCPPongDescriptor());
    }    
    
    /**
     * Sends a welcome message after receiving a demand from another peer to connect to
     * this servent, if the server is currently able to process the demand.
     * A welcome message is actually defined in the Gnutella protocol v0.4
     * @param socket the socket which links this client to the other peer.
     */
    private void sendWelcomeMessage(Socket socket) throws IOException
    {
	NetworkUtils.write(socket, "GNUTELLA OK\n\n");
    }
    
    /**
     * Sends a query hit to the peer connected by the socket.
     * @param socket the socket which links this client to the other server.
     * @param query the byte array that was received.
     */
    private void sendQueryHit(byte [] query, Socket socket)
    {
	TCPQueryDescriptor query_descriptor = new TCPQueryDescriptor(query);
	String search_criteria = query_descriptor.getSearchCriteria();
	ResultSet results = this.controller.searchFiles(search_criteria);
	try{
	    TCPQueryHitDescriptor query_hit_descriptor =/* A MODIFIER */
		new TCPQueryHitDescriptor(TCPDescriptorHeader.getDescriptorID(query),
					  (byte)10, (byte)0,
					  (byte)results.size(), (short)9090,
					  InetAddress.getLocalHost().getAddress(), 10,
					  results, TCPDescriptorHeader.getDescriptorID(query));
	    
	    NetworkUtils.write(socket, query_hit_descriptor.getTCPQueryHitDescriptor());
	}catch(UnknownHostException uhe)
	    { uhe.printStackTrace(); }
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
    
    
    /**
     * Processes the query received by the server.
     * @param query the raw data that was received by the server.
     */
    public void processQuery(byte [] query, Socket socket)
    {
	byte descriptor = TCPDescriptorHeader.getPayloadDescriptor(query);
	if(query == null)System.out.println("query est null");
	if(socket == null)System.out.println("socket est null");
	this.socket = socket;
	if(this.socket == null)System.out.println("this.socket est null");
	try{
	    if(descriptor == PayloadDescriptor.PING)
		{ System.out.println("On a reçu un ping."); sendAPong(query, socket); this.isOccupied = false; return; }
	    if(descriptor == PayloadDescriptor.QUERY)
		{ sendQueryHit(query, socket); return; }
	    if((new String(query)).equals("GNUTELLA CONNECT/0.4\n\n")){ 
		System.out.println("Someone asks you to connect him/her to the P2P network"); 
		sendWelcomeMessage(socket);
		return; 
	    }
	}catch(IOException ioe){
	    System.err.println("I/O Error in ClientSystemNetworkPerformer L62"); 
	    ioe.printStackTrace();
	}
    }
    
    
    //---------------------------------- Sending methods ----------------------------------\\
    
    
    /**
     * Asks to well-known peer if this servent can connect to the P2P network, via that remote
     * peer.
     * @param socket the socket which links the two peers.
     * @return true if the connection was accepted, false otherwise.
     */
    public boolean sendDemand2Connect(String ipAddress, int port) throws IOException
    {
	this.isOccupied = true;
	this.socket = new Socket(ipAddress, port);
	System.out.println("Le client lance un bonjour dans la toile");
	NetworkUtils.write(this.socket, "GNUTELLA CONNECT/0.4\n\n");
	
	System.out.println("Le client écoute...");
	String response = NetworkUtils.read(this.socket, 0);
	System.out.println("Le client percevrait-il quelque chose? "+response);
	
	if(response.equals("GNUTELLA OK\n\n")){ this.socket = socket; initTimer(); return true; }
	this.socket.close();
	return false;
    }
    
    
    /**
     * Sends a ping to a well-known peer.
     */
    public void sendPing() throws IOException
    {
	this.isOccupied = true;
	byte [] descriptorId = {15, 8, 1,   34, 4, 5,   7, 3, 1,   5, 8, 7,   80, 5, 4, 8 };
	TCPPingDescriptor ping = new TCPPingDescriptor(descriptorId, (byte)5, (byte)0);
	NetworkUtils.write(this.socket, ping.getPingDescriptor());
	System.out.println("On envoie un ping");
	this.servent.activateListening(this);
    }
    
    
    /**
     * Sends a query.
     * @param query the query datagram to be sent.
     */
    public void sendQuery(TCPQueryDescriptor query) throws IOException
    {
	this.isOccupied = true;
	NetworkUtils.write(this.socket, query.getTCPQueryDescriptor());
	this.servent.activateListening(this);
    }
    
    
    //----------------------------- state methods -----------------------------\\
    
    
    /**
     * @return true if this client is occupied sending or processing a query.
     */
    public boolean isOccupied()
    { return this.isOccupied; }
    
    
    /**
     * @return true if this client is ready to proces or is processing.
     */
    public boolean isActive()
    { return this.active; }
    
    /**
     * Sets the activity of this client: if <code>value</code> is true, this client
     * is considered as active. Otherwise the client is considered as inactive, and
     * its socket is closed.
     */
    public void setActive(boolean value)
    {
	this.active = value;
	if(value == false){
	    try{
		this.isOccupied = false;
		if(this.socket!=null)this.socket.close();
		System.out.println("Client was killed");
	    }catch(IOException ioe)
		{ System.err.println("I/O Error while closing a socket."); ioe.printStackTrace(); }
	}
    }
    
    
    //----------------------------- utility methods -----------------------------\\
    

    /**
     * Initiates the timer. Regularly, a ping is sent to explore the network.
     */
    private void initTimer()
    {
	TimerTask taskExecuter = new TimerTask(){
		public void run(){
		    try{
			System.out.println("ClientSystemNetworkPerformer: L222: On envoie un ping");
			sendPing();
		    }catch(IOException ioe){
			System.err.println("I/O error while sending a ping"); ioe.printStackTrace();
		    }
		}
	    };
	this.timer = new Timer();
	timer.schedule(taskExecuter, 0, 2*1000);//sends a ping every 15 seconds
    }
    
    
    /**
     * Returns this client's socket.
     */
    public Socket getSocket()
    { return this.socket; }
}
