import network.*;
import configuration.Setup;

/**
 * Tests the client
 */
public class TestClient
{
    public static void main(String [] args) throws Exception
    {
	if(args.length!=2) {
	    System.out.println("Usage: java TestClient <hote> <port>");
	    return;
	}
	Setup configuration = new Setup("config.bmb");
	ClientPerformerInterface performer = new ClientPerformer();
	Client client = new Client(configuration);
	client.startClient(args[0], Integer.parseInt(args[1]), performer);
    }
}
