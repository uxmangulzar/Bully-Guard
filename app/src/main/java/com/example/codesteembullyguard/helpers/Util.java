package com.example.codesteembullyguard.helpers;

import android.app.AlertDialog;
import android.content.Context;


import com.example.codesteembullyguard.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class Util {


    public static AlertDialog progressDialog(Context context) {
        AlertDialog dialog = new SpotsDialog(context, R.style.dialog_style);
        dialog.setCancelable(false);
        return dialog;
    }
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = sdf.format(new Date());
        return formattedDate;
    }
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(new Date());
        return formattedDate;
    }


}

