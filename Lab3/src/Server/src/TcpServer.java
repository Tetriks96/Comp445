import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements AutoCloseable
{
	private ServerSocket mServerSocket = null;
	private Socket mClientSocket = null;
	private PrintWriter mOut = null;
	private BufferedReader mIn = null;
	
	public TcpServer(int port) throws IOException
	{
		mServerSocket = new ServerSocket(port);
	}
	
	private void AcceptClient() throws IOException
	{
		mClientSocket = mServerSocket.accept();
	    mOut = new PrintWriter(mClientSocket.getOutputStream(), true);
	    mIn = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
	}
	
	public BufferedReader Receive() throws IOException
	{
		AcceptClient();
		
		String input = "";
		int c;
		while ((c = mIn.read()) != -1)
		{
			input += (char)c;
		}
		mClientSocket.shutdownInput();
		
		return new BufferedReader(new StringReader(input));
	}
	
	public void Send(String message) throws IOException
	{
		for (char c: message.toCharArray())
		{
			mOut.print(c);
		}
		mOut.flush();
		mClientSocket.shutdownOutput();
		
		CloseClientConnection();
	}
	
	private void CloseClientConnection() throws IOException
	{
		if (mClientSocket != null)
		{
			mClientSocket.close();
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

	@Override
	public void close() throws Exception
	{
		CloseClientConnection();
		
		if (mServerSocket != null)
		{
			mServerSocket.close();
		}
	}
}
