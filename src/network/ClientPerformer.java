package network;

import java.net.Socket;
import java.io.IOException;
import utils.NetworkUtils;
import parser.TCPPingDescriptor;
import parser.PayloadDescriptor;
import parser.TCPPongDescriptor;
import parser.TCPDescriptorHeader;

public class ClientPerformer implements ClientPerformerInterface
{
    public void perform(Socket socket)
    {
	try{
	    System.out.println("Le client lance un bonjour dans la toile");
	    NetworkUtils.write(socket, "GNUTELLA CONNECT/0.4\n\n");
	    
	    System.out.println("Le client écoute...");
	    System.out.println("Le client percevrait-il quelque chose? "+NetworkUtils.read(socket, 0));
	    
	    System.out.println("Le client envoie un ping");
	    byte [] descriptor_id = {12, 4, 5,  8, 1, 6,  5, 7, 8,  6, 4, 0,  5, 88, 56, 4};
	    TCPPingDescriptor ping = new TCPPingDescriptor(descriptor_id, (byte)1, (byte)0);
	    NetworkUtils.write(socket, ping.getPingDescriptor());
	    
	    System.out.println("Le client écoute...");
	    byte [] array = NetworkUtils.read(socket);
	    
	    System.out.println("Le client a reçu quelque chose et vérifie..."+array.length);
	    byte [] descriptorId2 = TCPDescriptorHeader.getDescriptorID(array);
	    boolean cbon = true;
	    for(int i=0; i<descriptorId2.length; i++)
		if(descriptorId2[i]!=descriptor_id[i])
		    { cbon=false; break; }
	    System.out.println("Les descripteurs sont égaux: "+cbon);
	    System.out.println("payloadDescriptor: "+TCPDescriptorHeader.getPayloadDescriptor(array));
	    System.out.println("ttl: "+TCPDescriptorHeader.getTtl(array));
	    System.out.println("Hops: "+TCPDescriptorHeader.getHops(array));
	    System.out.println("payload length: "+TCPDescriptorHeader.getPayloadLength(array));
	    TCPPongDescriptor descr2 = new TCPPongDescriptor(array);
	    System.out.println("Port: "+descr2.getPort());
	    System.out.println("nb de fichiers partagés: "+descr2.getNumberSharedFiles());
	    System.out.println("nb d'octets partagés: "+descr2.getNumberSharedKo());
	}
	catch(IOException ioe)
	    { ioe.printStackTrace(); }
    }
}
