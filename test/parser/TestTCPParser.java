import utils.ArrayManipulator;
import java.util.ListIterator;
import parser.*;

public class TestTCPParser
{
    public static void verifieNullite(Object o)
    {
	if(o==null)
	    System.out.println("null");
	else
	    System.out.println("pas null");
    }
    
    public static void main(String [] args)
    {
	System.out.println("<<<< Testing TCPPingDescriptor & TCPDescriptorHeader>>>>");
	byte [] descriptorId = {12, 15, 12,  3, 15, 4,  2, 1, 3,  1, 5, 8,  1, 5, 6, 12};
	TCPPingDescriptor descr = new TCPPingDescriptor(descriptorId, PayloadDescriptor.PING,
							(byte)5, (byte)3, 0);
	byte [] ping = descr.getPingDescriptor();
	byte [] descriptorId2 = TCPDescriptorHeader.getDescriptorID(ping);
	boolean cbon = true;
	for(int i=0; i<descriptorId2.length; i++)
	    if(descriptorId2[i]!=descriptorId[i])
		{ cbon=false; break; }
	System.out.println("Les descripteurs sont égaux: "+cbon);
	System.out.println("payloadDescriptor: "+TCPDescriptorHeader.getPayloadDescriptor(ping));
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(ping));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(ping));
	System.out.println("payload length: "+TCPDescriptorHeader.getPayloadLength(ping));
	TCPDescriptorHeader.setTtl(ping, (byte)12);
	TCPDescriptorHeader.setHops(ping, (byte)4);
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(ping));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(ping));
	
	//---------------------------------------------------------------\\
	
	System.out.println("\n<<<< Testing TCPPongDescriptor >>>>");
	byte[]ipaddress = {10, 2, 5, 3};
	TCPPongDescriptor descr2 = new TCPPongDescriptor(descriptorId, PayloadDescriptor.PONG,
							 (byte)6, (byte)8, (short)15, ipaddress,
							 500, 2000);
	byte[] pong = descr2.getTCPPongDescriptor();
	descriptorId2 = TCPDescriptorHeader.getDescriptorID(pong);
	cbon = true;
	for(int i=0; i<descriptorId2.length; i++)
	    if(descriptorId2[i]!=descriptorId[i])
		{ cbon=false; break; }
	System.out.println("Les descripteurs sont égaux: "+cbon);
	System.out.println("payloadDescriptor: "+TCPDescriptorHeader.getPayloadDescriptor(pong));
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(pong));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(pong));
	System.out.println("payload length: "+TCPDescriptorHeader.getPayloadLength(pong));
	TCPDescriptorHeader.setTtl(pong, (byte)12);
	TCPDescriptorHeader.setHops(pong, (byte)4);
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(pong));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(pong));
	System.out.println("Port: "+descr2.getPort());
	byte [] ipaddress2 = descr2.getIPAddress();
	cbon=true;
	for(int i=0; i<ipaddress2.length; i++)
	    if(ipaddress2[i]!=ipaddress[i])
		{ cbon=false; break; }
	System.out.println("Les adresses IP correspondent: "+cbon);
	System.out.println("nb de fichiers partagés: "+descr2.getNumberSharedFiles());
	System.out.println("nb d'octets partagés: "+descr2.getNumberSharedKo());
	
	//---------------------------------------------------------------\\
	
	System.out.println("\n<<<< Testing TCPQueryDescriptor >>>>");
	TCPQueryDescriptor tcpqd = new TCPQueryDescriptor(descriptorId, PayloadDescriptor.QUERY,
							  (byte)6, (byte)8, (short)200, "blablabla");
	pong = tcpqd.getTCPQueryDescriptor();
	descriptorId2 = TCPDescriptorHeader.getDescriptorID(pong);
	cbon = true;
	for(int i=0; i<descriptorId2.length; i++)
	    if(descriptorId2[i]!=descriptorId[i])
		{ cbon=false; break; }
	System.out.println("Les descripteurs sont égaux: "+cbon);
	System.out.println("payloadDescriptor: "+TCPDescriptorHeader.getPayloadDescriptor(pong)+"\t"+PayloadDescriptor.QUERY);
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(pong));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(pong));
	System.out.println("payload length: "+TCPDescriptorHeader.getPayloadLength(pong));
	TCPDescriptorHeader.setTtl(pong, (byte)12);
	TCPDescriptorHeader.setHops(pong, (byte)4);
	System.out.println("ttl: "+TCPDescriptorHeader.getTtl(pong));
	System.out.println("Hops: "+TCPDescriptorHeader.getHops(pong));
	
	System.out.println("Min speed: "+tcpqd.getMinSpeed());
	System.out.println("search criteria: "+tcpqd.getSearchCriteria());
	
	//---------------------------------------------------------------\\
	
	System.out.println("\n<<<< Testing Result >>>>");
	Result r = new Result(1235, 84654, "/home/bigbo$$/file.mp3");
	System.out.println("file index: "+r.getFileIndex());
	System.out.println("file size: "+r.getFileSize());
	System.out.println("file name: "+r.getFileName());
	Result r2 = new Result(r.getResultInByteArray());
	System.out.println("file index: "+r2.getFileIndex());
	System.out.println("file size: "+r2.getFileSize());
	System.out.println("file name: "+r2.getFileName());
	
	//---------------------------------------------------------------\\
	
	System.out.println("\n<<<< Testing ResultSet >>>>");
	ResultSet rs = new ResultSet();
	rs.addResult(r);
	rs.addResult(r2);
	rs.addResult(new Result(15, 854, "/home/file.mp4"));
	ListIterator it = rs.getListIterator();
	while(it.hasNext())
	    {
		r = (Result)it.next();
		System.out.print("index: "+r.getFileIndex()+"\tsize: "+r.getFileSize()+"\tname: "+r.getFileName());
		System.out.println("\tarray size: "+r.getArraySize());
	    }
	System.out.println("2nd constructor:");
	byte [] resultSetInArray = rs.resultSet2ByteArray();
	System.out.println("resultSetInArray.length: "+resultSetInArray.length);
	ResultSet rs2 = new ResultSet(resultSetInArray);
	it = rs2.getListIterator();
	while(it.hasNext())
	    {
		r = (Result)it.next();
		System.out.print("index: "+r.getFileIndex()+"\tsize: "+r.getFileSize()+"\tname: "+r.getFileName());
		System.out.println("\tarray size: "+r.getArraySize());
	    }
	
	//---------------------------------------------------------------\\
	
	System.out.println("\n<<<< Testing QueryHit >>>>");
	byte [] serventId = {15, 6, 8,  4, 5, 8,  8, 6, 4,  5, 4, 1,  8, 9, 4,  2, 13};
	rs.addResult(new Result(12, 45, "blabalbla.txt"));
	TCPQueryHitDescriptor qhd = new TCPQueryHitDescriptor(descriptorId, PayloadDescriptor.QUERYHIT,
							      (byte)6, (byte)8, 
							      (byte)100, (short)8080, ipaddress, 200, rs, serventId);
	System.out.println("nb of hits: "+qhd.getNbOfHits());
	System.out.println("port: "+qhd.getPort());
	ipaddress2 = qhd.getIPAddress();
	cbon = true;
	for(int i=0; i<ipaddress.length; i++)
	    if(ipaddress[i]!=ipaddress2[i])
		{ cbon=false; break; }
	System.out.println("Les adresses IP correspondent: "+cbon);
	System.out.println("Speed: "+qhd.getSpeed());
	it = qhd.getResultSetIterator();
	while(it.hasNext())
	    {
		r = (Result)it.next();
		System.out.print("index: "+r.getFileIndex()+"\tsize: "+r.getFileSize()+"\tname: "+r.getFileName());
		System.out.println("\tarray size: "+r.getArraySize());
	    }
	byte[] serventId2 = qhd.getServentIdentifier();
	cbon = true;
	for(int i=0; i<serventId.length; i++)
	    if(serventId[i]!=serventId2[i])
		{ cbon=false; break; }
	System.out.println("Les identifiants des servents correspondent: "+cbon);
    }
}
