public class QueryDescriptor extends Descriptor
{
    private TCPDescriptorHeader header;
    private byte [] min_speed;
    private byte [] search_criteria;
    
    /**
     * Creates a QueryDescriptor.
     * @param data: the data in little-endian form. It contains what is needed to create a TCPDescriptorHeader.
     * @param header: the header's descriptor. Can be null. In that case, a TCPDescriptorHeader is created with data.
     */
    public QueryDescriptor(byte [] data, TCPDescriptorHeader header) 
	throws IncorrectDescriptorException, IncorrectDescriptorHeaderException
    {
	int header_size = TCPDescriptorHeader.getHeaderSize();
	/* verifications */
	if(data.length <= header_size+3)//3 = 2 for the min speed + 1 for the search criteria.
	    throw new IncorrectDescriptorException("The size of the query descriptor is incorrect");
	
	/* creation */
	if(header == null)
	    this.header = new TCPDescriptorHeader(data);
	else
	    this.header = header;
	this.min_speed       = new byte[2];
	this.search_criteria = new byte[4];
	
	/* initialisation */
	this.min_speed[0] = data[header_size];
	this.min_speed[1] = data[header_size+1];
	int i; int limit = getQueryDescriptorSize()-2;
	for(i=2; i<limit; i++)
	    this.search_criteria[i-2] = data[header_size+i];
    }
    
    
    public byte [] getMinSpeed()
    { return this.min_speed; }
    
    /**
     * Returns the minimum speed at which the data must be delivered.
     * The minimum speed is returned as a short.
     * The parameter is a dummy parameter, and isn't taken into account.
     */
    public short getMinSpeed(int a)
    {
	ByteBuffer bb = ByteBuffer.wrap(this.min_speed);
	//payload_length is already little-endian. So we little-endian-ize it to become big-endian.
	bb.order(ByteOrder.LITTLE_ENDIAN);
	return bb.getInt();
    }
    
    public byte [] getSearchCriteria()
    { return this.search_criteria; }
    
    public int getQueryDescriptorSize()
    { return TCPDescriptorHeader.getHeaderSize()+this.min_speed.length+this.search_criteria.length; }
    
    public byte[] getRequest()
    {
	int shift = TCPDescriptorHeader.getHeaderSize();
	byte [] request = this.header.header2ByteArray(getPongDescriptorSize());
	
	request[0+shift] = this.min_speed[0];
	request[1+shift] = this.min_speed[1];
	
	int limit = getQueryDescriptorSize() - 2;
	
	for(int i=2+shift; i<limit; i++)
	    request[i] = this.search_criteria[i];
	
	return request;
    }
}
