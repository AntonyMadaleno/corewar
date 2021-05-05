import java.io.*;
import java.util.*;

public class Memory 
{
	//Using Instruction Class

	private int size;
	private Instruction[] memory_array; 

	public Memory(int size)
	{
		this.size = size;
		this.memory_array = new Instruction[size];
	}

	public Memory()
	{
		this(8000);
	}

	public boolean isFree(int from, int to)
	{
		from = from % this.size;
		to = to % this.size;
		int ini = Math.min(from, to);

		boolean ret = true;

		int[] def = {0,0,0,0,0};

		for (int i = 0; i < Math.abs(from - to); i++)
		{

			int[] line = this.memory_array[(i+ini)%this.size].getLine();

			if (!Arrays.equals(line, def))
			{
				ret = false;
			}
		}

		return ret;	

	}

	public int getSize()
	{
		return this.size;
	}

	public Instruction getInstruction(int addr) 
	{

		if(addr < 0)
		{
			addr = (this.size)-(Math.abs(addr)%(this.size));
		}

		return this.memory_array[addr%this.size]; 
	}

	public void setInstruction(int addr, Instruction ins)
	{

		if(addr < 0)
		{
			addr = (this.size)-(Math.abs(addr)%(this.size));
		}

		this.memory_array[addr%this.size] = ins;
	}

	public void copy(int addr1, int addr2)
	{
		if(addr1 < 0)
		{
			addr1 = (this.size)-(Math.abs(addr1)%(this.size));
		}
		if(addr2 < 0)
		{
			addr2 = (this.size)-(Math.abs(addr2)%(this.size));
		}
		if(addr1 >= this.size)
		{
			addr1 = addr1 % (this.size);
		}
		if(addr2 >= this.size)
		{
			addr2 = addr2 % (this.size);
		}

		this.setInstruction(addr2, memory_array[addr1].clone(addr2));
	}

	public void save(String dir, String filename)
	{

		new File(dir).mkdirs();
		File save = new File(dir + "/" + filename + ".memory");

		try 
		{
			FileWriter fw = new FileWriter(save);

			for (int i = 0; i < memory_array.length; i++)
			{
				fw.write(memory_array[i].toString() + "\n");
			}

			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}