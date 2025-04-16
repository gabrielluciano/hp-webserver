package com.gabrielluciano.hpwebserver.util;

import com.gabrielluciano.hpwebserver.api.ConnectionHandler;
import com.gabrielluciano.hpwebserver.api.ConnectionHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PingbackConnectionHandlerFactory implements ConnectionHandlerFactory {
    private static final Logger log = LoggerFactory.getLogger(PingbackConnectionHandlerFactory.class);

    @Override
    public ConnectionHandler createConnectionHandler(Socket socket) {
        return new PingbackConnectionHandler(socket);
    }

    public static class PingbackConnectionHandler extends ConnectionHandler {
        public PingbackConnectionHandler(Socket socket) {
            super(socket);
        }

        @Override
        public void run() {
            try (
                    Socket socket = super.socket;
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                String message = in.readLine();
                out.println(message);
            } catch (IOException ex) {
                log.error("Error in connection handler: ", ex);
                throw new RuntimeException(ex);
            }
        }
    }
}
