package tim.matura.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tiim
 * @since 24.08.13 16:12
 */
public class GraphWidget extends View implements ISoundReceiver {

    Paint paint = new Paint();

    private int[] chunks = new int[512];

    private int newest = 0;
    private int drawStart = 0;
    private int drawEnd = 0;

    public GraphWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GraphWidget(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int end = drawEnd < chunks.length ? drawEnd : drawStart - 1;
        float dx = (float) getWidth() / (float) chunks.length;


        paint.setColor(0x000000);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(dx);

        for (int i = drawStart, j = 0; i != end; i++, j++) {
            if (i == chunks.length) {
                i = 0;
            }
            canvas.drawLine(j * dx, getHeight(), j * dx, getHeight() - chunks[i], paint);
        }
    }

    @Override
    public void receive(int soundSample) {
        chunks[newest++] = soundSample;
        if (drawEnd < chunks.length) {
            drawEnd++;
        } else {
            drawStart++;
            if (drawStart == chunks.length) {
                drawStart = 0;
            }
        }

        if (newest == chunks.length) {
            newest = 0;
        }
    }
}
