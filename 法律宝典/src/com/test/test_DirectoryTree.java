package com.test;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class test_DirectoryTree extends JFrame{
    int i =6;
    JLabel test;
    public static void main(String[] args){
        new test_DirectoryTree();
    }
    test_DirectoryTree(){
        for(int j =0;j<i;j++){
            test= new JLabel("ÎÒÊÇJLabel"+j);
            System.out.println(test.getText());
            add(test);

        }
        setLayout(new GridLayout(7,1,1,1));
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
