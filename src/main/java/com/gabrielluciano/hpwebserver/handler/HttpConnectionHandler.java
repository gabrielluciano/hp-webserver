package com.gabrielluciano.hpwebserver.handler;

import com.gabrielluciano.hpwebserver.api.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpConnectionHandler extends ConnectionHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpConnectionHandler.class);

    public HttpConnectionHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (Socket socket = super.socket;
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String message;
            while ((message = in.readLine()) != null) {
                out.println("Ping back: " + message);
            }
        } catch (IOException ex) {
            log.error("Error in connection: {}", ex.getMessage(), ex);
        }
    }
}
