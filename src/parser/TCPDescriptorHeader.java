package parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import utils.ArrayManipulator;

/**
 * This class represents a descriptor header as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
public class TCPDescriptorHeader
{
    /**
     * Creates a TCP descriptor header.
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
     * @return the descriptor header
     */
    protected static byte[] createTCPDescriptorHeader(byte [] descriptorID,
						      byte payloadDescriptor,
						      byte ttl, byte hops,
						      int payloadLength)
    {
	int i;
	byte [] returnedArray = new byte[23+payloadLength];
	byte [] array = ByteBuffer.wrap(descriptorID).order(ByteOrder.LITTLE_ENDIAN).array();
	
	ArrayManipulator.copyArray(returnedArray, array, 0);
	
	returnedArray[16] = payloadDescriptor;
	returnedArray[17] = ttl;
	returnedArray[18] = hops;
	
	array = ArrayManipulator.int2ByteArray(payloadLength);
	ArrayManipulator.copyArray(returnedArray, array, 19);
	
	return returnedArray;
    }
    
    
    /**
     * Parses the given byte array to extract the descriptor's id.
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @return the descriptor ID in bigEndian
     */
    public static byte [] getDescriptorID(byte [] data)
    {
	byte [] descriptorId = new byte [16];
	ArrayManipulator.copyArray(descriptorId, data, 0);
	return ByteBuffer.wrap(descriptorId).order(ByteOrder.LITTLE_ENDIAN).array();
    }
    
    /**
     * Parses the given byte array to extract the descriptor's payload descriptor(PING,
     * PONG, QUERY, QUERYHIT, ... defined in PayloadDescriptor.java).
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @return the descriptor payload descriptor
     */
    public static byte getPayloadDescriptor(byte [] data)
    {
	return data[16];
    }
    
    /**
     * Parses the given byte array to extract the descriptor's TTL(Time To Live).
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @return the descriptor Time To Live
     */
    public static byte getTtl(byte [] data)
    {
	return data[17];
    }
    
    /**
     * Parses the given byte array to extract the descriptor's hops(The number of
     * times the descriptor has been forwarded).
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @return the descriptor Time To Live
     */
    public static byte getHops(byte [] data)
    {
	return data[18];
    }
        
    /**
     * Parses the given byte array to extract the descriptor's payload length.
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @return the descriptor payload length
     */
    public static int getPayloadLength(byte [] data)
    {
	byte [] payloadLengthArray = new byte[4];
	ArrayManipulator.copyArray(payloadLengthArray, data, 19);
	return ArrayManipulator.byteArray2Int(payloadLengthArray);
    }
    
    /**
     * Returns the header's length.
     * This method is here in case the TCP descriptor header's size varies.
     * @return the header's length.
     */
    public static int getHeaderLength()
    { return 23; }
    
    
    /**
     * Sets the TTL value
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @param newTtl the new ttl value. <b>WARNING:</b> The newTtl value isn't checked.
     * If it's negative, newTtl becomes equal to 0.
     */
    public static void setTtl(byte [] data, byte newTtl)
    {
	data[17] = newTtl;
    }
    
    /**
     * Sets the HOPS value
     * @param data the array to be parsed. <b>WARNING:</b> Must be in little-endian.
     * @param newHops the new hops value. <b>WARNING:</b> The newHops value isn't checked.
     * If it's negative, newHops becomes equal to 0.
     */
    public static void setHops(byte [] data, byte newHops)
    {
	data[18] = newHops;
    }
}
