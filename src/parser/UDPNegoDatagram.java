package parser;

import utils.ArrayManipulator;

import java.security.InvalidParameterException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPNegoDatagram
{    
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
	byte [] delay_array = ArrayManipulator.int2ByteArray(delay);
	byte [] width_array = ArrayManipulator.int2ByteArray(width);
	byte [] height_array = ArrayManipulator.int2ByteArray(height);
	int i;
	int shift = UDPDatagramHeader.getHeaderSize();
	//initialisation
	ArrayManipulator.copyArrayAtEnd(datagram, delay_array, shift);
	shift+=4;
	ArrayManipulator.copyArrayAtEnd(datagram, width_array, shift);
	shift+=4;
	ArrayManipulator.copyArrayAtEnd(datagram, height_array, shift);
	return datagram;
    }
    
    public static int getDelay(byte [] datagram)
    {
	byte delay[] = { datagram[UDPDatagramHeader.getHeaderSize()], 
			 datagram[UDPDatagramHeader.getHeaderSize()+1], 
			 datagram[UDPDatagramHeader.getHeaderSize()+2], 
			 datagram[UDPDatagramHeader.getHeaderSize()+3] };
	return ArrayManipulator.byteArray2Int(delay);
    }
    
    public static int getWidth(byte [] datagram)
    {
	byte width[] = { datagram[UDPDatagramHeader.getHeaderSize()+4],
			 datagram[UDPDatagramHeader.getHeaderSize()+5], 
			 datagram[UDPDatagramHeader.getHeaderSize()+6], 
			 datagram[UDPDatagramHeader.getHeaderSize()+7] };
	return ArrayManipulator.byteArray2Int(width);
    }
        
    public static int getHeight(byte [] datagram)
    {
	byte height[] = { datagram[UDPDatagramHeader.getHeaderSize()+8],
			  datagram[UDPDatagramHeader.getHeaderSize()+9], 
			  datagram[UDPDatagramHeader.getHeaderSize()+10], 
			  datagram[UDPDatagramHeader.getHeaderSize()+11] };
	return ArrayManipulator.byteArray2Int(height);
    } 
}




