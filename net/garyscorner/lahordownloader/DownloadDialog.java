//File:		DownloadDialog.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx

package net.garyscorner.lahordownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;


public class DownloadDialog extends javax.swing.JDialog {

    //my variables
    private File savefile;
    private URL url;
    
    private DownloaderThread downloader;
    FileOutputStream outfile;
    
    long filesize = 0;
    
    private DownloadProgress progress;
    
    private Timer progressTimer;
    
    //my functions
    
     
    
    //special init for me
    public void myinit(URL url, File savefile) {
        this.url = url;
        this.savefile = savefile;
        this.progress = new DownloadProgress();
        
        
        
    }
    
    //start the download
    public void startdownload() {
        
        ActionListener checkProgress = new ActionListener() {
            
            long filesize;
            int status = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(this.status != progress.getStatus()) {
                    this.status = progress.getStatus();  //ugly
                    
                    switch(this.status) {
                        case DownloadProgress.STATUS_CONNECTED:
                            jLabel_status.setText("Connected");
                            break;
                            
                        case DownloadProgress.STATUS_CONNECTING:
                            jLabel_status.setText("Connecting...");
                            break;
                        
                        case DownloadProgress.STATUS_ERROR:
                            jLabel_status.setText("ERROR!");
                            break;
                            
                        case DownloadProgress.STATUS_FINISHED:
                            jLabel_status.setText("Finished!");
                            break;
                            
                        case DownloadProgress.STATUS_PERROR:
                            jLabel_status.setText("Error parsing video URL!");
                            break;
                            
                        case DownloadProgress.STATUS_STARTED:
                            jLabel_status.setText("Downloading...");
                            break;
                            
                        case DownloadProgress.STATUS_RETREIVING:
                            jLabel_status.setText("Retreiving download URL from website...");
                            break;
                        default:
                            
                         
                    }
                    
                }
                
                //check progress and update progress bar here
                if(this.filesize > 0) {
                     jProgressBar_download.setValue((int) (progress.getCount() / 1024));
                } else if(this.filesize == -1) {
                    
                } else {
                    if((this.filesize = progress.getFileSize()) > 0 ) {
                        jProgressBar_download.setMaximum((int)(this.filesize / 1024));
                    }
                }
            }
            
        };
        
        progressTimer = new Timer(1000, checkProgress);
        progressTimer.start();
        
        try {
            outfile = new FileOutputStream(this.savefile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        downloader = new DownloaderThread(url, outfile, progress);
        
        System.err.println("Download thread starting...");
        downloader.start();
    }
    
    
    /**
     * Creates new form DownloadDialog
     */
    public DownloadDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar_download = new javax.swing.JProgressBar();
        jLabel_status = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel_status.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar_download, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jLabel_status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar_download, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_status, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_status;
    private javax.swing.JProgressBar jProgressBar_download;
    // End of variables declaration//GEN-END:variables
}
