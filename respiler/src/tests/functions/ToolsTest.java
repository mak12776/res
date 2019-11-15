package tests.functions;

import java.io.IOException;

import tools.BytesTools;
import tools.StreamTools;
import tools.bytes.BufferUnpackedViews;
import tools.exceptions.BaseException;

public class ToolsTest extends BaseTest
{
	public static void TestBufferLines(String name)
	{
		try
		{
			BufferUnpackedViews bufferLines = StreamTools.readLines(name);
			for (int lnum = 0; lnum < bufferLines.views.length; lnum += 1)
			{
				output.println(lnum + ": [" + bufferLines.getLineString(lnum) + "]");
			}
		}
		catch (IOException | BaseException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static int compare(byte[] buffer, byte[] bytes)
	{
		return BytesTools.compare(buffer, 0, bytes, 0, buffer.length);
	}

	public void TestCompare()
	{
		byte[] a;
		byte[] b;
		
		a = new byte[] {'a',  'b'};
		b = new byte[] {'a',  'b'};
		output.println(compare(a, b));
		
		a = new byte[] {-128};
		b = new byte[] {127};
		output.println(compare(a, b));
		
		a = new byte[] {127};
		b = new byte[] {-128};
		output.println(compare(a, b));
	}
}