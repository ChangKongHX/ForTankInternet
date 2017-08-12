package TankClient;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Tank {
	int id;
	//用于控制坦克的大小的变量
	static int  width1 = 10,length1 = 45;		//轮子的宽度和高度
	static int width2 = 20,length2 = 20,plength = 5;		//主体的宽度和高度
	//用于控制坦克参照点的坐标和坦克的速度
	private int x = 30,y = 30;
	private int speedX = 10,speedY = 10;
	//用于区分坦克好坏的量
	private boolean good;
	
	//坦克是否还活着
	private boolean isAlive = true;
	//定义一个随机数产生器
	private static Random r =new Random();
	//用于获取TankClient的引用
	TankClient tc;
	private Dir direction = Dir.D;
	//定义一个用于控制坦克移动固定步数才随机方向的变量
	private int step = r.nextInt(10)+5;
	public Tank(int x,int y,boolean good){
		this.x = x;
		this.y = y;
		this.good = good;
	}
	//定义一个构造方法
	public Tank(int x,int y,boolean good,TankClient tc,Dir direction){
		this(x,y,good);
		this.direction = direction;
		this.tc = tc;
	}
	//绘画方法，用于画出自己
	public void drawTank(Graphics g){
		if(!isAlive){//坦克死亡就不画,并判断是否需要移除
			if(!good){
				tc.tanks.remove(this);
				tc.setEnemyCount(tc.tanks.size());
			}
			return ;
		}
		if(good) g.setColor(Color.CYAN);
		else g.setColor(Color.yellow);
		switch(direction){
		case L://表示向左
			//上面轮子
			g.fill3DRect(x, y,length1,width1 ,false);
			//中间主体
			g.fill3DRect(x+(length1-length2)/2, y+width1, width2, length2,false);
			//下面的轮子
			g.fill3DRect(x, y+width1+width2,length1 , width1,false);
			//屁股
			g.fill3DRect(x+(length1-length2)/2+length2,y+width1 , plength, width2,false);
			//圆盖
			g.drawOval(x+(length1-length2)/2+length2/8, y+width1+width2/8,
					width2/4*3,width2/4*3);
			//炮筒
			g.drawLine(x+length1/2, y+width1+width2/2,
					x-5,  y+width1+width2/2);
			//画出坦克的编号
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+(length1-length2)/2+length2/8-width2/4*3, y+width1+width2/8+width2/4*3/2);
			break;
		case U://向上
			//左边轮子
			g.fill3DRect(x, y, width1, length1,false);
			//中间的主体
			g.fill3DRect(x+width1, y+(length1-length2)/2, width2, length2,false);
			//右边轮子
			g.fill3DRect(x+width1+width2, y, width1, length1,false);
			//坦克的屁股
			g.fill3DRect(x+width1, y+(length1-length2)/2+length2, width2, plength,false);
			//圆盖
			g.drawOval(x+width1+width2/8, y+(length1-length2)/2+length2/8,
					width2/4*3,width2/4*3 );
			//炮筒
			g.drawLine(x+width1+width2/2, y+length1/2,
					x+width1+width2/2, y-5);
			//画出坦克的编号
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+width1+width2/8-width2/4*3, y+(length1-length2)/2+length2/8+width2/4*3/2);
			break;
		case R://向右
			//上面轮子
			g.fill3DRect(x, y,length1,width1 ,false);
			//中间主体
			g.fill3DRect(x+(length1-length2)/2, y+width1, width2, length2,false);
			//下面的轮子
			g.fill3DRect(x, y+width1+width2,length1 , width1,false);
			//屁股
			g.fill3DRect(x+(length1-length2)/2-plength,y+width1 , plength, width2,false);
			//圆盖
			g.drawOval(x+(length1-length2)/2+length2/8, y+width1+width2/8,
					width2/4*3,width2/4*3);
			//炮筒
			g.drawLine(x+length1/2, y+width1+width2/2,
					x+length1+5,  y+width1+width2/2);
			//画出坦克的编号
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+(length1-length2)/2+length2/8-width2/4*3, y+width1+width2/8+width2/4*3/2);
			break;
		case D://向下
			//左边轮子
			g.fill3DRect(x, y, width1, length1,false);
			//中间的主体
			g.fill3DRect(x+width1, y+(length1-length2)/2, width2, length2,false);
			//右边轮子
			g.fill3DRect(x+width1+width2, y, width1, length1,false);
			//坦克的屁股
			g.fill3DRect(x+width1, y+(length1-length2)/2-plength, width2, plength,false);
			//圆盖
			g.drawOval(x+width1+width2/8, y+(length1-length2)/2+length2/8,
					width2/4*3,width2/4*3 );
			//炮筒
			g.drawLine(x+width1+width2/2, y+length1/2,
					x+width1+width2/2, y+length1+5);
			//画出坦克的编号
			g.setColor(Color.RED);
			g.drawString("ID: "+id,x+width1+width2/8-width2/4*3, y+(length1-length2)/2+length2/8+width2/4*3/2);
			break;
		}
	}
	//定义一个可以根据方向控制坦克移动的方法
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
	//事件监听机制，用于控制坦克的移动方向
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
	//按键松开
	public void keyReleased(KeyEvent e) {		
		if(e.getKeyCode()== KeyEvent.VK_J)
			fire();
	}
	//定义一个开火的方法
	public Bullet fire(){
		if(!isAlive) 
			return null;
		Bullet b = null;
		switch(direction){
		case L://左
			b = new Bullet(id,x-5,  y+width1+width2/2,good,direction,tc);
			break;
		case U://上
			b = new Bullet(id,x+width1+width2/2, y-5,good,direction,tc);
			break;
		case R://右
			b = new Bullet(id,x+length1+5,  y+width1+width2/2,good,direction,tc);
			break;
		case D://下
			b = new Bullet(id,x+width1+width2/2, y+length1+5,good,direction,tc);
			break;
		}
		tc.bullets.add(b);
		
		BulletNewMsg msg = new BulletNewMsg(b);
		tc.fnc.send(msg);
		
		return b;
	}
	//setter&getter方法
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
	//用于检测碰撞的辅助方法
	public Rectangle getRec(){
		Rectangle r = null;
		switch(direction){
		case L://向左
		case R://向右
			 r = new Rectangle(x,y,length1,width1*2+width2);
			break;
		case U://向上		
		case D://向下
			r = new Rectangle(x,y,width1*2+width2,length1);
			break;
		}
		return r;
	}
}
