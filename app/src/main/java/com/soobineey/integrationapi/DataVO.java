package com.soobineey.integrationapi;

public class DataVO {

    private int img;
    private String id;
    private String openingPrice;
    private String closingPrice;
    private String lowPrice;
    private String highPrice;
    private String tradeVolume;
    private String tradePrice;
    private String average;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpeningPrice() {
        return openingPrice;
//        final float fLimitNumberOpeningprice = Float.valueOf(openingPrice);
//        return String.format("%.2f", fLimitNumberOpeningprice);
    }

    public void setOpeningPrice(String openingPrice) {
        this.openingPrice = openingPrice;
    }

    public String getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(String closingPrice) {
        this.closingPrice = closingPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getHighPrice() {
        return highPrice;
//        final float fLimitNumberHighprice = Float.valueOf(highPrice);
//        return String.format("%.2f", fLimitNumberHighprice);
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(String tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
