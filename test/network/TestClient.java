import network.*;


/**
 * Tests the client
 */
public class TestClient
{
    public static void main(String [] args)
    {
	if(args.length!=2) {
	    System.out.println("Usage: java TestClient <hote> <port>");
	    return;
	}
	ClientPerformerInterface performer = new ClientPerformer();
	Client client = new Client(1);
	client.startClient(args[0], Integer.parseInt(args[1]), performer);
    }
}
