package com.soobineey.integrationapi;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String coSendURL = CO_COMM_URL + coUrlTail;
        coOpenConnector = new URL(coSendURL);

        HttpURLConnection coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        BufferedReader coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));

        String coSResultLastData = coResultDataBufferedReader.readLine();

        JSONObject jsonObject = new JSONObject(coSResultLastData);


        coResultDataVO.setOpeningPrice(jsonObject.getString("first"));
        coResultDataVO.setClosingPrice(jsonObject.getString("last"));
        coResultDataVO.setHighPrice(jsonObject.getString("high"));
        coResultDataVO.setLowPrice(jsonObject.getString("low"));

        // Get Mail
        coUrlTail = "/v1/account/user_info";
        coSendURL = CO_COMM_URL + coUrlTail + "?access_token=" + CONINONE_API_KEY;
        coOpenConnector = new URL(coSendURL);

        coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));

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
