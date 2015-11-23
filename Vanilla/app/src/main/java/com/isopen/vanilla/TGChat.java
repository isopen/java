package com.isopen.vanilla;

import android.net.Uri;
import android.util.Log;

import org.drinkless.td.libcore.telegram.TdApi;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Maxim Tyulenev on 03.06.15.
 */
public class TGChat extends TBase{

    private HashMap<String,Object> tempMap;

    public static void UpdateUserName(TdApi.TLObject object){

        try{

            jsonObject = new JSONObject(gson.toJson(object));

            TBase.firstName = jsonObject.get("firstName").toString();
            TBase.lastName = jsonObject.get("lastName").toString();

            Log.e("POLUCHIL::",TBase.firstName + " " + TBase.lastName);

        }catch (Exception e){

        }

    }

    public void UpdateDeleteMessages(TdApi.TLObject object) {
        try {

            jsonObject = new JSONObject(gson.toJson(object));

            String chatId = jsonObject.get("chatId").toString();

            id_item = id_chats.indexOf(chatId);
            //id_chats.remove(chatId);
            //id_users.remove(id_item);

            tempMap = id_users.get(id_item);
            tempMap.put("topMessage","");
            id_users.set(id_item, tempMap);
            //Log.e("IDCHATS::",id_chats.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void UpdateFile(TdApi.TLObject object){

        Log.e("STATUS::","PHOTO LOADING...");

        try {
            jsonObject = new JSONObject(gson.toJson(object));
            Log.e("FIELD::",jsonObject.toString());
            Integer fileId = Integer.valueOf(jsonObject.getJSONObject("file").get("id").toString());

            try {
                tempMap = id_users.get(upload_avatar.get(fileId));

                tempMap.put("avatarText","");
                tempMap.put("avatar", Uri.parse(jsonObject.getJSONObject("file").get("path").toString()));
               // Log.e("PATH::",jsonObject.getJSONObject("file").get("path").toString());
                id_users.set(upload_avatar.get(fileId), tempMap);
            }catch(Exception e){
                Log.e("UpdateFileView::",e.getMessage());
            }

        } catch (JSONException e) {
            Log.e("UpdateFile::",e.getMessage());
        }
    }

    public void UpdateUserPhoto(TdApi.TLObject object){

        try {
            jsonObject = new JSONObject(gson.toJson(object));
            Log.e("UPDATEPHOTO::",jsonObject.toString());
            TBase.chatId = jsonObject.get("userId").toString();

            photoSmall = Integer.valueOf(jsonObject.getJSONObject("photoSmall").get("id").toString());

            hashMap = new HashMap<>();
            TBase.id_item = TBase.id_chats.indexOf(chatId);
            tempMap = id_users.get(TBase.id_item);
            hashMap.put("firstName", tempMap.get("firstName"));
            hashMap.put("topMessage", tempMap.get("topMessage"));
            hashMap.put("date", tempMap.get("date"));
            hashMap.put("avatarText", "");
            hashMap.put("avatar", color[random.nextInt(color.length)]);

            TBase.upload_avatar.put(TBase.photoSmall, TBase.id_item);
            TChat.DownloadFile(TBase.photoSmall);

            id_users.set(TBase.id_item,hashMap);

        } catch (Exception e) {
            Log.e("ERROR::",e.getMessage());
            e.printStackTrace();
        }

    }

    public synchronized void UpdateTitleChat(TdApi.TLObject object){

        try {

            TBase.jsonObject = new JSONObject(TBase.gson.toJson(object));

            Log.e("dfksjfd", TBase.jsonObject.toString());

            TBase.chatId = TBase.jsonObject.getJSONObject("message").get("chatId").toString();
            TBase.id_item = TBase.id_chats.indexOf(TBase.chatId);
            Log.e("id_item", String.valueOf(TBase.id_item));

            if(id_item != -1) {

                //single chat
                id_chats.remove(chatId);
                id_chats.add(0, chatId);
                Log.e("ID_CHATS", id_chats.toString());

                tempMap = id_users.get(id_item);

                id_users.remove(id_item);

                //hashMap = new HashMap<>();

                //hashMap.put("firstName", tempMap.get("firstName").toString());

                try {
                    tempMap.put("topMessage", jsonObject.getJSONObject("message").getJSONObject("message").get("text").toString());
                } catch (Exception e) {
                    tempMap.put("topMessage", "(media contant)");
                }

                String spaceCount = tempMap.get("countMessage").toString();
                if(spaceCount.equals("")) {
                    tempMap.put("countMessage","1");
                }else{
                    int countMessage = Integer.valueOf(spaceCount) + 1;
                    tempMap.put("countMessage",countMessage);
                }

                //hashMap.put("listGroup",tempMap.get("listGroup"));

                try {
                    tempMap.put("date", TUtils.ParseDate(jsonObject.getJSONObject("message").get("date").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //group chat
                if(Integer.valueOf(chatId) < 0){

                    TChat.GetUser(Integer.valueOf(jsonObject.getJSONObject("message").get("fromId").toString()));

                    tempMap.put("userGroup",TBase.firstName + ": ");

                }

                //hashMap.put("avatarText", tempMap.get("avatarText"));
                //hashMap.put("avatar", tempMap.get("avatar"));
                id_users.add(0, tempMap);

            }else{

                if(Integer.valueOf(chatId) > 0) {

                    //single chat
                    TChat.GetUser(Integer.valueOf(chatId));

                    Log.e("NEWCHAT::", "BUILD");

                    String[] avatar = TUtils.AvatarText(firstName, lastName);

                    TBase.random = new Random();

                    id_chats.add(0, chatId);

                    hashMap = new HashMap<>();

                    hashMap.put("firstName", firstName + " " + lastName);

                    try {
                        hashMap.put("topMessage", jsonObject.getJSONObject("message").getJSONObject("message").get("text").toString());
                    } catch (Exception e) {
                        hashMap.put("topMessage", "(media contant)");
                    }
                    try {
                        hashMap.put("date", TUtils.ParseDate(jsonObject.getJSONObject("message").get("date").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hashMap.put("countMessage","1");
                    hashMap.put("listGroup",R.drawable.transparent);
                    hashMap.put("avatarText", avatar[0] + avatar[1]);
                    hashMap.put("avatar", TBase.color[random.nextInt(TBase.color.length)]);

                    Log.e("PHOTOSMALL::", photoSmall.toString());

                    id_users.add(0, hashMap);

                    //TBase.upload_avatar.clear();
                    TBase.upload_avatar.put(TBase.photoSmall, 0);
                    TChat.DownloadFile(TBase.photoSmall);
                }else{

                    //group chat
                    TChat.getGroupChat(Integer.valueOf(chatId));
                    Log.e("CroupChat::", "build");

                    //String[] avatar = TUtils.AvatarText(firstName, lastName);

                    TBase.random = new Random();

                    id_chats.add(0, chatId);

                    //firstName = jsonObject.getJSONObject("groupChat").get("title").toString();

                    hashMap = new HashMap<>();

                    hashMap.put("firstName"," " + TBase.firstName);
                    //Log.e("TOPMESSAGE::",jsonObject.getJSONObject("topMessage").toString());
                    try {
                        hashMap.put("topMessage", jsonObject.getJSONObject("message").getJSONObject("message").get("text").toString());
                    }catch (Exception f){
                        hashMap.put("topMessage", "(media contant)");
                    }
                    hashMap.put("countMessage","1");
                    hashMap.put("listGroup",R.drawable.list_group);
                    hashMap.put("date",TUtils.ParseDate(jsonObject.getJSONObject("message").get("date")));
                    //photoSmall = Integer.valueOf(jsonObject.getJSONObject("groupChat").getJSONObject("photoSmall").get("id").toString());
                    hashMap.put("avatarText","G");
                    hashMap.put("avatar",TBase.color[TBase.random.nextInt(TBase.color.length)]);

                    TChat.GetUser(Integer.valueOf(jsonObject.getJSONObject("message").get("fromId").toString()));

                    hashMap.put("userGroup",TBase.firstName + ": ");

                    //TBase.id_users.add(0,hashMap);

                    //Log.e("PHOTOSMALL::", photoSmall.toString());

                    id_users.add(0, hashMap);

                    //TBase.upload_avatar.clear();
                    TBase.upload_avatar.put(TBase.photoSmall, 0);
                    TChat.DownloadFile(TBase.photoSmall);

                }

            }

        } catch (Exception e) {
            //Log.e("dsfkjfds",e.getMessage());
            e.printStackTrace();
        }

    }

}
