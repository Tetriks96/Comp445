import java.util.Arrays;

public class HttpC
{
    public static void main(String[] args)
    {
    	System.out.println();
    	if (args.length == 0)
    	{
    		 return;
    	}
    	String arg0 = args[0].toLowerCase();
    	args = Arrays.copyOfRange(args, 1, args.length);
    	
    	switch (arg0)
    	{
    	case "help":
    		Help.Execute(args);
    		break;
    	case "get":
    		Get get = new Get(args);
    		get.Execute();
    		break;
    	case "post":
    		Post post = new Post(args);
    		post.Execute();
    		break;
    	}
    }
}