package parser;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class represents all of the results of a Query.
 * Please refer to this protocol Gnutella v0.4 for more precision about the aim of 
 * this class.
 */
public class ResultSet
{
    private ArrayList resultSet;
    
    
    /**
     * Instantiates a result set. This result set contains all the result
     * of a search and will be incorporated in a Query descriptor.
     */
    public ResultSet()
    {
	this.resultSet = new ArrayList();
    }
    
    /**
     * Creates a result set from the content of a byte array. The result
     * was returned during a query hit.<br/>
     * The byte array contains a set of Result under the form of
     * byte arrays. For more precisions, please refer to the Gnutella
     * protocol v0.4.
     * @param array the array that contains the set of results.
     */
    public ResultSet(byte [] array)
    {
	int i, j;
	for(i=0; i<array.length;)
	    {
		for(j=0; (j<array.length-1)&&(array[j]!=0||array[j+1]!=0);j++);
		if(array[j]==0&&array[j+1]==0){
		    byte [] extract = new byte[j-i+1];
		    this.resultSet.add(new Result(extract));
		}
		i+=j;
	    }
    }
    
    
    /**
     * Returns an iterator on the result set.
     * @return the list iterator.
     */
    public ListIterator getListIterator()
    { return this.resultSet.listIterator(); }
    
    
    /**
     * Adds a result to the result list.
     * @param r the Result object to be added.
     */
    public void addResult(Result r)
    { this.resultSet.add(r); }
    
    
    /**
     * Converts a ResultSet into a byte array.
     */
    public byte[] resultSet2ByteArray()
    {
	ListIterator it = this.resultSet.listIterator();
	Result r;
	int finalArraySize=0;
	while(it.hasNext())
	    {
		r = (Result)it.next();
		finalArraySize+=r.getArraySize();
	    }
	byte [] returnedArray = new byte[finalArraySize];
	int shift = 0;
	while(it.hasNext())
	    {
		r = (Result)it.next();
		copyArray(returnedArray, r.getResultInByteArray(), shift);
		shift+=r.getArraySize();
	    }
	return returnedArray;
    }
}
