package com.test;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//目录树连接数据库；获取数据库内容显示到面板；单击目录树定位大概指定内容位置;
public class test_TreeAndTL_with_PTC extends JFrame {
    static String dbname = "个人";//数据库名
    static String tablename = "民法典";

    static DefaultMutableTreeNode Treebian;
    static DefaultMutableTreeNode Treefengbian;
    static DefaultMutableTreeNode Treezhang;
    static DefaultMutableTreeNode Treejie;
    static DefaultMutableTreeNode Treetiao;

    //内容面板
    static JTextPane textPane = new JTextPane();
    static JScrollPane PaneSP = new JScrollPane(textPane);
    static StyledDocument doc = textPane.getStyledDocument();


    public static void main(String[] args){

        new test_TreeAndTL_with_PTC(tablename);

    }
    //添加编
    test_TreeAndTL_with_PTC(String name/*编*/){
        String url = "jdbc:mysql://127.0.0.1:3306/"+dbname+"?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(tablename);

        insertText(tablename+"\n",Color.black,45,StyleConstants.ALIGN_CENTER);
        textPane.setEditable(false);//设置禁止被编辑
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
                    insertText(rs.getString("编")+"\n",Color.black,40,0);
                    new fengbian1(rs.getString("编"));

                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //实例化树
        JTree jTree = new JTree(root);
        jTree.setShowsRootHandles(true);
        ///树的单击事件
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textPane.setCaretPosition(0);


                int selRow = jTree.getRowForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode check = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                //TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());

                String findText = check.toString();//需查找文本
                String shuruText = null;//文本里的内容
                try {
                    shuruText = doc.getText(0,doc.getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                int k = 0;
                if(!findText.isEmpty() && !shuruText.isEmpty()){

                    //Text t = new Text(findText.toString());
                    //System.out.println(t.toString());
                    //t.setFill(Color.rgb(102,204,255));
                    if (textPane.getSelectedText() == null) {
                        try {
                            k = doc.getText(0,doc.getLength()).indexOf(findText);
                        } catch (BadLocationException badLocationException) {
                            badLocationException.printStackTrace();
                        }
                    }
                    if (k > -1) { //String strData=strA.subString(k,strB.getText().length()+1);
                        textPane.setCaretPosition(k);
                        JScrollBar test =PaneSP.getVerticalScrollBar();
                        test.setValue(k);
                        textPane.select(k,k+findText.length());
                        textPane.requestFocus();
                    }
                }

                if(selRow != -1) {
                    if(e.getClickCount() == 2) {
                        System.out.println(check.toString());
                    }
                }
            }
        });

        JScrollPane jScrollPane = new JScrollPane(jTree);
        jScrollPane.setSize(500,800);
        jScrollPane.setLocation(10,10);


        PaneSP.setSize(1000,800);
        PaneSP.setLocation(jScrollPane.getWidth()+10+jScrollPane.getX(),10);

        add(PaneSP);
        add(jScrollPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200,850);
        setLocation(500,500);
        setLayout(null);
        setVisible(true);
    }
    ///插入文字操作
    public static void insertText(String text/*文本内容*/, Color colorName/*颜色*/ ,int textSize/*字体大小*/,int textAlign/*对齐方式*/){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, colorName);//设置文字颜色
        StyleConstants.setFontSize(set, textSize);//设置字体大小
        StyleConstants.setAlignment(set,textAlign);//设置文本对齐方式
        doc.setParagraphAttributes(textPane.getText().length(),doc.getLength()-textPane.getText().length(),set,false);
        try {
            doc.insertString(doc.getLength(), text, set);//插入文字
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
///分编--字体同一在前者基础上减5
class fengbian1{
    private String dbname = "layer";
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    fengbian1(String name){
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
                    test_TreeAndTL_with_PTC.Treefengbian = new DefaultMutableTreeNode(rs.getString("分编"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treefengbian);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("分编")+"\n",Color.black,35,0);
                    new zhang1(rs.getString("编"),rs.getString("分编"));

                }if(rs.getString("分编").equals("null")){
                    //System.out.println("启动章2");
                    new zhang1(rs.getString("编"),rs.getString("分编"));
                    break;
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///章--上同
class zhang1{
    private String dbname = "layer";
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang1(String name1/*编*/,String name2/*分编*/){
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
                    test_TreeAndTL_with_PTC.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    test_TreeAndTL_with_PTC.Treefengbian.add(test_TreeAndTL_with_PTC.Treezhang);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("章")+"\n",Color.black,30,0);
                    new jie1(name1,rs.getString("章"));

                    //System.out.println("启动节1");
                }if(!zhang.contains(rs.getString("章")) && name2.equals("null") && !rs.getString("章").equals("null")){//在编下面添加章
                    //System.out.println("我是启动章2");
                    zhang.add(rs.getString("章"));
                    test_TreeAndTL_with_PTC.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treezhang);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("章")+"\n",Color.black,30,0);
                    new jie1(name1,rs.getString("章"));

                    //System.out.println("启动节1");
                }if(rs.getString("章").equals("null")){//没章
                    //System.out.println("我有在的啊啊啊啊啊啊啊啊，哈哈哈哈");
                    //System.out.println("启动节2");
                    new jie1(name1,rs.getString("章"));
                    break;
                }
            }
            con.close();stm.close();rs.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///节
class jie1{
    private String dbname = "layer";
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    jie1(String name1/*编*/,String name2/*章*/){
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
                    test_TreeAndTL_with_PTC.Treejie = new DefaultMutableTreeNode(rs.getString("节"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treejie);
                    //System.out.println("启动条1");
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("节")+"\n",Color.black,25,0);
                    new tiao1(name1,rs.getString("分编"),name2,rs.getString("节"));

                }if(rs.getString("节").equals("null")){//没节
                    //System.out.println("我是节2");
                    //System.out.println("启动条");
                    new tiao1(name1,rs.getString("分编"),name2,rs.getString("节"));
                    break;
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///条
class tiao1{
    private String dbname = "layer";
    private String tablename = "民法典";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    tiao1(String name1,/*编*/String name2/*分编*/,String name3/*章*/,String name4/*节*/){
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
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("条")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("内容")+"\n",Color.black,20,0);
                    //System.out.println(rs.getString("条"));
                    //System.out.println("1");
                }
                //无节情况,添加到章下面
                if(!tiao.contains(rs.getString("条")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")){
                    tiao.add(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("条")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("内容")+"\n",Color.black,20,0);
                    //System.out.println("2");
                }
                //无分编情况,添加到节下面
                if(!tiao.contains(rs.getString("条")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treejie.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("      "+rs.getString("条")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("内容")+"\n",Color.black,20,0);
                    //System.out.println("3");
                }
                //无分编，无章，无节情况
                if(!tiao.contains(rs.getString("条")) && name2.equals("null") && name3.equals("null") && name4.equals("null") ){
                    //System.out.println("不可能啊！！！");
                    tiao.add(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("条")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("内容")+"\n",Color.black,20,0);
                    //System.out.println("4");
                }
                //都有
                if(!tiao.contains(rs.getString("条")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    test_TreeAndTL_with_PTC.Treejie.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("      "+rs.getString("条")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("内容")+"\n",Color.black,20,0);

                    //System.out.println("5");
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}