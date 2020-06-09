package com.soobineey.integrationapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BithumbInfo {

  static class BithumbThread extends Thread {
    // 서버 연결용 데이터
    private String bitConnAPI = "1248a2005f5f17399578dd2db359f65f";
    private String bitConnSecretAPI = "308a0c99013c2b41219bc646238210fb";
    private HashMap<String, String> bitHeader;
    private URL url;
    private Api_Client api_client;

    // 스레드가 끝났는지 끝나지 않았는지 판단하는 플래그 (진행중 - false, 종료 - true)
    public boolean bitBCheckThreadFlag = false;

    // 받아온 데이터 저장할 VO
    public DataVO bitResultDataVO = new DataVO();

    @Override
    public void run() {

      bitHeader = new HashMap<String, String>();
      bitHeader.put("order_currency", "VET");
      bitHeader.put("payment_currency", "KRW");

      api_client = new Api_Client(bitConnAPI, bitConnSecretAPI);

      String priceAPI = "https://api.bithumb.com/public/ticker/VET_KRW";

      try {
        url = new URL(priceAPI);

        HttpURLConnection bitHttpConn = (HttpURLConnection) url.openConnection();
        bitHttpConn.setRequestMethod("GET");

        BufferedReader bitResultDataBufferedReader = new BufferedReader(new InputStreamReader(bitHttpConn.getInputStream()));

        String priceInfomation = bitResultDataBufferedReader.readLine();

        JSONObject bitPriceInformaionObject = new JSONObject(priceInfomation);
        JSONObject bitDataInPriceInformation = bitPriceInformaionObject.getJSONObject("data");

        bitResultDataVO.setImg(R.drawable.bithumb);
        bitResultDataVO.setOpeningPrice(bitDataInPriceInformation.getString("opening_price"));
        bitResultDataVO.setClosingPrice(bitDataInPriceInformation.getString("closing_price"));
        bitResultDataVO.setLowPrice(bitDataInPriceInformation.getString("min_price"));
        bitResultDataVO.setHighPrice(bitDataInPriceInformation.getString("max_price"));

        String bitTodayTotalVolume = bitDataInPriceInformation.getString("units_traded_24H");
        String bitTodayTotalPrice = bitDataInPriceInformation.getString("acc_trade_value_24H");

        bitResultDataVO.setTradeVolume(String.format("%.2f", Double.valueOf(bitTodayTotalVolume)));
        bitResultDataVO.setTradePrice(String.format("%.2f", Double.valueOf(bitTodayTotalPrice)));

        Double bitTotalAverage = Double.valueOf(bitTodayTotalPrice) / Double.valueOf(bitTodayTotalVolume);

        bitResultDataVO.setAverage(String.valueOf(bitTotalAverage));

        String result = api_client.callApi("/info/account", bitHeader);

        JSONObject bitIdInformaionObject = new JSONObject(result);
        JSONObject bitDataInIdInformation = bitIdInformaionObject.getJSONObject("data");

        bitResultDataVO.setId(bitDataInIdInformation.getString("account_id"));

        bitBCheckThreadFlag = true;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
