import network.*;
import configuration.Setup;
import java.io.IOException;

public class TestServer
{
    public static void main(String [] args) throws Exception
    {
	Setup configuration = new Setup("config.bmb");
	ClientSystemNetworkPerformer clientSystem = new ClientSystemNetworkPerformer(configuration);
	ServerPerformerInterface performer = new ServerPerformer(clientSystem);
	Server server = new Server(configuration, performer);
	System.out.println("Launching the server");
	server.begin();
	System.out.println("Server has launched his threads");	
    }
}
