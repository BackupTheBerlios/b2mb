// cf http://java.sun.com/products/jdk/1.2/docs/guide/2d/spec/j2d-title.fm.html

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class MyImage extends JFrame{
    private BufferedImage image;

    MyImage(BufferedImage img){
	image = img;
    }

    public void paint(Graphics g){
	g.drawImage(image, 0, 0, this); 
    }

    public WritableRaster getRaster(){
	return image.getRaster();
    }

    public int getHeight(){
	//return image.getHeight();
	return getRaster().getHeight();
    }

    public int getWidth(){
	//return image.getWidth();
	return getRaster().getWidth();
    }

    public DataBuffer getDataBuffer(){
	return getRaster().getDataBuffer(); 
    }

    public SampleModel getSampleModel(){
	return getRaster().getSampleModel(); 
    }

    public void setRaster(Raster raster){
	image.setData(raster);
    }
    
    /**************************************/
    
    public static void main(String[] args) throws IOException{
	MyImage image = new MyImage(ImageIO.read(new File("linux.jpg")));
	
	// image.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	image.setSize(new Dimension(1024, 768));
	image.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	System.out.println("H : "+image.getHeight()+" W : "+image.getWidth());
	System.out.println("MinX : "+image.getRaster().getMinX()+" MinY : "+image.getRaster().getMinY());
	System.out.println("DataByte size : "+image.getDataBuffer().getSize());
	System.out.println("NbBands : "+image.getRaster().getNumBands());
	System.out.println("NbDataElements : "+image.getRaster().getNumDataElements());
	System.out.println("Data Type : "+image.getSampleModel().getDataType()); 
	image.show();
	
	/*****************************************************************************
	  byte[] buffer = new byte[taille du buffer récupéré];
	  DataByteBuffer dataBuffer = new DataByteBuffer(buffer, sa taille);
	  SampleModel model = new ComponentSampleModel(DataBuffer.TYPE_BYTE, w, h, 3 (PAS), 1(PAS), new int[] {0, 1, 2} );
	  OU SampleModel model = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, w, h, 3 (PAS), 1(PAS), new int[] {0, 1, 2} ); cf http://www.panix.com/userdirs/jutta/scoop/javaSampleModel.html
	  NOTE : w et h doivent être récupéré par l'intermédiaire du premier BufferedImage créé.
	         Pour les PAS, tester avec les Raster.getNumBands et getNumDataElements

	  MyWritableRaster raster = new MyWritableRaster(model, dataBuffer, new Point(0, 0);
	  image.setRaster(raster);
	  Ensuite, boucle while => raster.setSamples(int x, int y, int w, int h, int b, int[] iArray);
	  ( défintion d'une région spécifique )
	*****************************************************************************/
	/*****************************************************************************
	 ENCORE MIEUX :
	 byte[] buffer = new byte[1024]; => On stocke les données la 1ere fois
	 BufferedImage image = ImageIO.read(new ByteArrayInputStream(buffer));
	 Ensuite, on récupére le raster. => WritableRaster raster = image.getRaster();
	 A chaque fois que l'on reçoit un paquet => raster.setDataElements(int x, int y, int w, int h, Object inData)
	 Et c tout
	 *****************************************************************************/
    }
}



