package com.nstu.geolocationwificlient.data;

import androidx.annotation.NonNull;

public class Wifi {
    private final String title;
    private final String security;
    private int level;

    public Wifi() {
        this("WiFi", "Strong", 0);
    }
    public Wifi(String title, String security, int level) {
        this.title = title;
        this.security = security;
        this.level = level;
    }

    private void updateLevel(int level){
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getSecurity() {
        return security;
    }

    public int getLevel() {
        return level;
    }

    @NonNull
    @Override
    public String toString() {
        return "{title: " + this.title + ";"
                + "security: " + this.security + ";"
                + "level: " + this.level + "}";
    }
}
