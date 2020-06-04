package com.soobineey.integrationapi;

public class DataVO {

    private int img;
    private String id;
    private String openingPrice;
    private String closingPrice;
    private String lowPrice;
    private String highPrice;

    private boolean flag = false;

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
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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
