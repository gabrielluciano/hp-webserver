package com.gabrielluciano.hpwebserver.parser.http;

import com.gabrielluciano.hpwebserver.model.HttpMethod;
import com.gabrielluciano.hpwebserver.model.HttpRequest;

import java.util.Map;

public class HttpParser {
    private static final String CRLF = "\r\n";
    private static final String SPACE = " ";
    private static final String QUESTION_MARK = "?";
    private static final String QUESTION_MARK_REGEX = "\\?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private static final String HEADER_SEPARATOR = ": ";
    private static final String HOST_HEADER = "Host: ";
    private static final String HTTP_VERSION_1_1 = "HTTP/1.1";

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
    private static final int MINIMUM_REQUEST_LINES = 2;
    private static final int REQUEST_LINE_PARTS = 3;

    public HttpRequest parse(String request) {
        try {
            HttpRequest httpRequest = new HttpRequest();
            String[] lines = request.split(CRLF);

            if (lines.length < MINIMUM_REQUEST_LINES || lines[REQUEST_LINE_INDEX].isEmpty()) {
                throw new HttpParseException(HttpParseException.MISSING_REQUEST_LINE);
            }

            parseRequestLine(httpRequest, lines[REQUEST_LINE_INDEX]);
            parseHostLine(httpRequest, lines[HOST_LINE_INDEX]);
            parseHeaders(httpRequest, lines);

            return httpRequest;
        } catch (HttpParseException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpParseException(e);
        }
    }

    private void parseRequestLine(HttpRequest httpRequest, String requestLine) {
        String[] requestLineParts = requestLine.split(SPACE);

        if (requestLineParts.length != REQUEST_LINE_PARTS) {
            throw new HttpParseException(HttpParseException.MISSING_REQUEST_LINE);
        }

        HttpMethod requestMethod = HttpMethod.from(requestLineParts[REQUEST_METHOD_INDEX]);

        String requestUri = requestLineParts[REQUEST_URI_INDEX];
        parseRequestUri(httpRequest, requestUri);

        String requestVersion = requestLineParts[REQUEST_VERSION_INDEX];
        validateHttpVersion(requestVersion);

        httpRequest.setMethod(requestMethod);
        httpRequest.setVersion(requestVersion);
    }

    private void parseHostLine(HttpRequest httpRequest, String hostLine) {
        if (!hostLine.startsWith(HOST_HEADER)) {
            throw new HttpParseException(HttpParseException.MISSING_HOST_HEADER);
        }
        String host = hostLine.substring(HOST_START_INDEX);
        if (host.isEmpty()) {
            throw new HttpParseException(HttpParseException.MISSING_HOST_HEADER);
        }
        httpRequest.setHost(host);
    }

    private void parseHeaders(HttpRequest httpRequest, String[] lines) {
        for (int i = HEADERS_LINE_INDEX; i < lines.length; i++) {
            String headerLine = lines[i];
            if (headerLine.isEmpty()) {
                break;
            }
            String[] headerParts = headerLine.split(HEADER_SEPARATOR);
            if (headerParts.length != KEY_VALUE_PAIR_LENGTH || headerParts[KEY_INDEX].isEmpty()) {
                throw new HttpParseException(HttpParseException.INVALID_HEADER_FORMAT);

            }
            String headerName = headerParts[KEY_INDEX];
            String headerValue = headerParts[VALUE_INDEX];
            httpRequest.setHeader(headerName, headerValue);
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
        parseQueryParams(httpRequest, queryParams);

        httpRequest.setPath(requestPath);
    }

    private void validateHttpVersion(String httpVersion) {
        if (!HTTP_VERSION_1_1.equals(httpVersion)) {
            throw new HttpParseException(HttpParseException.INVALID_HTTP_VERSION);
        }
    }

    private void parseQueryParams(HttpRequest httpRequest, String[] queryParams) {
        for (String param : queryParams) {
            Map.Entry<String, String> queryParam = parseQueryParam(param);
            if (queryParam != null) {
                httpRequest.setQueryParam(queryParam.getKey(), queryParam.getValue());
            }
        }
    }

    private Map.Entry<String, String> parseQueryParam(String param) {
        String[] keyValue = param.split(EQUALS);
        if (keyValue.length == 0 || keyValue[KEY_INDEX].isEmpty())
            return null;

        if (keyValue.length == KEY_VALUE_PAIR_LENGTH) {
            String key = keyValue[KEY_INDEX];
            String value = keyValue[VALUE_INDEX];
            return Map.entry(key, value);
        }
        return Map.entry(keyValue[KEY_INDEX], "");
    }

    private boolean uriContainsQuery(String requestUri) {
        return requestUri.contains(QUESTION_MARK);
    }
}
