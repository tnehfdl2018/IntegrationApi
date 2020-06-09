package com.soobineey.integrationapi;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

public class ImmortalService extends Service {
    private boolean ckRestartFlag = false;

    private MainActivity mainActivity = new MainActivity();

    private Handler handler = new Handler();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                printLogForInfinite();
            }
        }).start();
        return onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ckRestartFlag = true;
    }
    private void printLogForInfinite() {
        // 슬립이 먹질 않음
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("루루룰", "스레드야 되라");
            mainActivity.forImmortal();
            if (ckRestartFlag) break;
        }


    }
}