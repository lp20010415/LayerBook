package com.test;

import javax.swing.*;
import java.awt.*;

public class GridBagLayout_test {

    public static void main(String[] args) {
        JFrame jf = new JFrame("���Դ���");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GridBagLayout gridBag = new GridBagLayout();    // ���ֹ�����
        GridBagConstraints c = null;                    // Լ��

        JPanel panel = new JPanel(gridBag);

        JButton btn01 = new JButton("Button01");
        JButton btn02 = new JButton("Button02");
        JButton btn03 = new JButton("Button03");
        JButton btn04 = new JButton("Button04");
        JButton btn05 = new JButton("Button05");
        JButton btn06 = new JButton("Button06");
        JButton btn07 = new JButton("Button07");
        JButton btn08 = new JButton("Button08");
        JButton btn09 = new JButton("Button09");
        JButton btn10 = new JButton("Button10");

        /* ��� ��� �� Լ�� �� ���ֹ����� */
        // Button01
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn01, c); // �ڲ�ʹ�õĽ��� c �ĸ���

        // Button02
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn02, c);

        // Button03
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn03, c);

        // Button04 ��ʾ����ռ����ǰ��ʣ��ռ䣨���У�����������ʾ����
        c = new GridBagConstraints();
       // c.gridwidth = GridBagConstraints.REMAINDER;
        //c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn04, c);

        // Button05 ��ʾ�����ռһ�У����У�����������ʾ����
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn05, c);

        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn06,c);

        /* ��� ��� �� ������� */
        panel.add(btn01);
        panel.add(btn02);
        panel.add(btn03);
        panel.add(btn04);
        panel.add(btn05);
        panel.add(btn06);

        jf.setContentPane(panel);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

}
