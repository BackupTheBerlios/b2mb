package parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import utils.ArrayManipulator;

/**
 * This class represents a Query descriptor as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
public class TCPQueryDescriptor
{
    private byte [] queryDescriptor;
    
    
    /**
     * Creates a TCP query descriptor.
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
			     
			     short minSpeed, String searchCriteria)
    {
	int shift = TCPDescriptorHeader.getHeaderLength();
	//conversion
	byte [] minSpeedArray = ArrayManipulator.short2ByteArray(minSpeed);
	byte [] searchCriteriaArray = searchCriteria.getBytes();
	shift+=minSpeedArray.length+searchCriteriaArray.length;
	
	//creation of the array
	this.queryDescriptor = TCPDescriptorHeader.createTCPDescriptorHeader(descriptorID,
									     payloadDescriptor,
									     ttl, hops,
									     payloadLength,
									     shift);
	//initialisation
	int i;
	shift = TCPDescriptorHeader.getHeaderLength();
	for(i=0; i<minSpeedArray.length; i++)
	    this.queryDescriptor[i+shift] = minSpeedArray[i];
	shift+=i;
	for(i=0; i<searchCriteriaArray.length; i++)
	    this.queryDescriptor[i+shift] = searchCriteriaArray[i];
    }
    

    /**
     * Returns the minimum speed (in kb/second) of servents that should
     * respond to this message. A servent receiving a Query descriptor with
     * a Minimum Speed field of n kb/s should only respond with a QueryHit if
     * it is able to communicate at a speed >= n kb/s.
     * @return the minimum speed (in kb/second) of servents that should
     * respond to this message.
     */
    public short getMinspeed()
    {
	int shift = TCPDescriptorHeader.getHeaderLength();
	byte [] array = new byte[2];
	for(int i=0; i<array.length; i++)
	    array[i] = this.queryDescriptor[i+shift];
	return ArrayManipulator.byteArray2Short(array);
    }

    /**
     * Returns the search criteria contained in this query descriptor.
     * @return the search criteria which is nul(i.e. 0x00) terminated search
     * string. The maximum length of this string is bounded by the Payload_Length
     * field of the descriptor header.
     */
    public String getSearchCriteria()
    {
	int shift = TCPDescriptorHeader.getHeaderLength()+2;
	byte [] array = new byte[this.queryDescriptor.length - 2];
	for(int i=0; i<array.length; i++)
	    array[i] = this.queryDescriptor[i+shift];
	return new String(array);
    }
}
