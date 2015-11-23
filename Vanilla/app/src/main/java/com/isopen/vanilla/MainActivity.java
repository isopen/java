package com.isopen.vanilla;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends Activity {

    static {
        try {
            System.loadLibrary("tdjni");
        } catch (UnsatisfiedLinkError e) {
            Log.w("DLTD", "Can't find tdjni", e);
        }


    }

    private TGChat tgChat;
    private ListView listView;
    private SimpleAdapter adapter;
    private FragmentManager fragmentManager = getFragmentManager();
    private ChatFragment chatFragment = new ChatFragment();

    public static HashMap<String,Object> tempMap;

    private void chatsTGSocket(){

        TG.setDir(this.getFilesDir().getAbsolutePath());
        TG.setUpdatesHandler(new Client.ResultHandler() {
            @Override
            public void onResult(final TdApi.TLObject object) {

                //Log.e("OBJECT::",object.toString());
                if(listView.getCount() >= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (object instanceof TdApi.UpdateNewMessage) {

                                tgChat.UpdateTitleChat(object);

                            }

                            if (object instanceof TdApi.UpdateFile) {

                                tgChat.UpdateFile(object);

                            }

                            //Parcelable state = listView.onSaveInstanceState();

                            //adapter.notifyDataSetInvalidated();
                            adapter.notifyDataSetChanged();
                            //listView.onRestoreInstanceState(state);

                        }
                    });

                }

                /*if(listView.getCount() >= 0) {

                    /*if (object instanceof TdApi.UpdateDeleteMessages) {

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        tgChat.UpdateDeleteMessages(object);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }*/

                    /*if (object instanceof TdApi.UpdateUserPhoto) {

                        tgChat.UpdateUserPhoto(object);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }*/

                //}

            }
        });

    }


    private void ListItem() {

        /*ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if((networkInfo != null)&&(networkInfo.isConnected())){
            Log.e("CONNECT","YES");
        }else{
            Log.e("CONNECT","NO");
        }*/



        listView = (ListView)findViewById(R.id.list_item);

        TBase.id_users = new ArrayList<>();
        TBase.id_chats = new ArrayList<>();
        TBase.upload_avatar = new HashMap<>();

        HashMap<String, Object> hashMap;

        String firstName;
        String lastName;
        Integer photoSmall;

        JSONObject user;

        for(int i = 0;i < TBase.jsonArray.length();i++){
            try {

                //single chat

                hashMap = new HashMap<>();
                TBase.random = new Random();

                user = TBase.jsonArray.getJSONObject(i).getJSONObject("type").getJSONObject("user");
                firstName = user.get("firstName").toString();
                lastName = user.get("lastName").toString();
                hashMap.put("firstName", firstName + " " + lastName);
                String[] avatar = TUtils.AvatarText(firstName, lastName);
                try {
                    hashMap.put("topMessage", TBase.jsonArray.getJSONObject(i).getJSONObject("topMessage").getJSONObject("message").get("text").toString());
                }catch(Exception e){
                    hashMap.put("topMessage", "(media contant)");
                }
                hashMap.put("countMessage","");
                hashMap.put("listGroup",R.drawable.transparent);
                hashMap.put("date", TUtils.ParseDate(TBase.jsonArray.getJSONObject(i).getJSONObject("topMessage").get("date")));
                photoSmall = Integer.valueOf(user.getJSONObject("profilePhoto").getJSONObject("small").get("id").toString());
                if(photoSmall != 0){
                    TBase.upload_avatar.put(photoSmall, i);
                }
                hashMap.put("avatarText", avatar[0] + avatar[1]);
                hashMap.put("avatar", TBase.color[TBase.random.nextInt(TBase.color.length)]);

                TBase.id_chats.add(TBase.jsonArray.getJSONObject(i).get("id").toString());
                TBase.id_users.add(i,hashMap);

            }catch (Exception e){
                try{

                    Log.e("GROUP::",TBase.jsonArray.getJSONObject(i).getJSONObject("type").toString());
                    //group chat
                    hashMap = new HashMap<>();
                    TBase.random = new Random();

                    user = TBase.jsonArray.getJSONObject(i).getJSONObject("type").getJSONObject("groupChat");
                    firstName = user.get("title").toString();
                    hashMap.put("firstName"," " + firstName);

                    TChat.GetUser(Integer.valueOf(TBase.jsonArray.getJSONObject(i).getJSONObject("topMessage").get("fromId").toString()));

                    hashMap.put("userGroup",TBase.firstName + ": ");

                    try {
                        hashMap.put("topMessage", TBase.jsonArray.getJSONObject(i).getJSONObject("topMessage").getJSONObject("message").get("text").toString());
                    }catch (Exception f){
                        hashMap.put("topMessage", "(media contant)");
                    }

                    hashMap.put("countMessage","");
                    hashMap.put("listGroup",R.drawable.list_group);
                    hashMap.put("date",TUtils.ParseDate(TBase.jsonArray.getJSONObject(i).getJSONObject("topMessage").get("date")));
                    photoSmall = Integer.valueOf(user.getJSONObject("photo").getJSONObject("small").get("id").toString());
                    if(photoSmall != 0){
                        TBase.upload_avatar.put(photoSmall, i);
                    }
                    hashMap.put("avatarText","G");
                    hashMap.put("avatar",TBase.color[TBase.random.nextInt(TBase.color.length)]);
                    TBase.id_chats.add(TBase.jsonArray.getJSONObject(i).get("id").toString());
                    TBase.id_users.add(i,hashMap);

                }catch (Exception f){

                    Log.e("Generat list error::",f.getMessage());
                }

            }
        }

        adapter = new SimpleAdapter(
                this,
                TBase.id_users,
                R.layout.item_chats,
                new String[]{
                        "listGroup",
                        "firstName",
                        "topMessage",
                        "date",
                        "avatarText",
                        "avatar",
                        "countMessage",
                        "userGroup"
                },
                new int[]{
                        R.id.list_group,
                        R.id.name_chat,
                        R.id.topMessage,
                        R.id.time_chat,
                        R.id.avatar_text,
                        R.id.avatar,
                        R.id.count_new_message,
                        R.id.user_group
                }
        );

        listView.setAdapter(adapter);

        Log.e("AVATAR::",TBase.upload_avatar.toString());

        Log.e("ID_USERS::",TBase.id_users.toString());

        Log.e("ID_CHATS::",TBase.id_chats.toString());

        for(Map.Entry<Integer,Integer> entry : TBase.upload_avatar.entrySet()){

            TChat.DownloadFile(Integer.valueOf(entry.getKey().toString()));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        chatsTGSocket();

        //File f = new File(this.getFilesDir().getAbsolutePath());
        //TUtils.emptyCache(f);

        //Log.e("CONTEXT::",this.getFilesDir().getPath());

        tgChat = new TGChat();

        TBase.sqBase = openOrCreateDatabase(TBase.dbName, Context.MODE_PRIVATE, null);

        //tBase.sqBase.execSQL(tBase.drop_info);
        TBase.sqBase.execSQL(TBase.create_info);
        try {
            TBase.sqBase.execSQL(TBase.ins_info);
        }catch(Exception e){}

        //tAuth.GetStatus();

        Cursor c = TBase.sqBase.rawQuery(TBase.sel_info,null);
        c.moveToFirst();

        Log.e("fdskj", c.getString(5));

        ActionBar actionBar = getActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        if(c.getInt(5) == 1){

            //Log.e("fdskjf","PP");

            TChat.GetChats();

            startService(new Intent(this,TService.class));

            View view = getLayoutInflater().inflate(R.layout.chats_action_bar, null);
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

            //view.setMinimumWidth(display.getWidth());

            Point size = new Point();
            display.getSize(size);
            view.setMinimumWidth(size.x);

            actionBar.setCustomView(view);

            view.findViewById(R.id.action_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventClick();
                }
            });

            setContentView(R.layout.chats_activity);

            ListItem();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("fdsjkf",String.valueOf(i));
                    //TBase.chatId = TBase.id_chats.get(i);
                    /*Intent chat_intent = new Intent(getApplicationContext(),ChatActivity.class);
                    chat_intent.putExtra("chatId",TBase.id_chats.get(i));
                    chat_intent.putExtra("textTitle",TBase.id_users.get(i).get("firstName").toString());
                    chat_intent.putExtra("avatarTitle",TBase.id_users.get(i).get("avatar").toString());
                    startActivity(chat_intent)*/

                    Bundle arg = new Bundle();

                    arg.putString("chatId",TBase.id_chats.get(i));
                    arg.putString("textTitle",TBase.id_users.get(i).get("firstName").toString());
                    arg.putString("avatarTitle",TBase.id_users.get(i).get("avatar").toString());

                    chatFragment.setArguments(arg);

                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.change_chats_screen, chatFragment)
                            .addToBackStack("chat")
                            .commit();
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                    //Log.e("ScrollDown::",Integer.toString(i));
                    //int bottom = listView.getMaxScrollAmount();

                    /*if(listView.getChildAt(0).getTop() == 0){
                        Toast toast = Toast.makeText(getApplicationContext(),"Да!",Toast.LENGTH_SHORT);
                        toast.show();
                    }*/
                    //Log.e("SCROLL::",String.valueOf(listView.getMaxScrollAmount()));
                    //listView.smoothScrollToPosition(listView.getCount()-1);
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i2, int i3) {

                    /*if((listView.getLastVisiblePosition() == listView.getCount() - 1)&&(listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight())){
                        Toast toast = Toast.makeText(getApplicationContext(),"Да!",Toast.LENGTH_SHORT);
                        toast.show();
                    }*/
                    //Log.e("ScrollCount::",String.valueOf());
                }
            });

        }else {

            //tBase.sqBase.execSQL(tBase.drop_info);

            Log.e("fskjfd", "fskdjfds");
            setContentView(R.layout.intro_layout);

            View view = getLayoutInflater().inflate(R.layout.intro_action_bar, null);
            actionBar.setCustomView(view);

            TextView textView = (TextView) findViewById(R.id.start_messaging_button);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                }
            });

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    public void EventClick(){
        //tChat.SendMessage(Long.valueOf(71561460),"Привет");
    }

    @Override
    public void onDestroy(){
        //File f = new File(TBase.context.getFilesDir().getAbsolutePath()); //Чистим кэш для новой загрузки аватарок(приложение выключено).
        //TUtils.emptyCache(f);
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //buildTGSocket();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(!(fragmentManager.popBackStackImmediate())){

            ActionBar actionBar = getActionBar();

            View v = getLayoutInflater().inflate(R.layout.chats_action_bar, null);

            Display display = getWindowManager().getDefaultDisplay();

            v.setMinimumWidth(display.getWidth());

            actionBar.setCustomView(v);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
