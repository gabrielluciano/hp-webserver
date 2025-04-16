package com.gabrielluciano.hpwebserver.config;

import com.gabrielluciano.hpwebserver.api.ConnectionDispatcher;
import com.gabrielluciano.hpwebserver.api.ConnectionHandlerFactory;
import com.gabrielluciano.hpwebserver.api.WebserverConfiguration;

public class DefaultWebserverConfiguration implements WebserverConfiguration {
    private final int port;
    private final int terminationTimeoutInSeconds;
    private final ConnectionDispatcher connectionDispatcher;
    private final ConnectionHandlerFactory connectionHandlerFactory;

    public DefaultWebserverConfiguration(int port,
                                         int terminationTimeoutInSeconds,
                                         ConnectionDispatcher connectionDispatcher,
                                         ConnectionHandlerFactory connectionHandlerFactory) {
        this.port = port;
        this.terminationTimeoutInSeconds = terminationTimeoutInSeconds;
        this.connectionDispatcher = connectionDispatcher;
        this.connectionHandlerFactory = connectionHandlerFactory;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getTerminationTimeoutSeconds() {
        return terminationTimeoutInSeconds;
    }

    @Override
    public ConnectionDispatcher getConnectionDispatcher() {
        return connectionDispatcher;
    }

    @Override
    public ConnectionHandlerFactory getConnectionHandlerFactory() {
        return connectionHandlerFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer port;
        private Integer terminationTimeoutInSeconds;
        private ConnectionDispatcher connectionDispatcher;
        private ConnectionHandlerFactory connectionHandlerFactory;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setTerminationTimeoutInSeconds(int terminationTimeoutInSeconds) {
            this.terminationTimeoutInSeconds = terminationTimeoutInSeconds;
            return this;
        }

        public Builder setConnectionDispatcher(ConnectionDispatcher connectionDispatcher) {
            this.connectionDispatcher = connectionDispatcher;
            return this;
        }

        public Builder setConnectionHandlerFactory(ConnectionHandlerFactory connectionHandlerFactory) {
            this.connectionHandlerFactory = connectionHandlerFactory;
            return this;
        }

        public DefaultWebserverConfiguration build() {
            if (port == null || terminationTimeoutInSeconds == null || connectionDispatcher == null || connectionHandlerFactory == null) {
                throw new IllegalStateException("Configuration is not complete. Please set all required fields.");
            }
            return new DefaultWebserverConfiguration(port, terminationTimeoutInSeconds, connectionDispatcher, connectionHandlerFactory);
        }
    }
}
