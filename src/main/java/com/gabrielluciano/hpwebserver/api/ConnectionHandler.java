package com.gabrielluciano.hpwebserver.api;

import java.net.Socket;

/**
 * Represents an abstract connection handler responsible for processing
 * client connections. This class implements the {@link Runnable} interface
 * to allow handling connections in a separate thread.
 */
public abstract class ConnectionHandler implements Runnable {

    /**
     * The socket associated with the client connection.
     */
    protected final Socket socket;

    /**
     * Constructs a new ConnectionHandler with the specified socket.
     *
     * @param socket the socket to be handled by this ConnectionHandler
     */
    protected ConnectionHandler(Socket socket) {
        this.socket = socket;
    }
}