/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.util.Log;


public class ClientActivity extends Activity {
    private Button btnStartService, btnStopService, btnCheckInterfaceName;
    private EditText edtMtuValue, edtInterfaceName;
    private CheckBox cbxAutostart;
    
    private OnClickListener startServiceListener = new OnClickListener() {        
    	@Override
        public void onClick(View arg0) {
        	try
        	{
        		int mtu = Integer.parseInt(edtMtuValue.getText().toString());
        		MyIO.WriteInt(openFileOutput("MTU_SETTINGS", 0), mtu);
        		
        		if(edtInterfaceName.getText().length() == 0)
        		{
        			Toast.makeText(getApplicationContext(), "You have to type interface name!", Toast.LENGTH_LONG).show();
        			return;
        		}
        		String ifc = edtInterfaceName.getText().toString().trim();
        		MyIO.WriteString(openFileOutput("INTERFACE_NAME", 0), ifc);
        		
        		startService(new Intent(ClientActivity.this, MyService.class));
        	}
        	catch (NumberFormatException nfe)
        	{
        		Toast.makeText(getApplicationContext(), "You have to type the MTU value!", Toast.LENGTH_LONG).show();
        	}
        	catch (Exception e)
        	{
        		Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        		Log.e("StartServiceClick", e.toString());
        	}
        }
    };
    
    private OnClickListener stopServiceListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            stopService(new Intent(ClientActivity.this, MyService.class));
        }
    };
    
    private OnClickListener checkInterfaceNameListener = new OnClickListener() {
    	@Override
    	public void onClick(View arg0) {
    		edtInterfaceName.setText(getInterfaceName());
    	}
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStopService  = (Button)findViewById(R.id.btnStopService);
        btnCheckInterfaceName = (Button)findViewById(R.id.btnCheckInterfaceName);
        btnStartService.setOnClickListener(startServiceListener);
        btnStopService.setOnClickListener(stopServiceListener);
        btnCheckInterfaceName.setOnClickListener(checkInterfaceNameListener);
        edtMtuValue = (EditText) findViewById(R.id.edtMtuValue);
        edtInterfaceName = (EditText) findViewById(R.id.edtInterfaceName);
        cbxAutostart = (CheckBox) findViewById(R.id.cbxAutostart);
        
        cbxAutostart.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		try
        		{
        			MyIO.WriteBool(getApplicationContext().openFileOutput("AUTOSTART", 3), isChecked);
        		}
        		catch (FileNotFoundException fnfe)
        		{
        			Log.e("WritingAutostart", fnfe.toString());
        		}
        	}
        });
        
        try {
        	edtMtuValue.setText(MyIO.ReadInt(openFileInput("MTU_SETTINGS"))+"");
        	cbxAutostart.setChecked(MyIO.ReadBool(getApplicationContext().openFileInput("AUTOSTART")));
        	String ifce = MyIO.ReadString(openFileInput("INTERFACE_NAME"));
        	if(ifce == null)
        		ifce = getInterfaceName();
        	edtInterfaceName.setText(ifce);
        }
        catch (Exception e){
        	Log.e("SetText", e.toString());
        }
    }
    
    private String getInterfaceName()
    {
    	String ifce = "";
    	try
    	{
    		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipInt=wifiInfo.getIpAddress();
			byte[] bytes = BigInteger.valueOf(ipInt).toByteArray();
			if(ByteOrder.nativeOrder()==ByteOrder.LITTLE_ENDIAN) {
				byte b0=bytes[0];
				byte b1=bytes[1];
				bytes[0]=bytes[3];
				bytes[1]=bytes[2];
				bytes[2]=b1;
				bytes[3]=b0;
			}
			InetAddress address = InetAddress.getByAddress(bytes);
			NetworkInterface network=NetworkInterface.getByInetAddress(address);
    		ifce = network.getName();
    	}
    	catch (Exception e)
    	{
    		Log.e("GetInterfaceName", e.toString());
    		Toast.makeText(getApplicationContext(), "You have to be connected to a network!", Toast.LENGTH_SHORT).show();
    	}
    	return ifce;
    }
}