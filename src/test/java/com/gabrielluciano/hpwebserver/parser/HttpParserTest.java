package com.gabrielluciano.hpwebserver.parser;

import com.gabrielluciano.hpwebserver.model.HttpMethod;
import com.gabrielluciano.hpwebserver.model.HttpRequest;
import com.gabrielluciano.hpwebserver.parser.http.HttpParseException;
import com.gabrielluciano.hpwebserver.parser.http.HttpParser;
import com.gabrielluciano.hpwebserver.util.HttpRequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals(HttpMethod.GET, parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getPath());
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
    }

    @Test
    void testParseGetWithQuery() {
        String request = HttpRequestFactory.getWithQuery("/search", "value=test", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals(HttpMethod.GET, parsedRequest.getMethod());
        assertEquals("/search", parsedRequest.getPath());
        assertEquals("test", parsedRequest.getQueryParam("value"));
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
    }

    @Test
    void testParseGetWithMultipleQueryParams() {
        String request = HttpRequestFactory.getWithQuery("/search", "name=john&age=25&city=", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("/search", parsedRequest.getPath());
        assertEquals("john", parsedRequest.getQueryParam("name"));
        assertEquals("25", parsedRequest.getQueryParam("age"));
        assertEquals("", parsedRequest.getQueryParam("city"));
    }

    @Test
    void testParseGetWithHeaders() {
        String request = HttpRequestFactory.getWithHeaders("/", "example.com", "Mozilla/5.0", "text/html");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals(HttpMethod.GET, parsedRequest.getMethod());
        assertEquals("/", parsedRequest.getPath());
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
        assertEquals("Mozilla/5.0", parsedRequest.getHeader("User-Agent"));
        assertEquals("text/html", parsedRequest.getHeader("Accept"));
    }

    @Test
    void testParseBrowserLikeRequest() {
        String request = HttpRequestFactory.browserLikeGet("/", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
                parsedRequest.getHeader("User-Agent"));
        assertEquals("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                parsedRequest.getHeader("Accept"));
        assertEquals("keep-alive", parsedRequest.getHeader("Connection"));
    }

    @Test
    void testParseGetWithAuth() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String request = HttpRequestFactory.getWithAuth("/secure", "example.com", token);
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals(HttpMethod.GET, parsedRequest.getMethod());
        assertEquals("/secure", parsedRequest.getPath());
        assertEquals("HTTP/1.1", parsedRequest.getVersion());
        assertEquals("example.com", parsedRequest.getHost());
        assertEquals("Bearer " + token, parsedRequest.getHeader("Authorization"));
    }

    @Test
    void testParseGetWithMalformedQuery() {
        String request = HttpRequestFactory.getWithQuery("/search", "&key=&=value&=", "example.com");
        HttpRequest parsedRequest = parser.parse(request);

        assertEquals("/search", parsedRequest.getPath());
        assertEquals("", parsedRequest.getQueryParam("key"));
        assertEquals(1, parsedRequest.getQueryParams().size());
    }

    @Test
    void testParseMissingRequestLine() {
        String request = HttpRequestFactory.missingRequestLine();
        HttpParseException exception = assertThrows(HttpParseException.class, () -> parser.parse(request));

        assertEquals(HttpParseException.MISSING_REQUEST_LINE, exception.getMessage());
    }

    @Test
    void testParseInvalidRequestMethod() {
        String request = HttpRequestFactory.invalidRequestMethod();
        HttpParseException exception = assertThrows(HttpParseException.class, () -> parser.parse(request));

        assertEquals(HttpParseException.INVALID_METHOD, exception.getMessage());
    }

    @Test
    void testParseMissingHostHeader() {
        String request = HttpRequestFactory.missingHostHeader();
        HttpParseException exception = assertThrows(HttpParseException.class, () -> parser.parse(request));

        assertEquals(HttpParseException.MISSING_HOST_HEADER, exception.getMessage());
    }

    @Test
    void testParseInvalidHttpVersion() {
        String request = HttpRequestFactory.invalidHttpVersion();
        HttpParseException exception = assertThrows(HttpParseException.class, () -> parser.parse(request));

        assertEquals(HttpParseException.INVALID_HTTP_VERSION, exception.getMessage());
    }

    @Test
    void testParseInvalidHeaderFormat() {
        String request = HttpRequestFactory.invalidHeaderFormat();
        HttpParseException exception = assertThrows(HttpParseException.class, () -> parser.parse(request));

        assertEquals(HttpParseException.INVALID_HEADER_FORMAT, exception.getMessage());
    }
}
