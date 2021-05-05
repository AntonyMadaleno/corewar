import java.io.*;
import java.util.*;

public class RedCode
{

	//Variables
	private String text;
	private int[][] compiled;

	//RedCode ruleset
	private String[] opcodeRef = {"DAT", "MOV", "ADD", "SUB", "JMP", "JMZ", "JMG", "DJZ", "CMP"};

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

	public static int getOpcodeID(String opcode)
	{

		String[] opcodeRef = {"DAT", "MOV", "ADD", "SUB", "JMP", "JMZ", "JMG", "DJZ", "CMP"};

		for (int i = 0; i < opcodeRef.length; i++)
		{
			if(opcodeRef[i].equals(opcode))
			{
				return i;
			}
		}

		return -1;

	}

	public static int[][] compile(String dir, String filename)
	{

		int[][] ret = new int[1024][5];
		int size = 0;

		File file = new File(dir + "/" + filename + ".RED");
		Scanner scanner;

		try {

			scanner = new Scanner(file);

			for (int i = 0; i < 1024; i++) 
			{
				try 
				{

					String line = scanner.nextLine();
					String[] parts = line.split(" ");

					if (getOpcodeID(parts[0]) != -1)
					{
						ret[i][0] = getOpcodeID(parts[0]); //opcode id
					}
					else
					{
						System.out.printf("Error at line (%d), \"%s\" opcode is invalid !\n", i, parts[0]);
						return null;
					}


					//ARG-A
					if (parts.length > 1)
					{
						if (parts[1].charAt(0) == '#' || ret[i][0] == 0)
						{
							ret[i][1] = 1;
						}
						else
						{
							if (parts[1].charAt(0) == '@')
							{
								ret[i][1] = 2;
							}
							else 
							{
								if(parts[1].charAt(0) == '-' || parts[1].charAt(0) == '0' || parts[1].charAt(0) == '1' || parts[1].charAt(0) == '2' || parts[1].charAt(0) == '3' || parts[1].charAt(0) == '4' || parts[1].charAt(0) == '5' || parts[1].charAt(0) == '6' || parts[1].charAt(0) == '7' || parts[1].charAt(0) == '8' || parts[1].charAt(0) == '9')
								{
									ret[i][1] = 0;
								}
								else
								{
									ret[i][1] = -1;
									System.out.printf("Error at line (%d), \"%s\" mode is invalid !\n", i, parts[2]);
									return null;
								}
							}
						}

						if (ret[i][1] == 0 || ret[i][0] == 0)
						{
							ret[i][2] = Integer.parseInt(parts[1]);
						}
						else
						{
							ret[i][2] = Integer.parseInt(parts[1].substring(1, parts[1].length() ) );
						}
					}

					//ARG-B
					if (parts.length == 3)
					{
						if (parts[2].charAt(0) == '#')
						{
							ret[i][3] = 1;
						}
						else
						{
							if (parts[2].charAt(0) == '@')
							{
								ret[i][3] = 2;
							}
							else 
							{
								if(parts[2].charAt(0) == '-' || parts[2].charAt(0) == '0' || parts[2].charAt(0) == '1' || parts[2].charAt(0) == '2' || parts[2].charAt(0) == '3' || parts[2].charAt(0) == '4' || parts[2].charAt(0) == '5' || parts[2].charAt(0) == '6' || parts[2].charAt(0) == '7' || parts[2].charAt(0) == '8' || parts[2].charAt(0) == '9')
								{
									ret[i][3] = 0;
								}
								else
								{
									ret[i][3] = -1;
									System.out.printf("Error at line (%d), \"%s\" mode is invalid !\n", i, parts[2]);
									return null;
								}
							}
						}

						if (ret[i][3] == 0)
						{
							ret[i][4] = Integer.parseInt(parts[2]);
						}
						else
						{
							ret[i][4] = Integer.parseInt(parts[2].substring(1, parts[2].length() ) );
						}
					}

				}
				catch (NoSuchElementException e) 
				{
					size = i;
					i = 1024;
				}//get out of the loop
			}



			scanner.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		return Arrays.copyOfRange(ret, 0, size);

	}

	public static boolean verify(String dir, String filename)
	{

		int[][] compiled = RedCode.compile(dir, filename);
		boolean ret = true;

		try 
		{
			for (int i = 0; i < compiled.length; i++)
			{
				if(compiled[i][0] == -1) {ret = false;} //opcode
				if(compiled[i][1] == -1) {ret = false;} //mode-a
				if(compiled[i][3] == -1) {ret = false;} //mode-b
			}

			return ret;
		}
		catch (NullPointerException e)
		{
			return false;
		}

		

	}

	public static String reverse(int[] instr)
	{

		String[] opcodeRef = {"DAT", "MOV", "ADD", "SUB", "JMP", "JMZ", "JMG", "DJZ", "CMP"};

		String opcode;

		if (instr[0] != -1)
		{
			opcode = opcodeRef[instr[0]];
		}
		else
		{
			opcode = "/!\\";
		}
		
		String argA = "";
		String argB = "";


		//argA

		if (instr[1] == 1 && instr[0] != 0)
		{
			argA = argA + "#";
		}
		if (instr[1] == 2)
		{
			argA = argA + "@";
		}

		//argB

		if (instr[3] == 1)
		{
			argB = argB + "#";
		}
		if (instr[3] == 2)
		{
			argB = argB + "@";
		}

		argA = argA + String.valueOf(instr[2]);
		argB = argB + String.valueOf(instr[4]);

		return opcode + "\t" + argA + "\t" + argB; 

	}

}