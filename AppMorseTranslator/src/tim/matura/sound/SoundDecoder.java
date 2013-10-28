package tim.matura.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Looper;
import tim.matura.processing.ISampleReceiver;
import tim.matura.utils.Logging;

/**
 * @author Tiim
 * @since 01.06.13 17:37
 */
public class SoundDecoder implements Runnable {

    private final int[] FORMATS = {8000, 11025, 22050, 44100};
    private final int chunkLength;

    private ISampleReceiver[] receivers;

    private boolean finished = false;

    public SoundDecoder(int chunkLength) {
        this.chunkLength = chunkLength;
    }

    public void setSampleReceiver(ISampleReceiver... receivers) {
        this.receivers = receivers;
    }

    @Override
    public void run() {
        Looper.prepare();
        int sleepTime = 10;
        short[] b = new short[chunkLength];
        AudioRecord record = findAudioRecord(FORMATS);
        record.startRecording();
        for (ISampleReceiver rec : receivers) {
            rec.setSamplePerSecond(record.getSampleRate());
        }
        while (!finished) {

            int result = record.read(b, 0, chunkLength);
            if (result == AudioRecord.ERROR_INVALID_OPERATION) {
                Logging.e("Invalid operation error");
                break;
            } else if (result == AudioRecord.ERROR_BAD_VALUE) {
                Logging.e("Bad value error");
                break;
            } else if (result == AudioRecord.ERROR) {
                Logging.e("Unknown error");
                break;
            }

            for (int i = 0; i < result; i++) {
                for (ISampleReceiver rec : receivers) {
                    rec.setSample(b[i]);
                }
            }

            if (result < chunkLength / 2) {
                sleepTime++;
            } else if (result == chunkLength) {
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

        Logging.d("Finished recording");
    }

    public AudioRecord findAudioRecord(int[] sampleRates) {
        for (int rate : sampleRates) {
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT}) {
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO}) {
                    try {
                        Logging.d("Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            Logging.d("Valid for this device! Trying it out.. " + bufferSize);
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                Logging.d("Found proper setting");
                                return recorder;
                            } else {
                                Logging.d("Invalid, keep trying");
                                recorder.release();
                            }
                        }
                    } catch (Exception e) {
                        Logging.e(rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        Logging.w("No valid settings found for this device!");
        return null;
    }

    public void finish(Thread t) {
        finished = true;
        if (t != null) {
            t.interrupt();
        }
    }
}

