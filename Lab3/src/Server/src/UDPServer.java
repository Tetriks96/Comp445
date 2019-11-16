import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPServer
{
	private int mPort;
	private InetAddress mRouterAddress;
	private short mRouterPort;
	
	public UDPServer(int port) throws IOException
	{
		mPort = port;
		mRouterAddress = InetAddress.getByName("localhost");
		mRouterPort = 3000;
	}

	public Packet Receive() throws IOException
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
		    System.out.println();
		    
		    return packet;
		}
	}
	
	public void Send(Packet packet) throws IOException
	{
		try
		(
			DatagramSocket ds = new DatagramSocket(mPort);
		)
		{
			byte[] output = packet.getBytes();

			DatagramPacket dp = new DatagramPacket(output, output.length, mRouterAddress, mRouterPort);
			ds.send(dp);
		}
	}
}
