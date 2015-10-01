package com.garagepark.rest;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.Person;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Path("v1/person")
public class PersonService {
	
	private static Logger logger = Logger.getLogger(PersonService.class);
	private static PersonRepository personRepository = PersonRepository.instance();
	private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response includeUser(@FormParam("nusp") String nusp,
								@FormParam("name") String name,
                                @FormParam("gender") String gender) {
		logger.info(String.format("POST /person params: nusp=%s name=%s gender=%s", nusp, name, gender));
		Person person = new Person(nusp, name, gender);
        boolean included = personRepository.insert(person);
        if (included)
            return Utils.buildSimpleResponse(CREATED);
		return Utils.buildSimpleResponse(ACCEPTED);
	}
	
	@GET
    @Path("/{nusp}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listUsers(@PathParam("nusp") String nusp) {
		logger.info("GET /person params: " + "nusp=" + nusp);
		Person person = personRepository.findByNusp(nusp);
		if (person != null) {
			String json = gson.toJson(person);
			return Utils.buildTextResponse(OK, json);
		} else {
			return Response.status(404).entity("Pessoa não encontrada.").build();
		}
	}
}
