package parser;

import java.util.ArrayList;
import java.util.ListIterator;
import utils.ArrayManipulator;

/**
 * This class represents a Query Hit descriptor as described in the
 * Gnutella protocol v0.4. Please refer to this protocol for more
 * precision.
 */
public class TCPQueryHitDescriptor
{
    private byte [] queryHitDescriptor;
    
    
    
    /**
     * Creates a TCP query hit descriptor.
     * This constructor should be called before sending a query hit, when the creation of a
     * query hit descriptor is needed.
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
     
     * @param nbOfHits The number of query hits in the result set
     * @param port The port number on which the responding host can accept incoming connections.
     * @param ipAddress The IP address of the responding host. It is in big-endian format.
     * @param speed The speed (in kb/second) of the responding host.
     * @param resultSet A set of responses to the corresponding Query. It contains a set of ResultSet.
     * @param serventIdentifier A 16-byte string uniquely identifying the responding servent on the 
     * network. This is typically some function of the servent’s network address.
     */
    public TCPQueryHitDescriptor(byte [] descriptorID,
				 byte payloadDescriptor,
				 byte ttl, byte hops,
				 
				 byte nbOfHits, short port,
				 byte[]ipAddress, int speed,
				 ResultSet resultSet, 
				 byte[]serventIdentifier)
    {
	byte [] resultSetArray  = resultSet.resultSet2ByteArray();
	int payloadLength = resultSetArray.length + 7 + ipAddress.length + serventIdentifier.length;
	this.queryHitDescriptor = TCPDescriptorHeader.createTCPDescriptorHeader(descriptorID,
										payloadDescriptor,
										ttl, hops,
										payloadLength);
	int shift = TCPDescriptorHeader.getHeaderLength();
	this.queryHitDescriptor[shift] = nbOfHits; shift++;
	ArrayManipulator.copyArray(this.queryHitDescriptor, ArrayManipulator.short2ByteArray(port), shift); shift+=2;
	ArrayManipulator.copyArray(this.queryHitDescriptor, ipAddress, shift); shift+=ipAddress.length;
	ArrayManipulator.copyArray(this.queryHitDescriptor, ArrayManipulator.int2ByteArray(speed), shift); shift+=4;
	ArrayManipulator.copyArray(this.queryHitDescriptor, resultSetArray, shift); shift+=resultSetArray.length;
	ArrayManipulator.copyArray(this.queryHitDescriptor, serventIdentifier, shift);
    }
    
    /**
     * Creates a query hit descriptor with the given query hit which is as a byte array.
     * This constructor should be called when receiving a TCP datagram(after a query).
     * @param queryHitInArray a query hit descriptor under the form of a byte array. It 
     * must be in little-endian.
     */
    public TCPQueryHitDescriptor(byte [] queryHitInArray)
    { this.queryHitDescriptor = queryHitInArray; }
    
    
    /**
     * Returns the number of query hits in the result set.
     * @return the number of query hits in the result set.
     */
    public byte getNbOfHits()
    { return this.queryHitDescriptor[TCPDescriptorHeader.getHeaderLength()]; }
    
    /**
     * Returns the port number on which the responding host can accept incoming connections.
     * @return the port number.
     */
    public short getPort()
    {
	byte [] portArray = new byte[2];
	portArray[0] = this.queryHitDescriptor[TCPDescriptorHeader.getHeaderLength()+1];
	portArray[1] = this.queryHitDescriptor[TCPDescriptorHeader.getHeaderLength()+2];
	return ArrayManipulator.byteArray2Short(portArray);
    }
    
    /**
     * Returns the IP address of the responding host. The IP address is an IPv4 address
     * and is in big-endian format.
     * @return the IP address of the responding host.
     */
    public byte [] getIPAddress()
    {
	byte [] ipAddress = new byte[4];
	int shift = TCPDescriptorHeader.getHeaderLength()+3;
	ArrayManipulator.copyArray(ipAddress, this.queryHitDescriptor, shift);
	return ipAddress;
    }
    
    /**
     * Returns the speed (in ko/second) of the responding host.
     * @return the speed (in ko/second) of the responding host.
     */
    public int getSpeed()
    {
	byte [] speed = new byte[4];
	int shift = TCPDescriptorHeader.getHeaderLength()+7;
	ArrayManipulator.copyArray(speed, this.queryHitDescriptor, shift);
	return ArrayManipulator.byteArray2Int(speed);
    }
    
    /**
     * Returns an iterator on the ArrayList that contains all the results of the query.
     * @return an iterator on the results.
     */
    public ListIterator getResultSetIterator()
    {
	byte [] resultSetArray = new byte[this.queryHitDescriptor.length 
					  -28-TCPDescriptorHeader.getHeaderLength()];
	System.out.println("-->"+resultSetArray.length+"\tqhd: "+this.queryHitDescriptor.length);
	int shift = TCPDescriptorHeader.getHeaderLength()+11;
	ArrayManipulator.copyArray(resultSetArray, this.queryHitDescriptor, shift);
	return new ResultSet(resultSetArray).getListIterator();
    }
    
    /**
     * Returns the servent's identifier. The latter is a 16-byte string uniquely identifying
     * the responding servent on the network. This is typically some function of the servent’s
     * network address.
     * @return the servent's identifier.
     */
    public byte[] getServentIdentifier()
    {
	byte [] serventIdentifier = new byte[17];
	int shift = this.queryHitDescriptor.length-17;
	ArrayManipulator.copyArray(serventIdentifier, this.queryHitDescriptor, shift);
	return serventIdentifier;
    }    
    
    /**
     * Returns this descriptor as a byte array, in little endian format.
     * @return the converted QueryHit descriptor.
     */
    public byte [] getTCPQueryHitDescriptor()
    { return this.queryHitDescriptor; }
}
