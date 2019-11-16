import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class TcpServer
{
	private UDPServer mUdpServer;
	
	public TcpServer(int port) throws IOException
	{
		mUdpServer = new UDPServer(port);
	}

	public BufferedReader Receive() throws IOException
	{
		return mUdpServer.Receive();
	}
	
	public void Send(String payload) throws IOException
	{
		mUdpServer.Send(payload);
	}
}
