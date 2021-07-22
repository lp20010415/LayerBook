package com.test;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test_ManyTextPane extends JFrame {
    static JTextPane jTextPane;
    static StyledDocument doc;
    static int x=0,y=0;
    JPanel jPanel = new JPanel();
    JButton jButton = new JButton("测试");
    JScrollPane jScrollPane = new JScrollPane(jPanel);
    public static void main(String[] args) {
        new test_ManyTextPane();
    }
    test_ManyTextPane(){
        jPanel.setLayout(null);

        jButton.setLocation(10,10);
        jButton.setSize(100,80);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextPane = new JTextPane();
                jTextPane.setSize(100,50);
                jTextPane.setLocation(x,y);
                doc = jTextPane.getStyledDocument();
                insertText("哈哈"+y,Color.red,25,0);


                y+=jTextPane.getHeight()+10;
                jPanel.setPreferredSize(new Dimension(1200,y));
                jPanel.add(jTextPane);
            }
        });

        jScrollPane.setLocation(10,100);
        jScrollPane.setSize(1200,500);

        add(jButton);
        add(jScrollPane);
        setLayout(null);
        setBounds(500, 250, 1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
    public static void insertText(String text/*文本内容*/, Color colorName/*颜色*/, int textSize/*字体大小*/, int textAlign/*对齐方式*/) {

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, colorName);//设置文字颜色
        StyleConstants.setFontSize(set, textSize);//设置字体大小
        StyleConstants.setAlignment(set, textAlign);//设置文本对齐方式
        //doc.setParagraphAttributes(textPane.getText().length(), doc.getLength() - textPane.getText().length(), set, false);
        try {
            doc.insertString(doc.getLength(), text, set);//插入文字
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
