package com.garagepark.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;

public class GrizzlyTestContainer implements TestContainerFactory {
    @Override
    public TestContainer create (final URI uri, DeploymentContext deploymentContext) {
        return new TestContainer() {
            private HttpServer server;

            @Override
            public ClientConfig getClientConfig () {
                return null;
            }

            @Override
            public URI getBaseUri () {
                return uri;
            }

            @Override
            public void start () {
                try {
                    this.server = GrizzlyWebContainerFactory.create(
                            uri, Collections.singletonMap("jersey.config.server.provider.packages",
                                    "com.garagepark.rest")
                    );
                    this.server.start();
                } catch (ProcessingException e) {
                    throw new TestContainerException(e);
                } catch (IOException e) {
                    throw new TestContainerException(e);
                }
            }

            @Override
            public void stop () {
                this.server.shutdownNow();
            }
        };
    }
}