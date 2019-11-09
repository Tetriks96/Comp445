import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystem
{
	private String dataDirectory;
	
	public FileSystem(String dataDirectory)
	{
		this.dataDirectory = dataDirectory;
	}
	
	public String Read(String file)
	{
		if (file.length() == 0)
		{
			try (Stream<Path> walk = Files.walk(Paths.get(dataDirectory))) {

				List<String> result = walk.filter(Files::isRegularFile).map(x -> x.getFileName().toString()).collect(Collectors.toList());

				String response =
						"HTTP/1.0 200 OK" + "\r\n" +
						"\r\n" +
						String.join("\r\n", result);
				return response;
			}
			catch (IOException e)
			{
				return "HTTP/1.0 500 Internal Server Error";
			}
		}
		else
		{
			try (BufferedReader in = new BufferedReader(new FileReader(dataDirectory + '\\' + file)))
			{
				String content = "";
				int c;
				while((c = in.read()) != -1)
				{
					content += (char) c;
				}
				
				String response =
					"HTTP/1.0 200 OK" + "\r\n" +
					"\r\n" +
					content;
				
				return response;
			}
			catch (FileNotFoundException e)
			{
				return "HTTP/1.0 404 Not Found";
			}
			catch (IOException e)
			{
				return "HTTP/1.0 500 Internal Server Error";
			}
		}
	}
	
	public String Write(String file, String content)
	{
		try (BufferedWriter out = new BufferedWriter(new FileWriter(dataDirectory + '\\' + file)))
		{
			out.write(content);

			return "HTTP/1.0 200 OK";
		}
		catch (FileNotFoundException e)
		{
			return "HTTP/1.0 404 Not Found";
		}
		catch (IOException e)
		{
			return "HTTP/1.0 500 Internal Server Error";
		}
	}
}
