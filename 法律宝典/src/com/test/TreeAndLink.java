package com.test;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TreeAndLink extends JFrame {
    static int cishu = 0;
    private String dbname = "layer";
    static String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(tablename);
    static DefaultMutableTreeNode Treebian;
    static DefaultMutableTreeNode Treefengbian;
    static DefaultMutableTreeNode Treezhang;
    static DefaultMutableTreeNode Treejie;
    static DefaultMutableTreeNode Treetiao;


    public static void main(String[] args){
        new TreeAndLink(tablename);

    }
    TreeAndLink(String name/*��*/){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+name);

            List<String> bian = new ArrayList<String>();
            while (rs.next()){
                if(!bian.contains(rs.getString("��"))){
                    //System.out.println("�����ֱ�");
                    bian.add(rs.getString("��"));
                    Treebian = new DefaultMutableTreeNode(rs.getString("��"));
                    root.add(Treebian);
                    new fengbian(rs.getString("��"));

                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }

        JTree jTree = new JTree(root);
        jTree.setShowsRootHandles(true);

        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = jTree.getRowForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode check = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                //TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 2) {
                        System.out.println(check.toString());
                    }
                }
            }
        });

        JScrollPane jScrollPane = new JScrollPane(jTree);
        jScrollPane.setSize(500,1000);
        jScrollPane.setLocation(0,0);

        add(jScrollPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocation(500,500);
        setLayout(null);
        setVisible(true);


    }

}
class fengbian{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    fengbian(String name){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            //System.out.println(name);
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where ��='"+name+"'");
            List<String> fengbian = new ArrayList<String>();
            while (rs.next()){
                if(!fengbian.contains(rs.getString("�ֱ�")) && !rs.getString("�ֱ�").equals("null")){
                    //System.out.println("������1");
                    fengbian.add(rs.getString("�ֱ�"));
                    TreeAndLink.Treefengbian = new DefaultMutableTreeNode(rs.getString("�ֱ�"));
                    new zhang(rs.getString("��"),rs.getString("�ֱ�"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treefengbian);
                }if(rs.getString("�ֱ�").equals("null")){
                    //System.out.println("������2");
                    new zhang(rs.getString("��"),rs.getString("�ֱ�"));
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class zhang{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang(String name1/*��*/,String name2/*�ֱ�*/){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where ��='"+name1+"' and �ֱ�='"+name2+"'");
            List<String> zhang = new ArrayList<String>();

            while (rs.next()){
                if(!zhang.contains(rs.getString("��")) && !name2.equals("null")){//�ڷֱ����������
                    //System.out.println("����������1");
                    zhang.add(rs.getString("��"));
                    TreeAndLink.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treefengbian.add(TreeAndLink.Treezhang);
                    new jie(name1,rs.getString("��"));
                    //System.out.println("������1");
                }if(!zhang.contains(rs.getString("��")) && name2.equals("null") && !rs.getString("��").equals("null")){//�ڱ����������
                    //System.out.println("����������2");
                    zhang.add(rs.getString("��"));
                    TreeAndLink.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treezhang);
                    new jie(name1,rs.getString("��"));
                    //System.out.println("������1");
                }if(rs.getString("��").equals("null")){//û��
                    //System.out.println("�����ڵİ�������������������������");
                    //System.out.println("������2");
                    new jie(name1,rs.getString("��"));
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class jie{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    jie(String name1/*��*/,String name2/*��*/){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where ��='"+name1+"' and ��='"+name2+"'");
            List<String> jie = new ArrayList<String>();
            while (rs.next()){
                if(!jie.contains(rs.getString("��")) && !name2.equals("null") && !rs.getString("��").equals("null")){//����������ӽ�
                    //System.out.println("���ǽ�1");
                    jie.add(rs.getString("��"));
                    TreeAndLink.Treejie = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treejie);
                    //System.out.println("������1");
                    new tiao(name1,rs.getString("�ֱ�"),name2,rs.getString("��"));

                }if(rs.getString("��").equals("null")){//û��
                    //System.out.println("���ǽ�2");
                    //System.out.println("������");
                    new tiao(name1,rs.getString("�ֱ�"),name2,rs.getString("��"));
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class tiao{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    tiao(String name1,/*��*/String name2/*�ֱ�*/,String name3/*��*/,String name4/*��*/){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where ��='"+name1+"'and �ֱ�='"+name2+"' and ��='"+name3+"' and ��='"+name4+"'");
            List<String> tiao = new ArrayList<String>();
            //System.out.println(name1);
            //System.out.println(name3);
            while (rs.next()){
                //�޷ֱ࣬�޽����,��ӵ�������
                if(!tiao.contains(rs.getString("��")) && name2.equals("null") && name4.equals("null") && !name3.equals("null")){
                    tiao.add(rs.getString("��"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treetiao);
                    //System.out.println(rs.getString("��"));
                    //System.out.println("1");
                }
                //�޽����,��ӵ�������
                if(!tiao.contains(rs.getString("��")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")){
                    tiao.add(rs.getString("��"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treetiao);
                    //System.out.println("2");
                }
                //�޷ֱ����,��ӵ�������
                if(!tiao.contains(rs.getString("��")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("��"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treejie.add(TreeAndLink.Treetiao);
                    //System.out.println("3");
                }
                //�޷ֱ࣬���£��޽����
                if(!tiao.contains(rs.getString("��")) && name2.equals("null") && name3.equals("null") && name4.equals("null") ){
                    //System.out.println("�����ܰ�������");
                    tiao.add(rs.getString("��"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treetiao);
                    //System.out.println("4");
                }
                //����
                if(!tiao.contains(rs.getString("��")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("��"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    TreeAndLink.Treejie.add(TreeAndLink.Treetiao);

                    //System.out.println("5");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
