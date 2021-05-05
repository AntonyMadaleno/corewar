public class Instruction
{
	//Using RedCode

	private int[] red;
	private int address;
	private int mem_size;

	public Instruction(int[] line, int addr, int mem_size)
	{

		this.red = line;
		if (addr >= 0)
		{
			this.address = addr%mem_size;
		}
		else
		{
			this.address = (this.mem_size)-(Math.abs(this.address)%(this.mem_size));;
		}
		this.mem_size = mem_size;

	}

	public Instruction clone(int naddr)
	{
		return new Instruction(this.red, naddr, this.mem_size);
	}

	public String toString()
	{
		return String.valueOf(this.address) + " :\t" + RedCode.reverse(this.red);
	}

	public int[] getLine()
	{
		return this.red;
	}

	public int getAdress()
	{
		return this.address;
	}

	public int getMemorySize()
	{
		return this.mem_size;
	}

}