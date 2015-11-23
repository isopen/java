package com.isopen.vanilla;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Maxim Tyulenev on 07.05.15.
 */
public class TAuth extends TBase{

    private TBase tBase = new TBase();

    public Object GetStatus(){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1);
        TG.getClientInstance().send(new TdApi.GetAuthState(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("dsfkjfds", object.toString());
                if (object instanceof TdApi.AuthStateWaitPhoneNumber) {
                    fl_auth = 0;
                } else if (object instanceof TdApi.AuthStateWaitCode) {
                    fl_auth = 3;
                } else if (object instanceof TdApi.AuthStateWaitName) {
                    fl_auth = 1;
                } else if (object instanceof TdApi.AuthStateOk) {
                    fl_auth = 5;
                }
                try {
                    queue.put(object);
                } catch (InterruptedException e) {
                }
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

    public Object SetCode(String code){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1);
        TG.getClientInstance().send(new TdApi.SetAuthCode(code),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("AuthSetCode::",object.toString());
                tBase.sqBase.execSQL(tBase.upd_info_online+"1;");
                Cursor c = tBase.sqBase.rawQuery(tBase.sel_info,null);
                c.moveToFirst();

                Log.e("ONLINE",c.getString(5));
                if(object instanceof TdApi.Error) {
                    fl_auth = 4;
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

    public void SetName(String firstName,String lastName){

        TG.getClientInstance().send(new TdApi.SetAuthName(firstName,lastName),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("AuthSetName:::",object.toString());
                //SetCode();
            }
        });

    }

    public Object SetNumber(String phone){

        final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1);
        TG.getClientInstance().send(new TdApi.SetAuthPhoneNumber(phone),new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.e("AuthSetPhoneNumber::",object.toString());
                if(object instanceof TdApi.Error){
                    fl_auth = 2;
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

    public void exit(){
            TG.getClientInstance().send(new TdApi.ResetAuth(), new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.TLObject object) {
                    Log.e("AuthReset::", object.toString());
                }
            });

    }

}
