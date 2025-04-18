package com.gabrielluciano.hpwebserver.parser;

import com.gabrielluciano.hpwebserver.model.HttpRequest;
import com.gabrielluciano.hpwebserver.util.HttpRequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpParserTest {

    private HttpParser parser;

    @BeforeEach
    void setUp() {
        parser = new HttpParser();
    }

    @Test
    void testParseSimpleGet() {
        String request = HttpRequestFactory.simpleGet("/", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getPath());
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
    }

    @Test
    void testParseGetWithQuery() {
        String request = HttpRequestFactory.getWithQuery("/search", "value=test", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/search", parsedRequest.getPath());
        assertEquals("test", parsedRequest.getQueryParam("value"));
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
    }

    @Test
    void testParseGetWithHeaders() {
        String request = HttpRequestFactory.getWithHeaders("/", "example.com", "Mozilla/5.0", "text/html");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getPath());
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
        assertEquals("Mozilla/5.0", parsedRequest.getHeader("User-Agent"));
        assertEquals("text/html", parsedRequest.getHeader("Accept"));
    }
}
