package parser;

import java.security.InvalidParameterException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class is destined to manipulate UDP datagram headers. It uses the Opcode class
 * to determine the values of the opcodes. To create a new UDP datagram header, call 
 * createUDPDatagramHeader().
 */
public class UDPDatagramHeader
{
    private final static int header_size = 6;//The number of bytes that constitutes the UDP datagram header
    
    
    
    /**
     * Creates an UDP datagram header and returns the corresponding byte array.<br>
     * <b>WARNING:</b> The returned byte array is in little-endian.
     * @param file_index the datagram's file index.
     * @param opcode the datagram's opcode. Values of opcodes are given in the <code>Opcode</code> class.
     * @param array_size the size that the returned byte array must have.
     * @return a byte array which represents the datagram header.
     */
    protected static byte[] createUDPDatagramHeader(int file_index, short opcode, int array_size)
    {
	if(array_size<header_size)
	    throw new InvalidParameterException("array_size must be greater than "+header_size+": "+array_size);
	ByteBuffer bb = ByteBuffer.allocate(array_size);
	bb = bb.order(ByteOrder.LITTLE_ENDIAN).putInt(file_index).putShort(opcode);
	return bb.array();
    }
    
    /**
     * Creates the smallest UDP datagram header and returns the corresponding byte array.<br>
     * <b>WARNING:</b> The returned byte array is in little-endian.
     * @param file_index the datagram's file index.
     * @param opcode the datagram's opcode. Values of opcodes are given in the <code>Opcode</code> class.
     * @return a byte array which represents the datagram header.
     */
    protected static byte[] createUDPDatagramHeader(int file_index, short opcode)
    {
	ByteBuffer bb = ByteBuffer.allocate(header_size);
	bb = bb.order(ByteOrder.LITTLE_ENDIAN).putInt(file_index).putShort(opcode);
	return bb.array();
    }
    
    
    
    /**
     * Determines whether the given parameter is a hello datagram or not.
     * @param datagram the datagram to be analysed.
     * @return true if the datagram is a hello datagram, false otherwise.
     */
    public static boolean isHelloDatagram(byte [] datagram)
    {
	return Opcode.isHello(getOpcode(datagram));
    }
    
    /**
     * Determines whether the given parameter is a nego datagram or not.
     * @param datagram the datagram to be analysed.
     * @return true if the datagram is a nego datagram, false otherwise.
     */
    public static boolean isNegoDatagram(byte [] datagram)
    {
	return Opcode.isNego(getOpcode(datagram));
    }
    
    /**
     * Determines whether the given parameter is a download datagram or not.
     * @param datagram the datagram to be analysed.
     * @return true if the datagram is a download datagram, false otherwise.
     */
    public static boolean isDownloadDatagram(byte [] datagram)
    {
	return Opcode.isDownload(getOpcode(datagram));
    }
    
    /**
     * Determines whether the given parameter is a image datagram or not.
     * @param datagram the datagram to be analysed.
     * @return true if the datagram is a image datagram, false otherwise.
     */
    public static boolean isImageDatagram(byte [] datagram)
    {
	return Opcode.isImage(getOpcode(datagram));
    }
    
    
    
    /**
     * Returns the datagram's opcode value in big-endian, for more ease.
     * @param datagram the datagram to be analysed.
     * @return the datagram's opcode.
     */
    public static short getOpcode(byte [] datagram)
    {
	byte opcode[] = { datagram[header_size-1], datagram[header_size-2] };
	ByteBuffer bb = ByteBuffer.wrap(opcode);
	return bb.getShort();
    }
    
    /**
     * Returns the datagram's file index value in big-endian, for more ease.
     * @param datagram the datagram to be analysed.
     * @return the datagram's file index.
     */
    public static int getFileIndex(byte [] datagram)
    {
	byte opcode[] = { datagram[3], datagram[2], datagram[1], datagram[0] };
	ByteBuffer bb = ByteBuffer.wrap(opcode);
	return bb.getInt();
    }
    
    public static int getHeaderSize()
    { return header_size; }
}
