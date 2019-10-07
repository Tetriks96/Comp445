import java.io.PrintWriter;

public class Get extends Request
{
	public Get(String[] args)
	{
		super(args);
		this.method = "GET";
	}
	
	protected void CompleteHeaders(PrintWriter out)
	{
		out.println();
	}
}
