package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintStream;

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
	    String read=r.readLine();
	    System.out.println("Ce qui a �t� lu:\n"+read);
	    if(read.equals("Client: bonjour"))
		System.out.println("Le serveur r�pond bonjour");
	    else
		System.out.println("Le serveur n'a pas compris ce qui a �t� dit, mais r�pond bonjour");
	    PrintStream out = new PrintStream(socket.getOutputStream());
	    out.println("Serveur: bonjour");
	    out.flush();
	}catch(IOException ioe){ ioe.printStackTrace(); }
    }
}
