package network;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
    
    
    /**
     * The constructor of this object.
     */
    public ClientSystemNetworkPerformer(GuiController controller, Setup conf)
    { this.conf = conf; this.controller = controller; }

    
    //---------------------------------- Processing methods ----------------------------------\\
    
    
    /**
     * Sends a PONG after receiving one, if the server is currently able to process the demand.
     * @param socket the socket which links this client to the other server.
     * @param query the byte array that was received.
     */
    private void sendAPong(byte [] query, Socket socket) throws IOException
    {
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
	try{
	    if(descriptor == PayloadDescriptor.PING)
		{ sendAPong(query, socket); return; }
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
    public boolean sendDemand2Connect(Socket socket) throws IOException
    {
	System.out.println("Le client lance un bonjour dans la toile");
	NetworkUtils.write(socket, "GNUTELLA CONNECT/0.4\n\n");
	
	System.out.println("Le client �coute...");
	String response = NetworkUtils.read(socket, 0);
	System.out.println("Le client percevrait-il quelque chose? "+response);
	
	if(response.equals("GNUTELLA OK\n\n"))return true;
	return false;
    }
    

    /**
     * Sends a ping to a well-known peer.
     * @param socket the socket which links the two peers.
     * @param ping the ping datagram to be sent.
     */
    public void sendPing(Socket socket, TCPPingDescriptor ping) throws IOException
    {
	NetworkUtils.write(socket, ping.getPingDescriptor());
	
	System.out.println("Le client �coute...");
	byte [] array = NetworkUtils.read(socket);
    }
    

    /**
     * Sends a query.
     * @param socket the socket which links the two peers.
     * @param query the query datagram to be sent.
     */
    public void sendQuery(Socket socket, TCPQueryDescriptor query) throws IOException
    {
	NetworkUtils.write(socket, query.getTCPQueryDescriptor());
    }
}
