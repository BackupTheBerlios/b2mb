import java.net.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.awt.image.*;

public class ServeurLineaire{
    public static void main(String[] args) throws Exception{
	
	int w_3 = 341;
	DatagramSocket socket = new DatagramSocket();
	
	byte[] buffer = new byte[2];
	DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

	BufferedImage image = ImageIO.read(new File(args[0]));
	Raster raster = image.getData();

	int height = raster.getHeight();
	int width = raster.getWidth();
	
	int raw = 0;
	int offset = 0;
	boolean valide = true;
	System.out.println("Demarré !");
	while(raw < height){
	    // System.out.println("Prêt a envoyer");
	    
	    if( (offset+w_3) < width ){
		buffer = (byte[])raster.getDataElements(offset, raw, w_3, 1, null);
		valide = true;
	    }
	    else{
		buffer = (byte[])raster.getDataElements(offset, raw, width-offset, 1, null);
		valide = false;
	    }
	    
	    if( valide )
		offset += w_3;
	    else
		offset += width-offset;
	    if( offset >= width ){
		++raw;
		offset = offset%width;
	    }
		
	    packet.setData(buffer);
	    
	    socket.send(packet);
	    Thread.currentThread().sleep(100);
	    
	    // System.out.println("Envoyé !"+raw);
	}

	System.out.println("Terminé !");
    }
}
