package com.gabrielluciano.hpwebserver.parser.http;

public class HttpParseException extends RuntimeException {
    public static final String MISSING_REQUEST_LINE = "Request line is missing";
    public static final String INVALID_METHOD = "Invalid HTTP method";
    public static final String MISSING_HOST_HEADER = "Host header is missing";
    public static final String INVALID_HTTP_VERSION = "Only HTTP/1.1 is supported";
    public static final String INVALID_HEADER_FORMAT = "Invalid header format";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred while parsing the request";

    public HttpParseException(String message) {
        super(message);
    }

    public HttpParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpParseException(Throwable cause) {
        super(UNEXPECTED_ERROR, cause);
    }
}
