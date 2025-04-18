package com.gabrielluciano.hpwebserver.parser;

import com.gabrielluciano.hpwebserver.model.HttpRequest;

public class HttpParser {
    private static final String CRLF = "\r\n";
    private static final String SPACE = " ";
    private static final String QUESTION_MARK = "?";
    private static final String QUESTION_MARK_REGEX = "\\?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private static final String HEADER_SEPARATOR = ": ";
    private static final String HOST_HEADER = "Host: ";
    private static final int HOST_START_INDEX = HOST_HEADER.length();

    private static final int REQUEST_LINE_INDEX = 0;
    private static final int REQUEST_METHOD_INDEX = 0;
    private static final int REQUEST_URI_INDEX = 1;
    private static final int REQUEST_VERSION_INDEX = 2;
    private static final int HOST_LINE_INDEX = 1;
    private static final int KEY_VALUE_PAIR_LENGTH = 2;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int URI_PATH_INDEX = 0;
    private static final int URI_QUERY_INDEX = 1;
    private static final int HEADERS_LINE_INDEX = 2;

    public HttpRequest parse(String request) {
        HttpRequest httpRequest = new HttpRequest();
        String[] lines = request.split(CRLF);

        parseRequestLine(httpRequest, lines[REQUEST_LINE_INDEX]);
        parseHostLine(httpRequest, lines[HOST_LINE_INDEX]);
        parseHeaders(httpRequest, lines);

        return httpRequest;
    }

    private void parseRequestLine(HttpRequest httpRequest, String requestLine) {
        String[] requestLineParts = requestLine.split(SPACE);
        String requestMethod = requestLineParts[REQUEST_METHOD_INDEX];

        String requestUri = requestLineParts[REQUEST_URI_INDEX];
        parseRequestUri(httpRequest, requestUri);

        String requestVersion = requestLineParts[REQUEST_VERSION_INDEX];

        httpRequest.setMethod(requestMethod);
        httpRequest.setVersion(requestVersion);
    }

    private void parseHostLine(HttpRequest httpRequest, String hostLine) {
        if (hostLine.startsWith(HOST_HEADER)) {
            String host = hostLine.substring(HOST_START_INDEX);
            httpRequest.setHost(host);
        }
    }

    private void parseHeaders(HttpRequest httpRequest, String[] lines) {
        for (int i = HEADERS_LINE_INDEX; i < lines.length; i++) {
            String headerLine = lines[i];
            if (headerLine.isEmpty()) {
                break;
            }
            String[] headerParts = headerLine.split(HEADER_SEPARATOR);
            if (headerParts.length == KEY_VALUE_PAIR_LENGTH) {
                String headerName = headerParts[KEY_INDEX];
                String headerValue = headerParts[VALUE_INDEX];
                httpRequest.setHeader(headerName, headerValue);
            }
        }
    }

    private void parseRequestUri(HttpRequest httpRequest, String requestUri) {
        if (!uriContainsQuery(requestUri)) {
            httpRequest.setPath(requestUri);
            return;
        }

        String[] uriParts = requestUri.split(QUESTION_MARK_REGEX);
        String requestPath = uriParts[URI_PATH_INDEX];
        String queryString = uriParts[URI_QUERY_INDEX];
        String[] queryParams = queryString.split(AMPERSAND);

        for (String param : queryParams) {
            String[] keyValue = param.split(EQUALS);
            if (keyValue.length == KEY_VALUE_PAIR_LENGTH) {
                String key = keyValue[KEY_INDEX];
                String value = keyValue[VALUE_INDEX];
                httpRequest.setQueryParam(key, value);
            }
        }

        httpRequest.setPath(requestPath);
    }

    private boolean uriContainsQuery(String requestUri) {
        return requestUri.contains(QUESTION_MARK);
    }
}
