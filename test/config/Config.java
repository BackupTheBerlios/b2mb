import java.io.*;
import java.util.*;

public class Config{
    public static void main(String[] args) throws Exception{
	FileInputStream in = new FileInputStream("config.sys");
	Properties def = new Properties();
	def.load(in);
	in.close();
	Properties props = new Properties(def);
	props.setProperty("port", "8086");
	System.out.println(props.getProperty("port"));
	System.out.println(props.getProperty("machine"));
	System.out.println(props.getProperty("toto", "tata"));
	FileOutputStream out = new FileOutputStream("config2.sys");
	props.store(out, "Sauvagarde");
	out.close();
    }
}
