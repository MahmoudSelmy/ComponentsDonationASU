package com.sheatouk.selmy.componentsdonationasu.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.sheatouk.selmy.componentsdonationasu.R;

/**
 * Created by pc on 28/04/2017.
 */

public class SheamusDialog extends ProgressDialog {
    public SheamusDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialogue);
    }
}
