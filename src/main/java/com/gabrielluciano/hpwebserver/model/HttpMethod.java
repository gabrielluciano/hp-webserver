package com.gabrielluciano.hpwebserver.model;

import com.gabrielluciano.hpwebserver.parser.http.HttpParseException;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static HttpMethod from(String method) {
        try {
            return valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new HttpParseException(HttpParseException.INVALID_METHOD);
        }
    }
}