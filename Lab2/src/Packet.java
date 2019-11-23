import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet
{
	public static final byte SYN = 0;
	public static final byte SYN_ACK = 1;
	public static final byte ACK = 2;
	public static final byte DATA = 3;
	
	public byte PacketType;
	public int SequenceNumber;
	public InetAddress PeerAddress;
	public short PeerPort;
	public String Payload;
	
	public Packet(byte[] data) throws UnknownHostException
	{
		PacketType = data[0];
		SequenceNumber = (new BigInteger(Arrays.copyOfRange(data, 1, 5))).intValue();
		PeerAddress = InetAddress.getByAddress(Arrays.copyOfRange(data, 5, 9));
		PeerPort = (new BigInteger(Arrays.copyOfRange(data, 9, 11))).shortValue();
	    Payload = new String(data, 11, data.length - 11);
	}
	
	public Packet(byte packetType, int sequenceNumber, InetAddress peerAddress, short peerPort, String payload)
	{
		PacketType = packetType;
		SequenceNumber = sequenceNumber;
		PeerAddress = peerAddress;
		PeerPort = peerPort;
	    Payload = payload;
	}
	
	public byte[] getBytes()
	{
		int payloadLength = Payload != null ? Payload.length() : 0;
		ByteBuffer bytes = ByteBuffer.allocate(11 + payloadLength);
		
		bytes.put(PacketType);
		bytes.putInt(SequenceNumber);
		bytes.put(PeerAddress.getAddress());
		bytes.putShort(PeerPort);
		
		if (Payload != null)
		{
			for (char c: Payload.toCharArray())
			{
				bytes.put((byte) c);
			}
		}
		
		return bytes.array();
	}
	
	public String GetHeader()
	{
		String header = "";
		header += "Packet type: " + PacketTypeToString(PacketType) + "\n";
		header += "Sequence number: " + SequenceNumber + "\n";
		header += "Peer address: " + PeerAddress + "\n";
		header += "Peer port: " + PeerPort;

		return header;
	}
	
	private String PacketTypeToString(byte type)
	{
		switch(type)
		{
		case SYN:
			return "SYN";
		case SYN_ACK:
			return "SYN-ACK";
		case ACK:
			return "ACK";
		case DATA:
			return "DATA";
		default:
			return "";
		}
	}
}
