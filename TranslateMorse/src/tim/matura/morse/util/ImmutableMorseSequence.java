package tim.matura.morse.util;

import tim.matura.morse.MorseCharacter;
import tim.matura.morse.MorseSequence;

import java.util.List;

/**
 * @author Tiim
 * @since 20.05.13 19:53
 */
public class ImmutableMorseSequence extends MorseSequence {
    public ImmutableMorseSequence() {
        super();
    }

    public ImmutableMorseSequence(List<MorseCharacter> list) {
        super(list);
    }

    public ImmutableMorseSequence(MorseCharacter... list) {
        super(list);
    }

    @Override
    public void append(MorseCharacter... c) {
        throw new UnsupportedOperationException("This instance is immutable");
    }

    @Override
    public void append(MorseSequence c) {
        throw new UnsupportedOperationException("This instance is immutable");
    }
}
