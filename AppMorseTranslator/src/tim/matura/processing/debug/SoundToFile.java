package tim.matura.processing.debug;

import android.os.Environment;
import tim.matura.processing.ISampleReceiver;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.Logging;

import java.io.*;

/**
 * @author Tim
 * @since 03.11.13 13:36
 */
public class SoundToFile implements ISoundReceiver, ISampleReceiver {

    private Writer osSmooth;
    private Writer osRaw;

    private static final SoundToFile INSTANCE = new SoundToFile();


    private SoundToFile() {
        File fSmooth = new File(Environment.getExternalStorageDirectory(), "smooth.txt");
        File fRaw = new File(Environment.getExternalStorageDirectory(), "raw.txt");
        Logging.d(fSmooth.getAbsolutePath());
        Logging.d(fRaw.getAbsolutePath());

        if (fSmooth.exists()) {
            fSmooth.delete();
        }
        if (fRaw.exists()) {
            fRaw.delete();
        }
        try {
            fRaw.createNewFile();
            fSmooth.createNewFile();
            osSmooth = new OutputStreamWriter(new FileOutputStream(fSmooth));
            osRaw = new OutputStreamWriter(new FileOutputStream(fRaw));
        } catch (IOException e) {
            Logging.d("IOException", e);
        }
    }


    @Override
    public void receive(int soundSample) {
        if (osSmooth != null) {
            try {
                osSmooth.write(String.valueOf(soundSample));
                osSmooth.write("\r\n");
            } catch (IOException e) {
                Logging.d("IOException", e);
            }
        }
    }

    @Override
    public void setSample(int sample) {
        if (osRaw != null) {
            try {
                osRaw.write(String.valueOf(sample));
                osRaw.write("\r\n");
            } catch (IOException e) {
                Logging.d("IOException", e);
            }
        }
    }


    public void flush() {
        try {
            osRaw.flush();
            osSmooth.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void setSamplePerSecond(int x) {

    }

    public static SoundToFile getInstance() {
        return INSTANCE;
    }
}
