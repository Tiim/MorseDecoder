package tim.matura.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import tim.matura.app.AppMorseTranslator.R;
import tim.matura.app.widget.LogWidget;
import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;
import tim.matura.morse.util.MorseToAudio;
import tim.matura.processing.ILengthUpdateListener;
import tim.matura.processing.ITextReceiver;
import tim.matura.processing.debug.FileLogger;
import tim.matura.processing.impl.*;
import tim.matura.sound.SoundDecoder;
import tim.matura.utils.Logging;

public class Main extends Activity {


    private Thread recordThread;
    private SoundDecoder decoder;
    private LogWidget textView;
    private AudioTrack playbackTrack;

    private LengthToMorse l2mProcessor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle unused) {
        super.onCreate(null);
        setContentView(R.layout.main);
//        soundGraph = (GraphWidget) findViewById(R.id.soundGraph);
        textView = (LogWidget) findViewById(R.id.textOutput);
//        textDitLength = (EditText) findViewById(R.id.textDitLength);
        Logging.setLogWidget((LogWidget) findViewById(R.id.log));
        Logging.d("Finished starting app");
       textView.append("Output: ");
    }

    public void stopRecording(final View unused) {
        if (recordThread != null) {
            Logging.d("Stop Recording");
            decoder.finish(recordThread);
            recordThread = null;
        }
    }

    //TODO: Pretty this ugly section up :(
    public void startRecording(final View unused) {
        if (recordThread != null) {
            Logging.d("Recording already started.");
            return;
        }
        Logging.d("Start Recording ..");
        decoder = new SoundDecoder(512);
        l2mProcessor = new LengthToMorse(
                new MorseToTextProcessor(
                        new ITextReceiver() {
                            @Override
                            public void setText(final String string) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.append(string);
                                    }
                                });
                            }
                        }
                )
        );
        decoder.setSampleReceiver(
                new BufferVolumeProcessor(
                        new SndToBinaryProcessor(
                                new BinaryToSndLengthProcessor(l2mProcessor)
                        )
                )
        );
        recordThread = new Thread(decoder);
        recordThread.start();
    }

    //TODO: Implement stop button..
    public void playMorse(final View unused) {
        String message = ((EditText) findViewById(R.id.morseInput)).getText().toString();
        if (message != null && !message.isEmpty()) {
            Translator t = new Translator(message);
            MorseSequence s = t.getSequence();
            Logging.d(s.toString());
            MorseToAudio translator = new MorseToAudio(s);
            byte[] b = translator.getSound();
            //TODO: use not deprecated chanel configuration
            playbackTrack = new AudioTrack(AudioManager.STREAM_MUSIC, MorseToAudio.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, b.length, AudioTrack.MODE_STATIC);
            playbackTrack.write(b, 0, b.length);
            playbackTrack.play();
        }
    }

    public void stopMorse(final  View unused) {
        if (playbackTrack != null) {
            playbackTrack.pause();
            playbackTrack.flush();
            playbackTrack = null;
        }
    }
}
