package parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import utils.ArrayManipulator;

/**
 * This class represents a Query descriptor as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
/* fonctionne */
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
    public TCPQueryDescriptor(byte [] descriptorID,
			      byte ttl, byte hops,
			      
			      short minSpeed, String searchCriteria)
    {
	//conversion
	byte [] minSpeedArray       = ArrayManipulator.short2ByteArray(minSpeed);
	byte [] searchCriteriaArray
	    = ByteBuffer.wrap(searchCriteria.getBytes()).order(ByteOrder.LITTLE_ENDIAN).array();
	int shift                   = minSpeedArray.length+searchCriteriaArray.length;
	
	//creation of the array
	this.queryDescriptor = TCPDescriptorHeader.createTCPDescriptorHeader(descriptorID,
									     PayloadDescriptor.QUERY,
									     ttl, hops,shift);
	//initialisation
	shift = TCPDescriptorHeader.getHeaderLength();
	ArrayManipulator.copyArray(this.queryDescriptor, minSpeedArray, shift);
	shift+=minSpeedArray.length;
	ArrayManipulator.copyArray(this.queryDescriptor, searchCriteriaArray, shift);
    }
    
    /**
     * Creates a query descriptor with the given byte array. The latter is a query
     * descriptor that was converted in byte array before transmission.
     * This constructor should be called when receiving a TCP datagram, which is a 
     * query descriptor.
     * @param queryDescriptorAsArray a query descriptor as a byte array. Must be in 
     * little-endian form.
     */
    public TCPQueryDescriptor(byte [] queryDescriptorAsArray)
    { this.queryDescriptor = queryDescriptorAsArray; }
    
    
    /**
     * Returns the minimum speed (in kb/second) of servents that should
     * respond to this message. A servent receiving a Query descriptor with
     * a Minimum Speed field of n kb/s should only respond with a QueryHit if
     * it is able to communicate at a speed >= n kb/s.
     * @return the minimum speed (in kb/second) of servents that should
     * respond to this message.
     */
    public short getMinSpeed()
    {
	int shift = TCPDescriptorHeader.getHeaderLength();
	byte [] array = new byte[2];
	ArrayManipulator.copyArray(array, this.queryDescriptor, shift);
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
	byte [] array = new byte[this.queryDescriptor.length - shift];
	ArrayManipulator.copyArray(array, this.queryDescriptor, shift);
	return new String(ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).array());
    }
    
    /**
     * Returns this query descriptor as a byte array in Little Endian form.
     * @return a query descriptor.
     */
    public byte [] getTCPQueryDescriptor()
    { return this.queryDescriptor; }
}
