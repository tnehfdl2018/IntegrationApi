package com.soobineey.integrationapi;

import android.util.Log;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


@SuppressWarnings("unused")
public class Api_Client {
  private static final String DEFAULT_ENCODING = "UTF-8";
  private static final String HMAC_SHA512 = "HmacSHA512";
  protected String api_url = "https://api.bithumb.com";
  protected String api_key;
  protected String api_secret;

  public Api_Client(String api_key, String api_secret) {
    this.api_key = api_key;
    this.api_secret = api_secret;
  }

  public static String encodeURIComponent(String s) {
    String result = null;

    try {
      result = URLEncoder.encode(s, "UTF-8")
          .replaceAll("\\+", "%20")
          .replaceAll("\\%21", "!")
          .replaceAll("\\%27", "'")
          .replaceAll("\\%28", "(")
          .replaceAll("\\%29", ")")
          .replaceAll("\\%26", "&")
          .replaceAll("\\%3D", "=")
          .replaceAll("\\%7E", "~");
    }

    // This exception should never occur.
    catch (UnsupportedEncodingException e) {
      result = s;
    }

    return result;
  }

  public static byte[] hmacSha512(String value, String key) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(
          key.getBytes(DEFAULT_ENCODING), HMAC_SHA512);

      Mac mac = Mac.getInstance(HMAC_SHA512);
      mac.init(keySpec);

      final byte[] macData = mac.doFinal(value.getBytes());
      byte[] hex = new Hex().encode(macData);

      //return mac.doFinal(value.getBytes(DEFAULT_ENCODING));
      return hex;

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String asHex(byte[] bytes) {
    return new String(Base64.encodeBase64(bytes));
  }

  /**
   *
   * @param apiHost 주소
   * @param apiMethod 호출 메소드
   * @param currency 주문통화, 결제통화, endpoint
   * @param httpHeaders 호출시 헤더값 (endpoint, headerParams, api_key, api_secret)
   * @return
   */

  private String request(String apiHost, String apiMethod, HashMap<String, String> currency, HashMap<String, String> httpHeaders) {
    String response = "";

    // apiHost의 첫 시작이 https://로 시작하면 메소드는 get으로 설정
    if (apiHost.startsWith("https://")) {
      HttpRequest request = HttpRequest.get(apiHost);
      // Accept all certificates
      request.trustAllCerts();
      // Accept all hostnames
      request.trustAllHosts();
    }

    if (apiMethod.toUpperCase().equals("HEAD")) {
    } else {
      // 메소드가 HEAD가 아니라면 request 초기화
      HttpRequest request = null;

      if (apiMethod.toUpperCase().equals("POST")) {

        request = new HttpRequest(apiHost, "POST");
        request.readTimeout(10000);

        if (httpHeaders != null && !httpHeaders.isEmpty()) {
          httpHeaders.put("api-client-type", "2");
          request.headers(httpHeaders); // 전송할 수 있는 헤더 값으로 만들기
          System.out.println(httpHeaders.toString());
        }
        if (currency != null && !currency.isEmpty()) {
          request.form(currency);
        }
      } else {
        request = HttpRequest.get(apiHost + Util.mapToQueryString(currency));
        request.readTimeout(10000);
      }

      if (request.ok()) {
        response = request.body();
      } else {
        response = "error : " + request.code() + ", message : " + request.body();
      }
      request.disconnect();
    }

    return response;
  }

  private HashMap<String, String> getHttpHeaders(String endpoint, HashMap<String, String> headerParams, String apiKey, String apiSecret) {

    // url의 파라미터 형태로 만든다. ex) ?aaa=aa&bbb=bb
    String urlParameter = Util.mapToQueryString(headerParams).replace("?", "");
    String nNonce = String.valueOf(System.currentTimeMillis()); // 요청 시간

    urlParameter = urlParameter.substring(0, urlParameter.length() - 1);

    urlParameter = encodeURIComponent(urlParameter);

    HashMap<String, String> headerArray = new HashMap<String, String>();


    String urlParams = endpoint + ";" + urlParameter + ";" + nNonce;

    String encryptionStr = asHex(hmacSha512(urlParams, apiSecret));

    Log.e("Api_Client endcoded ", encryptionStr);

    headerArray.put("Api-Key", apiKey);
    headerArray.put("Api-Sign", encryptionStr);
    headerArray.put("Api-Nonce", nNonce);

    return headerArray;

  }

  /**
   *
   * @param endpoint
   * @param params headerCurrency
   * @return
   */
  /**
   *
   * headerCurrency
   * order_currency : VET
   * payment_currency : KRW
   * endpoint : /info/account
   */
  @SuppressWarnings("unchecked")
  public String callApi(String endpoint, HashMap<String, String> params) {
    String rgResultDecode = "";
    HashMap<String, String> headerParams = new HashMap<String, String>();
    headerParams.put("endpoint", endpoint);

    if (params != null) {
      headerParams.putAll(params);
    }

    String api_host = api_url + endpoint;
    HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, headerParams, api_key, api_secret);

    rgResultDecode = request(api_host, "POST", headerParams, httpHeaders);

    if (!rgResultDecode.startsWith("error")) {
      try {
        HashMap<String, String> result = new ObjectMapper().readValue(rgResultDecode, HashMap.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return rgResultDecode;
  }
}