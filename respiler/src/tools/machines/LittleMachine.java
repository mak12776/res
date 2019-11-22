package tools.machines;

import tools.BytesTools;
import tools.SafeTools;
import tools.exceptions.InvalidByteCodeException;

public class LittleMachine
{	
	// WARNING: don't change the following constants:
	public static final int REGISTER_BITS = 64;
	public static final int ADDRESS_BITS = 32;
	
	public static final int REGISTER_BYTES = REGISTER_BITS / 8;
	public static final int ADDRESS_BYTES = ADDRESS_BITS / 8;
	
	public static final int NUMBER_OF_REGISTERS = 32;
	
	public LittleMachine(int bufferSize)
	{
		this.buffer = new byte[bufferSize];
		
		this.registers = new byte[NUMBER_OF_REGISTERS * REGISTER_BYTES];
		this.ip = 0;
	}
	
	public LittleMachine(byte[] buffer, int start)
	{
		this.buffer = buffer;
		
		this.registers = new byte[NUMBER_OF_REGISTERS * REGISTER_BYTES];
		this.ip = start;
	}
	
	private byte[] buffer;
	
	private byte[] registers;
	private int ip;
	
	public byte[] getBuffer()
	{
		return buffer;
	}
	
	public int getIP()
	{
		return ip;
	}
	
	public byte[] getRegisters()
	{
		return registers;
	}
	
	private int address;
	private int regIndex;
	
	private long next(final int length)
	{
		if (SafeTools.CHECK_INTEGER_BYTES)
			SafeTools.checkIntegerBytes(length);
		
		long value = 0;
		
		for (int i = 0; i < length; i += 1)
		{
			value <<= 8;
			value = value | ((long)buffer[ip++] & 0xFF);
		}
		
		return value;
	}
	
	// assembly instructions
	
	public static final int REG_OPR_BYTES = 1;
	public static final byte REG_NUM_MASK = (byte) 0x1F;
	public static final byte REG_INDEX_MASK = (byte) 0xE0;
	public static final int REG_INDEX_SHIFT = 5;
	
	private void nextRegIndex(final int length) throws InvalidByteCodeException
	{		
		byte REG = (byte) next(REG_OPR_BYTES);
		
		int num = REG & REG_NUM_MASK;
		int index = (REG & REG_INDEX_MASK) >>> REG_INDEX_MASK;
		
		regIndex = (num * REGISTER_BYTES) + (index * length);
		
		if (regIndex + length > registers.length)
			throw new InvalidByteCodeException("invalid register operand index: " + index);
	}

	private void nextImAddr()
	{
		address = (int) next(ADDRESS_BYTES);
	}
	
	private void nextRegAddr()
	{
		regIndex = ((byte) next(REG_OPR_BYTES) & REG_NUM_MASK) * REGISTER_BYTES;
		BytesTools.read(registers, regIndex, REGISTER_BYTES);
	}
	
	public static final short INST_NOOP = 			0x0;		
	
	public static final short INST_COPY_R8_IM8 = 	0x1;
	public static final short INST_COPY_R16_IM16 = 	0x2;
	public static final short INST_COPY_R32_IM32 = 	0x3;
	public static final short INST_COPY_R64_IM64 = 	0x4;
	
	private void copyRegIm(int length) throws InvalidByteCodeException
	{
		if (SafeTools.CHECK_INTEGER_BYTES)
			SafeTools.checkIntegerBytes(length);
		
		nextRegIndex(length);
		
		BytesTools.copy(registers, regIndex, buffer, ip, length);
		ip += length;
	}
	
	public static final short INST_COPY_RA_IM8 = 	0x5;
	public static final short INST_COPY_RA_IM16 =	0x6;
	public static final short INST_COPY_RA_IM32 = 	0x7;
	public static final short INST_COPY_RA_IM64 = 	0x8;
	
	private void copyRegAddrIm(int size)
	{
		if (SafeTools.CHECK_INTEGER_BYTES)
			SafeTools.checkIntegerBytes(size);
		
		nextRegAddr();
		
		BytesTools.copy(buffer, address, buffer, ip, size);
		ip += size;
	}
	
	public static final short INST_COPY_IA_IM8 = 	0x9;
	public static final short INST_COPY_IA_IM16 = 	0xA;
	public static final short INST_COPY_IA_IM32 = 	0xB;
	public static final short INST_COPY_IA_IM64 = 	0xC;
	
	private void copyImAddrIm(int size)
	{
		if (SafeTools.CHECK_INTEGER_BYTES)
			SafeTools.checkIntegerBytes(size);
		
		nextImAddr();
		
		BytesTools.copy(buffer, address, buffer, ip, size);
		ip += size;
	}
	
	public static final short INST_EXIT =			0xFF;
	
	public void run() throws InvalidByteCodeException
	{
		short inst;
		
		main_loop:
		while (ip < buffer.length)
		{
			switch (inst = (short) next(2))
			{
			case INST_NOOP:
				break;
			
				
			case INST_COPY_R8_IM8:
				copyRegIm(1);
				break;
				
			case INST_COPY_R16_IM16:
				copyRegIm(2);
				break;
				
			case INST_COPY_R32_IM32:
				copyRegIm(4);
				break;
				
			case INST_COPY_R64_IM64:
				copyRegIm(8);
				break;
				
				
			case INST_COPY_RA_IM8:
				copyRegAddrIm(1);
				break;
				
			case INST_COPY_RA_IM16:
				copyRegAddrIm(2);
				break;
				
			case INST_COPY_RA_IM32:
				copyRegAddrIm(4);
				break;
				
			case INST_COPY_RA_IM64:
				copyRegAddrIm(8);
				break;
				
				
			case INST_COPY_IA_IM8:
				copyImAddrIm(1);
				break;
				
			case INST_COPY_IA_IM16:
				copyImAddrIm(2);
				break;
				
			case INST_COPY_IA_IM32:
				copyImAddrIm(4);
				break;
				
			case INST_COPY_IA_IM64:
				copyImAddrIm(8);
				break;
				
			case INST_EXIT:
				break main_loop;
				
			default:
				throw new InvalidByteCodeException("invalid byte code " + Integer.toHexString(inst & 0xFFFF) + " at " + (ip - Short.BYTES));
			}
		}
	}
}
