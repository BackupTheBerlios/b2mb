package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * This class defines the basic behaviour of a server. It is used in 
 * the Server class. It establishes a Runnable that listens to all the
 * given ports, and acts in consequence.
 */
public class ServerRunnable implements Runnable
{
    private int conf;
    private ServerPerformerInterface performer;
    private ServerSocket server_socket;
    
    public ServerRunnable(int conf, int port, ServerPerformerInterface performer) 
	/* TEMPORARY: _______| :TO BE REPLACED BY A CONFIGURATION OBJECT */
	throws IOException
    {
	this.conf      = conf;
	this.performer = performer;
	server_socket  = new ServerSocket(port); /* TEMPORARY: TO BE REPLACED BY A CONFIGURATION OBJECT */
    }
    
    
    public void run()
    {
	while(true){
	    Socket socket = null;
	    try{
		synchronized(this.server_socket){
		    socket = this.server_socket.accept();
		}//...synchronized
		this.performer.perform(socket);
	    }//...try
	    catch(IOException ioe){ ioe.printStackTrace(); }
	    finally{
		try{ socket.close(); System.out.println("La connexion a ete fermee."+System.currentTimeMillis()); }
		catch(IOException ioe){ ioe.printStackTrace(); }
	    }
	}//...while
    }
}
