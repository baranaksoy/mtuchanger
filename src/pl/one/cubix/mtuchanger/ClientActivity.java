/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.util.Log;


public class ClientActivity extends Activity {
    private Button btnStartService;
    private Button btnStopService;
    private EditText edtMtuValue;
    private CheckBox cbxAutostart;
    
    private OnClickListener startServiceListener = new OnClickListener() {        
    	@Override
        public void onClick(View arg0) {
        	try
        	{
        		int mtu = Integer.parseInt(edtMtuValue.getText().toString());
        		MyIO.WriteInt(openFileOutput("MTU_SETTINGS", 0), mtu);
        		startService(new Intent(ClientActivity.this, MyService.class));
        	}
        	catch (NumberFormatException nfe)
        	{
        		Toast.makeText(getApplicationContext(), "You have to type the MTU value!", Toast.LENGTH_LONG).show();
        	}
        	catch (Exception e)
        	{
        		Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
        		Log.e("ClientAvtivity", e.toString());
        	}
        }
    };
    
    private OnClickListener stopServiceListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            stopService(new Intent(ClientActivity.this, MyService.class));
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStopService  = (Button)findViewById(R.id.btnStopService);
        btnStartService.setOnClickListener(startServiceListener);
        btnStopService.setOnClickListener(stopServiceListener);
        edtMtuValue = (EditText) findViewById(R.id.edtMtuValue);
        cbxAutostart = (CheckBox) findViewById(R.id.cbxAutostart);
        
        cbxAutostart.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		Log.d("CheckBox", "Checked:" + isChecked);
        		try
        		{
        			MyIO.WriteBool(getApplicationContext().openFileOutput("AUTOSTART", 3), isChecked);
        		}
        		catch (FileNotFoundException fnfe)
        		{
        			Log.e("ClientActivity", fnfe.toString());
        		}
        	}
        });
        
        try {
        	edtMtuValue.setText(MyIO.ReadInt(openFileInput("MTU_SETTINGS"))+"");
        	cbxAutostart.setChecked(MyIO.ReadBool(getApplicationContext().openFileInput("AUTOSTART")));
        }
        catch (Exception e){
        	Log.e("ClientActivity", e.toString());
        }
    }
}