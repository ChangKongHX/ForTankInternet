package TankClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg implements Msg{
	int msgType = Msg.TANK_NEW_MSG;
	Tank tank;
	TankClient tc;
	public TankNewMsg(TankClient tc){
		this.tc = tc;
	}
	public TankNewMsg(Tank tank){
		this.tank = tank;
	}
	public void send(DatagramSocket ds,String IP,int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.getX());
			dos.writeInt(tank.getY());
			dos.writeInt(tank.getDirection().ordinal());
			dos.writeBoolean(tank.isGood());
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
			
			int x = dis.readInt();
			int y = dis.readInt();
			Dir direction = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();
//System.out.println("id:"+id+"-x:"+x+"-y:"+y+"-direction£º"+direction+"-good: "+good);
			boolean exist = false;
			for(int i=0;i<tc.tanks.size();i++){
				Tank t = tc.tanks.get(i);
				if(t.id==id){
					exist = true;
					break;
				}
			}
			if(!exist){
				TankNewMsg tnMsg = new TankNewMsg(tc.myTank);
				tc.fnc.send(tnMsg);
				Tank t = new Tank(x,y,good,tc,direction);
				t.id = id;
				tc.tanks.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
