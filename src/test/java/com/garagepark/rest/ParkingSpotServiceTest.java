package com.garagepark.rest;

import com.garagepark.bd.ParkingSpotRepository;
import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.ParkingSpot;
import com.garagepark.bd.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import junit.framework.Assert;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;


public class ParkingSpotServiceTest extends BaseJerseyTest {

	private static final String NUSP_TEST = "8545876";
    private static final String BASE_URL = "v1/parking";
	private static final String NAME_FIELD = "name";
	private static final String NUSP_FIELD = "nusp";
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	public static final String PERSON_NAME_TEST = "Jose Da Silva Barroso";
	public static final String GENDER_TEST = "masculino";
	public static final String NAME_TEST = "professor_1";
	public static final String NAME_TEST_2 = "professor_2";

	@Override
	public Application configure() {
		return new ResourceConfig().register(new PersonService());
	}

	@Test
	public void shouldInsertNewParkingSpot() {
        MultivaluedMap<String, String> postParams = new MultivaluedHashMap<String, String>();
        postParams.putSingle(NAME_FIELD, NAME_TEST);
		Response response = target(BASE_URL).request().post(Entity.form(postParams));

        Assert.assertEquals(CREATED.getStatusCode(), response.getStatus());
		Assert.assertNotNull(ParkingSpotRepository.instance().findByName(NAME_TEST));
	}

    @Test
	public void shouldRetrieveParkingSpot() {
		ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
		Assert.assertNull(parkingSpotRepository.findByName(NAME_TEST));
		parkingSpotRepository.insert(new ParkingSpot(NAME_TEST));
        Response response = target(BASE_URL + "/" + NAME_TEST).request().get();

		String responseText = response.readEntity(String.class);
        ParkingSpot parkingSpot = GSON.fromJson(responseText, ParkingSpot.class);
		
		Assert.assertEquals(OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(NAME_TEST, parkingSpot.getName());
	}

    @Test
	public void shouldRetrieveAllParkingSpots() {
		ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
		Assert.assertNull(parkingSpotRepository.findByName(NAME_TEST));
		Assert.assertNull(parkingSpotRepository.findByName(NAME_TEST_2));
		parkingSpotRepository.insert(new ParkingSpot(NAME_TEST));
		parkingSpotRepository.insert(new ParkingSpot(NAME_TEST_2));
        Response response = target(BASE_URL).request().get();

		String responseText = response.readEntity(String.class);
		Type listType = new TypeToken<ArrayList<ParkingSpot>>() {}.getType();
        List<ParkingSpot> parkingSpots = GSON.fromJson(responseText, listType);

		Assert.assertEquals(2, parkingSpots.size());
		Assert.assertEquals(OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(NAME_TEST, parkingSpots.get(0).getName());
		Assert.assertEquals(NAME_TEST_2, parkingSpots.get(1).getName());
	}

	@Test
	public void shouldMakeReservation() {
		ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
		PersonRepository personRepository = PersonRepository.instance();
		Assert.assertNull(parkingSpotRepository.findByName(NAME_TEST));
		Assert.assertNull(personRepository.findByNusp(NUSP_TEST));
		personRepository.insert(new Person(NUSP_TEST, PERSON_NAME_TEST, GENDER_TEST));
		parkingSpotRepository.insert(new ParkingSpot(NAME_TEST));

		MultivaluedMap<String, String> postParams = new MultivaluedHashMap<>();
		postParams.putSingle(NUSP_FIELD, NUSP_TEST);
		Response response = target(BASE_URL + "/" + NAME_TEST + "/reservation").request().post(Entity.form(postParams));

		Assert.assertEquals(CREATED.getStatusCode(), response.getStatus());

		ParkingSpot parkingSpot = parkingSpotRepository.findByName(NAME_TEST);
		Assert.assertNotNull(parkingSpot);
		Assert.assertNotNull(parkingSpot.getOwner());
	}

	@Test
	public void shouldClearReservation() {
		ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
		PersonRepository personRepository = PersonRepository.instance();
		Assert.assertNull(parkingSpotRepository.findByName(NAME_TEST));
		Assert.assertNull(personRepository.findByNusp(NUSP_TEST));
		Person person = new Person(NUSP_TEST, PERSON_NAME_TEST, GENDER_TEST);
		personRepository.insert(person);
		ParkingSpot parkingSpot = new ParkingSpot(NAME_TEST);
		parkingSpot.setOwner(person);
		parkingSpotRepository.insert(parkingSpot);

		Assert.assertNotNull(parkingSpotRepository.findByName(NAME_TEST).getOwner());

		MultivaluedMap<String, String> postParams = new MultivaluedHashMap<>();
		Response response = target(BASE_URL + "/" + NAME_TEST + "/clear").request().put(Entity.form(postParams));

		Assert.assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());

		ParkingSpot parkingSpotCleared = parkingSpotRepository.findByName(NAME_TEST);
		Assert.assertNotNull(parkingSpotCleared);
		Assert.assertNull(parkingSpotCleared.getOwner());
	}
}
