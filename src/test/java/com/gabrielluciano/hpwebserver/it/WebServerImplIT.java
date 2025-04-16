package com.gabrielluciano.hpwebserver.it;

import com.gabrielluciano.hpwebserver.api.WebserverConfiguration;
import com.gabrielluciano.hpwebserver.config.DefaultWebserverConfiguration;
import com.gabrielluciano.hpwebserver.connection.VirtualThreadConnectionDispatcher;
import com.gabrielluciano.hpwebserver.server.WebServerImpl;
import com.gabrielluciano.hpwebserver.util.PingbackConnectionHandlerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebServerImplIT {

    public static final int MIN_PORT = 30000;
    public static final int MAX_PORT = 40000;

    private int port;
    private WebServerImpl webServer;

    @BeforeEach
    void setUp() {
        port = getRandomPort();
        WebserverConfiguration configuration = getWebserverConfiguration();

        webServer = new WebServerImpl(configuration);
        Executors.newSingleThreadExecutor().submit(webServer::start);
    }

    @AfterEach
    void tearDown() {
        webServer.stop();
    }

    @Test
    void testServerRespondsToClient() throws Exception {
        try (Socket clientSocket = new Socket("localhost", port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String message = "Hello, server!";
            out.println(message);

            String response = in.readLine();
            assertEquals(message, response);
        }
    }

    private WebserverConfiguration getWebserverConfiguration() {
        return DefaultWebserverConfiguration.builder()
                .setPort(port)
                .setTerminationTimeoutInSeconds(10)
                .setConnectionDispatcher(new VirtualThreadConnectionDispatcher())
                .setConnectionHandlerFactory(new PingbackConnectionHandlerFactory())
                .build();
    }

    private int getRandomPort() {
        Random random = new Random();
        return MIN_PORT + random.nextInt(MAX_PORT - MIN_PORT + 1);
    }
}