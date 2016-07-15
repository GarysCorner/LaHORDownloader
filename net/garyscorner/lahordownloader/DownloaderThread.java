//File:		DownloaderThread.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx



package net.garyscorner.lahordownloader;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DownloaderThread extends Thread{
    
    private URL url;
    FileOutputStream savefile;
    
    DownloadProgress progress;
    
    public DownloaderThread(URL url, FileOutputStream savefile, DownloadProgress progress) {
        super();
        this.url = url;
        this.savefile = savefile;
        this.progress = progress;
    }
    
    @Override
    public void run() {
        
        byte[] buff = new byte[1024];
        
        progress.setStatus(DownloadProgress.STATUS_CONNECTING);
        
        URLConnection conn = null;     
        try {
            conn = this.url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
            progress.setStatus(DownloadProgress.STATUS_ERROR);
        }
        
        progress.setStatus(DownloadProgress.STATUS_CONNECTED);
        
        long filesize = conn.getContentLength();
        System.err.printf("File size %1$d\n", filesize);
        progress.setFileSize(filesize);
        
        BufferedInputStream instream=null;
        try {
            instream = new BufferedInputStream(conn.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
            progress.setStatus(DownloadProgress.STATUS_ERROR);
        }
        
        int count;
        
        try {
            while((count = instream.read(buff, 0 ,1024)) != -1) {
                progress.setStatus(DownloadProgress.STATUS_STARTED);
                savefile.write(buff, 0 ,count);
                progress.addCount(count);
            }
            progress.setStatus(DownloadProgress.STATUS_FINISHED);
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
            progress.setStatus(DownloadProgress.STATUS_ERROR);
            
        }
        
        try {
            savefile.close();
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        System.err.println("File downloaded!");
        
    }
}

