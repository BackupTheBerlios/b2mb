public class Test{
    public static void main(String[] args) throws Exception{
	Setup setup = new Setup("tmp.txt");

	System.out.println("Path : "+setup.getPath());
	System.out.println("Nb processus : "+setup.getNbProcessus());
	System.out.println("Nb clients : "+setup.getNbClients());

	setup.setPath("toto/tata");
	setup.setNbProcessus(1);
	setup.setNbClients(0);
	
	setup.save();
    }
}
