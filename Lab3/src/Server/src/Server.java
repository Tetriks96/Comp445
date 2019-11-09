import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	public static void main(String[] args)
	{
		try
		(
			TcpServer tcpServer = new TcpServer(8080);
		)
		{
			while (true)
			{
				try
				{
					BufferedReader in = tcpServer.Receive();
					
					String input = "";
					int c;
					while ((c = in.read()) != -1)
					{
						input += (char)c;
					}
					in.close();
					
					System.out.println(input);
					
					String output = "Response";

					tcpServer.Send(output);
				}
				catch (Exception e)
				{
					System.out.println("An exception occured:");
					System.out.println(e);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Server crashed!");
			System.out.println(e);
		}
	}
}
