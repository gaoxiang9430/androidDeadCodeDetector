package com.chessclock.android;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;

public class CTextView extends TextView
{

    public CTextView(Context  r1)
    {
        super(r1);


        this.<init>(r1);
    }

    public CTextView(Context  r1, AttributeSet  r2)
    {
        super(r1, r2);


        this.<init>(r1, r2);
    }

    public void onDraw(Canvas  r1)
    {

        float f1;
        r1.save();
        f1 = (float) this.getHeight() / 2.0F;
        r1.rotate(180.0F, (float) this.getWidth() / 2.0F, f1);
        this.onDraw(r1);
        r1.restore();
    }
}
