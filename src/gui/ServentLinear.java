package gui;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*; 
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.*;

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
			
			
			boolean firstTime = true;
			int offset;
			
			while(true){
			    socket.receive(packet);

			    offset = UDPImageDatagram.getOffset(buffer);
			    // Height will always be 1 for linear
			    raster.setDataElements(offset%width, offset/width, UDPImageDatagram.getFragmentSize(buffer, packet.getLength())/3, 1, UDPImageDatagram.getFragment(buffer, packet.getLength()));
			    
			    // RE-DISPLAY	    
			    ServentLinear.this.repaint();

			    // Exit
			    if( (offset/width == (height-1)) && ((offset%width+UDPImageDatagram.getFragmentSize(buffer, packet.getLength())/3) == width) )
				break;
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
			int w_3 = 1006/3; 
			
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			BufferedImage image = ImageIO.read(new File(fileName));
			
			// Scaling
			AffineTransform scale = AffineTransform.getScaleInstance(width/(double)image.getWidth(), height/(double)image.getHeight());
			AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = scaleOp.filter(image, null); 

			// Converts in jpg
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", output);
			ByteArrayInputStream stream = new ByteArrayInputStream(output.toByteArray());
			image = ImageIO.read(stream);
			
			Raster raster = image.getData();

			System.out.println(image.getHeight()+"  "+raster.getHeight());
			int offset = 0;
			boolean valide = true;
						
			while( (offset/width) < height){
			    if( (offset%width+w_3) < width ){
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset%width, offset/width, w_3, 1, null));
				valide = true;
			    }
			    else{
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset%width, offset/width, width-offset%width, 1, null));
				valide = false;
			    }
			    			    
			    if( valide )
				offset += w_3;
			    else
				offset += width-offset%width;
			    			
			    packet.setData(buffer);
			    
			    socket.send(packet);
			    Thread.currentThread().sleep(1);
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    })).start();
    }

    // Server 10 raws by 10
    /*public void startServer(final String fileName){
	(new Thread(new Runnable(){
		public void run(){
		    try{
			int w_3 = 1006/3; 
			
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			BufferedImage image = ImageIO.read(new File(fileName));
			
			// Scaling
			AffineTransform scale = AffineTransform.getScaleInstance(width/(double)image.getWidth(), height/(double)image.getHeight());
			AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = scaleOp.filter(image, null); 
			
			// Converts in jpg
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", output);
			ByteArrayInputStream stream = new ByteArrayInputStream(output.toByteArray());
			image = ImageIO.read(stream);
			
			Raster raster = image.getData();

			int offset = 0;
			boolean valide = true;

			for(int i=0;i<10;i++){
			    while( (offset/width) < height){
			    if( (offset%width+w_3) < width ){
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset%width, offset/width, w_3, 1, null));
				valide = true;
			    }
			    else{
				buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, (byte[])raster.getDataElements(offset%width, offset/width, width-offset%width, 1, null));
				valide = false;
			    }
			    			    
			    if( valide )
				offset += w_3;
			    else{
				offset += width-offset%width;
				offset += 10*width;
			    }
			    packet.setData(buffer);
			    
			    socket.send(packet);
			    Thread.currentThread().sleep(1);
			}
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    })).start();
	    }*/
}









