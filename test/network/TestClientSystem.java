import network.*;
import configuration.Setup;
import java.io.IOException;
import network.*;
import configuration.Setup;
import java.net.Socket;
import java.net.InetSocketAddress;

public class TestClientSystem
{
    public static void main(String [] args) throws Exception
    {
	if(args.length != 2){System.err.println("java TestServer <hote> <port>"); return; }
	Setup configuration = new Setup("config.bmb");
	ClientSystemNetworkPerformer clientSystem = new ClientSystemNetworkPerformer(configuration);
	System.out.println("Let's send a Gnutella Connect, shall we?");
	Socket socket = new Socket();
	try{
	    socket.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])));
	    System.out.println("Le serveur accepte la connexion: "+clientSystem.sendDemand2Connect(socket));
	    socket.close();
	}
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
}
