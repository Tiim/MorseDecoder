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
import tim.matura.app.widget.GraphWidget;
import tim.matura.app.widget.LogWidget;
import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;
import tim.matura.morse.util.MorseToAudio;
import tim.matura.processing.ITextReceiver;
import tim.matura.processing.impl.*;
import tim.matura.sound.SoundDecoder;
import tim.matura.utils.Logging;

public class Main extends Activity {


    private Thread recordThread;
    private SoundDecoder decoder;
    private GraphWidget soundGraph;
    private TextView textView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.main);
        soundGraph = (GraphWidget) findViewById(R.id.soundGraph);
        textView = (TextView) findViewById(R.id.textOutput);
        Logging.setLogWidget((LogWidget) findViewById(R.id.log));
        Logging.d("Finished starting app");
    }

    public void stopRecording(View v) {
        if (recordThread != null) {
            Logging.d("Stop Recording");
            decoder.finish(recordThread);
            recordThread = null;
        }
    }

    public void startRecording(final View v) {
        if (recordThread != null) {
            Logging.d("Recording already started.");
            return;
        }
        Logging.d("Start Recording ..");
        decoder = new SoundDecoder(512);


        decoder.setSampleReceiver(
                new SampleSmoother(
                        new SndToBinaryProcessor(
                                new BinaryToSndLengthProcessor(
                                        new LengthToMorse2(
                                                new MorseToTextProcessor(
                                                        new ITextReceiver() {
                                                            @Override
                                                            public void setText(String string) {
                                                                textView.append(string);
                                                            }
                                                        }
                                                )
                                        )
                                )
                        ),
                        soundGraph
                )
        );

        recordThread = new Thread(decoder);
        recordThread.start();
    }


    public void playMorse(final View v) {
        String message = ((EditText) findViewById(R.id.morseInput)).getText().toString();
        if (message != null && !message.isEmpty()) {
            Translator t = new Translator(message);
            MorseSequence s = t.getSequence();
            Logging.d(s.toString());
            MorseToAudio translator = new MorseToAudio(s);
            byte[] b = translator.getSound();
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, MorseToAudio.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, b.length, AudioTrack.MODE_STATIC);
            track.write(b, 0, b.length);
            track.play();
            Logging.d("" + track.getPlayState());
        }
    }

}
