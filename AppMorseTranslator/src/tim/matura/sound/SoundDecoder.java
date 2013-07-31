package tim.matura.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import tim.matura.app.AppMorseTranslator.util.Ref;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tiim
 * @since 01.06.13 17:37
 */
public class SoundDecoder implements Runnable {

    private final int[] FORMATS = {8000, 11025, 22050, 44100};
    private final int chunkLenght;

    private ISoundReceiver receiver;

    private boolean finished = false;

    public SoundDecoder(int chunkLenght) {
        this.chunkLenght = chunkLenght;
    }

    public void setSoundReceiver(ISoundReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        int sleepTime = 10;
        short[] b = new short[chunkLenght];
        AudioRecord record = findAudioRecord(FORMATS);
        record.startRecording();
        int sum = 0;
        int counter = 0;
        while (!finished) {

            int result = record.read(b, 0, chunkLenght);
            if (result == AudioRecord.ERROR_INVALID_OPERATION) {
                Log.e(Ref.NAME, "Invalid operation error");
                break;
            } else if (result == AudioRecord.ERROR_BAD_VALUE) {
                Log.e(Ref.NAME, "Bad value error");
                break;
            } else if (result == AudioRecord.ERROR) {
                Log.e(Ref.NAME, "Unknown error");
                break;
            }

            for (int i = 0; i < result; i++) {
                sum += b[i];
                counter++;
                if (counter == chunkLenght) {
                    receiver.receive(sum);
                    counter = 0;
                    sum = 0;
                }
            }

            if (result < chunkLenght / 2) {
                sleepTime++;
            } else if (result == chunkLenght) {
                sleepTime--;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }
        }


        record.stop();
        record.release();
    }

    public AudioRecord findAudioRecord(int[] sampleRates) {
        for (int rate : sampleRates) {
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT}) {
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO}) {
                    try {
                        Log.d(Ref.NAME, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            Log.d(Ref.NAME, "Valid for this device! Trying it out.. " + bufferSize);
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                Log.d(Ref.NAME, "Found proper setting");
                                return recorder;
                            } else {
                                Log.d(Ref.NAME, "Invalid, keep trying");
                                recorder.release();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(Ref.NAME, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        Log.w(Ref.NAME, "No valid settings found for this device!");
        return null;
    }

    public void finish() {
        finished = true;
    }
}

