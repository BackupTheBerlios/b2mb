package network;

import java.net.ServerSocket;
import java.io.IOException;

/**
 * This class manages all the deeds of a server. It's behaviour is dictated
 * by ServerPerformer. This object installs everything for futur communication
 * with clients. It does nothing else.
 */
public class Server
{
    private int conf; /* TEMPORARY: TO BE REPLACED BY A CONFIGURATION OBJECT */
    private Thread [] server_threads;
    
    
    /**
     * Creates an instance of a server. This constructor only creates an array
     * of threads. The size of the array depends on the configuration of the system.
     * If you want the server to begin his job, you will have to call the begin() 
     * method.
     * @param conf: The object that contains the configuration of the application.
     * @param performer: An object that implements the ServerPerformer interface. The
     * latter dictates the behaviour of the server.
     */
    public Server(int conf, ServerPerformerInterface performer) /* TEMPORARY: TO BE REPLACED BY A CONFIGURATION OBJECT */
	throws IOException
    {
	/* initialisation */
	this.conf           = conf; /* TEMPORARY: TO BE REPLACED BY A CONFIGURATION OBJECT */
	this.server_threads = new Thread[conf]; /* TEMPORARY: TO BE REPLACED BY A CONFIGURATION OBJECT */
	
	/* creation of the thread array */
	Runnable r = new ServerRunnable(conf, 8080, performer);
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
