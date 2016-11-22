package com.chessclock.android;

import android.app.Activity;
import android.os.Handler;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;
import android.preference.PreferenceManager;
import android.net.Uri;
import android.provider.Settings$System;
import android.widget.Button;
import android.widget.TextView;
import android.os.PowerManager;
import android.content.Context;
import android.util.Log;
import android.app.AlertDialog$Builder;
import android.app.Dialog;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.Window;
import android.view.Menu;
import android.view.MenuItem;
import android.os.PowerManager$WakeLock;
import android.content.Intent;
import android.view.View$OnClickListener;

public class ChessClock extends Activity
{
    private static final int ABOUT = 2;
    private static String BRONSTEIN;
    private static String FISCHER;
    private static String NO_DELAY;
    private static final int RESET = 1;
    private static final int SETTINGS = 0;
    public static final String TAG = "INFO";
    public static final String V_MAJOR = "1";
    public static final String V_MINI = "0";
    public static final String V_MINOR = "2";
    private Runnable Blink;
    private Runnable Blink2;
    private DialogFactory DF;
    public View$OnClickListener P1ClickHandler;
    public View$OnClickListener P2ClickHandler;
    public View$OnClickListener PauseListener;
    private String alertTone;
    private int b_delay;
    private boolean blink;
    private String delay;
    private int delay_time;
    private boolean delayed;
    private boolean haptic;
    private boolean hapticChange;
    private Runnable mUpdateTimeTask;
    private Runnable mUpdateTimeTask2;
    private Handler myHandler;
    private int onTheClock;
    private PowerManager pm;
    private boolean prefmenu;
    private Ringtone ringtone;
    private int savedOTC;
    private long t_P1;
    private long t_P2;
    private int time;
    private boolean timeup;
    private PowerManager$WakeLock wl;

    static
    {


        NO_DELAY = "None";
        FISCHER = "Fischer";
        BRONSTEIN = "Bronstein";
    }

    public ChessClock()
    {
        myHandler = new Handler();
        DF = new DialogFactory();
        delay = NO_DELAY;
        ringtone = null;
        onTheClock = (int) 0;
        savedOTC = (int) 0;
        haptic = false;
        blink = false;
        timeup = false;
        prefmenu = false;
        delayed = false;
        hapticChange = false;
        P1ClickHandler = new ChessClock$1(this);
        P2ClickHandler = new ChessClock$2(this);
        PauseListener = new ChessClock$3(this);
        mUpdateTimeTask = new ChessClock$4(this);
        mUpdateTimeTask2 = new ChessClock$5(this);
        Blink = new ChessClock$6(this);
        Blink2 = new ChessClock$7(this);
    }

    public void CheckForNewPrefs()
    {

        SharedPreferences r1;
        String r4, r16, r17, $r18, r20, r21, $r22;
        SharedPreferences$Editor r5, r26, r32;
        int i0, i1, $i2, $i4;
        r1 = PreferenceManager.getDefaultSharedPreferences(this);
        alertTone = r1.getString("prefAlertSound", Settings$System.DEFAULT_RINGTONE_URI.toString());
        r4 = r1.getString("prefDelay", "None");

        if (r4.equals("") != false)
        {
            r4 = "None";
            r5 = r1.edit();
            r5.putString("prefDelay", "None");
            r5.commit();
        }

        if (r4 != delay)
        {
            this.SetUpGame();
        }

        label_4:
        {
            label_1:
            {
                label_0:
                {
                    try
                    {
                        r16 = "prefTime";
                    }
                    catch (Exception $r25)
                    {
                        break label_0;
                    }

                    r17 = "10";

                    try
                    {
                        $r18 = r1.getString(r16, r17);
                    }
                    catch (Exception $r25)
                    {
                        break label_0;
                    }

                    try
                    {
                        $i2 = Integer.parseInt($r18);
                        break label_1;
                    }
                    catch (Exception $r25)
                    {
                    }
                } //end label_0:


                i0 = 10;
                r26 = r1.edit();
                r26.putString("prefTime", "10");
                r26.commit();
                break label_4;
            } //end label_1:


            i0 = $i2;
        } //end label_4:


        if (i0 != time)
        {
            this.SetUpGame();
        }

        label_5:
        {
            label_3:
            {
                label_2:
                {
                    try
                    {
                        r20 = "prefDelayTime";
                    }
                    catch (Exception $r30)
                    {
                        break label_2;
                    }

                    r21 = "0";

                    try
                    {
                        $r22 = r1.getString(r20, r21);
                    }
                    catch (Exception $r30)
                    {
                        break label_2;
                    }

                    try
                    {
                        $i4 = Integer.parseInt($r22);
                        break label_3;
                    }
                    catch (Exception $r30)
                    {
                    }
                } //end label_2:


                i1 = 0;
                r32 = r1.edit();
                r32.putString("prefDelayTime", "0");
                r32.commit();
                break label_5;
            } //end label_3:


            i1 = $i4;
        } //end label_5:


        if (i1 != delay_time)
        {
            this.SetUpGame();
        }

        if (r1.getBoolean("prefHaptic", false) != haptic)
        {
            hapticChange = true;
            this.SetUpGame();
        }
    }

    private String FormatTime(long  l0)
    {

        int i2, i3, i4;
        String r2;
        i2 = (int) l0 / 1000;
        i3 = i2 / 60;
        i4 = i2 % 60;

        if (i4 >= 10)
        {
            r2 = (new StringBuilder()).append(i3).append(":").append(i4).toString();
        }
        else
        {
            r2 = (new StringBuilder()).append(i3).append(":0").append(i4).toString();
        }

        return r2;
    }

    private void P1Click()
    {

        int i2, i3, i14;
        Button r7, r8;
        TextView r13;
        r7 = (Button) this.findViewById(2131165186);
        r8 = (Button) this.findViewById(2131165189);
        r7.performHapticFeedback((int) 1);
        ((PowerManager) this.getBaseContext().getSystemService("power")).userActivity(1L, true);

        if (onTheClock != (int) 1)
        {
            onTheClock = (int) 1;

            if (savedOTC != 0)
            {
                savedOTC = (int) 0;
            }
            else
            {
                delayed = false;
            }

            r8.setBackgroundColor(-16711936);
            r7.setBackgroundColor(-3355444);

            if (delay.equals(BRONSTEIN) != false)
            {
                r13 = (TextView) this.findViewById(2131165187);
                i2 = (int) (t_P2 / 1000L);
                i3 = i2 / 60;
                i14 = i2 % 60 + 1;

                if (i14 != 60)
                {
                    if (t_P2 - 0L != 0)
                    {
                        if (t_P2 - (long) (time * 60000) == 0)
                        {
                            i14 = i14 + -1;
                        }
                    }
                    else
                    {
                        i14 = 0;
                    }
                }
                else
                {
                    i3 = i3 + 1;
                    i14 = 0;
                }

                if (i14 >= 10)
                {
                    r13.setText((new StringBuilder()).append(i3).append(":").append(i14).toString());
                }
                else
                {
                    r13.setText((new StringBuilder()).append(i3).append(":0").append(i14).toString());
                }
            }

            ((Button) this.findViewById(2131165188)).setBackgroundColor(-3355444);
            myHandler.removeCallbacks(mUpdateTimeTask);
            myHandler.removeCallbacks(mUpdateTimeTask2);
            myHandler.postDelayed(mUpdateTimeTask, 100L);
        }
    }

    private void P2Click()
    {

        int i3, i4, i15;
        Button r7, r8;
        TextView r13;
        r7 = (Button) this.findViewById(2131165186);
        r8 = (Button) this.findViewById(2131165189);
        r8.performHapticFeedback((int) 1);
        ((PowerManager) this.getBaseContext().getSystemService("power")).userActivity(1L, true);

        if (onTheClock != 2)
        {
            onTheClock = 2;

            if (savedOTC != 0)
            {
                savedOTC = (int) 0;
            }
            else
            {
                delayed = false;
            }

            r7.setBackgroundColor(-16711936);
            r8.setBackgroundColor(-3355444);

            if (delay.equals(BRONSTEIN) != false)
            {
                r13 = (TextView) this.findViewById(2131165190);
                i3 = (int) (t_P1 / 1000L);
                i4 = i3 / 60;
                i15 = i3 % 60 + 1;

                if (i15 != 60)
                {
                    if (t_P1 - 0L != 0)
                    {
                        if (t_P1 - (long) (time * 60000) == 0)
                        {
                            i15 = i15 + -1;
                        }
                    }
                    else
                    {
                        i15 = 0;
                    }
                }
                else
                {
                    i4 = i4 + 1;
                    i15 = 0;
                }

                if (i15 >= 10)
                {
                    r13.setText((new StringBuilder()).append(i4).append(":").append(i15).toString());
                }
                else
                {
                    r13.setText((new StringBuilder()).append(i4).append(":0").append(i15).toString());
                }
            }

            ((Button) this.findViewById(2131165188)).setBackgroundColor(-3355444);
            myHandler.removeCallbacks(mUpdateTimeTask);
            myHandler.removeCallbacks(mUpdateTimeTask2);
            myHandler.postDelayed(mUpdateTimeTask2, 100L);
        }
    }

    private void PauseGame()
    {

        Button r4, r5, r6;
        r4 = (Button) this.findViewById(2131165186);
        r5 = (Button) this.findViewById(2131165189);
        r6 = (Button) this.findViewById(2131165188);

        if (onTheClock != 0)
        {
            if (timeup == false)
            {
                savedOTC = onTheClock;
                onTheClock = 0;
                r4.setBackgroundColor(-3355444);
                r5.setBackgroundColor(-3355444);
                r6.setBackgroundColor(-16776961);
                myHandler.removeCallbacks(mUpdateTimeTask);
                myHandler.removeCallbacks(mUpdateTimeTask2);
            }
        }
    }

    private void PauseToggle()
    {

        Button r4, r5, r6;
        r4 = (Button) this.findViewById(2131165186);
        r5 = (Button) this.findViewById(2131165189);
        r6 = (Button) this.findViewById(2131165188);
        r6.performHapticFeedback(1);

        if (onTheClock == 0)
        {
            Log.v("INFO", "Info: Unpausing.");

            if (savedOTC != 1)
            {
                if (savedOTC == 2)
                {
                    this.P2Click();
                }
            }
            else
            {
                this.P1Click();
            }
        }
        else
        {
            savedOTC = onTheClock;
            onTheClock = 0;
            r4.setBackgroundColor(-3355444);
            r5.setBackgroundColor(-3355444);
            r6.setBackgroundColor(-16776961);
            myHandler.removeCallbacks(mUpdateTimeTask);
            myHandler.removeCallbacks(mUpdateTimeTask2);
        }
    }

    private Dialog ResetDialog()
    {

        AlertDialog$Builder r1;
        r1 = new AlertDialog$Builder(this);
        r1.setMessage("Reset both clocks?").setCancelable(false).setPositiveButton("Yes", new ChessClock$8(this)).setNegativeButton("No", new ChessClock$9(this));
        return r1.create();
    }

    private void SetUpGame()
    {

        SharedPreferences r1;
        SharedPreferences$Editor r9, r41, r58, r64;
        Uri r10;
        TextView r12, r13;
        Button r14, r15, r16;
        String r25, r26, $r27, r29, r30, $r31;
        int $i7, $i9;
        r1 = PreferenceManager.getDefaultSharedPreferences(this);
        haptic = r1.getBoolean("prefHaptic", false);
        r12 = (TextView) this.findViewById(2131165190);
        r13 = (TextView) this.findViewById(2131165187);
        r12.setTextColor(-3355444);
        r13.setTextColor(-3355444);
        r14 = (Button) this.findViewById(2131165186);
        r15 = (Button) this.findViewById(2131165189);
        r16 = (Button) this.findViewById(2131165188);
        r14.setHapticFeedbackEnabled(haptic);
        r15.setHapticFeedbackEnabled(haptic);
        r16.setHapticFeedbackEnabled(haptic);

        if (hapticChange == false)
        {
            delay = r1.getString("prefDelay", "None");

            if (delay.equals("") != false)
            {
                delay = "None";
                r9 = r1.edit();
                r9.putString("prefDelay", "None");
                r9.commit();
            }

            label_8:
            {
                label_6:
                {
                    try
                    {
                        r25 = "prefTime";
                    }
                    catch (Exception $r57)
                    {
                        break label_6;
                    }

                    r26 = "10";

                    try
                    {
                        $r27 = r1.getString(r25, r26);
                    }
                    catch (Exception $r57)
                    {
                        break label_6;
                    }

                    try
                    {
                        $i7 = Integer.parseInt($r27);
                    }
                    catch (Exception $r57)
                    {
                        break label_6;
                    }

                    try
                    {
                        time = $i7;
                        break label_8;
                    }
                    catch (Exception $r57)
                    {
                    }
                } //end label_6:


                time = 10;
                r58 = r1.edit();
                r58.putString("prefTime", "10");
                r58.commit();
            } //end label_8:


            label_9:
            {
                label_7:
                {
                    try
                    {
                        r29 = "prefDelayTime";
                    }
                    catch (Exception $r62)
                    {
                        break label_7;
                    }

                    r30 = "0";

                    try
                    {
                        $r31 = r1.getString(r29, r30);
                    }
                    catch (Exception $r62)
                    {
                        break label_7;
                    }

                    try
                    {
                        $i9 = Integer.parseInt($r31);
                    }
                    catch (Exception $r62)
                    {
                        break label_7;
                    }

                    try
                    {
                        delay_time = $i9;
                        break label_9;
                    }
                    catch (Exception $r62)
                    {
                    }
                } //end label_7:


                delay_time = (int) 0;
                r64 = r1.edit();
                r64.putString("prefDelayTime", "0");
                r64.commit();
            } //end label_9:


            alertTone = r1.getString("prefAlertSound", Settings$System.DEFAULT_RINGTONE_URI.toString());

            if (alertTone.equals("") != false)
            {
                alertTone = Settings$System.DEFAULT_RINGTONE_URI.toString();
                r41 = r1.edit();
                r41.putString("prefAlertSound", alertTone);
                r41.commit();
            }

            r10 = Uri.parse(alertTone);
            ringtone = RingtoneManager.getRingtone(this.getBaseContext(), r10);
            t_P1 = (long) (time * 60000);
            t_P2 = (long) (time * 60000);
            r14.setBackgroundColor(-3355444);
            r15.setBackgroundColor(-3355444);
            r16.setBackgroundColor(-3355444);
            r12.setText(this.FormatTime(t_P1));
            r13.setText(this.FormatTime(t_P2));
            r14.setOnClickListener(P1ClickHandler);
            //r15.setOnClickListener(P2ClickHandler);
            r16.setOnClickListener(PauseListener);
            myHandler.removeCallbacks(Blink);
            myHandler.removeCallbacks(Blink2);
        }
        else
        {
            hapticChange = false;
        }
    }

    static void access$0(ChessClock  r0)
    {


        r0.P1Click();
    }

    static void access$1(ChessClock  r0)
    {


        r0.P2Click();
    }

    static String access$10()
    {


        return BRONSTEIN;
    }

    static void access$11(ChessClock  r0, int  i0)
    {


        r0.b_delay = i0;
    }

    static int access$12(ChessClock  r0)
    {


        return r0.b_delay;
    }

    static int access$13(ChessClock  r0)
    {


        return r0.time;
    }

    static void access$14(ChessClock  r0, boolean  z0)
    {


        r0.timeup = z0;
    }

    static String access$15(ChessClock  r0)
    {


        return r0.alertTone;
    }

    static void access$16(ChessClock  r0, Ringtone  r1)
    {


        r0.ringtone = r1;
    }

    static Ringtone access$17(ChessClock  r0)
    {


        return r0.ringtone;
    }

    static Handler access$18(ChessClock  r0)
    {


        return r0.myHandler;
    }

    static Runnable access$19(ChessClock  r0)
    {


        return r0.mUpdateTimeTask2;
    }

    static void access$2(ChessClock  r0)
    {


        r0.PauseToggle();
    }

    static Runnable access$20(ChessClock  r0)
    {


        return r0.Blink;
    }

    static long access$21(ChessClock  r0)
    {


        return r0.t_P2;
    }

    static void access$22(ChessClock  r0, long  l0)
    {


        r0.t_P2 = l0;
    }

    static Runnable access$23(ChessClock  r0)
    {


        return r0.Blink2;
    }

    static boolean access$24(ChessClock  r0)
    {


        return r0.blink;
    }

    static void access$25(ChessClock  r0, boolean  z0)
    {


        r0.blink = z0;
    }

    static void access$26(ChessClock  r0)
    {


        r0.SetUpGame();
    }

    static void access$27(ChessClock  r0, int  i0)
    {


        r0.onTheClock = i0;
    }

    static String access$3(ChessClock  r0)
    {


        return r0.delay;
    }

    static String access$4()
    {


        return FISCHER;
    }

    static boolean access$5(ChessClock  r0)
    {


        return r0.delayed;
    }

    static void access$6(ChessClock  r0, boolean  z0)
    {


        r0.delayed = z0;
    }

    static long access$7(ChessClock  r0)
    {


        return r0.t_P1;
    }

    static int access$8(ChessClock  r0)
    {


        return r0.delay_time;
    }

    static void access$9(ChessClock  r0, long  l0)
    {


        r0.t_P1 = l0;
    }

    public void onCreate(Bundle  r1)
    {


        this.onCreate(r1);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        pm = (PowerManager) this.getSystemService("power");
        wl = pm.newWakeLock(10, "ChessWakeLock");
        this.setContentView(2130903041);
        this.SetUpGame();
    }

    protected Dialog onCreateDialog(int  i0)
    {

        Dialog r1;
        r1 = new Dialog(this);

        label_10:
        switch (i0)
        {
            case 1:
                r1 = this.ResetDialog();
                break label_10;

            case 2:
                r1 = DF.AboutDialog(this, "1", "2", "0");
                break label_10;

            default:
                break label_10;
        }

        return r1;
    }

    public boolean onCreateOptionsMenu(Menu  r1)
    {


        r1.add(0, (int) 1, 0, "Reset Clocks").setIcon(2130837506);
        r1.add(0, 2, 0, "About").setIcon(2130837504);
        return true;
    }

    public void onDestroy()
    {


        if (wl.isHeld() != false)
        {
            wl.release();
        }

        if (ringtone != null)
        {
            if (ringtone.isPlaying() != false)
            {
                ringtone.stop();
            }
        }

        this.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem  r1)
    {

        boolean z0;
        z0 = true;

        label_11:
        switch (r1.getItemId())
        {
            case 0:
                this.showPrefs();
                Log.v("INFO", "INFO: Trying to create Preferences screen");
                break label_11;

            case 1:
                this.showDialog((int) 1);
                break label_11;

            case 2:
                this.showDialog(2);
                break label_11;

            default:
                z0 = false;
                break label_11;
        }

        return z0;
    }

    public void onPause()
    {


        if (wl.isHeld() != false)
        {
            wl.release();
        }

        if (ringtone != null)
        {
            if (ringtone.isPlaying() != false)
            {
                ringtone.stop();
            }
        }

        this.PauseGame();
        this.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu  r1)
    {


        prefmenu = true;

        if (ringtone != null)
        {
            if (ringtone.isPlaying() != false)
            {
                ringtone.stop();
            }
        }

        this.PauseGame();
        return true;
    }

    public void onResume()
    {


        wl.acquire();

        if (ringtone != null)
        {
            if (ringtone.isPlaying() != false)
            {
                ringtone.stop();
            }
        }

        this.onResume();
    }

    public void onWindowFocusChanged(boolean  z0)
    {


        if (prefmenu != false)
        {
            prefmenu = false;
        }
        else
        {
            this.CheckForNewPrefs();
        }
    }

    private void showPrefs()
    {

		Intent prefsActivity = new Intent(ChessClock.this, Prefs.class);
        //this.startActivity(prefsActivity);
    }
	
	public void deadCode()
	{
		System.out.println("This method will never be invoked!!!");
	}
}
