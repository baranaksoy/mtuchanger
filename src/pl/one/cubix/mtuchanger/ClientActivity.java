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
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;


public class ClientActivity extends Activity {
    private Button btnStartService;
    private Button btnStopService;
    private EditText edtMtuValue;
    public static int mtu;
    
    private OnClickListener startServiceListener = new OnClickListener() {        
        @Override
        public void onClick(View arg0) {
        	try
        	{
        		mtu = Integer.parseInt(edtMtuValue.getText().toString());
        		startService(new Intent(ClientActivity.this, MyService.class));
        	}
        	catch (NumberFormatException nfe)
        	{
        		Toast.makeText(getApplicationContext(), "You have to type the MTU value!", Toast.LENGTH_LONG).show();
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
    }
}