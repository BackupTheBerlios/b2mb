public abstract class Descriptor
{
    TCPDescriptorHeader header;
    
    abstract byte [] getRequest();
}
