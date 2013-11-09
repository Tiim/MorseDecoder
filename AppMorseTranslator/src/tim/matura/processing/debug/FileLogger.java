package tim.matura.processing.debug;

import android.os.Environment;
import tim.matura.morse.MorseCharacter;
import tim.matura.processing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Tim
 * @since 09.11.13 10:18
 */
public class FileLogger implements IBinaryReceiver, IMorseReceiver, ISampleReceiver, ISoundLengthReceiver, ISoundReceiver, ITextReceiver {

    private Writer w;

    public FileLogger(String name) {
        File f = new File(Environment.getExternalStorageDirectory(), name);
        if (f.exists()) f.delete();
        try {
            f.createNewFile();
            w = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSound(boolean sound) {
        write(String.valueOf(sound));
    }

    @Override
    public void setSample(int sample) {
        write(String.valueOf(sample));
    }

    @Override
    public void receive(float soundSample) {
        write(String.valueOf(soundSample));
    }

    @Override
    public void setSamplePerSecond(int x) {
        write(String.valueOf(x));
    }

    @Override
    public void setMorseChar(MorseCharacter ch) {
        write(String.valueOf(ch));
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {
        write(String.valueOf(length) + " - " + isSound);
    }

    @Override
    public void setText(String string) {
        write(string);
    }

    private void write(String s) {
        try {
            w.write(s);
            w.write("\r\n");
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
