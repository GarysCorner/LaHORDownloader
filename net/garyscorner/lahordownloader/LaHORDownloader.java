//File:		LaHORDownloader.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx

package net.garyscorner.lahordownloader;

import javax.swing.JFrame;


public class LaHORDownloader {

    //variables
    
    //functions
    
    //start the app.
    public void run() {
        MainDialog maindialog = new MainDialog(null, true);
        
        maindialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  //make sure the program actually completes
        
        maindialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        System.err.println("Starting application...");
        LaHORDownloader app = new LaHORDownloader();
        app.run();
    }
    
}
