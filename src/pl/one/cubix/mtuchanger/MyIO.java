package pl.one.cubix.mtuchanger;

import java.io.*;

public class MyIO {
	public static int ReadInt(FileInputStream fin)
	{
		try {
			InputStreamReader isReader = new InputStreamReader(fin);
			char[] buffer = new char[4];
			isReader.read(buffer);
			isReader.close();
			fin.close();
			return Integer.parseInt(new String(buffer));
		}
		catch (Exception e) {
			return 1500;
		}
	}
	
	public static void WriteInt(FileOutputStream fout, int n)
	{
		try {
			fout.write((n+"").getBytes());
			fout.close();
		}
		catch (Exception e)
		{
			
		}
	}

}
