package network;

import java.net.Socket;

/**
 * Provides an interface for the client's behavioural class.
 */
public interface ClientPerformerInterface
{
    public void perform(Socket socket);
}
