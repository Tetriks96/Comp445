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
		
		Packet data = new Packet(DATA, mServerSequenceNumber, mServerAddress, mServerPort, payload);
		mUdpClient.Send(data);
		
		Packet ack = mUdpClient.Receive();
	}
	
	public BufferedReader Receive() throws IOException, InterruptedException
	{
		Packet data = mUdpClient.Receive();
		
		Packet ack = new Packet(ACK, mSequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
		
		return new BufferedReader(new StringReader(data.Payload));
	}
	
	private void Handshake() throws IOException, InterruptedException
	{
		Packet syn = new Packet(SYN, mSequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(syn);
		
		Packet synAck = mUdpClient.Receive();
		mServerSequenceNumber = synAck.SequenceNumber;
		
		Packet ack = new Packet(ACK, synAck.SequenceNumber, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
	}
}
