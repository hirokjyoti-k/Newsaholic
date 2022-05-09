package com.adbu.newsaholic.model;

public class LiveChannel {

    public LiveChannel(){}

    String NewsName;
    String NewsUrl;
    String NewsLogo;
    String Language;


    public LiveChannel(String newsName, String newsUrl, String newsLogo, String language) {
        NewsName = newsName;
        NewsUrl = newsUrl;
        NewsLogo = newsLogo;
        Language = language;
    }

    public String getNewsName() {
        return NewsName;
    }

    public String getNewsUrl() {
        return NewsUrl;
    }

    public String getNewsLogo() {
        return NewsLogo;
    }

    public String getLanguage() {
        return Language;
    }
}
