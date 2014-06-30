package pl.one.cubix.mtuchanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InformationActivity extends Activity {

	final String LOG = "InformationActivity";
	Context context;
	MtuChanger mtuChanger;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);

		Button configButton = (Button) findViewById(R.id.configButton);
		Button startButton = (Button) findViewById(R.id.startButton);
		Button stopButton = (Button) findViewById(R.id.stopButton);
		Button refreshButton = (Button) findViewById(R.id.refreshButton);
		context = getApplicationContext();

		configButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ConfigurationActivity.class);
				startActivity(intent);
			}
		});

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mtuChanger.RunService(context);
				loadContent();
			}
		});

		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mtuChanger.StopService(context);
				loadContent();
			}
		});

		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadContent();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		mtuChanger = null;
		try {
			mtuChanger = (MtuChanger) SerializationHelper
					.Deserialize(openFileInput("MTUCHANGER"));
		} catch (Exception e) {
			Log.e(LOG, "Exception while deserializing: " + e.toString());
		}
		if (mtuChanger == null) {
			mtuChanger = new MtuChanger();
		}

		loadContent();
	}

	@Override
	public void onPause() {
		super.onPause();

		try {
			SerializationHelper.Serialize(
					openFileOutput("MTUCHANGER", MODE_PRIVATE), mtuChanger);
		} catch (Exception e) {
			Log.e(LOG, "Exception while serializing: " + e.toString());
		}
	}

	void loadContent() {
		TextView interfaceTextView = (TextView) findViewById(R.id.interfaceTextView);
		TextView ipTextView = (TextView) findViewById(R.id.ipTextView);
		TextView currentMtuTextView = (TextView) findViewById(R.id.currentMtuTextView);
		TextView targetMtuTextView = (TextView) findViewById(R.id.targetMtuTextView);
		TextView methodTextView = (TextView) findViewById(R.id.methodTextView);
		TextView serviceTextView = (TextView) findViewById(R.id.serviceTextView);
		TextView autostartTextView = (TextView) findViewById(R.id.autostartTextView);
		TextView statusTextView = (TextView) findViewById(R.id.statusTextView);

		interfaceTextView.setText(mtuChanger.GetInterfaceName());
		ipTextView.setText(mtuChanger.GetInterfaceIP());
		currentMtuTextView.setText(mtuChanger.GetCurrentMtu() + "");
		targetMtuTextView.setText(mtuChanger.GetTargetMtu() + "");
		methodTextView.setText(mtuChanger.GetMethodInfo() + "");
		serviceTextView.setText(mtuChanger.GetServiceInfo() + "");
		autostartTextView.setText(mtuChanger.GetAutostartStatus()?"ON":"OFF");
		statusTextView.setText(mtuChanger.GetServiceStatus(context)?"ON":"OFF");
	}
}