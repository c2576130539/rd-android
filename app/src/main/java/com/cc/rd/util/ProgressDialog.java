package com.cc.rd.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cc.rd.R;

public class ProgressDialog {

    private static volatile ProgressDialog instance;

    private ProgressDialog() {
    }

    public static ProgressDialog getInstance() {
        if (instance == null) {
            synchronized (ProgressDialog.class) {
                if (instance == null) {
                    instance = new ProgressDialog();
                }
            }
        }
        return instance;
    }

    private MaterialDialog materialDialog;

    public void show(Context mContext) {
        materialDialog = new MaterialDialog.Builder(mContext)
                .content(R.string.progress_please_wait)
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false)
                .show();

    }

    public void dismiss() {
        materialDialog.dismiss();
    }
}
