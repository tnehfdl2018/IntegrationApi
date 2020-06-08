package com.soobineey.integrationapi;

public class DataVO {

    private int img;
    private String id;
    private String openingPrice;
    private String closingPrice;
    private String lowPrice;
    private String highPrice;
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
//        final float fLimitNumberClosingprice = Float.valueOf(closingPrice);
//        return String.format("%.2f", fLimitNumberClosingprice);
    }

    public void setClosingPrice(String closingPrice) {
        this.closingPrice = closingPrice;
    }

    public String getLowPrice() {
        return lowPrice;
//        final float fLimitNumberLowprice = Float.valueOf(lowPrice);
//        return String.format("%.2f", fLimitNumberLowprice);
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

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
