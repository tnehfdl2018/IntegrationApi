package com.soobineey.integrationapi;

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
        String coUrlParam = "?currency=redi";
        String coSendURL = CO_COMM_URL + coUrlTail + coUrlParam;
//        String coSendURL = CO_COMM_URL + coUrlTail;
        coOpenConnector = new URL(coSendURL);

        HttpURLConnection coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        BufferedReader coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));

        String coSResultData = coResultDataBufferedReader.readLine();

        JSONObject coJsonResultData = new JSONObject(coSResultData);


        coResultDataVO.setOpeningPrice(coJsonResultData.getString("first"));
        coResultDataVO.setClosingPrice(coJsonResultData.getString("last"));
        coResultDataVO.setHighPrice(coJsonResultData.getString("high"));
        coResultDataVO.setLowPrice(coJsonResultData.getString("low"));

        String coSTodayLastVolume = coJsonResultData.getString("volume"); // 거래량
        String coSHigh = coJsonResultData.getString("high"); // 고가
        String coSLow = coJsonResultData.getString("low"); // 저가
        // 평균단가 계산
        Double coDTotalPrice = Double.valueOf(coSHigh) + Double.valueOf(coSLow);
        String coSTotalPrice = String.valueOf(coDTotalPrice / 2);
        
        // 거래대금 계산
        String coSTradePrice = String.valueOf(Double.valueOf(coSTotalPrice) * Double.valueOf(coSTodayLastVolume));

        coResultDataVO.setTradePrice(String.format("%.2f", Double.valueOf(coSTradePrice)));
        coResultDataVO.setTradeVolume(String.format("%.2f", Double.valueOf(coSTodayLastVolume)));

        coResultDataVO.setAverage(coSTotalPrice);

        // Get Mail
        coUrlTail = "/v1/account/user_info";
        coSendURL = CO_COMM_URL + coUrlTail + "?access_token=" + CONINONE_API_KEY;
        coOpenConnector = new URL(coSendURL);

        coHttpConn = (HttpURLConnection) coOpenConnector.openConnection();
        coHttpConn.setRequestMethod("GET");

        coResultDataBufferedReader = new BufferedReader(new InputStreamReader(coHttpConn.getInputStream()));

        String coSResultIdData = coResultDataBufferedReader.readLine();

        // 단계별로 구분 되어있는 json형태에서 id까지 찾아 들어간다.
        JSONObject coJsonResultIdData = new JSONObject(coSResultIdData);
        String userInfo = coJsonResultIdData.getString("userInfo");

        coJsonResultIdData = new JSONObject(userInfo);
        String emailInfo = coJsonResultIdData.getString("emailInfo");

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