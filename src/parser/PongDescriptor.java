public class PongDescriptor extends Descriptor
{
    private byte [] port;
    private byte [] ip_address;
    private byte [] nb_of_shared_files;
    private byte [] nb_of_kilobytes_shared;
    
    
    public PongDescriptor(byte [] data, TCPDescriptorHeader header) 
	throws IncorrectDescriptorException, IncorrectDescriptorHeaderException
    {
	int header_size = TCPDescriptorHeader.getHeaderSize();
	/* verifications */
	if(data.length != header_size+14)
	    throw new IncorrectDescriptorException("The size of the pong descriptor is incorrect");
	
	/* creation */
	if(header == null)
	    this.header = new TCPDescriptorHeader(data);
	else
	    this.header = header;
	this.port                   = new byte[2];
	this.ip_address             = new byte[4];
	this.nb_of_shared_files     = new byte[4];
	this.nb_of_kilobytes_shared = new byte[4];
	
	/* initialisation */
	this.port[0] = data[header_size];
	this.port[1] = data[header_size+1];
	int i;
	for(i=2; i<6; i++)
	    this.ip_address[i-2] = data[header_size+i];
	for(; i<10; i++)
	    this.nb_of_shared_files[i-6] = data[header_size+i];
	for(; i<14; i++)
	    this.nb_of_kilobytes_shared[i-10] = data[header_size+i];
    }
    
    
    public byte [] getPort()
    { return this.port; }
    
    public byte [] getIPAddress()
    { return this.ip_address; }
    
    public byte [] getNbSharedFiles()
    { return this.nb_of_shared_files; }
    
    public byte [] getNbKbShared()
    { return this.nb_of_kilobytes_shared; }
    
    public static int getPongDescriptorSize()
    { return 14; }
    
    public byte[] getRequest()
    {
	int shift = TCPDescriptorHeader.getHeaderSize();
	byte [] request = this.header.header2ByteArray(getPongDescriptorSize());
	request[0+shift] = this.port[0];
	request[1+shift] = this.port[1];
	request[2+shift] = this.ip_address[0];
	request[3+shift] = this.ip_address[1];
	request[4+shift] = this.ip_address[2];
	request[5+shift] = this.ip_address[3];
	request[6+shift] = this.nb_of_shared_files[0];
	request[7+shift] = this.nb_of_shared_files[1];
	request[8+shift] = this.nb_of_shared_files[2];
	request[9+shift] = this.nb_of_shared_files[3];
	request[10+shift] = this.nb_of_kilobytes_shared[0];
	request[11+shift] = this.nb_of_kilobytes_shared[1];
	request[12+shift] = this.nb_of_kilobytes_shared[2];
	request[13+shift] = this.nb_of_kilobytes_shared[3];
	
	return request;
    }
}
