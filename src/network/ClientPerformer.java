package network;

import java.net.Socket;
import java.io.PrintStream;
import java.io.IOException;

public class ClientPerformer implements ClientPerformerInterface
{
    private ClientPerformerInterface performer; //The object that gives the client its behaviour
    
    public void perform(Socket socket)
    {
	try{
	    PrintStream out = new PrintStream(socket.getOutputStream());
	    for(int i=0; i<10; i++)
		out.println(i+" bonjour");
	}
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
}
