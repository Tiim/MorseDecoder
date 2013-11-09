package tim.matura.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.Utils;

/**
 * @author Tiim
 * @since 24.08.13 16:12
 */
public class GraphWidget extends View implements ISoundReceiver {

    Paint paint = new Paint();

    private int samplePerSecond = 100;
    private int[] chunks = new int[512];
    private int[] chunkMaker = new int[samplePerSecond];
    private int chunkPointer = 0;
    private int pointer = 0;
    private int length = 0;

    public GraphWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float dx = (float) canvas.getWidth() / (float) chunks.length;
        float heigth = Utils.max(chunks);


        canvas.drawColor(Color.LTGRAY);
        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(dx);

        for (int i = 0; i < length; i++) {
            int j = i + pointer;
            while (j >= chunks.length) {
                j -= chunks.length;
            }
            canvas.drawLine(i * dx, canvas.getHeight(), i * dx, canvas.getHeight() - (canvas.getHeight() / heigth) * chunks[j], paint);
        }
        paint.setColor(Color.RED);
        canvas.drawLine(0, canvas.getHeight() - (canvas.getHeight() / heigth) * 2000, canvas.getWidth(), canvas.getHeight() - (canvas.getHeight() / heigth) * 2000, paint);
    }

    @Override
    public void receive(float soundSample) {
        if (chunkPointer == samplePerSecond -1) {
            chunkPointer = 0;
            if (pointer >= chunks.length) {
                pointer = 0;
            }
            if (length < chunks.length) {
                length++;
            }
            chunks[pointer++] = Utils.sum(chunkMaker);
            postInvalidate();
        } else {
            chunkMaker[chunkPointer++] = (int)soundSample;
        }
    }

    @Override
    public void setSamplePerSecond(int x) {
        samplePerSecond = x;
        chunkMaker = new int[samplePerSecond];
    }
}
