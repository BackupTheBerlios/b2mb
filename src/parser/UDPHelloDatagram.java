package parser;

public class UDPHelloDatagram implements UDPDatagram
{
    /**
     * Creates a new Hello datagram. This datagram will be the first message sent 
     * by the client to the remote servent.
     * @param file_index the datagram's file index
     */
    public static byte[] createNewHelloDatagram(int file_index)
    { return UDPDatagramHeader.createUDPDatagramHeader(file_index, Opcode.HELLO); }
}
