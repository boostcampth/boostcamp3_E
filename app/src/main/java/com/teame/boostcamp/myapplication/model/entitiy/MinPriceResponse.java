package com.teame.boostcamp.myapplication.model.entitiy;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinPriceResponse {
    @Override
    public String toString() {
        return "MinPriceResponse{" +
                "items=" + items +
                '}';
    }

    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    public class Item {
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("lprice")
        @Expose
        private String lprice;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLprice() {
            return lprice;
        }

        public void setLprice(String lprice) {
            this.lprice = lprice;
        }


        @Override
        public String toString() {
            return "Item{" +
                    "link='" + link + '\'' +
                    ", image='" + image + '\'' +
                    ", lprice='" + lprice + '\'' +
                    '}';
        }

    }
}