import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

public abstract class Request {
	protected String method;
	protected boolean verbose = false;
	protected HashMap<String, String> headers = new HashMap<String, String>();
	protected URL url = null;
	protected String outputPath = null;
	
	public Request(String[] args)
	{
		int i = 0;
		while (i < args.length)
		{
			i = HandleArg(args, i);
		}
	}
	
	protected int HandleArg(String[] args, int i)
	{
		String arg = args[i];
		switch(arg)
		{
		case "-v":
			this.verbose = true;
			return i + 1;
		case "-h":
			if (i + 1 >= args.length)
			{
				System.out.println("Missing header-value pair after -h flag");
				return i + 1;
			}
			
			String[] tokens = args[i + 1].split(":");
			if (tokens.length != 2)
			{
				System.out.println("Unable to determine header-value pair");
				return i + 2;
			}
			
			headers.put(tokens[0], tokens[1]);
			return i + 2;
		case "-o":
			if (this.outputPath != null)
			{
				System.out.println("Output file path already set");
			}
			
			if (i + 1 >= args.length)
			{
				System.out.println("Missing output file path after -o flag");
				return i + 1;
			}
			
			this.outputPath = args[i + 1];
			return i + 2;
		default:
			if (this.url == null)
			{
				try
				{
					this.url = new URL(arg);
				}
				catch(Exception e)
				{
					try
					{
						this.url = new URL("http://" + arg);
					}
					catch(Exception e2)
					{
						System.out.println("Unable to parse URL");
					}
				}
			}
			else
			{
				System.out.println("Unrecognized argument: " + arg);
			}
			return i + 1;
		}
	}
	
	public void Execute()
	{
		try
		{
			InetAddress address = InetAddress.getByName(url.getHost());
			int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();
			
			TcpClient tcpClient = new TcpClient(8081, address, (short)port);
			
			String out = "";
			
		    out += this.method + " " + url.toString() + " HTTP/1.0" + "\n";
		    out += "Host: " + url.getHost() + "\n";
		    for (String key : headers.keySet())
		    {
		    	out += key + ": " + headers.get(key) + "\n";
		    }
		    out += "Connection: Close" + "\n";
		    
		    out = CompleteHeaders(out);
		    
		    tcpClient.Send(out);
		    
		    StringBuilder sb = new StringBuilder(8096);
		    
		    BufferedReader in = tcpClient.Receive();
		    
		    boolean bodyReached = false;

        	String line = in.readLine();
        	
        	String[] tokens = line.split(" ");
    		String code = tokens[1];
    		boolean redirect = code.length() == 3 && code.charAt(0) == '3';
        	
	        while(line != null)
	        {
	        	if (verbose || bodyReached)
	        	{
		        	sb.append(line + "\n");
	        	}
	        	
	        	if (line.length() == 0)
	        	{
	        		bodyReached = true;
	        	}
	        	else if (redirect && !bodyReached && line.startsWith("Location: "))
				{
					tokens = line.split(" ");
					this.url = new URL(tokens[1]);
				}

	        	line = in.readLine();
	        }

		    if (this.outputPath != null)
		    {
		    	try (FileWriter fw = new FileWriter(this.outputPath))
    			{
		    		fw.write(sb.toString());
		    		System.out.println("Outputed successfully to " + this.outputPath);
    			}
		    	catch (IOException e)
		    	{
		    		System.out.println("Input-output exception occured while writing to output file");
		    	}
		    }
		    
		    if (redirect)
		    {
		    	System.out.println("Redirecting...");
		    	System.out.println();
		    	Execute();
		    }
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	abstract protected String CompleteHeaders(String out);
}
