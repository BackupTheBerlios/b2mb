import utils.ArrayManipulator;
import parser.*;

public class TestTCPParser
{
    public static void main(String [] args)
    {
	System.out.println("<<<< Testing TCPPingDescriptor & TCPDescriptorHeader>>>>");
	byte [] descriptorId = {12, 15, 12,  3, 15, 4,  2, 1, 3,  1, 5, 8,  1, 5, 6, 12};
	TCPPingDescriptor descr = new TCPPingDescriptor(descriptorId, PayloadDescriptor.PING,
							(byte)5, (byte)3, 0);
	byte [] ping = descr.getPingDescriptor();
	byte [] descriptorId2 = TCPDescriptorHeader.getDescriptorID(ping);
	boolean cbon = true;
	for(int i=0; i<descriptorId2.length; i++)
	    if(descriptorId2[i]!=descriptorId[i])
		{ cbon=false; break; }
	System.out.println("Les descripteurs sont égaux: "+cbon);
	System.out.println("payloadDescriptor: "+TCPDescriptorHeader.getPayloadDescriptor(ping));
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(ping));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(ping));
	System.out.println("payload length: "+TCPDescriptorHeader.getPayloadLength(ping));
	TCPDescriptorHeader.setTtl(ping, (byte)12);
	TCPDescriptorHeader.setHops(ping, (byte)4);
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(ping));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(ping));
    }
}
