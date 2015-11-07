package com.garagepark.rest;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import javax.persistence.EntityManager;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.Assert;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Test;


public class PersonServiceTest extends BaseJerseyTest {

	private static final String NUSP_TEST = "8574986";
    private static final String BASE_URL = "v1/person";
	private static final String NUSP_FIELD = "nusp";
	private static final String NAME_FIELD = "name";
	private static final String VALID_FIELD = "valid";
	private static final String GENDER_FIELD = "gender";
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	public static final String NAME_TEST = "Jose Da Silva Barroso";
	public static final String GENDER_TEST = "masculino";

	@Override
	public Application configure() {
		return new ResourceConfig().register(new PersonService());
	}

	@Test
	public void shouldInsertNewUser() {
        MultivaluedMap<String, String> postParams = new MultivaluedHashMap<String, String>();
        postParams.putSingle(NUSP_FIELD, NUSP_TEST);
        postParams.putSingle(NAME_FIELD, NAME_TEST);
        postParams.putSingle(GENDER_FIELD, GENDER_TEST);
		Response response = target(BASE_URL).request().post(Entity.form(postParams));

        Assert.assertEquals(CREATED.getStatusCode(), response.getStatus());
		Assert.assertNotNull(PersonRepository.instance().findByNusp(NUSP_TEST));
	}


    @Test
	public void shouldRetrieveUser() {
		Assert.assertNull(PersonRepository.instance().findByNusp(NUSP_TEST));
		PersonRepository.instance().insert(new Person(NUSP_TEST, NAME_TEST, GENDER_TEST));
        Response response = target(BASE_URL + "/" + NUSP_TEST).request().get();

		String responseText = response.readEntity(String.class);
        Person person = GSON.fromJson(responseText, Person.class);
		
		Assert.assertEquals(OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(NUSP_TEST, person.getNusp());
		Assert.assertEquals(NAME_TEST, person.getName());
		Assert.assertEquals(GENDER_TEST, person.getGender());
		Assert.assertTrue(true == person.getValid());
	}

	@Test
	public void shouldUpdateUser() {
		Assert.assertNull(PersonRepository.instance().findByNusp(NUSP_TEST));
		PersonRepository.instance().insert(new Person(NUSP_TEST, NAME_TEST, GENDER_TEST));

		MultivaluedMap<String, String> putParams = new MultivaluedHashMap<String, String>();
		putParams.putSingle(NUSP_FIELD, NUSP_TEST);
		putParams.putSingle(VALID_FIELD, "false");
		Response response = target(BASE_URL).request().put(Entity.form(putParams));

		Assert.assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
	}
}
