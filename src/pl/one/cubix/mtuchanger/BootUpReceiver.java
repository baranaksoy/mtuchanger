/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.*;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		boolean status = false;
		try
		{
			status = (MyIO.ReadBool(context.getApplicationContext().openFileInput("AUTOSTART")));
		}
		catch (FileNotFoundException fnfe)
		{
			Log.e("BootUpReceiver", fnfe.toString());
		}
		
		if(status)
		{
			Intent myIntent = new Intent(context.getApplicationContext(), MyService.class);
			context.getApplicationContext().startService(myIntent);
		}
	}

}
