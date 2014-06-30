package pl.one.cubix.mtuchanger;

import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

public class IfconfigMethod implements IMethod {

	final String LOG = "IfconfigMethod";

	@Override
	public void Do(String interfaceName, int mtu) {
		try {
			Process p = Runtime.getRuntime().exec("su");

			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("ifconfig " + interfaceName + " mtu " + mtu + "\n");

			os.writeBytes("exit\n");
			os.flush();
			try {
				p.waitFor();
				if (p.exitValue() != 255) {
					Log.v(LOG, "Execution of command successful.");
				} else {
					Log.e(LOG, "Process error return value.");
				}
			} catch (InterruptedException e) {
				Log.e(LOG, e.toString());
			}
		} catch (IOException e) {
			Log.e(LOG, e.toString());
		}
	}
}
