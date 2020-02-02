package libs.buffers;

import libs.exceptions.BufferIsFullException;
import libs.exceptions.NotEnoughDataException;
import libs.safe.SafeTools;

public class BufferQueue
{	
	public static final boolean SAFE = true;
	
	private byte[] buffer;
	private int start;
	private int end;
	
	// constructors
	
	public BufferQueue(byte[] buffer, int start, int end)
	{
		if (SAFE)
			SafeTools.checkBufferStartEnd(buffer, start, end);
		
		this.buffer = buffer;
		this.start = start;
		this.end = end;
	}
	
	public BufferQueue(int size)
	{
		if (SAFE)
			SafeTools.checkNegativeZeroIndex(size, "size");
		
		this.buffer = new byte[size];
		this.start = 0;
		this.end = 0;
	}
	
	private BufferQueue() { }
	
	public static BufferQueue unsafe(byte[] buffer, int start, int end)
	{
		BufferQueue result = new BufferQueue();
		
		result.buffer = buffer;
		result.start = start;
		result.end = end;
		
		return result;
	}
	
	public static BufferQueue unsafe(int size)
	{
		BufferQueue result = new BufferQueue();
		
		result.buffer = new byte[size];
		result.start = 0;
		result.end = 0;
		
		return result;
	}
	
	// fields functions
	
	public byte[] getBytes()
	{
		return buffer;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getEnd()
	{
		return end;
	}
	
	// get and set byte
	
	public byte get(int index)
	{
		if (SAFE)
			SafeTools.checkIndexOutOfBounds(index, 0, end - start, "index");
		
		return buffer[start + index];
	}
	
	public void set(int index, byte value)
	{
		if (SAFE)
			SafeTools.checkIndexOutOfBounds(index, 0, end - start, "index");
		
		buffer[start + index] = value;
	}
	
	// unsafe get and set byte
	
	public byte getUnsafe(int index)
	{
		return buffer[start + index];
	}
	
	public void setUnsafe(int index, byte value)
	{
		buffer[start + index] = value;
	}
	
	// length and size functions
	
	public int length()
	{
		return end - start;
	}
	
	public int size()
	{
		return buffer.length;
	}
	
	// remaining functions
	
	public int remaining()
	{
		return buffer.length - end + start; 
	}
	
	public int leftRemaining()
	{
		return start;
	}
	
	public int rightRemaining()
	{
		return buffer.length - end;
	}
	
	// test functions
	
	public boolean isEmpty()
	{
		return start == end;
	}
	
	public boolean isFull()
	{
		return (start == 0) && (end == buffer.length);
	}
	
	// shift functions
	
	public static final int MAX_SHIFT = Integer.MAX_VALUE;
	
	public void shiftRight(int shift)
	{
		if (SAFE)
			SafeTools.checkInvalidIndexMinimum(shift, 0, "shift");
		
		shiftRightUnsafe(shift);
	}
	
	public void shiftRightUnsafe(int shift)
	{
		if (shift == 0)
			return;
		
		if (shift > buffer.length - end)
			shift = buffer.length - end;
		
		System.arraycopy(buffer, start, buffer, start + shift, end - start);
		start += shift;
		end += shift;
	}
	
	public void shiftLeft(int shift)
	{
		if (SAFE)
			SafeTools.checkInvalidIndexMinimum(shift, 0, "shift");
		
		shiftLeftUnsafe(shift);
	}
	
	public void shiftLeftUnsafe(int shift)
	{
		if (shift == 0)
			return;
		
		if (shift > start)
			shift = start;
		
		System.arraycopy(buffer, start, buffer, start - shift, end - start);
		start -= shift;
		end -= shift;
	}
	
	public void shift(int shift)
	{
		if (shift == 0)
			return;
		
		if (shift == Integer.MIN_VALUE)
			shiftLeftUnsafe(MAX_SHIFT);
		
		if (shift < 0)
			shiftLeftUnsafe(-shift);
		else
			shiftRightUnsafe(shift);
	}
	
	// append methods
	
	public void appendLeft(byte[] buffer, int start, int end)
	{
		if (SAFE)
			SafeTools.checkBufferStartEnd(buffer, start, end);
		
		appendLeftUnsafe(buffer, start, end);
	}
	
	public void appendLeftUnsafe(byte[] buffer, int start, int end)
	{
		int bufferLength = end - start;
		
		if (bufferLength > this.start)
			throw new BufferIsFullException();
		
		start -= bufferLength;
		System.arraycopy(buffer, start, this.buffer, start, bufferLength);
	}
	
	public void appendRight(byte[] buffer, int start, int end)
	{
		if (SAFE)
			SafeTools.checkBufferStartEnd(buffer, start, end);
		
		appendRightUnsafe(buffer, start, end);
	}
	
	public void appendRightUnsafe(byte[] buffer, int start, int end)
	{
		int bufferLength = end - start;
		
		if (bufferLength > this.buffer.length - this.end)
			throw new BufferIsFullException();
		
		System.arraycopy(buffer, start, this.buffer, end, bufferLength);
		end += bufferLength;
	}
	
	public void append(byte[] buffer, int start, int end, final int minShift, final boolean toLeft)
	{
		if (SAFE)
		{
			SafeTools.checkBufferStartEnd(buffer, start, end);
			SafeTools.checkInvalidIndexMinimum(minShift, 0, "shift");
		}
		
		appendUnsafe(buffer, start, end, minShift, toLeft);
	}
	
	public void appendUnsafe(byte[] buffer, int start, int end, final int shift, final boolean toLeft)
	{		
		int bufferLength = end - start;
		
		if (toLeft)
		{
			if (bufferLength <= this.start)
			{
				start -= bufferLength;
				System.arraycopy(buffer, start, this.buffer, this.start, bufferLength);
				return;
			}
		}
		else
		{
			if (bufferLength <= this.buffer.length - this.end)
			{
				System.arraycopy(buffer, start, this.buffer, this.end, bufferLength);
				this.end += bufferLength;
				return;
			}
		}
		
		if (bufferLength > this.buffer.length - this.end + this.start)
			throw new BufferIsFullException();
		
		if (toLeft)
		{
			shiftRight(Math.max(bufferLength - this.start, shift));
			
			start -= bufferLength;
			System.arraycopy(buffer, start, this.buffer, this.start, bufferLength);
			return;
		}
		else
		{
			shiftLeft(Math.max(this.buffer.length - this.end - bufferLength, shift));
			
			System.arraycopy(buffer, start, this.buffer, this.end, bufferLength);
			this.end += bufferLength;
			return;
		}
	}
	
	// pop methods
	
	public void popLeft(byte[] buffer, int start, int end)
	{
		if (SAFE)
			SafeTools.checkBufferStartEnd(buffer, start, end);
		
		popLeftUnsafe(buffer, start, end);
	}
	
	public void popLeftUnsafe(byte[] buffer, int start, int end)
	{
		int bufferLength = end - start;
		
		if (bufferLength > this.end - this.start)
			throw new NotEnoughDataException();
		
		System.arraycopy(this.buffer, this.start, buffer, start, bufferLength);
		this.start += bufferLength;
	}
	
	public void popRight(byte[] buffer, int start, int end)
	{
		if (SAFE)
			SafeTools.checkBufferStartEnd(buffer, start, end);
		
		popRightUnsafe(buffer, start, end);
	}
	
	public void popRightUnsafe(byte[] buffer, int start, int end)
	{		
		int bufferLength = end - start;
		
		if (bufferLength > this.end - this.start)
			throw new NotEnoughDataException();
		
		this.end -= bufferLength;
		System.arraycopy(this.buffer, this.end, buffer, start, bufferLength);
	}
}
