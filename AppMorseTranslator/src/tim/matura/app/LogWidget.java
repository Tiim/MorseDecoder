package tim.matura.app;

import android.content.Context;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Tim
 * @since 10.10.13
 */
public class LogWidget extends TextView {


    public LogWidget(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
//        setInputType(getInputType()|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setScrollBarStyle(SCROLLBAR_POSITION_RIGHT);
        setMovementMethod(new ScrollingMovementMethod());
//        setMaxLines(100);
    }

    public void append(final String s) {
        post(new Runnable() {
            @Override
            public void run() {
                LogWidget.super.append(" \n");
                LogWidget.super.append(s);

                final int scrollAmount = getLayout().getLineTop(getLineCount()) - getHeight();
                if (scrollAmount > 0) {
                    scrollTo(0, scrollAmount);
                } else {
                    scrollTo(0, 0);
                }
            }
        });
    }
}
