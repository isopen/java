package com.isopen.vanilla;

import android.util.Log;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Maxim Tyulenev on 11.05.15.
 */
public class TChat extends TBase{

    //int[] userId = new int[2];
    //TBase tBase = new TBase();

    public void CreateGroupChat(int[] userId,String title){

        TG.getClientInstance().send(new TdApi.CreateGroupChat(userId,title),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("CreateGroupChat:::",object.toString());
            }
        });

    }

    public static void SendMessage(Long chatId,String message){

        TdApi.InputMessageText inputMessageText = new TdApi.InputMessageText(message);

        /*TG.getClientInstance().send(new TdApi.SendMessage(chatId,inputMessageText),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("SendMessage:::",object.toString());
            }
        });*/

    }

    public static void GetChatHistory(long chatId,int fromId,int offset,int limit){

        TG.getClientInstance().send(new TdApi.GetChatHistory(chatId,fromId,offset,limit),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                try {
                    jsonObject = new JSONObject(gson.toJson(object));
                    Log.e("GetChatHistory::",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public static Object GetChats(){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1);
        TG.getClientInstance().send(new TdApi.GetChats(0,10000000),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                Log.e("GETCHATS::",gson.toJson(object));

                try {

                    jsonArray = new JSONObject(gson.toJson(object)).getJSONArray("chats");
                    typeChat = jsonArray.getJSONObject(0).getJSONObject("type").getJSONObject("user");
                    Log.e("GetChats:::",jsonArray.toString());

                } catch (Exception e) {
                    try{

                        jsonArray = new JSONObject(gson.toJson(object)).getJSONArray("chats");
                        typeChat = jsonArray.getJSONObject(0).getJSONObject("type").get("groupChat");

                    }catch(Exception k){

                    }
                    e.printStackTrace();
                }
                try {
                    queue.put(object);
                } catch (InterruptedException e) {}
                synchronized (queue) {
                    queue.notify();
                }
            }
        });
        if (queue.size() == 0) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return queue.poll();

    }

    public void PrivateChat(int userId){

        TG.getClientInstance().send(new TdApi.CreatePrivateChat(userId),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                try {
                    Long chatId = Long.valueOf(new JSONObject(gson.toJson(object)).get("id").toString());
                    Log.e("PrivateChat:::",String.valueOf(chatId));
                    SendMessage(chatId,"Привет.Как дела?");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void GetMe(){

        TG.getClientInstance().send(new TdApi.GetMe(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("GetMe:::",object.toString());
               /* try {
                    userId[1] = Integer.valueOf(new JSONObject(gson.toJson(object)).get("id").toString());
                    CreateGroupChat(userId,"Привет");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });

    }

    public void GetContacts(){

        TG.getClientInstance().send(new TdApi.GetContacts(),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                try {
                    //Log.e("fdskjdflk",object.toString());
                    JSONArray jsonArray = new JSONObject(gson.toJson(object)).getJSONArray("users");
                    JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                    userId = Integer.valueOf(jsonObject.get("id").toString());
                    //Log.e("GetContacts:::",String.valueOf(userId[0]));
                    //GetMe();
                    PrivateChat(userId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static Object GetChat(Long chatId){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);
        TG.getClientInstance().send(new TdApi.GetChat(chatId), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(object)).getJSONObject("type").getJSONObject("user");
                    TBase.firstName = jsonObject.get("firstName").toString();
                    TBase.lastName = jsonObject.get("lastName").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    queue.put(object);
                } catch (InterruptedException e) {}
                synchronized (queue) {
                    queue.notify();
                }

                Log.e("Chat:::",gson.toJson(object));
            }
        });
        if (queue.size() == 0) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return queue.poll();

    }

    public static Object getGroupChat(int chatId){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);

        TG.getClientInstance().send(new TdApi.GetChat(chatId), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(object)).getJSONObject("type").getJSONObject("groupChat");
                    //TBase.firstName = jsonObject.getJSONObject("topMessage").get("fromId").toString();
                    TBase.firstName = jsonObject.get("title").toString();
                    TBase.photoSmall = Integer.valueOf(jsonObject.getJSONObject("photoSmall").get("id").toString());
                    Log.e("Chat:::",gson.toJson(object));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    queue.put(object);
                } catch (InterruptedException e) {}
                synchronized (queue) {
                    queue.notify();
                }


            }
        });
        if (queue.size() == 0) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return queue.poll();

    }

    public static Object GetUser(int userId) {

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);

        TG.getClientInstance().send(new TdApi.GetUser(userId), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(object));
                    Log.e("df", jsonObject.toString());
                    TBase.firstName = jsonObject.get("firstName").toString();
                    TBase.lastName = jsonObject.get("lastName").toString();
                    TBase.photoSmall = Integer.valueOf(jsonObject.getJSONObject("photoSmall").get("id").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    queue.put(object);
                } catch (InterruptedException e) {}
                synchronized (queue) {
                    queue.notify();
                }

            }

        });
        if (queue.size() == 0) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return queue.poll();

    }

    public void GetUserFull(int userId){

        TG.getClientInstance().send(new TdApi.GetUserFull(userId),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("UserFull::",object.toString());
            }
        });

    }

    public static void DownloadFile(final int fileId){

        /*TG.getClientInstance().send(new TdApi.CancelDownloadFile(fileId),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                //Log.e("CancelDownloadFile::",object.toString());
                if(object instanceof TdApi.Ok) {

                }
            }
        });*/
        TG.getClientInstance().send(new TdApi.DownloadFile(fileId), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                //Log.e("DownloadFile::", object.toString());
            }
        });

    }

    public void Notification(int chatId){
        TdApi.NotificationSettingsScope notificationSettingsScope = new TdApi.NotificationSettingsForChat(chatId);
        TG.getClientInstance().send(new TdApi.SetNotificationSettings(notificationSettingsScope,new TdApi.NotificationSettings(0,"default",true,1)),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("dskfjsdkf",object.toString());
            }
        });
    }

}
