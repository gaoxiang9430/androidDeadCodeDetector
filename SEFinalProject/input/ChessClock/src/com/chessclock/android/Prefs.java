package com.chessclock.android;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.util.Log;

public class Prefs extends PreferenceActivity
{
    protected void onCreate(Bundle  r1)
    {


        this.onCreate(r1);
        Log.v("INFO", "INFO: Read prefs.xml");
        this.addPreferencesFromResource(2130968576);
        Log.v("INFO", "INFO: Finished onCreate");
    }
}
