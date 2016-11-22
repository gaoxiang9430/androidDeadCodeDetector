package com.chessclock.android;

import android.net.Uri;
import android.widget.TextView;
import android.widget.Button;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.os.Handler;

class ChessClock$4 implements java.lang.Runnable
{
    final ChessClock this$0;

    ChessClock$4(ChessClock  r1)
    {


        this$0 = r1;
        this.<init>();
    }

    public void run()
    {

        String r4;
        long l3;
        int i4, i5, i16;
        Uri r8;
        TextView r9;
        ChessClock r19, r23, r77, r92, r94;
        Button r30, r33, r36;
        r9 = (TextView) this$0.findViewById(2131165190);
        r4 = "";

        label_0:
        {
            if (ChessClock.access$3(this$0).equals(ChessClock.access$4()) != false)
            {
                if (ChessClock.access$5(this$0) == false)
                {
                    ChessClock.access$6(this$0, true);
                    r19 = this$0;
                    ChessClock.access$9(r19, ChessClock.access$7(r19) + (long) (ChessClock.access$8(this$0) * 1000));
                    break label_0;
                }
            }

            if (ChessClock.access$3(this$0).equals(ChessClock.access$10()) != false)
            {
                if (ChessClock.access$5(this$0) == false)
                {
                    ChessClock.access$6(this$0, true);
                    ChessClock.access$11(this$0, ChessClock.access$8(this$0) * 1000);
                    r77 = this$0;
                    ChessClock.access$9(r77, ChessClock.access$7(r77) + 100L);
                    r4 = (new StringBuilder("+")).append(ChessClock.access$12(this$0) / 1000).toString();
                    break label_0;
                }
            }

            if (ChessClock.access$3(this$0).equals(ChessClock.access$10()) != false)
            {
                if (ChessClock.access$5(this$0) != false)
                {
                    if (ChessClock.access$12(this$0) > 0)
                    {
                        r92 = this$0;
                        ChessClock.access$11(r92, ChessClock.access$12(r92) + -100);
                        r94 = this$0;
                        ChessClock.access$9(r94, ChessClock.access$7(r94) + 100L);
                    }

                    if (ChessClock.access$12(this$0) > 0)
                    {
                        r4 = (new StringBuilder("+")).append(ChessClock.access$12(this$0) / 1000 + 1).toString();
                    }
                }
            }
        } //end label_0:


        r23 = this$0;
        ChessClock.access$9(r23, ChessClock.access$7(r23) - 100L);
        l3 = ChessClock.access$7(this$0);
        i4 = (int) (l3 / 1000L);
        i5 = i4 / 60;
        i16 = i4 % 60 + 1;

        if (i16 != 60)
        {
            if (l3 - 0L != 0)
            {
                if (l3 - (long) (ChessClock.access$13(this$0) * 60000) == 0)
                {
                    i16 = i16 + -1;
                }
            }
            else
            {
                i16 = 0;
            }
        }
        else
        {
            i5 = i5 + 1;
            i16 = 0;
        }

        if (l3 - 0L != 0)
        {
            if (l3 - 60000L >= 0)
            {
                r9.setTextColor(-3355444);
            }
            else
            {
                r9.setTextColor(-256);
            }

            if (i16 >= 10)
            {
                r9.setText((new StringBuilder()).append(i5).append(":").append(i16).append(r4).toString());
            }
            else
            {
                r9.setText((new StringBuilder()).append(i5).append(":0").append(i16).append(r4).toString());
            }

            ChessClock.access$18(this$0).postDelayed(this, 100L);
        }
        else
        {
            ChessClock.access$14(this$0, true);
            r30 = (Button) this$0.findViewById(2131165186);
            r33 = (Button) this$0.findViewById(2131165189);
            r36 = (Button) this$0.findViewById(2131165188);
            r9.setTextColor(-65536);
            r33.setBackgroundColor(-65536);
            r30.setClickable(false);
            r33.setClickable(false);
            r36.setClickable(false);
            r8 = Uri.parse(ChessClock.access$15(this$0));
            ChessClock.access$16(this$0, RingtoneManager.getRingtone(this$0.getBaseContext(), r8));

            if (ChessClock.access$17(this$0) != null)
            {
                ChessClock.access$17(this$0).play();
            }

            ChessClock.access$18(this$0).removeCallbacks(ChessClock.access$19(this$0));
            ChessClock.access$18(this$0).postDelayed(ChessClock.access$20(this$0), 500L);
        }
    }
}
