import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet
{
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
		ByteBuffer bytes = ByteBuffer.allocate(11 + Payload.length());
		
		bytes.put(PacketType);
		bytes.putInt(SequenceNumber);
		bytes.put(PeerAddress.getAddress());
		bytes.putShort(PeerPort);
		
		for (char c: Payload.toCharArray())
		{
			bytes.put((byte) c);
		}
		
		return bytes.array();
	}
	
	public String GetHeader()
	{
		String header = "";
		header += "Packet type: " + PacketType + "\n";
		header += "Sequence number: " + SequenceNumber + "\n";
		header += "Peer address: " + PeerAddress + "\n";
		header += "Peer port: " + PeerPort;

		return header;
	}
}
