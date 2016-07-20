//File:		LaHORDownloader.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx

package net.garyscorner.lahordownloader;

import javax.swing.JFrame;


public class LaHORDownloader {

    //variables
    public static final String Version = "1.0";
    public static final String About = "La HOR Video Downloader v" + LaHORDownloader.Version + 
                                        " was created to allow downloading of videos from the Louisiana House of Representative's video on demand site at \"http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx\".  " +
                                        "Although this program was designed exclusively for this site it will most likely work on some other sites.  Please do not use this program to download things illegally!";
    
    
    
    //functions
    
    //start the app.
    public void run() {
        MainDialog maindialog = new MainDialog(new DummyFrame("LaHORDownloader"), true);
        
        maindialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  //make sure the program actually completes
        
        maindialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        System.err.println("Starting application...");
        LaHORDownloader app = new LaHORDownloader();
        app.run();
    }
    
    //Dummy frame for taskbar
    public class DummyFrame extends JFrame {
        public DummyFrame(String title) {
            super(title);
            this.setUndecorated(true);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
        }
    }
    
}
