package network;

import java.util.EventListener;
import java.net.Socket;

/**
 * This interface should be implemented by the client that processes
 * queries like GNUTELLA CONNECT or a PING.
 */
public interface NetworkQueryListener extends EventListener
{
    public void processQuery(byte [] descriptor, Socket socket);
    public void setOK(boolean val);
    public boolean getOK();
}
