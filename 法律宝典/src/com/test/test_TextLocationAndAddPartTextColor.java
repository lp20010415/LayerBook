package com.test;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test_TextLocationAndAddPartTextColor extends JFrame {
    static int i =0;
    static JTextPane editArea = new JTextPane();
    static int k = 0;
    static int k2 = 0;

    StyledDocument doc = editArea.getStyledDocument();
    public static void main (String[] args){
        System.out.println(editArea.getSelectedText());
        new test_TextLocationAndAddPartTextColor();
    }
    test_TextLocationAndAddPartTextColor(){
        JTextField findinputField = new JTextField();
        JButton btn = new JButton("????");
        JButton btn1 = new JButton("?滻");
        JScrollPane jScrollPane = new JScrollPane(editArea);

        //editArea.setEditable(false);

        insert("????\n",new Color(102,204,205),20,StyleConstants.ALIGN_CENTER);

        insert("????\n",Color.red,20,0);
        insert("????ţ",Color.red,20,0);

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
                editArea.setCaretPosition(0);

                String str1 = null;
                try {
                    str1 = doc.getText(0,doc.getLength());
                    System.out.println("str1????:"+str1);
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
                        System.out.println("editText.getLenght:"+str1.length());
                        try {
                            k = doc.getText(0,doc.getLength()).indexOf(str2);
                        } catch (BadLocationException badLocationException) {
                            badLocationException.printStackTrace();
                        }
                        System.out.println("editArea.getCaretPosition():"+editArea.getCaretPosition()+"-û??ѡ?е?k:" + k);
                    }else{
                        k = str1.indexOf(str2, editArea.getCaretPosition() - findinputField.getText().length() + 1);
                        System.out.println("ѡ?е?k:" + k);
                    }
                    if (k > -1) { //String strData=strA.subString(k,strB.getText().length()+1);
                        i++;
                        editArea.setCaretPosition(k);
                        editArea.select(k, k + str2.length());
                        k2 = k+str2.length();
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
                            JOptionPane.showMessageDialog(null, "?Ҳ????????ҵ????ݣ?", "????", JOptionPane.INFORMATION_MESSAGE);
                        }*/
                }
            }
        });

        btn1.setSize(80,50);
        btn1.setLocation(410,20);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //editArea.getSelectedText();
                String changeText = findinputField.getText();
                change(Color.black,35,0);
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

            doc.insertString(doc.getLength(), str, set);//????????
        }
        catch (BadLocationException e)
        {
        }
    }
    public void change(Color textcolor,int textSize,int textAlign){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
        StyleConstants.setFontSize(set,textSize);
        StyleConstants.setAlignment(set,textAlign);
        doc.setCharacterAttributes(k,k2,set,false);
        System.out.println("k:"+k+"k2:"+k2);
        //doc.setParagraphAttributes(6,25,set,false);
        //doc.setCharacterAttributes(i,i+editArea.getText().length(),set,false);
    }
}
