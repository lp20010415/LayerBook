package com.LayerBook;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchForm extends JFrame {
    //本地缓存
    static String[][] PersonData;
    static String[][] CountryData;
    static String[][] NaturalData;
    static String[][] FoodSafeData;
    static String[][] BusinessData;
    //检查是否点击搜索
    boolean checkBtn = false;
    //滑块移动的距离
    static int moveSlider = 0;
    //控制搜索内容显示框位置
    static int oldJTextPaneY = 0;//之前最底端位置;
    static int jTextPaneY = 10;
    //获取条数
    static int startRange = 0;
    static int endRange = 4;
    //表名数组
    static String[] Person = null;
    static String[] Country = null;
    static String[] Natural = null;
    static String[] FoodSafe = null;
    static String[] Business = null;
    //固定类型数组
    static String[] layerStyle = {"个人", "国家", "自然", "食品安全", "交易"};
    //文字插入
    static StyledDocument doc;

    ///操作界面组件初始化
    static JButton jButton = new JButton("检索");//按钮
    static ImageIcon iButton = new ImageIcon("./images/搜索search.png");//按钮图片
    static JTextField jTextField = new JTextField();//搜索框
    static JTextPane jTextPane;//搜索内容显示框
    static JPanel jPanel = new JPanel();//内容框-two
    static JScrollPane jScrollPane = new JScrollPane(jPanel);//内容框-one

    SearchForm() {
        ///操作界面组件相关设置
        //搜索框
        jTextField.setBounds(10, 10, 1150, 50);
        //按钮
        jButton.setBounds(jTextField.getX() + jTextField.getWidth() + 10, jTextField.getY(), 150, 50);
        jButton.setFont(new Font("宋体", 1, 20));
        jButton.setIcon(iButton);
        /*搜索按钮单击事件*/
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBtn = true;
                jPanel.removeAll();
                jTextPaneY = 10;
                oldJTextPaneY = 0;
                String text = jTextField.getText();
                new Thread(new searchNow(text), "正式搜索").start();
                repaint();
                validate();
            }
        });
        /*按钮图片设置*/
        iButton.setImage(iButton.getImage().getScaledInstance(30, 30, 0));
        //内容框-one
        jScrollPane.setBounds(10, jTextField.getHeight() + 20, 1310, 620);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//设置鼠标滚动滑块速度
        /*滚动条事件*/
        JScrollBar bar = jScrollPane.getVerticalScrollBar();
        bar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if(checkBtn){
                    moveSlider = bar.getMaximum()-bar.getVisibleAmount();
                    String inputText = jTextField.getText();
                    if(bar.getValue() == moveSlider){
                        startRange += 5;
                        endRange += 5;
                        new Thread(new searchNow(inputText), "滑块搜索").start();
                    }
                }
            }
        });
        /*内容框-two*/
        jPanel.setLayout(null);
        add(jTextField);
        add(jButton);
        add(jScrollPane);
        setTitle("检索所有法律");
        setLayout(null);
        setBounds(500, 250, 1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        new Thread(new getTableName(), "获取表名").start();
    }

    //给检索显示框插入文字
    public static void insert(String str, Color textcolor, int textSize, int textAlign) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);//文字颜色
        StyleConstants.setFontSize(set, textSize);//文字大小
        StyleConstants.setAlignment(set, textAlign);//文字对齐方式
        doc.setParagraphAttributes(jTextPane.getText().length(), doc.getLength() - jTextPane.getText().length(), set, false);
        try {
            doc.insertString(doc.getLength(), str, set);//插入文字
        } catch (BadLocationException e) {
        }
    }

    //圈出关键词
    public static void change(int first, int secondly, Color textcolor, int textSize, int textAlign) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
        StyleConstants.setFontSize(set, textSize);
        StyleConstants.setAlignment(set, textAlign);
        doc.setCharacterAttributes(first, secondly, set, false);
    }
}

//获取表名
class getTableName implements Runnable {
    static int num = 0;

    @Override
    public void run() {
        System.out.println("成功启动");
        for (int i = 0; i < SearchForm.layerStyle.length; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/information_schema?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery("select table_name from tables where table_schema='" + SearchForm.layerStyle[i] + "'");
                rs.last();
                switch (SearchForm.layerStyle[i]) {
                    case "个人":
                        SearchForm.Person = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Person[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "国家":
                        SearchForm.Country = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Country[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "自然":
                        SearchForm.Natural = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Natural[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "食品安全":
                        SearchForm.FoodSafe = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.FoodSafe[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "交易":
                        SearchForm.Business = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Business[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                }
                num = 0;
                rs.close();
                stm.close();
                stm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

//表名检索
class searchNow implements Runnable {
    static String searchText = null;
    static int g = 0;
    searchNow(String searchText) {
        searchNow.searchText = searchText;//获取输入框内的文本
    }
    @Override
    public void run() {
        for (int i = 0; i < SearchForm.layerStyle.length; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + SearchForm.layerStyle[i] + "?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                Statement stm = con.createStatement();
                switch (SearchForm.layerStyle[i]) {
                    case "个人"://SearchForm.Person
                        for (int k = 0; k < SearchForm.Person.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Person[k] + "` where 内容 like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            SearchForm.PersonData = new String[rs.getRow()][2];
                            while (rs.next()) {
                                //初始化
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //设置
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//增加ScrollPane的滚动条
                                //获取文本
                                SearchForm.insert(SearchForm.Person[k] + "->" + rs.getString("条"), Color.black, 35, 0);//目录-条
                                SearchForm.insert("\n     " + rs.getString("内容"), Color.black, 30, 0);//内容
                                //关键字颜色
                                SearchForm.jTextPane.setCaretPosition(0);
                                String str1 = SearchForm.doc.getText(0, SearchForm.doc.getLength());
                                String str2 = searchText;
                                while (g > -1) {
                                    if (SearchForm.jTextPane.getSelectedText() == null) {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() + 1);
                                    } else {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() - searchText.length() + 1);
                                    }
                                    if (g > -1) {
                                        SearchForm.jTextPane.setCaretPosition(g);
                                        SearchForm.jTextPane.select(g, g + str2.length());
                                    }
                                    if (g > 0 && SearchForm.jTextPane.getSelectedText() != null) {
                                        SearchForm.change(g, str2.length(), Color.red, 30, 0);
                                    }
                                }
                                g = 0;
                                SearchForm.jScrollPane.repaint();
                                SearchForm.jScrollPane.validate();
                            }
                        }
                        break;
                    case "国家"://SearchForm.Country
                        for (int k = 0; k < SearchForm.Country.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Country[k] + "` where 内容 like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Country[k]);
                            while (rs.next()) {
                                //初始化
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //设置
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//增加ScrollPane的滚动条
                                //获取文本
                                SearchForm.insert(SearchForm.Country[k] + "->" + rs.getString("条"), Color.black, 35, 0);//目录-条
                                SearchForm.insert("\n     " + rs.getString("内容"), Color.black, 30, 0);//内容
                                //关键字颜色
                                SearchForm.jTextPane.setCaretPosition(0);
                                String str1 = SearchForm.doc.getText(0, SearchForm.doc.getLength());
                                String str2 = searchText;
                                while (g > -1) {
                                    if (SearchForm.jTextPane.getSelectedText() == null) {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() + 1);
                                    } else {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() - searchText.length() + 1);
                                    }
                                    if (g > -1) {
                                        SearchForm.jTextPane.setCaretPosition(g);
                                        SearchForm.jTextPane.select(g, g + str2.length());
                                    }
                                    if (g > 0 && SearchForm.jTextPane.getSelectedText() != null) {
                                        SearchForm.change(g, str2.length(), Color.red, 30, 0);
                                    }
                                }
                                g = 0;
                                SearchForm.jScrollPane.repaint();
                                SearchForm.jScrollPane.validate();
                            }
                        }
                        break;
                    case "自然"://SearchForm.Natural
                        for (int k = 0; k < SearchForm.Natural.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Natural[k] + "` where 内容 like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Natural[k]);
                            while (rs.next()) {
                                //初始化
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //设置
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//增加ScrollPane的滚动条
                                //获取文本
                                SearchForm.insert(SearchForm.Natural[k] + "->" + rs.getString("条"), Color.black, 35, 0);//目录-条
                                SearchForm.insert("\n     " + rs.getString("内容"), Color.black, 30, 0);//内容
                                //关键字颜色
                                SearchForm.jTextPane.setCaretPosition(0);
                                String str1 = SearchForm.doc.getText(0, SearchForm.doc.getLength());
                                String str2 = searchText;
                                while (g > -1) {
                                    if (SearchForm.jTextPane.getSelectedText() == null) {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() + 1);
                                    } else {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() - searchText.length() + 1);
                                    }
                                    if (g > -1) {
                                        SearchForm.jTextPane.setCaretPosition(g);
                                        SearchForm.jTextPane.select(g, g + str2.length());
                                    }
                                    if (g > 0 && SearchForm.jTextPane.getSelectedText() != null) {
                                        SearchForm.change(g, str2.length(), Color.red, 30, 0);
                                    }
                                }
                                g = 0;
                                SearchForm.jScrollPane.repaint();
                                SearchForm.jScrollPane.validate();
                            }
                        }
                        break;
                    case "食品安全"://SearchForm.FoodSafe
                        for (int k = 0; k < SearchForm.FoodSafe.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.FoodSafe[k] + "` where 内容 like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.FoodSafe[k]);
                            while (rs.next()) {
                                //初始化
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //设置
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//增加ScrollPane的滚动条
                                //获取文本
                                SearchForm.insert(SearchForm.FoodSafe[k] + "->" + rs.getString("条"), Color.black, 35, 0);//目录-条
                                SearchForm.insert("\n     " + rs.getString("内容"), Color.black, 30, 0);//内容
                                //关键字颜色
                                SearchForm.jTextPane.setCaretPosition(0);
                                String str1 = SearchForm.doc.getText(0, SearchForm.doc.getLength());
                                String str2 = searchText;
                                while (g > -1) {
                                    if (SearchForm.jTextPane.getSelectedText() == null) {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() + 1);
                                    } else {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() - searchText.length() + 1);
                                    }
                                    if (g > -1) {
                                        SearchForm.jTextPane.setCaretPosition(g);
                                        SearchForm.jTextPane.select(g, g + str2.length());
                                    }
                                    if (g > 0 && SearchForm.jTextPane.getSelectedText() != null) {
                                        SearchForm.change(g, str2.length(), Color.red, 30, 0);
                                    }
                                }
                                g = 0;
                                SearchForm.jScrollPane.repaint();
                                SearchForm.jScrollPane.validate();
                            }
                        }
                        break;
                    case "交易"://SearchForm.Business
                        for (int k = 0; k < SearchForm.Business.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Business[k] + "` where 内容 like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Business[k]);
                            while (rs.next()) {
                                //初始化
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //设置
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//增加ScrollPane的滚动条
                                //获取文本
                                SearchForm.insert(SearchForm.Business[k] + "->" + rs.getString("条"), Color.black, 35, 0);//目录-条
                                SearchForm.insert("\n     " + rs.getString("内容"), Color.black, 30, 0);//内容
                                //关键字颜色
                                SearchForm.jTextPane.setCaretPosition(0);
                                String str1 = SearchForm.doc.getText(0, SearchForm.doc.getLength());
                                String str2 = searchText;
                                while (g > -1) {
                                    if (SearchForm.jTextPane.getSelectedText() == null) {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() + 1);
                                    } else {
                                        g = str1.indexOf(str2, SearchForm.jTextPane.getCaretPosition() - searchText.length() + 1);
                                    }
                                    if (g > -1) {
                                        SearchForm.jTextPane.setCaretPosition(g);
                                        SearchForm.jTextPane.select(g, g + str2.length());
                                    }
                                    if (g > 0 && SearchForm.jTextPane.getSelectedText() != null) {
                                        SearchForm.change(g, str2.length(), Color.red, 30, 0);
                                    }
                                }
                                g = 0;
                                SearchForm.jScrollPane.repaint();
                                SearchForm.jScrollPane.validate();
                            }
                        }
                        break;
                }
                con.close();
                stm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JScrollBar bar = SearchForm.jScrollPane.getVerticalScrollBar();
        bar.setValue(SearchForm.oldJTextPaneY);
        SearchForm.oldJTextPaneY += SearchForm.jTextPaneY;
    }
}

class AddSearchContent{
    AddSearchContent(){

    }
}

