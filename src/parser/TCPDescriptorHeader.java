import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//package fr.umlv.b2mb.parser;

public class TCPDescriptorHeader
{
    /************* ATTRIBUTES *************/
    private byte [] descriptor_id;
    private byte payload_descriptor;
    private byte ttl;
    private byte hops;
    private byte [] payload_length;
    
    
    
    /************* CONSTRUCTORS *************/
    /**
     * Constructor
     * Parses an array of data in order to extract the descriptor header contained in the array.
     * This constructor is mainly destined to be used when the system receives a datagram.
     * @param data: byte array of data which contains a descriptor header.
     * <b>WARNING:</b> the bytes must be little-endian.
     */
    public TCPDescriptorHeader(byte [] data) throws IncorrectDescriptorHeaderException
    {
	/* Please refer to the Gnutella protocol for the description of a header */
	/* verifications */
	if(data.length < 23)throw new IncorrectDescriptorHeaderException("data length");
	if(!isPayloadDescriptorCorrect(data[16]))
	    throw new IncorrectDescriptorHeaderException("incorrect payload descriptor");
	/* allocation */
	this.descriptor_id  = new byte[16];
	this.payload_length = new byte[4];
	
	/* parsing */
	for(int i=0; i<16; i++)
	    this.descriptor_id[i] = data[i];	
	this.payload_descriptor = data[16];
	this.ttl                = data[17];
	this.hops               = data[18];
	this.payload_length[0]  = data[19];
	this.payload_length[1]  = data[20];
	this.payload_length[2]  = data[21];
	this.payload_length[3]  = data[22];
    }
    
    
    /**
     * Constructor
     
     * Builds a DescriptorHeader with the different elements given in parameter;
     * Please refer to the Gnutella v0.4 protocol for more details.
     
     * <b>WARNING:</b> descriptor_id and payload_length aren't copied. Only their references are.
     * <b>WARNING:</b> the bytes must be BIG-endian: 
     * this constructor is mainly destined to be used when the creation of a TCPDescriptorHeader is needed.
     
     * @param descriptor_id: A 16-byte string uniquely identifying the descriptor on the network
     * @param payload_descriptor: Ping, Pong, Push, Query, QueryHit
     * @param ttl: Time To Live. The number of times the descriptor will be forwarded by
     * Gnutella servents before it is removed from the network. Each servent will
     * decrement the TTL before passing it on to another servent. When the TTL
     * reaches 0, the descriptor will no longer be forwarded.
     * @param hops: The number of times the descriptor has been forwarded. As a descriptor is
     * passed from servent to servent, the TTL and Hops fields of the header must
     * satisfy the following condition:
     * <p>TTL(0) = TTL(i) + Hops(i)</p>
     * <p>Where TTL(i) and Hops(i) are the value of the TTL and Hops fields of the
     * header at the descriptor’s i-th hop, for i >= 0.</p>
     * @param payload_length: The length of the descriptor immediately following this header. The next
     * descriptor header is located exactly Payload_Length bytes from the end of
     * this header i.e. there are no gaps or pad bytes in the Gnutella data stream.
     */
    public TCPDescriptorHeader(byte [] descriptor_id, byte payload_descriptor, 
			       byte ttl, byte hops, byte [] payload_length) 
	throws IncorrectDescriptorHeaderException
    {
	/* Please refer to the Gnutella protocol for the description of a header */
	
	/* verifications */
	if(descriptor_id.length != 16 || payload_length.length != 4)
	    throw new IncorrectDescriptorHeaderException("data length");
	ByteBuffer bb = ByteBuffer.allocate(3);
	bb=(bb.put(payload_descriptor).put(ttl).put(hops)).order(ByteOrder.LITTLE_ENDIAN);	
	if(!isPayloadDescriptorCorrect(bb.get(0)))
	    throw new IncorrectDescriptorHeaderException("incorrect payload descriptor");
	
	ByteBuffer ba1 = (ByteBuffer.wrap(descriptor_id)).order(ByteOrder.LITTLE_ENDIAN);
	ByteBuffer ba2 = (ByteBuffer.wrap(payload_length)).order(ByteOrder.LITTLE_ENDIAN);
	
	
	/* copy */
	this.descriptor_id      = ba1.array();//descriptor_id;
	this.payload_length     = ba2.array();//payload_length;
	
	this.payload_descriptor = bb.get(0);//payload_descriptor;
	this.ttl                = bb.get(1);//ttl;
	this.hops               = bb.get(2);//hops;
    }
    
    
    /************* METHODS *************/
    /**
     * Checks whether the payload decriptor is correct or not.
     * @return false if the payload descriptor is incorrect, true otherwise.
     */
    private boolean isPayloadDescriptorCorrect(byte data)
    {
	if(data != PayloadDescriptor.PING  &&
	   data != PayloadDescriptor.PONG  &&
	   data != PayloadDescriptor.PUSH  &&
	   data != PayloadDescriptor.QUERY &&
	   data != PayloadDescriptor.QUERYHIT)
	    return false;
	return true;
    }
    
    /**
     * @return the descriptor's id
     */
    public byte[] getDescriptorId()
    {return this.descriptor_id;}
    
    /**
     * @return the payload descriptor
     */
    public byte getPayloadDescriptor()
    {return this.payload_descriptor;}
    
    /**
     * @return the descriptor's time to live
     */
    public byte getTTL()
    {return this.ttl;}
    
    /**
     * @return the number of hops
     */
    public byte getHops()
    {return this.hops;}
    
    /**
     * @return the payload's length as a byte[]
     */
    public byte[] getPayloadLength()
    {return this.payload_length;}
    
    /**
     * @return the payload's length as an int. a is a dummy parameter and isn't used.
     */
    public int getPayloadLength(int a)
    {
	ByteBuffer bb = ByteBuffer.wrap(this.payload_length);
	//payload_length is already little-endian. So we little-endian-ize it to become big-endian.
	bb.order(ByteOrder.LITTLE_ENDIAN);
	return bb.getInt();
    }
    
    /**
     * Returns the number of bytes constituting the TCPDescriptorHeader.
     */
    public static int getHeaderSize()
    { return 23; }
    
    /**
     * Converts this header into an array of bytes.
     * The returned byte array is in little-endian.
     * @param size_rest_data: the size of the rest of the data.
     * @return: the byte array that contains the converted header.
     */
    public byte [] header2ByteArray(int size_rest_data)
    {
	byte [] request = new byte[getHeaderSize()+size_rest_data];
	
	for(int i=0; i<16; i++)
	    request[i] = this.descriptor_id[i];
	request[16] = this.payload_descriptor;
	request[17] = this.ttl;
	request[18] = this.hops;
	request[19] = this.payload_length[0];
	request[20] = this.payload_length[1];
	request[21] = this.payload_length[2];
	request[22] = this.payload_length[3];
	
	return request;
    }
    
    public static void main(String [] args)
    {
	try{
	    System.err.println("<<<< first test >>>>Testing the 1st constructor");
	    byte [] data = {12, 13, 2,  1, 5, 9,  4, 5, 3,  4, 4, 4,  7, 8, 2, 1,//descriptor_id;
			    0,//payload_descriptor;
			    5,//ttl;
			    8,//hops;
			    12,0,0,0};//payload_length;
	    TCPDescriptorHeader tcpdh = new TCPDescriptorHeader(data);
	    System.err.println("payload length: "+tcpdh.getPayloadLength(0));
	    System.err.println("hops: "+tcpdh.getHops());
	    System.err.println("ttl: "+tcpdh.getTTL());
	    System.err.println("payload descriptor: "+tcpdh.getPayloadDescriptor());
	    System.err.println("payload descriptor: 0: is correct? "+tcpdh.isPayloadDescriptorCorrect((byte)0));
	    System.err.println("payload descriptor: 15: is correct? "+tcpdh.isPayloadDescriptorCorrect((byte)15));
	    
	    System.err.println("\n<<<< second test >>>>Testing the 2nd constructor");
	    //The following throws an exception
	    //TCPDescriptorHeader tcpdh2 = new TCPDescriptorHeader(data, (byte)0, (byte)0, (byte)0, data);
	    byte [] descr = {12, 13, 2,  1, 5, 9,  4, 5, 3,  4, 4, 4,  7, 8, 2, 1};
	    byte [] pl = {12, 0, 0, 0};
	    TCPDescriptorHeader tcpdh2 = new TCPDescriptorHeader(descr, (byte)0, (byte)5, (byte)8, pl);
	    System.err.println("payload length: "+tcpdh2.getPayloadLength(0));
	    System.err.println("hops: "+tcpdh2.getHops());
	    System.err.println("ttl: "+tcpdh2.getTTL());
	    System.err.println("payload descriptor: "+tcpdh2.getPayloadDescriptor());
	    System.err.println("payload descriptor: 0: is correct? "+tcpdh2.isPayloadDescriptorCorrect((byte)0));
	    System.err.println("payload descriptor: 15: is correct? "+tcpdh2.isPayloadDescriptorCorrect((byte)15));
	}catch(IncorrectDescriptorHeaderException idh)
	    {System.err.println("y a un bleme: "+idh.getMessage());}
    }
}
