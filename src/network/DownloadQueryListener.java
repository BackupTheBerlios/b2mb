package network;

import java.util.EventListener;

/**
 * This interface should be implemented by all the client that down
 * A TERMINER
 */
public interface DownloadQueryListener extends EventListener
{
    public void downloadQuerySent();
}
