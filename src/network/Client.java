package network;

import configuration.Setup;

/**
 * <p>This class represents a client in a client-server architecture.
 * It simply initiates an array of thread that is activated by the
 * method begin().</p>
 * <p>The developer can start or stop a client-thread with the methods
 * startThread() or endThread(). These methods don't affect the whole
 * array of threads.</p>
 */
public class Client
{
    private Thread  [] clients; //Contains all the client-threads
    private boolean [] free_clients; //Tells which clients are free
    private Setup      conf; //Contains all the parameters of the application
    
    
    
    /**
     * Creates an instance of a client.<br>
     * <b>Warning:</b> the call to this constructor MUST be followed
     * by the call to startClient(). Otherwise, no thread will be started.
     * @param conf The object that contains the configuration of the application.
     */
    public Client(Setup conf)
    {
	this.conf    = conf;
	this.clients = new Thread[this.conf.getNbProcessus()];
	initialiseFreeClients();
    }
    
    /*
     * Initialises the array that tells which clients are free.
     */
    private void initialiseFreeClients()
    {
	this.free_clients = new boolean[this.conf.getNbProcessus()];
	for(int i=0; i<this.free_clients.length; i++)
	    this.free_clients[i] = true;
    }    
    
    /**
     * Removes dead threads from the list of the occupied threads.
     * May enable startClient() to process a little bit faster.
     */
    public void removeDeadThreads()
    {
	int i;
	
	for(i=0; i<this.free_clients.length; i++)
	    if( (this.clients[i] != null)&&(!this.clients[i].isAlive()) )
		this.free_clients[i] = true;
    }
    
    
    /**
     * Starts a client-thread that is chosen by this object.
     * This system manages the selection of a client-thread, and selects one
     * that is inactive.
     * <b>WARNING:</b>Make sure to create a new performer each time you call
     * this method, because it is not garanteed that there won't be any
     * share-variables problems with the threads.
     * @return the index of the thread that was selected, -1 if none could
     * have been selected.
     */
    public int startClient(String host, int port, ClientPerformerInterface performer)
    {
	int i;
	
	//finds a free client in 'free_clients'
	for(i=0; (i<this.free_clients.length) && (!this.free_clients[i]); i++)
	    if( (this.clients[i] != null)&&(!this.clients[i].isAlive()) )
		break;
		
	if(i==this.free_clients.length)	return -1;
	
	//initialise the client's property
	Runnable r = new ClientRunnable(performer, host, port);	
	this.clients[i] = new Thread(r);
	
	//the client starts his activity
	this.clients[i].start();
	
	return i;
    }
    
    
    /**
     * Ends the activity of the client that has <code>index</code> for reference.
     * @param index the index that was given by startThread().
     */
    public void endThread(int index)
    {
	
    }
}
