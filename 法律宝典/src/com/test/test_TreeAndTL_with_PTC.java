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
//Ŀ¼���������ݿ⣻��ȡ���ݿ�������ʾ����壻����Ŀ¼����λ���ָ������λ��;
public class test_TreeAndTL_with_PTC extends JFrame {
    static String dbname = "����";//���ݿ���
    static String tablename = "�񷨵�";

    static DefaultMutableTreeNode Treebian;
    static DefaultMutableTreeNode Treefengbian;
    static DefaultMutableTreeNode Treezhang;
    static DefaultMutableTreeNode Treejie;
    static DefaultMutableTreeNode Treetiao;

    //�������
    static JTextPane textPane = new JTextPane();
    static JScrollPane PaneSP = new JScrollPane(textPane);
    static StyledDocument doc = textPane.getStyledDocument();


    public static void main(String[] args){

        new test_TreeAndTL_with_PTC(tablename);

    }
    //��ӱ�
    test_TreeAndTL_with_PTC(String name/*��*/){
        String url = "jdbc:mysql://127.0.0.1:3306/"+dbname+"?user=root&password=root&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(tablename);

        insertText(tablename+"\n",Color.black,45,StyleConstants.ALIGN_CENTER);
        textPane.setEditable(false);//���ý�ֹ���༭
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
                    insertText(rs.getString("��")+"\n",Color.black,40,0);
                    new fengbian1(rs.getString("��"));

                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
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

                String findText = check.toString();//������ı�
                String shuruText = null;//�ı��������
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
    ///�������ֲ���
    public static void insertText(String text/*�ı�����*/, Color colorName/*��ɫ*/ ,int textSize/*�����С*/,int textAlign/*���뷽ʽ*/){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, colorName);//����������ɫ
        StyleConstants.setFontSize(set, textSize);//���������С
        StyleConstants.setAlignment(set,textAlign);//�����ı����뷽ʽ
        doc.setParagraphAttributes(textPane.getText().length(),doc.getLength()-textPane.getText().length(),set,false);
        try {
            doc.insertString(doc.getLength(), text, set);//��������
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
///�ֱ�--����ͬһ��ǰ�߻����ϼ�5
class fengbian1{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    fengbian1(String name){
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
                    test_TreeAndTL_with_PTC.Treefengbian = new DefaultMutableTreeNode(rs.getString("�ֱ�"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treefengbian);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("�ֱ�")+"\n",Color.black,35,0);
                    new zhang1(rs.getString("��"),rs.getString("�ֱ�"));

                }if(rs.getString("�ֱ�").equals("null")){
                    //System.out.println("������2");
                    new zhang1(rs.getString("��"),rs.getString("�ֱ�"));
                    break;
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///��--��ͬ
class zhang1{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";

    zhang1(String name1/*��*/,String name2/*�ֱ�*/){
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
                    test_TreeAndTL_with_PTC.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treefengbian.add(test_TreeAndTL_with_PTC.Treezhang);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("��")+"\n",Color.black,30,0);
                    new jie1(name1,rs.getString("��"));

                    //System.out.println("������1");
                }if(!zhang.contains(rs.getString("��")) && name2.equals("null") && !rs.getString("��").equals("null")){//�ڱ����������
                    //System.out.println("����������2");
                    zhang.add(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treezhang = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treezhang);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("��")+"\n",Color.black,30,0);
                    new jie1(name1,rs.getString("��"));

                    //System.out.println("������1");
                }if(rs.getString("��").equals("null")){//û��
                    //System.out.println("�����ڵİ�������������������������");
                    //System.out.println("������2");
                    new jie1(name1,rs.getString("��"));
                    break;
                }
            }
            con.close();stm.close();rs.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///��
class jie1{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    jie1(String name1/*��*/,String name2/*��*/){
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
                    test_TreeAndTL_with_PTC.Treejie = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treejie);
                    //System.out.println("������1");
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("��")+"\n",Color.black,25,0);
                    new tiao1(name1,rs.getString("�ֱ�"),name2,rs.getString("��"));

                }if(rs.getString("��").equals("null")){//û��
                    //System.out.println("���ǽ�2");
                    //System.out.println("������");
                    new tiao1(name1,rs.getString("�ֱ�"),name2,rs.getString("��"));
                    break;
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
///��
class tiao1{
    private String dbname = "layer";
    private String tablename = "�񷨵�";
    private String username = "root";
    private String userPassword = "root";
    private String url = "jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userPassword+"&useUnicode=true&&setCharacterEncoding=utf-8&&serverTimezone=GMT";
    tiao1(String name1,/*��*/String name2/*�ֱ�*/,String name3/*��*/,String name4/*��*/){
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
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("��")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("����")+"\n",Color.black,20,0);
                    //System.out.println(rs.getString("��"));
                    //System.out.println("1");
                }
                //�޽����,��ӵ�������
                if(!tiao.contains(rs.getString("��")) && name4.equals("null") && !name3.equals("null") && !name2.equals("null")){
                    tiao.add(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treezhang.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("    "+rs.getString("��")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("����")+"\n",Color.black,20,0);
                    //System.out.println("2");
                }
                //�޷ֱ����,��ӵ�������
                if(!tiao.contains(rs.getString("��")) && name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treejie.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("      "+rs.getString("��")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("����")+"\n",Color.black,20,0);
                    //System.out.println("3");
                }
                //�޷ֱ࣬���£��޽����
                if(!tiao.contains(rs.getString("��")) && name2.equals("null") && name3.equals("null") && name4.equals("null") ){
                    //System.out.println("�����ܰ�������");
                    tiao.add(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treebian.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("  "+rs.getString("��")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("����")+"\n",Color.black,20,0);
                    //System.out.println("4");
                }
                //����
                if(!tiao.contains(rs.getString("��")) && !name2.equals("null") && !name3.equals("null") && !name4.equals("null")){
                    tiao.add(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treetiao = new DefaultMutableTreeNode(rs.getString("��"));
                    test_TreeAndTL_with_PTC.Treejie.add(test_TreeAndTL_with_PTC.Treetiao);
                    test_TreeAndTL_with_PTC.insertText("      "+rs.getString("��")+"\n",Color.black,20,0);
                    test_TreeAndTL_with_PTC.insertText(rs.getString("����")+"\n",Color.black,20,0);

                    //System.out.println("5");
                }
            }
            con.close();stm.close();rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}