package com.gabrielluciano.hpwebserver.connection;

import com.gabrielluciano.hpwebserver.api.ConnectionDispatcher;
import com.gabrielluciano.hpwebserver.api.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class VirtualThreadConnectionDispatcher implements ConnectionDispatcher {
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadConnectionDispatcher.class);

    private final ExecutorService executor;

    public VirtualThreadConnectionDispatcher() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void dispatch(ConnectionHandler handler) {
        try {
            executor.submit(handler);
        } catch (RejectedExecutionException e) {
            log.warn("Connection handler was rejected — executor is shutting down or shut down", e);
        }
    }

    @Override
    public void shutdown(int timeout, TimeUnit unit) throws InterruptedException {
        log.info("Shutting down all connection handlers");
        executor.shutdown();
        var successfullyTerminated = executor.awaitTermination(timeout, unit);
        if (successfullyTerminated) {
            log.info("All connection handlers ended successfully");
        } else {
            log.warn("Some connection handlers were interrupted");
        }
    }
}
