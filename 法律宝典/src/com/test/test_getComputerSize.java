package com.test;

import java.awt.*;
//获取电脑屏幕大小
public class test_getComputerSize {
    public static void main(String[] args) {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screensize.getWidth();
        int screenHeight = (int)screensize.getHeight();
        System.out.println("宽:"+screenWidth+"高:"+screenHeight);
    }
}
