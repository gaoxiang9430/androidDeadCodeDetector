package com.chessclock.android;

import android.content.Context;
import android.app.Dialog;
import android.widget.TextView;

public class DialogFactory
{

    public DialogFactory()
    {


        this.<init>();
    }

    public Dialog AboutDialog(Context  r1, String  r2, String  r3, String  r4)
    {

        Dialog r5;
        r5 = new Dialog(r1);
        r5.setContentView(2130903040);
        r5.setTitle((new StringBuilder("Simple Chess Clock (SCC) v")).append(r2).append(".").append(r3).append(".").append(r4).toString());
        ((TextView) r5.findViewById(2131165185)).setText("Design/Coding: Carter Dewey\n\nSCC is free software licensed under the GNU GPLv3. You can view the GPLv3 at\nhttp://www.gnu.org/licenses/gpl-3.0.html\n\nPortions of the icon are licensed under the GFDL. You can view the GFDL at:\nhttp://www.gnu.org/copyleft/fdl.html\n\nTo report bugs or view source code, visit:\nhttp://code.google.com/p/simplechessclock/");
        return r5;
    }
}
