package tim.matura.utils;

import android.util.Log;
import tim.matura.app.Main;
import tim.matura.app.widget.LogWidget;

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

    public static final List<String> MESSAGES = new ArrayList<>();

    private static LogWidget view;

    public static void setLogWidget(LogWidget c) {
        view = c;
    }

    public static void d(String message) {
        Log.d(NAME, INSTANCE_ID + " - " + message);
        show(message);
    }

    public static void df(String message, Object... arg) {
        d(String.format(message, arg));
    }

    public static void d(String message, Throwable t) {
        Log.d(NAME, INSTANCE_ID + " - " + message, t);
        show("[DEBUG] " + message + " --> " + t.getMessage());
    }

    public static void e(String message) {
        Log.e(NAME, INSTANCE_ID + " - " + message);
        show("[ERROR] " + message);
    }

    public static void e(String message, Throwable t) {
        Log.e(NAME, INSTANCE_ID + " - " + message, t);
        show("[ERROR] " + message + " --> " + t.getMessage());
    }

    public static void w(String message) {
        Log.w(NAME, INSTANCE_ID + " - " + message);
        show("[WARNING] " + message);
    }

    public static void w(String message, Throwable t) {
        Log.w(NAME, INSTANCE_ID + " - " + message, t);
        show("[WARNING] " + message + "-->" + t.getMessage());
    }

    public static void show(String s) {
        if (view != null && s != null) {
            view.append(s);
        }
    }
}
