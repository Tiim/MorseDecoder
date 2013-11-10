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

//    private final ILengthUpdateListener lengthUpdateListener;
    private final IMorseReceiver[] receivers;

    private float morseTickLength = 0;//0.240f; //s     == 5 WPM == 25 BPM

    private float dit = MorseCharacter.DIT.getLenght() * morseTickLength;
    private float dah = MorseCharacter.DAH.getLenght() * morseTickLength;
    private float pLong = MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength;
    private float pShort = MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength;
    private float pMini = MorseCharacter.PAUSE_BETWEEN_SYMBOLS.getLenght() * morseTickLength;

    public LengthToMorse(/*ILengthUpdateListener l,*/ IMorseReceiver... receivers) {
//        lengthUpdateListener = l;
        setDitLength(240f);
        this.receivers = receivers;
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {

        // Some noise..
        if (length < 0.05) return;

        //Long or short
        if (isSound) {
            if (length <= dit * 2) {
                send(MorseCharacter.DIT);
            } else {
                send(MorseCharacter.DAH);
            }
        } else {


            if (length < (pShort + pMini) / 2) {
                send(MorseCharacter.PAUSE_BETWEEN_SYMBOLS);
            } else {
                send(MorseCharacter.PAUSE_SHORT);
            }
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
