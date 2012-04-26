package at.bartinger.candroid.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import at.bartinger.candroid.Constants;

/**
 * This class specifies the Client or a Player in a game over the Network.
 * Make sure that you add the INTERNET PERMISSION in you AndroidManifest.xml
 * <uses-permission android:name="android.permission.INTERNET"></uses-permission>
 * @author Dominic Bartl / Bartinger
 *
 */

public class GlobalClient extends Thread{

	private int port;
	private PacketListener listener = null;

	private DatagramSocket socket;
	private InetAddress sendto;

	Context context;


	public GlobalClient(Context c, String peerIP, int listenPort, int peerPort) throws SocketException, UnknownHostException {
		port = peerPort;
		socket = new DatagramSocket(listenPort);
		sendto = InetAddress.getByName(peerIP);
	}
	
	public GlobalClient(Context c, String peerIP, int listenPort, int peerPort, PacketListener listener) throws SocketException, UnknownHostException {
		port = peerPort;
		socket = new DatagramSocket(listenPort);
		sendto = InetAddress.getByName(peerIP);
		this.listener = listener;
	}

	@Override
	public void run() {

		DatagramPacket packet = new DatagramPacket( new byte[11], 11 );
		try {
			socket.receive( packet );
		} catch (IOException e) {
			Log.e(Constants.LOGTAG, "Receiveing failed!", e.fillInStackTrace());
		}
		if(listener != null){
			listener.onRecivePacket(packet.getData());
		}

	}




	public void send(InetAddress address,String msg) throws IOException{
		socket.send(new DatagramPacket(msg.getBytes(),msg.getBytes().length,address,port));
	}

	public void sendToPeer(String msg) throws IOException{
		socket.send(new DatagramPacket(msg.getBytes(),msg.getBytes().length,sendto,port));
	}

	public void send(InetAddress address,byte[] data) throws IOException{
		socket.send(new DatagramPacket(data,data.length,address,port));
	}

	public void sendToPeer(byte[] data) throws IOException{
		socket.send(new DatagramPacket(data,data.length,sendto,port));
	}

	public String getExternalIp() throws MalformedURLException, IOException{
		String myip = "";
		Scanner scanner = new Scanner(new URL("http://bartinger.kilu.org/myip.php").openStream());
		while(scanner.hasNextLine()){
			myip= scanner.nextLine();
		}
		scanner.close();
		return myip;
	}

	public InetAddress getExternalIP() throws IOException{
		String myip = "";
		Scanner scanner = new Scanner(new URL("http://bartinger.kilu.org/myip.php").openStream());
		while(scanner.hasNextLine()){
			myip= scanner.nextLine();
		}
		scanner.close();
		return InetAddress.getByName(myip);
	}
	
	public void setPacketListener(PacketListener listener){
		this.listener = listener;
	}

	public void close(){
		socket.close();
		this.destroy();
	}
}
