package libs.math.points;

/* 
*  generated by a Python script
*  don't modify it yourself
*/

class LongPoint
{
	private long x;
	private long y;

	// constructors

	public LongPoint(long x, long y)
	{
		this.x = x;
		this.y = y;
	}

	public LongPoint()
	{
		this.x = 0;
		this.y = 0;
	}

	// copy function

	public LongPoint copy()
	{
		return new LongPoint(this.x, this.y);
	}

	// test functions

	public boolean isNatural()
	{
		return (this.x > 0) && (this.y > 0);
	}

	// limit functions

	public void xLimit(long min, long max)
	{
		if (x < min) x = min;
		else if (x > max) x = max;
	}
	public void yLimit(long min, long max)
	{
		if (y < min) y = min;
		else if (y > max) y = max;
	}

	public void limit(int xMin, int xMax, int yMin, int yMax)
	{
		xLimit(xMin, xMax);
		yLimit(yMin, yMax);
	}

	public void limit(LongPoint minPoint, LongPoint maxPoint)
	{
		xLimit(minPoint.x, maxPoint.x);
		yLimit(minPoint.y, maxPoint.y);
	}

	// add functions

	public void add(long value)
	{
		this.x += value;
		this.y += value;
	}

	public void add(long x, long y)
	{
		this.x += x;
		this.y += y;
	}

	public void add(LongPoint other)
	{
		this.x += other.x;
		this.y += other.y;
	}

	// sub functions

	public void sub(long value)
	{
		this.x -= value;
		this.y -= value;
	}

	public void sub(long x, long y)
	{
		this.x -= x;
		this.y -= y;
	}

	public void sub(LongPoint other)
	{
		this.x -= other.x;
		this.y -= other.y;
	}

	// div functions

	public void div(long value)
	{
		this.x /= value;
		this.y /= value;
	}

	public void div(long x, long y)
	{
		this.x /= x;
		this.y /= y;
	}

	public void div(LongPoint other)
	{
		this.x /= other.x;
		this.y /= other.y;
	}

	// mul functions

	public void mul(long value)
	{
		this.x *= value;
		this.y *= value;
	}

	public void mul(long x, long y)
	{
		this.x *= x;
		this.y *= y;
	}

	public void mul(LongPoint other)
	{
		this.x *= other.x;
		this.y *= other.y;
	}
}
