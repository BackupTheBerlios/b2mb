package network;

/**
 * <p>This class represents a client in a client-server architecture.
 * It simply initiate an array of thread that is activated by the
 * method begin().</p>
 * <p>The developer can start or stop a client-thread with the methods
 * startThread() or endThread(). These methods don't affect the whole
 * array of threads.</p>
 */
public class Client
{
    private Thread  [] clients; //Contains all the client-threads
    private boolean [] free_clients; //Tells which clients are free
    private int        conf; //Contains all the parameters of the application
    private ClientPerformerInterface performer; //The object that gives the client its behaviour
    
    
    
    /**
     * Creates an instance of a client.<br>
     * <b>Warning:</b> the call to this constructor MUST be followed
     * by the call to begin(). Otherwise, no thread will be started.
     * @param conf The object that contains the configuration of the application.
     * @param performer An object that implements the ClientPerformerInterface
     * interface. The latter dictates the behaviour of the client.
     */
    public Client(int conf, ClientPerformerInterface performer)
    {
	this.conf      = conf;
	this.performer = performer;
	initialiseFreeClients();
	initialiseClientThreads();
    }
    
    /*
     * Initialises the array that tells which clients are free.
     */
    private void initialiseFreeClients()
    {
	this.free_clients = new boolean[this.conf];
	for(int i=0; i<this.free_clients.length; i++)
	    this.free_clients[i] = true;
    }    
    
    /*
     * Initialises the array of client-threads
     */
    private void initialiseClientThreads()
    {
	Runnable r = new ClientRunnable();
	
	this.clients = new Thread[this.conf];
	for(int i=0; i<this.clients.length; i++)
	    this.clients[i] = new Thread(r);
    }
    
    
    
    /**
     * Starts all the client's threads.
     */
    public void begin()
    {	
	for(int i=0; i<this.clients.length; i++)
	    this.clients[i].start();
    }
    
    
    
    /**
     * Starts a client-thread that is chosen by this object.
     * This system manages the selection of a client-thread, and selects one
     * that is inactive.
     * @return the index of the thread that was selected, -1 if none could
     * have been selected.
     */
    public int startThread()
    {
	return -1;
    }
    
    
    /**
     * Ends the activity of the client that has <code>index</code> for reference.
     * @param index the index that was given by startThread().
     */
    public void endThread(int index)
    {
	
    }
}
