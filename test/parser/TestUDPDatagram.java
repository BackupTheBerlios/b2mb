import parser.*;
import utils.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestUDPDatagram
{
    public static void main(String[]args)
    {
	int file_index = 5;
	short opcode   = Opcode.NEGO_BASE;
	short opcode2 = Opcode.DOWNLOAD;
	int delay      = 500;
	int width      = 600;
	int height     = 300;
	
	byte [] nego = UDPNegoDatagram.createNegoDatagram(file_index, //file_index
							  opcode,     //opcode
							  delay,      //delay
							  width,      //width
							  height);    //height
	/*System.out.println("Expected output:");
	System.out.println("opcode: "+Opcode.NEGO_BASE);
	System.out.println("file_index: "+file_index);
	System.out.println("is nego: true");
	System.out.println("is hello: false");
	System.out.println("delay: "+delay);
	System.out.println("width: "+width);
	System.out.println("height: "+height);
	System.out.println("");
	
	System.out.println("<<<<Testing the Nego datagram and the datagram header>>>>");
	if(nego.length != 18)System.out.println("incorrect datagram length");
	System.out.println("opcode: "+((int)UDPDatagramHeader.getOpcode(nego)));
	System.out.println("file index: "+UDPDatagramHeader.getFileIndex(nego));
	System.out.println("is nego: "+UDPDatagramHeader.isNegoDatagram(nego));
	System.out.println("is hello: "+UDPDatagramHeader.isHelloDatagram(nego));
	System.out.println("delay: "+UDPNegoDatagram.getDelay(nego));
	System.out.println("width: "+UDPNegoDatagram.getWidth(nego));
	System.out.println("height: "+UDPNegoDatagram.getHeight(nego));
	
	System.out.println("\n<<<<Testing the download datagram>>>>");
	int image_number = 250;	
	byte [] download = UDPDownloadDatagram.createDownloadDatagram(file_index,
								      image_number);
	if(download.length != 10)System.out.println("incorrect datagram length");
	System.out.println("opcode: "+((int)UDPDatagramHeader.getOpcode(download))+"\t"+Opcode.DOWNLOAD);
	System.out.println("file index: "+UDPDatagramHeader.getFileIndex(download));
	System.out.println("is download: "+UDPDatagramHeader.isDownloadDatagram(download));
	System.out.println("is nego: "+UDPDatagramHeader.isNegoDatagram(download));
	System.out.println("image number: "+UDPDownloadDatagram.getImageNumber(download));
	
	System.out.println("\n<<<<Testing the image datagram>>>>");
	image_number = 250;	
	int image_size   = 280;	
	int offset       = 50;
	byte [] fragment = {2, 5};
	byte [] image = UDPImageDatagram.createImageDatagram(file_index,
							     image_number,
							     image_size,
							     offset,
							     fragment);
	if(image.length != 18+fragment.length)System.out.println("incorrect datagram length: "+image.length);
	System.out.println("opcode: "+((int)UDPDatagramHeader.getOpcode(image))+"\t"+Opcode.IMAGE);
	System.out.println("file index: "+UDPDatagramHeader.getFileIndex(image));
	System.out.println("is image: "+UDPDatagramHeader.isImageDatagram(image));
	System.out.println("is nego: "+UDPDatagramHeader.isNegoDatagram(image));
	System.out.println("image number: "+UDPImageDatagram.getImageNumber(image));
	System.out.println("image size: "+UDPImageDatagram.getImageSize(image));
	System.out.println("offset: "+UDPImageDatagram.getOffset(image));
	
	System.out.println("Contenu de image:");
	for(int i=0; i<image.length; i++)
	    System.out.println(image[i]);
	boolean res = true;
	for(int j = 0; j<fragment.length; j++){
	    System.out.println("image: "+image[j+18]+"\tfragment: "+fragment[j]);
	    if(image[18+j]!=fragment[j])
		{
		    res = false;
		    //System.out.println("image: "+image[j+18]+"\tfragment: "+fragment[j]);
		}
	}
	if(res)
	    System.out.println("fragment == image's fragment");
	else
	    System.out.println("fragment != image's fragment");
	
	image = UDPImageDatagram.createImageDatagram(file_index,
						     image_number,
						     image_size,
						     offset,
						     fragment);
	byte [] array_res = ByteBuffer.wrap(UDPImageDatagram.getFragment(image)).order(ByteOrder.LITTLE_ENDIAN).array();
	res=true;
	System.out.println("--------");
	for(int j = 0; j<fragment.length; j++){
	    System.out.println("array_res: "+array_res[j]+"\tfragment: "+fragment[j]);
	    if(array_res[j]!=fragment[j])
		res = false;
	}
	if(res)
	    System.out.println("fragment == image's fragment");
	else
	    System.out.println("fragment != image's fragment");
	*/
	// Bat's test
	// Attention, pour que le test marche, il faut modifier la visibilité de la méthode ci-dessous !!
	//byte[] downBArray = UDPDatagramHeader.createUDPDatagramHeader(file_index, opcode2);
	//System.out.println("opcode2 is a download datagram => "+UDPDatagramHeader.isDownloadDatagram(downBArray));
	//System.out.println("header is => "+UDPDatagramHeader.getFileIndex(downBArray));

	short tmp = Opcode.DOWNLOAD;
	System.out.println(tmp);
	byte[] tmpByte = ArrayManipulator.short2ByteArray(tmp);
	System.out.println("tmp => "+ArrayManipulator.byteArray2Short(tmpByte));

	int tmp2 = 18;
	System.out.println(tmp2);
	byte[] tmp2Byte = ArrayManipulator.int2ByteArray(tmp2);
	System.out.println("tmp2 => "+ArrayManipulator.byteArray2Int(tmp2Byte));

	byte[] tmpDown = UDPDownloadDatagram.createDownloadDatagram(file_index, 7866);
	ByteBuffer tmpDownBuffer = ByteBuffer.wrap(tmpDown);
	for( int i=0;i<tmpDownBuffer.limit();i++)
	    System.out.print(tmpDownBuffer.get(i));
	System.out.println();
	System.out.println("Image number => "+UDPDownloadDatagram.getImageNumber(tmpDown));

	int y = 8327;
	System.out.println("\nNumber "+y);
	byte[] yByte = ByteBuffer.allocate(4).putInt(y).array(); 
	for(int i=0;i<4;i++)
	    System.out.print(yByte[i]);
	System.out.println();
	System.out.println(ByteBuffer.wrap(yByte).order(ByteOrder.LITTLE_ENDIAN).getInt());
	
	byte[] tmpImage = UDPImageDatagram.createImageDatagram(5, 89, 500, 98, yByte);
	System.out.println("Image number =>"+UDPImageDatagram.getImageNumber(tmpImage));
	System.out.println("Image size =>"+UDPImageDatagram.getImageSize(tmpImage));
	System.out.println("Image offset =>"+UDPImageDatagram.getOffset(tmpImage));	
	System.out.println("Image fragment =>"+ByteBuffer.wrap(UDPImageDatagram.getFragment(tmpImage)).getInt());	
    }
}





