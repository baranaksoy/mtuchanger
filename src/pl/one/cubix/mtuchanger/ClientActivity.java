/**
 * MTUchanger
 * Copyright (c) 2012 Jakub "Cubix651" Cisło - http://cubix.one.pl
 * 
 * License: X11 (MIT) See README file.
 */
package pl.one.cubix.mtuchanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class ClientActivity extends Activity {
    private Button btnStartService;
    private Button btnStopService;
    private EditText edtMtuValue;
    
    private OnClickListener startServiceListener = new OnClickListener() {        
    	@Override
        public void onClick(View arg0) {
        	try
        	{
        		int mtu = Integer.parseInt(edtMtuValue.getText().toString());
        		startService(new Intent(ClientActivity.this, MyService.class));
        		MyIO.WriteInt(openFileOutput("MTU_SETTINGS", 0), mtu);
        	}
        	catch (NumberFormatException nfe)
        	{
        		Toast.makeText(getApplicationContext(), "You have to type the MTU value!", Toast.LENGTH_LONG).show();
        	}
        	catch (Exception e)
        	{
        		Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
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
        
        try {
        	edtMtuValue.setText(MyIO.ReadInt(openFileInput("MTU_SETTINGS"))+"");
        }
        catch (Exception e){ }
    }
}