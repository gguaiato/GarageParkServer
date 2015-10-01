package com.garagepark.container;

import org.glassfish.jersey.server.ResourceConfig;

public class GarageParkContainer extends ResourceConfig {

    public GarageParkContainer() {
        packages("com.garagepark.rest");
    }

}