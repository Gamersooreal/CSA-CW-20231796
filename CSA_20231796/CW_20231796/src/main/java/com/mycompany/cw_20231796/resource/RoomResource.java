package com.mycompany.cw_20231796.resource;

import com.mycompany.cw_20231796.dao.MockDatabase;
import com.mycompany.cw_20231796.exception.DataNotFoundException;
import com.mycompany.cw_20231796.exception.RoomNotEmptyException;
import com.mycompany.cw_20231796.model.Room;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return MockDatabase.ROOMS;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room, @Context UriInfo uriInfo) {
        MockDatabase.ROOMS.add(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        throw new DataNotFoundException("Room with ID " + roomId + " not found.");
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room matchedRoom = null;

        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(roomId)) {
                matchedRoom = room;
                if (!room.getSensorIds().isEmpty()) {
                    throw new RoomNotEmptyException("Room " + roomId + " still has sensors assigned.");
                }
                break;
            }
        }

        // If room found (and no sensors), delete it
        if (matchedRoom != null) {
            MockDatabase.ROOMS.removeIf(room -> room.getId().equals(roomId));
        }

        // Return 204 whether room exists or not (idempotent delete)
        return Response.noContent().build();
    }
}
