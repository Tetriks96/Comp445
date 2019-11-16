import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class TcpServer
{
	public static final byte SYN = 0;
	public static final byte SYN_ACK = 1;
	public static final byte ACK = 2;
	public static final byte DATA = 3;
	
	private InetAddress mClientAddress;
	private short mClientPort;
	private UDPServer mUdpServer;
	
	public TcpServer(int port) throws IOException
	{
		mUdpServer = new UDPServer(port);
	}

	public BufferedReader Receive() throws IOException
	{
		Handshake();
		Packet packet = mUdpServer.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	public void Send(String payload) throws IOException
	{
		Packet packet = new Packet(DATA, 3000, mClientAddress, mClientPort, payload);
		mUdpServer.Send(packet);
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = mUdpServer.Receive();
		mClientAddress = syn.PeerAddress;
		mClientPort = syn.PeerPort;
		
		Packet synAck = new Packet(SYN_ACK, 1000, mClientAddress, mClientPort, null);
		mUdpServer.Send(synAck);
		
		Packet ack = mUdpServer.Receive();
	}
}
