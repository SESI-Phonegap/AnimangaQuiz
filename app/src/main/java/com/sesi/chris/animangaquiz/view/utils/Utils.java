package com.sesi.chris.animangaquiz.view.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.model.Anime;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utils class");
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean saveImage(Bitmap finalBitmap, String formato, Context context) {

        String date = (DateFormat.format("yyyyMMdd_hhmmss", new java.util.Date()).toString());
        String fname = "Image-" + date + formato;

        try {
            //Creas el path personalizado dentro de la memoria interna
            String sPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AnimangaQuiz";
            File storageDir = new File(sPath);

            if (!storageDir.exists() && !storageDir.mkdirs()) {
                Log.d("NOOO--", "No se creo el directorio");
            }
            File file = new File(storageDir.getAbsolutePath(), fname);
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            Log.e("Error-", e.getMessage());
            Toast.makeText(context, context.getString(R.string.msgWallpaperNoSaved), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static List<Anime> filtrarAnime(List<Anime> lstAnime, CharSequence s){
        List<Anime> lstAnimeFilter = new ArrayList<>();
        for (Anime anime : lstAnime){
            String textAnime = anime.getName().toLowerCase();
            if (textAnime.contains(s)){
                lstAnimeFilter.add(anime);
            }
        }
        return lstAnimeFilter;
    }

    public static Bitmap base64ToBitmapImage(String sUrlImage){
        byte[] decodedAvatar = Base64.decode(sUrlImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedAvatar, 0, decodedAvatar.length);
    }

    public static void sharedSocial(Context context, String userName) {
        List<Intent> targetShareIntents = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);

/*         if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                 || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus")
                 || packageName.contains("com.google.android.talk") || packageName.contains("com.slack")
                 || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca")
                 || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider")
                 || packageName.contains("com.android.mms")|| packageName.contains("com.linkedin.android")
                 || packageName.contains("com.google.android.apps.messaging")) {*/
                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                        || packageName.contains("com.whatsapp") || packageName.contains("com.facebook.orca")
                        || packageName.contains("com.google.android.apps.plus") || packageName.contains("com.skype.raider")) {
                    Intent intent = new Intent();

                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra(context.getString(R.string.app_name), resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.playstore, userName));
                    intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.compartir));
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), context.getString(R.string.msgCompartir));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                context.startActivity(chooserIntent);
            } else {
                Toast.makeText(context.getApplicationContext(), "No app to share.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
