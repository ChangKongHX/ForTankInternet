package TankClient;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ForNetClient {
	private int udpPort;
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	String IP;//服务器的IP
	DatagramSocket ds = null;
	TankClient tc;
	//构造方法
	public ForNetClient(TankClient tc){
		this.tc = tc;
		
	}
	public void connect(String IP,int port){
		
		this.IP= IP;
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		Socket s = null;
		try {
			s = new Socket(IP,port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			//用于设定坦克的好坏。
			if(id%2==0) {
				tc.myTank.setGood(false);
			}else{
				tc.myTank.setGood(true);
			}
			
			System.out.println("Connected to server! And server give me a ID:"+id);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(s!=null){
				try {
						s.close();
						s=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UDPRecvThread()).start();
	}
	public void send(Msg msg){
		msg.send(ds,IP,TankServer.UDP_PORT);
	}
	private class UDPRecvThread implements Runnable{
		byte[] buf = new byte[1024];
		public void run() {
			while (ds!=null){
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				try {
				ds.receive(dp);
				parse(dp);
System.out.println("a packer received from server!");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg = null;
			switch(msgType){
			case Msg.TANK_NEW_MSG:
				msg = new TankNewMsg(ForNetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(ForNetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.BULLET_NEW_MSG:
				msg = new BulletNewMsg(ForNetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				msg = new TankDeadMsg(ForNetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.BULLET_DEAE_MSG:
				msg = new BulletDeadMsg(ForNetClient.this.tc);
				msg.parse(dis);
				break;
			}
		}		
	}
}
