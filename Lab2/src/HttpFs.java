
public class HttpFs
{
	public static void main(String args[])
	{
		ArgsParser argsParser = new ArgsParser(args);
		System.out.println();
		
		System.out.println("Verbose output: " + argsParser.verbose);
		System.out.println("Port: " + argsParser.port);
		System.out.println("Directory: " + argsParser.directory);
		System.out.println();
		
		Server server = new Server(argsParser.port, argsParser.verbose, argsParser.directory);
		server.Listen();
	}
}
