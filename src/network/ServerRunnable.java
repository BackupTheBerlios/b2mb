package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

/**
 * This class defines the basic behaviour of a server. It is used in 
 * the Server class. It establishes a Runnable that listens to all the
 * given ports, and acts in consequence.
 */
public class ServerRunnable implements Runnable
{
    private ServerPerformerInterface performer;
    private ServerSocket server_socket;
    
    public ServerRunnable(int port, ServerPerformerInterface performer) 
	throws IOException
    {
	this.performer = performer;
	server_socket  = new ServerSocket(port);
    }
    
    
    public void run()
    {
	while(true){
	    Socket socket = null;
	    try{
		synchronized(this.server_socket){
		    socket = this.server_socket.accept();
		}//...synchronized
		socket.setSoTimeout(1000);
		this.performer.perform(socket);
	    }//...try
	    catch(SocketException se)
		{ System.err.println("socket exception: Connection may be closed at the other end."); }
	    catch(IOException ioe){ ioe.printStackTrace(); }
	    finally{
		try{ socket.close(); System.out.println("La connexion a ete fermee."); }
		catch(IOException ioe){ ioe.printStackTrace(); }
	    }
	}//...while
    }
}
