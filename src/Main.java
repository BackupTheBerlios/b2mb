import network.Servent;
import gui.VMainComponent;
import controller.GuiController;

/**
 * Main class. Starts the servent and shows the graphical user interface.
 */
public class Main{
    public static void main(String[] args) throws Exception{
	//VSplash splash = new VSplash();
	//splash.setVisible(true);
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
