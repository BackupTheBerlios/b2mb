package network;

import java.net.ServerSocket;
import java.io.IOException;
import configuration.Setup;

/**
 * This class manages all the deeds of a server. It's behaviour is dictated
 * by ServerPerformer. This object installs everything for futur communication
 * with clients. It does nothing else.
 */
public class Server
{
    private Setup conf;
    private Thread [] server_threads;
    
    
    /**
     * Creates an instance of a server. This constructor only creates an array
     * of threads. The size of the array depends on the configuration of the system.
     * If you want the server to begin his job, you will have to call the begin() 
     * method.
     * @param conf: The object that contains the configuration of the application.
     * @param performer: An object that implements the ServerPerformerInterface interface. The
     * latter dictates the behaviour of the server.
     */
    public Server(Setup conf, ServerPerformerInterface performer)
	throws IOException
    {
	/* initialisation */
	this.conf           = conf;
	this.server_threads = new Thread[this.conf.getNbProcessus()];
	
	/* creation of the thread array */
	Runnable r = new ServerRunnable(this.conf.getPort(), performer);
	for(int i=0; i<this.server_threads.length; i++)
	    this.server_threads[i]=new Thread(r);
    }
    
    
    /**
     * Starts all the server's threads
     */
    public void begin()
    {
	for(int i=0; i<this.server_threads.length; i++)
	    this.server_threads[i].start();
    }
}
