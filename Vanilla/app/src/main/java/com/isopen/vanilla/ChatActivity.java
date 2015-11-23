package com.isopen.vanilla;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatActivity extends Activity {

    private ListView listView;
    private TextView textView;
    private ImageView imageView;
    private EditText editText;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TG.setDir(this.getFilesDir().getAbsolutePath());
        TG.setUpdatesHandler(new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                Log.e("dsjfsd", object.toString());
                //if(object instanceof TdApi.UpdateUserName){

                    /*TGChat.UpdateUserName(object);

                    title = (TextView)findViewById(R.id.title);
                    title.setText(TBase.firstName + " " + TBase.lastName);*/

                //}

            }
        });

        TBase.chatId = getIntent().getExtras().getString("chatId");
        /*Log.e("Enter chat::",TBase.chatId);
        TChat.GetChat(Long.valueOf(TBase.chatId));*/

        //Log.e("Name chat",TBase.firstName + " " + TBase.lastName);

        ActionBar actionBar = getActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        View view = getLayoutInflater().inflate(R.layout.chat_action_bar, null);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        actionBar.setCustomView(view);

        actionBar.setIcon(R.drawable.transparent);

        setContentView(R.layout.chat_activity);

        textView = (TextView)findViewById(R.id.text_title);
        textView.setText(getIntent().getExtras().getString("textTitle"));

        imageView = (ImageView)findViewById(R.id.avatar_title);
        //avatarTitle.setBackgroundResource(R.drawable.avatar_border_radius);

        imageView.setImageURI(Uri.parse(Uri.decode(getIntent().getExtras().getString("avatarTitle"))));

        imageView = (ImageView)findViewById(R.id.action_bar_back);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editText = (EditText)findViewById(R.id.message);

        imageView = (ImageView)findViewById(R.id.send_chat);

        TChat.GetChatHistory(Long.valueOf(TBase.chatId),Integer.valueOf(TBase.chatId),0,5);

        listView = (ListView)findViewById(R.id.chat_box_message);
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("123","123");
        arrayList.add(0,hashMap);
        adapter = new SimpleAdapter(
                getApplicationContext(),
                arrayList,
                R.layout.item_chat,
                new String[]{
                        "123"
                },
                new int[]{
                        R.id.item_chat
                }
        );
        listView.setAdapter(adapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                editText.setText("");

                TChat.SendMessage(Long.valueOf(getIntent().getExtras().getString("chatId")),message);
                Log.e("SENDMESSAGE::","Отправил сообщение.");
            }
        });

        super.onCreate(savedInstanceState);

    }

    /*protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
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
