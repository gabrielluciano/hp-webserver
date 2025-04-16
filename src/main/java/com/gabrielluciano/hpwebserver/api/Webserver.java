package com.gabrielluciano.hpwebserver.api;

/**
 * Represents an abstract web server that can be started and stopped.
 * This class provides a base for implementing specific web server
 * configurations and behaviors.
 */
public abstract class Webserver {

    /**
     * The configuration for the web server.
     */
    protected final WebserverConfiguration configuration;

    /**
     * Constructs a new Webserver with the specified configuration.
     *
     * @param configuration the configuration for the web server
     */
    protected Webserver(WebserverConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Starts the web server.
     */
    public abstract void start();

    /**
     * Stops the web server.
     */
    public abstract void stop();
}