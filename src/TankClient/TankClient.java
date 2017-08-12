package TankClient;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
/**
 * @version̹�˴�ս2.0������棩
 * @author ����
 * @team �Ƽ���&������16��ȫ���Ա
 * */
public class TankClient extends Frame{
	public static final int GAME_W = 800,GAME_H = 600;
	//����һ���ҵ�̹��
	Tank myTank = new Tank(30,30,true,this,Dir.D);
	//����һ������������װ�ӵ�
	List <Bullet>bullets = new ArrayList<Bullet>();
	//����һ������������װ���˵�̹��
	List <Tank> tanks = new ArrayList<Tank>();
	private int enemyCount = 10;
	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}
	//����һ��ͼƬ������˫����
	Image offScreenImage = null;
	ForNetClient fnc = new ForNetClient(this);
	ConnDialog dialog = new ConnDialog();
	//��дpaint���������ڻ���̹��
	public void paint(Graphics g){
		//�����ǰ�ڵ�����Ŀ
		g.drawString("bulletsCount:"+bullets.size(), 10, 60);
		for(int i = 0;i<bullets.size();i++){
			Bullet b = bullets.get(i);
			
			if(b!=null&&b.isAlive()!=false){//����ӵ���������
				b.draw(g);
				//b.isHitTanks(tanks);
				if(b.isHitTank(myTank)){//����̹����������Ϣ
					TankDeadMsg msg = new TankDeadMsg(myTank.id);
					fnc.send(msg);
					BulletDeadMsg bdMsg = new BulletDeadMsg(b.tankID,b.id);
					fnc.send(bdMsg);
				}
			}else if(b.isAlive()==false){//����ӵ������ͽ����Ƴ�����
				bullets.remove(b);
			}
		}
		
		myTank.drawTank(g);
		//��������̹��
		for(int i = 0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			t.drawTank(g);
		}
	}
	//��дupdate����
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
	//��������̹����Ϸ��������
	public void launchFrame(){
		//��ӵ��˵�̹��
//		for(int i= 0;i<enemyCount;i++){
//			tanks.add(new Tank(100+50*i,
//					60,false,this,Dir.D));
//		}
		this.setTitle("̹�˴�ս2.0������棩");
		//������Ϸ����ı���ɫ
		this.setBackground(Color.darkGray);
		//���ô��ڳ��ֵ�λ��
		this.setLocation(300,100);
		this.setSize(GAME_W, GAME_H);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMoniter());
		//���ڴ�С���ɸ���
		this.setResizable(false);
		this.setVisible(true);
		//���������ʱ��˳������̹���ƶ����߳�
		new Thread(new PaintThread()).start();
		//fnc.connect("127.0.0.1", TankServer.TCP_PORT);
	}
	public static void main(String []args){
		TankClient tankClient = new TankClient();
		tankClient.launchFrame();
	}
	//����һ���ڲ��࣬
	public class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
//				//���ô��߳���ɵ���̹�˵��ƶ�
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
		Button b = new Button("ȷ��");
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
			this.setTitle("���ӷ�����");
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
