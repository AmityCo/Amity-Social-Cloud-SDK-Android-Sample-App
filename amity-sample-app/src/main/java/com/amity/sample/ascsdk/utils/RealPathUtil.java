package com.amity.sample.ascsdk.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import com.google.common.base.Objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import timber.log.Timber;

@Deprecated
public class RealPathUtil {

    public static String getRealPath(Context context, Uri uri) {
        if (isFile(uri)) {
            return getPathFromFile(context, uri);
        } else if (isDocument(context, uri)) {
            return getPathFromDocument(context, uri);
        } else {
            return getPathFromContent(context, uri);
        }
    }

    public static String getFileName(ContentResolver contentResolver, Uri uri) {
        if (uri.getScheme() == null || Objects.equal(ContentResolver.SCHEME_FILE, uri.getScheme())) {
            File file = new File(uri.getPath());
            return file.getName();
        }

        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isFile(Uri uri) {
        return uri.getScheme() == null
                || Objects.equal(ContentResolver.SCHEME_FILE, uri.getScheme());
    }

    private static String getPathFromFile(Context context, Uri uri) {
        return uri.getPath();
    }

    private static boolean isDocument(Context context, Uri uri) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri);
    }

    private static String getPathFromContent(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        cursor.close();
        return path;
    }

    private static String getPathFromDocument(Context context, Uri uri) {
        String fileName = getFileName(context.getContentResolver(), uri);
        InputStream inputStream = null;
        FileOutputStream output = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            File file = new File(context.getCacheDir(), fileName);
            output = new FileOutputStream(file);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
            return file.getAbsolutePath();
        } catch (OutOfMemoryError | Exception e) {
            Timber.e(e, e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                Timber.e(e, e.getMessage());
            }
        }
        return "";
    }

}
