public class QueryHitDescriptor extends Descriptor
{
    private byte nb_hits;
    private byte [] port;
    private byte [] ip_address;
    private byte [] speed;
    private ArrayList result_set;
    private byte [] servent_id;
    
    
    public QueryHitDescriptor(byte [] data, TCPDescriptorHeader header) 
	throws IncorrectDescriptorException, IncorrectDescriptorHeaderException
    {
	int header_size = TCPDescriptorHeader.getHeaderSize();
	/* verifications */
	if(data.length != header_size+getQueryHitDescriptorSize())
	    throw new IncorrectDescriptorException("The size of the query descriptor is incorrect");
	
	/* creation */
	if(header == null)
	    this.header = new TCPDescriptorHeader(data);
	else
	    this.header = header;
	this.port       = new byte[2];
	this.ip_address = new byte[4];
	this.speed      = new byte[4];
	this.result_set = new ArrayList(data[header_size]);
	this.servent_id = new byte[17];
	
	/* initialisation */
	this.nb_hits = data[header_size];
	this.port[0] = data[header_size+1];
	this.port[1] = data[header_size+2];
	
	this.ip_address[0] = data[header_size+3];
	this.ip_address[1] = data[header_size+4];
	this.ip_address[2] = data[header_size+5];
	this.ip_address[3] = data[header_size+6];
	
	this.speed[0] = data[header_size+7];
	this.speed[1] = data[header_size+8];
	this.speed[2] = data[header_size+9];
	this.speed[3] = data[header_size+10];
	
	for(byte i=0; i<nb_hits; i++)
	    result_set.add(new ResultSet(...));
	
	int shift = getQueryHitDescriptorSize()-this.servent_id.length;
	int limit = getQueryHitDescriptorSize();
	for(int i=0; i<this.servent_id.length; i++)
	    this.servent_id[i] = data[shift+i];
    }
    
    
    public byte [] getNbHits()
    { return this.nb_hits; }
    
    public static int getQueryHitDescriptorSize()
    { return 0; }
    
    public byte[] getRequest() //INCORRECT
    {
	int shift = TCPDescriptorHeader.getHeaderSize();
	byte [] request = this.header.header2ByteArray(getPongDescriptorSize());
	
	request[0+shift] = this.min_speed[0];
	request[1+shift] = this.min_speed[1];
	
	int limit = getQueryHitDescriptorSize() - 2;
	
	for(int i=2+shift; i<limit; i++)
	    request[i] = this.search_criteria[i];
	
	return request;
    }
}
