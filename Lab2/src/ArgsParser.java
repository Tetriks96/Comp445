
public class ArgsParser
{
	boolean verbose = false;
	int port = 8080;
	String directory = "";
	
	public ArgsParser(String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];
			switch(arg)
			{
				case "-v":
					verbose = true;
					break;
				case "-p":
					if (i + 1 < args.length)
					{
						String value = args[i + 1];
						try
						{
							this.port = Integer.parseInt(value);
						}
						catch (NumberFormatException e)
						{
							System.out.println("Invalid port number: " + value);
						}
					}
					else
					{
						System.out.println("Must specify port number when using -p flag");
					}
					i++;
					break;
				case "-d":
					if (i + 1 < args.length)
					{
						String value = args[i + 1];
						this.directory = value;
					}
					else
					{
						System.out.println("Must specify directory path when using -d flag");
					}
					i++;
					break;
				default:
					System.out.println("Unrecognized argument encountered: " + arg);
			}
		}
	}
}
