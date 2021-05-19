package com.test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddButtonToJScrollPane extends JFrame {
    JButton addbtn;
    int jpY=500;
    int i,y =0;
    public static void main(String[] args){
        new AddButtonToJScrollPane();
    }
    AddButtonToJScrollPane(){
        JPanel jp = new JPanel();
        JScrollPane jsp = new JScrollPane(jp);

        addbtn = new JButton("Ìí¼Ó°´Å¥");
        addbtn.setBounds(0,0,100,50);
        addbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = new JButton(String.valueOf(i));
                btn.setSize(100,50);
                //btn.setPreferredSize(new Dimension(100,100));
                btn.setLocation(0,y);
                y+=50;
                i++;
                jp.add(btn);
                jp.setPreferredSize(new Dimension(800,y));
                repaint();
                validate();
                doLayout();
            }
        });
        /*for(int i=0;i<6;i++){
            JButton button = new JButton(String.valueOf(i));
            button.setPreferredSize(new Dimension(100,100));
            jp.add(button);
        }*/

        jp.setLayout(null);

        //jp.setSize(500,jpY);
        //jp.setLocation(0,0);

        /*jsp.setSize(800,800);
        jsp.setLocation(0,addbtn.getHeight()+10);*/
        jsp.setBounds(0,addbtn.getHeight()+10,800,800);

        add(addbtn);
        add(jsp);
        setSize(500,500);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
