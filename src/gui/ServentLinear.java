package gui;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*; 
import javax.imageio.*;

import parser.UDPImageDatagram;

public class ServentLinear extends Container{
    private BufferedImage image = null;
    private Thread client;
    private int width = 0, height = 0;
    
    public ServentLinear(){
    }

    public void paint(Graphics g){
	if( image != null )
	    g.drawImage(image, 0, 0, this);
    }
    
    public void startClient(final String w, final String h){
	client = new Thread(new Runnable(){
		public void run(){
		    try{
			DatagramSocket socket = new DatagramSocket(50000);
			
			width = Integer.parseInt(w);
			height = Integer.parseInt(h);
			
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new  DatagramPacket(buffer, 1024); 
		
			// Creation de l'image avec taille connue
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			WritableRaster raster = image.getRaster();
		
			int raw = 0;
			int t = 1;
			boolean firstTime = true;
			while(true){
			    socket.receive(packet);
			    // Height will always be 1 for linear
			    raster.setDataElements(UDPImageDatagram.getOffset(buffer), raw, UDPImageDatagram.getFragmentSize(buffer, packet.getLength())/3, 1, UDPImageDatagram.getFragment(buffer, packet.getLength()));
			    // RE-DISPLAY	    
			    ServentLinear.this.repaint();
			    
			    // Exit
			    if( (raw == (height-1)) && ((UDPImageDatagram.getOffset(buffer)+UDPImageDatagram.getFragmentSize(buffer, packet.getLength())/3) == width) )
				break;

			    // Increment the raw if necessary
			    if( !firstTime && (UDPImageDatagram.getOffset(buffer) == 0))
				++ raw;
			    else firstTime = false;
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    });
	client.start();
    }
    
    public void startServer(final String fileName){
	(new Thread(new Runnable(){
		public void run(){
		    try{
			int w_3 = 1006/3; //341;
			
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			BufferedImage image = ImageIO.read(new File(fileName));
			
			// Scaling
			AffineTransform scale = AffineTransform.getScaleInstance(width/(double)image.getWidth(), height/(double)image.getHeight());
			AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = scaleOp.filter(image, null); 
			
			Raster raster = image.getData();

			int raw = 0;
			int offset = 0;
			boolean valide = true;
			
			while(raw < height){
			    if( (offset+w_3) < width ){
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset, raw, w_3, 1, null));
				valide = true;
			    }
			    else{
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset, raw, width-offset, 1, null));
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
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    })).start();
    }
}









