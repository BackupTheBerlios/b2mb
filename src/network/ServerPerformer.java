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
	    System.out.println("Ce qui a été lu:\n"+r.readLine());
	}catch(IOException ioe){ ioe.printStackTrace(); }
    }
}
