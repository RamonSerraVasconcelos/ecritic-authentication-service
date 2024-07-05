package com.ecritic.ecritic_authentication_service.config.properties;

public class ThreadRequestProperties {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }
}
