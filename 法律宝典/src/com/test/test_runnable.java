package com.test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class test_runnable extends JFrame {
    static String[] put = new String[2];
    static int num = 0;
    static String[] table = {"t1","t2"};
    static Timer t = new Timer();
    public static void main(String[] args) {
        try{
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try{
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_link?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery("select * from "+table[num]);
                        while (rs.next()){
                            put[num] = rs.getString(1);
                            System.out.println(rs.getString(1));
                            num++;
                        }
                        if(num > 1){
                            t.cancel();
                        }
                        con.close();stm.close();rs.close();
                    }catch (Exception e){

                    }



                }
            },0,1000);
        }catch (Exception f){

        }
        new test_runnable();
    }
    test_runnable(){
        JButton test = new JButton("≤‚ ‘");
        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i =0;i<put.length;i++){
                    System.out.println(put.length);
                    System.out.println(put[i]);
                }
                putdata putdata=new putdata();
                new Thread(putdata,"π˛π˛").start();
            }
        });
        add(test);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(100,100);

    }
}
class putdata implements Runnable{
    putdata(){

    }

    @Override
    public void run() {
        System.out.println("µ⁄"+"¥Œ");
    }
}

