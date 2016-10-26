/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrator on 2016/9/28.
 */
public class First {
    private JPanel NewOrOldPanel;
    private JButton reRunPreviousMRTestingButton;
    private JButton constuctANewMRButton;
    private JTextPane OldTextPane;
    private JTextPane iAmTotallyNewTextPane;



    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("EasyMT");
        frame.setContentPane(new First().NewOrOldPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 300);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
