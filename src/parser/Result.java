package parser;

import utils.ArrayManipulator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* fonctionne */
/**
 * This class represents a result of query. It contains the name, the size, and the
 * index of a file. A set of results is by the way represented by a ResultSet class.
 * Please refer to this protocol Gnutella v0.4 for more precision about the aim of 
 * this class.
 */
public class Result
{
    private int fileIndex;
    private int fileSize;
    private String fileName;
    private byte [] resultInByteArray;
    
    
    /**
     * Creates an element of a result set that follows a Query. This constructor must be
     * used before sending a result to another peer.
     * @param fileIndex A number, assigned by the responding host, which is used
     * to uniquely identify the file matching the corresponding query.
     * @param fileSize The size (in bytes) of the file whose index is fileIndex.
     * @param fileName The name of the file whose index is fileIndex.
     */
    public Result(int fileIndex, int fileSize, String fileName)
    {
	this.fileIndex = fileIndex;
	this.fileSize  = fileSize;
	this.fileName  = fileName;
	convertAttributesInArray();
    }
    
    /**
     * Creates an element of a result set that follows a Query. This constructor must be
     * used when receiving a packet from another peer. Please refer to the 
     * Gnutella protocol v0.4 to determine the content of resultArray.
     * @param resultArray the array from which this constructor will extract a Result.
     * This parameter must be in little-endian(and created by the other constructor).
     */
    public Result(byte [] resultArray)
    {
	byte[]fileArray = new byte[4];
	
	this.resultInByteArray = resultArray;
	
	ArrayManipulator.copyArray(fileArray, resultArray, 0);
	this.fileIndex = ArrayManipulator.byteArray2Int(fileArray);
	
	ArrayManipulator.copyArray(fileArray, resultArray, 4);
	this.fileSize = ArrayManipulator.byteArray2Int(fileArray);
	
	//-8: sizeof(fileIndex)-sizeof(fileSize); -1:the file name is a double null 
	//terminated byte buffer.
	byte[]fileNameArray = new byte[resultArray.length -8-1];
	ArrayManipulator.copyArray(fileNameArray, resultArray, 8);
	this.fileName = 
	    new String(ByteBuffer.wrap(fileNameArray).order(ByteOrder.LITTLE_ENDIAN).array());
    }
    
    
    /**
     * Converts the attributes of this class in a byte array, and store them in
     * this.resultInByteArray.
     */
    private void convertAttributesInArray()
    {
	byte [] fileNameArray = ByteBuffer.wrap(this.fileName.getBytes()).order(ByteOrder.LITTLE_ENDIAN).array();
	//+8: sizeof(fileIndex)+sizeof(fileSize); +2:the array is a double null 
	//terminated byte buffer.
	this.resultInByteArray = new byte[fileNameArray.length+8+2];
	ArrayManipulator.copyArray(this.resultInByteArray, ArrayManipulator.int2ByteArray(this.fileIndex), 0);
	ArrayManipulator.copyArray(this.resultInByteArray, ArrayManipulator.int2ByteArray(this.fileSize), 4);
	ArrayManipulator.copyArray(this.resultInByteArray, fileNameArray, 8);
	//the array is a double null terminated byte buffer.
	this.resultInByteArray[this.resultInByteArray.length-2]=0;
	this.resultInByteArray[this.resultInByteArray.length-1]=0;
    }
    
    
    /**
     * Returns this Result under the form of a byte array.
     * @return the result as a byte array.
     */
    public byte[] getResultInByteArray()
    { return this.resultInByteArray; }
    
    /**
     * Returns the size of the byte array that represents a Result.
     */
    public int getArraySize()
    { return this.resultInByteArray.length; }
    
    /**
     * Returns the result's file index
     */
    public int getFileIndex()
    { return this.fileIndex; }
    
    /**
     * Returns the result's file size
     */
    public int getFileSize()
    { return this.fileSize; }
    
    /**
     * Returns the result's file name
     */
    public String getFileName()
    { return this.fileName; }
}
