package tim.matura.morse;

import tim.matura.morse.util.MorseMap;

import static tim.matura.morse.MorseCharacter.PAUSE_SHORT;

/**
 * @author Tiim
 * @since 20.05.13 14:08
 */
public class Translator implements SequenceChangeListener {

    public static final MorseMap MORSE_MAP = MorseMap.INSTANCE;

    private MorseSequence sequence;

    private String string;

    public Translator(MorseSequence sequence) {
        if (sequence == null) {
            throw new NullPointerException("Sequence can not be null");
        }
        sequence.setChangeListener(this);
        this.sequence = sequence;
    }

    public Translator(String string) {
        if (string == null) {
            throw new NullPointerException("String can not be null");
        }
        this.string = string.toLowerCase();
    }

    public MorseSequence getSequence() {
        if (sequence == null) {
            translateToMorse();
        }
        return sequence;
    }

    public String getString() {
        if (string == null) {
            translateToString();
        }
        return string;
    }

    private void translateToString() {
        MorseSequence[] characters = sequence.splitCharacters();
        StringBuilder builder = new StringBuilder();
        for (MorseSequence c : characters) {
            if (MORSE_MAP.containsMorse(c)) {
                builder.append(MORSE_MAP.getChar(c));
            } else {
                System.out.println(c + "is not a valid morse character");
            }
        }
        string = builder.toString();
    }


    private void translateToMorse() {
        MorseSequence s = new MorseSequence();
        for (char c : string.toCharArray()) {
            if (MORSE_MAP.containsChar(c)) {
                s.append(MORSE_MAP.getMorse(c));
                s.append(PAUSE_SHORT);
            } else {
                System.out.println("'" + c + "' is not a valid char");
            }
        }
        sequence = s;
    }

    @Override
    public void sequenceChanged() {
        string = null;
    }
}
