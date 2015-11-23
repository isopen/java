package com.isopen.vanilla;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Maxim Tyulenev on 25.05.15.
 */
public class TUtils {

    public static Object ParseDate(Object date){

        Date parseDate = new Date(Long.valueOf(date.toString()+"000"));
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");

        calendar.add(calendar.DAY_OF_YEAR,-1);

        if((simpleDateFormat.format(new Date(calendar.getTimeInMillis())).equals(simpleDateFormat.format(parseDate)))||(parseDate.after(new Date(calendar.getTimeInMillis())))){
            String[] result = parseDate.toString().split(" ");
            return result[3];
        }

        calendar.add(calendar.WEEK_OF_MONTH,-1);

        if(parseDate.after(new Date(calendar.getTimeInMillis()))){
            String[] result = parseDate.toString().split(" ");
            return result[0];
        }

        if(parseDate.before(new Date(calendar.getTimeInMillis()))){
            String[] result = parseDate.toString().split(" ");
            return result[1] + " " + result[5];
        }

        String[] result = parseDate.toString().split(" ");
        return result[0];
    }

    public static String[] AvatarText(String firstName,String lastName){

        firstName = firstName.substring(0,1);
        if(!(lastName.equals(""))) {
            lastName = lastName.substring(0, 1);
        }else{
            lastName = "";
        }

        return new String[]{firstName, lastName};

    }

    public static void isCacheAvatar(HashMap<String,Object> hashMap,int index,Context context){

        //Log.e("CONTEXT::",);

        String s = context.getFilesDir().getAbsolutePath();
        File file = new File(s + "/" + TBase.photoSmall);

        if(file.exists()){

            File f = new File(context.getFilesDir().getAbsoluteFile() + "/" + TBase.photoSmall);
            hashMap.put("avatar", Uri.fromFile(f));
            hashMap.put("avatarText", "");

        }else {

            //if(TBase.photoSmall != 0) {

                TBase.upload_avatar.clear();
                TBase.upload_avatar.put(TBase.photoSmall, index);
                TChat.DownloadFile(TBase.photoSmall);

            //}

        }

    }

    public static boolean emptyCache(File dir){

        if(dir != null && dir.isDirectory()){

            String[] children = dir.list();

            for (int i = 0; i < children.length; i++){
                if(i > 1) {
                    File f = new File(dir + "/" + children[i]);
                    f.delete();

                }
                Log.e("FILE", String.valueOf(children[i]));
            }
        }
        return true;
    }

}
