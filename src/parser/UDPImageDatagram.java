package parser;

import utils.ArrayManipulator;

import java.security.InvalidParameterException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPImageDatagram
{
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
	byte [] image_number_array = ArrayManipulator.int2ByteArray(image_number);
	byte [] image_size_array = ArrayManipulator.int2ByteArray(image_size);
	byte [] offset_array = ArrayManipulator.int2ByteArray(offset);
	int i; int shift = UDPDatagramHeader.getHeaderSize();
	//initialisation
	ArrayManipulator.copyArrayAtEnd(datagram, image_number_array, shift);
	shift+=4;
	ArrayManipulator.copyArrayAtEnd(datagram, image_size_array, shift);
	shift+=4;
	ArrayManipulator.copyArrayAtEnd(datagram, offset_array, shift);
	shift+=4;
	// copy the fragment in a temporary array (in the little endian order) to avoid messing up 'fragment'...
	byte [] fragment_array = new byte[fragment.length];
	int j = fragment.length - 1;
	for(i=0; i<fragment.length; i++)
	    fragment_array[j--] = fragment[i];
	ArrayManipulator.copyArrayAtEnd(datagram, fragment_array, shift);
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
	return ArrayManipulator.byteArray2Int(image_number);
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
	return ArrayManipulator.byteArray2Int(image_size);
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
	return ArrayManipulator.byteArray2Int(offset);
    }
    
    /**
     * Returns the datagram's fragment (in the big endian order). Creates a new byte array and fill
     * it with the values of the fragment. This array's length is set to the fragment's length.
     * @param the datagram
     * @param the max limit of the fragment in the datagram
     */
    public static byte[] getFragment(byte [] datagram, int max_limit)
    {
	byte fragment[] = new byte[max_limit-18];
	int j = fragment.length - 1; // Minus 1 because it's a position

	for(int i=18;i<max_limit;i++)
	    fragment[j--] = datagram[i];
	
	return fragment;
    }
    
    /**
     * Returns the size of the fragment(in bytes).
     * @param the datagram
     * @param the max limit of the fragment in the datagram
     */
    public static int getFragmentSize(byte [] datagram, int max_limit)
    { return max_limit - 18; }
}



