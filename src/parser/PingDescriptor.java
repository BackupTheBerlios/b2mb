public class PingDescriptor extends Descriptor
{
    private TCPDescriptorHeader header;
    
    public PingDescriptor(byte [] data) throws IncorrectDescriptorHeaderException
    {
	this.header = new TCPDescriptorHeader(data);
    }
    
    
    public PingDescriptor(byte [] descriptor_id, byte payload_descriptor, 
			  byte ttl, byte hops, byte [] payload_length) 
	throws IncorrectDescriptorHeaderException
    {
	this.header = new TCPDescriptorHeader(descriptor_id, payload_descriptor, 
					      ttl, hops, payload_length);
    }
    
    public byte[] getRequest()
    {
	return this.header.header2ByteArray(0);
    }
}
