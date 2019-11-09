import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

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
		(
			TcpServer tcpServer = new TcpServer(this.port);
		)
		{
			System.out.println("Http File Server started!");
			System.out.println();
			
			while (true)
			{
				try
				{
					BufferedReader in = tcpServer.GetInput();
					
					StringBuilder out = new StringBuilder(8096);
					
					String input;
					input = in.readLine();
					PrintIfVerbose(input);
					
					String[] firstLine = input.split(" ");
					
					if (firstLine.length < 2)
					{
						out.append("HTTP/1.0 400 Bad Request" + "\n");
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
				    	PrintIfVerbose(input);
				    }
				    
				    PrintIfVerbose();

			    	String content = "";
				    
				    if (isPostRequest)
				    {
				    	char c;
				    	while (content.length() < contentLength && (c = (char)in.read()) != -1)
				    	{
					    	content += c;
				    	}
				    	PrintIfVerbose(content);
				    	PrintIfVerbose();
				    }
				    
				    if (param.contains(".."))
				    {
						out.append("HTTP/1.0 403 Forbidden" + "\n");
				    }
				    else if (isPostRequest)
				    {
				        out.append(fileSystem.Write(param, content) + "\n");
				    }
				    else
				    {
				        out.append(fileSystem.Read(param) + "\n");
				    }
				    
				    tcpServer.SendResponse(out.toString());
				}
				catch (Exception e)
				{
					System.out.println("An exception occured while handling a request:");
					System.out.println(e);
					System.out.println();
				}
			}
		}
		catch (Exception e)
		{
			PrintIfVerbose("Server crashed!");
			PrintIfVerbose(e.toString());
		}
	}
	
	void PrintIfVerbose()
	{
		PrintIfVerbose("");
	}
	
	void PrintIfVerbose(String string)
	{
		if (this.verbose)
		{
			System.out.println(string);
		}
	}
}
