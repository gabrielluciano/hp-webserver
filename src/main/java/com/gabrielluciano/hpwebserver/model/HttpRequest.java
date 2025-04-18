package com.gabrielluciano.hpwebserver.model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String host;
    private String version;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private String body;

    public HttpRequest() {
        this.queryParams = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParam(String name, String value) {
        queryParams.put(name, value);
    }

    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}