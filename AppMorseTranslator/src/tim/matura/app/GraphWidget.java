package tim.matura.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.Logging;
import tim.matura.utils.Utils;

/**
 * @author Tiim
 * @since 24.08.13 16:12
 */
public class GraphWidget extends View implements ISoundReceiver, IBinaryReceiver {

    Paint paint = new Paint();

    private int[] chunks = new int[512];

    private int pointer = 0;
    private int length = 0;

    public GraphWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        Logging.d("Created!");
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
            canvas.drawText("" + Logging.INSTANCE_ID, 20, 40, paint);
            paint.setStrokeWidth(dx);

            for (int i = 0; i < length; i++) {
                int j = i + pointer;
                while (j >= chunks.length) {
                    j -= chunks.length;
                }
                canvas.drawLine(i * dx, canvas.getHeight(), i * dx, canvas.getHeight() - (canvas.getHeight() / heigth) * chunks[j], paint);
            }
            paint.setColor(Color.RED);
            canvas.drawLine(0,canvas.getHeight() - (canvas.getHeight() / heigth) * 2000,canvas.getWidth(),canvas.getHeight() - (canvas.getHeight() / heigth) * 2000,paint);
    }

    @Override
    public void receive(int soundSample) {
        if (pointer >= chunks.length) {
            pointer = 0;
        }
        if (length < chunks.length) {
            length++;
        }
        chunks[pointer++] = soundSample;
        postInvalidate();
    }

    @Override
    public void setSound(boolean sound) {
        receive(sound ? 1 : 0);
    }
}
