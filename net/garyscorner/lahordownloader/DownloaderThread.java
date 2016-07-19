//File:		DownloaderThread.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx



package net.garyscorner.lahordownloader;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        
        if(getVideoURL()) {
            downloadVideo();
        } else {
            progress.setStatus(DownloadProgress.STATUS_PERROR);
        }
        
        try {
            this.savefile.close();
        } catch (IOException ex) {
            System.err.printf("There was an error closing the file:  %1$s%2$s", ex, System.lineSeparator());
        }
    }
    
    //populate the video url if failure for some reason return false
    private boolean getVideoURL() {
        
        progress.setStatus(DownloadProgress.STATUS_RETREIVING);
        
        URLConnection conn = null;
        try {
            conn = this.url.openConnection();
        } catch(IOException ex) {
            System.err.println("Error could not fetch URL:  " + this.url.toString());
            return false;
        }
        
        if(this.isInterrupted()) {
            System.err.println("Download thread was interrupted!");
            progress.setStatus(DownloadProgress.STATUS_CANCELED);
            return false;
        }
        
        BufferedReader inbuff;
        try {
            inbuff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Error opening stream to parse html!");
            return false;
        }
        
        
        Pattern pattern = Pattern.compile("<video.+?><source.+?/>");
        Matcher matcher = null;
        String line;
        
        int linenum = 0;
        boolean foundmatch = false;
        
        do {
            
            if(this.isInterrupted()) {
                System.err.println("Download thread was interrupted!");
                progress.setStatus(DownloadProgress.STATUS_CANCELED);
                return false;
            }
            
            try {
                line = inbuff.readLine();
            } catch (IOException ex) {
                System.err.println("Error while trying to read html from stream:  "+ ex);
                return false;
            }
            linenum++;
            
            if(line != null) {
                matcher = pattern.matcher(line);
                foundmatch = matcher.find();
            } else {
                break;
            }
            
            
            
        } while( line != null && !foundmatch  );
        
        if(foundmatch) {
            System.err.printf("Match object found line #%1$d:  %2$s%3$s", linenum , matcher.group(), System.lineSeparator());
            
            //find the URL
            pattern = Pattern.compile("src=\".+?\"");
            matcher = pattern.matcher(matcher.group());
            if(matcher.find()) {
                String match = matcher.group().substring(5,matcher.group().length()-1);
                System.err.println("Source URL found:  " + match);
                
                try {
                    url = new URL(match);
                } catch (MalformedURLException ex) {
                    System.err.println("Could not extract URL from:  " + match);
                    return false;
                }
                
            } else {
                System.err.println("Could not find \"src=\" in match!");
                return false;
            }
            
            
        } else {
            System.err.println("Error:  No match found!!!");
            return false;
        }
        
        return true;
    }
    
    
    //download the actual video
    private void downloadVideo() {
        
        progress.setStatus(DownloadProgress.STATUS_CONNECTING);
        
        byte[] buff = new byte[1024];
        
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
        
        if(this.isInterrupted()) {
            System.err.println("Download thread was interrupted!");
            progress.setStatus(DownloadProgress.STATUS_CANCELED);
            return;
        }
        
        BufferedInputStream instream=null;
        try {
            instream = new BufferedInputStream(conn.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
            progress.setStatus(DownloadProgress.STATUS_ERROR);
        }
        
        int count;
        
        progress.setStatus(DownloadProgress.STATUS_STARTED);
        
        try {
            
            while((count = instream.read(buff, 0 ,1024)) != -1) {
                if(this.isInterrupted()) {
                    System.err.println("Download thread was interrupted!");
                    progress.setStatus(DownloadProgress.STATUS_CANCELED);
                    return;
                }
                savefile.write(buff, 0 ,count);
                progress.addCount(count);
            }
            progress.setStatus(DownloadProgress.STATUS_FINISHED);
        } catch (IOException ex) {
            Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
            progress.setStatus(DownloadProgress.STATUS_ERROR);
            
        }
        
        
        System.err.println("File downloaded!");
        
    }
    
}

