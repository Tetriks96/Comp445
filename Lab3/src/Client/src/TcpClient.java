import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpClient
{
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
	}
	
	public BufferedReader Receive() throws IOException
	{
		Packet packet = mUdpClient.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = new Packet((byte)0, 1, mServerAddress, mServerPort, null);
		mUdpClient.Send(syn);
		
		Packet synAck = mUdpClient.Receive();
		
		Packet ack = new Packet((byte)2, 2001, mServerAddress, mServerPort, null);
		mUdpClient.Send(ack);
	}
}
