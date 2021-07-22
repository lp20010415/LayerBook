package com.test;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

public class test_movepicture extends JFrame {
    //进度条定义数值
    int JProgressBarMax = 100;
    int JProgressBarMin = 0;
    int JProgressBarValue = JProgressBarMin;
    //火柴人位置数值
    int pictureLocationX = 25;
    int pictureLocationY = 300;

    Timer t = new Timer();
    JLabel huocairen = new JLabel();
    JLabel jpbDownText = new JLabel("正在进入程序",JLabel.CENTER);
    JLabel leftText = new JLabel();
    JLabel rightText = new JLabel();

    JProgressBar jpb = new JProgressBar();
    Icon i = new ImageIcon("./images/缩小版独火柴人.gif");
    public static void main(String[] args){
        new test_movepicture();
    }
    test_movepicture(){

        //进度条
        jpb.setMaximum(JProgressBarMax);
        jpb.setMinimum(JProgressBarMin);
        jpb.setValue(JProgressBarValue);
        jpb.setSize(300,50);
        jpb.setLocation(pictureLocationX+25,pictureLocationY+i.getIconHeight());
        jpb.setStringPainted(true);
        jpb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });

        //图片
        huocairen.setIcon(i);
        huocairen.setLocation(pictureLocationX,pictureLocationY);
        huocairen.setSize(i.getIconWidth(),i.getIconHeight());
        System.out.println("i的width:"+i.getIconWidth()+"i的height"+i.getIconHeight());
        //进度条下文字
        jpbDownText.setFont(new Font("宋体",1,25));
        jpbDownText.setBounds(jpb.getX(),jpb.getY()+jpb.getHeight(),jpb.getWidth(),jpb.getHeight());

        //火柴人移动时间周期
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(JProgressBarValue<JProgressBarMax){
                    JProgressBarValue+=2;
                    jpb.setValue(JProgressBarValue);
                    pictureLocationX+=6;
                    huocairen.setLocation(pictureLocationX,pictureLocationY);
                    jpbDownText.setText(jpbDownText.getText()+".");
                    if(jpbDownText.getText().equals("正在进入程序........"))   jpbDownText.setText("正在进入程序");
                    System.out.println(pictureLocationX);

                }else if(JProgressBarValue == JProgressBarMax){
                    remove(huocairen);
                    remove(jpb);
                    remove(jpbDownText);
                    repaint();
                    System.out.println("Cool!!!!");
                    t.cancel();
                }
            }
        },0,200);

        add(huocairen);
        add(jpb);
        add(jpbDownText);
        setLayout(null);
        setVisible(true);
        setResizable(true);
        setSize(420,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
