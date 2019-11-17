import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class TcpClient
{
	public static final byte SYN = 0;
	public static final byte SYN_ACK = 1;
	public static final byte ACK = 2;
	public static final byte DATA = 3;
	
	private InetAddress mServerAddress;
	private short mServerPort;
	private UDPClient mUdpClient;
	
	private int mSequenceNumber;
	private int mServerSequenceNumber;
	
	private Map<Integer, Packet> mSentPackets;
	
	private Packet mOutOfOrderPacket;
	
	public TcpClient(int port, InetAddress serverAddress, short serverPort) throws IOException
	{
		mServerAddress = serverAddress;
		mServerPort = serverPort;
		mUdpClient = new UDPClient(port);
		
		mSequenceNumber = 0;
		
		mSentPackets = new HashMap<Integer, Packet>();
		
		mOutOfOrderPacket = null;
	}
	
	public void Send(String payload) throws IOException, InterruptedException
	{
		Handshake();
		
		Packet data = new Packet(DATA, mServerSequenceNumber, mServerAddress, mServerPort, payload);
		Send(data);
	}
	
	public BufferedReader Receive() throws IOException, InterruptedException
	{
		Packet data;
		if (mOutOfOrderPacket != null)
		{
			data = mOutOfOrderPacket;
			mOutOfOrderPacket = null;
		}
		else
		{
			data = mUdpClient.Receive();
		}
		
		mSequenceNumber++;
		
		Packet ack = new Packet(ACK, mSequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
		
		return new BufferedReader(new StringReader(data.Payload));
	}
	
	private void Send(Packet packet) throws IOException, InterruptedException
	{
		mUdpClient.Send(packet);
		if (!mSentPackets.containsKey(packet.SequenceNumber))
		{
			mSentPackets.put(packet.SequenceNumber, packet);
		}
		Packet ack = mUdpClient.Receive();
		if (ack.PacketType != ACK)
		{
			mOutOfOrderPacket = ack;
			ack = mUdpClient.Receive();
		}
		mServerSequenceNumber = ack.SequenceNumber;
	}
	
	private void Handshake() throws IOException, InterruptedException
	{
		Packet syn = new Packet(SYN, mSequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(syn);
		
		Packet synAck = mUdpClient.Receive();
		mServerSequenceNumber = synAck.SequenceNumber;
		
		mSequenceNumber++;
		
		Packet ack = new Packet(ACK, mSequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
	}
}
