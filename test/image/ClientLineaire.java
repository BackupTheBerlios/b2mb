import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class ClientLineaire extends JFrame{
    BufferedImage image;
    
    ClientLineaire(BufferedImage img){
	image = img;
    }

    public void paint(Graphics g){
	g.drawImage(image, 0, 0, this); 
    }
        
    public static void main(String[] args) throws Exception{
	DatagramSocket socket = new DatagramSocket(50000);
	
	int width = Integer.parseInt(args[0]);
	int height = Integer.parseInt(args[1]);
	
	byte[] buffer = new byte[1023];
	DatagramPacket packet = new  DatagramPacket(buffer, 1023); 

	// Creation de l'image avec taille connue
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	WritableRaster raster = image.getRaster();

	// Display
	ClientLineaire frame = new ClientLineaire(image);
	frame.setSize(new Dimension(width, height));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.show();
	
	int raw = 0;
	int offset = 0;
	while(true){
	    // System.out.println("Prêt a recevoir");
	    socket.receive(packet);
	   
	    // Height will always be 1 for linear 
	    
	    raster.setDataElements(offset, raw, packet.getLength()/3, 1, buffer);
	    
	    // RE-DISPLAY	    
	    frame.repaint();
	    
	    // INIT
	    offset += packet.getLength()/3;
	    if( offset >= width ){
		++raw;
		offset = offset%width;
	    }
	    
	    // SETDATA
	    packet.setData(buffer);

	    // System.out.println("Reçu !"+raw);
	}
    }
}
