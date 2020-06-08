package com.soobineey.integrationapi;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BithumbInfo {

  static class NetworkThread extends Thread {
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

        Log.e("빗썸", "데이터 조회");

        BufferedReader bitResultDataBufferedReader = new BufferedReader(new InputStreamReader(bitHttpConn.getInputStream()));
        Log.e("빗썸 ", "가격정보");

        String priceInfomation = bitResultDataBufferedReader.readLine();

        JSONObject bitPriceInformaionObject = new JSONObject(priceInfomation);
        JSONObject bitDataInPriceInformation = bitPriceInformaionObject.getJSONObject("data");

        bitResultDataVO.setImg(R.drawable.bithumb);
        bitResultDataVO.setOpeningPrice(bitDataInPriceInformation.getString("opening_price"));
        bitResultDataVO.setClosingPrice(bitDataInPriceInformation.getString("closing_price"));
        bitResultDataVO.setLowPrice(bitDataInPriceInformation.getString("min_price"));
        bitResultDataVO.setHighPrice(bitDataInPriceInformation.getString("max_price"));

//        Log.e("제이슨 ", json);
//        System.out.println(json);

        String result = api_client.callApi("/info/account", bitHeader);
//        Log.e("결과 값 ", result);
        System.out.println(result);
        Log.e("빗썸", "데이터 조회");

        JSONObject bitIdInformaionObject = new JSONObject(result);
        JSONObject bitDataInIdInformation = bitIdInformaionObject.getJSONObject("data");

        Log.e("코인원 ", "가격정보");

        bitResultDataVO.setId(bitDataInIdInformation.getString("account_id"));

        bitBCheckThreadFlag = true;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
