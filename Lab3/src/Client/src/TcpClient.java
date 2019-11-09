import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TcpClient
{
	private int mPort;
	private InetAddress mServerAddress;
	private short mServerPort;
	private InetAddress mRouterAddress;
	private int mRouterPort;
	
	public TcpClient(int port, InetAddress serverAddress, short serverPort) throws UnknownHostException
	{
		mPort = port;
		mServerAddress = serverAddress;
		mServerPort = serverPort;
		mRouterAddress = InetAddress.getByName("localhost");
		mRouterPort = 3000;
	}
	
	public void Send(String payload) throws IOException
	{
		try
		(
			DatagramSocket ds = new DatagramSocket(mPort);
		)
		{
			Packet packet = new Packet((byte)1, 1000, mServerAddress, mServerPort, payload);
			
			byte[] output = packet.getBytes();

			DatagramPacket dp = new DatagramPacket(output, output.length, mRouterAddress, mRouterPort);
			ds.send(dp);
		}
	}
	
	public BufferedReader Receive() throws IOException
	{
		try
		(
			DatagramSocket ds = new DatagramSocket(mPort);
		)
		{
		    byte[] buf = new byte[1024];
		    DatagramPacket dp = new DatagramPacket(buf, 1024);
		    ds.receive(dp);
		    
		    byte[] data = Arrays.copyOfRange(dp.getData(), 0, dp.getLength());
		    
		    Packet packet = new Packet(data);

		    System.out.println(packet.GetHeader());
			
			return new BufferedReader(new StringReader(packet.Payload));
		}
	}
}
