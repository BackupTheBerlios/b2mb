package parser;

/**
 * This class represents a Ping descriptor as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
/*fonctionne*/
public class TCPPingDescriptor
{
    private byte[] pingDescriptor;
    
    
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
     * @param returnedArraySize the size that the returned array must have.
     * @return the descriptor header
     */
    public TCPPingDescriptor(byte [] descriptorID,
			     byte ttl, byte hops)
    {
	this.pingDescriptor = 
	    TCPDescriptorHeader.createTCPDescriptorHeader(descriptorID,
							  PayloadDescriptor.PING,
							  ttl, hops,
							  0);
    }
    
    public byte[] getPingDescriptor()
    { return this.pingDescriptor; }
}
