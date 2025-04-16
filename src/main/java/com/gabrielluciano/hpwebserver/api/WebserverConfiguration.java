package com.gabrielluciano.hpwebserver.api;

/**
 * Represents the configuration for a web server.
 * This interface provides methods to retrieve essential
 * configuration details such as the server port, termination
 * timeout, and components required for handling connections.
 */
public interface WebserverConfiguration {

    /**
     * Retrieves the port on which the web server will listen.
     *
     * @return the port number
     */
    int getPort();

    /**
     * Retrieves the timeout in seconds for terminating the web server.
     *
     * @return the termination timeout in seconds
     */
    int getTerminationTimeoutSeconds();

    /**
     * Retrieves the connection dispatcher responsible for managing
     * and dispatching connection handlers.
     *
     * @return the connection dispatcher
     */
    ConnectionDispatcher getConnectionDispatcher();

    /**
     * Retrieves the factory for creating connection handlers.
     *
     * @return the connection handler factory
     */
    ConnectionHandlerFactory getConnectionHandlerFactory();
}