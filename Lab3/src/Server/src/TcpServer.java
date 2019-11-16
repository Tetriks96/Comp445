import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class TcpServer
{
	private InetAddress mClientAddress;
	private short mClientPort;
	private UDPServer mUdpServer;
	
	public TcpServer(int port) throws IOException
	{
		mClientAddress = InetAddress.getByName("localhost");
		mClientPort = 8081;
		mUdpServer = new UDPServer(port);
	}

	public BufferedReader Receive() throws IOException
	{
		Packet packet = mUdpServer.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	public void Send(String payload) throws IOException
	{
		Packet packet = new Packet((byte)2, 2000, mClientAddress, mClientPort, payload);
		mUdpServer.Send(packet);
	}
}
