package com.garagepark.rest;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.Person;
import controller.PersonController;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Path("v1/person")
public class PersonService {
	
	private static Logger logger = Logger.getLogger(PersonService.class);
	private PersonController personController = PersonController.instance();
	private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	private static final String ENCODING = "UTF-8";

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
	public Response newUser(@FormParam("nusp") String nusp,
								@FormParam("name") String name,
                                @FormParam("gender") String gender) {
		logger.info(String.format("POST /person params: nusp=%s name=%s gender=%s", nusp, name, gender));
		boolean included = personController.includeUser(nusp, name, gender);
        if (included)
            return Utils.buildSimpleResponse(CREATED);
		return Utils.buildSimpleResponse(ACCEPTED);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
	public Response setPersonValid(@FormParam("nusp") String nusp,
									  @FormParam("valid") Boolean valid) {
		logger.info(String.format("PUT /person params: nusp=%s valid=%b", nusp, valid));
		if (personController.updateValidPerson(nusp, valid)) {
			return Response.status(NO_CONTENT).build();
		}
		return Response.status(BAD_REQUEST).entity("{ result: \"Pessoa não pode ser atualizada.\" }").encoding("UTF-8").build();
	}

	@GET
    @Path("/{nusp}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
	public Response listUser(@PathParam("nusp") String nusp) {
		logger.info("GET /person params: " + "nusp=" + nusp);
		Person person = personController.getPerson(nusp);
		if (person != null) {
			String json = gson.toJson(person);
			return Utils.buildTextResponse(OK, json);
		}
		return Response.status(NOT_FOUND).entity("{ result: \"Pessoa não encontrada.\" }").build();

	}
}
