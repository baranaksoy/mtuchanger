package pl.one.cubix.mtuchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {
	final String LOG = "BootUpReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		MtuChanger mtuChanger = null;
		try {
			mtuChanger = (MtuChanger) SerializationHelper.Deserialize(context
					.openFileInput("MTUCHANGER"));
		} catch (Exception e) {
			Log.e(LOG, "Exception while deserializing: " + e.toString());
		}
		if (mtuChanger == null) {
			mtuChanger = new MtuChanger();
		}

		if (mtuChanger.GetAutostartStatus()) {
			mtuChanger.RunService(context.getApplicationContext());
		}
	}

}
