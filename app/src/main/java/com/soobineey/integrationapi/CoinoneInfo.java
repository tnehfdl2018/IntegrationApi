package com.soobineey.integrationapi;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class CoinoneInfo {

  static class ConinoneThread extends Thread {
    // 서버 연결용 데이터
    private String CONINONE_API_KEY = "f51a7cad-503a-47b6-9acd-9078808175de";
    private String CO_COMM_URL = "https://api.coinone.co.kr";
    private URL coOpenConnector = null;

    // 받아온 데이터 가공용
    public DataVO coResultDataVO = new DataVO();

    // 스레드가 끝났는지 끝나지 않았는지 판단하는 플래그 (진행중 - false, 종료 - true)
    public boolean coBCheckThreadFlag = false;

    private String TAG = "CoinoneInfo";

    @Override
    public void run() {
      try {
        // Get Price
        String coUrlTail = "/ticker/";
        String coUrlParam = "?currency=redi";
        String coSendURL = CO_COMM_URL + coUrlTail + coUrlParam;
//        String coSendURL = CO_COMM_URL + coUrlTail;
        coOpenConnector = new URL(coSendURL);

        HttpURLConnection coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        BufferedReader coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));
        Log.e("코인원 ", "가격정보");

        String coSResultLastData = coResultDataBufferedReader.readLine();

        JSONObject jsonObject = new JSONObject(coSResultLastData);


        coResultDataVO.setOpeningPrice(jsonObject.getString("first"));
        coResultDataVO.setClosingPrice(jsonObject.getString("last"));
        coResultDataVO.setHighPrice(jsonObject.getString("high"));
        coResultDataVO.setLowPrice(jsonObject.getString("low"));

        String coTodayLastPrice = jsonObject.getString("last");
        String coTodayLastVolume = jsonObject.getString("volume");

        String coSTodayLastPrice = String.format("%.2f", Double.valueOf(coTodayLastPrice));
        String coSodayLastPrice = String.format("%.2f", Double.valueOf(coTodayLastVolume));

        coResultDataVO.setTradePrice(coSTodayLastPrice);
        coResultDataVO.setTradeVolume(coSodayLastPrice);

        Double coTodayAverage = Double.valueOf(coTodayLastPrice) / Double.valueOf(coTodayLastVolume);

//        String testAverage = String.valueOf(Math.round(coTodayAverage *100) / 100.0);
//        coResultDataVO.setAverage(testAverage);
        coResultDataVO.setAverage(String.valueOf(coTodayAverage));

        Log.e("코인원 ", coTodayLastVolume);
        Log.e("코인원 ", coTodayLastPrice);
        Log.e("코인원 ", String.valueOf(coTodayAverage));

        // Get Mail
        coUrlTail = "/v1/account/user_info";
        coSendURL = CO_COMM_URL + coUrlTail + "?access_token=" + CONINONE_API_KEY;
        coOpenConnector = new URL(coSendURL);

        coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));
        Log.e("코인원 ", "아이디");

        coSResultLastData = coResultDataBufferedReader.readLine();

        // 단계별로 구분 되어있는 json형태에서 id까지 찾아 들어간다.
        jsonObject = new JSONObject(coSResultLastData);
        String userInfo = jsonObject.getString("userInfo");

        jsonObject = new JSONObject(userInfo);
        String emailInfo = jsonObject.getString("emailInfo");

        JSONObject emailObject = new JSONObject(emailInfo);

        coResultDataVO.setId(emailObject.getString("email"));
        coResultDataVO.setImg(R.drawable.coinone);

        coBCheckThreadFlag = true;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
