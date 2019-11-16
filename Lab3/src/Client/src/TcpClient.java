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
	
	public TcpClient(int port, InetAddress serverAddress, short serverPort) throws UnknownHostException
	{
		mServerAddress = serverAddress;
		mServerPort = serverPort;
		mUdpClient = new UDPClient(port);
	}
	
	public void Send(String payload) throws IOException, InterruptedException
	{
		Handshake();
		Thread.sleep(100);
		
		Packet packet = new Packet((byte)3, 3001, mServerAddress, mServerPort, payload);
		mUdpClient.Send(packet);
		Thread.sleep(100);
	}
	
	public BufferedReader Receive() throws IOException
	{
		Packet packet = mUdpClient.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = new Packet(SYN, 1, mServerAddress, mServerPort, null);
		mUdpClient.Send(syn);
		
		Packet synAck = mUdpClient.Receive();
		
		Packet ack = new Packet(ACK, 2001, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
	}
}
