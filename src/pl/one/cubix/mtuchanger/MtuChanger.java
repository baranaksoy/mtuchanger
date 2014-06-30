package pl.one.cubix.mtuchanger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MtuChanger implements Serializable {

	private String interfaceName;
	private int mtu;
	private String methodId;
	private boolean autostart = false;
	private String serviceId;

	final static String LOG = "MtuChanger";
	private static final long serialVersionUID = 2L;

	public MtuChanger() {
		this.interfaceName = "lo";
		this.mtu = 1500;
		this.methodId = MethodFactory.Default;
		this.serviceId = ServiceFactory.Default;
	}

	public MtuChanger(String interfaceName, int mtu, String methodId,
			String serviceClass) {
		this.interfaceName = interfaceName;
		this.mtu = mtu;
		this.methodId = methodId;
		this.serviceId = serviceClass;
	}

	public String GetInterfaceIP() {
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec("netcfg");
			p.waitFor();
			DataInputStream dis = new DataInputStream(p.getInputStream());
			String line;
			while ((line = dis.readLine()) != null) {
				String[] parts = line.split("[\t\n ]+");
				if (parts[0].equals(interfaceName)) {
					result = parts[2].trim();
				}
			}
			dis.close();
		} catch (IOException e) {
			Log.e(LOG, "Problem while reading the stream.");
		} catch (InterruptedException e) {
			Log.e(LOG, "Problem with process");
		} catch (Exception e) {
			Log.e(LOG, e.toString());
		}
		return result;
	}

	public int GetCurrentMtu() {
		int result = 0;
		try {

			BufferedReader br = new BufferedReader(new FileReader(new File(
					"/sys/class/net/" + interfaceName + "/mtu")));
			result = Integer.parseInt(br.readLine());
			br.close();
		} catch (IOException e) {
			Log.e(LOG, "Problem while reading the stream.");
		} catch (NumberFormatException e) {
			Log.e(LOG, "Problem while parsing file.");
		} catch (Exception e) {
			Log.e(LOG, e.toString());
		}
		return result;
	}

	public void RunService(Context context) {
		try {
			context.startService(new Intent(context, ServiceFactory
					.GetService(serviceId)));
		} catch (Exception e) {
			Log.e(LOG, "Exception while running service: " + e.toString());
		}
	}

	public void StopService(Context context) {
		try {
			context.stopService(new Intent(context, ServiceFactory
					.GetService(serviceId)));
		} catch (Exception e) {
			Log.e(LOG, "Exception while stopping service: " + e.toString());
		}
	}

	public String GetMethodInfo() {
		return methodId;
	}

	public String GetServiceInfo() {
		return serviceId;
	}

	public boolean GetServiceStatus(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (ServiceFactory.GetService(serviceId).getName()
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;

	}

	public int GetTargetMtu() {
		return mtu;
	}

	public String GetInterfaceName() {
		return interfaceName;
	}

	public boolean GetAutostartStatus() {
		return autostart;
	}

	public void SetInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void SetMtu(int mtu) {
		this.mtu = mtu;
	}

	public void SetMethod(String methodId) {
		this.methodId = methodId;
	}

	public void SetService(String serviceId) {
		this.serviceId = serviceId;
	}

	public void SetAutostart(boolean onBoot) {
		this.autostart = onBoot;
	}

	public static ArrayList<String> GetInterfacesList() {
		ArrayList<String> res = new ArrayList<String>();

		try {

			File netDir = new File("/sys/class/net/");
			for (File file : netDir.listFiles()) {
				if (file.isDirectory())
					res.add(file.getName());
			}
		} catch (Exception e) {
			Log.e(LOG, e.toString());
		}
		return res;
	}
}
