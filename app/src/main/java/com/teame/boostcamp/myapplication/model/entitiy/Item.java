package com.teame.boostcamp.myapplication.model.entitiy;

import com.google.firebase.firestore.Exclude;

public class Item {

    @Exclude
    public String key;

    public String name;

    public Double ratio;

    @Exclude
    public String lPrice;

    @Exclude
    public String img;

    @Exclude
    public String link;

    public Item() {

    }

    public Item(String key) {
        this.key = key;
    }

    public void setMinPriceResponse(MinPriceResponse minPriceResponse) {
        MinPriceResponse.Item info = minPriceResponse.getItems().get(0);
        this.img = info.getImage();
        this.link = info.getLink();
        this.lPrice = info.getLprice();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatio() {
        if(ratio == null)
            return 0.0;
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getlPrice() {
        return lPrice;
    }

    public void setlPrice(String lPrice) {
        this.lPrice = lPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", ratio=" + ratio +
                ", lPrice='" + lPrice + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Item){
            Item p = (Item) o;
            return this.name.equals(p.getName());
        } else
            return false;
    }

}
