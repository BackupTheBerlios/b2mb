import network.Servent;
import gui.VMainComponent;
import controller.GuiController;

public class Main{
    public static void main(String[] args) throws Exception{
	//VSplash splash = new VSplash();
	//splash.setVisible(true);
	System.out.println("blabla");
	String setupFile;
	if(args.length==1)
	    setupFile = args[0];
	else
	    setupFile = "config.bmb";
	
	GuiController controller = new GuiController();
	Servent servent = new Servent();
	VMainComponent view = new VMainComponent();
	
	controller.setModel(servent);
	controller.setView(view);
	view.setController(controller);
	servent.setController(controller);
	
	view.show();
	servent.startServent(setupFile);
	//splash.close();
    }
}
