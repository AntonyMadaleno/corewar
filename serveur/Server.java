import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread 
{

	final static int port = 9000;
  	private Socket socket;
  	private String name;
  	private String filename;
    private String stats;

  	public static void main(String[] args) throws Exception
  	{

        Scanner keybord_input;

        //hostname
        System.out.print("\nEntrer le nom de votre serveur : ");
        keybord_input = new Scanner(System.in);
        String hostname = keybord_input.nextLine();

        //max player
        System.out.print("\nEntrez le nombre de joueurs max : ");
        keybord_input = new Scanner(System.in);
        String max = keybord_input.nextLine();

  		Session[] sessions = new Session[10];
  		int cursor = 0;
  		int maxP = Integer.parseInt(max);
  		String[] files = new String[maxP];
        Server[] servs = new Server[maxP];

  		for(int i = 0; i < 10; i++)
  		{
  			sessions[i] = new Session(hostname + "-" + i, maxP, hostname);
  		}

    	try 
    	{

    		boolean loop = true;
      		ServerSocket socketServeur = new ServerSocket(port);
      		System.out.println("Lancement du serveur sur le port : " + port);
      		while (loop) 
      		{
        		Socket socketClient = socketServeur.accept();
        		Server t = new Server(socketClient, hostname);
        		t.start();
                servs[cursor] = t;

        		while(true)
        		{
        			if (t.getFileName() != null)
        			{
        				System.out.println(t.getFileName());
        				files[cursor] = t.getFileName();
        				break;
        			}
        		}

        		cursor++;

        		if (cursor == maxP)
        		{
        			loop = false;
        		}
      		}

    	} 
    	catch (Exception e) 
    	{
      		e.printStackTrace();
    	}

    	for(int i = 0; i < 10; i++)
  		{
  			sessions[i].submitFiles(files, files);
  			sessions[i].start();
  		}

        String stat = "";

        int count = 0;

        while (count < 10)
        {   
            System.out.print(""); //si je le retire ça marche plus mais jsp pourquoi (j'ai cassé 2 souris à cause de ça)
            if(sessions[count].finished())
            {
                stat += sessions[count].getStats() + "\n";
                count++;
            }

        }

        System.out.println(stat);

        for (int i = 0; i < servs.length; i++)
        {
            servs[i].setStats(stat);
        }

  	}

  	public Server(Socket socket, String name) 
  	{
    	this.socket = socket;
    	this.name = name;
  	}

  	public void run() 
  	{
    	try 
    	{
      		String message = "connexion sur le port : " + port;

      		System.out.println("Connexion avec le client : " + socket.getInetAddress());

      		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      		PrintStream out = new PrintStream(socket.getOutputStream());
      		out.println("Serv : " + message);
      		filename = in.readLine();

      		String redcode = "";
      		boolean loop = true;

            String nl;

            String dir = "../Save/server/" + this.name + "/redcode";

      		while (loop)
      		{
      			try
      			{
      				nl = in.readLine();
      				if (nl.equals("exit"))
      				{

                        if (!RedCode.verify(dir, filename))
                        {
                            redcode = "";
                            loop = false;
                        }
                        else
                        {
                            loop = false;
                        }

      				}
      				else {
      					redcode += nl + "\n";
      				}
      			}
      			catch (Exception e)
      			{
      				break;
      			}
      		}

      		this.save(filename, redcode);

            while (true)
            {

                try
                {

                    System.out.print("");

                    if(this.stats != null)
                    {
                        out.println("STATS " + this.stats);
                        break;
                    }
                }
                catch (Exception e)
                {
                    //do nothing
                }

            }

            out.println("END");

      		socket.close();
    	} 
    	catch (Exception e) 
    	{
      		e.printStackTrace();
    	}
  	}

  	private void save(String filename, String rc)
	{

		String dir = "../Save/server/" + this.name + "/redcode";

		new File(dir).mkdirs();
		File save = new File(dir + "/" + filename + ".RED");

		try 
		{
			FileWriter fw = new FileWriter(save);

			fw.write(rc);

			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

    public void setStats(String s)
    {
        this.stats = s;
    }

	public String getFileName()
	{
		return this.filename;
	}

}