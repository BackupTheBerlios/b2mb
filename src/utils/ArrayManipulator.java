package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ArrayManipulator
{
    /**
     * Converts an short in a byte array in the little endian order.
     * @return the converted short
     */
    public static byte [] short2ByteArray(short value)
    {
	ByteBuffer bb = ByteBuffer.allocate(2);
	bb = bb.order(ByteOrder.LITTLE_ENDIAN).putShort(value);
	return bb.array();
    }
        
    /**
     * Converts a byte array (ordered in the little endian order) in a short 
     * @return the short representing the array
     */
    public static short byteArray2Short(byte [] array)
    {
	byte[] arrayTmp = { array[1], array[0] };
	return (ByteBuffer.wrap(arrayTmp)).getShort();
    }
    
    
    /**
     * Converts an int in a byte array in the little endian order.
     * @return the converted int
     */
    public static byte[] int2ByteArray(int value)
    {
	ByteBuffer bb = ByteBuffer.allocate(4);
	bb = bb.order(ByteOrder.LITTLE_ENDIAN).putInt(value);
	return bb.array();
    }
        
    /**
     * Converts a byte array (ordered in the little endian order) in an int
     * @return the int representing the array
     */
    public static int byteArray2Int(byte [] array)
    {
	byte[] arrayTmp = { array[3], array[2], array[1], array[0] };
	return (ByteBuffer.wrap(arrayTmp)).getInt();
    }
    
    
    /**
     * Copies the content of src in dest. If dest's length is greater than src's,
     * then the elements in dest are shifted. Otherwise, src's elements are shifted.
     * @param dest the destination byte array
     * @param src the source byte array
     * @param shift 
     */
    public static void copyArray(byte[] dest, byte[] src, int shift)
    {
	int i;
	if(dest.length>src.length){
	    for(i=0; i<src.length; i++)
		dest[shift+i] = src[i];
	}
	else
	    for(i=0; i<dest.length; i++)
		dest[i] = src[shift+i];	    
    }

    /**
     * Copies the content of src at the end of dest, begin from the shift th element.
     * @param dest the destination byte array
     * @param src the source byte array
     * @param shift the position from where source will be added in destination
     */
    public static void copyArrayAtEnd(byte[] dest, byte[] src, int shift)
    {
	int i;
	for(i=0; i<src.length; i++)
	    dest[shift+i] = src[i];
    }    
}




