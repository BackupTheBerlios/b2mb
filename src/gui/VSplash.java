package fr.umlv.b2mb.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

/**
 * Display a splash before the soft starts
 * @author B2MB
 */

class VSplash extends JWindow {

    public VSplash(){
        JLabel l = new JLabel(new ImageIcon("Icons/splash.gif"));
        getContentPane().add(l, BorderLayout.CENTER);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();

        // Put image at the middle of the screen
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
		
    }

    public void close() {
	setVisible(false);
	dispose();
    }

}

