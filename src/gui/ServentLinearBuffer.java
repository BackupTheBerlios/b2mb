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
import utils.ArrayManipulator;

public class ServentLinearBuffer extends Container{
    private BufferedImage image = null;
    private Thread client;
    private int width = 0, height = 0;
    
    public ServentLinearBuffer(){ 
    }

    public void paint(Graphics g){
	if( image != null ){
	    g.drawImage(image, 0, 0, this);
	    System.out.println("Repaint");
	}
    }

    public void setImage(BufferedImage image){
	this.image = image;
	System.out.println(image);
    }
    
    public void startClient(final String w, final String h){
	client = new Thread(new Runnable(){
		public void run(){
		    try{
			DatagramSocket socket = new DatagramSocket(50000);
			
			width = Integer.parseInt(w);
			height = Integer.parseInt(h);

			// The client knows the file's size
			int size = 154421;
			byte[] imageByte = new byte[size];
			
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new  DatagramPacket(buffer, 1024); 

			int offset = 0;
			// Creation de l'image avec taille connue
			while(true){
			    socket.receive(packet);
			    
			    ArrayManipulator.copyArrayAtEnd(imageByte, UDPImageDatagram.getFragment(buffer, packet.getLength()), offset);
			    offset = UDPImageDatagram.getOffset(buffer);
			    System.out.println("Client > "+offset);
			    // Display and quit
			    if( offset == size ){
				ByteArrayInputStream stream = new ByteArrayInputStream(imageByte);
				//System.out.println(ImageIO.read(ImageIO.createImageInputStream(stream)));
				ServentLinearBuffer.this.setImage(ImageIO.read(ImageIO.createImageInputStream(stream)));
				ServentLinearBuffer.this.repaint();
				/*File f = new File("Images/linux.jpg");
				ImageInputStream input = ImageIO.createImageInputStream(f);
				byte[] t = new byte[size];
				input.readFully(t);

				for(int i=0;i<size;i++){
				    System.out.println(i);
				    if( t[i] != imageByte[i] ){
					System.out.println(t[i]+" != "+imageByte[i]);
					for(int j=0;j<8;j++)
					    System.out.print((t[i]>>j)&1);
					System.out.println();
					for(int j=0;j<8;j++)
					    System.out.print((imageByte[i]>>j)&1);
					System.out.println("ERROR !!");
					System.exit(1);
				    }
				    }*/
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
			ImageInputStream input = ImageIO.createImageInputStream(f);

			// Converts in JPG
			// ...

			// Possible scaling
			//AffineTransform scale = AffineTransform.getScaleInstance(width/(double)image.getWidth(), height/(double)image.getHeight());
			//AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			//image = scaleOp.filter(image, null); 
			
			int tmp = 0;
			int offset = 0;
			boolean valide = true;
			int size = (int)input.length();
			byte[] fragment = new byte[1006];
			while(offset <= size){
			    if( (offset+w_3) > size )
				fragment = new byte[size-offset];
			    
			    tmp = input.read(fragment);
			    buffer = UDPImageDatagram.createImageDatagram(89, 1, 
					     width*height, offset, fragment);

			    offset += tmp;
			    
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









