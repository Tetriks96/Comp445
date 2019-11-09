import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient implements AutoCloseable
{
	Socket mSocket = null;
	PrintWriter mOut = null;
	BufferedReader mIn = null;
	
	public TcpClient(InetAddress address, int port) throws IOException
	{
		boolean autoflush = true;
		mSocket = new Socket(address, port);
		mOut = new PrintWriter(mSocket.getOutputStream(), autoflush);
		mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
	}
	
	public void Send(String message) throws IOException
	{
		for (char c: message.toCharArray())
		{
			mOut.print(c);
		}
		mOut.flush();
		mSocket.shutdownOutput();
	}
	
	public BufferedReader Receive() throws IOException
	{
		String input = "";
		int c;
		while ((c = mIn.read()) != -1)
		{
			input += (char)c;
		}
		mSocket.shutdownInput();
		
		return new BufferedReader(new StringReader(input));
	}

	@Override
	public void close() throws Exception
	{
		if (mSocket != null)
		{
			mSocket.close();
		}
		
		if (mOut != null)
		{
			mOut.close();
		}
		
		if (mIn != null)
		{
			mIn.close();
		}
	}
}
