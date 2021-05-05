import java.io.*;
import java.util.*;

public class MARS
{
	//MARS

	private Memory memory;
	private Process[] queue; //process queue
	private String[] pids;
	private Session owner; //owning session
	private int p_count;
	private int cycle;
	private int cycle_threshold;

	public MARS(int mem_size, int max_cycle, int max_process)
	{
		this.memory = new Memory(mem_size);
		this.cycle = 0;
		this.cycle_threshold = max_cycle;
		this.queue = new Process[max_process];
		this.pids = new String[max_process];
		this.p_count = 0;
	}

	public MARS()
	{
		this(8000, 300000, 8);
	}

	public void setOwner(Session owner)
	{
		this.owner = owner;
	}

	public Session getOwner()
	{
		return this.owner;
	}

	public void init()
	{

		int[] def_array = {0,0,0,0,0};

		for (int i = 0; i < this.memory.getSize(); i++)
		{
			Instruction def_instruction = new Instruction(def_array, i,this.memory.getSize());

			this.memory.setInstruction(i, def_instruction);
		}		

	}

	public int[] loadLocal(String[] redfiles)
	{
		//redfiles must be loaded in session/redcode

		int[] ret = new int[redfiles.length];

		for (int i = 0; i < redfiles.length; i++)
		{

			if(redfiles[i] != null)
			{

				int[][] instrcutions = RedCode.compile("../session/redcode", redfiles[i]);

				boolean free = false;
				int a0 = 0;

				while (!free)
				{

					a0 = (int)(Math.random() * this.memory.getSize());
					free = this.memory.isFree(a0, a0 + instrcutions.length);

				}

				for (int j = 0; j < instrcutions.length; j++)
				{
					this.memory.setInstruction(j+a0, new Instruction(instrcutions[j], a0+j, this.memory.getSize() ) );
				}

				ret[i] = a0;
				this.pids[i] = redfiles[i];

			}

		}

		this.initProcesses(ret);

		return ret;

	}

	public int[] load(String dir, String[] redfiles)
	{

		int[] ret = new int[redfiles.length];

		for (int i = 0; i < redfiles.length; i++)
		{

			if(redfiles[i] != null)
			{

				int[][] instrcutions = RedCode.compile(dir, redfiles[i]);

				boolean free = false;
				int a0 = 0;

				while (!free)
				{

					a0 = (int)(Math.random() * this.memory.getSize());
					free = this.memory.isFree(a0, a0 + instrcutions.length);

				}

				for (int j = 0; j < instrcutions.length; j++)
				{
					this.memory.setInstruction(j+a0, new Instruction(instrcutions[j], a0+j, this.memory.getSize() ) );
				}

				ret[i] = a0;
				this.pids[i] = redfiles[i];

			}

		}

		this.initProcesses(ret);

		return ret;

	}

	private void initProcesses(int[] p)
	{

		for (int i = 0; i < p.length; i++)
		{

			while (this.getMemory().getInstruction(p[i]).getLine()[0] == 0)
			{
				p[i]++;
			}

			this.createProcess(this.pids[i], p[i]);
		}

	}

	public void run()
	{

		while (this.cycle < this.cycle_threshold)
		{
			this.step();
			this.cycle++;
		}

		boolean noDead = true;

		for (int i = 0; i < this.queue.length; i++)
		{
			if(!this.queue[i].active())
			{
				noDead = false;
			}
		}

		if(noDead)
		{
			this.owner.setStats("No process died !");
		}

	}

	private void step()
	{

		for (int i = 0; i < this.p_count; i++)
		{
			if (this.queue[i].active() && this.queue[i].execute() == false)
			{
				
				if(this.owner == null)
				{
					System.out.println("MAIN >> Process " + this.pids[i] + " died at cycle " + this.cycle);
				}
				else
				{
					//System.out.println(this.owner.getSessionId() + " >> Process " + this.pids[i] + " died at cycle " + this.cycle);
					this.owner.setStats(this.pids[i] + " : " + this.cycle);
				}

			}
		}

	}

	public Memory getMemory()
	{
		return this.memory;
	}

	public int getProcessCount()
	{
		return this.p_count;
	}

	private int createProcess(String name, int pointer)
	{

		if(p_count < this.queue.length && name != null)
		{
			this.queue[p_count] = new Process(this, pointer%this.memory.getSize(), name);
			this.p_count++;
			return pointer%this.memory.getSize();
		}

		return -1;
	}

}