package parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import utils.ArrayManipulator;

/**
 * This class represents a Pong descriptor as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
public class TCPPongDescriptor
{
    private byte [] pongDescriptor;
    
    /**
     * Creates a TCP pong descriptor.
     * @param descriptor_id A 16-byte string uniquely identifying the descriptor on the network
     * @param payload_descriptor Ping, Pong, Push, Query, QueryHit(defined in PayloadDescriptor.java)
     * @param ttl Time To Live. The number of times the descriptor will be forwarded by
     * Gnutella servents before it is removed from the network. Each servent will
     * decrement the TTL before passing it on to another servent. When the TTL
     * reaches 0, the descriptor will no longer be forwarded.
     * @param hops The number of times the descriptor has been forwarded. As a descriptor is
     * passed from servent to servent, the TTL and Hops fields of the header must
     * satisfy the following condition:
     * <p>TTL(0) = TTL(i) + Hops(i)</p>
     * <p>Where TTL(i) and Hops(i) are the value of the TTL and Hops fields of the
     * header at the descriptor’s i-th hop, for i >= 0.</p>
     * @param payload_length The length of the descriptor immediately following this header. The next
     * descriptor header is located exactly Payload_Length bytes from the end of
     * this header i.e. there are no gaps or pad bytes in the Gnutella data stream.
     * @param port The port number on which the responding host can accept incoming connections.
     * @param ipAddress The IP address of the responding host.
     * @param nbSharedFiles Shared The number of files that the servent with the given
     * IP address and port is sharing on the network.
     * @param nbSharedKo The number of kilobytes of data that the servent with the
     * given IP address and port is sharing on the network.
     * @return the descriptor header
     */
    public TCPPongDescriptor(byte [] descriptorID,
			     byte payloadDescriptor,
			     byte ttl, byte hops,
			     int payloadLength,
			     
			     short port, byte [] ipAddress,
			     int nbSharedFiles, int nbSharedKo)
    {
	int shift = TCPDescriptorHeader.getHeaderLength();
	this.pongDescriptor = TCPDescriptorHeader.createTCPDescriptorHeader(descriptorID,
									    payloadDescriptor,
									    ttl, hops,
									    payloadLength,
									    shift+14);
	byte [] array = ArrayManipulator.short2ByteArray(port);
	int i;
	for(i=0; i<array.length; i++)
	    this.pongDescriptor[i+shift] = array[i];
	shift+=i;
	for(i=0; i<ipAddress.length; i++)
	    this.pongDescriptor[i+shift] = ipAddress[i];
	array = ArrayManipulator.int2ByteArray(nbSharedFiles);
	shift+=i;
	for(i=0; i<array.length; i++)
	    this.pongDescriptor[i+shift] = array[i];
	array = ArrayManipulator.int2ByteArray(nbSharedKo);
	shift+=i;
	for(i=0; i<array.length; i++)
	    this.pongDescriptor[i+shift] = array[i];
    }
    

    /**
     * Returns the pong descriptor under the form of a byte array.
     * @return the converted pong descriptor.
     */
    public byte[] getTCPPongDescriptor()
    { return this.pongDescriptor; }
    
    /**
     * Returns the port contained in this pong descriptor.
     * @return the port number on which the responding host can accept
     * incoming connections.
     */
    public short getPort()
    {
	int shift = TCPDescriptorHeader.getHeaderLength();
	byte [] array = new byte[2];
	for(int i=0; i<array.length; i++)
	    array[i] = this.pongDescriptor[i+shift];
	return ArrayManipulator.byteArray2Short(array);
    }

    /**
     * Returns the IP address contained in this pong descriptor.
     * @return the IP addres of the responding host.
     */
    public byte[] getIPAddress()
    {
	int shift = TCPDescriptorHeader.getHeaderLength()+2;
	byte [] array = new byte[4];
	for(int i=0; i<array.length; i++)
	    array[i] = this.pongDescriptor[i+shift];
	return array;
    }
    
    /**
     * Returns the number of shared files contained in this pong descriptor.
     * @return The number of files that the servent with the given IP address
     * and port is sharing on the network.
     */
    public int getNumberSharedFiles()
    {
	int shift = TCPDescriptorHeader.getHeaderLength()+6;
	byte [] array = new byte[4];
	for(int i=0; i<array.length; i++)
	    array[i] = this.pongDescriptor[i+shift];
	return ArrayManipulator.byteArray2Int(array);
    }

    /**
     * Returns the number of shared kilobytes contained in this pong descriptor.
     * @return the number of kilobytes of data that the servent with the given
     * IP address and port is sharing on the network.
     */
    public int getNumberSharedKo()
    {
	int shift = TCPDescriptorHeader.getHeaderLength()+10;
	byte [] array = new byte[4];
	for(int i=0; i<array.length; i++)
	    array[i] = this.pongDescriptor[i+shift];
	return ArrayManipulator.byteArray2Int(array);
    }
}
