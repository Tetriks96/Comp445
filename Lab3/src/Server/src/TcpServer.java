import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class TcpServer
{
	private int mPort;
	private InetAddress mClientAddress;
	private short mClientPort;
	private InetAddress mRouterAddress;
	private short mRouterPort;
	
	public TcpServer(int port) throws IOException
	{
		mPort = port;
		mClientAddress = InetAddress.getByName("localhost");
		mClientPort = 8081;
		mRouterAddress = InetAddress.getByName("localhost");
		mRouterPort = 3000;
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
	
	public void Send(String payload) throws IOException
	{
		try
		(
			DatagramSocket ds = new DatagramSocket(mPort);
		)
		{
			Packet packet = new Packet((byte)2, 2000, mClientAddress, mClientPort, payload);
			
			byte[] output = packet.getBytes();

			DatagramPacket dp = new DatagramPacket(output, output.length, mRouterAddress, mRouterPort);
			ds.send(dp);
		}
	}
}
