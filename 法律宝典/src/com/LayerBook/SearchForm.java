package com.LayerBook;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SearchForm extends JFrame{

    static boolean checkfirst = true;//控制第一次位置

    //本地缓存
    static String[][] PersonData;
    static String[][] CountryData;
    static String[][] NaturalData;
    static String[][] FoodSafeData;
    static String[][] BusinessData;

    //检查是否点击搜索
    static boolean checkBtn = false;

    //滑块移动的距离
    static int moveSlider = 0;

    //控制搜索内容显示框位置
    static int oldScrollPaneY = 0;//之前最底端位置;
    static int ScrollPaneY = 10;

    //获取条数
    /*获取条数*/
    static int getPersonRow = 0;
    static int getCountryRow = 0;
    static int getNaturalRow = 0;
    static int getFoodSafeRow = 0;
    static int getBusinessRow = 0;
    /*获取数据总条数*/
    static int getPersonAll = 0;
    static int getCountryAll = 0;
    static int getNaturalAll = 0;
    static int getFoodSafeAll = 0;
    static int getBusinessAll = 0;

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
    ImageIcon iButton = new ImageIcon(this.getClass().getResource("/images/搜索search.png"));//按钮图片
    static JTextField jTextField = new JTextField();//搜索框
    static JTextPane jTextPane;//搜索内容显示框
    static JScrollPane ScrollContent;//出现滑块使内容能看全
    static JPanel jPanel = new JPanel();//内容框-two
    static JScrollPane jScrollPane = new JScrollPane(jPanel);//内容框-one
    static JScrollBar bar;

    SearchForm() {
        bar = jScrollPane.getVerticalScrollBar();
        //避免关闭后再打开数据依然是原样
        bar.setValue(0);
        jScrollPane.getVerticalScrollBar().setValue(0);
        jPanel.setPreferredSize(null);
        getPersonRow = 0;
        getCountryRow = 0;
        getNaturalRow = 0;
        getFoodSafeRow = 0;
        getBusinessRow = 0;
        getPersonAll = 0;
        getCountryAll = 0;
        getNaturalAll = 0;
        getFoodSafeAll = 0;
        getBusinessAll = 0;
        oldScrollPaneY = 0;
        ScrollPaneY = 10;
        moveSlider = 0;
        checkBtn = false;
        jPanel.removeAll();

        ///操作界面组件相关设置
        //搜索框
        jTextField.setFont(new Font("宋体", 1, 20));//设置字体大小
        jTextField.setBounds(10, 10, 1150, 50);

        //按钮
        jButton.setBounds(jTextField.getX() + jTextField.getWidth() + 10, jTextField.getY(), 150, 50);
        jButton.setFont(new Font("宋体", 1, 20));
        jButton.setIcon(iButton);
        /*搜索按钮单击事件*/
        jButton.addActionListener(e -> {
            checkBtn = true;
            jPanel.removeAll();
            ScrollPaneY = 10;
            oldScrollPaneY = 0;
            String text = jTextField.getText();
            new searchNow(text);
            repaint();
            validate();
        });
        /*按钮图片设置*/
        iButton.setImage(iButton.getImage().getScaledInstance(30, 30, 0));

        //内容框-one
        jScrollPane.setBounds(10, jTextField.getHeight() + 20, 1310, 620);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//设置鼠标滚动滑块速度

        /*内容框-two*/
        jPanel.setLayout(null);
        add(jTextField);
        add(jButton);
        add(jScrollPane);

        //主窗口相关设置
        setTitle("检索所有法律");
        setLayout(null);
        setBounds(500, 250, 1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        new Thread(new getTableName(), "获取表名").start();//获取表名
        SearchForm.jPanel.removeAll();//防止重新打开界面后之前的相关控件还在
        SearchForm.jTextField.setText("");//上同类似
        /*滚动条事件*/
        bar.addAdjustmentListener(e -> {
            System.out.println("Hello");
            if (checkBtn) {
                moveSlider = bar.getMaximum() - bar.getVisibleAmount();
                if (bar.getValue() == moveSlider) {
                    if (getPersonRow < getPersonAll) {
                        getPersonRow += 4;
                        if (getPersonRow > getPersonAll)
                            getPersonRow = getPersonRow - (getPersonRow - getPersonAll);
                    }
                    if (getCountryRow < getCountryAll) {
                        getCountryRow += 4;
                        if (getCountryRow > getCountryAll)
                            getCountryRow = getCountryRow - (getCountryRow - getCountryAll);
                    }
                    if (getNaturalRow < getNaturalAll) {
                        getNaturalRow += 4;
                        if (getNaturalRow > getNaturalAll)
                            getNaturalRow = getNaturalRow - (getNaturalRow - getNaturalAll);
                    }
                    if (getFoodSafeRow < getFoodSafeAll) {
                        getFoodSafeRow += 4;
                        if (getFoodSafeRow > getBusinessAll)
                            getFoodSafeRow = getFoodSafeRow - (getFoodSafeRow - getBusinessAll);
                    }
                    if (getBusinessRow < getBusinessAll) {
                        getBusinessRow += 4;
                        if (getBusinessRow > getFoodSafeAll)
                            getBusinessRow = getBusinessRow - (getBusinessRow - getFoodSafeAll);
                    }
                    if (getPersonAll != 0 || getCountryAll != 0 || getNaturalAll != 0 || getFoodSafeAll != 0 || getBusinessAll != 0)
                        new AddSearchContent();
                    if(getPersonRow == getPersonAll && getCountryRow == getCountryAll && getNaturalRow == getNaturalAll && getFoodSafeRow == getFoodSafeAll && getBusinessRow == getBusinessAll)
                        checkBtn = false;
                }
            } else
                JOptionPane.showMessageDialog(null,"所有结果已列出！","提示",JOptionPane.INFORMATION_MESSAGE);
        });
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
            e.printStackTrace();
        }
    }

    //圈出关键词
    public static void change(int first, int secondly, Color textcolor) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
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
class searchNow{
    boolean none = true;//检测是否有搜到内容
    String[] Person = SearchForm.Person;
    String[] Country = SearchForm.Country;
    String[] Natural = SearchForm.Natural;
    String[] FoodSafe = SearchForm.FoodSafe;
    String[] Business = SearchForm.Business;

    searchNow(String searchText) {
        for (int i = 0; i < SearchForm.layerStyle.length; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + SearchForm.layerStyle[i] + "?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                Statement stm = con.createStatement();
                switch (SearchForm.layerStyle[i]) {
                    case "个人"://SearchForm.Person
                        ArrayList<String> Person_layer = new ArrayList<>();
                        ArrayList<String> Person_item = new ArrayList<>();
                        ArrayList<String> Person_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Person.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Person[k] + "` where 内容 like '%" + searchText + "%'");
                            while (rs.next()) {
                                Person_layer.add(Person[k]);
                                Person_item.add(rs.getString("条"));
                                Person_content.add(rs.getString("内容"));
                            }
                        }
                        SearchForm.getPersonAll = Person_layer.size();
                        String[][] PersonData = new String[3][Person_layer.size()];
                        for (int j = 0; j < Person_layer.size(); j++) {
                            PersonData[0][j] = Person_layer.get(j);
                            PersonData[1][j] = Person_item.get(j);
                            PersonData[2][j] = Person_content.get(j);
                        }
                        SearchForm.PersonData = PersonData;
                        if (PersonData[0].length != 0) {
                            new AddSearchContent();
                            none = false;
                        }
                        break;
                    case "国家"://SearchForm.Country
                        ArrayList<String> Country_layer = new ArrayList<>();
                        ArrayList<String> Country_item = new ArrayList<>();
                        ArrayList<String> Country_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Country.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Country[k] + "` where 内容 like '%" + searchText + "%'");
                            while (rs.next()) {
                                Country_layer.add(Country[k]);
                                Country_item.add(rs.getString("条"));
                                Country_content.add(rs.getString("内容"));
                            }
                        }
                        SearchForm.getCountryAll = Country_layer.size();
                        String[][] CountryData = new String[3][Country_layer.size()];
                        for (int j = 0; j < Country_layer.size(); j++) {
                            CountryData[0][j] = Country_layer.get(j);
                            CountryData[1][j] = Country_item.get(j);
                            CountryData[2][j] = Country_content.get(j);
                        }
                        SearchForm.CountryData = CountryData;
                        if (CountryData[0].length != 0) {
                            new AddSearchContent();
                            none = false;
                        }
                        break;
                    case "自然"://SearchForm.Natural
                        ArrayList<String> Natural_layer = new ArrayList<>();
                        ArrayList<String> Natural_item = new ArrayList<>();
                        ArrayList<String> Natural_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Natural.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Natural[k] + "` where 内容 like '%" + searchText + "%'");
                            while (rs.next()) {
                                Natural_layer.add(Natural[k]);
                                Natural_item.add(rs.getString("条"));
                                Natural_content.add(rs.getString("内容"));
                            }
                        }
                        SearchForm.getNaturalAll = Natural_layer.size();
                        String[][] NaturalData = new String[3][Natural_layer.size()];
                        for (int j = 0; j < Natural_layer.size(); j++) {
                            NaturalData[0][j] = Natural_layer.get(j);
                            NaturalData[1][j] = Natural_item.get(j);
                            NaturalData[2][j] = Natural_content.get(j);
                        }
                        SearchForm.NaturalData = NaturalData;
                        if (NaturalData[0].length != 0) {
                            new AddSearchContent();
                            none = false;
                        }
                        break;
                    case "食品安全"://SearchForm.FoodSafe
                        ArrayList<String> FoodSafe_layer = new ArrayList<>();
                        ArrayList<String> FoodSafe_item = new ArrayList<>();
                        ArrayList<String> FoodSafe_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.FoodSafe.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.FoodSafe[k] + "` where 内容 like '%" + searchText + "%'");
                            while (rs.next()) {
                                FoodSafe_layer.add(FoodSafe[k]);
                                FoodSafe_item.add(rs.getString("条"));
                                FoodSafe_content.add(rs.getString("内容"));
                            }
                        }
                        SearchForm.getFoodSafeAll = FoodSafe_layer.size();
                        String[][] FoodSafeData = new String[3][FoodSafe_layer.size()];
                        for (int j = 0; j < FoodSafe_layer.size(); j++) {
                            FoodSafeData[0][j] = FoodSafe_layer.get(j);
                            FoodSafeData[1][j] = FoodSafe_item.get(j);
                            FoodSafeData[2][j] = FoodSafe_content.get(j);
                        }
                        SearchForm.FoodSafeData = FoodSafeData;
                        if (FoodSafeData[0].length != 0) {
                            new AddSearchContent();
                            none = false;
                        }
                        break;
                    case "交易"://SearchForm.Business
                        ArrayList<String> Business_layer = new ArrayList<>();
                        ArrayList<String> Business_item = new ArrayList<>();
                        ArrayList<String> Business_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Business.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Business[k] + "` where 内容 like '%" + searchText + "%'");
                            while (rs.next()) {
                                Business_layer.add(Business[k]);
                                Business_item.add(rs.getString("条"));
                                Business_content.add(rs.getString("内容"));
                            }
                        }
                        SearchForm.getBusinessAll = Business_layer.size();
                        String[][] BusinessData = new String[3][Business_layer.size()];
                        for (int j = 0; j < Business_layer.size(); j++) {
                            BusinessData[0][j] = Business_layer.get(j);
                            BusinessData[1][j] = Business_item.get(j);
                            BusinessData[2][j] = Business_content.get(j);
                        }
                        SearchForm.BusinessData = BusinessData;
                        if (BusinessData[0].length != 0) {
                            new AddSearchContent();
                            none = false;
                        }
                        break;
                }
                con.close();
                stm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //没搜索到
        if (none) {
            JLabel jl = new JLabel();
            jl.setText("没有搜索到！");
            jl.setFont(new Font("宋体", 1, 30));
            jl.setSize(200, 50);
            jl.setLocation((SearchForm.jPanel.getWidth() / 2) - (jl.getWidth() / 2), 10);
            SearchForm.jPanel.add(jl);
            SearchForm.jPanel.repaint();
            SearchForm.jPanel.validate();
        }
        SearchForm.bar.setValue(SearchForm.oldScrollPaneY);
        SearchForm.oldScrollPaneY += SearchForm.ScrollPaneY;
    }
}

//添加搜索内容
class AddSearchContent {
    int g = 0;//定位
    String searchText = SearchForm.jTextField.getText();
    int getPersonRow = SearchForm.getPersonRow;//获取数量
    int getCountryRow = SearchForm.getCountryRow;//获取数量
    int getNaturalRow = SearchForm.getNaturalRow;//获取数量
    int getFoodSafeRow = SearchForm.getFoodSafeRow;//获取数量
    int getBusinessRow = SearchForm.getBusinessRow;//获取数量

    int getPersonAll = SearchForm.getPersonAll;//获取总条数
    int getCountryAll = SearchForm.getCountryAll;//获取总条数
    int getNaturalAll = SearchForm.getNaturalAll;//获取总条数
    int getFoodSafeAll = SearchForm.getFoodSafeAll;//获取总条数
    int getBusinessAll = SearchForm.getBusinessAll;//获取总条数

    String[][] PersonData = SearchForm.PersonData;
    String[][] CountryData = SearchForm.CountryData;
    String[][] NaturalData = SearchForm.NaturalData;
    String[][] FoodSafeData = SearchForm.FoodSafeData;
    String[][] BusinessData = SearchForm.BusinessData;

    AddSearchContent() {
        try {
            //个人
            if (PersonData != null) {
                if (getPersonRow != getPersonAll) {
                    for (int i = getPersonRow; i < getPersonAll; i++) {
                        if (getPersonRow + 4 == i)
                            break;
                        System.out.println("我被执行"+i+"次");
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//设置jTextPane禁止编辑
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//包裹jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//滑动面板大小
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//滑动面板位置
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//方便添加下一个滑动面板
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//增加ScrollPane的滚动条
                        //获取文本
                        SearchForm.insert(PersonData[0][i] + "->" + PersonData[1][i], Color.black, 35, 0);//目录-条
                        SearchForm.insert("\n     " + PersonData[2][i], Color.black, 30, 0);//内容
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//置顶
                    }
                    SearchForm.jScrollPane.repaint();
                    SearchForm.jScrollPane.validate();
                }
            }

            //国家
            if (CountryData != null) {
                if (getCountryRow != getCountryAll) {
                    for (int i = getCountryRow; i < getCountryAll; i++) {//初始化
                        if (getCountryRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//设置jTextPane禁止编辑
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//包裹jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//滑动面板大小
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//滑动面板位置
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//方便添加下一个滑动面板
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//增加ScrollPane的滚动条
                        //获取文本
                        SearchForm.insert(CountryData[0][i] + "->" + CountryData[1][i], Color.black, 35, 0);//目录-条
                        SearchForm.insert("\n     " + CountryData[2][i], Color.black, 30, 0);//内容
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//置顶
                    }
                    SearchForm.jScrollPane.repaint();
                    SearchForm.jScrollPane.validate();
                }
            }

            //自然
            if (NaturalData != null) {
                if (getNaturalRow != getNaturalAll) {
                    for (int i = getNaturalRow; i < getNaturalAll; i++) {
                        if (getNaturalRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//设置jTextPane禁止编辑
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//包裹jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//滑动面板大小
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//滑动面板位置
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//方便添加下一个滑动面板
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//增加ScrollPane的滚动条
                        //获取文本
                        SearchForm.insert(NaturalData[0][i] + "->" + NaturalData[1][i], Color.black, 35, 0);//目录-条
                        SearchForm.insert("\n     " + NaturalData[2][i], Color.black, 30, 0);//内容
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//置顶
                    }
                }
            }

            //食品安全
            if (FoodSafeData != null) {
                if (getFoodSafeRow != getFoodSafeAll) {
                    for (int i = getFoodSafeRow; i < getFoodSafeAll; i++) {
                        if (getFoodSafeRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//设置jTextPane禁止编辑
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//包裹jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//滑动面板大小
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//滑动面板位置
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//方便添加下一个滑动面板
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//增加ScrollPane的滚动条
                        //获取文本
                        SearchForm.insert(FoodSafeData[0][i] + "->" + FoodSafeData[1][i], Color.black, 35, 0);//目录-条
                        SearchForm.insert("\n     " + FoodSafeData[2][i], Color.black, 30, 0);//内容
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//置顶
                    }
                }
            }

            //交易
            if (BusinessData != null) {
                if (getBusinessRow != getBusinessAll) {
                    for (int i = getBusinessRow; i < getBusinessAll; i++) {
                        if (getBusinessRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//设置jTextPane禁止编辑
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//包裹jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//滑动面板大小
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//滑动面板位置
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//方便添加下一个滑动面板
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//增加ScrollPane的滚动条
                        //获取文本
                        SearchForm.insert(BusinessData[0][i] + "->" + BusinessData[1][i], Color.black, 35, 0);//目录-条
                        SearchForm.insert("\n     " + BusinessData[2][i], Color.black, 30, 0);//内容
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//置顶
                    }
                }
            }

            //第一次滑块置顶
            if((getPersonRow == 0 || getCountryRow == 0 || getNaturalRow == 0 || getFoodSafeRow == 0 || getBusinessRow == 0) && SearchForm.checkfirst)
                SearchForm.bar.setValue(0);SearchForm.checkfirst = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchForm.checkBtn = true;
    }
}
