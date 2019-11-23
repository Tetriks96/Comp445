import java.io.PrintWriter;

public class Get extends Request
{
	public Get(String[] args)
	{
		super(args);
		this.method = "GET";
	}
	
	protected String CompleteHeaders(String out)
	{
		out += "\n";
		return out;
	}
}
