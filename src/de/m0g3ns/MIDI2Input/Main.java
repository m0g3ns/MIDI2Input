package de.m0g3ns.MIDI2Input;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    static boolean running = true;
    public static void main(String[] args) {
        new Input();

        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = null;
            try {
                image = ImageIO.read(Main.class.getResource("/resources/images/controlPanel.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);
            /// ... add other items
            // construct a TrayIcon
            int trayIconWidth = new TrayIcon(image).getSize().width;
            trayIcon = new TrayIcon(image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "MIDI2Input", popup);
            // set the TrayIcon properties

            trayIcon.addActionListener(listener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions
        }
    }

    public static void finish() {
        running = false;
    }
}