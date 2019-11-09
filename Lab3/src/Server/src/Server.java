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
			ServerSocket serverSocket = new ServerSocket(8080);
		)
		{
			while (true)
			{
				try
				(
					Socket clientSocket = serverSocket.accept();
				    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				)
				{
					String input = "";
					int c;
					
					while ((c = in.read()) != -1)
					{
						input += (char)c;
					}
					clientSocket.shutdownInput();
					
					System.out.println(input);
					
					String output = "Response";
					
					for (char c2: output.toCharArray())
					{
						out.print(c2);
					}
					out.flush();
					clientSocket.shutdownOutput();
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
