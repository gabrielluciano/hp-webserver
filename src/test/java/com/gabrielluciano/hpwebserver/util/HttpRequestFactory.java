package com.gabrielluciano.hpwebserver.util;

public class HttpRequestFactory {
    public static String simpleGet(String path, String host) {
        return """
                GET %s HTTP/1.1\r
                Host: %s\r
                \r
                """.formatted(path, host);
    }

    public static String getWithQuery(String path, String query, String host) {
        return """
                GET %s?%s HTTP/1.1\r
                Host: %s\r
                \r
                """.formatted(path, query, host);
    }

    public static String getWithHeaders(String path, String host, String userAgent, String accept) {
        return """
                GET %s HTTP/1.1\r
                Host: %s\r
                User-Agent: %s\r
                Accept: %s\r
                \r
                """.formatted(path, host, userAgent, accept);
    }

    public static String getWithAuth(String path, String host, String token) {
        return """
                GET %s HTTP/1.1\r
                Host: %s\r
                Authorization: Bearer %s\r
                \r
                """.formatted(path, host, token);
    }

    public static String getWithFoldedHeader(String path, String host) {
        return """
                GET %s HTTP/1.1\r
                Host: %s\r
                X-Custom-Header: part1\r
                 part2\r
                \r
                """.formatted(path, host);
    }

    public static String browserLikeGet(String path, String host) {
        return """
                GET %s HTTP/1.1\r
                Host: %s\r
                User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r
                Accept-Language: en-US,en;q=0.5\r
                Accept-Encoding: gzip, deflate\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                \r
                """.formatted(path, host);
    }
}
