package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ISoundLengthReceiver;
import tim.matura.utils.Logging;
import tim.matura.utils.Utils;

/**
 * @author Tiim
 * @since 29.07.13 12:35
 */
@Deprecated
public class LengthToMorseProcessor implements ISoundLengthReceiver {

    private final IMorseReceiver[] receivers;

    private State state = State.FINDING_TICK_LENGTH;

    private float morseTickLength = -1;


    public LengthToMorseProcessor(IMorseReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {

        if (state == State.FINDING_TICK_LENGTH) {
            if (!isSound) return;
            calculateTickLength(length);
        } else {
            if (isSound) {
                if (Utils.isApproxEqual(length, morseTickLength)) {
                    for (IMorseReceiver rec : receivers) {
                        rec.setMorseChar(MorseCharacter.DIT);
                    }
                    morseTickLength = Utils.average(length, morseTickLength);
                } else if (Utils.isApproxEqual(length, morseTickLength * MorseCharacter.DAH.getLenght())) {
                    for (IMorseReceiver rec : receivers) {
                        rec.setMorseChar(MorseCharacter.DAH);
                    }
                    morseTickLength = Utils.average(length, morseTickLength * MorseCharacter.DAH.getLenght()) / MorseCharacter.DAH.getLenght();
                }
            } else {

            }
        }
    }


    public void calculateTickLength(float lenght) {
        if (morseTickLength == -1) {
            //First morse "tick" ever
            morseTickLength = lenght;
        } else if (Utils.isApproxEqual(morseTickLength, lenght)) {
            // Following ticks with aprox. the same length
            morseTickLength = Utils.average(lenght, morseTickLength);
        } else if (Utils.isApproxEqual(morseTickLength, lenght * MorseCharacter.DAH.getLenght())) {
            //Following tick which is longer than the first few
            //that means the first were "DAH"s and the current is one a "DIT"
            morseTickLength = lenght / MorseCharacter.DAH.getLenght();
            changeState(State.DECODING);
        } else if (Utils.isApproxEqual(morseTickLength, lenght / MorseCharacter.DAH.getLenght())) {
            //Following tick which is shorter than the firs few
            //that means the first were "DIT"s and the current one is a "DAH"
            morseTickLength = lenght;
            changeState(State.DECODING);
        }

        if (state == State.DECODING) {
            Logging.d("Found ticklength: DIT:" + morseTickLength + ", DAH: " + (morseTickLength * MorseCharacter.DAH.getLenght()));
        }
    }


    public void changeState(State s) {
        state = s;
        Logging.d("Changed state to " + s);
    }

    enum State {
        DECODING,
        FINDING_TICK_LENGTH
    }
}
