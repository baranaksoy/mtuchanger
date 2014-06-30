package pl.one.cubix.mtuchanger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.util.Log;

public class SerializationHelper {
	final static String LOG = "SerializationHelper";

	public static void Serialize(FileOutputStream fileOut, Object o) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			Log.e(LOG, e.toString());
		}
	}

	public static Object Deserialize(FileInputStream fileIn) {
		Object result = null;
		try {
			ObjectInputStream in = new ObjectInputStream(fileIn);
			result = in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			Log.e(LOG, e.toString());
		}
		return result;
	}
}