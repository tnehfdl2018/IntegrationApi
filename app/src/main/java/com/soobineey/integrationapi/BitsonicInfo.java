package com.soobineey.integrationapi;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.stream.Collectors;

public class BitsonicInfo {

  static class BitsonicThread extends Thread {

    private String COMM_URL = "https://open-api.bitsonic.co.kr";
    private URL url = null;
    private String API = "e113c5d1a063a33805018f0e862003a534c2c5d538584c6adc9ebbc48be68555";
    private String SYMBOL = "btckrw";
    public DataVO vo = new DataVO();
    public boolean flag = false;
    private JSONObject jsonObject;
    private String sendURL;
    private HttpURLConnection con;
    private BufferedReader bufferedReader;
    private String resultJson;
    private String result;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

      String ticker = "/api/v1/ticker/24hr";
      sendURL = COMM_URL + ticker + "?symbol=" + SYMBOL + "&api_key=" + API;

      String account = "/api/v1/account";
      String secretKey = "3939106a261b058706a735db89d16a15fedf7459bab16b2f4084913b305728e7";
      long nonce = System.currentTimeMillis();
      String queryString = "nonce=" + nonce + "&api_key=" + API;

      // 사인키를 위한 string
      String strForSign = account + "/" + nonce + "/" + queryString;

      try {
        url = new URL(sendURL);

        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        resultJson = bufferedReader.lines().collect(Collectors.joining());

        jsonObject = new JSONObject(resultJson);
        result = String.valueOf(jsonObject.get("result"));

        jsonObject = new JSONObject(result);

        vo.setOpeningPrice(jsonObject.getString("o"));
        vo.setClosingPrice(jsonObject.getString("c"));
        vo.setHighPrice(jsonObject.getString("h"));
        vo.setLowPrice(jsonObject.getString("l"));

        // Get Mail

        sendURL = COMM_URL + account + "?nonce=" + nonce + "&api_key=" + API;
        url = new URL(sendURL);
        // HMAC SHA256
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);

        // BS-API-SIGNATURE
        String signatureResult = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(strForSign.getBytes("UTF-8")));

        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("BS-API-SIGNATURE", signatureResult);

        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        resultJson = bufferedReader.lines().collect(Collectors.joining());

        jsonObject = new JSONObject(resultJson);

        result = jsonObject.getString("result");
        System.out.println(result);

        jsonObject = new JSONObject(result);
        vo.setId(jsonObject.getString("email"));
        vo.setImg(R.drawable.bitsonic);

        flag = true;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
