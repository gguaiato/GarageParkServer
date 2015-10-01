package com.garagepark.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

public class Utils {

	private static final Gson gson = new Gson();

	public static Response buildSimpleResponse(Status status) {
		return Response.status(status.getStatusCode()).build();
	}

	public static Response buildTextResponse(Status status,
			String responseText) {
		return Response.status(status.getStatusCode())
				.entity(responseText).build();
	}

}
