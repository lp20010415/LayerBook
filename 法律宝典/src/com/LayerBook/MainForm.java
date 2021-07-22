package com.LayerBook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class MainForm extends JFrame implements ActionListener {
	//电脑屏幕相关数值----实现程序大概在屏幕中间
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenWidth = (int)screenSize.getWidth();//获取电脑屏幕的宽
	int screenHeight = (int)screenSize.getHeight();//获取电脑屏幕的高

	//进度条定义数值
	int JProgressBarMax = 100;
	int JProgressBarMin = 0;
	int JProgressBarValue = JProgressBarMin;
	//火柴人位置数值
	int pictureLocationX = 25;
	int pictureLocationY = 300;

	Timer t = new Timer();
	//启动组件
	JLabel NationalEmblem = new JLabel();//国徽
	JLabel Matchman = new JLabel();//火柴人
	JProgressBar jpb = new JProgressBar();//进度条
	JLabel jpbDownText = new JLabel("正在进入程序",0);//进度条下文字
	JLabel leftText = new JLabel("<html><body>学<br>法<br>&nbsp;<br>守<br>法</body></html>",0);//左边文字
	JLabel rightText = new JLabel("<html><body>懂<br>法<br>&nbsp;<br>用<br>法</body></html>",0);//右边文字
	//启动图片
	ImageIcon MatchmanPicture = new ImageIcon("./images/缩小版独火柴人.gif");
	ImageIcon NationalEmblemPicture = new ImageIcon("./images/国徽.jpg");

	//操作组件
	JLabel topText = new JLabel("法律类型",0);
	JButton Person = new JButton("个人");
	JButton Country = new JButton("国家");
	JButton Natural = new JButton("自然");
	JButton FoodSafe = new JButton("食品安全");
	JButton Business = new JButton("交易");
	JButton SearchAll = new JButton("检索所有法律");
	//操作界面相关数据
	int btnWidth = 250;//按钮宽度
	int btnHeight = 60;//按钮高度
	//按钮图片
	ImageIcon pIcon = new ImageIcon("./images/个人.png");
	ImageIcon cIcon = new ImageIcon("./images/国家.png");
	ImageIcon nIcon = new ImageIcon("./images/自然.png");
	ImageIcon fIcon = new ImageIcon("./images/食品安全.png");
	ImageIcon bIcon = new ImageIcon("./images/交易.png");
	ImageIcon sIcon = new ImageIcon("./images/搜索.png");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainForm();
	}
	MainForm(){

		///启动界面
		//国徽图片
		NationalEmblemPicture.setImage(NationalEmblemPicture.getImage().getScaledInstance(200,200,0));
		NationalEmblem.setIcon(NationalEmblemPicture);
		NationalEmblem.setBounds(98,30,200,200);
		//左边文字
		leftText.setBounds(0,0,70,180);
		leftText.setFont(new Font("宋体",1,30));
		//右边文字
		rightText.setBounds(330,0,70,180);
		rightText.setFont(new Font("宋体",1,30));
		//火柴人图片
		Matchman.setIcon(MatchmanPicture);
		Matchman.setBounds(pictureLocationX,pictureLocationY,MatchmanPicture.getIconWidth(),MatchmanPicture.getIconHeight());
		//进度条
		jpb.setMaximum(JProgressBarMax);
		jpb.setMinimum(JProgressBarMin);
		jpb.setValue(JProgressBarValue);
		jpb.setBounds(pictureLocationX+25,pictureLocationY+MatchmanPicture.getIconHeight(),300,50);
		jpb.setStringPainted(true);
		//进度条下文字
		jpbDownText.setFont(new Font("宋体",1,25));
		jpbDownText.setBounds(jpb.getX(),jpb.getY()+jpb.getHeight(),jpb.getWidth(),jpb.getHeight());

		///操作界面
		//文本相关设置
		topText.setBounds(75,0,btnWidth,btnHeight);
		topText.setFont(new Font("宋体",1,35));
		//按钮大小位置设置
		Person.setBounds(75,btnHeight,btnWidth,btnHeight);
		Country.setBounds(75,btnHeight*2+2,btnWidth,btnHeight);
		Natural.setBounds(75,btnHeight*3+4,btnWidth,btnHeight);
		FoodSafe.setBounds(75,btnHeight*4+6,btnWidth,btnHeight);
		Business.setBounds(75,btnHeight*5+8,btnWidth,btnHeight);
		SearchAll.setBounds(75,btnHeight*6+10,btnWidth,btnHeight);
		//按钮文字大小设置
		Person.setFont(new Font("宋体",1,20));
		Country.setFont(new Font("宋体",1,20));
		Natural.setFont(new Font("宋体",1,20));
		FoodSafe.setFont(new Font("宋体",1,20));
		Business.setFont(new Font("宋体",1,20));
		SearchAll.setFont(new Font("宋体",1,20));
		//按钮图片设置
		pIcon.setImage(pIcon.getImage().getScaledInstance(50,50,0));
		cIcon.setImage(cIcon.getImage().getScaledInstance(50,50,0));
		nIcon.setImage(nIcon.getImage().getScaledInstance(50,50,0));
		fIcon.setImage(fIcon.getImage().getScaledInstance(50,50,0));
		bIcon.setImage(bIcon.getImage().getScaledInstance(50,50,0));
		sIcon.setImage(sIcon.getImage().getScaledInstance(50,50,0));
		//按钮使用图片设置
		Person.setIcon(pIcon);
		Country.setIcon(cIcon);
		Natural.setIcon(nIcon);
		FoodSafe.setIcon(fIcon);
		Business.setIcon(bIcon);
		SearchAll.setIcon(sIcon);
		//按钮添加监听事件
		Person.addActionListener(this);
		Country.addActionListener(this);
		Natural.addActionListener(this);
		FoodSafe.addActionListener(this);
		Business.addActionListener(this);
		SearchAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SearchForm();
			}
		});

		//火柴人移动时间周期
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(JProgressBarValue<JProgressBarMax){
<<<<<<< HEAD
					JProgressBarValue+=2;//Ĭ��Ϊ2
=======
					JProgressBarValue+=2;//默认为2
>>>>>>> 1867bbb2b818395caaa7dda39508c20a8cc66745
					jpb.setValue(JProgressBarValue);
					pictureLocationX+=6;
					Matchman.setLocation(pictureLocationX,pictureLocationY);
					jpbDownText.setText(jpbDownText.getText()+".");
					if(jpbDownText.getText().equals("正在进入程序........"))
						jpbDownText.setText("正在进入程序");
				}else if(JProgressBarValue == JProgressBarMax){
					//移除启动组件
					remove(leftText);
					remove(rightText);
					remove(NationalEmblem);
					remove(Matchman);
					remove(jpb);
					remove(jpbDownText);
					repaint();
					t.cancel();
					//添加操作组件
					add(topText);
					add(Person);
					add(Country);
					add(Natural);
					add(FoodSafe);
					add(Business);
					add(SearchAll);
					repaint();
				}
			}
		},0,200);

		add(leftText);
		add(rightText);
		add(Matchman);
		add(NationalEmblem);
		add(jpb);
		add(jpbDownText);
		setTitle("法律宝典");
		setLayout(null);
		setVisible(true);
		setResizable(false);
		setBounds(500,250,420,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new SecondlyForm(e.getActionCommand());
	}
}
