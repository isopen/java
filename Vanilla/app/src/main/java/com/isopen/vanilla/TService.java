package com.isopen.vanilla;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TService extends Service {

    public TService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*@Override
    public void onTaskRemoved(Intent intent){

        File f = new File(getApplicationContext().getFilesDir().getAbsolutePath()); //Чистим кэш для новой загрузки аватарок(приложение выключено).
        TUtils.emptyCache(f);
    }*/

}
