//File:		MainDialog.java
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
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.Timer;





public class MainDialog extends javax.swing.JDialog {
    
    //my variables
    FileNameExtensionFilter filter;
    String path = null;
    //should be true when downloading
    private boolean downloading = false;
    
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
    
    //attempted close
    public boolean attemptwindowclose() {
        
        if(downloading) {
            
            if(JOptionPane.showConfirmDialog(this, "Cancel download?", "Cancel Download", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                System.err.println("Interrupting download thread...");
                downloader.interrupt();
                
                this.downloadFinished(DownloadProgress.STATUS_CANCELED);
                
                return false;
                
            } else {
                return false;
            }
            
        } else {
            return true;
        }
        
        
        
    }
    
    //initialize my stuff
    public void myinit() {
        
        filter = new FileNameExtensionFilter("MP4 Video", "mp4");
        
        
        this.progress = new DownloadProgress();
        
        this.setTitle("La HOR Video Downloader");
        
        
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
    
    //called when download stops or is finished
    private void downloadFinished(int finalstatus) {
        
        //join the downloaded thread
        
        try {
            downloader.join();  //join the downloader
            System.err.println("Joined download thread.");
        } catch (InterruptedException ex) {
            System.err.println("Error:  Could not join download thread!!!");
        }
        
        if(finalstatus == DownloadProgress.STATUS_CANCELED || finalstatus == DownloadProgress.STATUS_ERROR || finalstatus == DownloadProgress.STATUS_PERROR) {
            System.err.println("Downloaded thread ended with error, deleteing the file:  " + this.savefile);
            this.savefile.delete();
        }
        
        downloader = null;  //let the garbage collector take it away
        
        jButton_download.setEnabled(true);
        textfield_url.setEnabled(true);
        jButton_close.setText("Close");
        this.downloading = false;
        
    }
    
    //setup the download process
    private void startdownload(URL url, File savefile)  {
        
        this.downloading = true;
        jButton_close.setText("Cancel");
        jButton_download.setEnabled(false);
        textfield_url.setEnabled(false);
        
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
                                downloadFinished(this.status);
                                break;

                            case DownloadProgress.STATUS_FINISHED:
                                jLabel_status.setText("Finished!");
                                downloadFinished(this.status);
                                break;

                            case DownloadProgress.STATUS_PERROR:
                                jLabel_status.setText("Error parsing video URL!");
                                downloadFinished(this.status);
                                break;

                            case DownloadProgress.STATUS_STARTED:
                                jLabel_status.setText("Downloading...");
                                break;

                            case DownloadProgress.STATUS_RETREIVING:
                                jLabel_status.setText("Retreiving download URL from website...");
                                break;
                                
                            case DownloadProgress.STATUS_CANCELED:
                                jLabel_status.setText("Download canceled!");
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
            System.err.printf("An error occured while trying to open \"%1%s\":  %2$s", this.savefile, ex);
        }
        
        downloader = new DownloaderThread(url, outfile, progress);
        
        System.err.println("Download thread starting...");
        downloader.start();
        
        
    }
    
    /**
     * Creates new form MainDialog
     */
    public MainDialog(java.awt.Frame parent, boolean modal) {
        //super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        super(parent, modal);
        initComponents();
        this.myinit();
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
        jButton_close = new javax.swing.JButton();
        jLabel_url = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        MenuItem_About = new javax.swing.JMenu();
        jMenuItem_help = new javax.swing.JMenuItem();
        jMenuItem_AboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        textfield_url.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textfield_urlActionPerformed(evt);
            }
        });

        jButton_download.setText("Download");
        jButton_download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_downloadActionPerformed(evt);
            }
        });

        jLabel_status.setEditable(false);

        jButton_close.setText("Close");
        jButton_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_closeActionPerformed(evt);
            }
        });

        jLabel_url.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_url.setLabelFor(textfield_url);
        jLabel_url.setText("URL:");

        MenuItem_About.setMnemonic('A');
        MenuItem_About.setText("About");

        jMenuItem_help.setMnemonic('H');
        jMenuItem_help.setText("Help");
        jMenuItem_help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_helpActionPerformed(evt);
            }
        });
        MenuItem_About.add(jMenuItem_help);

        jMenuItem_AboutItem.setMnemonic('A');
        jMenuItem_AboutItem.setText("About");
        jMenuItem_AboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AboutItemActionPerformed(evt);
            }
        });
        MenuItem_About.add(jMenuItem_AboutItem);

        MenuBar.add(MenuItem_About);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_download, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_status)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_url, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textfield_url))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBar_download, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_close)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfield_url, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_url))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_download)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_status, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar_download, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_close))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_downloadActionPerformed
        
        JFileChooser filechooser = null;
        
        if(this.savefile != null) {
            filechooser = new JFileChooser(this.savefile.getParent());
        } else {
            filechooser = new JFileChooser();
        }
        
        
        filechooser.setFileFilter(filter);
        
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
                
                this.savefile = filechooser.getSelectedFile();
                System.err.println("New default file location:  " + this.savefile);
                
                if(this.savefile.exists()) {
                    if(JOptionPane.showConfirmDialog(this, "The file \"" + filechooser.getSelectedFile().toString() + "\" already exists!  Do you want to overwrite it?", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION) != JOptionPane.YES_OPTION) {
                        System.err.println("File download canceled!");
                        return;
                    } else {
                        System.err.println("User choose to overwrite file!");
                    }
                }
                    
                this.startdownload( url, this.savefile );
            }
        }
        
        
    }//GEN-LAST:event_jButton_downloadActionPerformed

    //catch the closing event and attempt to close
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(this.attemptwindowclose()) {
            System.err.println("Closing window.");
            this.dispose();
            System.exit(0);
            
        } else {
            System.err.println("Window close prevented.");
        }
            
    }//GEN-LAST:event_formWindowClosing

    private void jButton_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_closeActionPerformed
        if(this.attemptwindowclose()) { //attempt co close the window
            this.dispose();
            System.exit(0);
        }
    }//GEN-LAST:event_jButton_closeActionPerformed

    private void jMenuItem_AboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AboutItemActionPerformed
        System.err.println("Opening about dialog...");
        AboutDialog aboutdialog = new AboutDialog(null, true);
        aboutdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        aboutdialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_AboutItemActionPerformed

    private void jMenuItem_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_helpActionPerformed
        System.err.println("Opening help dialog"); 
        HelpDialog helpdialog = new HelpDialog(null, true);
        helpdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        helpdialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_helpActionPerformed

    private void textfield_urlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textfield_urlActionPerformed
        this.jButton_downloadActionPerformed(evt);
    }//GEN-LAST:event_textfield_urlActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenu MenuItem_About;
    private javax.swing.JButton jButton_close;
    private javax.swing.JButton jButton_download;
    private javax.swing.JTextField jLabel_status;
    private javax.swing.JLabel jLabel_url;
    private javax.swing.JMenuItem jMenuItem_AboutItem;
    private javax.swing.JMenuItem jMenuItem_help;
    private javax.swing.JProgressBar jProgressBar_download;
    private javax.swing.JTextField textfield_url;
    // End of variables declaration//GEN-END:variables
}
