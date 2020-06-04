package com.soobineey.integrationapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinoneInfo {

    private static String ACCESS_TOKEN = "f51a7cad-503a-47b6-9acd-9078808175de";
    private static String priceAPI = "https://api.coinone.co.kr";
    private static URL url = null;

    static class PriceThread extends Thread {
        @Override
        public void run() {

            String ticker = "/ticker/";
            String sendURL = priceAPI + ticker;

            try {
                url = new URL(sendURL);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result = bufferedReader.readLine();

                JSONObject jsonObject = new JSONObject(result);

                DataVO vo = new DataVO();

                vo.setOpeningPrice(jsonObject.getString("first"));
                vo.setClosingPrice(jsonObject.getString("last"));
                vo.setHighPrice(jsonObject.getString("high"));
                vo.setLowPrice(jsonObject.getString("low"));

                vo.setFlag(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    static class EmailThread extends Thread {
        @Override
        public void run() {
            String api = "/v1/account/user_info";
            String sendURL = priceAPI + api + "?access_token=" + ACCESS_TOKEN;

            try {
                url = new URL(sendURL);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result = bufferedReader.readLine();
                System.out.println("정보 : " + result);

                // 단계별로 구분 되어있는 json형태에서 id까지 찾아 들어간다.
                JSONObject jsonObject = new JSONObject(result);
                String userInfo = jsonObject.getString("userInfo");

                JSONObject object = new JSONObject(userInfo);
                String emailInfo = object.getString("emailInfo");

                JSONObject emailObject = new JSONObject(emailInfo);

                DataVO vo = new DataVO();
                vo.setId(emailObject.getString("email"));
                vo.setImg(R.drawable.coinone);

                vo.setFlag(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
