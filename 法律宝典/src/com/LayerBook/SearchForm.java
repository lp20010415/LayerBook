package com.LayerBook;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SearchForm extends JFrame{

    static boolean checkfirst = true;//���Ƶ�һ��λ��

    //���ػ���
    static String[][] PersonData;
    static String[][] CountryData;
    static String[][] NaturalData;
    static String[][] FoodSafeData;
    static String[][] BusinessData;

    //����Ƿ�������
    static boolean checkBtn = false;

    //�����ƶ��ľ���
    static int moveSlider = 0;

    //��������������ʾ��λ��
    static int oldScrollPaneY = 0;//֮ǰ��׶�λ��;
    static int ScrollPaneY = 10;

    //��ȡ����
    /*��ȡ����*/
    static int getPersonRow = 0;
    static int getCountryRow = 0;
    static int getNaturalRow = 0;
    static int getFoodSafeRow = 0;
    static int getBusinessRow = 0;
    /*��ȡ����������*/
    static int getPersonAll = 0;
    static int getCountryAll = 0;
    static int getNaturalAll = 0;
    static int getFoodSafeAll = 0;
    static int getBusinessAll = 0;

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
    ImageIcon iButton = new ImageIcon(this.getClass().getResource("/images/����search.png"));//��ťͼƬ
    static JTextField jTextField = new JTextField();//������
    static JTextPane jTextPane;//����������ʾ��
    static JScrollPane ScrollContent;//���ֻ���ʹ�����ܿ�ȫ
    static JPanel jPanel = new JPanel();//���ݿ�-two
    static JScrollPane jScrollPane = new JScrollPane(jPanel);//���ݿ�-one
    static JScrollBar bar;

    SearchForm() {
        bar = jScrollPane.getVerticalScrollBar();
        //����رպ��ٴ�������Ȼ��ԭ��
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

        ///������������������
        //������
        jTextField.setFont(new Font("����", 1, 20));//���������С
        jTextField.setBounds(10, 10, 1150, 50);

        //��ť
        jButton.setBounds(jTextField.getX() + jTextField.getWidth() + 10, jTextField.getY(), 150, 50);
        jButton.setFont(new Font("����", 1, 20));
        jButton.setIcon(iButton);
        /*������ť�����¼�*/
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
        /*��ťͼƬ����*/
        iButton.setImage(iButton.getImage().getScaledInstance(30, 30, 0));

        //���ݿ�-one
        jScrollPane.setBounds(10, jTextField.getHeight() + 20, 1310, 620);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(5);//���������������ٶ�

        /*���ݿ�-two*/
        jPanel.setLayout(null);
        add(jTextField);
        add(jButton);
        add(jScrollPane);

        //�������������
        setTitle("�������з���");
        setLayout(null);
        setBounds(500, 250, 1350, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        new Thread(new getTableName(), "��ȡ����").start();//��ȡ����
        SearchForm.jPanel.removeAll();//��ֹ���´򿪽����֮ǰ����ؿؼ�����
        SearchForm.jTextField.setText("");//��ͬ����
        /*�������¼�*/
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
                JOptionPane.showMessageDialog(null,"���н�����г���","��ʾ",JOptionPane.INFORMATION_MESSAGE);
        });
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
            e.printStackTrace();
        }
    }

    //Ȧ���ؼ���
    public static void change(int first, int secondly, Color textcolor) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textcolor);
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
class searchNow{
    boolean none = true;//����Ƿ����ѵ�����
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
                    case "����"://SearchForm.Person
                        ArrayList<String> Person_layer = new ArrayList<>();
                        ArrayList<String> Person_item = new ArrayList<>();
                        ArrayList<String> Person_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Person.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Person[k] + "` where ���� like '%" + searchText + "%'");
                            while (rs.next()) {
                                Person_layer.add(Person[k]);
                                Person_item.add(rs.getString("��"));
                                Person_content.add(rs.getString("����"));
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
                    case "����"://SearchForm.Country
                        ArrayList<String> Country_layer = new ArrayList<>();
                        ArrayList<String> Country_item = new ArrayList<>();
                        ArrayList<String> Country_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Country.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Country[k] + "` where ���� like '%" + searchText + "%'");
                            while (rs.next()) {
                                Country_layer.add(Country[k]);
                                Country_item.add(rs.getString("��"));
                                Country_content.add(rs.getString("����"));
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
                    case "��Ȼ"://SearchForm.Natural
                        ArrayList<String> Natural_layer = new ArrayList<>();
                        ArrayList<String> Natural_item = new ArrayList<>();
                        ArrayList<String> Natural_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Natural.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Natural[k] + "` where ���� like '%" + searchText + "%'");
                            while (rs.next()) {
                                Natural_layer.add(Natural[k]);
                                Natural_item.add(rs.getString("��"));
                                Natural_content.add(rs.getString("����"));
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
                    case "ʳƷ��ȫ"://SearchForm.FoodSafe
                        ArrayList<String> FoodSafe_layer = new ArrayList<>();
                        ArrayList<String> FoodSafe_item = new ArrayList<>();
                        ArrayList<String> FoodSafe_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.FoodSafe.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.FoodSafe[k] + "` where ���� like '%" + searchText + "%'");
                            while (rs.next()) {
                                FoodSafe_layer.add(FoodSafe[k]);
                                FoodSafe_item.add(rs.getString("��"));
                                FoodSafe_content.add(rs.getString("����"));
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
                    case "����"://SearchForm.Business
                        ArrayList<String> Business_layer = new ArrayList<>();
                        ArrayList<String> Business_item = new ArrayList<>();
                        ArrayList<String> Business_content = new ArrayList<>();
                        for (int k = 0; k < SearchForm.Business.length; k++) {
                            ResultSet rs = stm.executeQuery("select * from `" + SearchForm.Business[k] + "` where ���� like '%" + searchText + "%'");
                            while (rs.next()) {
                                Business_layer.add(Business[k]);
                                Business_item.add(rs.getString("��"));
                                Business_content.add(rs.getString("����"));
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
        //û������
        if (none) {
            JLabel jl = new JLabel();
            jl.setText("û����������");
            jl.setFont(new Font("����", 1, 30));
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

//�����������
class AddSearchContent {
    int g = 0;//��λ
    String searchText = SearchForm.jTextField.getText();
    int getPersonRow = SearchForm.getPersonRow;//��ȡ����
    int getCountryRow = SearchForm.getCountryRow;//��ȡ����
    int getNaturalRow = SearchForm.getNaturalRow;//��ȡ����
    int getFoodSafeRow = SearchForm.getFoodSafeRow;//��ȡ����
    int getBusinessRow = SearchForm.getBusinessRow;//��ȡ����

    int getPersonAll = SearchForm.getPersonAll;//��ȡ������
    int getCountryAll = SearchForm.getCountryAll;//��ȡ������
    int getNaturalAll = SearchForm.getNaturalAll;//��ȡ������
    int getFoodSafeAll = SearchForm.getFoodSafeAll;//��ȡ������
    int getBusinessAll = SearchForm.getBusinessAll;//��ȡ������

    String[][] PersonData = SearchForm.PersonData;
    String[][] CountryData = SearchForm.CountryData;
    String[][] NaturalData = SearchForm.NaturalData;
    String[][] FoodSafeData = SearchForm.FoodSafeData;
    String[][] BusinessData = SearchForm.BusinessData;

    AddSearchContent() {
        try {
            //����
            if (PersonData != null) {
                if (getPersonRow != getPersonAll) {
                    for (int i = getPersonRow; i < getPersonAll; i++) {
                        if (getPersonRow + 4 == i)
                            break;
                        System.out.println("�ұ�ִ��"+i+"��");
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//����jTextPane��ֹ�༭
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//����jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//��������С
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//�������λ��
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//���������һ���������
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//����ScrollPane�Ĺ�����
                        //��ȡ�ı�
                        SearchForm.insert(PersonData[0][i] + "->" + PersonData[1][i], Color.black, 35, 0);//Ŀ¼-��
                        SearchForm.insert("\n     " + PersonData[2][i], Color.black, 30, 0);//����
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//�ö�
                    }
                    SearchForm.jScrollPane.repaint();
                    SearchForm.jScrollPane.validate();
                }
            }

            //����
            if (CountryData != null) {
                if (getCountryRow != getCountryAll) {
                    for (int i = getCountryRow; i < getCountryAll; i++) {//��ʼ��
                        if (getCountryRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//����jTextPane��ֹ�༭
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//����jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//��������С
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//�������λ��
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//���������һ���������
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//����ScrollPane�Ĺ�����
                        //��ȡ�ı�
                        SearchForm.insert(CountryData[0][i] + "->" + CountryData[1][i], Color.black, 35, 0);//Ŀ¼-��
                        SearchForm.insert("\n     " + CountryData[2][i], Color.black, 30, 0);//����
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//�ö�
                    }
                    SearchForm.jScrollPane.repaint();
                    SearchForm.jScrollPane.validate();
                }
            }

            //��Ȼ
            if (NaturalData != null) {
                if (getNaturalRow != getNaturalAll) {
                    for (int i = getNaturalRow; i < getNaturalAll; i++) {
                        if (getNaturalRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//����jTextPane��ֹ�༭
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//����jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//��������С
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//�������λ��
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//���������һ���������
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//����ScrollPane�Ĺ�����
                        //��ȡ�ı�
                        SearchForm.insert(NaturalData[0][i] + "->" + NaturalData[1][i], Color.black, 35, 0);//Ŀ¼-��
                        SearchForm.insert("\n     " + NaturalData[2][i], Color.black, 30, 0);//����
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//�ö�
                    }
                }
            }

            //ʳƷ��ȫ
            if (FoodSafeData != null) {
                if (getFoodSafeRow != getFoodSafeAll) {
                    for (int i = getFoodSafeRow; i < getFoodSafeAll; i++) {
                        if (getFoodSafeRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//����jTextPane��ֹ�༭
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//����jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//��������С
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//�������λ��
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//���������һ���������
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//����ScrollPane�Ĺ�����
                        //��ȡ�ı�
                        SearchForm.insert(FoodSafeData[0][i] + "->" + FoodSafeData[1][i], Color.black, 35, 0);//Ŀ¼-��
                        SearchForm.insert("\n     " + FoodSafeData[2][i], Color.black, 30, 0);//����
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//�ö�
                    }
                }
            }

            //����
            if (BusinessData != null) {
                if (getBusinessRow != getBusinessAll) {
                    for (int i = getBusinessRow; i < getBusinessAll; i++) {
                        if (getBusinessRow + 4 == i)
                            break;
                        SearchForm.jTextPane = new JTextPane();
                        SearchForm.doc = SearchForm.jTextPane.getStyledDocument();
                        SearchForm.jTextPane.setEditable(false);//����jTextPane��ֹ�༭
                        SearchForm.ScrollContent = new JScrollPane(SearchForm.jTextPane);//����jTextPane
                        SearchForm.ScrollContent.setSize(1270, 200);//��������С
                        SearchForm.ScrollContent.setLocation(10, SearchForm.ScrollPaneY);//�������λ��
                        SearchForm.ScrollPaneY += SearchForm.ScrollContent.getHeight() + 10;//���������һ���������
                        SearchForm.jPanel.add(SearchForm.ScrollContent);
                        SearchForm.jPanel.setPreferredSize(new Dimension(1280, SearchForm.ScrollPaneY));//����ScrollPane�Ĺ�����
                        //��ȡ�ı�
                        SearchForm.insert(BusinessData[0][i] + "->" + BusinessData[1][i], Color.black, 35, 0);//Ŀ¼-��
                        SearchForm.insert("\n     " + BusinessData[2][i], Color.black, 30, 0);//����
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
                                SearchForm.change(g, str2.length(), Color.red);
                            }
                        }
                        g = 0;
                        SearchForm.jTextPane.setCaretPosition(0);//�ö�
                    }
                }
            }

            //��һ�λ����ö�
            if((getPersonRow == 0 || getCountryRow == 0 || getNaturalRow == 0 || getFoodSafeRow == 0 || getBusinessRow == 0) && SearchForm.checkfirst)
                SearchForm.bar.setValue(0);SearchForm.checkfirst = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchForm.checkBtn = true;
    }
}
