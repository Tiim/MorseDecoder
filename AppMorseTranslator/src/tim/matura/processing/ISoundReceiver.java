package tim.matura.processing;

/**
 * @author Tiim
 * @since 29.07.13 12:05
 */
public interface ISoundReceiver {

    public void receive(float soundSample);

    public void setSamplePerSecond(int x);
}
