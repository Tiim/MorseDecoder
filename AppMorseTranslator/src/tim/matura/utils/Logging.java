package tim.matura.utils;

import android.util.Log;
import tim.matura.app.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tiim
 * @since 24.08.13 10:36
 */
public class Logging {

    public static final int INSTANCE_ID = new Random().nextInt(2000);

    public static final String NAME = Main.class.getPackage().getName();

    public static final List<String> MESSAGES = new ArrayList<String>();

    public static void d(String message) {
        Log.d(NAME,INSTANCE_ID + " - " + message);
        MESSAGES.add("[DEBUG] " + message);
    }

    public static void d(String message, Throwable t) {
        Log.d(NAME, INSTANCE_ID + " - " + message, t);
        MESSAGES.add("[DEBUG] " + message + " --> " + t.getMessage());
    }

    public static void e(String message) {
        Log.e(NAME, INSTANCE_ID + " - " + message);
        MESSAGES.add("[ERROR] " + message);
    }

    public static void e(String message, Throwable t) {
        Log.e(NAME, INSTANCE_ID + " - " + message, t);
        MESSAGES.add("[ERROR] " + message + " --> " + t.getMessage());
    }

    public static void w(String message) {
        Log.w(NAME, INSTANCE_ID + " - " + message);
        MESSAGES.add("[WARNING] " + message);
    }

    public static void w(String message, Throwable t) {
        Log.w(NAME, INSTANCE_ID + " - " + message, t);
        MESSAGES.add("[WARNING] " + message + "-->" + t.getMessage());
    }
}
