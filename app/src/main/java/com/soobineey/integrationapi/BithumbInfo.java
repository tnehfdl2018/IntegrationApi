package com.soobineey.integrationapi;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BithumbInfo {

  static class NetworkThread extends Thread {
    private HashMap<String, String> rgParams;
    private boolean flag = false;
    private Api_Client api_client;
    public boolean bitBCheckThreadFlag = false;

    public DataVO bitResultDataVO = new DataVO();

    private JSONObject jsonObject;
    private JSONObject data;

    private URL url;

    @Override
    public void run() {

      rgParams = new HashMap<String, String>();
      rgParams.put("order_currency", "VET");
      rgParams.put("payment_currency", "KRW");

      api_client = new Api_Client("1248a2005f5f17399578dd2db359f65f", "308a0c99013c2b41219bc646238210fb");

      String priceAPI = "https://api.bithumb.com/public/ticker/VET_KRW";

      try {
        url = new URL(priceAPI);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String json = bufferedReader.readLine();

        jsonObject = new JSONObject(json);
        data = jsonObject.getJSONObject("data");

        bitResultDataVO.setImg(R.drawable.bithumb);
        bitResultDataVO.setOpeningPrice(data.getString("opening_price"));
        bitResultDataVO.setClosingPrice(data.getString("closing_price"));
        bitResultDataVO.setLowPrice(data.getString("min_price"));
        bitResultDataVO.setHighPrice(data.getString("max_price"));

//        Log.e("제이슨 ", json);
//        System.out.println(json);

        String result = api_client.callApi("/info/account", rgParams);
//        Log.e("결과 값 ", result);
        System.out.println(result);

        jsonObject = new JSONObject(result);
        data = jsonObject.getJSONObject("data");

        bitResultDataVO.setId(data.getString("account_id"));

        bitBCheckThreadFlag = true;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
