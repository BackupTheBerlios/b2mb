package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Defines all the server's actions.
 */
public class ServerPerformer implements ServerPerformerInterface
{
    /**
     * Performs the server's actions.
     * Is destined to be used with the following classes: Server, ServerRunnable.
     */
    public void perform(Socket socket)
    {
	try{
	    BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    String read;
	    while((read=r.readLine())!=null)
		System.out.println("Ce qui a �t� lu:\n"+read);
	}catch(IOException ioe){ ioe.printStackTrace(); }
    }
}
