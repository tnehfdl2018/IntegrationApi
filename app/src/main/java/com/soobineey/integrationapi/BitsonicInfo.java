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

    // 서버 연결용 데이터
    private String BT_COMM_URL = "https://open-api.bitsonic.co.kr";
    private String BT_API_CODE = "e113c5d1a063a33805018f0e862003a534c2c5d538584c6adc9ebbc48be68555";
    private String SYMBOL_PARAMS = "btckrw";
    private URL btOpenConnector = null;
    private HttpURLConnection btHttpUrlConn;
    private BufferedReader btResultDatabufferedReader;
    private String sendURL;

    // 스레드가 끝났는지 끝나지 않았는지 판단하는 플래그 (진행중 - false, 종료 - true)
    public boolean bsBCheckThreadFlag = false;
    
    // 받아온 데이터 가공용
    public DataVO bsResultDataVO = new DataVO();
    private String sResultBiganData;
    private String sResultLastData;

    private String TAG = "BitsonicInfo";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

      // 가격 정보를 받아오기 위한 url 생성
      String sendUrlTail = "/api/v1/ticker/24hr";
      sendURL = BT_COMM_URL + sendUrlTail + "?symbol=" + SYMBOL_PARAMS + "&api_key=" + BT_API_CODE;

      // 아이디 정보를 받아오기 위한 url 생성
      String accountTail = "/api/v1/account";
      String secretKey = "3939106a261b058706a735db89d16a15fedf7459bab16b2f4084913b305728e7";
      long nonce = System.currentTimeMillis();
      String queryString = "nonce=" + nonce + "&api_key=" + BT_API_CODE;

      // 사인키를 위한 string
      String strForSign = accountTail + "/" + nonce + "/" + queryString;

      try {
        // 가격정보 받아오기
        btOpenConnector = new URL(sendURL);

        btHttpUrlConn = (HttpURLConnection) btOpenConnector.openConnection();
        btHttpUrlConn.setRequestMethod("GET");

        btResultDatabufferedReader = new BufferedReader(new InputStreamReader(btHttpUrlConn.getInputStream()));

        sResultBiganData = btResultDatabufferedReader.lines().collect(Collectors.joining());

        JSONObject bsTotalResult = new JSONObject(sResultBiganData);
        sResultLastData = String.valueOf(bsTotalResult.get("result"));

        JSONObject bsfilterResult = new JSONObject(sResultLastData);

        bsResultDataVO.setOpeningPrice(bsfilterResult.getString("o"));
        bsResultDataVO.setClosingPrice(bsfilterResult.getString("c"));
        bsResultDataVO.setHighPrice(bsfilterResult.getString("h"));
        bsResultDataVO.setLowPrice(bsfilterResult.getString("l"));
        bsResultDataVO.setTradeVolume(bsfilterResult.getString("v"));

        // Get Mail (아이디 받아오기)
        sendURL = BT_COMM_URL + accountTail + "?nonce=" + nonce + "&api_key=" + BT_API_CODE;
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

        JSONObject bsTotalResultForId = new JSONObject(sResultBiganData);

        sResultLastData = bsTotalResultForId.getString("return_code");

        bsBCheckThreadFlag = true;
        if (sResultLastData.equals("1")) {
          sResultLastData = bsTotalResultForId.getString("result");

          JSONObject bsFilterResultForId = new JSONObject(sResultLastData);
          bsResultDataVO.setId(bsFilterResultForId.getString("email"));
          bsResultDataVO.setImg(R.drawable.bitsonic);

        } else {
          Log.e("BitsonicInfo ", bsTotalResultForId.getString("return_code"));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
