package network;

import java.net.Socket;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientPerformer implements ClientPerformerInterface
{
    private ClientPerformerInterface performer; //The object that gives the client its behaviour
    
    public void perform(Socket socket)
    {
	try{
	    PrintStream out = new PrintStream(socket.getOutputStream());
	    System.out.println("Le client lance un bonjour dans la toile");
	    out.println("Client: bonjour");
	    out.flush();
	    
	    System.out.println("Le client écoute...");
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    String read = in.readLine();
	    System.out.println("Le client se sent moins seul parce qu'on lui a répondu.");
	    System.out.println("Cette personne a dit: "+read);
	}
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
}
