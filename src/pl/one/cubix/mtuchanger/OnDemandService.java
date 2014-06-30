package pl.one.cubix.mtuchanger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class OnDemandService extends Service {

	String LOG = "OnDemandService";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		try {
			MtuChanger mtuChanger = (MtuChanger) SerializationHelper
					.Deserialize(openFileInput("MTUCHANGER"));
			MethodFactory.GetMethod(mtuChanger.GetMethodInfo()).Do(
					mtuChanger.GetInterfaceName(), mtuChanger.GetTargetMtu());
			Toast.makeText(getApplicationContext(), "MTU has been changed.",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(LOG, "Error while loading intent: " + e.toString());
		}
		stopSelf(startId);
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
