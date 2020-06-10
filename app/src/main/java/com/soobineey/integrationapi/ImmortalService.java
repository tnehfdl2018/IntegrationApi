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
        startBackgroundThread();
      }
    }).start();
    return super.onStartCommand(intent, flag, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    ckRestartFlag = true;
  }
  private void startBackgroundThread() {
    while (true) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      mainActivity.forImmortal();
      if (ckRestartFlag) break;
    }


  }
}