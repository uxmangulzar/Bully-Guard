package com.example.codesteembullyguard.helpers;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

public class UriToFileConverter {

    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {
        if (uri == null) return null;

        ContentResolver contentResolver = context.getContentResolver();

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For API 31 and above, use the MediaStore
                String[] projection = {MediaStore.Images.Media.DATA};
                try (Cursor cursor = contentResolver.query(uri, projection, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        return cursor.getString(columnIndex);
                    }
                }
            } else {
                // For API 30 and below, handle it differently
                String[] projection = {MediaStore.Images.Media.DATA};
                try (Cursor cursor = contentResolver.query(uri, projection, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        return cursor.getString(columnIndex);
                    }
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null; // Unable to retrieve a file path
    }
}

