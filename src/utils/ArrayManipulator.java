package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ArrayManipulator
{
    /**
     * Converts an short in a byte array in the little endian order.
     * En attendant la classe Util !-)
     * @return the converted short
     */
    public static byte [] short2ByteArray(short value)
    {
	ByteBuffer bb = ByteBuffer.allocate(4);
	bb = (bb.putShort(value)).order(ByteOrder.LITTLE_ENDIAN);
	return bb.array();
    }
        
    /**
     * Converts a byte array in an short in the little endian order.
     * @return the short representing the array
     */
    public static short byteArray2Short(byte [] array)
    { return (ByteBuffer.wrap(array)).getShort(); }
    
    
    /**
     * Converts an int in a byte array in the little endian order.
     * En attendant la classe Util !-)
     * @return the converted int
     */
    public static byte[] int2ByteArray(int value)
    {
	ByteBuffer bb = ByteBuffer.allocate(4);
	bb = (bb.putInt(value)).order(ByteOrder.LITTLE_ENDIAN);
	return bb.array();
    }
        
    /**
     * Converts a byte array in an int in the little endian order.
     * @return the int representing the array
     */
    public static int byteArray2Int(byte [] array)
    { return (ByteBuffer.wrap(array)).getInt(); }
    
    
    /**
     * Copies the content of src in dest. If dest's length is greater than src's,
     * then the elements in dest are shifted. Otherwise, src's elements are shifted.
     */
    public static void copyArray(byte[]dest, byte[]src, int shift)
    {
	int i;
	if(dest.length>src.length)
	    for(i=0; i<src.length; i++)
		dest[shift+i] = src[i];
	else
	    for(i=0; i<dest.length; i++)
		dest[i] = src[shift+i];	    
    }    
}
