package com.garagepark.rest;

import com.garagepark.bd.ParkingSpotRepository;
import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.ParkingSpot;
import com.garagepark.bd.model.Person;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class BaseJerseyTest extends JerseyTest {

    private EntityManagerFactory emFactory;

    private EntityManager em;

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
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
        clearDatabase();
        if (em != null) {
            em.close();
        }
        if (emFactory != null) {
            emFactory.close();
        }
    }

    public void clearDatabase() throws Exception {
        cleanParkingSpotRepository();
        cleanPersonRepository();
    }

    private void cleanPersonRepository() {
        PersonRepository personRepository = PersonRepository.instance();
        List<Person> persons = personRepository.list(Integer.MAX_VALUE);
        for (Person person : persons) {
            personRepository.delete(person.getNusp());
        }
    }

    private void cleanParkingSpotRepository() {
        ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
        List<ParkingSpot> parkingSpots = parkingSpotRepository.list(Integer.MAX_VALUE);
        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRepository.delete(parkingSpot.getName());
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