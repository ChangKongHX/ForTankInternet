package TankClient;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
/**
 * @version坦克大战2.0（网络版）
 * @author 长空
 * @team 云计算&大数据16级全体成员
 * */
public class TankClient extends Frame{
	public static final int GAME_W = 800,GAME_H = 600;
	//构建一个我的坦克
	Tank myTank = new Tank(30,30,true,this,Dir.D);
	//创建一个容器，用于装子弹
	List <Bullet>bullets = new ArrayList<Bullet>();
	//创建一个容器，用于装敌人的坦克
	List <Tank> tanks = new ArrayList<Tank>();
	private int enemyCount = 10;
	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}
	//定义一张图片，用于双缓冲
	Image offScreenImage = null;
	ForNetClient fnc = new ForNetClient(this);
	ConnDialog dialog = new ConnDialog();
	//重写paint方法，用于画出坦克
	public void paint(Graphics g){
		//输出当前炮弹的数目
		g.drawString("bulletsCount:"+bullets.size(), 10, 60);
		for(int i = 0;i<bullets.size();i++){
			Bullet b = bullets.get(i);
			
			if(b!=null&&b.isAlive()!=false){//如果子弹还还活着
				b.draw(g);
				//b.isHitTanks(tanks);
				if(b.isHitTank(myTank)){//发送坦克死亡的消息
					TankDeadMsg msg = new TankDeadMsg(myTank.id);
					fnc.send(msg);
					BulletDeadMsg bdMsg = new BulletDeadMsg(b.tankID,b.id);
					fnc.send(bdMsg);
				}
			}else if(b.isAlive()==false){//如果子弹死亡就将其移出容器
				bullets.remove(b);
			}
		}
		
		myTank.drawTank(g);
		//画出其他坦克
		for(int i = 0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			t.drawTank(g);
		}
	}
	//重写update方法
	public void update(Graphics g){
		if(offScreenImage==null){
			offScreenImage = this.createImage(GAME_W,GAME_H);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.darkGray);
		gOffScreen.fillRect(0, 0, GAME_W, GAME_H);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	//用于启动坦克游戏的主界面
	public void launchFrame(){
		//添加敌人的坦克
//		for(int i= 0;i<enemyCount;i++){
//			tanks.add(new Tank(100+50*i,
//					60,false,this,Dir.D));
//		}
		this.setTitle("坦克大战2.0（网络版）");
		//设置游戏界面的背景色
		this.setBackground(Color.darkGray);
		//设置窗口出现的位置
		this.setLocation(300,100);
		this.setSize(GAME_W, GAME_H);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMoniter());
		//窗口大小不可更改
		this.setResizable(false);
		this.setVisible(true);
		//启动界面的时候顺便启动坦克移动的线程
		new Thread(new PaintThread()).start();
		//fnc.connect("127.0.0.1", TankServer.TCP_PORT);
	}
	public static void main(String []args){
		TankClient tankClient = new TankClient();
		tankClient.launchFrame();
	}
	//定义一个内部类，
	public class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
//				//利用此线程完成敌人坦克的移动
//				if(tanks.isEmpty()){
//					return;
//				}else{
//					for(int i = 0;i<tanks.size();i++){
//						Tank t =tanks.get(i);
//						t.move();
//					}
//				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public class KeyMoniter extends KeyAdapter{
		public void keyReleased(KeyEvent k) {
			myTank.keyReleased(k);
		}
		public void keyPressed(KeyEvent k) {
			int key = k.getKeyCode();
			if(key==KeyEvent.VK_C){
				dialog.setVisible(true);
			}else{
				myTank.keyPressed(k);
			}
			
		}
	}
	class ConnDialog extends Dialog{
		Button b = new Button("确定");
		TextField tfIP = new TextField("127.0.0.1",12);
		TextField tfPort = new TextField(""+TankServer.TCP_PORT,4);
		TextField tfUDPPort = new TextField("2333",4);
		public ConnDialog(){
			super(TankClient.this,true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP: "));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfUDPPort);
			this.add(b);
			this.setTitle("链接服务器");
			this.setLocation(300,300);
			this.setResizable(false);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}				
			});
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int UDPPort = Integer.parseInt(tfUDPPort.getText().trim());
					fnc.setUdpPort(UDPPort);
					fnc.connect(IP, port);
					setVisible(false);
				}
				
			});
		}
	}
}
