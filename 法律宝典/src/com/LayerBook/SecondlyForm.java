package com.LayerBook;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//���ַ���
public class SecondlyForm extends JFrame implements ActionListener {
    static String dataname = "";
    JButton go;
    JLabel tips = new JLabel("�������ݣ�����������룬лл��",0);

    SecondlyForm(String shujukuming) {
        JPanel goPane = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(goPane);

        int j = 0;//�ж�����
        int x = 51, y = 30;//�̶���ťλ��
        dataname = shujukuming;
        String url = "jdbc:mysql://127.0.0.1:3306/" + dataname + "?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            //��ȡ����
            ResultSet rs = stm.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema='" + dataname + "'");
            Boolean checkrs = rs.next();
            rs.first();rs.previous();
            while (rs.next()) {
                //<-��ť�������
                go = new JButton(rs.getString(1));
                go.setFont(new Font("����", Font.BOLD, 0));//���ð�ť��������
                go.setFocusPainted(false);//������ť������Ŀ��
                ImageIcon i = new ImageIcon("./images/"+ dataname +"/" + rs.getString("table_name") + ".jpg");//���ݱ������谴ťͼƬ
                i.setImage(i.getImage().getScaledInstance(250, 360, 0));//����ͼƬ��С
                go.setIcon(i);
                go.setSize(new Dimension(250, 360));
                //��ť�������->
                //<-ʵ�ְ�ť���в���
                j++;
                if (j > 4) {
                    x = 51;
                    y += go.getHeight() + 30;
                    j -= 4;
                }
                go.setLocation(x, y);
                x += go.getWidth() + 51;
                //ʵ�ְ�ť���в���->
                go.addActionListener(this);
                goPane.setPreferredSize(new Dimension(0,y));
                /*go.addActionListener(new ActionListener() {������Ӽ����¼�Ҳ��
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
        //��ʾ����
        tips.setBounds(250,0,800,300);
        tips.setFont(new Font("����",1,50));
        tips.setForeground(Color.red);

        ///JPanel����
        goPane.setLayout(null);

        ///JScrollPane����
        jScrollPane.setBounds(35, 25, 1260, 650);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//���û����ٶ�

        //����������
        add(jScrollPane);
        setTitle("���ɱ���");
        setLayout(null);
        setBounds(500,250,1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String tablename = e.getActionCommand();//����
        System.out.println(tablename);
        if(!tablename.isEmpty()){
            System.out.println(dataname);
            Action actionMessage = new Action("", "", "��ʾ��Ϣ");
            Action actionForm = new Action(dataname, tablename, "��������");
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
        if (order.equals("��������")){
            this.dataname = dataname;
            this.tablename = tablename;
            this.order = order;
        }else if(order.equals("��ʾ��Ϣ")){
            this.order = order;
        }
    }

    @Override
    public void run() {
        if (order.equals("��������"))
            new ThirdForm(dataname, tablename);
        else if(order.equals("��ʾ��Ϣ"))
            JOptionPane.showMessageDialog(null,"��Ҫ�������ݣ�ȷ������","����",JOptionPane.INFORMATION_MESSAGE);
    }
}

