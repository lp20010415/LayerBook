package com.LayerBook;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class MainForm extends JFrame implements ActionListener {
	//������Ļ�����ֵ----ʵ�ֳ���������Ļ�м�
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenWidth = (int)screenSize.getWidth();//��ȡ������Ļ�Ŀ�
	int screenHeight = (int)screenSize.getHeight();//��ȡ������Ļ�ĸ�

	//������������ֵ
	int JProgressBarMax = 100;
	int JProgressBarMin = 0;
	int JProgressBarValue = JProgressBarMin;
	//�����λ����ֵ
	int pictureLocationX = 25;
	int pictureLocationY = 300;

	Timer t = new Timer();
	//�������
	JLabel NationalEmblem = new JLabel();//����
	JLabel Matchman = new JLabel();//�����
	JProgressBar jpb = new JProgressBar();//������
	JLabel jpbDownText = new JLabel("���ڽ������",0);//������������
	JLabel leftText = new JLabel("<html><body>ѧ<br>��<br>&nbsp;<br>��<br>��</body></html>",0);//�������
	JLabel rightText = new JLabel("<html><body>��<br>��<br>&nbsp;<br>��<br>��</body></html>",0);//�ұ�����
	//����ͼƬ
	ImageIcon MatchmanPicture = new ImageIcon("./images/��С��������.gif");
	ImageIcon NationalEmblemPicture = new ImageIcon("./images/����.jpg");

	//�������
	JLabel topText = new JLabel("��������",0);
	JButton Person = new JButton("����");
	JButton Country = new JButton("����");
	JButton Natural = new JButton("��Ȼ");
	JButton FoodSafe = new JButton("ʳƷ��ȫ");
	JButton Business = new JButton("����");
	JButton SearchAll = new JButton("�������з���");
	//���������������
	int btnWidth = 250;//��ť���
	int btnHeight = 60;//��ť�߶�
	//��ťͼƬ
	ImageIcon pIcon = new ImageIcon("./images/����.png");
	ImageIcon cIcon = new ImageIcon("./images/����.png");
	ImageIcon nIcon = new ImageIcon("./images/��Ȼ.png");
	ImageIcon fIcon = new ImageIcon("./images/ʳƷ��ȫ.png");
	ImageIcon bIcon = new ImageIcon("./images/����.png");
	ImageIcon sIcon = new ImageIcon("./images/����.png");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainForm();
	}
	MainForm(){

		///��������
		//����ͼƬ
		NationalEmblemPicture.setImage(NationalEmblemPicture.getImage().getScaledInstance(200,200,0));
		NationalEmblem.setIcon(NationalEmblemPicture);
		NationalEmblem.setBounds(98,30,200,200);
		//�������
		leftText.setBounds(0,0,70,180);
		leftText.setFont(new Font("����",1,30));
		//�ұ�����
		rightText.setBounds(330,0,70,180);
		rightText.setFont(new Font("����",1,30));
		//�����ͼƬ
		Matchman.setIcon(MatchmanPicture);
		Matchman.setBounds(pictureLocationX,pictureLocationY,MatchmanPicture.getIconWidth(),MatchmanPicture.getIconHeight());
		//������
		jpb.setMaximum(JProgressBarMax);
		jpb.setMinimum(JProgressBarMin);
		jpb.setValue(JProgressBarValue);
		jpb.setBounds(pictureLocationX+25,pictureLocationY+MatchmanPicture.getIconHeight(),300,50);
		jpb.setStringPainted(true);
		//������������
		jpbDownText.setFont(new Font("����",1,25));
		jpbDownText.setBounds(jpb.getX(),jpb.getY()+jpb.getHeight(),jpb.getWidth(),jpb.getHeight());

		///��������
		//�ı��������
		topText.setBounds(75,0,btnWidth,btnHeight);
		topText.setFont(new Font("����",1,35));
		//��ť��Сλ������
		Person.setBounds(75,btnHeight,btnWidth,btnHeight);
		Country.setBounds(75,btnHeight*2+2,btnWidth,btnHeight);
		Natural.setBounds(75,btnHeight*3+4,btnWidth,btnHeight);
		FoodSafe.setBounds(75,btnHeight*4+6,btnWidth,btnHeight);
		Business.setBounds(75,btnHeight*5+8,btnWidth,btnHeight);
		SearchAll.setBounds(75,btnHeight*6+10,btnWidth,btnHeight);
		//��ť���ִ�С����
		Person.setFont(new Font("����",1,20));
		Country.setFont(new Font("����",1,20));
		Natural.setFont(new Font("����",1,20));
		FoodSafe.setFont(new Font("����",1,20));
		Business.setFont(new Font("����",1,20));
		SearchAll.setFont(new Font("����",1,20));
		//��ťͼƬ����
		pIcon.setImage(pIcon.getImage().getScaledInstance(50,50,0));
		cIcon.setImage(cIcon.getImage().getScaledInstance(50,50,0));
		nIcon.setImage(nIcon.getImage().getScaledInstance(50,50,0));
		fIcon.setImage(fIcon.getImage().getScaledInstance(50,50,0));
		bIcon.setImage(bIcon.getImage().getScaledInstance(50,50,0));
		sIcon.setImage(sIcon.getImage().getScaledInstance(50,50,0));
		//��ťʹ��ͼƬ����
		Person.setIcon(pIcon);
		Country.setIcon(cIcon);
		Natural.setIcon(nIcon);
		FoodSafe.setIcon(fIcon);
		Business.setIcon(bIcon);
		SearchAll.setIcon(sIcon);
		//��ť��Ӽ����¼�
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

		//������ƶ�ʱ������
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(JProgressBarValue<JProgressBarMax){
					JProgressBarValue+=50;//Ĭ��Ϊ2
					jpb.setValue(JProgressBarValue);
					pictureLocationX+=6;
					Matchman.setLocation(pictureLocationX,pictureLocationY);
					jpbDownText.setText(jpbDownText.getText()+".");
					if(jpbDownText.getText().equals("���ڽ������........"))
						jpbDownText.setText("���ڽ������");
				}else if(JProgressBarValue == JProgressBarMax){
					//�Ƴ��������
					remove(leftText);
					remove(rightText);
					remove(NationalEmblem);
					remove(Matchman);
					remove(jpb);
					remove(jpbDownText);
					repaint();
					t.cancel();
					//��Ӳ������
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
		setTitle("���ɱ���");
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
