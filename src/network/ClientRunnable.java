package network;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;

public class ClientRunnable implements Runnable
{
    private ClientPerformerInterface performer;
    private int conf;
    private int port;
    private String host;
    
    public ClientRunnable(int conf, ClientPerformerInterface performer, String host, int port)
    {
	this.conf      = conf;
	this.performer = performer;
	this.port      = port;
	this.host      = host;
    }
    
    
    public void run()
    {
	Socket s = new Socket();
	try{
	    s.connect(new InetSocketAddress(this.host, this.port));
	    performer.perform(s);
	    s.close();
	}
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
}
