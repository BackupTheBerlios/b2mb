package network;

import java.util.EventListener;
import java.net.Socket;
import parser.TCPPingDescriptor;
import java.io.IOException;

/**
 * This interface should be implemented by the client that processes
 * queries like GNUTELLA CONNECT or a PING.
 */
public interface NetworkQueryListener extends EventListener
{
    public void processQuery(byte [] descriptor, Socket socket);
    public boolean sendDemand2Connect(Socket socket) throws IOException;
    public void sendPing(Socket socket, TCPPingDescriptor ping) throws IOException;
}
