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
	
	public void Send(String payload) throws IOException
	{
		Packet packet = new Packet((byte)1, 1000, mServerAddress, mServerPort, payload);
		mUdpClient.Send(packet);
	}
	
	public BufferedReader Receive() throws IOException
	{
		Packet packet = mUdpClient.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
}
