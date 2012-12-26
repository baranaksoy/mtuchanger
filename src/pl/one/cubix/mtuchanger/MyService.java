/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.io.*;

public class MyService extends Service {

	private BroadcastReceiver mWifiStateChangedReceiver;
    private Toast myToast;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mWifiStateChangedReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED)
                {
                	try {
                		changeMtu(MyIO.ReadInt(openFileInput("MTU_SETTINGS")));
                	}catch (Exception e){
                		Log.e("MyService", e.toString());
                	}
                }

            }
        };
        
        myToast = Toast.makeText(getApplicationContext(), 
                                 "The service has been created", 
                                 Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    public void onDestroy() {
    	this.unregisterReceiver(mWifiStateChangedReceiver);
    	
        myToast.setText("The service has been stopped");
        myToast.show();
        
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        this.registerReceiver(mWifiStateChangedReceiver,new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        myToast.setDuration(Toast.LENGTH_LONG);
        try {
        	myToast.setText("The service has been launched.\nMTU will be changed to " + MyIO.ReadInt(openFileInput("MTU_SETTINGS")));
        }catch (Exception e){
        	Log.e("MyService", e.toString());
        }
        myToast.show();
        myToast.setDuration(Toast.LENGTH_SHORT);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    public void changeMtu(int x)
	{
		java.lang.Process p;  
		try {  
			// Perform su to get root privileges  
			p = Runtime.getRuntime().exec("su");   

			// Attempt to write a file to a root-only  
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  
			os.writeBytes("busybox ifconfig tiwlan0 mtu " + x + "\n");  

			// Close the terminal  
			os.writeBytes("exit\n");  
			os.flush();  
			try {  
				p.waitFor();  
				if (p.exitValue() != 255) {  
					// Code to run on success  
					myToast.setText("Changed MTU to " + x);  
				}  
				else {  
					// Code to run on unsuccessful  
					myToast.setText("Error");
					Log.e("MyService", "ChangeMtu unsuccessful");
				}  
			} catch (InterruptedException e) {  
				// Code to run in interrupted exception  
				myToast.setText("Error");
				Log.e("MyService", e.toString());
			}  
		} catch (IOException e) {  
			// Code to run in input/output exception  
			myToast.setText("Error");
			Log.e("MyService", e.toString());
		}  
		myToast.show();
	}

}
