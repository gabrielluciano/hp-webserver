package com.gabrielluciano.hpwebserver;

import com.gabrielluciano.hpwebserver.config.DefaultWebserverConfiguration;
import com.gabrielluciano.hpwebserver.connection.ConnectionHandlerFactoryImpl;
import com.gabrielluciano.hpwebserver.connection.VirtualThreadConnectionDispatcher;
import com.gabrielluciano.hpwebserver.api.Webserver;
import com.gabrielluciano.hpwebserver.api.WebserverConfiguration;
import com.gabrielluciano.hpwebserver.server.WebServerImpl;

public class Application {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "GMT");

        WebserverConfiguration configuration = DefaultWebserverConfiguration.builder()
                .setPort(8080)
                .setTerminationTimeoutInSeconds(10)
                .setConnectionDispatcher(new VirtualThreadConnectionDispatcher())
                .setConnectionHandlerFactory(new ConnectionHandlerFactoryImpl())
                .build();

        Webserver webserver = new WebServerImpl(configuration);
        Runtime.getRuntime().addShutdownHook(new Thread(webserver::stop));
        webserver.start();
    }
}
