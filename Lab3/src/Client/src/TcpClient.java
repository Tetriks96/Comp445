import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpClient
{
	private UDPClient mUdpClient;
	
	public TcpClient(int port, InetAddress serverAddress, short serverPort) throws UnknownHostException
	{
		mUdpClient = new UDPClient(port, serverAddress, serverPort);
	}
	
	public void Send(String payload) throws IOException
	{
		mUdpClient.Send(payload);
	}
	
	public BufferedReader Receive() throws IOException
	{
		return mUdpClient.Receive();
	}
}
