package parser;

import java.security.InvalidParameterException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPImageDatagram
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
     * Creates a new UDP image datagram. This datagram is the one that contains a fragment
     * of the sent image. Its size is limited to 1024.
     * @param file_index the datagram's file index.
     * @param image_number the number that identifies the image(not the fragment). Each image 
     * has its own image number.
     * @param image_size the total size of the original image(in bytes).
     * @param offset the offset of the image. The reference is the beginning of the image.
     * The offset is expressed in bytes. It enables the recomposition of the image.
     * @param fragment the corresponding image fragment.
     * @return an image datagram that corresponds to the given parameters.
     * @throws InvalidParameterException if the length of the fragment is greater than 
     * 1006(1006+18 = 1024 which is the maximum length for an image datagram).
     */
    public static byte[] createImageDatagram(int file_index, int image_number, 
					     int image_size, int offset, byte [] fragment)
	throws InvalidParameterException
    {
	//creation
	if(fragment.length>1006)throw new InvalidParameterException("fragment length too important");
	//1006+18 = 1024 = datagram max length
	byte [] datagram = UDPDatagramHeader.createUDPDatagramHeader(file_index, Opcode.IMAGE, 18+fragment.length);
	byte [] image_number_array = int2ByteArray(image_number);
	byte [] image_size_array = int2ByteArray(image_size);
	byte [] offset_array = int2ByteArray(offset);
	int i; int shift = UDPDatagramHeader.getHeaderSize();
	//initialisation
	for(i=0; i<4; i++)
	    datagram[i+shift] = image_number_array[i];
	shift+=i;
	for(i=0; i<4; i++)
	    datagram[i+shift] = image_size_array[i];
	shift+=i;
	for(i=0; i<4; i++)
	    datagram[i+shift] = offset_array[i];
	shift+=i;
	//copy the fragment in a temporary array to avoid messing up 'fragment'...
	byte [] fragment_array = new byte[fragment.length];
	for(i=0; i<fragment.length; i++)
	    fragment_array[i] = fragment[i];
	//...then order in the little-endian way.
	fragment_array = ByteBuffer.wrap(fragment_array).order(ByteOrder.LITTLE_ENDIAN).array();
	for(i=0; i<fragment.length; i++)
	    datagram[i+shift] = fragment_array[i];
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
    
    /**
     * Returns the size of the original image(in bytes).
     */
    public static int getImageSize(byte [] datagram)
    {
	byte image_size[] = { datagram[UDPDatagramHeader.getHeaderSize()+4], 
			      datagram[UDPDatagramHeader.getHeaderSize()+5], 
			      datagram[UDPDatagramHeader.getHeaderSize()+6], 
			      datagram[UDPDatagramHeader.getHeaderSize()+7] };
	return byteArray2Int(image_size);
    }
    
    /**
     * Returns the fragment's offset. It can be used to indicate the position
     * of the fragment in the whole image.
     */
    public static int getOffset(byte [] datagram)
    {
	byte offset[] = { datagram[UDPDatagramHeader.getHeaderSize()+8],
			  datagram[UDPDatagramHeader.getHeaderSize()+9], 
			  datagram[UDPDatagramHeader.getHeaderSize()+10], 
			  datagram[UDPDatagramHeader.getHeaderSize()+11] };
	return byteArray2Int(offset);
    }
    
    /**
     * Returns the datagram's fragment. Creates a new byte array and fill it with the
     * values of the fragment. This array's length is set to the fragment's length.
     */
    public static byte[] getFragment(byte [] datagram)
    {
	byte fragment[] = new byte[datagram.length-18];
	for(int i=0; i<fragment.length; i++)
	    fragment[i] = datagram[i+18];
	return fragment;
    }
    
    /**
     * Returns the size of the fragment(in bytes).
     */
    public static int getFragmentSize(byte [] datagram)
    { return datagram.length - 18; }
}



