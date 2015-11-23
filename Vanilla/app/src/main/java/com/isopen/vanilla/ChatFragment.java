package com.isopen.vanilla;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maxim Tyulenev on 17.06.15.
 */

public class ChatFragment extends Fragment{

    private ListView listView;

    private EditText editText;
    private SimpleAdapter adapter;
    private TextView textView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment,
                container, false);

        ActionBar actionBar = getActivity().getActionBar();

        View v = getActivity().getLayoutInflater().inflate(R.layout.chat_action_bar, null);

        //Display display = ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay();
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        //v.setMinimumWidth(display.getWidth());

        Point size = new Point();
        display.getSize(size);
        v.setMinimumWidth(size.x);

        actionBar.setCustomView(v);

        textView = (TextView)getActivity().findViewById(R.id.text_title);
        textView.setText(getArguments().getString("textTitle"));

        imageView = (ImageView)getActivity().findViewById(R.id.avatar_title);
        //imageView.setBackgroundResource(R.drawable.avatar_border_radius);
        imageView.setImageURI(Uri.parse(Uri.decode(getArguments().getString("avatarTitle"))));


        imageView = (ImageView)getActivity().findViewById(R.id.action_bar_back);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        TBase.chatId = getArguments().getString("chatId");

        editText = (EditText)view.findViewById(R.id.message);

        imageView = (ImageView)view.findViewById(R.id.send_chat);

        TChat.GetChatHistory(Long.valueOf(TBase.chatId),Integer.valueOf(TBase.chatId),0,5);

        listView = (ListView)view.findViewById(R.id.chat_box_message);
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("123","123");
        arrayList.add(0,hashMap);
        adapter = new SimpleAdapter(
                view.getContext(),
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

                TChat.SendMessage(Long.valueOf(TBase.chatId),message);
                Log.e("SENDMESSAGE::","Отправил сообщение.");
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
