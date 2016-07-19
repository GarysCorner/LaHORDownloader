//File:		MainDialog.java
//Author:	Gary Bezet
//Date:		2016-07-14
//Desc:		This program was created to help Nick download videos from the Louisiana House or Representatives video on demand site at 
//HORWebsite:   http://house.louisiana.gov/H_Video/Hse_Video_OnDemand.aspx


package net.garyscorner.lahordownloader;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;




public class MainDialog extends javax.swing.JDialog {
    
    //my variables
    JFileChooser filechooser;
    FileNameExtensionFilter filter;
    
    //Oldialog
    private File savefile;
    private URL url;
    
    private DownloaderThread downloader;
    FileOutputStream outfile;
    
    long filesize = 0;
    
    private DownloadProgress progress;
    
    private Timer progressTimer;
    //end old dialog
    
    //my functions
    
    //initialize my stuff
    public void myinit() {
        filechooser = new JFileChooser();
        filter = new FileNameExtensionFilter("MP4 Video", "mp4");
        filechooser.setFileFilter(filter);
        
        this.progress = new DownloadProgress();
        
    }
    
    //validate the URL
    private URL validateURL(String url) {
        
        
        URL returnvalue;
        try {
            returnvalue = new URL(url);
        } catch (MalformedURLException ex) {
            System.err.printf("Bad URL provided:  %1$s", url);
            JOptionPane.showMessageDialog(this, "Bad URL!", "Error", JOptionPane.ERROR_MESSAGE);
            returnvalue = null;
        }
        
        return returnvalue;
    }
    
    
    
    //setup the download process
    private void startdownload(URL url, File savefile)  {
        
        this.url = url;
        this.savefile = savefile;
        
        ActionListener checkProgress = new ActionListener() {
            
            long updatecount = 0;
            
            long filesize;
            int status = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                long newupdatecount = progress.getCount();
                
                if(updatecount < newupdatecount) {
                    
                    updatecount = newupdatecount;
                    
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
     * Creates new form MainDialog
     */
    public MainDialog(java.awt.Frame parent, boolean modal) {
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

        textfield_url = new javax.swing.JTextField();
        jButton_download = new javax.swing.JButton();
        jProgressBar_download = new javax.swing.JProgressBar();
        jLabel_status = new javax.swing.JTextField();
        MenuBar = new javax.swing.JMenuBar();
        MenuItem_File = new javax.swing.JMenu();
        MenuItem_Preferences = new javax.swing.JMenuItem();
        MenuItem_About = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton_download.setText("Download");
        jButton_download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_downloadActionPerformed(evt);
            }
        });

        MenuItem_File.setText("File");

        MenuItem_Preferences.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, 0));
        MenuItem_Preferences.setText("Preferences");
        MenuItem_File.add(MenuItem_Preferences);

        MenuBar.add(MenuItem_File);

        MenuItem_About.setText("About");
        MenuBar.add(MenuItem_About);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textfield_url)
            .addComponent(jButton_download, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jProgressBar_download, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_status)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(textfield_url, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_download)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar_download, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_status, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_downloadActionPerformed
        
        System.err.println("Validating the URL...");
        String inputurl = this.textfield_url.getText();
        URL url = this.validateURL(inputurl);
        if(url != null) {

            Pattern pattern = Pattern.compile("[^/]*$");
            Matcher matcher = pattern.matcher(inputurl);
            
            if(matcher.find()) {
                System.err.println("Found default base filename:  " + matcher.group() );
                filechooser.setSelectedFile(new File(matcher.group() + ".mp4"));
            } else {
                System.err.println("Could not get default filename from:  " + inputurl);
            }
            
            //if they approved file then go for the download
            if( filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
                this.startdownload( url, filechooser.getSelectedFile() );
            }
        }
        
        
    }//GEN-LAST:event_jButton_downloadActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenu MenuItem_About;
    private javax.swing.JMenu MenuItem_File;
    private javax.swing.JMenuItem MenuItem_Preferences;
    private javax.swing.JButton jButton_download;
    private javax.swing.JTextField jLabel_status;
    private javax.swing.JProgressBar jProgressBar_download;
    private javax.swing.JTextField textfield_url;
    // End of variables declaration//GEN-END:variables
}
