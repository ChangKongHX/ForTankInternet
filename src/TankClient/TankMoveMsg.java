package TankClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg implements Msg {
	int msgType = Msg.TANK_MOVE_MSG;
	int id;
	Dir dir;
	int x;
	int y;
	TankClient tc;
	public TankMoveMsg(TankClient tc){
		this.tc = tc;
	}
	public TankMoveMsg(int id,Dir dir,int x,int y){
		this.id = id;
		this.dir = dir;
		this.x= x;
		this.y = y;
	}
	public void send(DatagramSocket ds, String IP, int udpPort) {		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);	
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
			dos.writeInt(x);
			dos.writeInt(y);
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
			int id = dis.readInt();
			if(tc.myTank.id==id){
				return;
			}
			Dir direction = Dir.values()[dis.readInt()];
			int x = dis.readInt();
			int y = dis.readInt();
//System.out.println("id:"+id+"-x:"+x+"-y:"+y+"-direction£º"+direction+"-good: "+good);
			boolean exits = false;
			for(int i = 0;i<tc.tanks.size();i++){
				Tank t = tc.tanks.get(i);
				if(t.id==id){
					t.setDirection(direction);
					t.setX(x);
					t.setY(y);
					exits = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
