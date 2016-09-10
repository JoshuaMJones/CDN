package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by ArO on 9/09/2016.
 */
public class CacheGUI {
    private JPanel mainPanel;
    private JList<String> cacheFileList;
    private JScrollPane cacheFilePane;
    private JScrollPane cacheLogPane;
    private JButton deleteSelectedCacheFileButton;
    private JButton deleteAllCacheFilesButton;
    private JTextPane cacheLog;
    private JButton logRefreshButton;
    private JLabel logLabel;
    private JLabel cacheFileLabel;
    private JButton fileRefreshButton;
    private Cache thisCache;
    private DefaultListModel<String> listModel;
    public String fileDir;
    public DateFormat dateFormat;

    public CacheGUI(Cache cache) {
        thisCache = cache;
        fileDir = thisCache.fileDir;
        JFrame mainFrame = new JFrame("Cache GUI");
        mainFrame.setContentPane(this.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setSize(500, 500);
        cacheLog.setEditable(false);
        dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");

        listModel = new DefaultListModel<>();
        cacheFileList.setModel(listModel);
        deleteSelectedCacheFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                //Delete whichever file is currently selected
                if(cacheFileList.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(null, "No file is selected, please select a file to delete");
                    return;
                }
                int currentIndex = cacheFileList.getSelectedIndex();
                String currentTime = dateFormat.format(Calendar.getInstance().getTime());
                String fileName = thisCache.fileNames.get(currentIndex);
                try{
                    File fileToDelete = new File(fileDir + fileName);
                    //Path filePath = new Path(fileToDelete);

                    Boolean isDeleted = fileToDelete.delete();
                    if(isDeleted){
                        thisCache.cacheLogs.add("Cache operation: Deleted the file " + fileName + " from the cache at: " + currentTime);
                        thisCache.fileNames.remove(currentIndex);
                        listModel.remove(currentIndex);
                    }
                }catch(Exception ex){
                    System.out.println("failed to delete file: " + fileName);
                }

            }
        });
        deleteAllCacheFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Refresh the list first
                listModel.clear();
                thisCache.filesInDir = thisCache.directory.listFiles();
                for(int i=0; i < thisCache.filesInDir.length;i++){
                    listModel.add(i, thisCache.filesInDir[i].getName());
                }
                //delete all cached files

                thisCache.filesInDir = thisCache.directory.listFiles();
                for(File curFile : thisCache.filesInDir){
                    String fileName = curFile.getName();
                    Boolean isDeleted = curFile.delete();
                    if(isDeleted){
                        int curIndex = thisCache.fileNames.indexOf(fileName);
                        listModel.remove(curIndex);
                        thisCache.fileNames.remove(curIndex);
                    }
                }

                //listModel.clear();
                //thisCache.fileNames.clear();
                String currentTime = dateFormat.format(Calendar.getInstance().getTime());
                thisCache.cacheLogs.add("Cache operation: Deleted all files from the cache at: " + currentTime);

            }
        });
        logRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Make sure that all the logs are written into the box
                String paneText = concatString(thisCache.cacheLogs);
                cacheLog.setText(paneText);
            }
        });

        fileRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.clear();
                thisCache.filesInDir = thisCache.directory.listFiles();
                for(int i=0; i < thisCache.filesInDir.length;i++){
                    listModel.add(i, thisCache.filesInDir[i].getName());
                }
            }
        });
    }

    private String concatString(ArrayList<String> list){
        String wholeString = "";

        for(String current : list){
            wholeString += current + "\n";
        }

        return wholeString;
    }
}
