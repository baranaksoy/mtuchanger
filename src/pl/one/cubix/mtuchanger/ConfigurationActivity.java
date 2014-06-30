package pl.one.cubix.mtuchanger;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfigurationActivity extends Activity {
	final String LOG = "ConfigurationActivity";
	MtuChanger mtuChanger;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);

		final Spinner interfaceSpinner = (Spinner) findViewById(R.id.interfaceSpinner);
		final EditText mtuEditText = (EditText) findViewById(R.id.mtuEditText);
		final Spinner methodSpinner = (Spinner) findViewById(R.id.methodSpinner);
		final Spinner serviceSpinner = (Spinner) findViewById(R.id.serviceSpinner);
		final CheckBox autostartCheckBox = (CheckBox) findViewById(R.id.autostartCheckBox);
		final Button saveButton = (Button) findViewById(R.id.saveButton);

		ArrayAdapter<String> interfaceAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				MtuChanger.GetInterfacesList());
		interfaceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		interfaceSpinner.setAdapter(interfaceAdapter);

		ArrayList<String> methodArrayList = MethodFactory.GetMethods();
		ArrayAdapter<String> methodAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, methodArrayList);
		methodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		methodSpinner.setAdapter(methodAdapter);

		ArrayList<String> serviceArrayList = ServiceFactory.GetServices();
		ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, serviceArrayList);
		serviceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serviceSpinner.setAdapter(serviceAdapter);

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

		interfaceSpinner.setSelection(interfaceAdapter.getPosition(mtuChanger
				.GetInterfaceName()));
		mtuEditText.setText(mtuChanger.GetTargetMtu() + "");
		methodSpinner.setSelection(methodAdapter.getPosition(mtuChanger
				.GetMethodInfo()));
		serviceSpinner.setSelection(serviceAdapter.getPosition(mtuChanger
				.GetServiceInfo()));
		autostartCheckBox.setChecked(mtuChanger.GetAutostartStatus());

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					mtuChanger.SetInterfaceName(interfaceSpinner
							.getSelectedItem().toString());
					mtuChanger.SetMtu(Integer.parseInt(mtuEditText.getText()
							.toString()));
					mtuChanger.SetMethod(methodSpinner.getSelectedItem()
							.toString());
					mtuChanger.SetService(serviceSpinner.getSelectedItem()
							.toString());
					mtuChanger.SetAutostart(autostartCheckBox.isChecked());

					finish();
				} catch (NumberFormatException e) {
					Toast.makeText(getApplicationContext(),
							"Type proper values!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();

		try {
			SerializationHelper.Serialize(
					openFileOutput("MTUCHANGER", MODE_PRIVATE),
					mtuChanger);
		} catch (Exception e) {
			Log.e(LOG, "Exception while serializing: " + e.toString());
		}
	}
}
