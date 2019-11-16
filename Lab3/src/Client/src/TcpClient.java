import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
	
	public TcpClient(int port, InetAddress serverAddress, short serverPort) throws UnknownHostException
	{
		mServerAddress = serverAddress;
		mServerPort = serverPort;
		mUdpClient = new UDPClient(port);
		
		mSequenceNumber = 0;
	}
	
	public void Send(String payload) throws IOException, InterruptedException
	{
		Handshake();
		Thread.sleep(100);
		
		Packet packet = new Packet(DATA, mServerSequenceNumber, mServerAddress, mServerPort, payload);
		Send(packet);
	}
	
	private void Send(Packet packet) throws IOException
	{
		mUdpClient.Send(packet);
		mServerSequenceNumber++;
	}
	
	public BufferedReader Receive() throws IOException
	{
		Packet packet = InnerReceive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	private Packet InnerReceive() throws IOException
	{
		Packet packet = mUdpClient.Receive();
		mSequenceNumber++;
		return packet;
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = new Packet(SYN, mSequenceNumber, mServerAddress, mServerPort, null);
		Send(syn);
		
		Packet synAck = InnerReceive();
		mServerSequenceNumber = synAck.SequenceNumber;
		
		Packet ack = new Packet(ACK, mServerSequenceNumber, mServerAddress, mServerPort, null);
		Send(ack);
	}
}
