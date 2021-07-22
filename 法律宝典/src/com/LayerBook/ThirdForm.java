package com.LayerBook;

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
public class ThirdForm extends JFrame {
    //数据库相关
    static String dbname = "";
    static String tablename = "";
    //目录树操作
    static DefaultMutableTreeNode Treebian;
    static DefaultMutableTreeNode Treefengbian;
    static DefaultMutableTreeNode Treezhang;
    static DefaultMutableTreeNode Treejie;
    static DefaultMutableTreeNode Treetiao;
    DefaultMutableTreeNode root;
    //内容面板
    static JTextPane textPane = new JTextPane();
    static JScrollPane panetext = new JScrollPane(textPane);
    static StyledDocument doc = textPane.getStyledDocument();
    //查询
    static JTextField search = new JTextField();
    static JButton searchUp = new JButton("向上搜");
    static JButton searchDown = new JButton("向下搜");

    //添加编
    ThirdForm(String name1/*数据库名*/, String name2/*表名*/) {
        textPane.setText("");//将textPane清空，以免多窗口重复文本。
        dbname = name1;//数据库名
        tablename = name2;//表名
        /*数据连接地址*/
        String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        root = new DefaultMutableTreeNode(tablename);//创建主树

        insertText(tablename + "\n", Color.black, 45, StyleConstants.ALIGN_CENTER);//插入文本
        textPane.setEditable(false);//设置禁止被编辑
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from `" + tablename + "`");
            Boolean checkrs = rs.next();
            List<String> bian = new ArrayList<String>();
            while (rs.next()) {//有内容执行下面命令
                if (!bian.contains(rs.getString("编"))) {
                    //System.out.println("启动分编");
                    bian.add(rs.getString("编"));
                    Treebian = new DefaultMutableTreeNode(rs.getString("编"));
                    root.add(Treebian);
                    insertText(rs.getString("编") + "\n", Color.black, 40, 0);
                    new fengbian1(rs.getString("编"));

                }
            }
            if(!checkrs) {//没内容执行下面命令
                insertText("还未添加内容，敬请期待！", Color.red, 50, 1);
            }
            con.close();
            stm.close();
            rs.close();
        } catch (Exception e) {
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

                String text = check.toString();//需查找文本
                String shuruText = null;//文本里的内容
                try {
                    shuruText = doc.getText(0, doc.getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                int k = 0;
                if (!text.isEmpty() && !shuruText.isEmpty()) {
                    if (textPane.getSelectedText() == null) {
                        k = shuruText.indexOf(text);
                    }
                    if (k > -1) { //String strData=strA.subString(k,strB.getText().length()+1);
                        textPane.setCaretPosition(k);
                        JScrollBar test = panetext.getVerticalScrollBar();
                        test.setValue(k);
                        textPane.select(k, k + text.length());
                        textPane.requestFocus();
                    }
                }

                if (selRow != -1) {
                    if (e.getClickCount() == 2) {
                        System.out.println(check.toString());
                    }
                }
            }
        });
        //目录树相关设置
        JScrollPane jScrollPane = new JScrollPane(jTree);
        jScrollPane.setSize(300, 680);
        jScrollPane.setLocation(10, 10);

        //查询框相关设置
        search.setSize(820, 70);
        search.setFont(new Font("宋体", 1, 35));
        search.setLocation(jScrollPane.getWidth() + 20, 10);
        ///按钮
        //向上
        searchUp.setSize(80, 70);
        searchUp.setLocation(search.getX() + search.getWidth() + 10, 10);
        //向下
        searchDown.setSize(80, 70);
        searchDown.setLocation(searchUp.getX() + searchUp.getWidth() + 10, 10);
        ////向上搜事件
        searchUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = search.getText();
                String paneText = "";
                try {
                    paneText = doc.getText(0, doc.getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                int k = 0;
                if (textPane.getSelectedText() == null) {
                    k = paneText.lastIndexOf(searchText, textPane.getCaretPosition() - 1);
                } else {
                    k = paneText.lastIndexOf(searchText, textPane.getCaretPosition() - textPane.getSelectedText().length() - 1);

                }
                if (k > -1) {
                    textPane.setCaretPosition(k);
                    textPane.select(k, k + searchText.length());
                    textPane.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "找不到您查找的内容！", "查找", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        ////向下搜事件
        searchDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = search.getText();
                String paneText = "";
                try {
                    paneText = doc.getText(0, doc.getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                int k = 0;
                if (search.getSelectedText() == null) {
                    k = paneText.indexOf(searchText, textPane.getCaretPosition() + 1);
                } else {
                    k = paneText.indexOf(searchText, textPane.getCaretPosition() - searchText.length() + 1);
                }
                if (k > -1) {
                    textPane.setCaretPosition(k);
                    textPane.select(k, k + searchText.length());
                    textPane.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "找不到查找的内容？", "查找", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //内容面板相关设置
        panetext.setSize(1000, 600);
        panetext.setLocation(jScrollPane.getWidth() + 20, search.getHeight() + 20);

        add(search);
        add(searchUp);
        add(searchDown);
        add(panetext);
        add(jScrollPane);
        setTitle("法律宝典-"+name2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500,250,1350, 750);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        textPane.setCaretPosition(0);//置顶
    }

    ///插入文字操作
    public static void insertText(String text/*文本内容*/, Color colorName/*颜色*/, int textSize/*字体大小*/, int textAlign/*对齐方式*/) {

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, colorName);//设置文字颜色
        StyleConstants.setFontSize(set, textSize);//设置字体大小
        StyleConstants.setAlignment(set, textAlign);//设置文本对齐方式
        doc.setParagraphAttributes(textPane.getText().length(), doc.getLength() - textPane.getText().length(), set, false);
        try {
            doc.insertString(doc.getLength(), text, set);//插入文字
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

class GetData{

}

///分编--字体同一在前者基础上减5
class fengbian1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    fengbian1(String name) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            //System.out.println(name);
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where 编='" + name + "'");
            List<String> fengbian = new ArrayList<String>();
            while (rs.next()) {
                if (!fengbian.contains(rs.getString("分编")) && !rs.getString("分编").equals("null")) {
                    //System.out.println("启动章1");
                    fengbian.add(rs.getString("分编"));
                    ThirdForm.Treefengbian = new DefaultMutableTreeNode(rs.getString("分编"));
                    ThirdForm.Treebian.add(ThirdForm.Treefengbian);
                    ThirdForm.insertText(rs.getString("分编") + "\n", Color.black, 35, 0);
                    new zhang1(rs.getString("编"), rs.getString("分编"));

                }
                if (rs.getString("分编").equals("null")) {
                    //System.out.println("启动章2");
                    new zhang1(rs.getString("编"), rs.getString("分编"));
                    break;
                }
            }
            con.close();
            stm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

///章--上同
class zhang1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang1(String name1/*编*/, String name2/*分编*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where 编='" + name1 + "' and 分编='" + name2 + "'");
            List<String> zhang = new ArrayList<String>();

            while (rs.next()) {
                if (!zhang.contains(rs.getString("章")) && !name2.equals("null")) {//在分编下面添加章
                    //System.out.println("我是启动章1");
                    zhang.add(rs.getString("章"));
                    ThirdForm.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    ThirdForm.Treefengbian.add(ThirdForm.Treezhang);
                    ThirdForm.insertText("  " + rs.getString("章") + "\n", Color.black, 30, 0);
                    new jie1(name1, rs.getString("章"));

                    //System.out.println("启动节1");
                }
                if (!zhang.contains(rs.getString("章")) && name2.equals("null") && !rs.getString("章").equals("null")) {//在编下面添加章
                    //System.out.println("我是启动章2");
                    zhang.add(rs.getString("章"));
                    ThirdForm.Treezhang = new DefaultMutableTreeNode(rs.getString("章"));
                    ThirdForm.Treebian.add(ThirdForm.Treezhang);
                    ThirdForm.insertText("  " + rs.getString("章") + "\n", Color.black, 30, 0);
                    new jie1(name1, rs.getString("章"));

                    //System.out.println("启动节1");
                }
                if (rs.getString("章").equals("null")) {//没章
                    //System.out.println("我有在的啊啊啊啊啊啊啊啊，哈哈哈哈");
                    //System.out.println("启动节2");
                    new jie1(name1, rs.getString("章"));
                    break;
                }
            }
            con.close();
            stm.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

///节
class jie1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    jie1(String name1/*编*/, String name2/*章*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where 编='" + name1 + "' and 章='" + name2 + "'");
            List<String> jie = new ArrayList<String>();
            while (rs.next()) {
                if (!jie.contains(rs.getString("节")) && !name2.equals("null") && !rs.getString("节").equals("null")) {//在章下面添加节
                    //System.out.println("我是节1");
                    jie.add(rs.getString("节"));
                    ThirdForm.Treejie = new DefaultMutableTreeNode(rs.getString("节"));
                    ThirdForm.Treezhang.add(ThirdForm.Treejie);
                    //System.out.println("启动条1");
                    ThirdForm.insertText("    " + rs.getString("节") + "\n", Color.black, 25, 0);
                    new tiao1(name1, rs.getString("分编"), name2, rs.getString("节"));

                }
                if (rs.getString("节").equals("null")) {//没节
                    //System.out.println("我是节2");
                    //System.out.println("启动条");
                    new tiao1(name1, rs.getString("分编"), name2, rs.getString("节"));
                    break;
                }
            }
            con.close();
            stm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

///条
class tiao1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    tiao1(String name1,/*编*/String name2/*分编*/, String name3/*章*/, String name4/*节*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where 编='" + name1 + "'and 分编='" + name2 + "' and 章='" + name3 + "' and 节='" + name4 + "'");
            List<String> tiao = new ArrayList<String>();
            //System.out.println(name1);
            //System.out.println(name3);
            while (rs.next()) {
                //无分编，无节情况,添加到章下面
                if (!tiao.contains(rs.getString("条")) && name2.equals("null") && name4.equals("null") && !name3.equals("null")) {
                    tiao.add(rs.getString("条"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    ThirdForm.Treezhang.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("    " + rs.getString("条") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("内容") + "\n", Color.black, 20, 0);
                    //System.out.println(rs.getString("条"));
                    //System.out.println("1");
                }
                //无节情况,添加到章下面
                if (!tiao.contains(rs.getString("条")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")) {
                    tiao.add(rs.getString("条"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    ThirdForm.Treezhang.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("    " + rs.getString("条") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("内容") + "\n", Color.black, 20, 0);
                    //System.out.println("2");
                }
                //无分编情况,添加到节下面
                if (!tiao.contains(rs.getString("条")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")) {
                    tiao.add(rs.getString("条"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    ThirdForm.Treejie.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("      " + rs.getString("条") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("内容") + "\n", Color.black, 20, 0);
                    //System.out.println("3");
                }
                //无分编，无章，无节情况
                if (!tiao.contains(rs.getString("条")) && name2.equals("null") && name3.equals("null") && name4.equals("null")) {
                    //System.out.println("不可能啊！！！");
                    tiao.add(rs.getString("条"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    ThirdForm.Treebian.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("  " + rs.getString("条") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("内容") + "\n", Color.black, 20, 0);
                    //System.out.println("4");
                }
                //都有
                if (!tiao.contains(rs.getString("条")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")) {
                    tiao.add(rs.getString("条"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("条"));
                    ThirdForm.Treejie.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("      " + rs.getString("条") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("内容") + "\n", Color.black, 20, 0);

                    //System.out.println("5");
                }
            }
            con.close();
            stm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
