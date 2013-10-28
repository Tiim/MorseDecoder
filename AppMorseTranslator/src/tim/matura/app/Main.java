package tim.matura.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import tim.matura.app.AppMorseTranslator.R;
import tim.matura.app.widget.GraphWidget;
import tim.matura.app.widget.LogWidget;
import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;
import tim.matura.morse.util.MorseToAudio;
import tim.matura.processing.ILengthUpdateListener;
import tim.matura.processing.ITextReceiver;
import tim.matura.processing.impl.*;
import tim.matura.sound.SoundDecoder;
import tim.matura.utils.Logging;

public class Main extends Activity implements TextWatcher {


    private Thread recordThread;
    private SoundDecoder decoder;
    private GraphWidget soundGraph;
    private TextView textView;
    private EditText textDitLength;

    private LengthToMorse2 l2mProcessor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle unused) {
        super.onCreate(null);
        setContentView(R.layout.main);
        soundGraph = (GraphWidget) findViewById(R.id.soundGraph);
        textView = (TextView) findViewById(R.id.textOutput);
        textDitLength = (EditText) findViewById(R.id.textDitLength);
        Logging.setLogWidget((LogWidget) findViewById(R.id.log));
        Logging.d("Finished starting app");

    }

    public void stopRecording(final View unused) {
        if (recordThread != null) {
            Logging.d("Stop Recording");
            decoder.finish(recordThread);
            recordThread = null;
        }
    }

    public void startRecording(final View unused) {
        if (recordThread != null) {
            Logging.d("Recording already started.");
            return;
        }
        Logging.d("Start Recording ..");
        decoder = new SoundDecoder(512);

        l2mProcessor = new LengthToMorse2(
                new ILengthUpdateListener() {
                    @Override
                    public void lengthChanged(float val) {
                        textDitLength.setText(Integer.toString((int) val));
                    }
                },
                new MorseToTextProcessor(
                        new ITextReceiver() {
                            @Override
                            public void setText(String string) {
                                textView.append(string);
                            }
                        }
                )
        );
        decoder.setSampleReceiver(
                new SampleSmoother(
                        new SndToBinaryProcessor(
                                new BinaryToSndLengthProcessor(l2mProcessor)
                        ),
                        soundGraph
                )
        );

        recordThread = new Thread(decoder);
        recordThread.start();
    }


    public void playMorse(final View unused) {
        String message = ((EditText) findViewById(R.id.morseInput)).getText().toString();
        if (message != null && !message.isEmpty()) {
            Translator t = new Translator(message);
            MorseSequence s = t.getSequence();
            Logging.d(s.toString());
            MorseToAudio translator = new MorseToAudio(s);
            byte[] b = translator.getSound();
            //TODO: use not deprecated chanel configuration
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, MorseToAudio.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, b.length, AudioTrack.MODE_STATIC);
            track.write(b, 0, b.length);
            track.play();
            Logging.d("" + track.getPlayState());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //DUMMY
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //DUMMY
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (l2mProcessor != null) {
            l2mProcessor.setDitLength(Integer.parseInt(s.toString()));
        }
    }
}
