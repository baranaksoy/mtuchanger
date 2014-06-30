package pl.one.cubix.mtuchanger;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceFactory {
	private static HashMap<String, Class<?>> services;
	public final static String Default;

	/*
	 * This functionality should use reflection. Unfortunately it's not well
	 * implemented on Android devices...
	 */
	static {
		services = new HashMap<String, Class<?>>();
		// put your services here
		services.put("on demand", OnDemandService.class);
		services.put("on turning WiFi", WiFiService.class);

		// choose default method
		Default = "on demand";
	}

	public static Class<?> GetService(String id) {
		return services.get(id);
	}

	public static ArrayList<String> GetServices() {
		return new ArrayList<String>(services.keySet());
	}
}
