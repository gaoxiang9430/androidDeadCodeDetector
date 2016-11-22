package com.chessclock.android;

import android.widget.TextView;
import android.os.Handler;

class ChessClock$6 implements java.lang.Runnable
{
    final ChessClock this$0;

    ChessClock$6(ChessClock  r1)
    {


        this$0 = r1;
        this.<init>();
    }

    public void run()
    {

        TextView r3;
        r3 = (TextView) this$0.findViewById(2131165190);

        if (ChessClock.access$24(this$0) != false)
        {
            ChessClock.access$25(this$0, false);
            r3.setText("0:00");
        }
        else
        {
            ChessClock.access$25(this$0, true);
            r3.setText("");
        }

        ChessClock.access$18(this$0).postDelayed(this, 500L);
    }
}
