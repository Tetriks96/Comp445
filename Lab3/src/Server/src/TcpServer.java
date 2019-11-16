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
		mUdpServer = new UDPServer(port);
	}

	public BufferedReader Receive() throws IOException
	{
		Handshake();
		Packet packet = mUdpServer.Receive();
		return new BufferedReader(new StringReader(packet.Payload));
	}
	
	public void Send(String payload) throws IOException
	{
		Packet packet = new Packet((byte)3, 3000, mClientAddress, mClientPort, payload);
		mUdpServer.Send(packet);
	}
	
	private void Handshake() throws IOException
	{
		Packet syn = mUdpServer.Receive();
		mClientAddress = syn.PeerAddress;
		mClientPort = syn.PeerPort;
		
		Packet synAck = new Packet((byte)1, 1000, mClientAddress, mClientPort, null);
		mUdpServer.Send(synAck);
		
		Packet ack = mUdpServer.Receive();
	}
}
