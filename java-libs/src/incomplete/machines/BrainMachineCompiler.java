package incomplete.machines;

import libs.bytes.BufferUnpackedViews;
import libs.bytes.PackedView;
import libs.types.ByteTest;

public class BrainMachineCompiler
{
	private static String[] InstNames = new String[] {
		"mov",
		"xchg",
		"bswp",
	};
	
	private enum InstCodes
	{
		MOV(0), 
		XCHG(1),
		BSWP(2),
		;
		
		private final int index;
		
		private InstCodes(int index)
		{
			this.index = index;
		}
	}
	
	private static BufferUnpackedViews InstNamesBuffer = BufferUnpackedViews.from(InstNames);
	
	public static void compileLines(BufferUnpackedViews lines)
	{
		PackedView view = new PackedView();
		PackedView inst = new PackedView();
		
		int lnum = 0;
		
		while (lnum < lines.views.length)
		{
			lines.copyViewTo(lnum, view);
			
			if (view.isEmpty())
				continue;
			
			while (true)
			{
				if (view.lstrip(ByteTest.isBlank) && view.isEmpty())
					break;
				
				
			}
			
			lnum += 1;
		}
	}
}