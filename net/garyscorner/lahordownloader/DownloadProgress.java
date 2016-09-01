//File:		DownloadDialog.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.asp

package net.garyscorner.lahordownloader;


//This clas allows us to keep up with progress and status
public class DownloadProgress {
    
    private long filesize = 0;
    private long count = 0;
    private boolean finished = false;  //is file download finished
    private int status = 0;
    private long updatecount = 1;
    
    //status flags
    public final static int STATUS_NONE = 0;  //no status
    public final static int STATUS_CONNECTING = 3;  //obvious
    public final static int STATUS_CONNECTED = 4;
    public final static int STATUS_RETREIVING = 5;
    public final static int STATUS_STARTED = 1; //started downloaded
    public final static int STATUS_FINISHED = 2;  //download completed
    public final static int STATUS_ERROR = -1;  //Error occurred
    public final static int STATUS_PERROR = -2;  //failed to parse mp4 from URL
    public final static int STATUS_CANCELED=-10;
    
    //returns the number of times updates to determine if we need to update the GUI
    public synchronized long getUpdateCount() {
        return this.updatecount;
    }
    
    public synchronized void setStatus(int status) {
        this.status = status;
        this.updatecount++;
    }
    
    public synchronized int getStatus() {
        return this.status;
    }
    
    public synchronized void setFinished() {
        finished = true;
        this.updatecount++;
    }
    
    public synchronized boolean getFinished() {
        return finished;
    }
    
    public synchronized void setFileSize(long filesize) {
        this.filesize = filesize;
        this.updatecount++;
    }
    
    public synchronized void addCount(int count) {
        this.count += count;
        this.updatecount++;
    }
    
    public synchronized long getFileSize() {
        return this.filesize;
    }
    
    public synchronized long getCount() {
        return this.count;
    }
    
}
