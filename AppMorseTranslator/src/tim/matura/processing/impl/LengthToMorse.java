package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.processing.ILengthUpdateListener;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ISoundLengthReceiver;
import tim.matura.utils.Logging;
import tim.matura.utils.Utils;

/**
 * @author Tim
 * @since 21.10.13
 */
public class LengthToMorse implements ISoundLengthReceiver {

    private final ILengthUpdateListener lengthUpdateListener;
    private final IMorseReceiver[] receivers;

    private float morseTickLength = 0.240f; //s     == 5 WPM == 25 BPM

    private float dit = MorseCharacter.DIT.getLenght() * morseTickLength;
    private float dah = MorseCharacter.DAH.getLenght() * morseTickLength;
    private float pLong = MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength;
    private float pShort = MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength;
    private float pMini = MorseCharacter.PAUSE_BETWEEN_SYMBOLS.getLenght() * morseTickLength;

    public LengthToMorse(ILengthUpdateListener l, IMorseReceiver... receivers) {
        lengthUpdateListener = l;

        this.receivers = receivers;
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {

        // Some noise..
        if (length < 0.05) return;

        //Long or short
        if (isSound && length <= dit * 2) {
            send(MorseCharacter.DIT);
            update(MorseCharacter.DIT, length);
        } else if (isSound && length > dit * 2) {
            send(MorseCharacter.DAH);
            update(MorseCharacter.DAH, length);

            //Different pauses
        } else if (!isSound && Utils.isApproxEqual(pLong, length)) {
            send(MorseCharacter.PAUSE_LONG);
            update(MorseCharacter.PAUSE_LONG, length);
        } else if (!isSound && Utils.isApproxEqual(pShort, length)) {
            send(MorseCharacter.PAUSE_SHORT);
            update(MorseCharacter.PAUSE_SHORT, length);
        } else if (!isSound && Utils.isApproxEqual(pMini, length)) {
            send(MorseCharacter.PAUSE_BETWEEN_SYMBOLS);
            update(MorseCharacter.PAUSE_BETWEEN_SYMBOLS, length);
        } else {
            send(MorseCharacter.UNKNOWN);
        }

    }

    private void update(MorseCharacter character, float length) {
        length *= character.getLenght();
        float dummy = morseTickLength;
        morseTickLength = Utils.average(length, morseTickLength, morseTickLength, morseTickLength, morseTickLength);
        if (morseTickLength != dummy) {
//            lengthUpdateListener.lengthChanged(morseTickLength * 2000f);
//            setDitLength(morseTickLength * 2000f);
        }
    }

    private void send(MorseCharacter c) {
        if (c != MorseCharacter.PAUSE_BETWEEN_SYMBOLS) {
            for (IMorseReceiver r : receivers) {
                r.setMorseChar(c);
            }
            Logging.d(c.toString());
        }
    }

    public void setDitLength(float ditLength) {
        this.morseTickLength = ditLength / 2000f;
        dit = MorseCharacter.DIT.getLenght() * morseTickLength;
        dah = MorseCharacter.DAH.getLenght() * morseTickLength;
        pLong = MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength;
        pShort = MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength;
        pMini = MorseCharacter.PAUSE_BETWEEN_SYMBOLS.getLenght() * morseTickLength;

        Logging.d("Expected Values:");
        Logging.d(dit + ": DIT");
        Logging.d(dah + ": DAH");
        Logging.d(pShort + ": Pause1");
        Logging.d(pLong + ": Pause2");

    }
}
