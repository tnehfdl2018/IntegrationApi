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

    private String BT_COMM_URL = "https://open-api.bitsonic.co.kr";
    private URL btOpenConnector = null;
    private String BT_API_CODE = "e113c5d1a063a33805018f0e862003a534c2c5d538584c6adc9ebbc48be68555";
    private String SYMBOL_PARAMS = "btckrw";
    public DataVO resultDataVO = new DataVO();
    public boolean bCheckThreadFlag = false;
    private JSONObject jsonObject;
    private String sendURL;
    private HttpURLConnection btHttpUrlConn;
    private BufferedReader btResultDatabufferedReader;
    private String sResultBiganData;
    private String sResultLastData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

      String ticker = "/api/v1/ticker/24hr";
      sendURL = BT_COMM_URL + ticker + "?symbol=" + SYMBOL_PARAMS + "&api_key=" + BT_API_CODE;

      String account = "/api/v1/account";
      String secretKey = "3939106a261b058706a735db89d16a15fedf7459bab16b2f4084913b305728e7";
      long nonce = System.currentTimeMillis();
      String queryString = "nonce=" + nonce + "&api_key=" + BT_API_CODE;

      // 사인키를 위한 string
      String strForSign = account + "/" + nonce + "/" + queryString;

      try {
        btOpenConnector = new URL(sendURL);

        btHttpUrlConn = (HttpURLConnection) btOpenConnector.openConnection();
        btHttpUrlConn.setRequestMethod("GET");

        btResultDatabufferedReader = new BufferedReader(new InputStreamReader(btHttpUrlConn.getInputStream()));

        sResultBiganData = btResultDatabufferedReader.lines().collect(Collectors.joining());

        jsonObject = new JSONObject(sResultBiganData);
        sResultLastData = String.valueOf(jsonObject.get("result"));

        jsonObject = new JSONObject(sResultLastData);

        resultDataVO.setOpeningPrice(jsonObject.getString("o"));
        resultDataVO.setClosingPrice(jsonObject.getString("c"));
        resultDataVO.setHighPrice(jsonObject.getString("h"));
        resultDataVO.setLowPrice(jsonObject.getString("l"));

        // Get Mail
        sendURL = BT_COMM_URL + account + "?nonce=" + nonce + "&api_key=" + BT_API_CODE;
        Log.e("BitsonicInfo ", String.valueOf(nonce));
        btOpenConnector = new URL(sendURL);
        // HMAC SHA256
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);

        // BS-API-SIGNATURE
        String signatureResult = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(strForSign.getBytes("UTF-8")));

        btHttpUrlConn = (HttpURLConnection) btOpenConnector.openConnection();
        btHttpUrlConn.setRequestMethod("GET");
        btHttpUrlConn.setRequestProperty("BS-API-SIGNATURE", signatureResult);

        btResultDatabufferedReader = new BufferedReader(new InputStreamReader(btHttpUrlConn.getInputStream()));

        sResultBiganData = btResultDatabufferedReader.lines().collect(Collectors.joining());

        jsonObject = new JSONObject(sResultBiganData);

        sResultLastData = jsonObject.getString("return_code");

        if (sResultLastData.equals("1")) {
          sResultLastData = jsonObject.getString("result");

          jsonObject = new JSONObject(sResultLastData);
          resultDataVO.setId(jsonObject.getString("email"));
          resultDataVO.setImg(R.drawable.bitsonic);

          bCheckThreadFlag = true;
        } else {
          Log.e("BitsonicInfo ", jsonObject.getString("return_code"));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
