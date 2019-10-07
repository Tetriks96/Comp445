import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Post extends Request
{
	protected String inlineData;
	protected String filePath;
	
	public Post(String[] args)
	{
		super(args);
		this.method = "POST";
	}
	
	protected int HandleArg(String[] args, int i)
	{
		String arg = args[i];
		switch(arg)
		{
		case "-d":
			if (this.filePath != null)
			{
				System.out.println("Unable to use inline data because file was already provided");
				return i + 2;
			}
			
			if (i + 1 >= args.length)
			{
				System.out.println("Missing in-line data after -d flag");
				return i + 1;
			}

			this.inlineData = args[i + 1];
			return i + 2;
		case "-f":
			if (this.inlineData != null)
			{
				System.out.println("Unable to use file because inline data was already provided");
				return i + 2;
			}
			
			if (i + 1 >= args.length)
			{
				System.out.println("Missing file path after -f flag");
				return i + 1;
			}
			
			this.filePath = args[i + 1];
			return i + 2;
		default:
			return super.HandleArg(args, i);
		}
	}
	
	protected void CompleteHeaders(PrintWriter out)
	{
		String body = "";
		
		if (this.inlineData != null)
		{
			body = this.inlineData;
		}
		else if (this.filePath != null)
		{
			try(FileReader fileReader = new FileReader(this.filePath))
			{
			    int i = fileReader.read();
			    while(i != -1) {
			        body += (char)i;
			        i = fileReader.read();
			    }
			}
			catch (FileNotFoundException e)
			{
			    System.out.println("File not found");
			}
			catch (IOException e)
			{
			    System.out.println("Input-output exception occured while reading from input file");
			}
		}

		out.println("Content-Length: " + body.length());
	    
	    if (body.length() > 0)
	    {
		    out.println("Content-Type: text/plain");
		    out.println();
			out.println(body);
		}
	    else
	    {
		    out.println();
	    }
	}
}
