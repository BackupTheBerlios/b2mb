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
	switch(descriptor)
	    {
	    case(PayloadDescriptor.QUERYHIT):
		/* Faudrait appeler l'objet intéressé par un queryhit */
		break;
	    default:
		this.listener.processQuery(query, socket);
	    }
    }
    
    
    /**
     * Performs the server's actions.
     * Is destined to be used with the following classes: Server, ServerRunnable.
     */
    public void perform(Socket socket)
    {
	try{
	    listener.setOK(false);
	    while(!listener.getOK())
		dispatch(NetworkUtils.read(socket), socket);
	}catch(IOException ioe){ ioe.printStackTrace(); }
    }
}
