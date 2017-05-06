package sis.pewpew.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DelayService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
