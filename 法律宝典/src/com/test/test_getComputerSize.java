package com.test;

import java.awt.*;
//��ȡ������Ļ��С
public class test_getComputerSize {
    public static void main(String[] args) {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screensize.getWidth();
        int screenHeight = (int)screensize.getHeight();
        System.out.println("��:"+screenWidth+"��:"+screenHeight);
    }
}
