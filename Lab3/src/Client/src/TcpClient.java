import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TcpClient
{
	private int mPort;
	private InetAddress mServerAddress;
	private int mServerPort;
	
	public TcpClient(int port, InetAddress serverAddress, int serverPort) throws UnknownHostException
	{
		mPort = port;
		mServerAddress = serverAddress;
		mServerPort = serverPort;
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
			output.put((byte) 1);
			
			// Sequence Number
			output.putInt(1000);
			
			// Peer IP
			output.put(mServerAddress.getAddress());
			
			// Peer port
			output.putShort((short) mServerPort);
			
			for (char c: payload.toCharArray())
			{
				output.put((byte) c);
			}

			DatagramPacket dp = new DatagramPacket(output.array(), output.position(), mServerAddress, mServerPort);
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

			System.out.println("Packet type: " + packet.PacketType);
			System.out.println("Sequence number: " + packet.SequenceNumber);
			System.out.println("Client address: " + packet.PeerAddress);
			System.out.println("Peer port: " + packet.PeerPort);
			
			return new BufferedReader(new StringReader(packet.Payload));
		}
	}
}
