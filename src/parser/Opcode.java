package parser;

public class Opcode
{
    /** Corresponds to the hello opcode. */
    public static final short HELLO     = (short)0xFFFF;
    
    /** Corresponds to the nego opcode, which indicates the basic fragmentation mode */
    public static final short NEGO_BASE = (short)0x0001;
    
    /** Corresponds to the download opcode. */
    public static final short DOWNLOAD  = (short)0xFFFE;
    
    /** Corresponds to the image opcode. */
    public static final short IMAGE     = (short)0xFFFD;
    
    
    /**
     * Tells if the given opcode is a nego opcode.
     * @return true if the given opcode is a nego opcode, false otherwise.
     */
    public static boolean isNego(short opcode)
    { return (opcode>=0x0001)&&(opcode<=0x09FF); }
}
