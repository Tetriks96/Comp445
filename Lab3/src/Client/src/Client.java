import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
	public static void main(String[] args)
	{
		try
		{
			String output = args.length > 0 ? args[0] : "message";
			
			int port = 8081;
			InetAddress serverAddress = InetAddress.getByName("localhost");
			int serverPort = 8080;

			TcpClient tcpClient = new TcpClient(port, serverAddress, serverPort);
			
			tcpClient.Send(output);
			
			BufferedReader in = tcpClient.Receive();
			
			String input = "";
			int c;
			while((c = in.read()) != -1)
			{
				input += (char)c;
			}
			in.close();
			
			System.out.println(input);
		}
		catch (Exception e)
		{
			System.out.println("An exception occured:");
			System.out.println(e);
		}
	}
}
