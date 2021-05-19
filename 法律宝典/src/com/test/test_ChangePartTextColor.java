package com.test;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test_ChangePartTextColor extends JFrame {
    static int i =0;
    static JTextPane editArea = new JTextPane();
    static int k = 0;
    static int k2 = 0;

    StyledDocument doc = editArea.getStyledDocument();
    public static void main (String[] args){
        System.out.println(editArea.getSelectedText());
        new test_ChangePartTextColor();
    }
    test_ChangePartTextColor(){
        JTextField findinputField = new JTextField();
        JButton btn = new JButton("查找");
        JButton btn1 = new JButton("替换");
        JScrollPane jScrollPane = new JScrollPane(editArea);

        //editArea.setEditable(false);
        insert("哈哈哈哈哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或",Color.black,25,0);
        jScrollPane.setSize(500,1000);
        jScrollPane.setLocation(20,80);
        //editArea.setLineWrap(true);
        //textArea.setSize(jScrollPane.getWidth(),jScrollPane.getHeight());

        findinputField.setSize(300,50);
        findinputField.setLocation(20,20);

        btn.setSize(80,50);
        btn.setLocation(330,20);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str1 = null;
                try {
                    str1 = doc.getText(0,doc.getLength());
                    System.out.println("str1内容:"+str1);
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                String str2 = findinputField.getText();
                System.out.println("str2:"+str2.length());
                //String strA = str1.toUpperCase();
                //String strB = str2.toUpperCase();
                if(!str1.isEmpty() && !str2.isEmpty()) {

                    //Text t = new Text(findText.toString());
                    //System.out.println(t.toString());
                    //t.setFill(Color.rgb(102,204,255));
                    if (editArea.getSelectedText() == null){
                        k = str1.indexOf(str2,editArea.getCaretPosition() + 1);
                    }else{
                        k = str1.indexOf(str2, editArea.getCaretPosition() - findinputField.getText().length() + 1);
                        System.out.println("选中的k:" + k);
                    }
                    if (k > -1) { //String strData=strA.subString(k,strB.getText().length()+1);
                        i++;
                        editArea.setCaretPosition(k);
                        editArea.select(k, k + str2.length());
                        k2 = str2.length();
                        System.out.println(editArea.getSelectedText());
                        editArea.requestFocus();

                    }

                    /*if (downButton.isSelected()) {
                        if (editArea.getSelectedText() == null)
                            k = strA.indexOf(strB, editArea.getCaretPosition() + 1);
                        else
                            k = strA.indexOf(strB, editArea.getCaretPosition() - findText.getText().length() + 1);
                        if (k > -1) { //String strData=strA.subString(k,strB.getText().length()+1);
                            editArea.setCaretPosition(k);
                            editArea.select(k, k + strB.length());
                        } else {
                            JOptionPane.showMessageDialog(null, "找不到您查找的内容！", "查找", JOptionPane.INFORMATION_MESSAGE);
                        }*/
                }
            }
        });

        btn1.setSize(80,50);
        btn1.setLocation(410,20);
        btn1.addActionListener(new ActionListener() {//全部替换
            @Override
            public void actionPerformed(ActionEvent e) {
                editArea.setCaretPosition(0);
                int k = 0;
                String str1 = null;
                try {
                    str1 = doc.getText(0,doc.getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                String str2 = findinputField.getText();
                while (k>-1){
                    if (editArea.getSelectedText() == null){
                        k = str1.indexOf(str2,editArea.getCaretPosition() + 1);
                    }else{
                        k = str1.indexOf(str2, editArea.getCaretPosition() - str2.length() + 1);
                    }
                    if(k > -1){
                        editArea.setCaretPosition(k);
                        editArea.select(k, k + str2.length());
                    }
                    if (k > 0 && editArea.getSelectedText() != null) {
                        change(k,str2.length(),Color.red,25,0);
                    }
                }

            }
        });

        add(jScrollPane);
        add(findinputField);
        add(btn);
        add(btn1);
        setLayout(null);
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
    public void insert(String str,Color textcolor,int textSize,int textAlign){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
        StyleConstants.setFontSize(set,textSize);

        StyleConstants.setAlignment(set,textAlign);

        doc.setParagraphAttributes(editArea.getText().length(),doc.getLength()-editArea.getText().length(),set,false);
        //doc.setParagraphAttributes(6,25,set,false);
        //doc.setCharacterAttributes(i,i+editArea.getText().length(),set,false);
        try{

            doc.insertString(doc.getLength(), str, set);//插入文字
        }
        catch (BadLocationException e)
        {
        }
    }
    public void change(int first,int secondly,Color textcolor,int textSize,int textAlign){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
        StyleConstants.setFontSize(set,textSize);
        StyleConstants.setAlignment(set,textAlign);
        doc.setCharacterAttributes(first,secondly,set,false);
        System.out.println("k:"+first+"k2:"+secondly);
        //doc.setParagraphAttributes(6,25,set,false);
        //doc.setCharacterAttributes(i,i+editArea.getText().length(),set,false);
    }
}
