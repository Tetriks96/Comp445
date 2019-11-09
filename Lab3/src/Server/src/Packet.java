import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
}
