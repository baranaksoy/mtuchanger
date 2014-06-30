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

public class WiFiService extends Service {

	BroadcastReceiver receiver;
	MtuChanger mtuChanger;
	String LOG = "WiFiService";

	@Override
	public void onCreate() {
		Log.v(LOG, "onCreate");
		super.onCreate();

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED) {
					try {
						if (mtuChanger == null) {
							mtuChanger = (MtuChanger) SerializationHelper
									.Deserialize(context
											.openFileInput("MTUCHANGER"));
						}
						MethodFactory.GetMethod(mtuChanger.GetMethodInfo())
								.Do(mtuChanger.GetInterfaceName(), mtuChanger.GetTargetMtu());
						Toast.makeText(getApplicationContext(),
								"MTU has been changed.", Toast.LENGTH_SHORT)
								.show();
					} catch (Exception e) {
						Log.e(LOG, e.toString());
					}
				}

			}
		};
	}

	@Override
	public void onDestroy() {
		Log.v(LOG, "onDestroy");
		this.unregisterReceiver(receiver);

		Toast.makeText(getApplicationContext(), "WiFi monitor has been stopped.",
				Toast.LENGTH_LONG).show();

		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.v(LOG, "onStartCommand begin");

		registerReceiver(receiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));

		Toast.makeText(getApplicationContext(),
				"MTU will be changed on every turning on WiFi.",
				Toast.LENGTH_LONG).show();
		Log.v(LOG, "onStartCommand end");
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
