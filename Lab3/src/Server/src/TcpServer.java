import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TcpServer
{
	private int mPort;
	private InetAddress mClientAddress;
	private int mClientPort;
	private InetAddress mRouterAddress;
	private int mRouterPort;
	
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

			System.out.println("Packet type: " + packet.PacketType);
			System.out.println("Sequence number: " + packet.SequenceNumber);
			System.out.println("Client address: " + packet.PeerAddress);
			System.out.println("Peer port: " + packet.PeerPort);
			
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
			ByteBuffer output = ByteBuffer.allocate(1024);
			
			// Packet type
			output.put((byte) 2);
			
			// Sequence Number
			output.putInt(2000);
			
			// Peer IP
			output.put(mClientAddress.getAddress());
			
			// Peer port
			output.putShort((short) mClientPort);
			
			for (char c: payload.toCharArray())
			{
				output.put((byte) c);
			}

			DatagramPacket dp = new DatagramPacket(output.array(), output.position(), mRouterAddress, mRouterPort);
			ds.send(dp);
		}
	}
}
