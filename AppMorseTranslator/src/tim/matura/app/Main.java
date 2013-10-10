package tim.matura.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import tim.matura.app.AppMorseTranslator.R;
import tim.matura.processing.ITextReceiver;
import tim.matura.processing.impl.*;
import tim.matura.sound.SoundDecoder;
import tim.matura.utils.Logging;

public class Main extends Activity {


    private Thread recordThread;
    private SoundDecoder decoder;
    private GraphWidget soundGraph;
    private GraphWidget booleanGraph;
    private TextView textView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.main);
        soundGraph = (GraphWidget) findViewById(R.id.soundGraph);
        booleanGraph = (GraphWidget) findViewById(R.id.boolGraph);
        textView = (TextView) findViewById(R.id.textOutput);


        TextView instance = (TextView) findViewById(R.id.instanceNr);
        Logging.setLogWidget((LogWidget) findViewById(R.id.log));
        instance.setText(Integer.toString(Logging.INSTANCE_ID));
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
                new SampleToChunkProcessor(
                        new SndToBinaryProcessor(
                                new BinaryToSndLengthProcessor(
                                        new LengthToMorseProcessor(
                                                new MorseToTextProcessor(
                                                        new ITextReceiver() {
                                                            @Override
                                                            public void setText(String string) {
                                                                textView.append(string);
                                                            }
                                                        }
                                                )
                                        )
                                ),
                                booleanGraph

                        ),
                        soundGraph
                )
        );

        recordThread = new Thread(decoder);
        recordThread.start();
    }


}
