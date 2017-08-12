package TankClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class BulletDeadMsg implements Msg {
	int msgType = Msg.BULLET_DEAE_MSG;
	int tankId;
	int id;
	TankClient tc;
	public BulletDeadMsg(TankClient tc){
		this.tc = tc;
	}
	public BulletDeadMsg(int tankId,int id){
		this.tankId = tankId;
		this.id = id;
	}
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);			
			dos.writeInt(tankId);	
			dos.writeInt(id);			
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

	@Override
	public void parse(DataInputStream dis) {
		try {
			int tankId= dis.readInt();
			int id = dis.readInt();
//System.out.println("id:"+id+"-x:"+x+"-y:"+y+"-direction£º"+direction+"-good: "+good);
			for(int i = 0;i<tc.bullets.size();i++){
				Bullet b= tc.bullets.get(i);
				if(b.tankID==tankId&&b.id==id){
					b.setAlive(false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
