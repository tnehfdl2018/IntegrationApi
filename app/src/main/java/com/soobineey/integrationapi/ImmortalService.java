package com.soobineey.integrationapi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class ImmortalService extends Service {
  private boolean ckRestartFlag = false;

  private MainActivity mainActivity = new MainActivity();

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

  // 다시 앱이 실행 되면 flag를 true로 바꿔 아래 메소드를 빠져 나가도록 한다.
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
      mainActivity.coinoneLookupForShow();
      mainActivity.bithumbLookupForShow();

      if (ckRestartFlag) break;
    }


  }
}