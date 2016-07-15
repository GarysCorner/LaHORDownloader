//File:		DownloaderThread.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx



package net.garyscorner.lahordownloader;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DownloaderThread extends Thread{
    
    private URL url;
    FileOutputStream savefile;
    
    public DownloaderThread(URL url, FileOutputStream savefile) {
        super();
        this.url = url;
        this.savefile = savefile;
    }
    
    @Override
    public void run() {
        
        byte[] buff = new byte[1024];
        
        URLConnection conn = null;     
        try {
            conn = this.url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        long filesize = conn.getContentLength();
        System.err.printf("File size %1$d\n", filesize);
       
        
        BufferedInputStream instream=null;
        try {
            instream = new BufferedInputStream(conn.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int count;
        
        try {
            while((count = instream.read(buff, 0 ,1024)) != -1) {
                
                savefile.write(buff, 0 ,count);
            }
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            savefile.close();
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        System.err.println("File downloaded!");
        
    }
}
