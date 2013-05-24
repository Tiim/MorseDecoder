package tim.matura.morse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tiim
 * @since 20.05.13 12:40
 */
public class MorseSequence {

    private SequenceChangeListener changeListener;
    private List<MorseCharacter> characters;

    public MorseSequence() {
        characters = new ArrayList<MorseCharacter>();
    }

    public MorseSequence(List<MorseCharacter> list) {
        characters = new ArrayList<MorseCharacter>(list);
    }

    public MorseSequence(MorseCharacter... list) {
        characters = new ArrayList<MorseCharacter>(Arrays.asList(list));
    }

    public MorseCharacter getCharAt(int i) {
        return characters.get(i);
    }

    public int getLenght() {
        return characters.size();
    }

    public void append(MorseCharacter... c) {
        for (MorseCharacter ch : c) {
            characters.add(ch);
        }
        if (changeListener != null) {
            changeListener.sequenceChanged();
        }
    }

    public void append(MorseSequence c) {
        for (MorseCharacter ch : c.characters) {
            characters.add(ch);
        }
        if (changeListener != null) {
            changeListener.sequenceChanged();
        }
    }

    public MorseSequence[] splitCharacters() {
        List<MorseSequence> sequences = new LinkedList<MorseSequence>();
        MorseSequence character = new MorseSequence();
        for (MorseCharacter c : characters) {
            if (c != MorseCharacter.PAUSE_SHORT) {
                character.append(c);
            } else {
                sequences.add(character);
                character = new MorseSequence();
            }
        }
        if (character.getLenght() > 0) {
            sequences.add(character);
        }
        return sequences.toArray(new MorseSequence[0]);
    }

    void setChangeListener(SequenceChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Morsecode [");
        for (MorseCharacter c : characters) {
            switch (c) {
                case DIT:
                    b.append('.');
                    break;
                case DAH:
                    b.append('-');
                    break;
                case PAUSE_SHORT:
                    b.append(' ');
                    break;
                case PAUSE_LONG:
                    b.append(" / ");
                    break;
            }
        }
        b.append(']');
        return b.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        int x = 0;
        for (int i = 0; i < (float) getLenght() / 15f; i++) {
            int h = 0;
            for (int j = 0; j < getLenght() % 15; j++) {
                h <<= 2;
                h |= characters.get(x++).getId();
            }
            hash ^= h;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MorseSequence)) {
            return false;
        }
        MorseSequence m = (MorseSequence) obj;
        if (m.getLenght() != getLenght()) {
            return false;
        }
        for (int i = 0; i < getLenght(); i++) {
            if (characters.get(i) != m.characters.get(i)) {
                return false;
            }
        }
        return true;
    }
}
