package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
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
	byte [] data_read;
	//initTimer();
	while(this.listener.isActive()){
	    System.out.println("sdjlfjlsdj");
	    data_read = NetworkUtils.read(socket);
	    if(data_read != null)
		dispatch(data_read, socket);
	}
	this.listener.setActive(false);
    }

    /**
     * Initiates the timer. Regularly, a ping is sent to explore the network.
     */
    private void initTimer()
    {
	final NetworkQueryListener client = this.listener;
	TimerTask taskExecuter = new TimerTask(){
		public void run(){
		    try{
			System.out.println("ServerPerformer: L71: On envoie un ping");
			client.sendPing();
		    }catch(IOException ioe){
			System.err.println("I/O error while sending a ping"); ioe.printStackTrace();
		    }
		}
	    };
	Timer timer = new Timer();
	timer.schedule(taskExecuter, 0, 2*1000000);//sends a ping every 15 seconds
    }
}
