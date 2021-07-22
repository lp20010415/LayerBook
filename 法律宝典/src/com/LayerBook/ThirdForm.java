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

//Ŀ¼���������ݿ⣻��ȡ���ݿ�������ʾ����壻����Ŀ¼����λ���ָ������λ��;
public class ThirdForm extends JFrame {
    //���ݿ����
    static String dbname = "";
    static String tablename = "";
    //Ŀ¼������
    static DefaultMutableTreeNode Treebian;
    static DefaultMutableTreeNode Treefengbian;
    static DefaultMutableTreeNode Treezhang;
    static DefaultMutableTreeNode Treejie;
    static DefaultMutableTreeNode Treetiao;
    DefaultMutableTreeNode root;
    //�������
    static JTextPane textPane = new JTextPane();
    static JScrollPane panetext = new JScrollPane(textPane);
    static StyledDocument doc = textPane.getStyledDocument();
    //��ѯ
    static JTextField search = new JTextField();
    static JButton searchUp = new JButton("������");
    static JButton searchDown = new JButton("������");

    //��ӱ�
    ThirdForm(String name1/*���ݿ���*/, String name2/*����*/) {
        textPane.setText("");//��textPane��գ�����ര���ظ��ı���
        dbname = name1;//���ݿ���
        tablename = name2;//����
        /*�������ӵ�ַ*/
        String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        root = new DefaultMutableTreeNode(tablename);//��������

        insertText(tablename + "\n", Color.black, 45, StyleConstants.ALIGN_CENTER);//�����ı�
        textPane.setEditable(false);//���ý�ֹ���༭
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from `" + tablename + "`");
            Boolean checkrs = rs.next();
            List<String> bian = new ArrayList<String>();
            while (rs.next()) {//������ִ����������
                if (!bian.contains(rs.getString("��"))) {
                    //System.out.println("�����ֱ�");
                    bian.add(rs.getString("��"));
                    Treebian = new DefaultMutableTreeNode(rs.getString("��"));
                    root.add(Treebian);
                    insertText(rs.getString("��") + "\n", Color.black, 40, 0);
                    new fengbian1(rs.getString("��"));

                }
            }
            if(!checkrs) {//û����ִ����������
                insertText("��δ������ݣ������ڴ���", Color.red, 50, 1);
            }
            con.close();
            stm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ʵ������
        JTree jTree = new JTree(root);
        jTree.setShowsRootHandles(true);
        ///���ĵ����¼�
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textPane.setCaretPosition(0);
                int selRow = jTree.getRowForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode check = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                //TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());

                String text = check.toString();//������ı�
                String shuruText = null;//�ı��������
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
        //Ŀ¼���������
        JScrollPane jScrollPane = new JScrollPane(jTree);
        jScrollPane.setSize(300, 680);
        jScrollPane.setLocation(10, 10);

        //��ѯ���������
        search.setSize(820, 70);
        search.setFont(new Font("����", 1, 35));
        search.setLocation(jScrollPane.getWidth() + 20, 10);
        ///��ť
        //����
        searchUp.setSize(80, 70);
        searchUp.setLocation(search.getX() + search.getWidth() + 10, 10);
        //����
        searchDown.setSize(80, 70);
        searchDown.setLocation(searchUp.getX() + searchUp.getWidth() + 10, 10);
        ////�������¼�
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
                    JOptionPane.showMessageDialog(null, "�Ҳ��������ҵ����ݣ�", "����", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        ////�������¼�
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
                    JOptionPane.showMessageDialog(null, "�Ҳ������ҵ����ݣ�", "����", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //��������������
        panetext.setSize(1000, 600);
        panetext.setLocation(jScrollPane.getWidth() + 20, search.getHeight() + 20);

        add(search);
        add(searchUp);
        add(searchDown);
        add(panetext);
        add(jScrollPane);
        setTitle("���ɱ���-"+name2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500,250,1350, 750);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        textPane.setCaretPosition(0);//�ö�
    }

    ///�������ֲ���
    public static void insertText(String text/*�ı�����*/, Color colorName/*��ɫ*/, int textSize/*�����С*/, int textAlign/*���뷽ʽ*/) {

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, colorName);//����������ɫ
        StyleConstants.setFontSize(set, textSize);//���������С
        StyleConstants.setAlignment(set, textAlign);//�����ı����뷽ʽ
        doc.setParagraphAttributes(textPane.getText().length(), doc.getLength() - textPane.getText().length(), set, false);
        try {
            doc.insertString(doc.getLength(), text, set);//��������
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

class GetData{

}

///�ֱ�--����ͬһ��ǰ�߻����ϼ�5
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
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where ��='" + name + "'");
            List<String> fengbian = new ArrayList<String>();
            while (rs.next()) {
                if (!fengbian.contains(rs.getString("�ֱ�")) && !rs.getString("�ֱ�").equals("null")) {
                    //System.out.println("������1");
                    fengbian.add(rs.getString("�ֱ�"));
                    ThirdForm.Treefengbian = new DefaultMutableTreeNode(rs.getString("�ֱ�"));
                    ThirdForm.Treebian.add(ThirdForm.Treefengbian);
                    ThirdForm.insertText(rs.getString("�ֱ�") + "\n", Color.black, 35, 0);
                    new zhang1(rs.getString("��"), rs.getString("�ֱ�"));

                }
                if (rs.getString("�ֱ�").equals("null")) {
                    //System.out.println("������2");
                    new zhang1(rs.getString("��"), rs.getString("�ֱ�"));
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

///��--��ͬ
class zhang1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang1(String name1/*��*/, String name2/*�ֱ�*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where ��='" + name1 + "' and �ֱ�='" + name2 + "'");
            List<String> zhang = new ArrayList<String>();

            while (rs.next()) {
                if (!zhang.contains(rs.getString("��")) && !name2.equals("null")) {//�ڷֱ����������
                    //System.out.println("����������1");
                    zhang.add(rs.getString("��"));
                    ThirdForm.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treefengbian.add(ThirdForm.Treezhang);
                    ThirdForm.insertText("  " + rs.getString("��") + "\n", Color.black, 30, 0);
                    new jie1(name1, rs.getString("��"));

                    //System.out.println("������1");
                }
                if (!zhang.contains(rs.getString("��")) && name2.equals("null") && !rs.getString("��").equals("null")) {//�ڱ����������
                    //System.out.println("����������2");
                    zhang.add(rs.getString("��"));
                    ThirdForm.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treebian.add(ThirdForm.Treezhang);
                    ThirdForm.insertText("  " + rs.getString("��") + "\n", Color.black, 30, 0);
                    new jie1(name1, rs.getString("��"));

                    //System.out.println("������1");
                }
                if (rs.getString("��").equals("null")) {//û��
                    //System.out.println("�����ڵİ�������������������������");
                    //System.out.println("������2");
                    new jie1(name1, rs.getString("��"));
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

///��
class jie1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    jie1(String name1/*��*/, String name2/*��*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where ��='" + name1 + "' and ��='" + name2 + "'");
            List<String> jie = new ArrayList<String>();
            while (rs.next()) {
                if (!jie.contains(rs.getString("��")) && !name2.equals("null") && !rs.getString("��").equals("null")) {//����������ӽ�
                    //System.out.println("���ǽ�1");
                    jie.add(rs.getString("��"));
                    ThirdForm.Treejie = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treezhang.add(ThirdForm.Treejie);
                    //System.out.println("������1");
                    ThirdForm.insertText("    " + rs.getString("��") + "\n", Color.black, 25, 0);
                    new tiao1(name1, rs.getString("�ֱ�"), name2, rs.getString("��"));

                }
                if (rs.getString("��").equals("null")) {//û��
                    //System.out.println("���ǽ�2");
                    //System.out.println("������");
                    new tiao1(name1, rs.getString("�ֱ�"), name2, rs.getString("��"));
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

///��
class tiao1 {
    String dbname = ThirdForm.dbname;
    String tablename = ThirdForm.tablename;

    String url = "jdbc:mysql://127.0.0.1:3306/" + dbname + "?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    tiao1(String name1,/*��*/String name2/*�ֱ�*/, String name3/*��*/, String name4/*��*/) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from " + tablename + " where ��='" + name1 + "'and �ֱ�='" + name2 + "' and ��='" + name3 + "' and ��='" + name4 + "'");
            List<String> tiao = new ArrayList<String>();
            //System.out.println(name1);
            //System.out.println(name3);
            while (rs.next()) {
                //�޷ֱ࣬�޽����,��ӵ�������
                if (!tiao.contains(rs.getString("��")) && name2.equals("null") && name4.equals("null") && !name3.equals("null")) {
                    tiao.add(rs.getString("��"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treezhang.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("    " + rs.getString("��") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("����") + "\n", Color.black, 20, 0);
                    //System.out.println(rs.getString("��"));
                    //System.out.println("1");
                }
                //�޽����,��ӵ�������
                if (!tiao.contains(rs.getString("��")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")) {
                    tiao.add(rs.getString("��"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treezhang.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("    " + rs.getString("��") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("����") + "\n", Color.black, 20, 0);
                    //System.out.println("2");
                }
                //�޷ֱ����,��ӵ�������
                if (!tiao.contains(rs.getString("��")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")) {
                    tiao.add(rs.getString("��"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treejie.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("      " + rs.getString("��") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("����") + "\n", Color.black, 20, 0);
                    //System.out.println("3");
                }
                //�޷ֱ࣬���£��޽����
                if (!tiao.contains(rs.getString("��")) && name2.equals("null") && name3.equals("null") && name4.equals("null")) {
                    //System.out.println("�����ܰ�������");
                    tiao.add(rs.getString("��"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treebian.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("  " + rs.getString("��") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("����") + "\n", Color.black, 20, 0);
                    //System.out.println("4");
                }
                //����
                if (!tiao.contains(rs.getString("��")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")) {
                    tiao.add(rs.getString("��"));
                    ThirdForm.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    ThirdForm.Treejie.add(ThirdForm.Treetiao);
                    ThirdForm.insertText("      " + rs.getString("��") + "\n", Color.black, 20, 0);
                    ThirdForm.insertText(rs.getString("����") + "\n", Color.black, 20, 0);

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
