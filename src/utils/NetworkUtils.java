package utils;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class defines some small utilities to read/write in 
 * TCP sockets.
 */
public class NetworkUtils
{
    /**
     * Writes the String <code>message</code> in the given socket. It doesn't
     * close the socket connection.
     * @param socket the socket in which the client must write.
     * @param message the message to be written.
     * @exception IOException if an error occured.
     */
    public static void write(Socket socket, String message)throws IOException
    {
	OutputStream out = socket.getOutputStream();
	out.write(message.getBytes());
    }
    
    /**
     * Writes <code>array</code> in the given socket. It doesn't
     * close the socket connection.
     * @param socket the socket in which the client must write.
     * @param array the array to be written.
     * @exception IOException if an error occured.
     */
    public static void write(Socket socket, byte[]array)throws IOException
    {
	OutputStream out = socket.getOutputStream();
	out.write(array);
    }
        
    
    /**
     * Reads in the given socket in order to extract from it a byte array.
     * It only reads at most the 1024 first bytes.
     * @param socket the socket in which the client must listen.
     * @return the byte array read, null if EOF was reached.
     * @exception IOException if an error occured.
     */
    public static byte [] read(Socket socket) throws IOException
    {
	InputStream in = socket.getInputStream();
	byte [] array = new byte[1024];
	try{
	    int nb_bytes_read = in.read(array);
	    if(nb_bytes_read<=0) return null;
	    byte [] returned_array = new byte[nb_bytes_read];
	    ArrayManipulator.copyArray(returned_array, array, 0);
	    return returned_array;
	}catch(java.net.SocketTimeoutException ste){}
	return null;
    }
    
    /**
     * Reads in the given socket in order to extract from it a String.
     * It only reads at most the 1024 first bytes of the input stream.
     * @param socket the socket in which the client must listen.
     * @param dummy a dummy parameter that isn't used.
     * @return the string read, null if EOF was reached.
     * @exception IOException if an error occured.
     */
    public static String read(Socket socket, int dummy) throws IOException
    {
	InputStream in = socket.getInputStream();
	byte [] array = new byte[1024];
	int nb_bytes_read = in.read(array);
	if(nb_bytes_read<=0) return null;
	byte [] returned_array = new byte[nb_bytes_read];
	ArrayManipulator.copyArray(returned_array, array, 0);
	return new String(returned_array);
    }
}
