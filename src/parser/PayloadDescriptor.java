package parser;

public class PayloadDescriptor
{
    public final static byte PING     = 0x00;
    public final static byte PONG     = 0x01;
    public final static byte PUSH     = 0x40;
    public final static byte QUERY    = (byte)0x80;
    public final static byte QUERYHIT = (byte)0x81;
}
