package TankClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class BulletNewMsg implements Msg {
	int msgType = Msg.BULLET_NEW_MSG;
	TankClient tc;
	Bullet b;
	public BulletNewMsg(TankClient tc){
		this.tc = tc;
	}
	public BulletNewMsg(Bullet b){
		this.b = b;
	}
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);	
			dos.writeInt(b.tankID);	
			dos.writeInt(b.id);
			dos.writeInt(b.getX());
			dos.writeInt(b.getY());
			dos.writeInt(b.dir.ordinal());
			dos.writeBoolean(b.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();		
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
			ds.send(dp);
		} catch(SocketException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void parse(DataInputStream dis) {
		try {
			int tankID = dis.readInt();
			if(tankID ==tc.myTank.id){
				return;
			}
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Dir direction = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();
//System.out.println("id:"+id+"-x:"+x+"-y:"+y+"-direction£º"+direction+"-good: "+good);
			Bullet b = new Bullet(tankID, x,y,good,direction,tc);
			b.id = id;
			tc.bullets.add(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
