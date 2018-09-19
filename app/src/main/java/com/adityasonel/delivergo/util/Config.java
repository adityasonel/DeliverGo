package com.adityasonel.delivergo.util;

public class Config {

    public static String getMockApiUrl(String offset, String limit) {
        return "https://mock-api-mobile.dev.lalamove.com/deliveries?" + "offset=" + offset + "&limit=" + limit;
    }

    public static String getLocalApiUrl(String offset, String limit) {
        return "http://192.168.43.189:8080/deliveries?" + "offset=" + offset + "&limit=" + limit;
    }
}
