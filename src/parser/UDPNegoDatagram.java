package parser;

import java.security.InvalidParameterException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPNegoDatagram
{
    /**
     * Converts an int in a byte array in the little endian order.
     * @return the converted int
     */
    private static byte[] int2ByteArray(int value)
    {
	ByteBuffer bb = ByteBuffer.allocate(4);
	bb = (bb.putInt(value)).order(ByteOrder.LITTLE_ENDIAN);
	return bb.array();
    }
    
    /**
     * Converts a byte array in an int in the little endian order.
     * @return the int representing the array
     */
    private static int byteArray2Int(byte [] array)
    { return (ByteBuffer.wrap(array)).getInt(); }
    
    /**
     * Creates a new Nego datagram...
     * @param file_index the datagram's file index
     */
    public static byte[] createNegoDatagram(int file_index, short opcode, int delay, int width, int height)
	throws InvalidParameterException
    {
	//creation
	if(!Opcode.isNego(opcode)) throw new InvalidParameterException("opcode isn't a correct value");
	byte datagram[] = UDPDatagramHeader.createUDPDatagramHeader(file_index, opcode, 18);
	byte [] delay_array = int2ByteArray(delay);
	byte [] width_array = int2ByteArray(width);
	byte [] height_array = int2ByteArray(height);
	int i;
	//initialisation
	for(i=0; i<4; i++)
	    datagram[i+6] = delay_array[i];
	for(i=0; i<4; i++)
	    datagram[i+10] = width_array[i];
	for(i=0; i<4; i++)
	    datagram[i+14] = height_array[i];
	return datagram;
    }
    
    public static int getDelay(byte [] datagram)
    {
	byte delay[] = { datagram[UDPDatagramHeader.getHeaderSize()], 
			 datagram[UDPDatagramHeader.getHeaderSize()+1], 
			 datagram[UDPDatagramHeader.getHeaderSize()+2], 
			 datagram[UDPDatagramHeader.getHeaderSize()+3] };
	return byteArray2Int(delay);
    }
    
    public static int getWidth(byte [] datagram)
    {
	byte width[] = { datagram[UDPDatagramHeader.getHeaderSize()+4],
			 datagram[UDPDatagramHeader.getHeaderSize()+5], 
			 datagram[UDPDatagramHeader.getHeaderSize()+6], 
			 datagram[UDPDatagramHeader.getHeaderSize()+7] };
	return byteArray2Int(width);
    }
        
    public static int getHeight(byte [] datagram)
    {
	byte height[] = { datagram[UDPDatagramHeader.getHeaderSize()+8],
			  datagram[UDPDatagramHeader.getHeaderSize()+9], 
			  datagram[UDPDatagramHeader.getHeaderSize()+10], 
			  datagram[UDPDatagramHeader.getHeaderSize()+11] };
	return byteArray2Int(height);
    } 
}
