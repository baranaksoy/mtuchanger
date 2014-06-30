package pl.one.cubix.mtuchanger;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodFactory {
	private static HashMap<String, IMethod> methods;
	public final static String Default;

	/*
	 * This functionality should use reflection. Unfortunately it's not well
	 * implemented on Android devices...
	 */
	static {
		methods = new HashMap<String, IMethod>();
		// put your methods here
		methods.put("ifconfig", new IfconfigMethod());

		// choose default method
		Default = "ifconfig";
	}

	public static IMethod GetMethod(String id) {
		return methods.get(id);
	}

	public static ArrayList<String> GetMethods() {
		return new ArrayList<String>(methods.keySet());
	}
}
