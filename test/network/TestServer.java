import network.*;
import java.io.IOException;

public class TestServer
{
    public static void main(String [] args) throws IOException
    {
	ServerPerformerInterface performer = new ServerPerformer();
	Server server = new Server(10, performer);
	System.out.println("Launching the server");
	server.begin();
	System.out.println("Server is finished");
    }
}
