package com.example.fishthibaker.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fishthibaker.R;


public class CommonUtils {
    public static void showDialog(Activity context, String title, String msg, boolean isBack) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isBack) {
                    context.onBackPressed();
                }
            }
        });
        dialog.create().show();
    }

    public static void loadNetworkImage(Context context, String path, ImageView view) {
        Glide.with(context).load(path).placeholder(R.drawable.ic_profile_placeholder).error(R.drawable.ic_profile_placeholder).into(view);
    }

    public static void hideSoftKeyboard(Context localActivity) {
        try {

          /*  InputMethodManager inputMethodManager = (InputMethodManager) localActivity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(localActivity.getCurrentFocus().getWindowToken(), 0);*/

            InputMethodManager imm = (InputMethodManager) localActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
