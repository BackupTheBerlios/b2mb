package network;

import java.net.Socket;

/**
 * Defines all the server's actions.
 */
public interface ServerPerformerInterface
{
    /**
     * Performs the server's actions.
     * Is destined to be used with the following classes: Server, ServerRunnable.
     */
    public void perform(Socket socket);
}
