package com.gabrielluciano.hpwebserver.connection;

import com.gabrielluciano.hpwebserver.api.ConnectionHandlerFactory;
import com.gabrielluciano.hpwebserver.api.ConnectionHandler;
import com.gabrielluciano.hpwebserver.handler.HttpConnectionHandler;

import java.net.Socket;

public class ConnectionHandlerFactoryImpl implements ConnectionHandlerFactory {

    @Override
    public ConnectionHandler createConnectionHandler(Socket socket) {
        return new HttpConnectionHandler(socket);
    }
}
