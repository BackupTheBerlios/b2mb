package fr.umlv.b2mb.gui;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*; 
import javax.imageio.*;

public class ServentLinear extends JPanel{
    private BufferedImage image = null;
    private Thread client;
    
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
		
			int width = Integer.parseInt(w);
			int height = Integer.parseInt(h);
			
			byte[] buffer = new byte[1023];
			DatagramPacket packet = new  DatagramPacket(buffer, 1023); 
		
			// Creation de l'image avec taille connue
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			WritableRaster raster = image.getRaster();
		
			int raw = 0;
			int offset = 0;
			while(true){
			    socket.receive(packet);
		    
			    // Height will always be 1 for linear 
			    raster.setDataElements(offset, raw, packet.getLength()/3, 1, buffer);
		    
			    // RE-DISPLAY	    
			    ServentLinear.this.repaint();

			    // Exit
			    if( (raw == (height-1)) && ((offset+packet.getLength()/3) == width) )
				break;
			    			    
			    // INIT
			    offset += packet.getLength()/3;
			    if( offset >= width ){
				++raw;
				offset = offset%width;
			    }
		    
			    // SETDATA
			    //packet.setData(buffer);
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    });
	client.start();
    }

    public void stopClient(){
	image = null;
	client.stop();
    }

    public void startServer(final String fileName){
	(new Thread(new Runnable(){
		public void run(){
		    try{
			int w_3 = 341;
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			BufferedImage image = ImageIO.read(new File(fileName));

			// Scaling
			AffineTransform scale = AffineTransform.getScaleInstance(500/150, 500/110);
			//getShearInstance(2, 2);
			AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = scaleOp.filter(image, null); 
			
			Raster raster = image.getData();

			int height = raster.getHeight();
			int width = raster.getWidth();
			System.out.println(height+"  "+width);
			
			int raw = 0;
			int offset = 0;
			boolean valide = true;
		    
			while(raw < height){
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
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    })).start();
    }
}









