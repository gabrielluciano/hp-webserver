package com.gabrielluciano.hpwebserver.server;

import com.gabrielluciano.hpwebserver.api.ConnectionDispatcher;
import com.gabrielluciano.hpwebserver.api.ConnectionHandlerFactory;
import com.gabrielluciano.hpwebserver.api.Webserver;
import com.gabrielluciano.hpwebserver.api.WebserverConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class WebServerImpl extends Webserver {
    private static final Logger log = LoggerFactory.getLogger(WebServerImpl.class);

    private final ConnectionDispatcher connectionDispatcher;
    private final ConnectionHandlerFactory handlerFactory;

    private ServerSocket serverSocket;
    private volatile boolean running;

    public WebServerImpl(WebserverConfiguration configuration) {
        super(configuration);
        this.connectionDispatcher = configuration.getConnectionDispatcher();
        this.handlerFactory = configuration.getConnectionHandlerFactory();
        this.running = false;
    }

    @Override
    public void start() {
        this.running = true;
        try {
            log.info("Starting server...");
            serverSocket = new ServerSocket(configuration.getPort());
            log.info("Server successfully started on port {}", configuration.getPort());
            while (running) {
                Socket clientSocket = serverSocket.accept();
                log.info("New connection at {}", LocalDateTime.now());
                connectionDispatcher.dispatch(handlerFactory.createConnectionHandler(clientSocket));
            }
        } catch (IOException ex) {
            if (running) {
                log.error("Unexpected IO error in server loop: {}", ex.getMessage(), ex);
            } else {
                log.info("Server shut down gracefully.");
            }
        }
    }

    @Override
    public void stop() {
        log.info("Gracefully shutting down server");
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            connectionDispatcher.shutdown(configuration.getTerminationTimeoutSeconds(), TimeUnit.SECONDS);
        } catch (IOException ex) {
            log.error("Error closing server: {}", ex.getMessage(), ex);
        } catch (InterruptedException ex) {
            log.error("Thread was interrupted: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }
}
