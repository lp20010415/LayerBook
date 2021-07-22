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
    static String tablename = "民法典";
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
    TreeAndLink(String name/*编*/){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+name);

            List<String> bian = new ArrayList<String>();
            while (rs.next()){
                if(!bian.contains(rs.getString("编"))){
                    //System.out.println("启动分编");
                    bian.add(rs.getString("编"));
                    Treebian = new DefaultMutableTreeNode(rs.getString("编"));
                    root.add(Treebian);
                    new fengbian(rs.getString("编"));

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
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    fengbian(String name){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            //System.out.println(name);
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where 编='"+name+"'");
            List<String> fengbian = new ArrayList<String>();
            while (rs.next()){
                if(!fengbian.contains(rs.getString("分编")) && !rs.getString("分编").equals("null")){
                    //System.out.println("启动章1");
                    fengbian.add(rs.getString("分编"));
                    TreeAndLink.Treefengbian = new DefaultMutableTreeNode(rs.getString("分编"));
                    new zhang(rs.getString("编"),rs.getString("分编"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treefengbian);
                }if(rs.getString("分编").equals("null")){
                    //System.out.println("启动章2");
                    new zhang(rs.getString("编"),rs.getString("分编"));
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
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang(String name1/*编*/,String name2/*分编*/){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where 编='"+name1+"' and 分编='"+name2+"'");
            List<String> zhang = new ArrayList<String>();

            while (rs.next()){
                if(!zhang.contains(rs.getString("章")) && !name2.equals("null")){//在分编下面添加章
                    //System.out.println("我是启动章1");
                    zhang.add(rs.getString("章"));
                    TreeAndLink.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    TreeAndLink.Treefengbian.add(TreeAndLink.Treezhang);
                    new jie(name1,rs.getString("章"));
                    //System.out.println("启动节1");
                }if(!zhang.contains(rs.getString("章")) && name2.equals("null") && !rs.getString("章").equals("null")){//在编下面添加章
                    //System.out.println("我是启动章2");
                    zhang.add(rs.getString("章"));
                    TreeAndLink.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treezhang);
                    new jie(name1,rs.getString("章"));
                    //System.out.println("启动节1");
                }if(rs.getString("章").equals("null")){//没章
                    //System.out.println("我有在的啊啊啊啊啊啊啊啊，哈哈哈哈");
                    //System.out.println("启动节2");
                    new jie(name1,rs.getString("章"));
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
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    jie(String name1/*编*/,String name2/*章*/){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where 编='"+name1+"' and 章='"+name2+"'");
            List<String> jie = new ArrayList<String>();
            while (rs.next()){
                if(!jie.contains(rs.getString("节")) && !name2.equals("null") && !rs.getString("节").equals("null")){//在章下面添加节
                    //System.out.println("我是节1");
                    jie.add(rs.getString("节"));
                    TreeAndLink.Treejie = new DefaultMutableTreeNode(rs.getString("节"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treejie);
                    //System.out.println("启动条1");
                    new tiao(name1,rs.getString("分编"),name2,rs.getString("节"));

                }if(rs.getString("节").equals("null")){//没节
                    //System.out.println("我是节2");
                    //System.out.println("启动条");
                    new tiao(name1,rs.getString("分编"),name2,rs.getString("节"));
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
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    tiao(String name1,/*编*/String name2/*分编*/,String name3/*章*/,String name4/*节*/){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+tablename+" where 编='"+name1+"'and 分编='"+name2+"' and 章='"+name3+"' and 节='"+name4+"'");
            List<String> tiao = new ArrayList<String>();
            //System.out.println(name1);
            //System.out.println(name3);
            while (rs.next()){
                //无分编，无节情况,添加到章下面
                if(!tiao.contains(rs.getString("条")) && name2.equals("null") && name4.equals("null") && !name3.equals("null")){
                    tiao.add(rs.getString("条"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treetiao);
                    //System.out.println(rs.getString("条"));
                    //System.out.println("1");
                }
                //无节情况,添加到章下面
                if(!tiao.contains(rs.getString("条")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")){
                    tiao.add(rs.getString("条"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    TreeAndLink.Treezhang.add(TreeAndLink.Treetiao);
                    //System.out.println("2");
                }
                //无分编情况,添加到节下面
                if(!tiao.contains(rs.getString("条")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("条"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    TreeAndLink.Treejie.add(TreeAndLink.Treetiao);
                    //System.out.println("3");
                }
                //无分编，无章，无节情况
                if(!tiao.contains(rs.getString("条")) && name2.equals("null") && name3.equals("null") && name4.equals("null") ){
                    //System.out.println("不可能啊！！！");
                    tiao.add(rs.getString("条"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    TreeAndLink.Treebian.add(TreeAndLink.Treetiao);
                    //System.out.println("4");
                }
                //都有
                if(!tiao.contains(rs.getString("条")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("条"));
                    TreeAndLink.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    TreeAndLink.Treejie.add(TreeAndLink.Treetiao);

                    //System.out.println("5");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
