package network;

import java.io.IOException;
import java.net.Socket;
import utils.NetworkUtils;
import parser.TCPDescriptorHeader;
import parser.PayloadDescriptor;


/**
 * Defines all the server's actions.
 */
public class ServerPerformer implements ServerPerformerInterface
{
    private NetworkQueryListener listener;
    
    
    public ServerPerformer(NetworkQueryListener listener)
    { this.listener = listener; }
    
    
    /**
     * Dispatches the processing of the query depending on the nature of the query.
     * @param query the query that was sent to this server.
     */
    private void dispatch(byte [] query, Socket socket)
    {
	byte descriptor = TCPDescriptorHeader.getPayloadDescriptor(query);
	if(this.listener == null)System.out.println("listener est null");
	if(query == null)System.out.println("query est null");
	if(socket == null)System.out.println("socket est null");
	switch(descriptor)
	    {
	    case(PayloadDescriptor.QUERYHIT):
		/* Faudrait appeler l'objet intéressé par un queryhit */
		System.out.println("Le serveur a reçu un queryhit");
		break;
	    case(PayloadDescriptor.PONG):
		System.out.println("Le serveur a reçu un pong");
		break;
	    default:
		this.listener.processQuery(query, socket);
	    }
    }
    
    
    /**
     * Performs the server's actions.
     * Is destined to be used with the following classes: Server, ServerRunnable.
     */
    public void perform(Socket socket) throws IOException
    {
	this.listener.setActive(true);
	while(this.listener.isActive()){
	    System.out.println("On est dans la boucle");
	    dispatch(NetworkUtils.read(socket), socket);
	}
    }
}
