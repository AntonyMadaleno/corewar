import java.net.*;
import java.io.*;
import java.util.*;

public class Client {

  	final static int port = 9000;

  	public static void main(String[] args) 
  	{

    	Socket socket;
    	PrintStream out;

        Scanner keybord_input;

        //host
        System.out.print("\nEntrer l'addresse de l'hote : ");
        keybord_input = new Scanner(System.in);
        String host = keybord_input.nextLine();

        //username
        System.out.print("\nEntrer votre nom : ");
        keybord_input = new Scanner(System.in);
        String username = keybord_input.nextLine();

        //path
        System.out.print("\nEntrer le chemin du dossier contenant votre fichier redcode : ");
        keybord_input = new Scanner(System.in);
        String dir = keybord_input.nextLine();

        //filename
        System.out.print("\nEntrer le nom de votre fichier redcode : ");
        keybord_input = new Scanner(System.in);
        String filename = keybord_input.nextLine();

    	Scanner scanner;

    	try 
    	{

      		InetAddress serveur = InetAddress.getByName(host);
      		socket = new Socket(serveur, port);

      		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      		out = new PrintStream(socket.getOutputStream());

      		System.out.println(in.readLine());

      		out.println(username + "_" + filename);

      		File file = new File(dir + "/" + filename + ".RED");
      		scanner = new Scanner(file);
      		String nl;
      		int size = 1024;

      		for (int i=0; i < 1024; i++) {
	      		try 
	      		{
	      			nl = scanner.nextLine();
	      			out.println(nl);
	      		}
	      		catch (NoSuchElementException e)
	      		{
	      			size = i;
					i = 1024;
	      		}

	      	}
	      	out.println("exit");

            boolean loop = true;

            while (loop)
            {
                try
                {
                    nl = in.readLine();

                    if(nl.equals("END"))
                    {
                        loop = false;
                    }
                    else
                    {
                        if(nl != null)
                        {
                            System.out.println("Server : " + nl);
                        }
                    }


                }
                catch(Exception e)
                {
                    break;
                }
            }

            socket.close();

    	} 
    	catch (Exception e) 
    	{
      		e.printStackTrace();
    	}
  	}
}