package com.teame.boostcamp.myapplication.model.entitiy;

public class Banner {
    private String country;
    private String detail;
    private String countryCode;

    public Banner() {
    }

    public Banner(String country, String detail, String countryCode) {
        this.country = country;
        this.detail = detail;
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
