package gui;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class ServentLinearByTen extends Container{
    private BufferedImage image = null;
    private Thread client;
    private int incr = 10;
    private int width = 0, height = 0;
    
    public ServentLinearByTen(){
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
			
			byte[] buffer = new byte[1023];
			DatagramPacket packet = new  DatagramPacket(buffer, 1023); 
		
			// Creation de l'image avec taille connue
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			WritableRaster raster = image.getRaster();
			
			int offset = 0;
			int raw;
			for(int i=0;i<10;i++){
			    raw = i;
			    //int offset = 0;
			    while(true){
				socket.receive(packet);
				//System.out.print(raw+" ");
				// Height will always be 1 for linear 
				System.out.println("("+raw+" ,"+offset+")");
				raster.setDataElements(offset, raw, packet.getLength()/3, 1, buffer);
		    
				// RE-DISPLAY	    
				ServentLinearByTen.this.repaint();
				
				// Exit
				//if( (raw == (height-1)) && ((offset+packet.getLength()/3) == width) )
				//break;
				
				// Increase the offset
				offset += packet.getLength()/3;

				// Increase the raw and descrease the offset
				if( offset >= width ){
				    offset = offset%width;
				    
				    if( (raw+incr) < height)
					raw += incr;
				    else // Exit the loop
					break;
				}
		    
				// SETDATA
				// packet.setData(buffer);
			    }
			    //System.out.println();
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
			int w_3 = 341;
			DatagramSocket socket = new DatagramSocket();
	
			byte[] buffer = new byte[2];
			DatagramPacket packet = new  DatagramPacket(buffer, 2, InetAddress.getLocalHost(), 50000); 

			BufferedImage image = ImageIO.read(new File(fileName));
			Raster raster = image.getData();

			//int height = raster.getHeight();
			//int width = raster.getWidth();
						
			boolean valide = true;

			int offset = 0;
			int raw;
			for(int i=0;i<10;i++){
			    Thread.currentThread().sleep(1000);
			    raw = i;
			    //int offset = 0;
			    while(raw < height){
				if( (offset+w_3) < width ){
				    buffer = (byte[])raster.getDataElements(offset, raw, w_3, 1, null);
				    valide = true;
				}
				else{
				    buffer = (byte[])raster.getDataElements(offset, raw, width-offset, 1, null);
				    valide = false;
				}
				System.out.println("Serveur : ("+raw+" ,"+offset+")");
				// Increase the offset
				if( valide )
				    offset += w_3;
				else
				    offset += width-offset;

				// Increase the raw and descrease the offset
				if( offset >= width ){
				    offset = offset%width;
				    
				    if( (raw+incr) < height )
					raw += incr;
				    else // Exit the loop
					break;
				}

				// Set the data
				packet.setData(buffer);
			
				socket.send(packet);
				Thread.currentThread().sleep(100);
			    }
			}
			socket.close();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    })).start();
    }
}
