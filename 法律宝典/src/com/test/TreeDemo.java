package com.test;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreeDemo extends JFrame {
    public static void main(String[] args){
        new TreeDemo();
    }
    TreeDemo(){
        JPanel panel = new JPanel();


        /*//���ڵ�
        DefaultMutableTreeNode first_father = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode first_mother = new DefaultMutableTreeNode("ĸ��");

        //�ӽڵ�
        DefaultMutableTreeNode secondly_son = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode secondly_daughter = new DefaultMutableTreeNode("Ů��");

        //��ڵ�
        DefaultMutableTreeNode third_grandson = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode third_granddaughter = new DefaultMutableTreeNode("��Ů");

        //���ڵ�����ӽڵ�
        first_father.add(secondly_son);
        first_father.add(secondly_daughter);
        first_mother.add(secondly_son);
        first_mother.add(secondly_daughter);

        //�ӽڵ������ڵ�
        secondly_son.add(third_grandson);
        secondly_son.add(third_granddaughter);
        secondly_daughter.add(third_grandson);
        secondly_daughter.add(third_granddaughter);

        JTree jTree1 = new JTree(first_father);
        JTree jTree2 = new JTree(first_mother);
        jTree1.setShowsRootHandles(true);
        jTree2.setShowsRootHandles(true);

        JScrollPane jScrollPane1 = new JScrollPane(jTree1);
        JScrollPane jScrollPane2 = new JScrollPane(jTree2);
        panel.add(jScrollPane1, BorderLayout.CENTER);
        panel.add(jScrollPane2, BorderLayout.CENTER);*/

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        for(int i = 0;i < 10;i++){
            DefaultMutableTreeNode root_secondly = new DefaultMutableTreeNode(i);
            root.add(root_secondly);
            for(int j = 0;j < 10;j++){
                DefaultMutableTreeNode root_thrid = new DefaultMutableTreeNode("������"+j);
                
                root_secondly.add(root_thrid);
            }

        }
        JTree jTree = new JTree(root);
        jTree.setShowsRootHandles(true);
        //jTree.setVisible(true);


        JScrollPane jScrollPane = new JScrollPane(jTree);
        //jScrollPane.add(jTree);
        jScrollPane.setSize(200,300);
        panel.setSize(500,300);
        //panel.add(jScrollPane);
        //panel.add(jTree);
        //setContentPane(panel);
        //add(jTree);
        add(jScrollPane);
        jScrollPane.setLocation(0,0);

        setLayout(null);
        setVisible(true);
        setSize(500,500);
        setLocation(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
