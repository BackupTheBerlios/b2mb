package parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPDownloadDatagram
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
     * Creates a new UDP Download datagram. The returned datagram is destined to be sent
     * when the client is ready to download.
     * @param file_index the datagram's file index
     */
    public static byte[] createDownloadDatagram(int file_index, int image_number)
    {
	//creation
	byte datagram[] = UDPDatagramHeader.createUDPDatagramHeader(file_index, Opcode.DOWNLOAD, 10);
	byte [] image_number_array = int2ByteArray(image_number);
	int i; int shift = UDPDatagramHeader.getHeaderSize();
	//initialisation
	for(i=0; i<4; i++)
	    datagram[i+shift] = image_number_array[i];
	return datagram;
    }
    
    /**
     * Returns the datagram's image number.
     */
    public static int getImageNumber(byte [] datagram)
    {
	byte image_number[] = { datagram[UDPDatagramHeader.getHeaderSize()], 
				datagram[UDPDatagramHeader.getHeaderSize()+1], 
				datagram[UDPDatagramHeader.getHeaderSize()+2], 
				datagram[UDPDatagramHeader.getHeaderSize()+3] };
	return byteArray2Int(image_number);
    }
}
