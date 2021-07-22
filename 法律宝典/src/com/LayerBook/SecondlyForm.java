package com.LayerBook;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//部分法律
public class SecondlyForm extends JFrame implements ActionListener {
    static String dataname = "";
    JButton go;
    JLabel tips = new JLabel("暂无内容，后续将会加入，谢谢！",0);

    SecondlyForm(String shujukuming) {
        JPanel goPane = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(goPane);

        int j = 0;//判断行数
        int x = 51, y = 30;//固定按钮位置
        dataname = shujukuming;
        String url = "jdbc:mysql://127.0.0.1:3306/" + dataname + "?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            //获取表名
            ResultSet rs = stm.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema='" + dataname + "'");
            Boolean checkrs = rs.next();
            rs.first();rs.previous();
            while (rs.next()) {
                //<-按钮相关设置
                go = new JButton(rs.getString(1));
                go.setFont(new Font("宋体", Font.BOLD, 0));//设置按钮文字隐藏
                go.setFocusPainted(false);//消除按钮字体外的框框
                ImageIcon i = new ImageIcon("./images/"+ dataname +"/" + rs.getString("table_name") + ".jpg");//根据表名给予按钮图片
                i.setImage(i.getImage().getScaledInstance(250, 360, 0));//控制图片大小
                go.setIcon(i);
                go.setSize(new Dimension(250, 360));
                //按钮相关设置->
                //<-实现按钮换行操作
                j++;
                if (j > 4) {
                    x = 51;
                    y += go.getHeight() + 30;
                    j -= 4;
                }
                go.setLocation(x, y);
                x += go.getWidth() + 51;
                //实现按钮换行操作->
                go.addActionListener(this);
                goPane.setPreferredSize(new Dimension(0,y));
                /*go.addActionListener(new ActionListener() {这样添加监听事件也行
                @Override public void actionPerformed(ActionEvent e) {
                String tablename = e.getActionCommand();//&#x8868;&#x540D;
                System.out.println(tablename);
                }
                });*/
                goPane.add(go);
            }
            if(!checkrs){
                goPane.add(tips);
            }
            con.close();stm.close();rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //提示设置
        tips.setBounds(250,0,800,300);
        tips.setFont(new Font("宋体",1,50));
        tips.setForeground(Color.red);

        ///JPanel设置
        goPane.setLayout(null);

        ///JScrollPane设置
        jScrollPane.setBounds(35, 25, 1260, 650);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//设置滑块速度

        //主窗口设置
        add(jScrollPane);
        setTitle("法律宝典");
        setLayout(null);
        setBounds(500,250,1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String tablename = e.getActionCommand();//表名
        System.out.println(tablename);
        if(!tablename.isEmpty()){
            System.out.println(dataname);
            Action actionMessage = new Action("", "", "显示信息");
            Action actionForm = new Action(dataname, tablename, "启动窗口");
            new Thread(actionForm).start();
            new Thread(actionMessage).start();
        }
    }
}

class Action implements Runnable{
    private String dataname;
    private String tablename;
    private String order;
    public Action(String dataname, String tablename, String order){
        if (order.equals("启动窗口")){
            this.dataname = dataname;
            this.tablename = tablename;
            this.order = order;
        }else if(order.equals("显示信息")){
            this.order = order;
        }
    }

    @Override
    public void run() {
        if (order.equals("启动窗口"))
            new ThirdForm(dataname, tablename);
        else if(order.equals("显示信息"))
            JOptionPane.showMessageDialog(null,"将要加载数据，确定继续","加载",JOptionPane.INFORMATION_MESSAGE);
    }
}

