package parser;

import utils.ArrayManipulator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPDownloadDatagram
{
    /**
     * Creates a new UDP Download datagram. The returned datagram is destined to be sent
     * when the client is ready to download.
     * @param file_index the datagram's file index
     */
    public static byte[] createDownloadDatagram(int file_index, int image_number)
    {
	//creation
	byte datagram[] = UDPDatagramHeader.createUDPDatagramHeader(file_index, Opcode.DOWNLOAD, 10);
	byte [] image_number_array = ArrayManipulator.int2ByteArray(image_number);
	int i; int shift = UDPDatagramHeader.getHeaderSize();
	//initialisation
	ArrayManipulator.copyArrayAtEnd(datagram, image_number_array, shift);
	return datagram;
    }
    
    /**
     * Returns the datagram's image number (in the big endian order)
     */
    public static int getImageNumber(byte [] datagram)
    {
	int size = UDPDatagramHeader.getHeaderSize();
	byte image_number[] = { datagram[size], 
				datagram[size+1], 
				datagram[size+2], 
				datagram[size+3] };
	return ArrayManipulator.byteArray2Int(image_number);
    }
}
