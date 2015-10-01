package com.garagepark.rest;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseJerseyTest extends JerseyTest {

    private EntityManagerFactory emFactory;

    private EntityManager em;

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:unit-testing-jpa", "sa", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            emFactory = Persistence.createEntityManagerFactory("garagepark");
            em = emFactory.createEntityManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        //clearDatabase();
        if (em != null) {
            em.close();
        }
        if (emFactory != null) {
            emFactory.close();
        }
    }

    public void clearDatabase() throws Exception {
        try {
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("TRUNCATE SCHEMA public AND COMMIT");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }


    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyTestContainer();
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.property(ClientProperties.FOLLOW_REDIRECTS, false);
        super.configureClient(config);
    }
}