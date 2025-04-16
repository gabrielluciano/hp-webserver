package com.gabrielluciano.hpwebserver.api;

import java.net.Socket;

/**
 * A factory interface for creating instances of {@link ConnectionHandler}.
 * This allows for the creation of connection handlers with specific
 * configurations or behaviors.
 */
public interface ConnectionHandlerFactory {

    /**
     * Creates a new instance of a ConnectionHandler.
     *
     * @param socket the socket to be handled by the ConnectionHandler
     * @return a new instance of a ConnectionHandler
     */
    ConnectionHandler createConnectionHandler(Socket socket);
}
