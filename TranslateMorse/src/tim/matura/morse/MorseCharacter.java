package tim.matura.morse;

/**
 * @author Tiim
 * @since 20.05.13 12:45
 */
public enum MorseCharacter {
    DIT(0, 1),
    DAH(1, 3),
    PAUSE_SHORT(2, 3),
    PAUSE_LONG(3, 7);

    private int lenght;
    private int id;

    private MorseCharacter(int id, int lenght) {
        this.lenght = lenght;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getLenght() {
        return lenght;
    }
}