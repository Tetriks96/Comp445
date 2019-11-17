import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class UDPClient
{
	private int mPort;
	private InetAddress mRouterAddress;
	private int mRouterPort;
	private DatagramChannel mDatagramChannel;
	
	public UDPClient(int port) throws IOException
	{
		mPort = port;
		mRouterAddress = InetAddress.getByName("localhost");
		mRouterPort = 3000;
		mDatagramChannel = DatagramChannel.open();
		mDatagramChannel.socket().bind(new InetSocketAddress(mPort));
	}
	
	public void Send(Packet packet) throws IOException, InterruptedException
	{
		byte[] output = packet.getBytes();

		DatagramPacket dp = new DatagramPacket(output, output.length, mRouterAddress, mRouterPort);
		
		mDatagramChannel.socket().send(dp);
	}
	
	public Packet Receive() throws IOException
	{
	    byte[] buf = new byte[1024];
	    DatagramPacket dp = new DatagramPacket(buf, 1024);
	    mDatagramChannel.socket().receive(dp);
	    
	    byte[] data = Arrays.copyOfRange(dp.getData(), 0, dp.getLength());
	    
	    Packet packet = new Packet(data);

	    System.out.println(packet.GetHeader());
	    if (packet.Payload != null && packet.Payload.length() > 0)
	    {
	    	System.out.println(packet.Payload);
	    }
	    System.out.println();
	    
	    return packet;
	}
}
