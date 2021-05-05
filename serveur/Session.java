import java.io.*;
import java.util.*;

public class Session extends Thread 
{

	private Thread t;
	private String session_id;
	private String saveDir;
	private MARS mars;
	private String[] files;
	private String[] players;
	private int file_count;
	private String stats;
	private boolean finished = false;


	public Session(String id, int max_player, String saveDir)
	{
		this.session_id = id;
		this.saveDir = saveDir;
		this.files = new String[max_player];
		this.players = new String[max_player];
		this.file_count = 0;
	}

	public String[] getPlayers()
	{
		return this.players;
	}

	public void submit(String player, String filename)
	{
		if (this.file_count < this.files.length)
		{
			this.files[file_count] = filename;
			this.players[file_count] = player;
			this.file_count++;
		}
		else
		{
			System.out.println("Session " + this.session_id + " : nombre de soumission de fichier depasser !");
		}

	}

	public void submitFiles(String[] players, String[] files)
	{
		for (int i = 0; i < files.length; i++)
		{
			this.submit(players[i], files[i]);
		}
	}

	public void setMars(int mem_size, int max_cycle)
	{
		this.mars = new MARS(mem_size, max_cycle, this.files.length);
		this.mars.init();
	}

	public String getSessionId()
	{
		return this.session_id;
	}

	public void run()
	{

		try
		{

			System.out.println(session_id + " running !");

			if(this.mars == null)
			{
				this.mars = new MARS(files.length*2500, files.length*25000, this.files.length);
				this.mars.init();
			}
			this.mars.setOwner(this);
			this.mars.load("../Save/server/" + this.saveDir + "/redcode" , this.files);
			this.mars.run();
			this.mars.getMemory().save("../Save/server/" + this.saveDir + "/" + this.session_id, session_id);
			this.save("../Save/server/" + this.saveDir + "/" + this.session_id, session_id);
			this.finished = true;

		}
		catch(Exception e)
		{
			System.out.println("Session :" + session_id + " has stopped working !");
			e.printStackTrace();
			this.finished = true;
		}

	}

	public void start()
	{
		if(t == null)
		{
			t = new Thread(this, session_id);
			t.start();
		}
	}

	public void setStats(String s)
	{
		if (this.stats != null)
		{
			this.stats += "\n" + session_id + " : " + s;
		}
		else
		{
			this.stats = session_id + " : " + s;
		}
	}

	public String getStats()
	{
		return this.stats;
	}

	public boolean finished()
	{
		return this.finished;
	}

	public void save(String dir, String filename)
	{

		new File(dir).mkdirs();
		File save = new File(dir + "/" + filename + ".stat");

		try 
		{
			FileWriter fw = new FileWriter(save);

			try 
			{

				fw.write(files.length + "");
				for (int i = 0; i < files.length; i++)
				{
					fw.write(" : " + files[i]);
				}
				fw.write("\n");
				fw.write(this.getStats());
				
			}
			catch (Exception e)
			{
				System.out.println("Cant write stats");
			}


			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}