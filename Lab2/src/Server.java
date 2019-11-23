import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private int port = 8080;
	private boolean verbose = false;
    FileSystem fileSystem = null;
	
	public Server(int port, boolean verbose, String dataDirectory)
	{
		this.port = port;
		this.verbose = verbose;
		this.fileSystem = new FileSystem(dataDirectory);
	}
	
	public void Listen()
	{
		try
		{
			TcpServer tcpServer = new TcpServer(port);
			
			System.out.println("Http File Server started!");
			System.out.println();
			
			while (true)
			{
				try
				{
					BufferedReader in = tcpServer.Receive();
					String out = "";
					
					String input;
					input = in.readLine();
					
					String[] firstLine = input.split(" ");
					
					if (firstLine.length < 2)
					{
						out += "HTTP/1.0 400 Bad Request" + "\n";
						tcpServer.Send(out);
						return;
					}
					
					boolean isPostRequest = firstLine[0].toLowerCase().startsWith("post");
					
					String param = "";
					String modifiedUrl = firstLine[1].replaceAll("//", "");
					int index = modifiedUrl.indexOf("/");
					if (index != -1)
					{
						param = modifiedUrl.substring(index + 1);
					}
					
					int contentLength = 0;
					
				    while ((input = in.readLine()) != null && input.length() > 0)
				    {
				    	if (isPostRequest && input.startsWith("Content-Length: "))
				    	{
				    		contentLength = Integer.parseInt(input.split(" ")[1]);
				    	}
				    }

			    	String content = "";
				    
				    if (isPostRequest)
				    {
				    	char c;
				    	while (content.length() < contentLength && (c = (char)in.read()) != -1)
				    	{
					    	content += c;
				    	}
				    }
				    
				    if (param.contains(".."))
				    {
						out += "HTTP/1.0 403 Forbidden" + "\n";
				    }
				    else if (isPostRequest)
				    {
				        out += fileSystem.Write(param, content) + "\n";
				    }
				    else
				    {
				        out += fileSystem.Read(param) + "\n";
				    }
				    
				    tcpServer.Send(out);
				    
				    System.out.println();
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
			PrintIfVerbose("Server crashed!");
			PrintIfVerbose(e.toString());
		}
	}
	
	void PrintIfVerbose(String string)
	{
		if (this.verbose)
		{
			System.out.println(string);
		}
	}
}
