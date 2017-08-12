package TankClient;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Tank {
	int id;
	//���ڿ���̹�˵Ĵ�С�ı���
	static int  width1 = 10,length1 = 45;		//���ӵĿ�Ⱥ͸߶�
	static int width2 = 20,length2 = 20,plength = 5;		//����Ŀ�Ⱥ͸߶�
	//���ڿ���̹�˲��յ�������̹�˵��ٶ�
	private int x = 30,y = 30;
	private int speedX = 10,speedY = 10;
	//��������̹�˺û�����
	private boolean good;
	
	//̹���Ƿ񻹻���
	private boolean isAlive = true;
	//����һ�������������
	private static Random r =new Random();
	//���ڻ�ȡTankClient������
	TankClient tc;
	private Dir direction = Dir.D;
	//����һ�����ڿ���̹���ƶ��̶��������������ı���
	private int step = r.nextInt(10)+5;
	public Tank(int x,int y,boolean good){
		this.x = x;
		this.y = y;
		this.good = good;
	}
	//����һ�����췽��
	public Tank(int x,int y,boolean good,TankClient tc,Dir direction){
		this(x,y,good);
		this.direction = direction;
		this.tc = tc;
	}
	//�滭���������ڻ����Լ�
	public void drawTank(Graphics g){
		if(!isAlive){//̹�������Ͳ���,���ж��Ƿ���Ҫ�Ƴ�
			if(!good){
				tc.tanks.remove(this);
				tc.setEnemyCount(tc.tanks.size());
			}
			return ;
		}
		if(good) g.setColor(Color.CYAN);
		else g.setColor(Color.yellow);
		switch(direction){
		case L://��ʾ����
			//��������
			g.fill3DRect(x, y,length1,width1 ,false);
			//�м�����
			g.fill3DRect(x+(length1-length2)/2, y+width1, width2, length2,false);
			//���������
			g.fill3DRect(x, y+width1+width2,length1 , width1,false);
			//ƨ��
			g.fill3DRect(x+(length1-length2)/2+length2,y+width1 , plength, width2,false);
			//Բ��
			g.drawOval(x+(length1-length2)/2+length2/8, y+width1+width2/8,
					width2/4*3,width2/4*3);
			//��Ͳ
			g.drawLine(x+length1/2, y+width1+width2/2,
					x-5,  y+width1+width2/2);
			//����̹�˵ı��
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+(length1-length2)/2+length2/8-width2/4*3, y+width1+width2/8+width2/4*3/2);
			break;
		case U://����
			//�������
			g.fill3DRect(x, y, width1, length1,false);
			//�м������
			g.fill3DRect(x+width1, y+(length1-length2)/2, width2, length2,false);
			//�ұ�����
			g.fill3DRect(x+width1+width2, y, width1, length1,false);
			//̹�˵�ƨ��
			g.fill3DRect(x+width1, y+(length1-length2)/2+length2, width2, plength,false);
			//Բ��
			g.drawOval(x+width1+width2/8, y+(length1-length2)/2+length2/8,
					width2/4*3,width2/4*3 );
			//��Ͳ
			g.drawLine(x+width1+width2/2, y+length1/2,
					x+width1+width2/2, y-5);
			//����̹�˵ı��
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+width1+width2/8-width2/4*3, y+(length1-length2)/2+length2/8+width2/4*3/2);
			break;
		case R://����
			//��������
			g.fill3DRect(x, y,length1,width1 ,false);
			//�м�����
			g.fill3DRect(x+(length1-length2)/2, y+width1, width2, length2,false);
			//���������
			g.fill3DRect(x, y+width1+width2,length1 , width1,false);
			//ƨ��
			g.fill3DRect(x+(length1-length2)/2-plength,y+width1 , plength, width2,false);
			//Բ��
			g.drawOval(x+(length1-length2)/2+length2/8, y+width1+width2/8,
					width2/4*3,width2/4*3);
			//��Ͳ
			g.drawLine(x+length1/2, y+width1+width2/2,
					x+length1+5,  y+width1+width2/2);
			//����̹�˵ı��
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+(length1-length2)/2+length2/8-width2/4*3, y+width1+width2/8+width2/4*3/2);
			break;
		case D://����
			//�������
			g.fill3DRect(x, y, width1, length1,false);
			//�м������
			g.fill3DRect(x+width1, y+(length1-length2)/2, width2, length2,false);
			//�ұ�����
			g.fill3DRect(x+width1+width2, y, width1, length1,false);
			//̹�˵�ƨ��
			g.fill3DRect(x+width1, y+(length1-length2)/2-plength, width2, plength,false);
			//Բ��
			g.drawOval(x+width1+width2/8, y+(length1-length2)/2+length2/8,
					width2/4*3,width2/4*3 );
			//��Ͳ
			g.drawLine(x+width1+width2/2, y+length1/2,
					x+width1+width2/2, y+length1+5);
			//����̹�˵ı��
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+width1+width2/8-width2/4*3, y+(length1-length2)/2+length2/8+width2/4*3/2);
			break;
		}
	}
	//����һ�����Ը��ݷ������̹���ƶ��ķ���
	public void move(){
		switch(direction){
		case L:
			x-=speedX;
			break;
		case U:
			y-=speedY;
			break;
		case R:
			x+=speedX;
			break;
		case D:
			y+=speedY;
			break;
		}
		if(x<0) x=0;
		if(y<30) y=30;
		if(x+length1>TankClient.GAME_W) x = TankClient.GAME_W-length1;
		if(y+length1>TankClient.GAME_H) y = TankClient.GAME_H-length1;
//		if(!good){			
//			Dir [] directions = Dir.values();
//			if(step==0||x == 0||y==30||x == TankClient.GAME_W-length1
//					||y==TankClient.GAME_H-length1){
//				step = r.nextInt(10)+5;
//				int randomNum = r.nextInt(directions.length);
//				direction = directions[randomNum] ;
//			}
//			if(r.nextInt(40)>35)	
//				this.fire();
//			step--;
//		}
	}
	//�¼��������ƣ����ڿ���̹�˵��ƶ�����
	public void keyPressed(KeyEvent e){
		Dir oldDir = this.direction;
		int oldX = this.x;
		int oldY = this.y;
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_A:
			direction = Dir.L;
			move();
			break;
		case KeyEvent.VK_W:
			direction = Dir.U;
			move();
			break;
		case KeyEvent.VK_D:
			direction = Dir.R;
			move();
			break;
		case KeyEvent.VK_S:
			direction = Dir.D;	
			move();		
			break;
		}
		if(direction!=oldDir||x!=oldX||y!=oldY){
			TankMoveMsg msg = new TankMoveMsg(id,direction,x,y);
			tc.fnc.send(msg);
		}
	}
	//�����ɿ�
	public void keyReleased(KeyEvent e) {		
		if(e.getKeyCode()== KeyEvent.VK_J)
			fire();
	}
	//����һ������ķ���
	public Bullet fire(){
		if(!isAlive) 
			return null;
		Bullet b = null;
		switch(direction){
		case L://��
			b = new Bullet(id,x-5,  y+width1+width2/2,good,direction,tc);
			break;
		case U://��
			b = new Bullet(id,x+width1+width2/2, y-5,good,direction,tc);
			break;
		case R://��
			b = new Bullet(id,x+length1+5,  y+width1+width2/2,good,direction,tc);
			break;
		case D://��
			b = new Bullet(id,x+width1+width2/2, y+length1+5,good,direction,tc);
			break;
		}
		tc.bullets.add(b);
		
		BulletNewMsg msg = new BulletNewMsg(b);
		tc.fnc.send(msg);
		
		return b;
	}
	//setter&getter����
	public int getSpeedX() {
		return speedX;
	}
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public int getSpeedY() {
		return speedY;
	}
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	public Dir getDirection() {
		return direction;
	}
	public void setDirection(Dir direction) {
		this.direction = direction;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	//���ڼ����ײ�ĸ�������
	public Rectangle getRec(){
		Rectangle r = null;
		switch(direction){
		case L://����
		case R://����
			 r = new Rectangle(x,y,length1,width1*2+width2);
			break;
		case U://����		
		case D://����
			r = new Rectangle(x,y,width1*2+width2,length1);
			break;
		}
		return r;
	}
}
