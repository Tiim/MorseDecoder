package tim.matura.app.AppMorseTranslator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import tim.matura.app.AppMorseTranslator.util.Ref;
import tim.matura.sound.SoundDecoder;

public class Main extends Activity {


    Thread recordThread;
    SoundDecoder decoder;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d(Ref.NAME, "Start Recording");
    }

    public void stopRecording(View v) {
        if (recordThread != null) {
            Log.d(Ref.NAME, "Stop Recording");
            decoder.finish();
            recordThread.interrupt();
            recordThread = null;
        }
    }

    public void startRecording(final View v) {
        if (recordThread == null) {
            new ProgressDialog(this);
//            decoder = new SoundDecoder(512);
//            decoder.setSoundReceiver(new SndToBinaryProcessor(new BinaryToSndLengthProcessor(new LengthToMorseProcessor(new MorseToTextProcessor(new ITextReceiver() {
//                @Override
//                public void setText(String string) {
//                    Log.d(Ref.NAME, string);
//                }
//            })))));
            recordThread = new Thread(decoder);
            recordThread.start();
        }
    }
}
