package com.soobineey.integrationapi;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinoneInfo {

  static class ConinoneThread extends Thread {
    private String ACCESS_TOKEN = "f51a7cad-503a-47b6-9acd-9078808175de";
    private String priceAPI = "https://api.coinone.co.kr";
    private URL url = null;

    public DataVO vo = new DataVO();
    public boolean flag = false;

    @Override
    public void run() {
      try {
        // Get Price
        String api = "/ticker/";
        String sendURL = priceAPI + api;
        url = new URL(sendURL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String result = bufferedReader.readLine();

        JSONObject jsonObject = new JSONObject(result);

        vo.setOpeningPrice(jsonObject.getString("first"));
        vo.setClosingPrice(jsonObject.getString("last"));
        vo.setHighPrice(jsonObject.getString("high"));
        vo.setLowPrice(jsonObject.getString("low"));

        // Get Mail
        api = "/v1/account/user_info";
        sendURL = priceAPI + api + "?access_token=" + ACCESS_TOKEN;
        url = new URL(sendURL);

        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        result = bufferedReader.readLine();
        System.out.println("정보 : " + result);

        // 단계별로 구분 되어있는 json형태에서 id까지 찾아 들어간다.
        jsonObject = new JSONObject(result);
        String userInfo = jsonObject.getString("userInfo");

        jsonObject = new JSONObject(userInfo);
        String emailInfo = jsonObject.getString("emailInfo");

        JSONObject emailObject = new JSONObject(emailInfo);

        vo.setId(emailObject.getString("email"));
        vo.setImg(R.drawable.coinone);

        flag = true;
        Log.v("CoinoneInfo", "try-catch 마지막 줄");

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
