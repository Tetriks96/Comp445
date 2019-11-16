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
	
	private int mSequenceNumber;
	private int mClientSequenceNumber;
	
	public TcpServer(int port) throws IOException
	{
		mUdpServer = new UDPServer(port);
		mSequenceNumber = 0;
	}

	public BufferedReader Receive() throws IOException
	{
		Handshake();
		Packet packet = InnerReceive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	private Packet InnerReceive() throws IOException
	{
		Packet packet = mUdpServer.Receive();
		mSequenceNumber++;
		return packet;
	}
	
	public void Send(String payload) throws IOException
	{
		Packet packet = new Packet(DATA, mClientSequenceNumber, mClientAddress, mClientPort, payload);
		Send(packet);
	}
	
	private void Send(Packet packet) throws IOException
	{
		mUdpServer.Send(packet);
		mClientSequenceNumber++;
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = InnerReceive();
		mClientAddress = syn.PeerAddress;
		mClientPort = syn.PeerPort;
		mClientSequenceNumber = syn.SequenceNumber;
		
		Packet synAck = new Packet(SYN_ACK, mSequenceNumber, mClientAddress, mClientPort, null);
		Send(synAck);
		
		Packet ack = InnerReceive();
	}
}
