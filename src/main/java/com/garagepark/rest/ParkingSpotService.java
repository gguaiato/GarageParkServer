package com.garagepark.rest;

import com.garagepark.bd.model.ParkingSpot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.ParkingController;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.Response.Status.*;


@Path("v1/parking")
public class ParkingSpotService {

    private static Logger logger = Logger.getLogger(ParkingSpotService.class);
    private ParkingController parkingController = ParkingController.instance();
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static final String ENCODING = "UTF-8";

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
    public Response newParkingSpot(@FormParam("name") String name) {
        logger.info(String.format("POST /parking params: name=%s", name));
        boolean included = parkingController.includeParkingSPot(name);
        if (included)
            return Utils.buildSimpleResponse(CREATED);
        return Utils.buildSimpleResponse(ACCEPTED);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
    public Response getParkingSpot(@PathParam("name") String name) {
        logger.info("GET /parking/name params: " + "name=" + name);
        ParkingSpot parkingSpot = parkingController.getParkingSpot(name);
        if (parkingSpot != null) {
            String json = gson.toJson(parkingSpot);
            return Utils.buildTextResponse(OK, json);
        }
        return Response.status(NOT_FOUND).entity("{ result: \"Parking Spot não encontrado.\" }").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
    public Response listParkingSpots(@QueryParam("limit") int limit){
        logger.info("GET /parking");
        List<ParkingSpot> parkingSpots = parkingController.listParkingSpots(limit);
        String json = gson.toJson(parkingSpots);
        return Utils.buildTextResponse(OK, json);
    }

    @POST
    @Path("{name}/reservation")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
    public Response doReservation(@PathParam("name") String name,
                                  @FormParam("nusp") String nusp) {
        boolean reservationMade = parkingController.makeReservation(name, nusp);
        if (reservationMade) {
            return Utils.buildSimpleResponse(CREATED);
        }
        return Utils.buildSimpleResponse(ACCEPTED);
    }


    @PUT
    @Path("{name}/clear")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
    public Response clearReservation(@PathParam("name") String name) {
        boolean reservationCleared = parkingController.clearReservation(name);
        if (reservationCleared) {
            return Utils.buildSimpleResponse(NO_CONTENT);
        }
        return Utils.buildSimpleResponse(NOT_FOUND);
    }

}
