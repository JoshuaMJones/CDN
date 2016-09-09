package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ArO on 9/09/2016.
 */
public class CacheGUI {
    private JPanel mainPanel;
    private JList<String> cacheFileList;
    private JScrollPane cacheFilePane;
    private JScrollPane CacheLogPane;
    private JButton deleteSelectedCacheFileButton;
    private JButton deleteAllCacheFilesButton;
    private JTextPane cacheLog;
    private JButton logRefreshButton;
    private Cache thisCache;
    private DefaultListModel<String> listModel;

    public CacheGUI(Cache cache) {
        thisCache = cache;
        JFrame mainFrame = new JFrame("Cache GUI");
        mainFrame.setContentPane(this.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setSize(450, 300);
        cacheLog.setEditable(false);

        listModel = new DefaultListModel<>();
        cacheFileList.setModel(listModel);
        deleteSelectedCacheFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Delete whichever file is currently selected
            }
        });
        deleteAllCacheFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delete all cached files
            }
        });
        logRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Make sure that all the logs are written into the box
            }
        });
    }
}
