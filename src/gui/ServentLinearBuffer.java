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
import java.nio.*;

import parser.UDPImageDatagram;
import utils.ArrayManipulator;

public class ServentLinearBuffer extends Container{
    private BufferedImage image = null;
    private Thread client;
    private int width = 0, height = 0;
    
    public ServentLinearBuffer(){ 
    }

    public void paint(Graphics g){
	if( image != null )
	    g.drawImage(image, 0, 0, this);
    }

    public void setImage(BufferedImage image){
	this.image = image;
    }
    
    public void startClient(final String w, final String h){
	client = new Thread(new Runnable(){
		public void run(){
		    try{
			DatagramSocket socket = new DatagramSocket(50000);
			
			width = Integer.parseInt(w);
			height = Integer.parseInt(h);
			
			// The client knows the file's size
			int size = /*8106;*/ 129575; //154421;
			byte[] imageByte = new byte[size];
			
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new  DatagramPacket(buffer, 1024); 

			int offset = 0;
			// Creation de l'image avec taille connue
			while(true){
			    socket.receive(packet);

			    offset = UDPImageDatagram.getOffset(buffer);
			    ArrayManipulator.copyArrayAtEnd(imageByte, UDPImageDatagram.getFragment(buffer, packet.getLength()), offset);
			    
			    // At the end Display and quit
			    if( (offset+UDPImageDatagram.getFragmentSize(buffer, packet.getLength())) == size ){
				byte[] b = UDPImageDatagram.getFragment(buffer, packet.getLength());
				ByteArrayInputStream stream = new ByteArrayInputStream(imageByte);
				ServentLinearBuffer.this.setImage(ImageIO.read(stream));
				ServentLinearBuffer.this.repaint();
				
				break;
			    }
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
			int w_3 = 1006;
			
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			
			File f = new File(fileName);
			BufferedImage image = ImageIO.read(f);

			// Scaling
			AffineTransform scale = AffineTransform.getScaleInstance(width/(double)image.getWidth(), height/(double)image.getHeight());
			AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = scaleOp.filter(image, null); 
			
			// Converts into jpg
			ByteArrayOutputStream output = new ByteArrayOutputStream();			
			ImageIO.write(image, "jpg", output);
			byte[] imageBuffer = output.toByteArray();

			System.out.println("Taille "+imageBuffer.length);
			int offset = 0;
			int size = (int)imageBuffer.length;
			byte[] fragment = new byte[1006];
			ByteBuffer byteBuffer = ByteBuffer.wrap(imageBuffer);
			
			while(offset <= size){
			    // Only at the end 
			    if( (offset+w_3) > size )
				fragment = new byte[size-offset];
			    
			    // Init the fragment
			    byteBuffer.get(fragment);
						    
			    buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, fragment);

			    // Increments the offset
			    offset += w_3;

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
}









