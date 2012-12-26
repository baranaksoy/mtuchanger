/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import java.io.*;
import android.util.Log;

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
			Log.e("MyIO", e.toString());
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
			Log.e("MyIO", e.toString());
		}
	}
	public static boolean ReadBool(FileInputStream fin)
	{
		try {
			InputStreamReader isReader = new InputStreamReader(fin);
			char[] buffer = new char[1];
			isReader.read(buffer);
			isReader.close();
			fin.close();
			return (Integer.parseInt(new String(buffer))==1);
		}
		catch (Exception e) {
			Log.e("MyIO", e.toString());
			return false;
		}
	}
	
	public static void WriteBool(FileOutputStream fout, boolean n)
	{
		try {
			fout.write(((n?1:0)+"").getBytes());
			fout.close();
		}
		catch (Exception e)
		{
			Log.e("MyIO", e.toString());
		}
	}

}
