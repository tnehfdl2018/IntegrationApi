package com.soobineey.integrationapi;

public class DataVO {

    private int img;
    private String id;
    private String openingPrice;
    private String closingPrice;
    private String lowPrice;
    private String highPrice;

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

    public DataVO(int img, String id, String openingPrice, String closingPrice, String lowPrice, String highPrice) {
        this.img = img;
        this.id = id;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
    }

    public DataVO() {

    }
}
