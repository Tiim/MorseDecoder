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
    //    private GraphWidget soundGraph;
    private TextView textView;
    private EditText textDitLength;

    private LengthToMorse l2mProcessor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle unused) {
        super.onCreate(null);
        setContentView(R.layout.main);
//        soundGraph = (GraphWidget) findViewById(R.id.soundGraph);
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

    //TODO: Pretty this ugly section up :(
    public void startRecording(final View unused) {
        if (recordThread != null) {
            Logging.d("Recording already started.");
            return;
        }
        Logging.d("Start Recording ..");
        decoder = new SoundDecoder(512);
        l2mProcessor = new LengthToMorse(
                new ILengthUpdateListener() {
                    @Override
                    public void lengthChanged(final float val) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textDitLength.setText(Integer.toString((int) val));
                            }
                        });
                    }
                },
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
//                new FileLogger("idedroid/samples.txt"),
                new BufferVolumeProcessor(
//                        new FileLogger("idedroid/average.txt")
                        new SndToBinaryProcessor(
                                new BinaryToSndLengthProcessor(l2mProcessor)
                        )
//                        soundGraph
                )
        );
        setDitLength(null);
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
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, MorseToAudio.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, b.length, AudioTrack.MODE_STATIC);
            track.write(b, 0, b.length);
            track.play();
            Logging.d("" + track.getPlayState());
        }
    }

    //TODO: Make it work event if recording thread has not been started yet
    public void setDitLength(View unused) {
        if (l2mProcessor != null) {
            l2mProcessor.setDitLength(Integer.parseInt(textDitLength.getText().toString()));
        }
    }

}
