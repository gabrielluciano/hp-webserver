package com.gabrielluciano.hpwebserver.api;

import java.util.concurrent.TimeUnit;

/**
 * Represents a dispatcher responsible for managing and dispatching
 * connection handlers to handle incoming connections.
 */
public interface ConnectionDispatcher {

    void dispatch(ConnectionHandler handler);

    void shutdown(int timeout, TimeUnit unit) throws InterruptedException;
}
