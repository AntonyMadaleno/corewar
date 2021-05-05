import java.io.*;
import java.util.*;

public class Process
{
	//Process

	private String pid;
	private int pointer;
	private MARS owner;
	private boolean alive;
	private int deathCycle;

	public Process(MARS owner, int pointer, String pid)
	{

		this.owner = owner;
		this.pointer = pointer;
		this.pid = pid;
		this.alive = true;
		this.deathCycle = 0;

	}

	public boolean active()
	{
		return this.alive;
	}

	public String getPID()
	{
		return this.pid;
	}

	public boolean execute()
	{

		if (this.alive == false)
		{
			return false;
		}

		Instruction cmd = this.owner.getMemory().getInstruction(this.pointer);

		int opcode = cmd.getLine()[0];
		boolean ret = false;


		if(opcode == 0)
		{
			ret = this.dat(cmd);
		}
		if(opcode == 1)
		{
			ret = this.mov(cmd);
		}
		if(opcode == 2)
		{
			ret = this.add(cmd);
		}
		if(opcode == 3)
		{
			ret = this.sub(cmd);
		}
		if(opcode == 4)
		{
			ret = this.jmp(cmd);
		}
		if(opcode == 5)
		{
			ret = this.jmz(cmd);
		}
		if(opcode == 6)
		{
			ret = this.jmg(cmd);
		}
		if(opcode == 7)
		{
			ret = this.djz(cmd);
		}
		if(opcode == 8)
		{
			ret = this.cmp(cmd);
		}

		if (ret == false)
		{
			this.alive = false;
		}

		this.deathCycle++;

		return ret;

	}

	/**
		
	DAT = 0	-data
	MOV = 1	-move from A to B
	ADD = 2	-add A to B, store result in B
	SUB = 3	-subtract A from B, store result in B
	JMP = 4	-transfer execution to A
	JMZ = 5	-transfer execution to A if B is zero
	JMG = 6	-transfer control to address A if contents of B are greater than zero.
	DJZ = 7	-decrement B, if B is non-zero, transfer execution to A
	CMP = 8	-Compare A and B, if they are not equal then we skip the next instruction

	**/

	private boolean dat(Instruction cmd)
	{
		//-DAT
		return false;

	}

	private boolean mov(Instruction cmd)
	{
		//-MOV

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];


		//------------
		if(mode_A == 0)
		{
			arg_A = arg_A + this.pointer;
		}

		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2] + this.pointer;
			}
			else
			{
				return false;
			}
			
		}

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2] + this.pointer;
			}
			else
			{
				return false;
			}
			
		}


		if(mode_A == 1)
		{
			int[] arr = {0,0,arg_A,0,0};
			Instruction instr = new Instruction(arr, arg_B,this.owner.getMemory().getSize());
			this.owner.getMemory().setInstruction(arg_B, instr);
			this.pointer++;
			return true;
		}
		if(mode_A == 0 || mode_A == 2)
		{
			this.owner.getMemory().copy(arg_A, arg_B);
			this.pointer++;
			return true;
		}

		return false;

	}

	private boolean add(Instruction cmd)
	{	
		//ADD

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];


		//------------
		if(mode_A == 0)
		{
			arg_A = arg_A + this.pointer;
		}

		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}


		if (this.owner.getMemory().getInstruction(arg_B).getLine()[0] == 0)
		{
			int[] arr = {0,0,arg_A + this.owner.getMemory().getInstruction(arg_B).getLine()[2],0,0};
			Instruction instr = new Instruction(arr, arg_B, this.owner.getMemory().getSize());
			this.owner.getMemory().setInstruction(arg_B, instr);
			this.pointer++;
			return true;
		}

		return false;

	}

	private boolean sub(Instruction cmd)
	{	
		//SUB

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];


		//------------
		if(mode_A == 0)
		{
			arg_A = arg_A + this.pointer;
		}

		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		arg_A = -arg_A;


		if (this.owner.getMemory().getInstruction(arg_B).getLine()[0] == 0)
		{
			int[] arr = {0,0,arg_A + this.owner.getMemory().getInstruction(arg_B).getLine()[2],0,0};
			Instruction instr = new Instruction(arr, arg_B, this.owner.getMemory().getSize());
			this.owner.getMemory().setInstruction(arg_B, instr);
			this.pointer++;
			return true;
		}

		return false;

	}


	private boolean jmp(Instruction cmd)
	{	
		//JMP

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		//------------
		if(mode_A == 0)
		{
			arg_A = arg_A + this.pointer;
		}

		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		this.pointer = arg_A;

		return true;

	}

	private boolean jmz(Instruction cmd)
	{
		//JMZ

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		if(arg_B == 0)
		{
			return this.jmp(cmd);
		}
		
		this.pointer++;
		return true;


	}

	private boolean jmg(Instruction cmd)
	{
		//JMG

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		if(arg_B > 0)
		{
			return this.jmp(cmd);
		}
		
		this.pointer++;
		return true;


	}


	private boolean djz(Instruction cmd)
	{
		//DJZ

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];

		//------------
		if(mode_A == 0)
		{
			arg_A = arg_A + this.pointer;
		}

		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		//------------
		if(mode_B == 0)
		{
			arg_B = arg_B + this.pointer;
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		if (this.owner.getMemory().getInstruction(arg_B).getLine()[0] == 0)
		{
			this.owner.getMemory().getInstruction(arg_B).getLine()[2]--;
		}
		else
		{
			return false;
		}

		if(this.owner.getMemory().getInstruction(arg_B).getLine()[2] == 0)
		{
			this.pointer = arg_A;
			return true;
		}

		this.pointer++;
		return true;

	}

	private boolean cmp(Instruction cmd)
	{

		int arg_A = cmd.getLine()[2];
		int mode_A = cmd.getLine()[1];

		int arg_B = cmd.getLine()[4];
		int mode_B = cmd.getLine()[3];
		
		//------------

		if(mode_A == 0)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}
		if(mode_A == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}

			if (this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[0] == 0)
			{
				arg_A = this.owner.getMemory().getInstruction(this.pointer + arg_A).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		//------------

		if(mode_B == 0)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		if(mode_B == 2)
		{

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}

			if (this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[0] == 0)
			{
				arg_B = this.owner.getMemory().getInstruction(this.pointer + arg_B).getLine()[2];
			}
			else
			{
				return false;
			}
			
		}

		if (arg_A != arg_B)
		{
			this.pointer++;
		}

		this.pointer++;
		return true;

	}


}