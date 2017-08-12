package TankClient;

import java.awt.*;
import TankClient.Tank.*;
import java.util.List;
public class Bullet {
	private static int ID = 1;
	private int x,y;	
	private int speedX = 30,speedY = 30;
	Dir dir;
	private boolean isAlive = true;
	private int bullet_W = 5,bullet_H = 1;
	int tankID;
	int id;
	//�����ж��ӵ��Ĺ���
	private boolean good;
	TankClient tc = null;
	public Bullet(int tankID,int x,int y,boolean good ,Dir dir,TankClient tc){
		this.tankID = tankID;
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.tc = tc;
		this.id =ID++; 
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		if(tc.myTank.isGood()){
			g.setColor(Color.CYAN);
		}else{
			g.setColor(Color.YELLOW);
		}
		//g.fillRect(x, y, bullet_W, bullet_H);
		switch(dir){
		case L:
			g.fillRect(x, y, bullet_W, bullet_H);
		break;
		case U:
			g.fillRect(x, y, bullet_H, bullet_W);
			break;
		case R:
			g.fillRect(x, y, bullet_W, bullet_H);
			break;
		case D:
			g.fillRect(x, y, bullet_H, bullet_W);
			break;
		}		
		g.setColor(c);
		move();
	}
	private void move() {
		switch(dir){
		case L://����
			x-=speedX;
			break;
		case U://����
			y-=speedY;
			break;
		case R://����
			x+=speedX;
			break;
		case D://����
			y+=speedY;
			break;
		}
		if(x<0||y<0||x>TankClient.GAME_W||y>TankClient.GAME_W){
			isAlive = false;
		}		
	}
	//���������ײ���ķ���
	public Rectangle getRec(){
		return new Rectangle(x,y,bullet_W,bullet_H);
	}
	//�����ײ
	public boolean isHitTank(Tank t){
		if(this.isAlive&&t.isAlive()&&this.getRec().intersects(t.getRec())&&this.good!=t.isGood()){
			t.setAlive(false);
			this.isAlive = false;
			return true;
		}
		return false;
	}
	//�����һ����ײ��⣬���ڱ���˫��ѭ��
	public boolean isHitTanks(List<Tank> tanks){
		for(int i = 0;i<tanks.size();i++){
			if(isHitTank(tanks.get(i))){
				return true;
			}
		}
		return false ;
	}
//getter&setter
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
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}


