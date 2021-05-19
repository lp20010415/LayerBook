package com.LayerBook;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchForm extends JFrame {
    //���ػ���
    static String[][] PersonData;
    static String[][] CountryData;
    static String[][] NaturalData;
    static String[][] FoodSafeData;
    static String[][] BusinessData;
    //����Ƿ�������
    boolean checkBtn = false;
    //�����ƶ��ľ���
    static int moveSlider = 0;
    //��������������ʾ��λ��
    static int oldJTextPaneY = 0;//֮ǰ��׶�λ��;
    static int jTextPaneY = 10;
    //��ȡ����
    static int startRange = 0;
    static int endRange = 4;
    //��������
    static String[] Person = null;
    static String[] Country = null;
    static String[] Natural = null;
    static String[] FoodSafe = null;
    static String[] Business = null;
    //�̶���������
    static String[] layerStyle = {"����", "����", "��Ȼ", "ʳƷ��ȫ", "����"};
    //���ֲ���
    static StyledDocument doc;

    ///�������������ʼ��
    static JButton jButton = new JButton("����");//��ť
    static ImageIcon iButton = new ImageIcon("./images/����search.png");//��ťͼƬ
    static JTextField jTextField = new JTextField();//������
    static JTextPane jTextPane;//����������ʾ��
    static JPanel jPanel = new JPanel();//���ݿ�-two
    static JScrollPane jScrollPane = new JScrollPane(jPanel);//���ݿ�-one

    SearchForm() {
        ///������������������
        //������
        jTextField.setBounds(10, 10, 1150, 50);
        //��ť
        jButton.setBounds(jTextField.getX() + jTextField.getWidth() + 10, jTextField.getY(), 150, 50);
        jButton.setFont(new Font("����", 1, 20));
        jButton.setIcon(iButton);
        /*������ť�����¼�*/
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBtn = true;
                jPanel.removeAll();
                jTextPaneY = 10;
                oldJTextPaneY = 0;
                String text = jTextField.getText();
                new Thread(new searchNow(text), "��ʽ����").start();
                repaint();
                validate();
            }
        });
        /*��ťͼƬ����*/
        iButton.setImage(iButton.getImage().getScaledInstance(30, 30, 0));
        //���ݿ�-one
        jScrollPane.setBounds(10, jTextField.getHeight() + 20, 1310, 620);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//���������������ٶ�
        /*�������¼�*/
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
                        new Thread(new searchNow(inputText), "��������").start();
                    }
                }
            }
        });
        /*���ݿ�-two*/
        jPanel.setLayout(null);
        add(jTextField);
        add(jButton);
        add(jScrollPane);
        setTitle("�������з���");
        setLayout(null);
        setBounds(500, 250, 1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        new Thread(new getTableName(), "��ȡ����").start();
    }

    //��������ʾ���������
    public static void insert(String str, Color textcolor, int textSize, int textAlign) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);//������ɫ
        StyleConstants.setFontSize(set, textSize);//���ִ�С
        StyleConstants.setAlignment(set, textAlign);//���ֶ��뷽ʽ
        doc.setParagraphAttributes(jTextPane.getText().length(), doc.getLength() - jTextPane.getText().length(), set, false);
        try {
            doc.insertString(doc.getLength(), str, set);//��������
        } catch (BadLocationException e) {
        }
    }

    //Ȧ���ؼ���
    public static void change(int first, int secondly, Color textcolor, int textSize, int textAlign) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
        StyleConstants.setFontSize(set, textSize);
        StyleConstants.setAlignment(set, textAlign);
        doc.setCharacterAttributes(first, secondly, set, false);
    }
}

//��ȡ����
class getTableName implements Runnable {
    static int num = 0;

    @Override
    public void run() {
        System.out.println("�ɹ�����");
        for (int i = 0; i < SearchForm.layerStyle.length; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/information_schema?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery("select table_name from tables where table_schema='" + SearchForm.layerStyle[i] + "'");
                rs.last();
                switch (SearchForm.layerStyle[i]) {
                    case "����":
                        SearchForm.Person = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Person[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "����":
                        SearchForm.Country = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Country[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "��Ȼ":
                        SearchForm.Natural = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.Natural[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "ʳƷ��ȫ":
                        SearchForm.FoodSafe = new String[rs.getRow()];
                        rs.first();
                        rs.previous();
                        while (rs.next()) {
                            SearchForm.FoodSafe[num] = rs.getString("table_name");
                            num++;
                        }
                        break;
                    case "����":
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

//��������
class searchNow implements Runnable {
    static String searchText = null;
    static int g = 0;
    searchNow(String searchText) {
        searchNow.searchText = searchText;//��ȡ������ڵ��ı�
    }
    @Override
    public void run() {
        for (int i = 0; i < SearchForm.layerStyle.length; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + SearchForm.layerStyle[i] + "?user=root&password=root&&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT");
                Statement stm = con.createStatement();
                switch (SearchForm.layerStyle[i]) {
                    case "����"://SearchForm.Person
                        for (int k = 0; k < SearchForm.Person.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Person[k] + "` where ���� like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            SearchForm.PersonData = new String[rs.getRow()][2];
                            while (rs.next()) {
                                //��ʼ��
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //����
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//����ScrollPane�Ĺ�����
                                //��ȡ�ı�
                                SearchForm.insert(SearchForm.Person[k] + "->" + rs.getString("��"), Color.black, 35, 0);//Ŀ¼-��
                                SearchForm.insert("\n     " + rs.getString("����"), Color.black, 30, 0);//����
                                //�ؼ�����ɫ
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
                    case "����"://SearchForm.Country
                        for (int k = 0; k < SearchForm.Country.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Country[k] + "` where ���� like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Country[k]);
                            while (rs.next()) {
                                //��ʼ��
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //����
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//����ScrollPane�Ĺ�����
                                //��ȡ�ı�
                                SearchForm.insert(SearchForm.Country[k] + "->" + rs.getString("��"), Color.black, 35, 0);//Ŀ¼-��
                                SearchForm.insert("\n     " + rs.getString("����"), Color.black, 30, 0);//����
                                //�ؼ�����ɫ
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
                    case "��Ȼ"://SearchForm.Natural
                        for (int k = 0; k < SearchForm.Natural.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Natural[k] + "` where ���� like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Natural[k]);
                            while (rs.next()) {
                                //��ʼ��
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //����
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//����ScrollPane�Ĺ�����
                                //��ȡ�ı�
                                SearchForm.insert(SearchForm.Natural[k] + "->" + rs.getString("��"), Color.black, 35, 0);//Ŀ¼-��
                                SearchForm.insert("\n     " + rs.getString("����"), Color.black, 30, 0);//����
                                //�ؼ�����ɫ
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
                    case "ʳƷ��ȫ"://SearchForm.FoodSafe
                        for (int k = 0; k < SearchForm.FoodSafe.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.FoodSafe[k] + "` where ���� like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.FoodSafe[k]);
                            while (rs.next()) {
                                //��ʼ��
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //����
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//����ScrollPane�Ĺ�����
                                //��ȡ�ı�
                                SearchForm.insert(SearchForm.FoodSafe[k] + "->" + rs.getString("��"), Color.black, 35, 0);//Ŀ¼-��
                                SearchForm.insert("\n     " + rs.getString("����"), Color.black, 30, 0);//����
                                //�ؼ�����ɫ
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
                    case "����"://SearchForm.Business
                        for (int k = 0; k < SearchForm.Business.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Business[k] + "` where ���� like '%" + searchText + "%' limit " + SearchForm.startRange + "," + SearchForm.endRange);
                            System.out.println(SearchForm.Business[k]);
                            while (rs.next()) {
                                //��ʼ��
                                SearchForm.jTextPane = new JTextPane();
                                SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                                //����
                                SearchForm.jTextPane.setSize(1270, 200);
                                SearchForm.jTextPane.setLocation(10, SearchForm.jTextPaneY);
                                SearchForm.jTextPaneY += SearchForm.jTextPane.getHeight() + 10;
                                SearchForm.jTextPane.setEditable(false);
                                SearchForm.jPanel.add(SearchForm.jTextPane);
                                SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.jTextPaneY));//����ScrollPane�Ĺ�����
                                //��ȡ�ı�
                                SearchForm.insert(SearchForm.Business[k] + "->" + rs.getString("��"), Color.black, 35, 0);//Ŀ¼-��
                                SearchForm.insert("\n     " + rs.getString("����"), Color.black, 30, 0);//����
                                //�ؼ�����ɫ
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

