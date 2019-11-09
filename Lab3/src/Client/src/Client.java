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
			
			InetAddress host = InetAddress.getByName("localhost");
			int port = 8080;
			
			try
			(
				TcpClient tcpClient = new TcpClient(host, port);
			)
			{
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
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}
