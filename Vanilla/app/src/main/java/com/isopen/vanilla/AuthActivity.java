package com.isopen.vanilla;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;


public class AuthActivity extends Activity {

    private TAuth tAuth;
    private TBase tBase;
    private EditText editText;
    private EditText editText1;
    private AlertDialog.Builder builder;
    private FragmentManager fragmentManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TG.setDir(this.getFilesDir().getAbsolutePath());
        TG.setUpdatesHandler(new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

            }
        });

        tAuth = new TAuth();
        tBase = new TBase();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.auth_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.transparent);

        View view = getLayoutInflater().inflate(R.layout.auth_action_bar, null);
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        view.findViewById(R.id.action_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventClick(view,0);
            }
        });
        view.findViewById(R.id.action_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventClick(view,0);
            }
        });
        actionBar.setCustomView(view);

        TextView textView = (TextView) findViewById(R.id.send_phone_number);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tAuth.exit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        tAuth.exit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id_parent", 0);
        startActivity(intent);
    }

    public void alert(int title){
        builder = new AlertDialog.Builder(AuthActivity.this);
        builder.setTitle(title)
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(false)
                .setNegativeButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void EventClick(View view,int done) {

        switch (view.getId()){
            case R.id.action_bar_back:
                tAuth.exit();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id_parent", 0);
                tBase.sqBase.execSQL(tBase.upd_info_online+"0;");
                startActivity(intent);
                break;
            case R.id.action_done:
                switch (done) {
                    case 0:
                        try {
                                tAuth.GetStatus();
                                Log.e("dsjfdkls",String.valueOf(tBase.fl_auth));
                                fragmentManager = getFragmentManager();
                                textView = (TextView) findViewById(R.id.title);
                                switch (tBase.fl_auth) {
                                    case 0:

                                        editText = (EditText) findViewById(R.id.user_phone);
                                        Log.e("fdksjfsk", editText.getText().toString());

                                        tAuth.SetNumber("7" + editText.getText().toString());
                                        if(tBase.fl_auth != 2) {
                                            EventClick(view, 0);
                                        }else{
                                            alert(R.string.PrivacyFloodControlError);
                                        }

                                        break;
                                    case 1:
                                        textView.setText(R.string.YourName);
                                        NameFragment nameFragment = new NameFragment();
                                        fragmentManager.beginTransaction().replace(R.id.search_close_btn, nameFragment).commit();
                                        view.findViewById(R.id.action_done).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                EventClick(view,2);
                                            }
                                        });
                                        break;
                                    case 2:
                                        alert(R.string.PrivacyFloodControlError);
                                        break;
                                    case 3:
                                        textView.setText(R.string.YourCode);

                                        CodeFragment codeFragment = new CodeFragment();
                                        fragmentManager.beginTransaction().replace(R.id.search_close_btn, codeFragment).commit();
                                        view.findViewById(R.id.action_done).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                EventClick(view,1);
                                            }
                                        });
                                        break;
                                }
                        } catch (Exception e) {
                            alert(R.string.InvalidPhoneNumber);
                        }
                        break;
                    case 1:
                        editText = (EditText) findViewById(R.id.user_code);
                        //Log.e("fksjfsd",editText.getText().toString());
                        tAuth.SetCode(editText.getText().toString());
                        if(tBase.fl_auth == 4){
                            alert(R.string.InvalidCode);
                        }else {
                            Intent chat_intent = new Intent(getApplicationContext(),MainActivity.class);
                            chat_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(chat_intent);
                            finish();
                        }
                        break;
                    case 2:
                        editText = (EditText) findViewById(R.id.user_firstname);
                        editText1 = (EditText) findViewById(R.id.user_lastname);
                        tAuth.SetName(editText.getText().toString(),editText1.getText().toString());
                        EventClick(view,0);
                        break;
                }
        }


    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fl_auth",tBase.fl_auth);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
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
